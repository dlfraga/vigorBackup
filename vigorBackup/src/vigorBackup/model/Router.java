package vigorBackup.model;

import java.util.Base64;
import java.util.Calendar;
import java.util.List;

/**
 * Router class. It's used to store all data needed to download the files.
 */
public class Router {
	/**
	 * ID of the router. Not currently in use.
	 */
	private Long id;
	/**
	 * Description of the router.
	 */
	private String descri;
	/**
	 * Where the router is.
	 */
	private String siteName;
	/**
	 * Was the last backup ok?
	 */
	private boolean isOk;
	/**
	 * Last backup date.
	 */
	private Calendar lastBackupDate;
	/**
	 * List of connection addresses.
	 */
	private List<Address> connectionAddresses;
	/**
	 * Router's passwd.
	 */
	private String passwd;
	/**
	 * Router's login.
	 */
	private String login;
	/**
	 * Router model, as in {@link ERouterModels}.
	 */
	private ERouterModels routerModel;

	/**
	 * Instantiates a new router.
	 */
	public Router() {

	}

	/**
	 * Gets the router descri.
	 * 
	 * @return Router's descri.
	 */
	public final String getDescription() {
		return descri;
	}

	/**
	 * Sets the router's descriptions.
	 * 
	 * @param description
	 *            The new description.
	 */
	public final void setDescription(final String description) {
		this.descri = description;
	}

	/**
	 * Was the last backup ok?
	 * 
	 * @return True if the last backup was completed successfully.
	 */
	public final boolean isOk() {
		return isOk;
	}

	/**
	 * Sets the last backup status.
	 * 
	 * @param status
	 *            The status.
	 */
	public final void setOk(final boolean status) {
		this.isOk = status;
	}

	/**
	 * Gets the last backup date.
	 * 
	 * @return The last backup date.
	 */
	public final Calendar getLastBackupDate() {
		return lastBackupDate;
	}

	/**
	 * Sets the last backup date.
	 * 
	 * @param date
	 *            The backup date.
	 */
	public final void setLastBackupDate(final Calendar date) {
		this.lastBackupDate = date;
	}

	/**
	 * Gets the router's ID.
	 * 
	 * @return The id.
	 */
	public final Long getId() {
		return id;
	}
	
	/**
	 * Sets the router id.
	 * @param routerId The id.
	 */
	public final void setId(final long routerId) {
		this.id = routerId;
	}

	/**
	 * Gets a list of all connection addresses.
	 * 
	 * @return A list of connections addresses.
	 */
	public final List<Address> getConnectionAddresses() {
		return connectionAddresses;
	}

	/**
	 * Sets the connection address list.
	 * 
	 * @param list
	 *            The list of addresses.
	 */
	public final void setConnectionAddresses(final List<Address> list) {
		this.connectionAddresses = list;
	}

	/**
	 * @return the passwd
	 */
	public final String getPassword() {
		return passwd;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(final String password) {
		this.passwd = password;
	}

	/**
	 * @return the login
	 */
	public final String getUsername() {
		return login;
	}

	/**
	 * @param username
	 *            the login to set
	 */
	public final void setUsername(final String username) {
		this.login = username;
	}

	/**
	 * Encodes the plain text login in base64.
	 * 
	 * @return The encoded login
	 */
	public final String getBase64EncodedUsername() {
		return Base64.getEncoder().encodeToString(getUsername().getBytes());
	}

	/**
	 * Encodes the plain text passwd in base64.
	 * 
	 * @return the encoded passwd
	 */
	public final String getBase64EncodedPassword() {
		return Base64.getEncoder().encodeToString(getPassword().getBytes());
	}

	/**
	 * Gets the site name, to be used as a descri of where the router is.
	 * 
	 * @return The name of the site
	 */
	public final String getSiteName() {
		return siteName;
	}

	/**
	 * Gets the site name, to be used as a descri of where the router is.
	 * 
	 * @param siteNme
	 *            The site name.
	 */
	public final void setSiteName(final String siteNme) {
		this.siteName = siteNme;
	}

	/**
	 * Gets the router model.
	 * 
	 * @return The model.
	 * @see ERouterModels
	 */
	public final ERouterModels getRouterModel() {
		return routerModel;
	}

	/**
	 * Sets the current router model.
	 * 
	 * @param routerMdel
	 *            The model.
	 * @see ERouterModels
	 */
	public final void setRouterModel(final ERouterModels routerMdel) {
		this.routerModel = routerMdel;
	}

	@Override
	public final String toString() {
		return getRouterModel().name() + getSiteName() + getUsername()
				+ getPassword() + getConnectionAddresses().toString();

	}

}
