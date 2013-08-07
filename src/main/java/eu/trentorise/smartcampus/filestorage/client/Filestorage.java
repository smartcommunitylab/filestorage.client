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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.trentorise.smartcampus.filestorage.client.RestCaller.RequestType;
import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.ListAccount;
import eu.trentorise.smartcampus.filestorage.client.model.ListStorage;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.Token;
import eu.trentorise.smartcampus.filestorage.client.retriever.HttpResourceRetriever;
import eu.trentorise.smartcampus.filestorage.client.retriever.ResourceRetriever;
import eu.trentorise.smartcampus.network.FileParam;
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

	private RestCaller restCaller = new RestCaller();
	private String serverUrl;
	private String baseUrl;
	private String appId;

	private static final String SERVICE = "/smartcampus.filestorage/";

	private static final String STORAGE = "storage/";
	private static final String ACCOUNT = "account/";
	private static final String RESOURCE = "resource/";
	private static final String SHARED_RESOURCE = "resource/";
	private static final String MY_RESOURCE = "myresource/";

	private static final String APP_OPERATION = "app/";
	private static final String USER_OPERATION = "user/";
	private static final String AUTH_HEADER = "AUTH_TOKEN";

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
	 * @param userAccountId
	 *            id of the user storage account in which store the resource
	 * @param createSocialData
	 *            true to create social entity associated to the resource
	 * @return information about resources
	 * @throws FilestorageException
	 */
	public Metadata storeResourceByUser(File resource, String authToken,
			String userAccountId, boolean createSocialData)
			throws FilestorageException {

		return storeResource(resource, authToken, userAccountId,
				createSocialData, USER_OPERATION);
		// try {
		// FileParam multipartParam = new FileParam(resource,
		// RESOURCE_PARAM_NAME);
		// Map<String, Object> parameters = new HashMap<String, Object>();
		// parameters.put("createSocialData", createSocialData);
		// String response = RemoteConnector.postJSON(serverUrl, SERVICE,
		// authToken, parameters, multipartParam);
		// return Metadata.toObject(response);
		// // String params = "?createSocialData=" + createSocialData;
		// // return restCaller.callOneResult(RequestType.POST, serverUrl
		// // + "/resource/" + appId + "/" + userAccountId + params,
		// // Arrays.asList(header), resource, "file", Metadata.class);
		// } catch (Exception e) {
		// logger.error("Exception storing resource", e);
		// throw new FilestorageException(e);
		// }
	}

	public Metadata storeResourceByApp(File resource, String authToken,
			String userAccountId, boolean createSocialData)
			throws FilestorageException {
		return storeResource(resource, authToken, userAccountId,
				createSocialData, APP_OPERATION);
	}

	private Metadata storeResource(File resource, String authToken,
			String userAccountId, boolean createSocialData, String operationType)
			throws FilestorageException {
		try {
			FileParam multipartParam = new FileParam(resource,
					RESOURCE_PARAM_NAME);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("createSocialData", createSocialData);

			String response = RemoteConnector.postJSON(serverUrl, SERVICE
					+ RESOURCE + "create/" + operationType + appId + "/"
					+ userAccountId, authToken, parameters, multipartParam);
			return Metadata.toObject(response);
			// String params = "?createSocialData=" + createSocialData;
			// return restCaller.callOneResult(RequestType.POST, serverUrl
			// + "/resource/" + appId + "/" + userAccountId + params,
			// Arrays.asList(header), resource, "file", Metadata.class);
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
			RemoteConnector.deleteJSON(serverUrl, SERVICE + RESOURCE
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
	 * @param userAccountId
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
		// HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		// try {
		// restCaller.callOneResult(RequestType.POST, serverUrl + "/resource/"
		// + appId + "/" + userAccountId + "/" + resourceId,
		// Arrays.asList(header), resource, "file", null);
		// } catch (Exception e) {
		//
		// }
	}

	public void updateResourceByApp(String authToken, String resourceId,
			File resource) throws FilestorageException {
		updateResource(authToken, resourceId, resource, APP_OPERATION);
	}

	private void updateResource(String authToken, String resourceId,
			File resource, String operationType) throws FilestorageException {
		FileParam multipartParam = new FileParam(resource, RESOURCE_PARAM_NAME);
		try {
			RemoteConnector.postJSON(serverUrl, SERVICE + RESOURCE
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
		// HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		// try {
		// return restCaller.callOneResult(RequestType.GET,
		// serverUrl + "/useraccount/" + appId, Arrays.asList(header),
		// ListAccount.class).getUserAccounts();
		// } catch (Exception e) {
		// logger.error("Exception getting user accounts", e);
		// throw new FilestorageException(e);
		// }
	}

	public List<Account> getAccountsByApp(String authToken)
			throws FilestorageException, SecurityException {
		return getAccounts(authToken, APP_OPERATION);
	}

	private List<Account> getAccounts(String authToken, String operationType)
			throws SecurityException, FilestorageException {
		try {
			String response = RemoteConnector.getJSON(serverUrl, SERVICE
					+ ACCOUNT + operationType + appId, authToken);
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
			String response = RemoteConnector.getJSON(serverUrl, SERVICE
					+ ACCOUNT + operationType + appId + "/" + accountId,
					authToken);
			return Account.toObject(response);
		} catch (RemoteException e) {
			logger.error(
					String.format("Exception getting account %s", accountId), e);
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
		// try {
		// return restCaller
		// .callOneResult(RequestType.GET,
		// serverUrl + "/appaccount/" + appId, null,
		// ListStorage.class).getStorages();
		// } catch (Exception e) {
		// logger.error("Exception getting user accounts", e);
		// throw new FilestorageException(e);
		// }
	}

	private List<Storage> getStorages(String authToken, String operationType)
			throws SecurityException, FilestorageException {
		try {
			String response = RemoteConnector.getJSON(serverUrl, SERVICE
					+ STORAGE + appId, authToken);
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
			String response = RemoteConnector.getJSON(serverUrl, SERVICE
					+ STORAGE + appId + "/" + storageId, authToken);
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
			String response = RemoteConnector.postJSON(serverUrl, SERVICE
					+ STORAGE + appId, Storage.toJson(storage), authToken);
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
			String response = RemoteConnector.deleteJSON(serverUrl, SERVICE
					+ STORAGE + appId + "/" + storageId, authToken);
			return Boolean.valueOf(response);
		} catch (RemoteException e) {
			logger.error(String
					.format("Exception deleting storage %s of app %s",
							storageId, appId), e);
			throw new FilestorageException(e);
		}
	}

	public Storage updateStorageByApp(String authToken, String storageId,
			Storage storage) throws FilestorageException {
		return updateStorage(authToken, storageId, storage, APP_OPERATION);
	}

	private Storage updateStorage(String authToken, String storageId,
			Storage storage, String operationType) throws FilestorageException {
		try {
			String response = RemoteConnector.putJSON(serverUrl, SERVICE
					+ STORAGE + appId + "/" + storageId,
					Storage.toJson(storage), authToken);
			return Storage.toObject(response);
		} catch (RemoteException e) {
			logger.error(String
					.format("Exception deleting storage %s of app %s",
							storageId, appId), e);
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
	 * @return the {@link Account} stored
	 * @throws FilestorageException
	 */
	public Account storeUserAccount(String authToken, Account userAccount)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.POST, serverUrl
					+ "/useraccount/" + appId, Arrays.asList(header),
					userAccount, Account.class);
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
			Token token = getResourceToken(authToken, resourceId, false);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
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
	public Resource getMyResource(String authToken, String resourceId)
			throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId, true);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token);
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
			return restCaller.callOneResult(RequestType.PUT, serverUrl
					+ "/updatesocial/" + appId + "/" + resourceId + "/"
					+ entityId, Arrays.asList(header), null, Metadata.class);
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
			return restCaller.callOneResult(RequestType.GET, serverUrl
					+ "/metadata/" + appId + "/" + resourceId,
					Arrays.asList(header), null, Metadata.class);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	private Token getResourceToken(String authToken, String resourceId,
			boolean owned) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {

			String functionality = owned ? "myresource" : "resource";

			return restCaller.callOneResult(RequestType.GET, serverUrl + "/"
					+ functionality + "/" + appId + "/" + resourceId,
					Arrays.asList(header), null, Token.class);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	private ResourceRetriever resourceRetrieverFactory(Token token) {
		ResourceRetriever retriever = null;
		switch (token.getStorageType()) {
		case DROPBOX:
			retriever = new HttpResourceRetriever(baseUrl, appId);
			break;

		default:
			throw new IllegalArgumentException(
					"StorageType requested doesn't exist");
		}

		return retriever;
	}

}
