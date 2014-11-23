package vigorBackup.model;

import java.util.Base64;
import java.util.Base64.Encoder;

public class Vigor2925 extends DefaultRouterWebDownloader {

	public Vigor2925(Router router) {
		super(router);
	}

//	public Vigor2925(String userName, String password) {
//		super(userName, password);
//	}
//
//	/**
//	 * This method implements the draytek "enconding" on it's login routine.
//	 * It basically encodes data to UTF8
//	 * @param data Data to be encoded
//	 * 		
//	 */
//	@Override
//	protected String encodeData(String data) {
//		Encoder encoder = Base64.getEncoder();
//		return encoder.encodeToString(data.getBytes());
//	}

}
