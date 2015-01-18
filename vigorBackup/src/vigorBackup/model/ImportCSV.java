package vigorBackup.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImportCSV {
	private BufferedReader bufR;
	private String csvFile = "routers.csv";
	private String csvSpitBy = ",";
	private String line = "";
	private List<Router> routerList;
	private static final int MODEL_CODE_INDEX = 0;
	private static final int SITE_NAME_INDEX = 1;
	private static final int DESCRIPTION_INDEX = 2;
	private static final int USERNAME_INDEX = 3;
	private static final int PASSWORD_INDEX = 4;
	private static final int ADDRESS_INDEX = 5;

	public ImportCSV() {
		routerList = new ArrayList<>();
	}

	public List<Router> loadCsv() {
		try {
			bufR = new BufferedReader(new FileReader(csvFile));
			while ((line = bufR.readLine()) != null) {
				String[] splittenLine = line.split(csvSpitBy);
				// Ignore the first line if it's the csv header
				if (splittenLine[MODEL_CODE_INDEX].equalsIgnoreCase("modelCode")) {
					continue;
				}
				Router router = new Router();
				router.setModelCode(Integer
						.parseInt(splittenLine[MODEL_CODE_INDEX]));
				router.setSiteName(splittenLine[SITE_NAME_INDEX]);
				router.setDescription(splittenLine[DESCRIPTION_INDEX]);
				router.setUsername(splittenLine[USERNAME_INDEX]);
				router.setPassword(splittenLine[PASSWORD_INDEX]);
				List<Address> addressList = new ArrayList<>();
				int index = ADDRESS_INDEX;
				do {
					Address address = new Address();
					address.setAddress(new URL(splittenLine[index]));
					addressList.add(address);
					index++;

				} while (splittenLine.length > index);
				router.setConnectionAddresses(addressList);
				routerList.add(router);
				router = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufR != null) {
				try {
					bufR.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return routerList;
	}
}
