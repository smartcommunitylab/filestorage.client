package eu.trentorise.smartcampus.filestorage.client;

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;

public class TestUtils {

	public static Storage createStorage(String appId) {
		Storage storage = new Storage();
		storage.setAppId(appId);
		storage.setName("Sample storage " + System.currentTimeMillis());
		storage.setStorageType(StorageType.DROPBOX);
		storage.setConfigurations(createSampleConfigurations());
		return storage;
	}

	private static List<Configuration> createSampleConfigurations() {
		List<Configuration> confs = new ArrayList<Configuration>();
		confs.add(new Configuration("sampleName", "sampleValue"));
		return confs;
	}
}
