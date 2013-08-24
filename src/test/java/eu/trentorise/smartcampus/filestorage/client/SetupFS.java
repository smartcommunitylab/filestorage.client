package eu.trentorise.smartcampus.filestorage.client;

import java.util.Arrays;

import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;

public class SetupFS {

	/**
	 * @param args
	 * @throws FilestorageException
	 * @throws SecurityException
	 */
	public static void main(String[] args) throws SecurityException,
			FilestorageException {
		Filestorage fs = new Filestorage("http://localhost:8088",
				"mobileTemplate");

		Storage storage = new Storage();
		storage.setAppId("mobileTemplate");
		storage.setName("mobile template storage");
		storage.setStorageType(StorageType.DROPBOX);

		storage.setConfigurations(Arrays.asList(new Configuration("APP_KEY",
				"yerhpkuav29wxou"), new Configuration("APP_SECRET",
				"7lprb483fpfj8ob")));

		storage = fs.createStorage("4770e7c6-91ee-41aa-bb35-99a90460136b",
				storage);
		System.out.println("Create storage " + storage.getId());

	}
}
