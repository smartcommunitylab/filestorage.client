package eu.trentorise.smartcampus.filestorage.client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;

public class FilestorageClientUserTest {

	private static Filestorage filestorage;

	@BeforeClass
	public static void init() {
		filestorage = new Filestorage(TestConstants.BASEURL,
				TestConstants.APPID);
	}

	@Test
	public void resources() throws SecurityException, FilestorageException,
			URISyntaxException, IOException {
		Storage storage = TestUtils.createStorage(TestConstants.APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils
				.createAccount(storage, TestConstants.USERID);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		int metadataSize = filestorage.getAllResourceMetadataByApp(
				TestConstants.APP_AUTH_TOKEN, null, null).size();
		File resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME);
		Metadata metadata = filestorage.storeResourceByUser(resource,
				TestConstants.USER_AUTH_TOKEN, account.getId(), false);
		Assert.assertEquals(TestConstants.APPID, metadata.getAppId());
		Assert.assertEquals(account.getId(), metadata.getAccountId());
		Assert.assertTrue(metadata.getResourceId() != null);
		boolean exceptionThrown = false;
		try {
			metadata = filestorage.storeResourceByUser(resource,
					TestConstants.USER_AUTH_TOKEN, account.getId(), false);
		} catch (FilestorageException e) {
			exceptionThrown = true;
		}

		Assert.assertTrue(exceptionThrown);

		byte[] resourceContent = FileUtils.readFileToByteArray(TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME_UPDATE));

		Metadata metadata1 = filestorage.storeResourceByUser(resourceContent,
				TestConstants.RESOURCE_CONTENT_TYPE,
				TestConstants.RESOURCE_NAME_UPDATE,
				TestConstants.USER_AUTH_TOKEN, account.getId(), false);

		Assert.assertTrue(metadata1.getResourceId() != null);

		filestorage.deleteResourceByUser(TestConstants.USER_AUTH_TOKEN,
				metadata1.getResourceId());

		Resource res = filestorage.getResourceByUser(
				TestConstants.USER_AUTH_TOKEN, metadata.getResourceId());
		Assert.assertNotNull(res);

		resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME_UPDATE);
		filestorage.updateResourceByUser(TestConstants.USER_AUTH_TOKEN,
				metadata.getResourceId(), resource);
		long resourceSize = metadata.getSize();
		metadata = filestorage.getResourceMetadataByUser(
				TestConstants.USER_AUTH_TOKEN, metadata.getResourceId());
		Assert.assertNotSame(resourceSize, metadata.getSize());

		Assert.assertEquals(
				metadataSize + 1,
				filestorage.getAllResourceMetadataByApp(
						TestConstants.APP_AUTH_TOKEN, -1, null).size());
		filestorage.deleteResourceByUser(TestConstants.USER_AUTH_TOKEN,
				metadata.getResourceId());
	}

	@Before
	public void cleanup() throws FilestorageException {

		Storage storage = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);
		Account account = filestorage
				.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		if (account != null) {
			filestorage.deleteAccountByUser(TestConstants.USER_AUTH_TOKEN);
		}
		if (storage != null) {
			filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		}

	}

	@Test
	public void account() throws SecurityException, FilestorageException {
		Storage storage = TestUtils.createStorage(TestConstants.APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils
				.createAccount(storage, TestConstants.USERID);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		Assert.assertTrue(account != null && account.getId() != null);
		account = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertNotNull(account);

		Account reloaded = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertTrue(reloaded.getId().equals(account.getId()));

		String newName = "Change name";
		account.setName(newName);
		filestorage.updateAccountByUser(TestConstants.USER_AUTH_TOKEN, account);
		reloaded = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertEquals(newName, reloaded.getName());
		filestorage.deleteAccountByUser(TestConstants.USER_AUTH_TOKEN);
		filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		account = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertNull(account);

	}

}
