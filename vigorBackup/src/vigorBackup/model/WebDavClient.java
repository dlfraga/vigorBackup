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

/**
 * Client for saving files in a WebDav server.
 *
 */
public final class WebDavClient {
	/**
	 * Sardine webdav client.
	 */
	private static Sardine sardineClient;
	/**
	 * This is a list of the downloaders. It's used to get the files that need
	 * to be saved.
	 */
	private static List<BaseDownloader> downloadersList;
	/**
	 * The webdav url, as loaded from the config file.
	 */
	private static String webDavURL;
	/**
	 * When listing files we don't want the search to go deeper than one level.
	 * This is a security measure to avoid problems if the user specifies a
	 * wrong webdav address.
	 */
	private static final int WEBDAV_MAX_LIST_DEPTH = 1;

	/**
	 * Save all files that the downloaders got to a webdav server.
	 * 
	 * @param routersDownloaders
	 *            The downloader list.
	 */
	public static void saveFilesToWebDav(
			final List<BaseDownloader> routersDownloaders) {
		webDavURL = (String) Configs.getConfig(EConfigs.WEBDAV_ADDRESS);
		String wbUser = (String) Configs
				.getConfig(EConfigs.WEBDAV_USERNAME);
		String wbPass = (String) Configs
				.getConfig(EConfigs.WEBDAV_PASSWORD);
		sardineClient = SardineFactory.begin(wbUser, wbPass);
		downloadersList = routersDownloaders;
		try {
			saveNewFiles();
		} catch (IOException e) {
			if (!(boolean) Configs.getConfig(EConfigs.IS_DEBUG_ON)) {
				System.out.println("Could not save files to WebDav. "
						+ "Activate debug mode in " + "properties to know why");
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
	 *             If a problem occurs.
	 */
	private static void saveNewFiles() throws IOException {
		for (BaseDownloader routerDownloader : downloadersList) {
			// Only bother saving backups that are ok
			if (!routerDownloader.isBackupOK()) {
				continue;
			}
			StringBuilder sbDirname = new StringBuilder(routerDownloader
					.getRouter().getSiteName());
			// Checks if the directory name have the needed slashes.
			if (!sbDirname.toString().endsWith("/")) {
				sbDirname.append("/");
			}
			String webdavBackupDir = webDavURL + sbDirname.toString();
			String fileName = routerDownloader.getBackupFileName();

			if (!sardineClient.exists(webdavBackupDir)) {
				sardineClient.createDirectory(webdavBackupDir);
			}

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
	private static void deleteOldFiles(final String webDavBackupDir)
			throws IOException {
		int daysToKeepFiles = (int) Configs.getConfig(EConfigs.DAYS_TO_KEEP);
		List<DavResource> filesList = sardineClient.list(webDavBackupDir,
				WEBDAV_MAX_LIST_DEPTH);
		// We use the localDateTime in java8
		LocalDateTime minusDate = LocalDateTime.now().minusDays(
				daysToKeepFiles);
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
					public int compare(final DavResource o1,
							final DavResource o2) {

						return o1.getModified().compareTo(o2.getModified());
					}
				}).collect(Collectors.toList());

		if (!configFilesList.isEmpty()) {
			// FIXME: ConfigFilesList will never have more than DAYS_TO_KEEP
			while (configFilesList.size() > daysToKeepFiles) {
				sardineClient.delete(configFilesList.get(0).getHref()
						.toString());

			}
			configFilesList.remove(0);
			// delete the oldest file that is below the limit DAYS_TO_KEEP
			sardineClient.delete(configFilesList.get(0).getHref().toString());
		}
	}

	/**
	 * This class should not be instantiated.
	 */
	private WebDavClient() {
		// nt used
	}
}
