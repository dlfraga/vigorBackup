package vigorBackup.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.Days;

import vigorBackup.model.DefaultRouterWebDownloader;
import vigorBackup.model.ERouterModels;
import vigorBackup.model.LoadFromCSV;
import vigorBackup.model.Router;

public class Main {
	/**
	 * Root directory to save downloaded files
	 */
	public static String ROOT_DIRECTORY;
	private static Properties props;

	public static void main(String[] args) {
		if(args == null){
			System.out.println("You need to specify a config file location \n "
					+ "I'm going to create a default one now");
		}
		String configFile = args[0];
		
		props = new Properties();
		try {
			props.load(new FileInputStream(new File(
					configFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("The config file could not be found");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't read the file.");
			System.exit(2);
		}
		ROOT_DIRECTORY = props.getProperty("backup.directory");
		cleanOldBackups();
		
		List<DefaultRouterWebDownloader> routersDownloaders = new ArrayList<>();

		LoadFromCSV importcsv = new LoadFromCSV();
		List<Router> routerList = importcsv.loadCsv();
		for (Router router : routerList) {
			routersDownloaders.add(ERouterModels.returnDownloader(router));
		}

		for (DefaultRouterWebDownloader defaultRouterWebDownloader : routersDownloaders) {
			defaultRouterWebDownloader.start();
		}

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
						if (days.getDays() > Integer.parseInt(props
								.getProperty("days.to.keep.backups"))) {
							backup.delete();
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: Treat
			e.printStackTrace();
		}

	}

}
