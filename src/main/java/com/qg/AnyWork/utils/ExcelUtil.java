package com.qg.AnyWork.utils;

import com.qg.AnyWork.exception.question.ExcelReadException;
import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.model.Testpaper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.Count;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.formula.ptg.EqualPtg;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.omg.CORBA.StringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作Excel表格的工具类
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
public class ExcelUtil {

    public static final ExcelUtil util = new ExcelUtil();

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 读取解析Excel文件流
     * @param input
     * @return
     * @throws Exception
     */
    public static List<Question> getQuestionList(InputStream input) throws Exception {
        //选择题
        Map<Integer, String> map1 = new HashMap<>();
        map1.put(1, "content");
        map1.put(2, "A");
        map1.put(3, "B");
        map1.put(4, "C");
        map1.put(5, "D");
        map1.put(6, "key");
        map1.put(7, "socre");
        //判断题、问答题、编程题、综合题
        Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "content");
        map2.put(2, "key");
        map2.put(3, "socre");
        //填空题、
        Map<Integer, String> map3 = new HashMap<>();
        map3.put(1, "content");
        map3.put(2, "key");
        map3.put(3, "socre");
        map3.put(4, "other");

        return new ExcelUtil().readQuest(input, Question.class, map1, map2, map3, map2, map2, map2);
    }

    /**
     * 读取试卷题目
     * @param input
     * @param clazz
     * @param map1
     * @param map2
     * @param map3
     * @param map4
     * @param map5
     * @param map6
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> List<Question> readQuest(InputStream input, Class<T> clazz,
                                            Map<Integer, String> map1,                      //选择题
                                            Map<Integer, String> map2,                      //判断题
                                            Map<Integer, String> map3,                      //填空题
                                            Map<Integer, String> map4,                      //问答题
                                            Map<Integer, String> map5,                      //编程题
                                            Map<Integer, String> map6) throws Exception {   //综合题

        List<Question> questionList = new ArrayList<>();

        try {
            Workbook wb = WorkbookFactory.create(input);
            Sheet sheet = wb.getSheetAt(0);
            Field field = null;
            Class tempClazz = Question.class;

            //从第二行开始读取
            for (int i=2; i<sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                if (null == row){
                    //空行不处理
                    continue;
                }

                Map<Integer, String> map = null;
                //获取每一行的第一个单元格
                Cell cell = row.getCell(0);
                if (null == cell){
                    //空行不处理
                    continue;
                }

                //存储类型
                String status = cell.getStringCellValue();
                if (status.startsWith("A")){
                    //解析选择题
                    map = map1;
                } else if (status.startsWith("B")){
                    //判断题
                    map = map2;
                } else if (status.startsWith("C")){
                    //填空题
                    map = map3;
                } else if (status.startsWith("D")){
                    //问答题
                    map = map4;
                } else if (status.startsWith("E")){
                    //编程题
                    map = map5;
                } else if (status.startsWith("F")){
                    //综合题
                    map = map6;
                } else {
                    //未知信息
                    continue;
                }

                T t = (T) tempClazz.newInstance();
                T t1 = (T) tempClazz.newInstance();

                //从第二个单元格开始读取
                for (int j=1; j<row.getLastCellNum(); j++){
                    //获取存储类型
                    try {
                        field = tempClazz.getDeclaredField(map.get(j));
                    } catch (NullPointerException e){
                        break;
                    }

                    field.setAccessible(true);
                    //获得单元格对象
                    Cell realCell = row.getCell(j);
                    if (null == realCell || "".equals(realCell.toString()))
                        continue;
                    t = addingT(field, t1, realCell);
                }

                //强制转换类型
                Question question = (Question) t;
                if (null == question.getContent()){
                    continue;
                }

                if (status.startsWith("A")){
                    question.setType(1);
                } else if (status.startsWith("B")){
                    question.setType(2);
                } else if (status.startsWith("C")){
                    question.setType(3);
                    question.setKey(question.getKey().replaceAll("#", "∏"));    //替換答案中非法字符
                } else if (status.startsWith("D")){
                    question.setType(4);
                } else if (status.startsWith("E")){
                    question.setType(5);
                } else if (status.startsWith("F")){
                    question.setType(6);
                }

                questionList.add(question);
            }
        } finally {
            if (input != null){
                try{
                    input.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return questionList;
    }

    private <T> T addingT(Field field, T t, Cell cell) throws IllegalArgumentException, IllegalAccessException {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:     //字符串
                field.set(t, cell.getStringCellValue());
                break;

            case Cell.CELL_TYPE_BOOLEAN:    //Boolean对象
                field.set(t, cell.getBooleanCellValue());
                break;

            case Cell.CELL_TYPE_NUMERIC:

                //是否是日期格式
                if (DateUtil.isCellDateFormatted(cell)){
                    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    field.set(t, cell.getDateCellValue());
                } else {
                    //读取为数字
                    Integer longVal = (int) Math.round(cell.getNumericCellValue());
                    double doubleVal = Math.round(cell.getNumericCellValue());

                    if(Double.parseDouble(longVal + ".0") == doubleVal){
                        String type = field.getType().toString() ;
                        if (type.equals("class java.lang.String")){
                            field.set(t, longVal+"") ;
                        } else {
                            field.set(t, longVal);
                        }
                    } else {
                        field.set(t, doubleVal);
                    }
                }
                break;

            case Cell.CELL_TYPE_FORMULA:    //公式
                field.set(t, cell.getCellFormula());
                break;

            case HSSFCell.CELL_TYPE_BLANK:  //空值
                break;

            case HSSFCell.CELL_TYPE_ERROR:  //故障
                break;
        }

        return t;
    }

    /**
     * 输出Excel
     */


    /**
     *
     * @param title 标题
     * @param dataSet 数据列表
     * @param out 输出流
     * @param pattern 时间格式参数
     */
    public static  <T> void  export(String title, List<T> dataSet, OutputStream out, String pattern) {

        if(dataSet == null || dataSet.isEmpty()) {
            //空数据
            return;
        }
        //声明一个表格
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        //表格头
        HSSFRow row = sheet.createRow(0);
        ListIterator<T> it = dataSet.listIterator();
        //将游标定位到列表结尾
        while (it.hasNext()){
            it.next();
        }

        int index = 0;

        //确定六种题型的数字
        int A_num = 1, B_num =1, C_num = 1, D_num =1, E_num = 1, F_num =1;

        while (it.hasPrevious()){
            index++;
            T t = it.previous();

            /**
             * 插入标题
             */
            if (A_num == 1){
                index++;
                row = sheet.createRow(index);
                String[] headers = {"选择题", "题目内容", "选项A", "选项B", "选项C", "选项D", "正确答案", "分数"};
                util.addCell(headers, row, style);
                index++;
            } else if(B_num == 1){
                index++;
                row = sheet.createRow(index);
                String[] headers = {"判断题", "题目内容", "正确答案",  "分数"};
                util.addCell(headers, row, style);
                index++;
            } else if(C_num == 1){
                index++;
                row = sheet.createRow(index);
                String[] headers = {"填空题", "题目内容", "正确答案",  "分数", "个数"};
                util.addCell(headers, row, style);
                index++;
            } else if(D_num == 1){
                index++;
                row = sheet.createRow(index);
                String[] headers = {"问答题", "题目内容", "正确答案",  "分数"};
                util.addCell(headers, row, style);
                index++;
            } else if(E_num == 1){
                index++;
                row = sheet.createRow(index);
                String[] headers = {"编程题", "题目内容", "正确答案",  "分数"};
                util.addCell(headers, row, style);
                index++;
            } else if(F_num == 1){
                index++;
                row = sheet.createRow(index);
                String[] headers = {"综合题", "题目内容", "正确答案",  "分数"};
                util.addCell(headers, row, style);
                index++;
            }

            //跳到新的一行
            row = sheet.createRow(index);

            //利用反射
            Field[] fields = t.getClass().getDeclaredFields();
            int j = 0;
            for (int i=1; i<fields.length; i++){
                HSSFCell cell = row.createCell(j);
                Field field = fields[i];
                String fieldName = field.getName();
                if (fieldName.equals("testpaperId")){
                    continue;
                }
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Class clazz = t.getClass();
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    Object textValue = null;
                    //对属性进行判断
                    if (value == null){
                        continue;
                    }
                    j++;

                    //将类型转化为带数字的字母类型
                    if(fieldName.equals("type")){
                        switch (String.valueOf((int)value)) {
                            case "1":
                                value = "A" + (A_num++);
                                break;
                            case "2":
                                value = "B" + (B_num++);
                                break;
                            case "3":
                                value = "C" + (C_num++);
                                break;
                            case "4":
                                value = "D" + (D_num++);
                                break;
                            case "5":
                                value = "E" + (E_num++);
                                break;
                            case "6":
                                value = "F" + (F_num++);
                                break;
                        }
                    }

                    if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        textValue = 1;
                        if (!bValue) {
                            textValue = 2;
                        }
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof byte[]) {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                1023, 255, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
                        patriarch.createPicture(anchor, workbook.addPicture(
                                bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value;
                    }

                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = null;
                        if (textValue instanceof String){
                            matcher = p.matcher((String)textValue);
                        } else {
                            matcher = p.matcher(String.valueOf(textValue));
                        }
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble((String) textValue));
                        } else {
                            if (textValue instanceof String) {
                                HSSFRichTextString richString = new HSSFRichTextString(
                                        (String) textValue);
                                HSSFFont font3 = workbook.createFont();
                                font3.setColor(HSSFColor.BLACK.index);
                                richString.applyFont(font3);
                                cell.setCellValue(richString);
                            } else {
                                cell.setCellValue(Double.valueOf(textValue.toString()));
                            }
                        }
                    }

                } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new ExcelReadException("Excel导出失败！" + e.getMessage());
                }
            }
        }

        try {
            workbook.write(out);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输出每种题目的标题
     * @param headers
     * @param row
     * @param style
     */
    private void addCell(String[] headers, HSSFRow row, HSSFCellStyle style) {
        for(int i=0; i<headers.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
    }


}
