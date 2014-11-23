package vigorBackup.model;

/**
 * Defines the methods and fields that inheriting classes shoud override
 * 
 * @author Daniel
 *
 */
public class RouteDefaultDecoder {
	private String password;
	private String userName;

	/**
	 * 
	 * @param userName
	 *            The login name. It can be used on the encoding routine
	 * @param password
	 *            The uses's password
	 */
	public RouteDefaultDecoder(String userName, String password) {
		setPassword(password);
		setUserName(userName);
	}

	/**
	 * Method responsible for the correct 'encription' implemented by the
	 * routers. It must be overriden on the inheriting classes.
	 */
	protected String encodeData(String data) {
		return data;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the encodedPassword
	 */
	public String getEncodedPassword() {
		return encodeData(getPassword());
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the encodedUser
	 */
	public String getEncodedUser() {
		return encodeData(getUserName());
	}

}
