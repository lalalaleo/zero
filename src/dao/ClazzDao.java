package dao;

import java.util.Iterator;

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
				throw new Exception("不存在");
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
				throw new Exception("不存在");
			session.update(clazz);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Iterator<Clazz> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Clazz";  
	        Query query = session.createQuery(hql);  
	        Iterator<Clazz> clazzList = query.iterate();
			session.getTransaction().commit();
			return clazzList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
