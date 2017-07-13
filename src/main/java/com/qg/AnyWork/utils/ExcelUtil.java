package com.qg.AnyWork.utils;

import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.model.Testpaper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作Excel表格的工具类
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
public class ExcelUtil {

    public static final ExcelUtil util = new ExcelUtil();

    // TODO: 2017/7/12 不用添加关于试卷简介的内容
//    public static Textpaper getTextpaper(InputStream input) throws Exception {
//        Map<Integer, String> map = new HashMap<>();
//
//        return util.readText(input, map);
//    }

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
        map3.put(3, "other");
        map3.put(4, "socre");

        return new ExcelUtil().readQuest(input, Question.class, map1, map2, map3, map2, map2, map2);
    }

    private <T> Testpaper readText(InputStream input, Map<Integer, String> map) throws Exception {
        return null;
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
}
