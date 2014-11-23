package vigorBackup.controller;



import java.net.MalformedURLException;

import vigorBackup.model.Vigor2925;

public class Main {

	public static void main(String[] args) throws MalformedURLException {
//		EntityManagerFactory factory = Persistence.createEntityManagerFactory("vigorBackupDB");
//		EntityManager em = factory.createEntityManager();
		
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
		//List<Router> routerList = f;
		
		
//		em.close();
//		factory.close();
		Vigor2925 router = new Vigor2925("admin", "***REMOVED***");
		System.out.println(router.getEncodedUser());
		System.out.println(router.getEncodedPassword());
		
		
	}

}
