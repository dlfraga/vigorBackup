package vigorBackup.factory;

import vigorBackup.model.DefaultRouterWebDownloader;
import vigorBackup.model.Router;
import vigorBackup.model.Vigor2910;
import vigorBackup.model.Vigor2925;

public class RouterDownloaderFactory {
	public static final int VIGOR_2925 = 0;
	public static final int VIGOR_2910 = 1;
	
	
	public DefaultRouterWebDownloader getDownloader(int routerIndex, Router router){			
		if(routerIndex == VIGOR_2910){
			return new Vigor2910(router);
		}
		if(routerIndex == VIGOR_2925){
			return new Vigor2925(router);
		}
		
		return null;
	}

}
