package vigorBackup.controller;

import vigorBackup.model.LoadConfigFile;

public class Main {
	
	public static void main(String[] args){
		LoadConfigFile.loadConfigFile(args);
	}
	
	
//	/**
//	 * List of the downloaders for the routers
//	 */
//	private static List<DefaultRouterWebDownloader> routersDownloaders = new ArrayList<>();
//
//	/**
//	 * Main class.
//	 * 
//	 * @param args
//	 *            The first argument must be the config file path. Other args
//	 *            are ignored.
//	 */
//	public static void main(String[] args) {
//		loadConfigFile(args);
//		cleanOldBackups();
//		backupFiles();
//		EmailBackupReport.sendBackupReport(routersDownloaders);
//	}
//
//	private static void backupFiles() {
//		routersDownloaders = new ArrayList<>();
//		LoadFromCSV importcsv = new LoadFromCSV();
//		List<Router> routerList = importcsv.loadCsv();
//		// Java 8 lambdas.
//		routerList.forEach(router -> routersDownloaders.add(ERouterModels
//				.returnDownloader(router)));
//		// Uses a executor service to lauch all the threads
//		ExecutorService execServ = Executors.newCachedThreadPool();
//		routersDownloaders.forEach(routersDown -> {
//			execServ.execute(routersDown);
//		});
//
//		execServ.shutdown();
//		try {
//			System.out.println("Waiting 10 minutes for all threads to finish");
//			if (execServ.awaitTermination(10, TimeUnit.MINUTES)) {
//				System.out.println("All threads finished sucessfully");
//			} else {
//				System.out.println("Some threads were killed by timeout");
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	private static void cleanOldBackups() {
//		try {
//			File dir = new File(ROOT_DIRECTORY);
//			if (dir.exists()) {
//				File[] directories = dir.listFiles(File::isDirectory);
//				for (File file : directories) {
//					File[] backups = file.listFiles(File::isFile);
//					for (File backup : backups) {
//						DateTime start = new DateTime(DateTime.now());
//						DateTime end = new DateTime(backup.lastModified());
//						Days days = Days.daysBetween(end, start);
//						if (days.getDays() > DAYS_TO_KEEP_FILES) {
//							backup.delete();
//						}
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			// TODO: Treat exceptions
//			e.printStackTrace();
//		}
//
//	}

}
