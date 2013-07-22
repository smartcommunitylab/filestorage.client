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
package eu.trentorise.smartcampus.filestorage.client;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import eu.trentorise.smartcampus.filestorage.client.RestCaller.RequestType;
import eu.trentorise.smartcampus.filestorage.client.model.AppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.ListAppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.ListUserAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Token;
import eu.trentorise.smartcampus.filestorage.client.model.UserAccount;
import eu.trentorise.smartcampus.filestorage.client.retriever.HttpResourceRetriever;
import eu.trentorise.smartcampus.filestorage.client.retriever.ResourceRetriever;

/**
 * Filestorage APIs
 * 
 * @author mirko perillo
 * 
 */
public class Filestorage {
	private static final Logger logger = Logger.getLogger(Filestorage.class);

	private RestCaller restCaller = new RestCaller();
	private String serviceUrl;
	private String baseUrl;
	private String appName;

	private static final String contextPath = "smartcampus.filestorage";
	private static final String AUTH_HEADER = "AUTH_TOKEN";

	/**
	 * 
	 * @param baseUrl
	 *            the http base url of the service
	 * @param appName
	 *            name of the application
	 */
	public Filestorage(String baseUrl, String appName) {
		this.appName = appName;
		this.baseUrl = baseUrl;
		serviceUrl = baseUrl;
		serviceUrl += (baseUrl.endsWith("/")) ? "" : "/";
		serviceUrl += contextPath;
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param authToken
	 *            the authentication token
	 * @param userAccountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResource(File resource, String authToken,
			String userAccountId, boolean createSocialData)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			String params = "?createSocialData=" + createSocialData;
			return restCaller.callOneResult(RequestType.POST, serviceUrl
					+ "/resource/" + appName + "/" + userAccountId + params,
					Arrays.asList(header), resource, "file", Metadata.class,
					null);
		} catch (Exception e) {
			logger.error("Exception storing resource", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * deletes a resource
	 * 
	 * @param authToken
	 *            the authentication token
	 * @param userAccountId
	 *            id of the user storage account
	 * @param resourceId
	 *            if of resource to remove
	 * @throws FilestorageException
	 */
	public void deleteResource(String authToken, String userAccountId,
			String resourceId) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			restCaller.callOneResult(RequestType.DELETE, serviceUrl
					+ "/resource/" + appName + "/" + userAccountId + "/"
					+ resourceId, Arrays.asList(header), null, String.class,
					null);
		} catch (Exception e) {
			logger.error("Exception deleting resource", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * updates a resource
	 * 
	 * @param authToken
	 *            authentication token
	 * @param userAccountId
	 *            id of the user storage account
	 * @param resourceId
	 *            id of the resource to update
	 * @param resource
	 *            new resource content
	 */
	public void updateResource(String authToken, String userAccountId,
			String resourceId, File resource) {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			restCaller.callOneResult(RequestType.POST, serviceUrl
					+ "/resource/" + appName + "/" + userAccountId + "/"
					+ resourceId, Arrays.asList(header), resource, "file",
					null, null);
		} catch (Exception e) {

		}
	}

/**
 	 * retrieves all the user storage accounts binded to the application name and to the authentication token
	 * @param authToken the authentication token
	 * @return list of {@link UserAccount)
	 * @throws FilestorageException
	 */
	public List<UserAccount> getUserAccounts(String authToken)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.GET,
					serviceUrl + "/useraccount/" + appName,
					Arrays.asList(header), ListUserAccount.class, null)
					.getUserAccounts();
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}

	}

	/**
	 * retrieves all the application storage account binded to the application
	 * name
	 * 
	 * @return the list of {@link AppAccount}
	 * @throws FilestorageException
	 */
	public List<AppAccount> getAppAccounts() throws FilestorageException {
		try {
			return restCaller.callOneResult(RequestType.GET,
					serviceUrl + "/appaccount/" + appName, null,
					ListAppAccount.class, null).getAppAccounts();
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * stores an user storage account
	 * 
	 * @param authToken
	 *            authentication token
	 * @param userAccount
	 *            userAccount to store
	 * @return the {@link UserAccount} stored
	 * @throws FilestorageException
	 */
	public UserAccount storeUserAccount(String authToken,
			UserAccount userAccount) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.POST, serviceUrl
					+ "/useraccount/" + appName, Arrays.asList(header),
					userAccount, UserAccount.class, null);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}

	}

	/**
	 * retrieves a shared resource
	 * 
	 * @param authToken
	 *            authentication token
	 * @param resourceId
	 *            id of the resource
	 * @return the {@link Resource}
	 * @throws FilestorageException
	 */
	public Resource getSharedResource(String authToken, String resourceId)
			throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId,
					ResourceType.SHARED_RESOURCE, null);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}

	}

	/**
	 * retrieves a shared resource token
	 * 
	 * @param authToken
	 *            authentication token
	 * @param resourceId
	 *            id of the resource
	 * @return the {@link Resource}
	 * @throws FilestorageException
	 */

	public Token getSharedResourceToken(String authToken, String resourceId)
			throws FilestorageException {
		try {
			return getResourceToken(authToken, resourceId,
					ResourceType.SHARED_RESOURCE, null);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves a public resource token
	 * 
	 * @param authentication
	 *            authentication to access public resource
	 * @param resourceId
	 *            id of the resource
	 * @return the {@link Resource}
	 * @throws FilestorageException
	 */
	public Token getPublicResourceToken(Authentication authentication,
			String resourceId) throws FilestorageException {
		return getResourceToken(null, resourceId, ResourceType.PUBLIC_RESOURCE,
				authentication);
	}

	public Token getMobileResourceToken(Authentication authentication,
			String resourceId) throws FilestorageException {
		return getResourceToken(null, resourceId, ResourceType.MOBILE_RESOURCE,
				authentication);
	}

	/**
	 * retrieves an owned resource
	 * 
	 * @param authToken
	 *            authentication token
	 * @param resourceId
	 *            id of the resource
	 * @return {@link Resource}
	 * @throws FilestorageException
	 */
	public Resource getMyResource(String authToken, String resourceId)
			throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId,
					ResourceType.MY_RESOURCE, null);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves an owned resource token
	 * 
	 * @param authToken
	 *            authentication token
	 * @param resourceId
	 *            id of the resource
	 * @return {@link Resource}
	 * @throws FilestorageException
	 */

	public Token getMyResourceToken(String authToken, String resourceId)
			throws FilestorageException {
		try {
			return getResourceToken(authToken, resourceId,
					ResourceType.MY_RESOURCE, null);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * update social entity associated to the resource. Social entity MUST be
	 * owned be the user.
	 * 
	 * @param authToken
	 *            authentication token
	 * @param resourceId
	 *            id of the resource
	 * @param entityId
	 *            social entity id
	 * @return updated information about resource
	 * @throws FilestorageException
	 */
	public Metadata updateSocialData(String authToken, String resourceId,
			String entityId) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.PUT, serviceUrl
					+ "/updatesocial/" + appName + "/" + resourceId + "/"
					+ entityId, Arrays.asList(header), null, Metadata.class,
					null);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves the resource metadata
	 * 
	 * @param authToken
	 *            authentication token
	 * @param resourceId
	 *            id of the resource
	 * @return the {@link Metadata} binded to the given resource
	 * @throws FilestorageException
	 */
	public Metadata getResourceMetadata(String authToken, String resourceId)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.GET, serviceUrl
					+ "/metadata/" + appName + "/" + resourceId,
					Arrays.asList(header), null, Metadata.class, null);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	private Token getResourceToken(String authToken, String resourceId,
			ResourceType resourceType, Authentication authentication)
			throws FilestorageException {

		HttpHeader header = null;
		if (authToken != null) {
			header = new HttpHeader(AUTH_HEADER, authToken);
		}
		try {

			String functionality = "";
			switch (resourceType) {
			case MY_RESOURCE:
				functionality = "myresource";
				break;
			case SHARED_RESOURCE:
				functionality = "resource";
				break;
			case PUBLIC_RESOURCE:
				functionality = "publicresource";
				break;
			case MOBILE_RESOURCE:
				functionality = "mobile";
				break;
			default:
				functionality = "myresource";
				break;
			}
			return restCaller.callOneResult(RequestType.GET, serviceUrl + "/"
					+ functionality + "/" + appName + "/" + resourceId,
					header != null ? Arrays.asList(header) : null, null,
					Token.class, authentication);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	private ResourceRetriever resourceRetrieverFactory(Token token) {
		ResourceRetriever retriever = null;
		switch (token.getStorageType()) {
		case DROPBOX:
			retriever = new HttpResourceRetriever(baseUrl, appName);
			break;

		default:
			throw new IllegalArgumentException(
					"StorageType requested doesn't exist");
		}

		return retriever;
	}

	private static enum ResourceType {
		MY_RESOURCE, SHARED_RESOURCE, PUBLIC_RESOURCE, MOBILE_RESOURCE;
	}
}
