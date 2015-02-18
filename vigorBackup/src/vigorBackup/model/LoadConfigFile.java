package vigorBackup.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadConfigFile {
	/**
	 * Root directory to save downloaded files. It's loaded from the config file
	 */
	public static String ROOT_DIRECTORY;
	/**
	 * Days to keep the backup files.
	 */
	public static int DAYS_TO_KEEP_FILES;
	/**
	 * CSV router list
	 * 
	 * @see LoadFromCSV
	 */
	public static String ROUTER_LIST_FILE;
	/**
	 * Must be true to enable the email report funcionality.
	 */
	public static boolean IS_EMAIL_ENABLED;
	/**
	 * The smtp host to send emails.
	 */
	public static String SMTP_HOST;
	/**
	 * Must be true if the smtp server requires authentication.
	 */
	public static boolean IS_SMTP_AUTH_NEEDED;
	/**
	 * Enabled debug mode for smtp. Must be enabled if errors are encountered.
	 */
	public static boolean IS_SMTP_DEBUG_ON;
	/**
	 * Must be true to enable SSL on SMTP transaction.
	 */
	public static boolean IS_SMTP_SSL_ENABLED;
	/**
	 * The "TO" address in the email.
	 */
	public static String SMTP_TO_EMAIL;
	/**
	 * The "FROM" address in the e-mail;
	 */
	public static String SMTP_FROM_EMAIL;
	/**
	 * The username used to authenticate on the smtp server.
	 */
	public static String SMTP_LOGIN_USERNAME;
	/**
	 * The passsord to the smtp server.
	 */
	public static String SMTP_PASSWORD;
	/**
	 * The smtp server port.
	 */
	public static int SMTP_PORT;
	/**
	 * The webdav server address.
	 */
	public static String WEBDAV_ADDRESS;
	/**
	 * The webdav server username.
	 */
	public static String WEBDAV_USERNAME;
	/**
	 * The webdav server password.
	 */
	public static String WEBDAV_PASSWORD;

	/**
	 * Default configuration filename that the program will try to load on
	 * startup, if we don't receive a string on the program args.
	 */
	private static final String DEFAULT_CONFIG_FILENAME = "configs.properties";
	/**
	 * Properties that are loaded from the config file.
	 */
	private static Properties props;

	public static void loadConfigFile(String[] args) {
		File configFile = null;
		if (args == null || args.length == 0 || args[0].equalsIgnoreCase("")) {
			configFile = new File(DEFAULT_CONFIG_FILENAME);
			if (!configFile.exists()) {
				System.out
						.println("You didn't specify a config file location and I \n"
								+ "didn't find one on the current directory. \n"
								+ "I'm going to create a default one for you to customize");
				createDefaultConfigFile();
				System.exit(1);
			}

		} else {
			configFile = new File(args[0]);
		}
		props = new Properties();
		try {
			props.load(new FileInputStream(configFile));
		} catch (IOException e) {
			System.out
					.println("The specified config file could not be found \n"
							+ "To create a default one start the program without arguments");
			System.exit(1);
		}
		// TODO: Find a better way to deal with the Enum and the static
		// variables here, this repeated code is sad. Maybe use the owner API?
		// http://owner.aeonbits.org/docs/welcome/
		ROOT_DIRECTORY = props.getProperty(EConfigs.ROOT_DIRECTORY
				.getConfigCode());
		DAYS_TO_KEEP_FILES = Integer.parseInt(props
				.getProperty(EConfigs.DAYS_TO_KEEP_FILES.getConfigCode()));
		SMTP_HOST = props.getProperty(EConfigs.SMTP_HOST.getConfigCode());
		IS_SMTP_AUTH_NEEDED = Boolean.parseBoolean(props
				.getProperty(EConfigs.IS_SMTP_AUTH_NEEDED.getConfigCode()));
		IS_SMTP_DEBUG_ON = Boolean.parseBoolean(props
				.getProperty(EConfigs.IS_SMTP_DEBUG_ON.getConfigCode()));
		IS_SMTP_SSL_ENABLED = Boolean.parseBoolean(props
				.getProperty(EConfigs.IS_SMTP_SSL_ENABLED.getConfigCode()));
		SMTP_TO_EMAIL = props.getProperty(EConfigs.SMTP_TO_EMAIL
				.getConfigCode());
		SMTP_FROM_EMAIL = props.getProperty(EConfigs.SMTP_FROM_EMAIL
				.getConfigCode());
		SMTP_PASSWORD = props.getProperty(EConfigs.SMTP_PASSWORD
				.getConfigCode());
		SMTP_LOGIN_USERNAME = props.getProperty(EConfigs.SMTP_LOGIN_USERNAME
				.getConfigCode());
		SMTP_PORT = Integer.parseInt(props.getProperty(EConfigs.SMTP_PORT
				.getConfigCode()));
		
		WEBDAV_ADDRESS = props.getProperty(EConfigs.WEBDAV_ADDRESS
				.getConfigCode());
		if (!WEBDAV_ADDRESS.endsWith("/"))
			WEBDAV_ADDRESS += "/";
		WEBDAV_ADDRESS = WEBDAV_ADDRESS.replaceAll(" ","%20");

		WEBDAV_PASSWORD = props.getProperty(EConfigs.WEBDAV_PASSWORD
				.getConfigCode());
		WEBDAV_USERNAME = props.getProperty(EConfigs.WEBDAV_USERNAME
				.getConfigCode());
	}

	private static void createDefaultConfigFile() {
		StringBuilder sampleData = new StringBuilder();
		for (int i = 0; i < EConfigs.values().length; i++) {
			sampleData.append(EConfigs.values()[i].getConfigCode() + " = "
					+ EConfigs.values()[i].getDefaultValue() + "\n");
		}
		File configFileToBeCreated = new File(DEFAULT_CONFIG_FILENAME);
		try {
			configFileToBeCreated.createNewFile();
			FileOutputStream fio = new FileOutputStream(configFileToBeCreated);
			fio.write(sampleData.toString().getBytes());
			fio.close();
			System.out.println("Created a sample configuration file on "
					+ configFileToBeCreated.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Could not create the sample configuation file");
		}

	}

}
