package vigorBackup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import vigorBackup.model.BaseDownloader;
import vigorBackup.model.ERouterModels;
import vigorBackup.model.EmailBackupReport;
import vigorBackup.model.FileSystemClient;
import vigorBackup.model.Configs;
import vigorBackup.model.LoadFromCSV;
import vigorBackup.model.Router;
import vigorBackup.model.WebDavClient;

/**
 * Main class. It starts the backup routines.
 */
public final class Main {
	/**
	 * Time to wait for the threads to finish.
	 */
	private static final int THREAD_TIMEOUT = 10;
	/**
	 * List of the downloaders for the routers.
	 */
	private static List<BaseDownloader> routersDownloaders = new ArrayList<>();
	/**
	 * The router list.
	 */
	private static List<Router> routerList = new ArrayList<>();

	/**
	 * Main class.
	 * 
	 * @param args
	 *            The first argument must be the config file path. Other args
	 *            are ignored.
	 */
	public static void main(final String[] args) {
		Configs.loadConfigFile(args);
		routerList = LoadFromCSV.loadCsv();
		backupFiles();
		WebDavClient.saveFilesToWebDav(routersDownloaders);
		FileSystemClient.saveToFileSystem(routersDownloaders);
		EmailBackupReport.sendBackupReport(routersDownloaders);
	}

	/**
	 * Backup the firmware of the loaded router list. This method lauches all
	 * backups simultaneously and waits for then to end before continuing.
	 * DAS!
	 */
	private static void backupFiles() {
		routersDownloaders = new ArrayList<>();
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
			if (execServ.awaitTermination(THREAD_TIMEOUT, TimeUnit.MINUTES)) {
				System.out.println("All threads finished sucessfully");
			} else {
				System.out.println("Some threads were killed by timeout");
			}
		} catch (InterruptedException e) {
			// TODO Treat exceptions
			// XXX LOL
			e.printStackTrace();
		}
	}

	/**
	 * Main class.
	 */
	private Main() {
		
	}
}
