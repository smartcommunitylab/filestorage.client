package eu.trentorise.smartcampus.filestorage.client;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;

public class TestUtils {

	public static Storage createStorage(String appId) {
		Storage storage = new Storage();
		storage.setAppId(appId);
		storage.setName("Sample storage " + System.currentTimeMillis());
		storage.setStorageType(StorageType.DROPBOX);
		storage.setConfigurations(createSampleStorageConfigurations());
		return storage;
	}

	public static Account createAccount(Storage storage, String userId) {
		Account account = new Account();
		account.setAppId(storage.getAppId());
		account.setName("Sample account " + System.currentTimeMillis());
		account.setStorageType(storage.getStorageType());
		account.setUserId(userId);
		account.setConfigurations(createSampleAccountConfigurations());
		return account;
	}

	private static List<Configuration> createSampleStorageConfigurations() {
		List<Configuration> confs = new ArrayList<Configuration>();
		confs.add(new Configuration("APP_KEY", TestConstants.APP_KEY));
		confs.add(new Configuration("APP_SECRET", TestConstants.APP_SECRET));
		return confs;
	}

	private static List<Configuration> createSampleAccountConfigurations() {
		List<Configuration> confs = new ArrayList<Configuration>();
		confs.add(new Configuration("USER_KEY", TestConstants.USER_KEY));
		confs.add(new Configuration("USER_SECRET", TestConstants.USER_SECRET));
		return confs;
	}

	public static File getResourceSample(String filename)
			throws URISyntaxException {
		return new File(TestUtils.class.getResource("/" + filename).toURI());
	}
}
