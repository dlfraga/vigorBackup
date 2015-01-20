package vigorBackup.factory;

import vigorBackup.model.DefaultRouterWebDownloader;
import vigorBackup.model.Router;
import vigorBackup.model.Vigor2910;
import vigorBackup.model.Vigor2920;
import vigorBackup.model.Vigor2925;
import vigorBackup.model.Vigor3200;
import vigorBackup.model.Vigor3300;

public class RouterDownloaderFactory {
	public static final int VIGOR_2925 = 0;
	public static final int VIGOR_2910 = 1;
	public static final int VIGOR_3300 = 2;
	public static final int VIGOR_2920 = 3;
	public static final int VIGOR_3200 = 4;

	public DefaultRouterWebDownloader getDownloader(int routerIndex,
			Router router) {
		if (routerIndex == VIGOR_2910)
			return new Vigor2910(router);
		if (routerIndex == VIGOR_2925)
			return new Vigor2925(router);
		if (routerIndex == VIGOR_3300)
			return new Vigor3300(router);
		if (routerIndex == VIGOR_2920)
			return new Vigor2920(router);
		if (routerIndex == VIGOR_3200)
			return new Vigor3200(router);

		return null;
	}

}
