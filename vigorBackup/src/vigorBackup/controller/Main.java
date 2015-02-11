package vigorBackup.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Days;

import vigorBackup.model.DefaultRouterWebDownloader;
import vigorBackup.model.ERouterModels;
import vigorBackup.model.EmailBackupReport;
import vigorBackup.model.LoadFromCSV;
import vigorBackup.model.Router;

public class Main {
	/**
	 * Root directory to save downloaded files. It's loaded from the config file
	 */
	public static String ROOT_DIRECTORY;
	/**
	 * Days to keep the backup files.
	 */
	public static int DAYS_TO_KEEP_FILES;
	/**
	 * CSV router list
	 * 
	 * @see LoadFromCSV
	 */
	public static String ROUTER_LIST_FILE;
	/**
	 * Properties that are loaded from the config file
	 */
	private static Properties props;
	/**
	 * List of the downloaders for the routers
	 */
	private static List<DefaultRouterWebDownloader> routersDownloaders = new ArrayList<>();
	/**
	 * Fields used to send emails.
	 */
	public static String SMTP_HOST;
	public static boolean IS_SMTP_AUTH_NEEDED;
	public static boolean IS_SMTP_DEBUG_ON;
	public static boolean IS_SMTP_SSL_ENABLED;
	public static String SMTP_TO_EMAIL;
	public static String SMTP_FROM_EMAIL;
	public static String SMTP_LOGIN_USERNAME;
	public static String SMTP_PASSWORD;
	public static int SMTP_PORT;

	/**
	 * Main class.
	 * 
	 * @param args
	 *            The first argument must be the config file path. Other args
	 *            are ignored.
	 */
	public static void main(String[] args) {
		loadConfigFile(args);
		cleanOldBackups();
		backupFiles();
		EmailBackupReport.sendBackupReport(routersDownloaders);
	}

	private static void backupFiles() {
		routersDownloaders = new ArrayList<>();
		LoadFromCSV importcsv = new LoadFromCSV();
		List<Router> routerList = importcsv.loadCsv();
		// Java 8 lambdas.
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
			if(execServ.awaitTermination(10, TimeUnit.MINUTES)){
				System.out.println("All threads finished sucessfully");
			} else {
				System.out.println("Some threads were killed by timeout");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static void loadConfigFile(String[] args) {
		if (args.length == 0) {
			System.out.println("You need to specify a config file location\n"
					+ "I'm going to create a default one now");
			// TODO: Create a default config file.
			System.exit(1);
		}
		String configFile = args[0];
		props = new Properties();
		try {
			props.load(new FileInputStream(new File(configFile)));
		} catch (FileNotFoundException e) {
			// TODO Treat exceptions
			System.out.println("The config file could not be found");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't read the file.");
			System.exit(2);
		}
		ROOT_DIRECTORY = props.getProperty("backup.directory");
		DAYS_TO_KEEP_FILES = Integer.parseInt(props
				.getProperty("days.to.keep.backups"));
		SMTP_HOST = props.getProperty("mail.smtp.host");
		IS_SMTP_AUTH_NEEDED = Boolean.parseBoolean(props
				.getProperty("mail.smtp.auth"));
		IS_SMTP_DEBUG_ON = Boolean.parseBoolean(props
				.getProperty("mail.smtp.debug"));
		IS_SMTP_SSL_ENABLED = Boolean.parseBoolean(props
				.getProperty("mail.smtp.ssl.enable"));
		SMTP_TO_EMAIL = props.getProperty("mail.smtp.to");
		SMTP_FROM_EMAIL = props.getProperty("mail.smtp.from");
		SMTP_PASSWORD = props.getProperty("mail.smtp.password");
		SMTP_LOGIN_USERNAME = props.getProperty("mail.smtp.username");
		SMTP_PORT = Integer.parseInt(props.getProperty("mail.smtp.port"));

	}

	private static void cleanOldBackups() {
		try {
			File dir = new File(ROOT_DIRECTORY);
			if (dir.exists()) {
				File[] directories = dir.listFiles(File::isDirectory);
				for (File file : directories) {
					File[] backups = file.listFiles(File::isFile);
					for (File backup : backups) {
						DateTime start = new DateTime(DateTime.now());
						DateTime end = new DateTime(backup.lastModified());
						Days days = Days.daysBetween(end, start);
						if (days.getDays() > DAYS_TO_KEEP_FILES) {
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
