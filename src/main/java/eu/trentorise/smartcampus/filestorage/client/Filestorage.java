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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.ListAccount;
import eu.trentorise.smartcampus.filestorage.client.model.ListStorage;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.Token;
import eu.trentorise.smartcampus.filestorage.client.network.FileParam;
import eu.trentorise.smartcampus.filestorage.client.network.MultipartRemoteConnector;
import eu.trentorise.smartcampus.filestorage.client.retriever.HttpResourceRetriever;
import eu.trentorise.smartcampus.filestorage.client.retriever.ResourceRetriever;
import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;

/**
 * Filestorage APIs
 * 
 * @author mirko perillo
 * 
 */
public class Filestorage {
	private static final Logger logger = Logger.getLogger(Filestorage.class);

	private String serverUrl;
	private String appId;

	private static final String SERVICE = "/smartcampus.filestorage/";

	private static final String STORAGE = "storage/";
	private static final String ACCOUNT = "account/";
	private static final String RESOURCE = "resource/";
	private static final String SHARED_RESOURCE = "sharedresource/";
	private static final String MY_RESOURCE = "resource/";
	private static final String METADATA = "metadata/";
	private static final String SOCIAL = "updatesocial/";

	public static final String APP_OPERATION = "app/";
	public static final String USER_OPERATION = "user/";

	private static final String RESOURCE_PARAM_NAME = "file";

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
		serverUrl += (serverUrl.endsWith("/")) ? "" : "/";
	}

	/**
	 * stores a resource in given user storage account
	 * 
	 * @param resource
	 *            the resource to store
	 * @param authToken
	 *            the authentication token
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

	public Metadata storeResourceByApp(File resource, String authToken,
			String accountId, boolean createSocialData)
			throws FilestorageException {
		return storeResource(resource, authToken, accountId, createSocialData,
				APP_OPERATION);
	}

	private Metadata storeResource(File resource, String authToken,
			String accountId, boolean createSocialData, String operationType)
			throws FilestorageException {
		try {
			FileParam multipartParam = new FileParam(resource,
					RESOURCE_PARAM_NAME);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("createSocialData", createSocialData);

			String response = MultipartRemoteConnector.postJSON(serverUrl,
					SERVICE + RESOURCE + "create/" + operationType + appId
							+ "/" + accountId, authToken, parameters,
					multipartParam);
			return Metadata.toObject(response);
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
	 * @param resourceId
	 *            if of resource to remove
	 * @throws FilestorageException
	 */
	public void deleteResourceByUser(String authToken, String resourceId)
			throws FilestorageException {
		deleteResource(authToken, resourceId, USER_OPERATION);
	}

	public void deleteResourceByApp(String authToken, String resourceId)
			throws FilestorageException {
		deleteResource(authToken, resourceId, APP_OPERATION);
	}

	private void deleteResource(String authToken, String resourceId,
			String operationType) throws FilestorageException {
		try {
			MultipartRemoteConnector.deleteJSON(serverUrl, SERVICE + RESOURCE
					+ operationType + appId + "/" + resourceId, authToken);
		} catch (Exception e) {
			logger.error(
					String.format("Exception updating resource %s", resourceId),
					e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * updates a resource
	 * 
	 * @param authToken
	 *            authentication token
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

	public void updateResourceByApp(String authToken, String resourceId,
			File resource) throws FilestorageException {
		updateResource(authToken, resourceId, resource, APP_OPERATION);
	}

	private void updateResource(String authToken, String resourceId,
			File resource, String operationType) throws FilestorageException {
		FileParam multipartParam = new FileParam(resource, RESOURCE_PARAM_NAME);
		try {
			MultipartRemoteConnector.postJSON(serverUrl, SERVICE + RESOURCE
					+ operationType + appId + "/" + resourceId, authToken,
					multipartParam);
		} catch (RemoteException e) {
			logger.error(
					String.format("Exception updating resource %s", resourceId),
					e);
			throw new FilestorageException(e);
		}
	}

/**
 	 * retrieves all the user storage accounts binded to the application name and to the authentication token
	 * @param authToken the authentication token
	 * @return list of {@link Account)
	 * @throws FilestorageException
 * @throws RemoteException 
 * @throws SecurityException 
	 */
	public List<Account> getAccountsByUser(String authToken)
			throws FilestorageException, SecurityException {
		return getAccounts(authToken, USER_OPERATION);
	}

	public List<Account> getAccountsByApp(String authToken)
			throws FilestorageException, SecurityException {
		return getAccounts(authToken, APP_OPERATION);
	}

	private List<Account> getAccounts(String authToken, String operationType)
			throws SecurityException, FilestorageException {
		try {
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					SERVICE + ACCOUNT + operationType + appId, authToken);
			return ListAccount.toObject(response).getAccounts();
		} catch (RemoteException e) {
			logger.error(String.format("Exception getting accounts"), e);
			throw new FilestorageException(e);
		}
	}

	public Account getAccountByApp(String authToken, String accountId)
			throws FilestorageException {
		return getAccount(authToken, accountId, APP_OPERATION);
	}

	public Account getAccountByUser(String authToken, String accountId)
			throws FilestorageException {
		return getAccount(authToken, accountId, USER_OPERATION);
	}

	private Account getAccount(String authToken, String accountId,
			String operationType) throws FilestorageException {
		try {
			String response = MultipartRemoteConnector
					.getJSON(serverUrl, SERVICE + ACCOUNT + operationType
							+ appId + "/" + accountId, authToken);
			return Account.toObject(response);
		} catch (RemoteException e) {
			logger.error(
					String.format("Exception getting account %s", accountId), e);
			throw new FilestorageException(e);
		}
	}

	public void deleteAccountByApp(String authToken, String accountId)
			throws FilestorageException {
		deleteAccount(authToken, accountId, APP_OPERATION);
	}

	public void deleteAccountByUser(String authToken, String accountId)
			throws FilestorageException {
		deleteAccount(authToken, accountId, USER_OPERATION);
	}

	private void deleteAccount(String authToken, String accountId,
			String operationType) throws FilestorageException {
		try {
			MultipartRemoteConnector.deleteJSON(serverUrl, SERVICE + ACCOUNT
					+ operationType + appId + "/" + accountId, authToken);
		} catch (RemoteException e) {
			logger.error(
					String.format("Exception getting account %s", accountId), e);
			throw new FilestorageException(e);
		}
	}

	public Account createAccountByApp(String authToken, Account account)
			throws FilestorageException {
		return createAccount(authToken, account, APP_OPERATION);
	}

	public Account createAccountByUser(String authToken, Account account)
			throws FilestorageException {
		return createAccount(authToken, account, USER_OPERATION);
	}

	private Account createAccount(String authToken, Account account,
			String operationType) throws FilestorageException {
		try {
			String response = MultipartRemoteConnector.postJSON(serverUrl,
					SERVICE + ACCOUNT + operationType + appId,
					Account.toJson(account), authToken);
			return Account.toObject(response);
		} catch (RemoteException e) {
			logger.error(String.format(
					"Exception creating new account %s for app %s",
					account.getName(), account.getAppId()), e);
			throw new FilestorageException(e);
		}
	}

	public void updateAccountByApp(String authToken, Account account)
			throws FilestorageException {
		updateAccount(authToken, account, APP_OPERATION);
	}

	public void updateAccountByUser(String authToken, Account account)
			throws FilestorageException {
		updateAccount(authToken, account, USER_OPERATION);
	}

	private void updateAccount(String authToken, Account account,
			String operationType) throws FilestorageException {
		try {
			MultipartRemoteConnector.putJSON(serverUrl, SERVICE + ACCOUNT
					+ operationType + appId + "/" + account.getId(),
					Account.toJson(account), authToken);
		} catch (RemoteException e) {
			logger.error(String.format(
					"Exception updating account %s for app %s",
					account.getName(), account.getAppId()), e);
			throw new FilestorageException(e);
		}
	}

	/**
	 * retrieves all the application storage account binded to the application
	 * name
	 * 
	 * @return the list of {@link Storage}
	 * @throws FilestorageException
	 */
	public List<Storage> getStoragesByApp(String authToken)
			throws FilestorageException {
		return getStorages(authToken, APP_OPERATION);
	}

	private List<Storage> getStorages(String authToken, String operationType)
			throws SecurityException, FilestorageException {
		try {
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					SERVICE + STORAGE + appId, authToken);
			return ListStorage.toObject(response).getStorages();
		} catch (RemoteException e) {
			logger.error(String.format("Exception reading storages of app %s",
					appId), e);
			throw new FilestorageException(e);
		}
	}

	public Storage getStorageByApp(String authToken, String storageId)
			throws FilestorageException {
		return getStorage(authToken, storageId, APP_OPERATION);
	}

	private Storage getStorage(String authToken, String storageId,
			String operationType) throws SecurityException,
			FilestorageException {
		try {
			String response = MultipartRemoteConnector.getJSON(serverUrl,
					SERVICE + STORAGE + appId + "/" + storageId, authToken);
			return Storage.toObject(response);
		} catch (RemoteException e) {
			logger.error(
					String.format("Exception reading storage %s of app %s",
							storageId, appId), e);
			throw new FilestorageException(e);
		}
	}

	public Storage createStorageByApp(String authToken, Storage storage)
			throws SecurityException, FilestorageException {
		return createStorage(authToken, storage, APP_OPERATION);
	}

	private Storage createStorage(String authToken, Storage storage,
			String operationType) throws SecurityException,
			FilestorageException {
		try {
			String response = MultipartRemoteConnector.postJSON(serverUrl,
					SERVICE + STORAGE + appId, Storage.toJson(storage),
					authToken);
			return Storage.toObject(response);
		} catch (RemoteException e) {
			logger.error(String.format("Exception creating storage for app %s",
					appId), e);
			throw new FilestorageException(e);
		}
	}

	public boolean deleteStorageByApp(String authToken, String storageId)
			throws FilestorageException {
		return deleteStorage(authToken, storageId, APP_OPERATION);
	}

	private boolean deleteStorage(String authToken, String storageId,
			String operationType) throws FilestorageException {

		try {
			String response = MultipartRemoteConnector.deleteJSON(serverUrl,
					SERVICE + STORAGE + appId + "/" + storageId, authToken);
			return Boolean.valueOf(response);
		} catch (RemoteException e) {
			logger.error(String
					.format("Exception deleting storage %s of app %s",
							storageId, appId), e);
			throw new FilestorageException(e);
		}
	}

	public Storage updateStorageByApp(String authToken, Storage storage)
			throws FilestorageException {
		return updateStorage(authToken, storage, APP_OPERATION);
	}

	private Storage updateStorage(String authToken, Storage storage,
			String operationType) throws FilestorageException {
		try {
			String response = MultipartRemoteConnector.putJSON(serverUrl,
					SERVICE + STORAGE + appId + "/" + storage.getId(),
					Storage.toJson(storage), authToken);
			return Storage.toObject(response);
		} catch (RemoteException e) {
			logger.error(String.format(
					"Exception deleting storage %s of app %s", storage.getId(),
					appId), e);
			throw new FilestorageException(e);
		}
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
	public Resource getMyResourceByApp(String authToken, String resourceId)
			throws FilestorageException {
		return getMyResource(authToken, resourceId, APP_OPERATION);
	}

	public Resource getMyResourceByUser(String authToken, String resourceId)
			throws FilestorageException {
		return getMyResource(authToken, resourceId, USER_OPERATION);
	}

	private Resource getMyResource(String authToken, String resourceId,
			String operationType) throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId, null, true,
					operationType);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token,
					operationType);
		} catch (Exception e) {
			logger.error(
					String.format("Exception getting resource %s", resourceId),
					e);
			throw new FilestorageException(e);
		}
	}

	public Resource getSharedResourceByApp(String authToken, String resourceId,
			String userId) throws FilestorageException {
		return getSharedResource(authToken, resourceId, userId, APP_OPERATION);
	}

	public Resource getSharedResourceByUser(String authToken, String resourceId)
			throws FilestorageException {
		return getSharedResource(authToken, resourceId, null, USER_OPERATION);
	}

	private Resource getSharedResource(String authToken, String resourceId,
			String userId, String operationType) throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId, userId,
					false, operationType);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token,
					operationType);
		} catch (Exception e) {
			logger.error(
					String.format("Exception getting resource %s", resourceId),
					e);
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

	public Metadata updateSocialDataByApp(String authToken, String resourceId,
			String entityId) throws FilestorageException {
		return updateSocialData(authToken, resourceId, entityId, APP_OPERATION);
	}

	private Metadata updateSocialData(String authToken, String resourceId,
			String entityId, String operationType) throws FilestorageException {
		try {
			String response = RemoteConnector.putJSON(serverUrl, SERVICE
					+ SOCIAL + appId + "/" + resourceId + "/" + entityId,
					authToken);
			return Metadata.toObject(response);
		} catch (RemoteException e) {
			logger.error(String
					.format("Exception updating social data of resource %s",
							resourceId), e);
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
	public Metadata getResourceMetadataByUser(String authToken,
			String resourceId) throws FilestorageException {
		return getResourceMetadata(authToken, resourceId, USER_OPERATION);
	}

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
					SERVICE + METADATA + uri, authToken);
			return Metadata.toObject(response);
		} catch (RemoteException e) {
			logger.error(String.format(
					"Exception getting metadata of resource %s", resourceId));
			throw new FilestorageException(e);
		}
	}

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
					SERVICE + METADATA + uri, authToken, parameters);
			return Metadata.toList(response);
		} catch (RemoteException e) {
			logger.error(String.format("Exception getting all metadata of %s",
					appId));
			throw new FilestorageException(e);
		}
	}

	private Token getResourceToken(String authToken, String resourceId,
			String userId, boolean owned, String operationType)
			throws FilestorageException {
		try {
			String functionality = owned ? MY_RESOURCE : SHARED_RESOURCE;
			String response = RemoteConnector.getJSON(serverUrl, SERVICE
					+ functionality
					+ operationType
					+ appId
					+ "/"
					+ (!owned && operationType.equals(APP_OPERATION) ? userId
							+ "/" : "") + resourceId, authToken);
			return Token.toObject(response);
		} catch (RemoteException e) {
			logger.error(
					String.format("Exception getting resource %s", resourceId),
					e);
			throw new FilestorageException(e);
		}
	}

	private ResourceRetriever resourceRetrieverFactory(Token token) {
		ResourceRetriever retriever = null;
		switch (token.getStorageType()) {
		case DROPBOX:
			retriever = new HttpResourceRetriever(serverUrl, appId);
			break;

		default:
			throw new IllegalArgumentException(
					"StorageType requested doesn't exist");
		}

		return retriever;
	}

}
