package vigorBackup.model;

import java.net.URL;

/**
 * Address that can be used to access the router. 
 */
public class Address {
	//This class is prepared to be an entity in hibernate.
	/**
	 * Address id.
	 */
	private Long id;
	/**
	 * The address url.
	 */
	private URL address;
	/**
	 * The router that this address belongs to.
	 */
	private Router router;

	/**
	 * Gets the address.
	 * 
	 * @return The address.
	 */
	public final URL getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param addr
	 *            The address to be set.
	 */
	public final void setAddress(final URL addr) {
		this.address = addr;
	}

	/**
	 * Gets the associated router.
	 * 
	 * @return The router.
	 */
	public final Router getRouter() {
		return router;
	}

	/**
	 * Sets the router.
	 * @param routr
	 *            The router to be set.
	 */
	public final void setRouter(final Router routr) {
		this.router = routr;
	}

	/**
	 * Gets the address id.
	 * @return The id.
	 */
	public final Long getId() {
		return id;
	}

}
