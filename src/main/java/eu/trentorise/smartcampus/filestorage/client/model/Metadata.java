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


/**
 * <i>Metadata</i> represents all the informations about a resource
 * 
 * @author mirko perillo
 * 
 */
public class Metadata {
	/**
	 * name of the resource
	 */
	private String name;

	/**
	 * resource id
	 */
	private String resourceId;

	/**
	 * social id binded to resource
	 */
	private String socialId;

	/**
	 * optional external id for the resource
	 */
	private String fileExternalId;

	/**
	 * account id in which resource is stored
	 */
	private String accountId;
	/**
	 * application which resource is binded to
	 */
	private String appId;

	/**
	 * MIME type of resource
	 */
	private String contentType;

	/**
	 * size of the resource in bytes
	 */
	private long size;

	/**
	 * creation time
	 */
	private long creationTs;

	/**
	 * last modification time
	 */
	private long lastModifiedTs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String eid) {
		this.socialId = eid;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getCreationTs() {
		return creationTs;
	}

	public void setCreationTs(long creationTs) {
		this.creationTs = creationTs;
	}

	public long getLastModifiedTs() {
		return lastModifiedTs;
	}

	public void setLastModifiedTs(long lastModifiedTs) {
		this.lastModifiedTs = lastModifiedTs;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFileExternalId() {
		return fileExternalId;
	}

	public void setFileExternalId(String fileExternalId) {
		this.fileExternalId = fileExternalId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
