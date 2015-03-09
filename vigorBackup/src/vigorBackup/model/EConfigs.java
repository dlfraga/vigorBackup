package vigorBackup.model;

/**
 * This enum takes care of the config file contents. It is used to keep the key
 * strings always constant in the program.
 */
public enum EConfigs {
	/**
	 * The backup directory, where all backup files will be stored.
	 */
	ROOT_DIRECTORY("backup.directory", "c:/backupsVigor/"),
	/**
	 * Days to keep old backup files.
	 */
	DAYS_TO_KEEP("days.to.keep.backups", "30"),
	/**
	 * The routers CSV file. It will be read on runtime.
	 * 
	 * @see LoadFromCSV
	 */
	ROUTER_LIST_FILE("routers.list.file", "routers.csv"),
	/**
	 * SMTP host that will be used to send e-mail reports.
	 */
	SMTP_HOST("mail.smtp.host", "smtp.changeme.com"),
	/**
	 * Checks if smtp auth is needed.
	 */
	IS_SMTP_AUTH_NEEDED("mail.smtp.auth", "true"),
	/**
	 * Checks if the program is in debug mode where some stacktraces will be
	 * shown.
	 */
	IS_DEBUG_ON("debug.mode", "true"),
	/**
	 * Checks if we need to enable SSL to send e-mail.
	 */
	IS_SMTP_SSL_ENABLED("mail.smtp.ssl.enable", "true"),
	/**
	 * The e-mail address to send e-mail to.
	 */
	SMTP_TO_EMAIL("mail.smtp.to", "first@changeme.com, second@changeme.com"),
	/**
	 * The address that the e-mail is sent from.
	 */
	SMTP_FROM_EMAIL("mail.smtp.from", "from@changeme.com.br"),
	/**
	 * The username to logon to send e-mail.
	 */
	SMTP_LOGIN_USERNAME("mail.smtp.username", "login@changeme.com"),
	/**
	 * The smtp password.
	 */
	SMTP_PASSWORD("mail.smtp.password", "mySecureP@ssw0rd"),
	/**
	 * The smtp server port.
	 */
	SMTP_PORT("mail.smtp.port", "465"),
	/**
	 * The webdav server address.
	 */
	WEBDAV_ADDRESS("webdav.server", "http://webdav.server.com"),
	/**
	 * The webdav server username.
	 */
	WEBDAV_USERNAME("webdav.username", "login@webdavserver.com"),
	/**
	 * The webdav server password.
	 */
	WEBDAV_PASSWORD("webdav.password", "MyWebDavPassword"),
	/**
	 * The CSV routerFile separator.
	 */
	CSV_FILE_SEPARATOR("csv.router.file.separator", ";");

	/**
	 * The string that will be used to load the values from the config files.
	 */
	private String configKey;
	/**
	 * The default value that will be inserted when creating config files.
	 */
	private String defaultValue;

	/**
	 * Enum's construtor.
	 * 
	 * @param confString
	 *            The key to the configuration file.
	 * @param defaultVlue
	 *            The default value to the configuration file.
	 */
	private EConfigs(final String confString, final String defaultVlue) {
		this.configKey = confString;
		this.defaultValue = defaultVlue;
	}

	/**
	 * Gets the code that is used on the configation file. It's the first
	 * parameter on the enum value's creation @see {@link EConfigs}.
	 * 
	 * @return An String with the config code.
	 */
	public String getKey() {
		return configKey;
	}

	/**
	 * Gets the default value that it's used on the default configuration file
	 * creation. It's only use is to help whoever is configuring the program.
	 * It's the second parameter on the enum value's creation @see
	 * {@link EConfigs}.
	 * 
	 * @return An String with the default configuration value.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

}
