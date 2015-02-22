package vigorBackup.model;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This class implements the backup routines specific to Vigor2925 routers.
 * 
 * @see BaseRouterDownloader
 */
public class Vigor2925 extends BaseRouterDownloader {
	private String cookie;
	private HttpURLConnection connection;
	private final static String FILE_DOWNLOAD_STRING = "/V2925_temp.cfg";

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
			disableSSLChecks();
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
					+ FILE_DOWNLOAD_STRING);
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
			isDownloadOk = true;

		} catch (Exception e) {
			isDownloadOk = false;
			//e.printStackTrace();
		}

		return isDownloadOk;

	}

	public String getCookies() {
		return cookie;
	}

	public void setCookies(String cookies) {
		this.cookie = cookies;
	}

	static {
		// TODO: Checkbox to ignore valid certificates
		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						return true;
					}
				});
	}

	private void disableSSLChecks() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		}

	}

}
