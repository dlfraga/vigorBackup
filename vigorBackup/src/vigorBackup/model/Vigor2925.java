package vigorBackup.model;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Vigor2925 extends DefaultRouterWebDownloader {
	private HttpsURLConnection conn;
	private List<String> cookies;

	public Vigor2925(Router router) {
		super(router);
	}

	/**
	 * 
	 */
	@Override
	protected boolean downloadBackupFromUrl(Address address) throws IOException {
		String stringAdd = address.getAddress().toString();
		// url used to authenticate
		stringAdd += "/cgi-bin/wlogin.cgi";

		// turn on cookies
		CookieHandler.setDefault(new CookieManager());
		URL urlc = new URL(stringAdd);
		conn = (HttpsURLConnection) urlc.openConnection();
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", address.getAddress().toString());
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		int responseCode = conn.getResponseCode();
		setCookies(conn.getHeaderFields().get("Set-Cookie"));
		return true;

	}

	private void setCookies(List<String> list) {
		this.cookies = cookies;

	}

}
