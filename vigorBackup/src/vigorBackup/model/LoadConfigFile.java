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
	 * Fields used to send emails.
	 */
	public static boolean IS_EMAIL_ENABLED;
	public static String SMTP_HOST;
	public static boolean IS_SMTP_AUTH_NEEDED;
	public static boolean IS_SMTP_DEBUG_ON;
	public static boolean IS_SMTP_SSL_ENABLED;
	public static String SMTP_TO_EMAIL;
	public static String SMTP_FROM_EMAIL;
	public static String SMTP_LOGIN_USERNAME;
	public static String SMTP_PASSWORD;
	public static int SMTP_PORT;

	/**
	 * Default configuration filename that the program will try to load on
	 * startup, if we don't receive a string on the program args.
	 */
	private static final String DEFAULT_CONFIG_FILENAME = "configs.properties";
	/**
	 * Properties that are loaded from the config file
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
		
		ROOT_DIRECTORY = props.getProperty(EPropsConfigFile.ROOT_DIRECTORY
				.getConfigCode());
		
		DAYS_TO_KEEP_FILES = Integer.parseInt(props
				.getProperty(EPropsConfigFile.DAYS_TO_KEEP_FILES
						.getConfigCode()));
		SMTP_HOST = props.getProperty(EPropsConfigFile.SMTP_HOST
				.getConfigCode());
		IS_SMTP_AUTH_NEEDED = Boolean.parseBoolean(props
				.getProperty(EPropsConfigFile.IS_SMTP_AUTH_NEEDED
						.getConfigCode()));
		IS_SMTP_DEBUG_ON = Boolean
				.parseBoolean(props
						.getProperty(EPropsConfigFile.IS_SMTP_DEBUG_ON
								.getConfigCode()));
		IS_SMTP_SSL_ENABLED = Boolean.parseBoolean(props
				.getProperty(EPropsConfigFile.IS_SMTP_SSL_ENABLED
						.getConfigCode()));
		SMTP_TO_EMAIL = props.getProperty(EPropsConfigFile.SMTP_TO_EMAIL
				.getConfigCode());
		SMTP_FROM_EMAIL = props.getProperty(EPropsConfigFile.SMTP_FROM_EMAIL
				.getConfigCode());
		SMTP_PASSWORD = props.getProperty(EPropsConfigFile.SMTP_PASSWORD
				.getConfigCode());
		SMTP_LOGIN_USERNAME = props
				.getProperty(EPropsConfigFile.SMTP_LOGIN_USERNAME
						.getConfigCode());
		SMTP_PORT = Integer.parseInt(props
				.getProperty(EPropsConfigFile.SMTP_PORT.getConfigCode()));

	}

	private static void createDefaultConfigFile() {
		StringBuilder sampleData = new StringBuilder();
		for (int i = 0; i < EPropsConfigFile.values().length; i++) {
			sampleData.append(EPropsConfigFile.values()[i].getConfigCode()
					+ " = " + EPropsConfigFile.values()[i].getDefaultValue()
					+ "\n");
		}
		File configFileToBeCreated = new File(DEFAULT_CONFIG_FILENAME);
		try {
			configFileToBeCreated.createNewFile();
			FileOutputStream fio = new FileOutputStream(configFileToBeCreated);
			fio.write(sampleData.toString().getBytes());
			fio.close();
			System.out.println("Created a sample configuration file");
		} catch (IOException e) {
			System.out.println("Could not create the sample configuation file");
		}

	}

}
