package vigorBackup.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Router {
	@Id
	@GeneratedValue
	private Long id;
	private String description;
	private boolean isOk;
	@Temporal(TemporalType.DATE)
	private Calendar lastBackupDate;
	@OneToMany(mappedBy="router")
	private List<Address> connectionAddresses;

	public Router() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public Calendar getLastBackupDate() {
		return lastBackupDate;
	}

	public void setLastBackupDate(Calendar lastBackupDate) {
		this.lastBackupDate = lastBackupDate;
	}

	public Long getId() {
		return id;
	}

	public List<Address> getConnectionAddresses() {
		return connectionAddresses;
	}

	public void setConnectionAddresses(ArrayList<Address> connectionAddresses) {
		this.connectionAddresses = connectionAddresses;
	}
	
	
}
