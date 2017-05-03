package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {

//		SessionFactory sessionFactory = new  Configuration().configure().buildSessionFactory();

		Configuration configuration = new Configuration();
    	configuration.configure("hibernate.cfg.xml");
    	System.out.println("Hibernate Configuration loaded");
    	
    	//apply configuration property settings to StandardServiceRegistryBuilder
    	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    	System.out.println("Hibernate serviceRegistry created");
    	
    	SessionFactory sessionFactory = configuration
    						.buildSessionFactory(serviceRegistry);
        return sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
