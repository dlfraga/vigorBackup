package vigorBackup.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class is responsible for loading the config file. The static fields in
 * it are used by other classes.
 */
public final class Configs {
	/**
	 * Creates a map object that used the EConfigs as keys and the
	 * configurations as its values.
	 */
	private static Map<EConfigs, Object> confs = new EnumMap<>(EConfigs.class);
	/**
	 * Default configuration filename that the program will try to load on
	 * startup, if we don't receive a string on the program args.
	 */
	private static final String DEFAULT_CONFIG_FILENAME = "vigorBackup.ini";
	/**
	 * Properties that are loaded from the config file.
	 */
	private static Properties props;

	/**
	 * Loads the config file from disk.
	 * 
	 * @param args
	 *            The args received from the main method, including the config
	 *            file path.
	 */
	public static void loadConfigFile(final String[] args) {
		File configFile = null;
		if (args == null || args.length == 0 || args[0].equalsIgnoreCase("")) {
			configFile = new File(DEFAULT_CONFIG_FILENAME);
			if (!configFile.exists()) {
				System.out.println("You didn't specify a config "
						+ "file location and I \n" + "didn't find one on the "
						+ "current directory. \n"
						+ "I'm going to create a default "
						+ "one for you to customize");
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
			System.out.println("The specified config "
					+ "file could not be found \n" + "To create a default one "
					+ "start the program without arguments");
			System.exit(1);
		}

		// Load and validate the root directory
		File rootDirFile = null;
		String rootDir = props.getProperty(EConfigs.ROOT_DIRECTORY.getKey());
		try {
			rootDirFile = new File(rootDir);
			if (!rootDirFile.canWrite()) {
				throw new IOException();
			}
		} catch (NullPointerException e) {
			System.out.println("Root directory was not specified");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Can't write to the specified path");
			System.exit(1);
		}
		// saves the config the the map
		confs.put(EConfigs.ROOT_DIRECTORY, rootDirFile);

		// Load and validate the days to keep files
		int daysToKeep = 0;

		try {
			String days = props.getProperty(EConfigs.DAYS_TO_KEEP.getKey());
			daysToKeep = Integer.parseInt(days);
		} catch (NumberFormatException e) {
			System.out.println("Invalid days to keep files");
			System.exit(1);
		}
		// saves the config to the map
		confs.put(EConfigs.DAYS_TO_KEEP, daysToKeep);

		// Loads the smtp host address
		confs.put(EConfigs.SMTP_HOST,
				props.getProperty(EConfigs.SMTP_HOST.getKey()));

		confs.put(EConfigs.IS_SMTP_AUTH_NEEDED, Boolean.parseBoolean(props
				.getProperty(EConfigs.IS_SMTP_AUTH_NEEDED.getKey())));

		confs.put(EConfigs.IS_DEBUG_ON, Boolean.parseBoolean(props
				.getProperty(EConfigs.IS_DEBUG_ON.getKey())));

		confs.put(EConfigs.IS_SMTP_SSL_ENABLED, Boolean.parseBoolean(props
				.getProperty(EConfigs.IS_SMTP_SSL_ENABLED.getKey())));

		confs.put(EConfigs.SMTP_TO_EMAIL,
				props.getProperty(EConfigs.SMTP_TO_EMAIL.getKey()));

		confs.put(EConfigs.SMTP_FROM_EMAIL,
				props.getProperty(EConfigs.SMTP_FROM_EMAIL.getKey()));

		confs.put(EConfigs.SMTP_PASSWORD,
				props.getProperty(EConfigs.SMTP_PASSWORD.getKey()));

		confs.put(EConfigs.SMTP_LOGIN_USERNAME,
				props.getProperty(EConfigs.SMTP_LOGIN_USERNAME.getKey()));

		confs.put(EConfigs.SMTP_PORT, Integer.parseInt(props
				.getProperty(EConfigs.SMTP_PORT.getKey())));

		confs.put(EConfigs.ROUTER_LIST_FILE,
				props.getProperty(EConfigs.ROUTER_LIST_FILE.getKey()));

		// Read webdav address
		String webDavAddr = props.getProperty(EConfigs.WEBDAV_ADDRESS.getKey());
		if (!webDavAddr.endsWith("/")) {
			webDavAddr += "/";
		}
		webDavAddr = webDavAddr.replaceAll(" ", "%20");
		confs.put(EConfigs.WEBDAV_ADDRESS, webDavAddr);

		confs.put(EConfigs.WEBDAV_PASSWORD,
				props.getProperty(EConfigs.WEBDAV_PASSWORD.getKey()));
		confs.put(EConfigs.WEBDAV_USERNAME,
				props.getProperty(EConfigs.WEBDAV_USERNAME.getKey()));
		confs.put(
				EConfigs.CSV_FILE_SEPARATOR,
				props.getProperty(EConfigs.CSV_FILE_SEPARATOR.getKey()).charAt(
						0));
	}

	/**
	 * Creates a default configuration file if one wasn't specified or found. It
	 * uses the default values that can be found in {@link EConfigs}.
	 */
	private static void createDefaultConfigFile() {
		StringBuilder sampleData = new StringBuilder();
		for (int i = 0; i < EConfigs.values().length; i++) {
			sampleData.append(EConfigs.values()[i].getKey() + " = "
					+ EConfigs.values()[i].getDefaultValue() + "\r\n");
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

	/**
	 * Gets a configuration that was loaded from the config file. The output
	 * must be cast to the needed object.
	 * 
	 * @param econfig
	 *            The configuration wanted. All parameters can be seen on
	 *            {@link EConfigs}.
	 * @return The configuration, as an instance of the class object. It must be
	 *         cast to the desired type.
	 */
	public static Object getConfig(final EConfigs econfig) {
		return confs.get(econfig);
	}

	/**
	 * This class should not be instantiated.
	 */
	private Configs() {
		// not used
	}

}
