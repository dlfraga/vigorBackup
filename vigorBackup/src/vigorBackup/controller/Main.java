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

import vigorBackup.factory.RouterDownloaderFactory;
import vigorBackup.model.DefaultRouterWebDownloader;
import vigorBackup.model.LoadFromCSV;
import vigorBackup.model.Router;

public class Main {
	/**
	 * Root directory to save downloaded files
	 */
	public static String ROOT_DIRECTORY;
	private static Properties props;

	public static void main(String[] args) {
		//WebDavClient webDavClient = new WebDavClient();
		
		
		props = new Properties();
		try {
			props.load(new FileInputStream(new File(
					"src/META-INF/configs.properties")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ROOT_DIRECTORY = props.getProperty("backup.directory");
		cleanOldBackups();
		RouterDownloaderFactory routerFactory = new RouterDownloaderFactory();
		List<DefaultRouterWebDownloader> routersDownloaders = new ArrayList<>();

		LoadFromCSV importcsv = new LoadFromCSV();
		List<Router> routerList = importcsv.loadCsv();
		for (Router router : routerList) {
			routersDownloaders.add(routerFactory.getDownloader(
					router.getModelCode(), router));
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
