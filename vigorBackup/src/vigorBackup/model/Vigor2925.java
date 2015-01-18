package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class implements the backup routines specific to Vigor2925 routers.
 * @see DefaultRouterWebDownloader
 */
public class Vigor2925 extends DefaultRouterWebDownloader {
	private String cookie;
	private HttpURLConnection connection;

	public Vigor2925(final Router router) {
		super(router);
	}

	/**
	 * Downloads a backup file by authenticating to wlogin.cgi using a POST. The
	 * user and password are first base64'ed and then sent. The retrieved cookie
	 * contains the session information and will be used later to download the
	 * file.
	 */
	@Override
	public boolean downloadBackupFromUrl(Address address) {
		boolean isDownloadOk = false;
		try {

			String request = address.getAddress().toString();
			// url used to authenticate
			request += "/cgi-bin/wlogin.cgi";
			String urlParameters = "aa="
					+ getRouter().getBase64EncodedUsername() + "&ab="
					+ getRouter().getBase64EncodedPassword();
			// + "&sslgroup=-1&obj3=&obj4=&obj5=&obj6=&obj7=";

			URL url = new URL(request);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			String cookie = connection.getHeaderFields().get("Set-Cookie")
					.get(0);

			connection.disconnect();

			URL downloadUrl = new URL(address.getAddress().toString()
					+ "/V2925_das.cfg");
			connection = (HttpURLConnection) downloadUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Cookie", cookie);
			connection
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");

			int contentLength = connection.getContentLength();
			InputStream raw = connection.getInputStream();
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
				isDownloadOk = false;
			}
			setDownloadedBackup(data);
			saveDataToFile(getDownloadedBackup());

			isDownloadOk = true;

		} catch (Exception e) {
			isDownloadOk = false;

		}

		return isDownloadOk;

	}

	public String getCookies() {
		return cookie;
	}

	public void setCookies(String cookies) {
		this.cookie = cookies;
	}

}
