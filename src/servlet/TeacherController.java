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
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import model.*;
import wxmethod.WxService;
import dao.*;

@Controller
public class TeacherController {
	@RequestMapping(value = "findteacher", method = RequestMethod.POST)
	public void FindTeacher (People people, HttpServletRequest request, HttpServletResponse response){
		PeopleDao peoDao = new PeopleDao();
		SubjectDao subDao = new SubjectDao();
		PrintWriter printWriter = null;
		
		try {
			People peopleFind = peoDao.findByOpenId(people);
			List<Subject> subjectList = subDao.listByTeaId(peopleFind.getUseId());
			List<String> subNameList = new ArrayList<String>();
			if(peopleFind != null && subjectList != null){
				for(int i = 0; i < subjectList.size(); i++){
					subNameList.add(subjectList.get(i).getSubName());
				}
				Map<String, Object> mapResult = new HashMap<String, Object>();
				mapResult.put("openid", peopleFind.getOpenId());
				mapResult.put("name", peopleFind.getUseName());
				mapResult.put("info", "未设置");
				mapResult.put("classes", subNameList);
				mapResult.put("mobile", "未设置");
				mapResult.put("email", "未设置");
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
	
	@RequestMapping(value = "sendfindclazz", method = RequestMethod.POST)
	public void SendFindClazz (People people, HttpServletRequest request, HttpServletResponse response){
		ClazzDao claDao = new ClazzDao();
		PrintWriter printWriter = null;
		
		try {
			List clazzList = claDao.listByTea(people.getOpenId());
			if(clazzList != null){
				List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
				for(int i = 0; i < clazzList.size(); i++){
					Object[] objects = (Object[]) clazzList.get(i);
					Clazz clazz = (Clazz) objects[0];
					Subject subject = (Subject) objects[1];
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", "" + subject.getSubName() + clazz.getClaTab());
					map.put("classid", clazz.getClaId());
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
	
	@RequestMapping(value = "sendfindstudent", method = RequestMethod.POST)
	public void SendFindStudent (Clazz clazz, HttpServletRequest request, HttpServletResponse response){
		PeopleDao peoDao = new PeopleDao();
		PrintWriter printWriter = null;
		
		try {
			List<People> peopleList = peoDao.listByCla(clazz.getClaId());
			if(peopleList != null){
				List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
				for(int i = 0; i < peopleList.size(); i++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", peopleList.get(i).getUseName());
					map.put("number", peopleList.get(i).getUseNum());
					map.put("studentid", peopleList.get(i).getOpenId());
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
	
	@RequestMapping(value = "sendmessage", method = RequestMethod.POST)
	public void SendMessage (People people, @RequestParam(value="addresseeList")List<String> addresseeList, @RequestParam(value="message")String message, @RequestParam(value="classid")String claId, 
			HttpServletRequest request, HttpServletResponse response){
		InformationDao infDao = new InformationDao();
		SubjectDao subDao = new SubjectDao();
		PeopleDao peoDao = new PeopleDao();
		WxService wx = new WxService();
		PrintWriter printWriter = null;
		
		try {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			if(people != null){
				Subject subjectFind = subDao.findByClaId(claId);
				People peopleFind = peoDao.findByOpenId(people);
				for(int i = 0; i < addresseeList.size() - 1; i++){
					Information inf = new Information();
					inf.setInf(message);
					inf.setFromId(people.getOpenId());
					inf.setToUseId(addresseeList.get(i));
					inf.setToClaId(claId);
					infDao.creat(inf);
					wx.sendTemplateInfo(addresseeList.get(i), "", "", peopleFind.getUseName(), subjectFind.getSubName(), message);
				}
				mapResult.put("result", "Yes");
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
	
	@RequestMapping(value = "history", method = RequestMethod.POST)
	public void History (People people, @RequestParam(value="classid")String claId, HttpServletRequest request, HttpServletResponse response){
		InformationDao infDao = new InformationDao();
		PrintWriter printWriter = null;
		
		try {
			List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
			List infoList;
			if(people != null){
				if(claId.equals("all")){
					infoList = infDao.listByOpenId(people.getOpenId());	
				}
				else{
					infoList = infDao.listByOpenIdClaId(people.getOpenId(), claId);	
				}
				for(int i = 0; i < infoList.size(); i++){
					Object[] objects = (Object[]) infoList.get(i);
					Information infFind = (Information) objects[0];
					Subject subFind = (Subject) objects[1];
					People peoFind = (People) objects[2];
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("msgid", infFind.getInfId());
					map.put("class", subFind.getSubName());
					map.put("sender", peoFind.getUseName());
					map.put("content", infFind.getInf());
					map.put("time", infFind.getInfTime().toString());
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
