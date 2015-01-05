package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Concrete class for Vigor2910. This router uses auth basic.
 * 
 * @author Daniel
 */

public class Vigor2910 extends DefaultRouterWebDownloader {

	public Vigor2910(Router router) {
		super(router);
	}

	
	@Override
	public boolean downloadBackupFromUrl(Address address) {
		try {
			String stringAdd = address.getAddress().toString();
			stringAdd += "/V2910_date.cfg";
			URL url = new URL(stringAdd);

			HttpURLConnection urc = (HttpURLConnection) url.openConnection();
			if (getRouter().getUsername() != null
					&& getRouter().getUsername().length() > 0
					&& getRouter().getPassword() != null
					&& getRouter().getPassword().length() > 0) {
				final String authString = getRouter().getUsername() + ":"
						+ getRouter().getPassword();
				urc.setRequestProperty("Authorization", "Basic "
						+ Base64.getEncoder().encodeToString(authString.getBytes()));

			}
			urc.setAllowUserInteraction(true);
			urc.setDoInput(true);
			urc.setDoOutput(true);
			int contentLength = urc.getContentLength();
			InputStream raw = urc.getInputStream();
			InputStream in = new BufferedInputStream(raw);
			byte[] data = new byte[contentLength];
			int bytesRead = 0;
			int offset = 0;
			while (offset < contentLength) {
				bytesRead = in.read(data, offset, data.length - offset);
				if (bytesRead == -1)
					break;
				offset += bytesRead;
			}
			in.close();

			if (offset != contentLength) {
				return false;
			}
			
			setDownloadedBackup(data);
			saveDataToFile(getDownloadedBackup());
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}



	/**
	 * Download the router's backup using all available connection addresses,
	 * sequentially
	 * 
	 * @return True if at least one of the links worked and the download was
	 *         successful
	 */
	@Override
	public boolean downloadBackup() {
		boolean isBackupDone = false;
		int backupTry = 0;
		for (Address addr : getRouter().getConnectionAddresses()) {
			backupTry++;

			// Only try new addresses if the last one didn't work
			if (!isBackupDone) {
				System.out.println(backupTry);
				isBackupDone = downloadBackupFromUrl(addr);

			}
		}
		return isBackupDone;
	}

}
