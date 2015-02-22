package vigorBackup.model;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * This enum holds all router types to be used in the application. It is also
 * used as a factory.
 */
public enum ERouterModels {
	/**
	 * The router types. The constructor values are used later in importing
	 * data.
	 */
	VIGOR_2925(0), VIGOR_2910(1), VIGOR_3300(2), VIGOR_2920(3), VIGOR_3200(4);
	/**
	 * Model code that is used to read CSV files.
	 */
	private int modelCode;

	/**
	 * List of supported routers.
	 * 
	 * @param modelType
	 *            The model code, defined manually.
	 */
	private ERouterModels(final int modelType) {
		this.modelCode = modelType;
	}

	/**
	 * Map that will be used when looking up modelcode indexes.
	 */
	private static HashMap<Integer, ERouterModels> lookUp = new HashMap<>();
	/**
	 * Creates the map with the previously set values
	 */
	static {
		for (ERouterModels emodel : EnumSet.allOf(ERouterModels.class)) {
			lookUp.put(emodel.getCode(), emodel);
		}
	}

	/**
	 * Gets the model code for the choosen type.
	 * 
	 * @return The model code.
	 */
	public int getCode() {
		return modelCode;
	}

	/**
	 * Looks up the model code in the available types.
	 * 
	 * @param code
	 *            The code to be looked up.
	 * @return The type that has been found.
	 */
	public static ERouterModels get(final int code) {
		return lookUp.get(code);
	}

	/**
	 * Creates a new downloader object based on the enums types.
	 * 
	 * @param router
	 *            The default router that will be used on the downloader.
	 * @return The downloader that will be later used to save the firmware.
	 */
	public static BaseDownloader returnDownloader(final Router router) {
		switch (router.getRouterModel()) {
		case VIGOR_2910:
			return new Vigor2910(router);
		case VIGOR_2920:
			return new Vigor2920(router);
		case VIGOR_2925:
			return new Vigor2925(router);
		case VIGOR_3200:
			return new Vigor3200(router);
		case VIGOR_3300:
			return new Vigor3300(router);
		default:
			throw new IllegalArgumentException();
		}
	}

}
