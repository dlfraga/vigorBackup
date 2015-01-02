package vigorBackup.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import vigorBackup.factory.RouterDownloaderFactory;
import vigorBackup.model.Address;
import vigorBackup.model.IRouterDownloader;
import vigorBackup.model.Router;
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
//		Vigor2925 router = new Vigor2925("admin", "***REMOVED***");
//		System.out.println(router.getEncodedUser());
//		System.out.println(router.getEncodedPassword());
		Router router = new Router();
		Router router2 = new Router();
		router.setPassword("***REMOVED***");
		router.setUsername("admin");
		
		router2.setPassword("***REMOVED***");
		router2.setUsername("admin");
		
		Address address = new Address();
		Address address2 = new Address();
		Address address3 = new Address();
		
		Address address4 = new Address();
		address4.setAddress(new URL("http://mail.bioensaios.com.br:8181"));
		ArrayList<Address> addList2 = new ArrayList<>();
		addList2.add(address4);
		router2.setConnectionAddresses(addList2);
		
		
		address.setAddress(new URL("http://agronomica.no-ip.info:8180"));
		address2.setAddress(new URL("http://agronomica.no-ip.info:8181"));
		address3.setAddress(new URL("http://agronomica.no-ip.info:8182"));
		ArrayList<Address> addList = new ArrayList<>();
		addList.add(address);
		addList.add(address2);
		addList.add(address3);
		router.setConnectionAddresses(addList);
		RouterDownloaderFactory routerFactory = new RouterDownloaderFactory();
		IRouterDownloader routerDownloader1 = routerFactory.getDownloader(routerFactory.VIGOR_2925, router);
		IRouterDownloader routerDownloader2 = routerFactory.getDownloader(routerFactory.VIGOR_2910, router2);
		
		System.out.println(routerDownloader1.downloadBackup());
		System.out.println(routerDownloader2.downloadBackup());
		
		
	}

}
