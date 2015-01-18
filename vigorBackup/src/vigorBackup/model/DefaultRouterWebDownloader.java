package vigorBackup.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Defines a Default Router Downloader that the specific routers should inherit
 * and methods that they can override as needed
 * 
 * @author Daniel
 *
 */
public class DefaultRouterWebDownloader {
	private Router router;
	private byte[] downloadedBackup;
	private String FILENAME_SUFFIX = "-backup.cfg";
	
	/**
	 * Download the router's backup using all available connection addresses,
	 * sequentially
	 * 
	 * @return True if at least one of the links worked and the download was
	 *         successful
	 */
	public boolean downloadBackup() {
		boolean isBackupDone = false;
		int backupTry = 0;
		for (Address addr : getRouter().getConnectionAddresses()) {
			backupTry++;

			// Only try new addresses if the last one didn't work
			if (!isBackupDone) {
				System.out.println(backupTry + " " + addr.getAddress().toString());
				isBackupDone = downloadBackupFromUrl(addr);

			}
		}
		return isBackupDone;
	}

	
	/**
	 * Downloads a file from a specific address.
	 * @param address The address that will be used to download the file
	 * @return True if the download was successful 
	 */
	public boolean downloadBackupFromUrl(Address address){
		return false;
		
	}

	/**
	 * @author Daniel
	 * @param router
	 *            The router object that needs to have it's backup downloaded
	 */
	public DefaultRouterWebDownloader(Router router) {
		this.router = router;
	}

	/**
	 * @return the router
	 */
	public Router getRouter() {
		return router;
	}

	/**
	 * @param router
	 *            the router to set
	 */
	public void setRouter(Router router) {
		this.router = router;
	}

	/**
	 * @return the downloadedBackup
	 */
	public byte[] getDownloadedBackup() {
		return downloadedBackup;
	}

	/**
	 * @param downloadedBackup
	 *            the downloadedBackup to set
	 */
	public void setDownloadedBackup(byte[] downloadedBackup) {
		this.downloadedBackup = downloadedBackup;
	}

	/**
	 * Saves the downloaded data to a local file.
	 * 
	 * @param data
	 *            The data to be saved. Usually it's the backup
	 */
	public void saveDataToFile(byte[] data) {
		String directory = this.getRouter().getSiteName();
		try {
			new File(directory).mkdir();
		} catch (Exception e) {
			// TODO: Treat the could not create directory or security exception
			e.printStackTrace();
		}
		
		String filename = directory + "\\" + this.getRouter().getSiteName() + FILENAME_SUFFIX ;
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

}
