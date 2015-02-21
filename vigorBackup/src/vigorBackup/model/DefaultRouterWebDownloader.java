package vigorBackup.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private Date date = new Date();

	/**
	 * The backup file suffix.
	 */
	private static final String FILENAME_SUFFIX = "-backup.cfg";
	
	/**
	 * True if the downloader got a file successfully
	 */
	private boolean isBackupOK = false;

	/**
	 * Download the router's backup using all available connection addresses,
	 * sequentially.
	 * 
	 * @return True if at least one of the links worked and the download was
	 *         successful
	 */
	public void downloadBackup() {
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
		setBackupOK(isBackupDone);
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
	 * Formats the correct filename for the backup file. It's used to make all
	 * saving implementations uniform.
	 * 
	 * @return The formatted filename.
	 */
	public String getBackupFileName() {
		return this.getRouter().getSiteName() + "-" + dateFormat.format(date)
				+ FILENAME_SUFFIX;
	}

	/**
	 * Run method to support multithreaded downloads. It only executes the
	 * backup process.
	 */
	@Override
	public void run() {
		downloadBackup();
	}

	/**
	 * Return the backup status
	 * 
	 * @return True if the download was completed successfully.
	 */
	public boolean isBackupOK() {
		return isBackupOK;
	}

	/**
	 * Sets the backups status
	 * 
	 * @param isBackupOK
	 *            True if ok, false otherwise.
	 */
	public void setBackupOK(boolean isBackupOK) {
		this.isBackupOK = isBackupOK;
	}

}
