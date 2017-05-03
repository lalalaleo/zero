package servlet;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import model.*;
import dao.*;

@Controller
public class TeacherController {
	@RequestMapping(value = "findteacher", method = RequestMethod.POST)
	public void FindTeacher (People people, HttpServletRequest request, HttpServletResponse response){
		PeopleDao dao = new PeopleDao();
		PrintWriter printWriter = null;
		
		try {
			People findpeople = dao.find(people);
			if(findpeople != null){
				Map<String, Object> mapResult = new HashMap<String, Object>();
				mapResult.put("openid", findpeople.getOpenId());
				mapResult.put("name", findpeople.getUseName());
				mapResult.put("info", "123");
				mapResult.put("classes", "123");
				mapResult.put("mobile", "123");
				mapResult.put("email", "123");
				String jsonResult = JSON.toJSONString(mapResult);
				printWriter = response.getWriter();
				printWriter.print(jsonResult);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}
}
