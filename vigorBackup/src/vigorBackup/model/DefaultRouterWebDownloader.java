package vigorBackup.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import vigorBackup.controller.Main;

/**
 * Defines a Default Router Downloader that the specific routers should inherit
 * and methods that they can override as needed.
 */
public class DefaultRouterWebDownloader extends Thread {

	/**
	 * The router that will have its firmware downloaded.
	 */
	private Router router;
	/**
	 * The resulting file.
	 */
	private byte[] downloadedBackup;
	/**
	 * The current date to be used on the backup filename.
	 */
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	Date date = new Date();

	/**
	 * The backup file suffix.
	 */
	private static final String FILENAME_SUFFIX = "-backup.cfg";

	/**
	 * Download the router's backup using all available connection addresses,
	 * sequentially.
	 * 
	 * @return True if at least one of the links worked and the download was
	 *         successful
	 */
	public final boolean downloadBackup() {
		boolean isBackupDone = false;
		// int backupTry = 0;

		for (Address addr : getRouter().getConnectionAddresses()) {
			// backupTry++;
			// Only try new addresses if the last one didn't work
			if (!isBackupDone) {
				// System.out.println(backupTry + " "
				// + addr.getAddress().toString());
				isBackupDone = downloadBackupFromUrl(addr);
			}
		}
		if (isBackupDone) {
			System.out.println("Done" + " " + getRouter().getSiteName());
		} else {
			System.out.println("Failed" + " " + getRouter().getSiteName());
		}
		return isBackupDone;
	}

	/**
	 * Downloads a file from a specific address.
	 * 
	 * @param address
	 *            The address that will be used to download the file
	 * @return True if the download was successful
	 */
	public boolean downloadBackupFromUrl(Address address) {
		return false;

	}

	/**
	 * @param router
	 *            The router object that needs to have it's backup downloaded
	 */
	public DefaultRouterWebDownloader(Router router) {
		this.router = router;
	}

	/**
	 * @return the router
	 */
	public final Router getRouter() {
		return router;
	}

	/**
	 * @param router
	 *            the router to set
	 */
	public final void setRouter(Router router) {
		this.router = router;
	}

	/**
	 * @return the downloadedBackup
	 */
	public final byte[] getDownloadedBackup() {
		return downloadedBackup;
	}

	/**
	 * @param downloadedBackup
	 *            the downloadedBackup to set
	 */
	public final void setDownloadedBackup(byte[] downloadedBackup) {
		this.downloadedBackup = downloadedBackup;
	}

	/**
	 * Saves the downloaded data to a local file.
	 * 
	 * @param data
	 *            The data to be saved. Usually it's the backup
	 */
	public final void saveDataToFile(byte[] data) {
		String directory = Main.ROOT_DIRECTORY + this.getRouter().getSiteName();
		try {
			new File(directory).mkdirs();
		} catch (Exception e) {
			// TODO: Treat the could not create directory or security exception
			e.printStackTrace();
		}

		String filename = directory + "\\" + this.getRouter().getSiteName()
				+ "-" + dateFormat.format(date) + FILENAME_SUFFIX;
		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Can't create file
			e.printStackTrace();
		}

	}

	
	
	/**
	 * Run method to support multithreaded downloads. It only executes the
	 * backup process.
	 */
	@Override
	public void run() {
		downloadBackup();
	}

}
