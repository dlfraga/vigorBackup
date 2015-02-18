package vigorBackup.model;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Loads router data from a CSV file.
 */
public class LoadFromCSV {
	// TODO: Give the user an FileChooser to open whatever csv file he wants
	private String csvFile = "routers.csv";
	private List<Router> routerList;
	private static final char CSV_SEPARATOR = ';';
	/**
	 * Indexes of the CSV file. The 0 index is in the leftmost position.
	 */
	private static final int MODEL_CODE_INDEX = 0;
	private static final int SITE_NAME_INDEX = 1;
	private static final int DESCRIPTION_INDEX = 2;
	private static final int USERNAME_INDEX = 3;
	private static final int PASSWORD_INDEX = 4;
	private static final String[] CSV_COLUMN_NAMES = { "modelCode", "siteName",
			"description", "userName", "passWord", "mainAddress",
			"secondaryAddress" };
	/**
	 * The file can have one or more addresses. All columns after the main
	 * address are treated as additional addresses that can be used to download
	 * the backups.
	 */
	private static final int MAIN_ADDRESS_INDEX = 5;

	public LoadFromCSV() {
		routerList = new ArrayList<>();
	}

	/**
	 * Loads the routers from a CSV file.
	 * 
	 * @return a list with all the routers that could be read from the file.
	 */
	public List<Router> loadCsv() {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(csvFile),
					CSV_SEPARATOR);
			List<String[]> entries = csvReader.readAll();
			csvReader.close();
			for (String[] splittenLine : entries) {

				// Ignore the first line if it's the csv header
				if (splittenLine[MODEL_CODE_INDEX]
						.equalsIgnoreCase(CSV_COLUMN_NAMES[0])) {
					continue;
				} else if (splittenLine[MODEL_CODE_INDEX].equalsIgnoreCase("")) {
					continue;
				}
				Router router = new Router();
				int modelCode = Integer.parseInt(splittenLine[MODEL_CODE_INDEX]);
				//Skip lines with wrong model coded
				if(modelCode < 0 || modelCode >= ERouterModels.values().length){
					continue;
				} else {
					router.setRouterModel(ERouterModels.get(modelCode));
				}
				
				router.setSiteName(splittenLine[SITE_NAME_INDEX]);
				router.setDescription(splittenLine[DESCRIPTION_INDEX]);
				router.setUsername(splittenLine[USERNAME_INDEX]);
				router.setPassword(splittenLine[PASSWORD_INDEX]);
				List<Address> addressList = new ArrayList<>();
				int index = MAIN_ADDRESS_INDEX;
				do {
					Address address = new Address();
					try {
						// Ignore malformed addresses or excel csv format quirks
						address.setAddress(new URL(splittenLine[index]));
						addressList.add(address);
					} catch (Exception e) {
						// TODO: Log malformed address problem
						
					}
					index++;

				} while (splittenLine.length > index);
				router.setConnectionAddresses(addressList);
				routerList.add(router);
				router = null;

			}

		} catch (IOException e) {
			// TODO The file could not be read. Inform user
			e.printStackTrace();
		}

		return routerList;
	}
}
