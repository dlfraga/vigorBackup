package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

public class Vigor3300 extends BaseDownloader {
	// I don't know how to validate if the backup was really successful, so we
	// use this minimum size to guess.
	private int MIN_BACKUP_SIZE = 5072;
	private HttpURLConnection urc;

	public Vigor3300(Router router) {
		super(router);
	}

	@Override
	public boolean downloadBackupFromUrl(Address address) {
		try {
			String stringAdd = address.getAddress().toString();
			stringAdd += "/cgi-bin/mainfunction.cgi?set=download_cli_configuration";
			URL url = new URL(stringAdd);
			
			if(url.getProtocol().equals("http")){
				urc = (HttpURLConnection) url.openConnection();	
			} else {
				urc = (HttpsURLConnection) url.openConnection();
			}

			
			if (getRouter().getUsername() != null
					&& getRouter().getUsername().length() > 0
					&& getRouter().getPassword() != null
					&& getRouter().getPassword().length() > 0) {
				final String authString = getRouter().getUsername() + ":"
						+ getRouter().getPassword();
				urc.setRequestProperty(
						"Authorization",
						"Basic "
								+ Base64.getEncoder().encodeToString(
										authString.getBytes()));

			}
			urc.setAllowUserInteraction(true);
			urc.setDoInput(true);
			urc.setDoOutput(true);
			InputStream raw = urc.getInputStream();
			InputStream in = new BufferedInputStream(raw);
			ByteArrayOutputStream bio = new ByteArrayOutputStream();
			int count;
			byte buffer[] = new byte[1024];
			// Vigor3300 has a problem: We can't obtain the backup's size ahead
			// of time (via contentlength). We need to keep downloading until
			// it's over (-1)
			while ((count = in.read(buffer, 0, buffer.length)) != -1) {
				bio.write(buffer, 0, count);
			}

			in.close();
			setDownloadedBackup(bio.toByteArray());			
			if (bio.size() < MIN_BACKUP_SIZE) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
