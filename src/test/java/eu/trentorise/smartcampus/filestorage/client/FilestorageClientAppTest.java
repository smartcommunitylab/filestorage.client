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

public class FilestorageClientAppTest {

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
		account = filestorage.createAccountByApp(TestConstants.APP_AUTH_TOKEN,
				account);
		int metadataSize = filestorage.getAllResourceMetadataByApp(
				TestConstants.APP_AUTH_TOKEN, null, null).size();
		File resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME);
		Metadata metadata = filestorage.storeResourceByApp(resource,
				TestConstants.APP_AUTH_TOKEN, account.getId(), false);

		Assert.assertEquals(TestConstants.APPID, metadata.getAppId());
		Assert.assertEquals(account.getId(), metadata.getAccountId());
		Assert.assertTrue(metadata.getResourceId() != null);
		boolean exceptionThrown = false;
		try {
			metadata = filestorage.storeResourceByApp(resource,
					TestConstants.APP_AUTH_TOKEN, account.getId(), false);
		} catch (FilestorageException e) {
			exceptionThrown = true;
		}

		Assert.assertTrue(exceptionThrown);

		byte[] resourceContent = FileUtils.readFileToByteArray(TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME_UPDATE));

		Metadata metadata1 = filestorage.storeResourceByApp(resourceContent,
				TestConstants.RESOURCE_CONTENT_TYPE,
				TestConstants.RESOURCE_NAME_UPDATE,
				TestConstants.APP_AUTH_TOKEN, account.getId(), false);

		Assert.assertTrue(metadata1.getResourceId() != null);

		filestorage.deleteResourceByApp(TestConstants.APP_AUTH_TOKEN,
				metadata1.getResourceId());

		Resource res = filestorage.getMyResourceByApp(
				TestConstants.APP_AUTH_TOKEN, metadata.getResourceId());
		Assert.assertNotNull(res);

		resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME_UPDATE);
		filestorage.updateResourceByApp(TestConstants.APP_AUTH_TOKEN,
				metadata.getResourceId(), resource);
		long resourceSize = metadata.getSize();
		metadata = filestorage.getResourceMetadataByApp(
				TestConstants.APP_AUTH_TOKEN, metadata.getResourceId());
		Assert.assertNotSame(resourceSize, metadata.getSize());

		Assert.assertEquals(
				metadataSize + 1,
				filestorage.getAllResourceMetadataByApp(
						TestConstants.APP_AUTH_TOKEN, -1, 0).size());

		filestorage.deleteResourceByApp(TestConstants.APP_AUTH_TOKEN,
				metadata.getResourceId());

	}

	@Test
	public void storage() throws FilestorageException {
		Storage storage = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);
		if (storage != null) {
			filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		}
		
		storage = TestUtils.createStorage(TestConstants.APPID);
		
		Storage newStorage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN, storage);
		Assert.assertNotNull(newStorage);
		storage = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);
		Assert.assertEquals(storage.getId(),newStorage.getId());
		Assert.assertEquals(storage.getAppId(),newStorage.getAppId());
		
		String newName = "Change name";
		storage.setName(newName);
		storage = filestorage.updateStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Storage reload = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);

		Assert.assertTrue(reload.getName().equals(newName)
				&& storage.getName().equals(newName));

		Assert.assertTrue(filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN));
		storage = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);
		Assert.assertNull(storage);
	}

	@Before
	public void cleanup() throws FilestorageException {
		Storage storage = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);
		List<Account> accounts = filestorage
				.getAccountsByApp(TestConstants.APP_AUTH_TOKEN);
		for (Account account : accounts) {
			if (account.getName().contains("Sample")) {
				filestorage.deleteAccountByApp(TestConstants.APP_AUTH_TOKEN,
						account.getId());
			}
		}
		if (storage != null) {
			filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		}

	}

	@Test
	public void account() throws SecurityException, FilestorageException {
		List<Account> accounts = filestorage
				.getAccountsByApp(TestConstants.APP_AUTH_TOKEN);
		int size = accounts.size();
		Storage storage = TestUtils.createStorage(TestConstants.APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils
				.createAccount(storage, TestConstants.USERID);
		account = filestorage.createAccountByApp(TestConstants.APP_AUTH_TOKEN,
				account);
		Assert.assertTrue(account != null && account.getId() != null);
		accounts = filestorage.getAccountsByApp(TestConstants.APP_AUTH_TOKEN);
		Assert.assertTrue(accounts.size() == size + 1);

		Account reloaded = filestorage.getAccountByApp(
				TestConstants.APP_AUTH_TOKEN, account.getId());
		Assert.assertTrue(reloaded.getId().equals(account.getId()));

		String newName = "Change name";
		account.setName(newName);
		filestorage.updateAccountByApp(TestConstants.APP_AUTH_TOKEN, account);
		reloaded = filestorage.getAccountByApp(TestConstants.APP_AUTH_TOKEN,
				account.getId());
		Assert.assertEquals(newName, reloaded.getName());
		filestorage.deleteAccountByApp(TestConstants.APP_AUTH_TOKEN,
				account.getId());
		filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		accounts = filestorage.getAccountsByApp(TestConstants.APP_AUTH_TOKEN);
		Assert.assertTrue(accounts.size() == size);

	}

}
