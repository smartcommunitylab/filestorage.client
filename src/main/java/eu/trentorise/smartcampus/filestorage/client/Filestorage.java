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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.ListAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.Token;
import eu.trentorise.smartcampus.filestorage.client.network.ByteArrayParam;
import eu.trentorise.smartcampus.filestorage.client.network.FileParam;
import eu.trentorise.smartcampus.filestorage.client.network.MultipartParam;
import eu.trentorise.smartcampus.filestorage.client.network.MultipartRemoteConnector;
import eu.trentorise.smartcampus.filestorage.client.retriever.HttpResourceRetriever;
import eu.trentorise.smartcampus.filestorage.client.retriever.ResourceRetriever;
import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;

/**
 * Filestorage APIs
 * 
 * @author mirko perillo
 * 
 */
public class Filestorage {
	// private static final Logger logger = Logger.getLogger(Filestorage.class);

	private String serverUrl;
	private String appId;

	protected static final String STORAGE = "storage/";
	protected static final String ACCOUNT = "account/";
	protected static final String RESOURCE = "resource/";
	protected static final String SHARED_RESOURCE = "sharedresource/";
	protected static final String MY_RESOURCE = "resource/";
	protected static final String METADATA = "metadata/";
	protected static final String THUMBNAIL = "thumbnail/";
	protected static final String SOCIAL = "updatesocial/";
	protected static final String REQUEST_AUTH = "requestAuth/";

	public static final String APP_OPERATION = "app/";
	public static final String USER_OPERATION = "user/";

	protected static final String RESOURCE_PARAM_NAME = "file";

	/**
	 * 
	 * @param baseUrl
	 *            the http base url of the service
	 * @param appId
	 *            name of the application
	 */
	public Filestorage(String serverUrl, String appId) {
		this.appId = appId;
		this.serverUrl = serverUrl;
		this.serverUrl += (serverUrl.endsWith("/")) ? "" : "/";
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param authToken
	 *            the user access token
	 * @param accountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResourceByUser(File resource, String authToken,
			String accountId, boolean createSocialData)
			throws FilestorageException {

		return storeResource(resource, authToken, accountId, createSocialData,
				USER_OPERATION);
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param inputStream
	 *            inputstream of the file
	 * @param authToken
	 *            client access token
	 * @param accountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResourceByUser(File resource, InputStream inputStream,
			String authToken, String accountId, boolean createSocialData)
			throws FilestorageException {

		return storeResource(resource, inputStream, authToken, accountId,
				createSocialData, USER_OPERATION);
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param authToken
	 *            client access token
	 * @param accountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResourceByApp(File resource, String authToken,
			String accountId, boolean createSocialData)
			throws FilestorageException {
		return storeResource(resource, authToken, accountId, createSocialData,
				APP_OPERATION);
	}

	/**
	 * 
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param inputStream
	 *            inputstream of the file
	 * @param authToken
	 *            client access token
	 * @param accountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return
	 * @throws FilestorageException
	 */

	public Metadata storeResourceByApp(File resource, InputStream inputStream,
			String authToken, String accountId, boolean createSocialData)
			throws FilestorageException {
		return storeResource(resource, inputStream, authToken, accountId,
				createSocialData, APP_OPERATION);
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param authToken
	 *            client access token
	 * @param accountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResourceByApp(byte[] resourceContent,
			String resourceName, String resourceContentType, String authToken,
			String accountId, boolean createSocialData)
			throws FilestorageException {
		return storeResource(resourceContent, resourceName,
				resourceContentType, authToken, accountId, createSocialData,
				APP_OPERATION);
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param authToken
	 *            user access token
	 * @param accountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResourceByUser(byte[] resourceContent,
			String resourceName, String resourceContentType, String authToken,
			String accountId, boolean createSocialData)
			throws FilestorageException {
		return storeResource(resourceContent, resourceName,
				resourceContentType, authToken, accountId, createSocialData,
				USER_OPERATION);
	}

	private Metadata storeResource(File resource, String authToken,
			String accountId, boolean createSocialData, String operationType)
			throws FilestorageException {
		FileParam multipartParam = new FileParam(RESOURCE_PARAM_NAME, resource);
		return storeResource(multipartParam, authToken, accountId,
				createSocialData, operationType);
	}

	private Metadata storeResource(File resource, InputStream inputStream,
			String authToken, String accountId, boolean createSocialData,
			String operationType) throws FilestorageException {
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("createSocialData", createSocialData);
			String response = MultipartRemoteConnector.postJSON(serverUrl,
					"localstorage/" + RESOURCE + "create/" + operationType
							+ appId + "/" + accountId, authToken, parameters,
					inputStream, resource);
			return JsonUtils.toObject(response, Metadata.class);
		} catch (Exception e) {
			// logger.error("Exception storing resource", e);
			throw new FilestorageException(e);
		}

	}

	private Metadata storeResource(byte[] resourceContent, String resourceName,
			String resourceContentType, String authToken, String accountId,
			boolean createSocialData, String operationType)
			throws FilestorageException {
		ByteArrayParam multipartParam = new ByteArrayParam(RESOURCE_PARAM_NAME,
				resourceName, resourceContentType, resourceContent);
		return storeResource(multipartParam, authToken, accountId,
				createSocialData, operationType);
	}

	private Metadata storeResource(MultipartParam resource, String authToken,
			String accountId, boolean createSocialData, String operationType)
			throws FilestorageException {
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("createSocialData", createSocialData);

			String response = MultipartRemoteConnector.postJSON(serverUrl,
					RESOURCE + "create/" + operationType + appId + "/"
							+ accountId, authToken, parameters, resource);
			return JsonUtils.toObject(response, Metadata.class);
		} catch (Exception e) {
			// logger.error("Exception storing resource", e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * deletes a resource
	 * 
	 * @param authToken
	 *            user access token
	 * @param resourceId
	 *            if of resource to remove
	 * @throws FilestorageException
	 */
	public void deleteResourceByUser(String authToken, String resourceId)
			throws FilestorageException {
		deleteResource(authToken, resourceId, USER_OPERATION);
	}

	/**
	 * deletes a resource
	 * 
	 * @param authToken
	 *            client access token
	 * @param resourceId
	 *            if of resource to remove
	 * @throws FilestorageException
	 */
	public void deleteResourceByApp(String authToken, String resourceId)
			throws FilestorageException {
		deleteResource(authToken, resourceId, APP_OPERATION);
	}

	private void deleteResource(String authToken, String resourceId,
			String operationType) throws FilestorageException {
		try {
			MultipartRemoteConnector.deleteJSON(serverUrl, RESOURCE
					+ operationType + appId + "/" + resourceId, authToken);
		} catch (Exception e) {
			// logger.error(
			// String.format("Exception updating resource %s", resourceId),
			// e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * updates a resource
	 * 
	 * @param authToken
	 *            user access token
	 * @param accountId
	 *            id of the user storage account
	 * @param resourceId
	 *            id of the resource to update
	 * @param resource
	 *            new resource content
	 * @throws FilestorageException
	 */
	public void updateResourceByUser(String authToken, String resourceId,
			File resource) throws FilestorageException {
		updateResource(authToken, resourceId, resource, USER_OPERATION);
	}

	/**
	 * updates a resource
	 * 
	 * @param authToken
	 *            client access token
	 * @param accountId
	 *            id of the user storage account
	 * @param resourceId
	 *            id of the resource to update
	 * @param resource
	 *            new resource content
	 * @throws FilestorageException
	 */
	public void updateResourceByApp(String authToken, String resourceId,
			File resource) throws FilestorageException {
		updateResource(authToken, resourceId, resource, APP_OPERATION);
	}

	private void updateResource(String authToken, String resourceId,
			File resource, String operationType) throws FilestorageException {
		FileParam multipartParam = new FileParam(RESOURCE_PARAM_NAME, resource);
		try {
			MultipartRemoteConnector.postJSON(serverUrl, RESOURCE
					+ operationType + appId + "/" + resourceId, authToken,
					multipartParam);
		} catch (RemoteException e) {
			// logger.error(
			// String.format("Exception updating resource %s", resourceId),
			// e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves the user storage account bound to the application name and to
	 * the access token
	 * 
	 * @param authToken
	 *            user access token
	 * @return {@link Account} instance
	 * @throws FilestorageException
	 * @throws RemoteException
	 * @throws SecurityException
	 */
	public Account getAccountByUser(String authToken)
			throws FilestorageException, SecurityException {
		return getAccount(authToken, null, USER_OPERATION);
	}

/**
 	 * retrieves all the user storage accounts bound to the application name
	 * @param authToken client access token
	 * @return list of {@link Account)
	 * @throws FilestorageException
	 * @throws RemoteException 
	 * @throws SecurityException 
	 */
	public List<Account> getAccountsByApp(String authToken)
			throws FilestorageException, SecurityException {
		return getAccounts(authToken, APP_OPERATION);
	}

	private List<Account> getAccounts(String authToken, String operationType)
			throws SecurityException, FilestorageException {
		try {
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					ACCOUNT + operationType + appId, authToken);
			return JsonUtils.toObject(response, ListAccount.class)
					.getAccounts();
		} catch (RemoteException e) {
			// logger.error(String.format("Exception getting accounts"), e);
			throw new FilestorageException(e);
		}
	}

/**
 	 * retrieves the specified user storage account 
	 * @param authToken client access token
	 * @return list of {@link Account)
	 * @throws FilestorageException
	 * @throws RemoteException 
	 * @throws SecurityException 
	 */
	public Account getAccountByApp(String authToken, String accountId)
			throws FilestorageException {
		return getAccount(authToken, accountId, APP_OPERATION);
	}

	private Account getAccount(String authToken, String accountId,
			String operationType) throws FilestorageException {
		try {
			String service = ACCOUNT + operationType + appId;
			if (accountId != null)
				service += "/" + accountId;
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					service, authToken);
			return JsonUtils.toObject(response, Account.class);
		} catch (RemoteException e) {
			// logger.error(
			// String.format("Exception getting account %s", accountId), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * Delete the specified user account
	 * 
	 * @param authToken
	 *            client access token
	 * @param accountId
	 *            account ID
	 * @throws FilestorageException
	 */
	public void deleteAccountByApp(String authToken, String accountId)
			throws FilestorageException {
		deleteAccount(authToken, accountId, APP_OPERATION);
	}

	/**
	 * Delete the specified user account
	 * 
	 * @param authToken
	 *            user access token
	 * @throws FilestorageException
	 */
	public void deleteAccountByUser(String authToken)
			throws FilestorageException {
		deleteAccount(authToken, null, USER_OPERATION);
	}

	private void deleteAccount(String authToken, String accountId,
			String operationType) throws FilestorageException {
		try {
			String service = ACCOUNT + operationType + appId;
			if (accountId != null)
				service += "/" + accountId;
			MultipartRemoteConnector.deleteJSON(serverUrl, service, authToken);
		} catch (RemoteException e) {
			// logger.error(
			// String.format("Exception getting account %s", accountId), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * Create a new user storage account
	 * 
	 * @param authToken
	 *            client access token
	 * @param account
	 *            {@link Account} data. Field {@link Account#appId} is required
	 * @return
	 * @throws FilestorageException
	 */
	public Account createAccountByApp(String authToken, Account account)
			throws FilestorageException {
		return createAccount(authToken, account, APP_OPERATION);
	}

	/**
	 * Create a new user storage account
	 * 
	 * @param authToken
	 *            user access token
	 * @param account
	 *            {@link Account} data. Field {@link Account#appId} is required
	 * @return
	 * @throws FilestorageException
	 */
	public Account createAccountByUser(String authToken, Account account)
			throws FilestorageException {
		return createAccount(authToken, account, USER_OPERATION);
	}

	private Account createAccount(String authToken, Account account,
			String operationType) throws FilestorageException {
		try {
			String response = MultipartRemoteConnector.postJSON(serverUrl,
					ACCOUNT + operationType + appId, JsonUtils.toJSON(account),
					authToken);
			return JsonUtils.toObject(response, Account.class);
		} catch (RemoteException e) {
			// logger.error(String.format(
			// "Exception creating new account %s for app %s",
			// account.getName(), account.getAppId()), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * Update user storage account
	 * 
	 * @param authToken
	 *            client access token
	 * @param account
	 *            {@link Account} to update. {@link Account#getId()} should not
	 *            be null
	 * @throws FilestorageException
	 */
	public void updateAccountByApp(String authToken, Account account)
			throws FilestorageException {
		updateAccount(authToken, account, account.getId(), APP_OPERATION);
	}

	/**
	 * Update user storage account
	 * 
	 * @param authToken
	 *            user access token
	 * @param account
	 *            {@link Account} to update
	 * @throws FilestorageException
	 */
	public void updateAccountByUser(String authToken, Account account)
			throws FilestorageException {
		updateAccount(authToken, account, null, USER_OPERATION);
	}

	private void updateAccount(String authToken, Account account,
			String accountId, String operationType) throws FilestorageException {
		try {
			String service = ACCOUNT + operationType + appId;
			if (accountId != null)
				service += "/" + accountId;

			MultipartRemoteConnector.putJSON(serverUrl, service,
					JsonUtils.toJSON(account), authToken);
		} catch (RemoteException e) {
			// logger.error(String.format(
			// "Exception updating account %s for app %s",
			// account.getName(), account.getAppId()), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * Get storage registration information
	 * 
	 * @param authToken
	 *            client access token
	 * @return {@link Storage} instance
	 * @throws FilestorageException
	 */
	public Storage getStorage(String authToken) throws FilestorageException {
		return getStorage(authToken, appId, APP_OPERATION);
	}

	// public Storage getStorageByApp(String authToken, String appId) throws
	// FilestorageException {
	// return getStorage(authToken, appId, APP_OPERATION);
	// }

	// public Storage getStorageByUser(String authToken) throws
	// FilestorageException {
	// return getStorage(authToken, appId, USER_OPERATION);
	// }

	// public Storage getStorageByUserAndApp(String authToken, String appId)
	// throws FilestorageException {
	// return getStorage(authToken, appId, USER_OPERATION);
	// }

	private Storage getStorage(String authToken, String appId,
			String operationType) throws SecurityException,
			FilestorageException {
		try {
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					STORAGE + operationType + appId, authToken);
			return JsonUtils.toObject(response, Storage.class);
		} catch (RemoteException e) {
			throw new FilestorageException(e);
		}
	}

	/**
	 * Create and register a new storage.
	 * 
	 * @param authToken
	 *            client access token
	 * @param storage
	 *            {@link Storage} to store
	 * @return registered {@link Storage} instance
	 * @throws SecurityException
	 * @throws FilestorageException
	 */
	public Storage createStorage(String authToken, Storage storage)
			throws SecurityException, FilestorageException {
		return createStorage(authToken, storage, APP_OPERATION);
	}

	private Storage createStorage(String authToken, Storage storage,
			String operationType) throws SecurityException,
			FilestorageException {
		try {
			String response = MultipartRemoteConnector.postJSON(serverUrl,
					STORAGE + operationType + appId, JsonUtils.toJSON(storage),
					authToken);
			return JsonUtils.toObject(response, Storage.class);
		} catch (RemoteException e) {
			// logger.error(String.format("Exception creating storage for app %s",
			// appId), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * Delete storage registration
	 * 
	 * @param authToken
	 *            client access token
	 * @return true if the operation was successful
	 * @throws FilestorageException
	 */
	public boolean deleteStorage(String authToken) throws FilestorageException {
		return deleteStorage(authToken, APP_OPERATION);
	}

	private boolean deleteStorage(String authToken, String operationType)
			throws FilestorageException {

		try {
			String response = MultipartRemoteConnector.deleteJSON(serverUrl,
					STORAGE + operationType + appId, authToken);
			return Boolean.valueOf(response);
		} catch (RemoteException e) {
			throw new FilestorageException(e);
		}
	}

	/**
	 * Update storage registration information
	 * 
	 * @param authToken
	 *            client access token
	 * @param storage
	 *            {@link Storage} instance to update
	 * @return
	 * @throws FilestorageException
	 */
	public Storage updateStorage(String authToken, Storage storage)
			throws FilestorageException {
		return updateStorage(authToken, storage, APP_OPERATION);
	}

	private Storage updateStorage(String authToken, Storage storage,
			String operationType) throws FilestorageException {
		try {
			String response = MultipartRemoteConnector.putJSON(serverUrl,
					STORAGE + operationType + appId, JsonUtils.toJSON(storage),
					authToken);
			return JsonUtils.toObject(response, Storage.class);
		} catch (RemoteException e) {
			// logger.error(String.format(
			// "Exception deleting storage %s of app %s", storage.getId(),
			// appId), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves a user resource
	 * 
	 * @param authToken
	 *            client access token
	 * @param resourceId
	 *            id of the resource
	 * @return {@link Resource}
	 * @throws FilestorageException
	 */
	public Resource getResourceByApp(String authToken, String resourceId)
			throws FilestorageException {
		return getMyResource(authToken, resourceId, APP_OPERATION, null);
	}

	public Resource getResourceByApp(String authToken, String resourceId,
			OutputStream outputStream) throws FilestorageException {
		return getMyResource(authToken, resourceId, APP_OPERATION, outputStream);
	}

	/**
	 * retrieves a user resource
	 * 
	 * @param authToken
	 *            user access token
	 * @param resourceId
	 *            id of the resource
	 * @return {@link Resource}
	 * @throws FilestorageException
	 */
	public Resource getResourceByUser(String authToken, String resourceId)
			throws FilestorageException {
		return getMyResource(authToken, resourceId, USER_OPERATION, null);
	}

	/**
	 * 
	 * @param authToken
	 *            user acces token
	 * @param resourceId
	 *            id of the resource
	 * @param outputStream
	 *            outputstream of the data
	 * @return
	 * @throws FilestorageException
	 */
	public Resource getResourceByUser(String authToken, String resourceId,
			OutputStream outputStream) throws FilestorageException {
		return getMyResource(authToken, resourceId, USER_OPERATION,
				outputStream);
	}

	private Resource getMyResource(String authToken, String resourceId,
			String operationType, OutputStream outputStream)
			throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId, null, true,
					operationType);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token,
					operationType, outputStream);
		} catch (Exception e) {
			// logger.error(
			// String.format("Exception getting resource %s", resourceId),
			// e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves the resource shared with the user
	 * 
	 * @param authToken
	 *            client access token
	 * @param resourceId
	 *            resource ID
	 * @param userId
	 *            user ID
	 * @return {@link Resource} data
	 * @throws FilestorageException
	 */
	public Resource getSharedResourceByApp(String authToken, String resourceId,
			String userId) throws FilestorageException {
		return getSharedResource(authToken, resourceId, userId, APP_OPERATION,
				null);
	}

	/**
	 * 
	 * @param authToken
	 *            client access token
	 * @param resourceId
	 *            resource ID
	 * @param userId
	 *            user ID
	 * @param outputStream
	 *            outputstream of the data
	 * @return
	 * @throws FilestorageException
	 */
	public Resource getSharedResourceByApp(String authToken, String resourceId,
			String userId, OutputStream outputStream)
			throws FilestorageException {
		return getSharedResource(authToken, resourceId, userId, APP_OPERATION,
				outputStream);
	}

	/**
	 * retrieves the resource shared with the user
	 * 
	 * @param authToken
	 *            user access token
	 * @param resourceId
	 *            resource ID
	 * @return {@link Resource} data
	 * @throws FilestorageException
	 */
	public Resource getSharedResourceByUser(String authToken, String resourceId)
			throws FilestorageException {
		return getSharedResource(authToken, resourceId, null, USER_OPERATION,
				null);
	}

	/**
	 * 
	 * @param authToken
	 *            user access token
	 * @param resourceId
	 *            resource ID
	 * 
	 * @param outputStream
	 *            outputstream of the data
	 * @return
	 * @throws FilestorageException
	 */
	public Resource getSharedResourceByUser(String authToken,
			String resourceId, OutputStream outputStream)
			throws FilestorageException {
		return getSharedResource(authToken, resourceId, null, USER_OPERATION,
				outputStream);
	}

	private Resource getSharedResource(String authToken, String resourceId,
			String userId, String operationType, OutputStream outputStream)
			throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId, userId,
					false, operationType);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token,
					operationType, outputStream);
		} catch (Exception e) {
			// logger.error(
			// String.format("Exception getting resource %s", resourceId),
			// e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * update social entity associated to the resource. Social entity MUST be
	 * owned be the user.
	 * 
	 * @param authToken
	 *            client access token
	 * @param resourceId
	 *            id of the resource
	 * @param entityId
	 *            social entity id
	 * @return updated information about resource
	 * @throws FilestorageException
	 */
	public Metadata updateSocialDataByApp(String authToken, String resourceId,
			String entityId) throws FilestorageException {
		return updateSocialData(authToken, resourceId, entityId, APP_OPERATION);
	}

	/**
	 * update social entity associated to the resource. Social entity MUST be
	 * owned be the user.
	 * 
	 * @param authToken
	 *            user access token
	 * @param resourceId
	 *            id of the resource
	 * @param entityId
	 *            social entity id
	 * @return updated information about resource
	 * @throws FilestorageException
	 */
	public Metadata updateSocialDataByUser(String authToken, String resourceId,
			String entityId) throws FilestorageException {
		return updateSocialData(authToken, resourceId, entityId, USER_OPERATION);
	}

	private Metadata updateSocialData(String authToken, String resourceId,
			String entityId, String operationType) throws FilestorageException {
		try {
			String response = RemoteConnector.putJSON(serverUrl,
					SOCIAL + operationType + appId + "/" + resourceId + "/"
							+ entityId, authToken);
			return JsonUtils.toObject(response, Metadata.class);
		} catch (RemoteException e) {
			// logger.error(String
			// .format("Exception updating social data of resource %s",
			// resourceId), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves the resource metadata
	 * 
	 * @param authToken
	 *            user access token
	 * @param resourceId
	 *            id of the resource
	 * @return the {@link Metadata} binded to the given resource
	 * @throws FilestorageException
	 */
	public Metadata getResourceMetadataByUser(String authToken,
			String resourceId) throws FilestorageException {
		return getResourceMetadata(authToken, resourceId, USER_OPERATION);
	}

	/**
	 * retrieves the resource metadata
	 * 
	 * @param authToken
	 *            client access token
	 * @param resourceId
	 *            id of the resource
	 * @return the {@link Metadata} binded to the given resource
	 * @throws FilestorageException
	 */
	public Metadata getResourceMetadataByApp(String authToken, String resourceId)
			throws FilestorageException {
		return getResourceMetadata(authToken, resourceId, APP_OPERATION);
	}

	private Metadata getResourceMetadata(String authToken, String resourceId,
			String operationType) throws FilestorageException {

		try {
			String uri = null;
			if (operationType.equals(APP_OPERATION)) {
				uri = operationType + appId + "/" + resourceId;
			}
			if (operationType.equals(USER_OPERATION)) {
				uri = operationType + appId + "/" + resourceId;
			}
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					METADATA + uri, authToken);
			return JsonUtils.toObject(response, Metadata.class);
		} catch (RemoteException e) {
			// logger.error(String.format(
			// "Exception getting metadata of resource %s", resourceId));
			throw new FilestorageException(e);
		}
	}

	/**
	 * creates resource metadata
	 * 
	 * @param authToken
	 * @param resource
	 * @param accountId
	 * @param createSocialData
	 * @return metadata created
	 * @throws FilestorageException
	 */
	public Metadata createMetadataByUser(String authToken, Resource resource,
			String accountId, boolean createSocialData)
			throws FilestorageException {
		return createMetadata(authToken, resource, accountId, createSocialData,
				USER_OPERATION);
	}

	private Metadata createMetadata(String authToken, Resource resource,
			String accountId, boolean createSocialData, String operationType)
			throws FilestorageException {
		try {
			String uri = null;
			if (operationType.equals(APP_OPERATION)) {
				uri = operationType + appId + "/" + accountId;
			}
			if (operationType.equals(USER_OPERATION)) {
				uri = operationType + appId + "/" + accountId;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("createSocialData", createSocialData);
			String response = MultipartRemoteConnector.postJSON(serverUrl,
					METADATA + uri, JsonUtils.toJSON(resource), authToken,
					params);
			return JsonUtils.toObject(response, Metadata.class);
		} catch (RemoteException e) {
			// logger.error(String.format(
			// "Exception getting metadata of resource %s", resourceId));
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves all the resource metadata for the application
	 * 
	 * @param authToken
	 *            client access token
	 * @param position
	 *            position to start from in the list
	 * @param size
	 *            number of results to return
	 * @return the {@link Metadata} binded to the given resource
	 * @throws FilestorageException
	 */
	public List<Metadata> getAllResourceMetadataByApp(String authToken,
			Integer position, Integer size) throws FilestorageException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (position != null) {
			parameters.put("position", position);
		}
		if (size != null) {
			parameters.put("size", size);
		}
		return getAllResourceMetadata(authToken, APP_OPERATION,
				parameters.size() == 0 ? null : parameters);
	}

	/**
	 * retrieves all the resource metadata for the user
	 * 
	 * @param authToken
	 *            user access token
	 * @param position
	 *            position to start from in the list
	 * @param size
	 *            number of results to return
	 * @return the {@link Metadata} binded to the given resource
	 * @throws FilestorageException
	 */
	public List<Metadata> getAllResourceMetadataByUser(String authToken,
			Integer position, Integer size) throws FilestorageException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (position != null) {
			parameters.put("position", position);
		}
		if (size != null) {
			parameters.put("size", size);
		}
		return getAllResourceMetadata(authToken, USER_OPERATION,
				parameters.size() == 0 ? null : parameters);
	}

	private List<Metadata> getAllResourceMetadata(String authToken,
			String operationType, Map<String, Object> parameters)
			throws FilestorageException {
		try {
			String uri = null;
			if (operationType.equals(APP_OPERATION)) {
				uri = "all/" + operationType + appId;
			}
			if (operationType.equals(USER_OPERATION)) {
				uri = "all/" + operationType + appId;
			}
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					METADATA + uri, authToken, parameters);
			return JsonUtils.toObjectList(response, Metadata.class);
		} catch (RemoteException e) {
			// logger.error(String.format("Exception getting all metadata of %s",
			// appId));
			throw new FilestorageException(e);
		}
	}

	public Token getSharedResourceTokenByUser(String authToken,
			String resourceId) throws FilestorageException {
		return getResourceToken(authToken, resourceId, null, false,
				USER_OPERATION);
	}

	public Token getSharedResourceTokenByApp(String authToken,
			String resourceId, String userId) throws FilestorageException {
		return getResourceToken(authToken, resourceId, userId, false,
				APP_OPERATION);
	}

	public Token getResourceTokenByApp(String authToken, String resourceId)
			throws FilestorageException {
		return getResourceToken(authToken, resourceId, null, true,
				APP_OPERATION);
	}

	public Token getResourceTokenByUser(String authToken, String resourceId)
			throws FilestorageException {
		return getResourceToken(authToken, resourceId, null, true,
				USER_OPERATION);
	}

	private Token getResourceToken(String authToken, String resourceId,
			String userId, boolean owned, String operationType)
			throws FilestorageException {
		try {
			String functionality = owned ? MY_RESOURCE : SHARED_RESOURCE;
			String response = RemoteConnector
					.getJSON(
							serverUrl,
							functionality
									+ operationType
									+ appId
									+ "/"
									+ (!owned
											&& operationType
													.equals(APP_OPERATION) ? userId
											+ "/"
											: "") + resourceId, authToken);
			return JsonUtils.toObject(response, Token.class);
		} catch (RemoteException e) {
			// logger.error(
			// String.format("Exception getting resource %s", resourceId),
			// e);
			throw new FilestorageException(e);
		}
	}

	public void getThumbnailByUser(String authToken, String resourceId,
			OutputStream output) throws FilestorageException {
		getThumbnail(authToken, resourceId, output, USER_OPERATION);
	}

	private void getThumbnail(String authToken, String resourceId,
			OutputStream output, String operationType)
			throws FilestorageException {
		try {
			InputStream in = MultipartRemoteConnector.getBinaryStream(
					serverUrl, THUMBNAIL + operationType + appId + "/"
							+ resourceId, authToken);
			if (in != null) {
				byte[] buffer = new byte[1024];
				int readed = 0;
				while ((readed = in.read(buffer)) > 0) {
					output.write(buffer, 0, readed);
				}
			}
		} catch (Exception e) {
			// logger.error(
			// String.format("Exception getting resource %s", resourceId),
			// e);
			throw new FilestorageException(e);
		}
	}

	private ResourceRetriever resourceRetrieverFactory(Token token) {
		ResourceRetriever retriever = null;
		switch (token.getStorageType()) {
		case DROPBOX:
			retriever = new HttpResourceRetriever(serverUrl, appId);
			break;
		case LOCAL:
			retriever = new HttpResourceRetriever(serverUrl, appId);
			break;
		default:
			throw new IllegalArgumentException(
					"StorageType requested doesn't exist");
		}

		return retriever;
	}

	/**
	 * retrieves the authorization URL to use for the storage authorization
	 * procedure
	 * 
	 * @param token
	 * @return
	 * @throws FilestorageException
	 */
	public String getAuthorizationURL(String token) throws FilestorageException {
		try {
			String response = RemoteConnector.getJSON(serverUrl, REQUEST_AUTH
					+ appId, token);
			return response;
		} catch (RemoteException e) {
			throw new FilestorageException(e);
		}
	}

}
