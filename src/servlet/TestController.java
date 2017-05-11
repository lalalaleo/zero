package servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.*;
import dao.*;

@Controller
public class TestController {
	
	@RequestMapping(value = "addchoice", method = RequestMethod.POST)
	public void AddChoice(Choice choice, HttpServletRequest request, HttpServletResponse response) {
		ChoiceDao dao = new ChoiceDao();
		try {
			dao.creat(choice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "addclazz", method = RequestMethod.POST)
	public void AddClazz(Clazz clazz, HttpServletRequest request, HttpServletResponse response) {
		ClazzDao dao = new ClazzDao();
		try {
			dao.creat(clazz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "addinformation", method = RequestMethod.POST)
	public void AddInformation(Information information, HttpServletRequest request, HttpServletResponse response) {
		InformationDao dao = new InformationDao();
		try {
			dao.creat(information);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "addpeople", method = RequestMethod.POST)
	public void AddPeople(People people, HttpServletRequest request, HttpServletResponse response) {
		PeopleDao dao = new PeopleDao();
		try {
			dao.creat(people);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "addsubject", method = RequestMethod.POST)
	public void AddSubject(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		SubjectDao dao = new SubjectDao();
		try {
			dao.creat(subject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
