package vigorBackup.model;

/**
 * This enum takes care of the config file contents. It is used to keep the key
 * strings always constant in the program.
 */
public enum EPropsConfigFile {
	/**
	 * The backup directory, where all backup files will be stored.
	 */
	ROOT_DIRECTORY("backup.directory", "c:/backupsVigor/"),
	/**
	 * Days to keep old backup files.
	 */
	DAYS_TO_KEEP_FILES("days.to.keep.backups", "30"),
	/**
	 * The routers CSV file. It will be read on runtime.
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
	 * Checks if the smtp connection is on debug mode. If it is debug data will
	 * be shown on the sysout.
	 */
	IS_SMTP_DEBUG_ON("mail.smtp.debug", "true"),
	/**
	 * Checks if we need to enable SSL to send e-mail.
	 */
	IS_SMTP_SSL_ENABLED("mail.smtp.ssl.enable", "true"),
	/**
	 * The e-mail address to send e-mail to.
	 */
	SMTP_TO_EMAIL("mail.smtp.to", "destination@changeme.com"),
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
	SMTP_PORT("mail.smtp.port", "465");

	private String configString;
	private String defaultValue;

	private EPropsConfigFile(String confString, String defaultVlue) {
		this.configString = confString;
		this.defaultValue = defaultVlue;
	}

	public String getConfigCode() {
		return configString;
	}
	
	public String getDefaultValue(){
		return defaultValue;
	}
	

}
