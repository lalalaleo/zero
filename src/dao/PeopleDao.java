package dao;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import hibernate.HibernateUtil;
import model.*;

public class PeopleDao {
	public Boolean creat(People people) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(people);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean delete(People people) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			People peopleFind = (People) session.get(People.class, people.getUseId());
			if(peopleFind == null) 
				throw new Exception("不存在");
			session.delete(people);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean modify(People people) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			People peopleFind = (People) session.get(People.class, people.getUseId());
			if(peopleFind == null) 
				throw new Exception("不存在");
			session.update(people);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Iterator<People> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from People";  
	        Query query = session.createQuery(hql);  
	        Iterator<People> peopleList = query.iterate();
			session.getTransaction().commit();
			return peopleList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public People find(People people) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			People peopleFind = (People) session.get(People.class, people.getUseId());  
			session.getTransaction().commit();
			return peopleFind;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
