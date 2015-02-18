package vigorBackup.model;
import java.net.URL;


//@Entity
public class Address {
//	@Id
//	@GeneratedValue
	private Long id;
	private URL address;
//	@OneToOne
	private Router router;

	public URL getAddress() {
		return address;
	}

	public void setAddress(URL address) {
		this.address = address;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public Long getId() {
		return id;
	}

}
