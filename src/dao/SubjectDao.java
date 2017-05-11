package dao;

import java.util.List;

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
				throw new Exception("没有数据");
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
				throw new Exception("没有数据");
			session.update(subject);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public List<Subject> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Subject";  
			Query query = session.createQuery(hql);  
			List<Subject> subjectList = query.list();
			session.getTransaction().commit();
			return subjectList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Subject find(Subject subject) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Subject subjectFind = (Subject) session.get(Subject.class, subject.getSubId());
			session.getTransaction().commit();
			return subjectFind;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public List<Subject> listByTeaId(String subTeaId) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Subject where subTeaId = ? ";  
			Query query = session.createQuery(hql);  
			query.setString(0, subTeaId);
			List<Subject> subjectList = query.list();
			session.getTransaction().commit();
			return subjectList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Subject findByClaId(String claId) throws Exception{
		try {
			Subject subjectFind = null;
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT subject.* FROM subject, clazz "
					+ "WHERE subject.subId = clazz.subId AND clazz.claId = ? ";
			Query query = session.createSQLQuery(sql).addEntity(Subject.class); 
			query.setString(0, claId);
			List<Subject> subjectList = query.list();
			session.getTransaction().commit();
			if(subjectList.size() == 1){
				subjectFind = subjectList.get(0);
			}
			return subjectFind;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
