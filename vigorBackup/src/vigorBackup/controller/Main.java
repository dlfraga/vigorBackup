package vigorBackup.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import vigorBackup.model.DefaultRouterWebDownloader;
import vigorBackup.model.ERouterModels;
import vigorBackup.model.LoadConfigFile;
import vigorBackup.model.LoadFromCSV;
import vigorBackup.model.Router;
import vigorBackup.model.WebDavClient;
/**
 * Main class. It starts the backup routines.
 */
public class Main {
	/**
	 * List of the downloaders for the routers
	 */
	private static List<DefaultRouterWebDownloader> routersDownloaders = new ArrayList<>();
	

	/**
	 * Main class.
	 * 
	 * @param args
	 *            The first argument must be the config file path. Other args
	 *            are ignored.
	 */
	public static void main(String[] args) {
		LoadConfigFile.loadConfigFile(args);
		cleanOldBackups();
		backupFiles();
		WebDavClient.saveFilesToWebDav(routersDownloaders);
//		EmailBackupReport.sendBackupReport(routersDownloaders);
	}

	/**
	 * TODO: Document this method.
	 */
	private static void backupFiles() {
		routersDownloaders = new ArrayList<>();
		LoadFromCSV importcsv = new LoadFromCSV();
		List<Router> routerList = importcsv.loadCsv();
		// Java 8 
		routerList.forEach(router -> routersDownloaders.add(ERouterModels
				.returnDownloader(router)));
		// Uses a executor service to lauch all the threads
		ExecutorService execServ = Executors.newCachedThreadPool();
		routersDownloaders.forEach(routersDown -> {
			execServ.execute(routersDown);
		});

		execServ.shutdown();
		try {
			System.out.println("Waiting 10 minutes for all threads to finish");
			if (execServ.awaitTermination(10, TimeUnit.MINUTES)) {
				System.out.println("All threads finished sucessfully");
			} else {
				System.out.println("Some threads were killed by timeout");
			}
		} catch (InterruptedException e) {
			// TODO Treat exceptions
			e.printStackTrace();
		}
	}

	private static void cleanOldBackups() {
		try {
			File dir = new File(LoadConfigFile.ROOT_DIRECTORY);
			LocalDateTime minusDate = LocalDateTime.now().minusDays(LoadConfigFile.DAYS_TO_KEEP_FILES);
			ZonedDateTime zdt = minusDate.atZone(ZoneId.of("America/Sao_Paulo"));
			if (dir.exists()) {
				File[] directories = dir.listFiles(File::isDirectory);
				for (File file : directories) {
					File[] backups = file.listFiles(File::isFile);
					for (File backup : backups) {
						if (backup.lastModified() < zdt.toInstant().toEpochMilli()) {
							backup.delete();
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: Treat exceptions
			e.printStackTrace();
		}

	}

}
