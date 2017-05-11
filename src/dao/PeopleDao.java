package dao;

import java.util.List;

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
				throw new Exception("没有数据");
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
				throw new Exception("没有数据");
			session.update(people);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public List<People> list() throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from People";  
			Query query = session.createQuery(hql);  
			List<People> peopleList = query.list();
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

	public People findByOpenId(People people) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from People where openId = ? ";
			Query query = session.createQuery(hql); 
			query.setString(0, people.getOpenId());
			List<People> peopleList = query.list();
			session.getTransaction().commit();
			People peopleFind = peopleList.get(0);
			return peopleFind;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public List<People> listHaveTea(String openId) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT people.* FROM people, subject, clazz "
					+ "WHERE people.useId = subject.subTeaId AND subject.subId = clazz.subId AND clazz.claId IN "
					+ "(SELECT choice.claId FROM choice,people WHERE choice.useId = people.useId AND people.openId = ? ) ";  
			Query query = session.createSQLQuery(sql).addEntity(People.class); 
			query.setString(0, openId);
			List<People> peopleList = query.list();
			session.getTransaction().commit();
			return peopleList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public People isUser(People people) throws Exception{
		try {
			People peopleFind = null;
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT people.* FROM people "
					+ "WHERE people.useName = ? AND people.useSchool = ? AND people.useNum = ? AND people.openId = '' "; 
			Query query = session.createSQLQuery(sql).addEntity(People.class); 
			query.setString(0, people.getUseName());
			query.setString(1, people.getUseSchool());
			query.setString(2, people.getUseNum());
			List<People> peopleList = query.list();
			if(peopleList.size() == 1){
				peopleFind = peopleList.get(0);
				peopleFind.setOpenId(people.getOpenId());
				session.update(peopleFind);
			}
			session.getTransaction().commit();
			return peopleFind;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
	
	public List<People> listByCla(String claId) throws Exception{
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String sql = "SELECT DISTINCT people.* FROM people, choice "
					+ "WHERE people.useId = choice.useId AND choice.claId = ? AND people.openId != '' ";
			Query query = session.createSQLQuery(sql).addEntity(People.class); 
			query.setString(0, claId);
			List<People> peopleList = query.list();
			session.getTransaction().commit();
			return peopleList;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
