package vigorBackup.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Loads router data from a CSV file.
 */
public final class LoadFromCSV {
	/**
	 * Router list that will be built as we read the CSV file.
	 */
	private static List<Router> routerList = new ArrayList<>();
	/**
	 * The default csv filename we will try to read the data from.
	 */
	private static final String DEFAULT_CSV_FILENAME = "routers.csv";

	/**
	 * Loads the routers from a CSV file.
	 * 
	 * @return a list with all the routers that could be read from the file.
	 */
	public static List<Router> loadCsv() {
		CSVReader csvReader;
		List<String[]> entries = null;
		try {
			FileReader file = new FileReader(LoadConfigFile.ROUTER_LIST_FILE);
			csvReader = new CSVReader(file, LoadConfigFile.CSV_FILE_SEPARATOR);
			entries = csvReader.readAll();
			csvReader.close();
		} catch (IOException e) {
			System.out.println("Router CSV list not found. "
					+ "Creating a default one...");
			createDefaultCsvFile();
		}

		for (String[] line : entries) {
			Router router = new Router();
			// Ignore the first line if it's the csv header
			if (line[ECsvItens.MODEL_CODE.getIndex()]
					.equalsIgnoreCase(ECsvItens.MODEL_CODE.getName())) {
				continue;
			} else if (line[ECsvItens.MODEL_CODE.getIndex()]
					.equalsIgnoreCase("")) {
				continue;
			}

			int modelCode = Integer.parseInt(line[ECsvItens.MODEL_CODE
					.getIndex()]);
			// Skip lines with wrong model coded
			if (modelCode < 0 || modelCode >= ERouterModels.values().length) {
				continue;
			} else {
				router.setRouterModel(ERouterModels.get(modelCode));
			}

			router.setSiteName(line[ECsvItens.SITE_NAME.getIndex()]);
			router.setDescription(line[ECsvItens.DESCRIPTION.getIndex()]);
			router.setUsername(line[ECsvItens.USER_NAME.getIndex()]);
			router.setPassword(line[ECsvItens.PASSWORD.getIndex()]);
			List<Address> addressList = new ArrayList<>();
			int index = ECsvItens.MAIN_ADDRESS.getIndex();
			do {
				Address address = new Address();
				try {
					// Ignore malformed addresses or excel csv format quirks
					address.setAddress(new URL(line[index]));
					addressList.add(address);
				} catch (Exception e) {
					if (LoadConfigFile.IS_SMTP_DEBUG_ON) {
						System.out.println("Invalid address in CSV:"
								+ line[index] + "\n");
					}
				}
				index++;

			} while (line.length > index);
			router.setConnectionAddresses(addressList);
			routerList.add(router);
			router = null;
		}

		return routerList;
	}

	/**
	 * Creates a default CSV file that can be customized by the user. It has
	 * only the header line.
	 */
	private static void createDefaultCsvFile() {
		StringBuilder sampleData = new StringBuilder();
		for (int i = 0; i < ECsvItens.values().length; i++) {
			sampleData.append(ECsvItens.values()[i].getName() + ";");
		}
		sampleData.append("\n");
		File csvFile = new File(DEFAULT_CSV_FILENAME);
		try {
			csvFile.createNewFile();
			FileOutputStream fio = new FileOutputStream(csvFile);
			fio.write(sampleData.toString().getBytes());
			fio.close();
			System.out.println("Created a sample CSV file on "
					+ csvFile.getAbsolutePath());
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Could not create the sample CSV");
			System.exit(1);
		}

	}

	/**
	 * Enumerates all fields that will be read from the CSV file. This is also
	 * used to create a default CSV that can be customized by the user.
	 */
	private enum ECsvItens {
		/**
		 * The model code, as indexed on {@link ERouterModels}.
		 */
		MODEL_CODE(0, "modelCode"),
		/**
		 * The name of where the router is.
		 */
		SITE_NAME(1, "siteName"),
		/**
		 * Additional description.
		 */
		DESCRIPTION(2, "description"),
		/**
		 * The username to logon on the router.
		 */
		USER_NAME(3, "userName"),
		/**
		 * The password to logon on the router.
		 */
		PASSWORD(4, "password"),
		/**
		 * The file can have one or more addresses. All columns after the main
		 * address are treated as additional addresses that can be used to
		 * download the backups.
		 */
		MAIN_ADDRESS(5, "mainAddress");

		/**
		 * The index of the CSV column.
		 */
		private int index;
		/**
		 * The CSV column name.
		 */
		private String name;

		/**
		 * Enum constructor.
		 * 
		 * @param indx
		 *            CSV File index.
		 * @param nme
		 *            CSV column name.
		 */
		private ECsvItens(final int indx, final String nme) {
			this.index = indx;
			this.name = nme;
		}

		/**
		 * Gets the columns name from this enum.
		 * 
		 * @return Column name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the CSV column index.
		 * 
		 * @return The CSV column index.
		 */
		public int getIndex() {
			return index;
		}

	};

	/**
	 * This class should not be instantiated.
	 */
	private LoadFromCSV() {
		// Not used
	}
}
