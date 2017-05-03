package dao;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import hibernate.HibernateUtil;
import model.*;

public class ChoiceDao {
	public Boolean creat(Choice choice) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(choice);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean delete(Choice choice) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Choice choiceFind = (Choice) session.get(Choice.class, choice.getChoId());
			if(choiceFind == null) 
				throw new Exception("不存在");
			session.delete(choice);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean modify(Choice choice) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Choice choiceFind = (Choice) session.get(Choice.class, choice.getChoId());
			if(choiceFind == null) 
				throw new Exception("不存在");
			session.update(choice);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Iterator<Choice> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Choice";  
	        Query query = session.createQuery(hql);  
	        Iterator<Choice> choiceList = query.iterate();
			session.getTransaction().commit();
			return choiceList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
