package vigorBackup.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

public class WebDavClient {
	private static Sardine sardineClient;
	private static List<DefaultRouterWebDownloader> downloadersList;
	private static String webDavURL;
	private static final int WEBDAV_MAX_LIST_DEPTH = 1;

	public static void saveFilesToWebDav(
			List<DefaultRouterWebDownloader> routersDownloaders) {
		webDavURL = LoadConfigFile.WEBDAV_ADDRESS;
		sardineClient = SardineFactory.begin(LoadConfigFile.WEBDAV_USERNAME,
				LoadConfigFile.WEBDAV_PASSWORD);
		downloadersList = routersDownloaders;
		try {
			saveNewFiles();
		} catch (IOException e) {
			if (!LoadConfigFile.IS_SMTP_DEBUG_ON) {
				System.out
						.println("Could not save files to WebDav. Active debug mode in properties to know why");
			} else {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Saves new files in the webdav folder. The format is:
	 * {currentWebDavAddress/RouterSiteName/FileName}.
	 * 
	 * @throws IOException
	 */
	private static void saveNewFiles() throws IOException {
		for (DefaultRouterWebDownloader routerDownloader : downloadersList) {
			// Only bother saving backups that are ok
			if (!routerDownloader.isBackupOK())
				continue;
			StringBuilder sbDirname = new StringBuilder(routerDownloader
					.getRouter().getSiteName());
			// Checks if the directory name have the needed slashes.
			if (!sbDirname.toString().endsWith("/"))
				sbDirname.append("/");
			String webdavBackupDir = webDavURL + sbDirname.toString();
			String fileName = routerDownloader.getBackupFileName();

			if (!sardineClient.exists(webdavBackupDir))
				sardineClient.createDirectory(webdavBackupDir);

			ByteArrayInputStream byis = new ByteArrayInputStream(
					routerDownloader.getDownloadedBackup());

			sardineClient.put(webdavBackupDir + fileName, byis,
					"application/octet-stream", false,
					routerDownloader.getDownloadedBackup().length);
			// Only delete old backups if new ones are being made.
			deleteOldFiles(webdavBackupDir);
		}
	}

	/**
	 * Deletes old backup files.
	 * 
	 * @param webDavBackupDir
	 *            The directory that will be looked into to find the files.
	 * @throws IOException
	 *             When a problem ocurrs.
	 */
	private static void deleteOldFiles(String webDavBackupDir)
			throws IOException {
		List<DavResource> filesList = sardineClient.list(webDavBackupDir,
				WEBDAV_MAX_LIST_DEPTH);
		// We use the localDateTime in java8
		LocalDateTime minusDate = LocalDateTime.now().minusDays(
				LoadConfigFile.DAYS_TO_KEEP_FILES);
		ZonedDateTime zdt = minusDate.atZone(ZoneId.systemDefault());
		
		// Apply a filter to make sure we only have backup files on it and all
		// of them are older than the defined days to keep.
		// then we sort the list based on the modification time.
		// java 8
		List<DavResource> configFilesList = filesList
				.stream()
				.filter(file -> (!file.isDirectory()
						&& file.getDisplayName().endsWith(".cfg") && file
						.getModified().before(Date.from(zdt.toInstant()))))
				.sorted(new Comparator<DavResource>() {

					@Override
					public int compare(DavResource o1, DavResource o2) {

						return o1.getModified().compareTo(o2.getModified());
					}
				}).collect(Collectors.toList());

		if (!configFilesList.isEmpty()) {			
			while(configFilesList.size() > LoadConfigFile.DAYS_TO_KEEP_FILES){
				sardineClient.delete(configFilesList.get(0).getHref().toString());
				configFilesList.remove(0);
			}
			//delete the oldest file that is below the limit DAYS_TO_KEEP_FILES
			sardineClient.delete(configFilesList.get(0).getHref().toString());
		}
	}

}
