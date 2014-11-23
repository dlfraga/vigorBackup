package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

/**
 * Concrete class for Vigor2910. This router uses auth basic, so we don't need
 * to worry about the encoding algorithm
 * 
 * @author Daniel
 */

public class Vigor2910 extends DefaultRouterWebDownloader {

	public Vigor2910(Router router) {
		super(router);
	}

	@Override
	protected boolean downloadBackupFromUrl(Address address) throws IOException {
		String stringAdd = address.getAddress().toString();
		stringAdd += "/BackupCFG.bin";
		URL url = address.getAddress();
		
		HttpURLConnection urc = (HttpURLConnection) url.openConnection();
		if (getRouter().getUsername() != null
				&& getRouter().getUsername().length() > 0
				&& getRouter().getPassword() != null
				&& getRouter().getPassword().length() > 0) {
			final String authString = getRouter().getUsername() + ":"
					+ getRouter().getPassword();
			urc.setRequestProperty("Authorization", "Basic"
					+ Base64.getEncoder().encodeToString(authString.getBytes()));

		}
		urc.setRequestMethod("GET");
		urc.setAllowUserInteraction(false);
		urc.setDoInput(false);
		urc.setDoOutput(true);

		String contentType = urc.getContentType();
		int contentLength = urc.getContentLength();

		if (contentType.startsWith("text/") || contentLength == -1) {
			System.out.println("this is not a binary file");
			return false;
		}
		InputStream raw = urc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1) {
				return false;
			}
			offset += bytesRead;
		}
		in.close();

		if (offset != contentLength) {
			return false;
		}

		setDownloadedBackup(data);
		return true;

	}

}
