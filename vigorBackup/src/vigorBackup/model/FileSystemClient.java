package vigorBackup.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class FileSystemClient {

	public static void saveToFileSystem(
			List<DefaultRouterWebDownloader> routerDonwloaderList) {
		routerDonwloaderList.forEach(routerDownloader -> {
			if (routerDownloader.isBackupOK()) {
				saveDataToFile(routerDownloader);
			}

		});

	}

	/**
	 * Saves the downloaded data to a local file.
	 * 
	 * @param data
	 *            The data to be saved. Usually it's the backup
	 */
	private static void saveDataToFile(DefaultRouterWebDownloader downloader) {
		String directory = LoadConfigFile.ROOT_DIRECTORY
				+ downloader.getRouter().getSiteName();
		try {
			new File(directory).mkdirs();
		} catch (Exception e) {
			// TODO: Treat the could not create directory or security exception
			e.printStackTrace();
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(directory + "\\"
					+ downloader.getBackupFileName());
			out.write(downloader.getDownloadedBackup());
			out.flush();
			out.close();
			cleanOldBackups(directory);
		} catch (IOException e) {
			// TODO Can't create file
			e.printStackTrace();
		}

	}

	private static void cleanOldBackups(String directory) {
		try {
			File dir = new File(directory);
			LocalDateTime minusDate = LocalDateTime.now().minusDays(
					LoadConfigFile.DAYS_TO_KEEP_FILES);
			ZonedDateTime zdt = minusDate
					.atZone(ZoneId.of("America/Sao_Paulo"));
			File[] backups = dir.listFiles(File::isFile);
			for (File backup : backups) {
				if (backup.lastModified() < zdt.toInstant().toEpochMilli()) {
					backup.delete();
				}

			}

		} catch (Exception e) {
			// TODO: Treat exceptions
			e.printStackTrace();
		}

	}

}
