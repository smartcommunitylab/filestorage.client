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
