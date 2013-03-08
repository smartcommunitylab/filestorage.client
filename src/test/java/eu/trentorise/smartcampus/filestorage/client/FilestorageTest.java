package eu.trentorise.smartcampus.filestorage.client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.smartcampus.filestorage.client.model.AppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;
import eu.trentorise.smartcampus.filestorage.client.model.UserAccount;

public class FilestorageTest {

	private static Filestorage filestorage;

	@BeforeClass
	public static void init() {
		filestorage = new Filestorage(TestConstants.BASEURL,
				TestConstants.APPNAME);
	}

	@Test
	public void apiTest() throws FilestorageException, URISyntaxException,
			IOException {

		// getting the applications accounts
		List<AppAccount> appAccounts = filestorage.getAppAccounts();

		UserAccount userAccount = createUserAccount(appAccounts.get(0));
		userAccount = filestorage.storeUserAccount(TestConstants.AUTH_TOKEN,
				userAccount);

		// getting the user accounts
		List<UserAccount> userAccounts = filestorage
				.getUserAccounts(TestConstants.AUTH_TOKEN);

		// store resource
		File sample = getFileSample(TestConstants.RESOURCE_NAME);
		String resourceId = filestorage.storeResource(sample,
				TestConstants.AUTH_TOKEN, userAccount.getId());

		// getting resource informations
		Metadata meta = filestorage.getResourceMetadata(
				TestConstants.AUTH_TOKEN, resourceId);
		Assert.assertEquals(TestConstants.RESOURCE_NAME, meta.getName());
		Assert.assertEquals(TestConstants.APPNAME, meta.getAppName());
		// getting a resource
		Resource res = filestorage.getResource(TestConstants.AUTH_TOKEN,
				resourceId);
		Assert.assertEquals(TestConstants.RESOURCE_NAME, res.getName());
		Assert.assertNotNull(res.getContent());

		// update resource
		filestorage.updateResource(TestConstants.AUTH_TOKEN,
				userAccount.getId(), resourceId,
				getFileSample(TestConstants.RESOURCE_NAME_UPDATE));

		// delete resource
		filestorage.deleteResource(TestConstants.AUTH_TOKEN,
				userAccount.getId(), resourceId);
	}

	private File getFileSample(String filename) throws URISyntaxException {
		return new File(getClass().getResource("/" + filename).toURI());
	}

	private UserAccount createUserAccount(AppAccount appAccount) {
		UserAccount userAccount = new UserAccount();
		userAccount.setAppAccountId(appAccount.getId());
		userAccount.setAppName(appAccount.getAppName());
		userAccount.setAccountName("test dropbox user account");
		userAccount.setStorageType(StorageType.DROPBOX);
		userAccount.setConfigurations(getConfiguration());
		return userAccount;
	}

	private List<Configuration> getConfiguration() {
		return Arrays.asList(new Configuration("USER_KEY",
				TestConstants.USER_KEY), new Configuration("USER_SECRET",
				TestConstants.USER_SECRET));
	}
}
