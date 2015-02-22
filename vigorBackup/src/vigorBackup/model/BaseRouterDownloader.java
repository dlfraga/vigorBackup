package vigorBackup.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Defines a Default Router Downloader that the concrete routers should inherit
 * and the methods that must be overridden.
 */
public abstract class BaseRouterDownloader extends Thread {

	/**
	 * The router that will have its firmware downloaded.
	 */
	private Router router;
	/**
	 * The resulting file.
	 */
	private byte[] downloadedBackup;
	/**
	 * The Date Format that will be used in the backup filename.
	 */
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	/**
	 * The current date to be used on the backup format.
	 */
	private Date date = new Date();

	/**
	 * The backup file suffix.
	 */
	private static final String FILENAME_SUFFIX = "-backup.cfg";
	
	/**
	 * True if the downloader got a file successfully.
	 */
	private boolean isBackupOK = false;

	/**
	 * Download the router's backup using all available connection addresses,
	 * sequentially.
	 * 
	 */
	public final void downloadBackup() {
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
	public abstract boolean downloadBackupFromUrl(final Address address);

	/**
	 * @param routr
	 *            The router object that needs to have it's backup downloaded
	 */
	public BaseRouterDownloader(final Router routr) {
		this.router = routr;
	}

	/**
	 * @return the router
	 */
	public final Router getRouter() {
		return router;
	}

	/**
	 * @param routr
	 *            the router to set
	 */
	public final void setRouter(final Router routr) {
		this.router = routr;
	}

	/**
	 * @return the downloadedBackup
	 */
	public final byte[] getDownloadedBackup() {
		return downloadedBackup;
	}

	/**
	 * @param downloadedBackp
	 *            the downloadedBackup to set
	 */
	public final void setDownloadedBackup(final byte[] downloadedBackp) {
		this.downloadedBackup = downloadedBackp;
	}

	/**
	 * Formats the correct filename for the backup file. It's used to make all
	 * saving implementations uniform.
	 * 
	 * @return The formatted filename.
	 */
	public final String getBackupFileName() {
		return this.getRouter().getSiteName() + "-" + dateFormat.format(date)
				+ FILENAME_SUFFIX;
	}

	/**
	 * Run method to support multithreaded downloads. It only executes the
	 * backup process.
	 */
	@Override
	public final void run() {
		downloadBackup();
	}

	/**
	 * Return the backup status.
	 * 
	 * @return True if the download was completed successfully.
	 */
	public final boolean isBackupOK() {
		return isBackupOK;
	}

	/**
	 * Sets the backups status.
	 * 
	 * @param isBackpOK
	 *            True if ok, false otherwise.
	 */
	public final void setBackupOK(final boolean isBackpOK) {
		this.isBackupOK = isBackpOK;
	}

}
