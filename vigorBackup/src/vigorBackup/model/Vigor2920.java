package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class implements the backup routines specific to Vigor2920 routers.
 * 
 * @see BaseDownloader
 */
public class Vigor2920 extends BaseDownloader {
	/**
	 * The connection.
	 */
	private HttpURLConnection conn;
	/**
	 * Temporary string thats used to download the firmware data.
	 */
	private static final String FILE_DOWNLOAD_STRING = "/V2920_temp.cfg";
	/**
	 * User agent to be passed to the connection.
	 */
	private static final String USER_AGENT = "Mozilla/5.0 "
			+ "(Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0";

	/**
	 * Creates a new downloader.
	 * 
	 * @param router The router that will have it's firmware downloaded.
	 */
	public Vigor2920(final Router router) {
		super(router);
	}

	/**
	 * Downloads a backup file by authenticating to wlogin.cgi using a POST. The
	 * user and password are first base64'ed and then sent. The retrieved cookie
	 * contains the session information and will be used later to download the
	 * file.
	 */
	@Override
	public final boolean downloadBackupFromUrl(final Address address) {
		boolean isDownloadOk = false;
		try {
			//Connects to the router and authenticates.
			String request = address.getAddress().toString();
			// url used to authenticate
			request += "/cgi-bin/wlogin.cgi";
			//This router uses some special parameters to authenticate
			String urlParameters = "aa="
					+ getRouter().getBase64EncodedUsername() + "&ab="
					+ getRouter().getBase64EncodedPassword();
			// + "&sslgroup=-1&obj3=&obj4=&obj5=&obj6=&obj7=";

			URL url = new URL(request);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			conn.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(
					conn.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			String cookie = conn.getHeaderFields().get("Set-Cookie").get(0);

			conn.disconnect();

			//Uses the session cookie to download the firmware file. 
			URL downloadUrl = new URL(address.getAddress().toString()
					+ FILE_DOWNLOAD_STRING);
			conn = (HttpURLConnection) downloadUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("User-Agent", USER_AGENT);

			int contentLength = conn.getContentLength();
			InputStream raw = conn.getInputStream();
			InputStream in = new BufferedInputStream(raw);
			byte[] data = new byte[contentLength];
			int bytesRead = 0;
			int offset = 0;
			while (offset < contentLength) {
				bytesRead = in.read(data, offset, data.length - offset);
				if (bytesRead == -1) {
					break;
				}
				offset += bytesRead;
			}
			in.close();

			if (offset != contentLength) {
				isDownloadOk = false;
			}
			setDownloadedBackup(data);
			isDownloadOk = true;

		} catch (Exception e) {
			isDownloadOk = false;
		}

		return isDownloadOk;

	}

}
