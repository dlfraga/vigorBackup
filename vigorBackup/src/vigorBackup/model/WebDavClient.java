package vigorBackup.model;

import java.io.IOException;
import java.util.List;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
/**
 * @see https://github.com/lookfirst/sardine/wiki/UsageGuide
 */
public class WebDavClient {

	public WebDavClient() {
		Sardine sardine = SardineFactory.begin("webdav", "***REMOVED***");
		
		try {
			List<DavResource> resources = sardine.list("http://intranet");
			for (DavResource davResource : resources) {
				System.out.println(davResource);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
