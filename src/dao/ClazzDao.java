package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import hibernate.HibernateUtil;
import model.*;

public class ClazzDao {
	public Boolean creat(Clazz clazz) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(clazz);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean delete(Clazz clazz) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Clazz clazzFind = (Clazz) session.get(Clazz.class, clazz.getClaId());
			if(clazzFind == null) 
				throw new Exception("没有数据");
			session.delete(clazz);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean modify(Clazz clazz) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Clazz clazzFind = (Clazz) session.get(Clazz.class, clazz.getClaId());
			if(clazzFind == null) 
				throw new Exception("没有数据");
			session.update(clazz);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public List<Clazz> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Clazz";  
	        Query query = session.createQuery(hql);  
	        List<Clazz> clazzList = query.list();
			session.getTransaction().commit();
			return clazzList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Clazz find (Clazz clazz) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Clazz clazzFind = (Clazz) session.get(Clazz.class, clazz.getClaId());
			session.getTransaction().commit();
			return clazzFind;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public List clazzTable (String openId) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT clazz.*, subject.* FROM clazz, subject, people "
					+ "WHERE clazz.subId = subject.subId AND subject.subTeaId = people.useId AND ( clazz.claId IN "
					+ "(SELECT choice.claId FROM choice, people WHERE choice.useId = people.useId AND people.openId = ? ) "
					+ "OR people.openId = ? ) ";  
	        Query query = session.createSQLQuery(sql).addEntity("clazz", Clazz.class).addEntity("subject", Subject.class);
	        query.setString(0, openId);
	        query.setString(1, openId);
	        List clazzList = query.list();
			session.getTransaction().commit();
			return clazzList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public List listByTea (String openId) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT clazz.*, subject.* FROM clazz, subject, people "
					+ "WHERE clazz.subId = subject.subId AND subject.subTeaId = people.useId AND people.openId = ? ";  
	        Query query = session.createSQLQuery(sql).addEntity("clazz", Clazz.class).addEntity("subject", Subject.class);
	        query.setString(0, openId);
	        List clazzList = query.list();
			session.getTransaction().commit();
			return clazzList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public List<Clazz> listBySubName (String openId, String subName) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT clazz.* FROM clazz, subject, people a, choice, people b "
					+ "WHERE clazz.subId = subject.subId AND subject.subTeaId = a.useId AND choice.claId = clazz.claId AND choice.useId = b.useId "
					+ "AND subject.subName = ? AND ( a.openId = ? OR b.openId = ? ) ";  
	        Query query = session.createSQLQuery(sql).addEntity("clazz", Clazz.class);
	        query.setString(0, subName);
	        query.setString(1, openId);
	        query.setString(2, openId);
	        List<Clazz> clazzList = query.list();
			session.getTransaction().commit();
			return clazzList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
