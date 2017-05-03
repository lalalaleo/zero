package dao;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import hibernate.HibernateUtil;
import model.*;

public class SubjectDao {
	public Boolean creat(Subject subject) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(subject);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean delete(Subject subject) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Subject subjectFind = (Subject) session.get(Subject.class, subject.getSubId());
			if(subjectFind == null) 
				throw new Exception("不存在");
			session.delete(subject);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean modify(Subject subject) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Subject subjectFind = (Subject) session.get(Subject.class, subject.getSubId());
			if(subjectFind == null) 
				throw new Exception("不存在");
			session.update(subject);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Iterator<Subject> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Subject";  
	        Query query = session.createQuery(hql);  
	        Iterator<Subject> subjectList = query.iterate();
			session.getTransaction().commit();
			return subjectList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
