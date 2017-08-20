package com.qg.AnyWork;

import com.qg.AnyWork.dao.TestDao;
import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.utils.ExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnyWorkApplicationTests {

	@Autowired
	private TestDao testDao;

	@Test
	public void contextLoads() {

//		int userid = 12;
//		List<Question> list = testDao.getQuestionByTestpaperId(8);
//		try {
//			OutputStream outputStream = new FileOutputStream(userid + "export2003_a.xls");
//			ExcelUtil.export(""+userid, list, outputStream, "yyyy-MM-dd HH:mm:ss");
//		} catch (Exception e){
//			e.printStackTrace();
//		}
	}

}
