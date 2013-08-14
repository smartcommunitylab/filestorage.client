package eu.trentorise.smartcampus.filestorage.client;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.Assert;

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
			URISyntaxException {
		Storage storage = TestUtils.createStorage(TestConstants.APPID);
		storage = filestorage.createStorageByApp(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils
				.createAccount(storage, TestConstants.USERID);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		int metadataSize = filestorage.getAllResourceMetadataByApp(
				TestConstants.APP_AUTH_TOKEN).size();
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

		Resource res = filestorage.getMyResourceByUser(
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

		Assert.assertEquals(metadataSize + 1, filestorage
				.getAllResourceMetadataByApp(TestConstants.APP_AUTH_TOKEN)
				.size());
		filestorage.deleteResourceByUser(TestConstants.USER_AUTH_TOKEN,
				metadata.getResourceId());
	}

	@Before
	public void cleanup() throws FilestorageException {
		List<Storage> storages = filestorage
				.getStoragesByApp(TestConstants.APP_AUTH_TOKEN);
		List<Account> accounts = filestorage
				.getAccountsByUser(TestConstants.USER_AUTH_TOKEN);
		for (Account account : accounts) {
			if (account.getName().contains("Sample")) {
				filestorage.deleteAccountByUser(TestConstants.USER_AUTH_TOKEN,
						account.getId());
			}
		}
		for (Storage storage : storages) {
			if (storage.getName().contains("Sample")) {
				filestorage.deleteStorageByApp(TestConstants.APP_AUTH_TOKEN,
						storage.getId());
			}
		}

	}

	@Test
	public void account() throws SecurityException, FilestorageException {
		List<Account> accounts = filestorage
				.getAccountsByUser(TestConstants.USER_AUTH_TOKEN);
		int size = accounts.size();
		Storage storage = TestUtils.createStorage(TestConstants.APPID);
		storage = filestorage.createStorageByApp(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils
				.createAccount(storage, TestConstants.USERID);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		Assert.assertTrue(account != null && account.getId() != null);
		accounts = filestorage.getAccountsByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertTrue(accounts.size() == size + 1);

		Account reloaded = filestorage.getAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account.getId());
		Assert.assertTrue(reloaded.getId().equals(account.getId()));

		String newName = "Change name";
		account.setName(newName);
		filestorage.updateAccountByUser(TestConstants.USER_AUTH_TOKEN, account);
		reloaded = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN,
				account.getId());
		Assert.assertEquals(newName, reloaded.getName());
		filestorage.deleteAccountByUser(TestConstants.USER_AUTH_TOKEN,
				account.getId());
		filestorage.deleteStorageByApp(TestConstants.APP_AUTH_TOKEN,
				storage.getId());
		accounts = filestorage.getAccountsByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertTrue(accounts.size() == size);

	}

}