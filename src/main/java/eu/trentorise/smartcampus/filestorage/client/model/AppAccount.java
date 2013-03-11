package eu.trentorise.smartcampus.filestorage.client.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <i>ApplicationAccount</i> defines all the informations about a storage
 * application account. A storage application account defines a type of storage
 * for a defined application. Every storage application account can be related
 * to many user storage accounts.
 * 
 * @author mirko perillo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AppAccount {
	/**
	 * id of the application account
	 */
	private String id;
	/**
	 * application name binded to the application storage account
	 */
	private String appName;
	/**
	 * name to represent the account
	 */
	private String appAccountName;
	/**
	 * type of the storage. See {@link StorageType} for supported storages
	 */
	private StorageType storageType;

	@XmlElementWrapper
	@XmlElement(name = "configuration")
	private List<Configuration> configurations;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppAccountName() {
		return appAccountName;
	}

	public void setAppAccountName(String appAccountName) {
		this.appAccountName = appAccountName;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

}
