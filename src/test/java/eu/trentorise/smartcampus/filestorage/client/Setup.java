package eu.trentorise.smartcampus.filestorage.client;

import java.util.Arrays;

import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;

public class Setup {

	/**
	 * @param args
	 * @throws FilestorageException
	 * @throws SecurityException
	 */
	public static void main(String[] args) throws SecurityException,
			FilestorageException {
		Filestorage fs = new Filestorage("https://vas-dev.smartcampuslab.it",
				"mobileTemplate");

		Storage storage = new Storage();
		storage.setAppId("mobileTemplate");
		storage.setName("mobile template storage");
		storage.setStorageType(StorageType.DROPBOX);

		storage.setConfigurations(Arrays.asList(new Configuration("APP_KEY",
				"yerhpkuav29wxou"), new Configuration("APP_SECRET",
				"7lprb483fpfj8ob")));

		storage = fs.createStorageByApp("b8986270-56d3-4d59-a13b-8b6963476592",
				storage);
		System.out.println("Create storage " + storage.getId());

	}
}
