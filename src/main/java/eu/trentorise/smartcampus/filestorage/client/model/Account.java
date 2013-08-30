/**
 *    Copyright 2012-2013 Trento RISE
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
 */

package eu.trentorise.smartcampus.filestorage.client.model;

import java.util.List;

/**
 * <i>Account</i> contains all the informations about a user storage account. A
 * Account is binded to a {@link Storage}.
 * 
 * @author mirko perillo
 * 
 */
public class Account {
	/**
	 * id of the account
	 */
	private String id;
	/**
	 * id of the user
	 */
	private String userId;
	/**
	 * application name binded to user storage account
	 */
	private String appId;
	/**
	 * Type of storage
	 */
	private StorageType storageType;
	/**
	 * name to represent the user storage account
	 */
	private String name;
	/**
	 * list of the configurations of the account storage
	 */
	private List<Configuration> configurations;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(StorageType storage) {
		this.storageType = storage;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
