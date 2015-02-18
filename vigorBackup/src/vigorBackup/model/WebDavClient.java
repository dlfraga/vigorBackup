package vigorBackup.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

public class WebDavClient {
	private static Sardine sardineClient;
	private static List<DefaultRouterWebDownloader> downloadersList;
	private static String webDavURL;
	private static final int WEBDAV_MAX_LIST_DEPTH = 1;
	//TODO: Try to validate if the webdav ur is valid.

	public static void saveFilesToWebDav(
			List<DefaultRouterWebDownloader> routersDownloaders) {
		webDavURL = LoadConfigFile.WEBDAV_ADDRESS;
		sardineClient = SardineFactory.begin(LoadConfigFile.WEBDAV_USERNAME,
				LoadConfigFile.WEBDAV_PASSWORD);
		downloadersList = routersDownloaders;
		try {
			saveNewFiles();
		} catch (IOException e) {
			// TODO: Treat exception
			e.printStackTrace();
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
			//Only delete old backups if new ones are being made. 
			deleteOldFiles(webdavBackupDir);
		}
	}

	/**
	 * Deletes old backup files.
	 * @param webDavBackupDir The directory that will be looked into to find the files.
	 * @throws IOException When a problem ocurrs. 
	 */
	private static void deleteOldFiles(String webDavBackupDir)
			throws IOException {
		List<DavResource> filesList = sardineClient.list(webDavBackupDir, WEBDAV_MAX_LIST_DEPTH);
		// We use the localDateTime in java8
		LocalDateTime minusDate = LocalDateTime.now().minusDays(
				LoadConfigFile.DAYS_TO_KEEP_FILES);
		ZonedDateTime zdt = minusDate.atZone(ZoneId.of("America/Sao_Paulo"));
		//
		for (DavResource file : filesList) {
			//Try to make sure we are not eliminating something we shouldn't.
			if (!file.isDirectory() && file.getDisplayName().endsWith(".cfg")) {				
				if (file.getModified().before(Date.from(zdt.toInstant()))) {
					sardineClient.delete(file.getHref().toString());
				}

			}
		}

	}

}
