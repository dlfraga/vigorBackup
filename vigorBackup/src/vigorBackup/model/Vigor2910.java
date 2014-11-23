package vigorBackup.model;

/**
 * Concrete class for Vigor2910. This router uses auth basic, so we don't need
 * to worry about the encoding algorithm
 * @author Daniel 
 */

public class Vigor2910 extends RouteDefaultDecoder{

	public Vigor2910(String userName, String password) {
		super(userName, password);
	}

	
	
}
