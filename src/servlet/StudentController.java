package servlet;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class StudentController {
	@RequestMapping(value = "listhavetea", method = RequestMethod.POST)
	public void ListHaveTea (People people, HttpServletRequest request, HttpServletResponse response){
		PeopleDao peoDao = new PeopleDao();
		PrintWriter printWriter = null;
		
		try {
			List<People> peopleList = peoDao.listHaveTea(people.getOpenId());
			if(peopleList != null){
				List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
				for(int i = 0; i < peopleList.size(); i++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("openid", peopleList.get(i).getOpenId());
					map.put("name", peopleList.get(i).getUseName());
					listResult.add(map);
				}
				String jsonResult = JSON.toJSONString(listResult);
				response.setCharacterEncoding("UTF-8");
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
	
	@RequestMapping(value = "findclazz", method = RequestMethod.POST)
	public void FindClazz (Clazz clazz, HttpServletRequest request, HttpServletResponse response){
		ClazzDao claDao = new ClazzDao();
		PeopleDao peoDao = new PeopleDao();
		SubjectDao subDao = new SubjectDao();
		PrintWriter printWriter = null;
		
		try {
			Clazz clazzFind = claDao.find(clazz);
			Subject subject = new Subject();
			subject.setSubId(clazzFind.getSubId());
			Subject subjectFind = subDao.find(subject);
			People people = new People();
			people.setUseId(subjectFind.getSubTeaId());
			People peopleFind = peoDao.find(people);
			if(clazzFind != null && subjectFind != null && peopleFind != null){
				Map<String, Object> mapResult = new HashMap<String, Object>();
				mapResult.put("class_id", clazzFind.getClaId());
				mapResult.put("class_name", subjectFind.getSubName());
				mapResult.put("class_info", subjectFind.getSubInfo());
				mapResult.put("teacher_id", peopleFind.getOpenId());
				mapResult.put("teacher_name", peopleFind.getUseName());
				mapResult.put("count", "未设置");
				mapResult.put("time", clazzFind.getClaTime());
				String jsonResult = JSON.toJSONString(mapResult);
				response.setCharacterEncoding("UTF-8");
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
	
	@RequestMapping(value = "clazztable", method = RequestMethod.POST)
	public void ClazzTable (People people, HttpServletRequest request, HttpServletResponse response){
		ClazzDao claDao = new ClazzDao();
		PrintWriter printWriter = null;
		
		try {
			List clazzTable = claDao.clazzTable(people.getOpenId());
			if(clazzTable != null){
				List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
				for(int i = 0; i < clazzTable.size(); i++){
					Object[] objects = (Object[]) clazzTable.get(i);
					Clazz clazz = (Clazz) objects[0];
					Subject subject = (Subject) objects[1];
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", subject.getSubName());
					map.put("time", clazz.getClaTime());
					listResult.add(map);
				}
				String jsonResult = JSON.toJSONString(listResult);
				response.setCharacterEncoding("UTF-8");
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
