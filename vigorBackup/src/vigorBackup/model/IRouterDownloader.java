package vigorBackup.model;

interface IRouterDownloader {
	/**
	 * Download the backup from a specific url
	 * @param address The address that will be used to connect to the router
	 * @return True if the download was successful
	 */
	boolean downloadBackupFromUrl(Address address);
	
}
