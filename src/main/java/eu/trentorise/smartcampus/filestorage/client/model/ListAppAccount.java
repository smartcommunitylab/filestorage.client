package eu.trentorise.smartcampus.filestorage.client.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Utility class to be complaint with internal protocol of list representation
 * 
 * @author mirko perillo
 * 
 */
@XmlRootElement(name = "appAccounts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListAppAccount {

	@XmlElement(name = "appAccount")
	private List<AppAccount> appAccounts;

	public List<AppAccount> getAppAccounts() {
		return appAccounts;
	}

	public void setAppAccounts(List<AppAccount> appAccounts) {
		this.appAccounts = appAccounts;
	}

}
