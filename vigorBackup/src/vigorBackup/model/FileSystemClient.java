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

/**
 * This class is responsible for saving of files to the FileSystem.
 */
public final class FileSystemClient {
	/**
	 * Checks if the program is in debug mode.
	 */
	private static boolean isDebugOn = (boolean) Configs
			.getConfig(EConfigs.IS_DEBUG_ON);

	/**
	 * Saves the files got by the downloaders to disk. Only downloaders that
	 * successfully downloaded files are processed.
	 * 
	 * @param downloaderList
	 *            The list of downloaders to have their backups saved.
	 */
	public static void saveToFileSystem(
			final List<BaseDownloader> downloaderList) {

		for (BaseDownloader downloader : downloaderList) {
			if (downloader.isBackupOK()) {
				saveDataToFile(downloader);
			}
		}

	}

	/**
	 * Saves the downloaded data to a local file.
	 * 
	 * @param downloader
	 *            The downloader that we will get the backup file from.
	 */
	private static void saveDataToFile(final BaseDownloader downloader) {
		String directory = (String) Configs
				.getConfig(EConfigs.ROOT_DIRECTORY);
		directory += downloader.getRouter().getSiteName();
		try {
			new File(directory).mkdirs();
		} catch (Exception e) {
			if (!isDebugOn) {
				System.out.println("Error creating backup directory. "
						+ "Activate debug for more info");
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
			if (!isDebugOn) {
				System.out.println("Error saving files. "
						+ "Activate debug for more info");
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
	private static void cleanOldBackups(final String directory) {

		File dir = new File(directory);
		LocalDateTime minusDate = LocalDateTime.now().minusDays(
				(int) Configs.getConfig(EConfigs.DAYS_TO_KEEP));
		ZonedDateTime zdt = minusDate.atZone(ZoneId.systemDefault());
		// We use a file Filter to weed out the files or directories that we
		// don't care about.
		FileFilter backupFileFilter = new FileFilter() {

			@Override
			public boolean accept(final File file) {
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
			public int compare(final File o1, final File o2) {
				if (o1.lastModified() > o2.lastModified()) {
					return 1;
				}
				if (o1.lastModified() < o2.lastModified()) {
					return -1;
				}
				if (o1.lastModified() == o2.lastModified()) {
					return 0;
				}
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

	/**
	 * Private constructor. This class should not be instantiated.
	 */
	private FileSystemClient() {

	}
}
