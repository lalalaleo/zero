package dao;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import hibernate.HibernateUtil;
import model.*;

public class InformationDao {
	public Boolean creat(Information info) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(info);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean delete(Information info) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Information infoFind = (Information) session.get(Information.class, info.getInfId());
			if(infoFind == null) 
				throw new Exception("不存在");
			session.delete(info);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public Boolean modify(Information info) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Information infoFind = (Information) session.get(Information.class, info.getInfId());
			if(infoFind == null) 
				throw new Exception("不存在");
			session.update(info);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public Iterator<Information> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Information";  
	        Query query = session.createQuery(hql);  
	        Iterator<Information> infoList = query.iterate();
			session.getTransaction().commit();
			return infoList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
