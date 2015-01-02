package vigorBackup.model;

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
		String filename = "vigor.cfg";
		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
