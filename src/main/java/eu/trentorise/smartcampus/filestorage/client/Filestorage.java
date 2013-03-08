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

public class Filestorage {
	private static final Logger logger = Logger.getLogger(Filestorage.class);

	private RestCaller restCaller = new RestCaller();
	private String serviceUrl;
	private String baseUrl;
	private String appName;

	private static final String contextPath = "smartcampus.filestorage";
	private static final String AUTH_HEADER = "AUTH_TOKEN";

	public Filestorage(String baseUrl, String appName) {
		this.appName = appName;
		this.baseUrl = baseUrl;
		serviceUrl = baseUrl;
		serviceUrl += (baseUrl.endsWith("/")) ? "" : "/";
		serviceUrl += contextPath;
	}

	public String storeResource(File resource, String authToken,
			String userAccountId) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.POST, serviceUrl
					+ "/resource/" + appName + "/" + userAccountId,
					Arrays.asList(header), resource, "file", String.class);
		} catch (Exception e) {
			logger.error("Exception storing resource", e);
			throw new FilestorageException(e);
		}
	}

	// public String storeResource(byte[] content, String contentType,
	// String resourceName, String authToken, String userAccountId) {
	// HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
	// try {
	// restCaller.callOneResult(RequestType.POST, serviceUrl
	// + "/resource/" + appName + "/" + userAccountId,
	// Arrays.asList(header), null, "file", String.class);
	// } catch (Exception e) {
	//
	// }
	// return null;
	// }

	public void deleteResource(String authToken, String userAccountId,
			String resourceId) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			restCaller.callOneResult(RequestType.DELETE, serviceUrl
					+ "/resource/" + appName + "/" + userAccountId + "/"
					+ resourceId, Arrays.asList(header), null, String.class);
		} catch (Exception e) {
			logger.error("Exception deleting resource", e);
			throw new FilestorageException(e);
		}
	}

	// public void updateResource(String authToken, String userAccountId,
	// String resourceId, byte[] content) {
	// HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
	// try {
	// restCaller.callOneResult(RequestType.POST, serviceUrl
	// + "/resource/" + appName + "/" + userAccountId,
	// Arrays.asList(header), null, "file", String.class);
	// } catch (Exception e) {
	//
	// }
	// }

	public void updateResource(String authToken, String userAccountId,
			String resourceId, File resource) {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			restCaller
					.callOneResult(RequestType.POST, serviceUrl + "/resource/"
							+ appName + "/" + userAccountId + "/" + resourceId,
							Arrays.asList(header), resource, "file", null);
		} catch (Exception e) {

		}
	}

	public List<UserAccount> getUserAccounts(String authToken)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.GET,
					serviceUrl + "/useraccount/" + appName,
					Arrays.asList(header), ListUserAccount.class)
					.getUserAccounts();
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}

	}

	public List<AppAccount> getAppAccounts() throws FilestorageException {
		try {
			return restCaller.callOneResult(RequestType.GET,
					serviceUrl + "/appaccount/" + appName, null,
					ListAppAccount.class).getAppAccounts();
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	public UserAccount storeUserAccount(String authToken,
			UserAccount userAccount) throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.POST, serviceUrl
					+ "/useraccount/" + appName, Arrays.asList(header),
					userAccount, UserAccount.class);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}

	}

	public Resource getResource(String authToken, String resourceId)
			throws FilestorageException {
		try {
			Token token = getResourceToken(authToken, resourceId);
			ResourceRetriever retriever = resourceRetrieverFactory(token);
			return retriever.getResource(authToken, resourceId, token);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}

	}

	public Metadata getResourceMetadata(String authToken, String resourceId)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.GET, serviceUrl
					+ "/metadata/" + appName + "/" + resourceId,
					Arrays.asList(header), null, Metadata.class);
		} catch (Exception e) {
			logger.error("Exception getting user accounts", e);
			throw new FilestorageException(e);
		}
	}

	private Token getResourceToken(String authToken, String resourceId)
			throws FilestorageException {
		HttpHeader header = new HttpHeader(AUTH_HEADER, authToken);
		try {
			return restCaller.callOneResult(RequestType.GET, serviceUrl
					+ "/resource/" + appName + "/" + resourceId,
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
			retriever = new HttpResourceRetriever(baseUrl, appName);
			break;

		default:
			throw new IllegalArgumentException(
					"StorageType requested doesn't exist");
		}

		return retriever;
	}

}
