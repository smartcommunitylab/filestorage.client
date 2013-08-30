/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.filestorage.client.model;

import java.util.List;

/**
 * <i>ApplicationAccount</i> defines all the informations about a storage
 * application account. A storage application account defines a type of storage
 * for a defined application. Every storage application account can be related
 * to many user storage accounts.
 * 
 * @author mirko perillo
 * 
 */
public class Storage {
	/**
	 * id of the application account
	 */
	private String id;
	/**
	 * application name binded to the application storage account
	 */
	private String appId;
	/**
	 * name to represent the account
	 */
	private String name;
	/**
	 * type of the storage. See {@link StorageType} for supported storages
	 */
	private StorageType storageType;

	/** optional redirect URL when authorization flow is used for the storage **/
	private String redirect;
	
	private List<Configuration> configurations;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

}
