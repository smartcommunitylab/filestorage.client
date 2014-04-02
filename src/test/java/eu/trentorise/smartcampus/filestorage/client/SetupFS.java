/**
 *    Copyright 2012-2013 Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
				""), new Configuration("APP_SECRET",
				"")));

		storage = fs.createStorage("", storage);
		System.out.println("Create storage " + storage.getId());

	}
}
