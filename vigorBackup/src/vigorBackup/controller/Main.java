package vigorBackup.controller;



import java.net.MalformedURLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import vigorBackup.model.Router;

public class Main {

	public static void main(String[] args) throws MalformedURLException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("vigorBackupDB");
		EntityManager em = factory.createEntityManager();
		
//		Router router2 = new Router();
//		router2.setDescription("teste");
//		router2.setOk(false);
//		
//		Address addr = new Address();
//		addr.setAddress(new URL("http://intranet"));
//		addr.setRouter(router2);
//		em.getTransaction().begin();
//		em.persist(router2);
//		em.persist(addr);
//		em.getTransaction().commit();
		List<Router> routerList = f
		
		
		em.close();
		factory.close();

	}

}
