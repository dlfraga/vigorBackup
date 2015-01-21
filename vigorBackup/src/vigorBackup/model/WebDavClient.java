package vigorBackup.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.sardine.impl.SardineException;
/**
 * @see https://github.com/lookfirst/sardine/wiki/UsageGuide
 */
public class WebDavClient {

	public WebDavClient() {
		Sardine sardine = SardineFactory.begin("w", "sm");
		
		try {
			String address = "htt";
			URL url = new URL(address);
			
			List<DavResource> resources = sardine.list(url.toURI().toASCIIString());
			for (DavResource davResource : resources) {
				System.out.println(davResource);
			}
			sardine.createDirectory(address + "/Teste-Webdav2");
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SardineException e){
			//TODO: No permission / directory exists
			System.out.println("Directory exists");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
