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
		Filestorage fs = new Filestorage("https://vas-dev.smartcampuslab.it/core.filestorage",
				"rk");

		Storage storage = new Storage();
		storage.setAppId("rk");
		storage.setName("rk mobile template storage");
		storage.setStorageType(StorageType.DROPBOX);

		storage.setConfigurations(Arrays.asList(new Configuration("APP_KEY",
				"yerhpkuav29wxou"), new Configuration("APP_SECRET",
				"7lprb483fpfj8ob")));

		storage = fs.createStorage("fb7b22f1-0c64-45e6-b0dd-61d3cb4d45ff", storage);
		System.out.println("Create storage " + storage.getId());

	}
}
