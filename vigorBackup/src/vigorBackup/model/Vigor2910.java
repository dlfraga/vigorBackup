package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.IOException;
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

	/**
	 * Downloads the backup file from a specified URL
	 * 
	 * @param address
	 *            The address that will be used to download the file
	 * @return true if the download was successful
	 * @throws IOException
	 *             if the file is broken / didn't download successfully
	 */
	@Override
	protected boolean downloadBackupFromUrl(Address address) throws IOException {
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
			throw new IOException("Only read " + offset + " bytes; Expected "
					+ contentLength + " bytes");
		}
		
		setDownloadedBackup(data);
		saveDataToFile(getDownloadedBackup());
		return true;
	}

}
