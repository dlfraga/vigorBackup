package vigorBackup.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileSystemClient {

	/**
	 * Saves the files got by the downloaders to disk. Only downloaders that
	 * successfully downloaded files are processed.
	 * 
	 * @param routerDonwloaderList
	 *            The list of downloaders to have their backups saved.
	 */
	public static void saveToFileSystem(
			List<DefaultRouterWebDownloader> routerDonwloaderList) {

		for (DefaultRouterWebDownloader defaultRouterWebDownloader : routerDonwloaderList) {
			if (defaultRouterWebDownloader.isBackupOK()) {
				saveDataToFile(defaultRouterWebDownloader);
			}
		}

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
			if (!LoadConfigFile.IS_SMTP_DEBUG_ON) {
				System.out
						.println("Error creating backup directory. Activate debug for more info");
			} else {
				e.printStackTrace();
			}
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
			if (!LoadConfigFile.IS_SMTP_DEBUG_ON) {
				System.out
						.println("Error saving files. Activate debug for more info");
			} else {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Clean old backups that have been saved before.
	 * 
	 * @param directory
	 *            The backup where the files are stored.
	 */
	private static void cleanOldBackups(String directory) {

		File dir = new File(directory);
		LocalDateTime minusDate = LocalDateTime.now().minusDays(
				LoadConfigFile.DAYS_TO_KEEP_FILES);
		ZonedDateTime zdt = minusDate.atZone(ZoneId.systemDefault());
		// We use a file Filter to weed out the files or directories that we
		// don't care about.
		FileFilter backupFileFilter = new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.isFile()) {
					String path = file.getAbsolutePath().toLowerCase();
					if (path.endsWith(".cfg")) {
						if (file.lastModified() < zdt.toInstant()
								.toEpochMilli()) {
							return true;
						}
					}
				}
				return false;
			}
		};

		File[] backups = dir.listFiles(backupFileFilter);
		// sort files according to their modification time. This makes sure
		// we only delete the oldest ones.
		Arrays.sort(backups, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				if (o1.lastModified() > o2.lastModified())
					return 1;
				if (o1.lastModified() < o2.lastModified())
					return -1;
				if (o1.lastModified() == o2.lastModified())
					return 0;
				return 0;
			}
		});
		// TODO: Backups that are older than the days to keep and are in the
		// folder should be deleted to keep
		// them at the specified number. This accounts for changes in
		// configuration, because we only delete one file for one
		// successfull backup.
		if (backups.length > 0) {
			backups[0].delete();
		}
	}

}
