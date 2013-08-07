/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
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
 ******************************************************************************/
package eu.trentorise.smartcampus.filestorage.client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;

public class FilestorageTest {

	private static Filestorage filestorage;

	@BeforeClass
	public static void init() {
		filestorage = new Filestorage(TestConstants.BASEURL,
				TestConstants.APPID);
	}

	@Test
	public void apiTest() throws FilestorageException, URISyntaxException,
			IOException {

		// getting the applications accounts
		List<Storage> appAccounts = filestorage
				.getStoragesByApp(TestConstants.APP_AUTH_TOKEN);

		Account userAccount = createUserAccount(appAccounts.get(0));
		userAccount = filestorage.storeUserAccount(TestConstants.AUTH_TOKEN,
				userAccount);

		// getting the user accounts
		List<Account> userAccounts = filestorage
				.getAccountsByUser(TestConstants.AUTH_TOKEN);

		// store resource
		File sample = getFileSample(TestConstants.RESOURCE_NAME);
		Metadata meta = filestorage.storeResourceByUser(sample,
				TestConstants.AUTH_TOKEN, userAccount.getId(), true);
		Assert.assertNotNull(meta.getSocialId());

		// getting resource informations
		meta = filestorage.getResourceMetadata(TestConstants.AUTH_TOKEN,
				meta.getResourceId());
		Assert.assertEquals(TestConstants.RESOURCE_NAME, meta.getName());
		Assert.assertEquals(TestConstants.APPID, meta.getAppId());
		// getting a resource
		Resource res = filestorage.getMyResource(TestConstants.AUTH_TOKEN,
				meta.getResourceId());
		Assert.assertEquals(TestConstants.RESOURCE_NAME, res.getName());
		Assert.assertNotNull(res.getContent());

		// update resource
		filestorage.updateResourceByUser(TestConstants.AUTH_TOKEN,
				meta.getResourceId(),
				getFileSample(TestConstants.RESOURCE_NAME_UPDATE));

		// delete resource
		filestorage.deleteResourceByUser(TestConstants.AUTH_TOKEN,
				meta.getResourceId());
	}

	private File getFileSample(String filename) throws URISyntaxException {
		return new File(getClass().getResource("/" + filename).toURI());
	}

	private Account createUserAccount(Storage appAccount) {
		Account userAccount = new Account();
		userAccount.setStorageId(appAccount.getId());
		userAccount.setAppId(appAccount.getAppId());
		userAccount.setName("test dropbox user account");
		userAccount.setStorageType(StorageType.DROPBOX);
		userAccount.setConfigurations(getConfiguration());
		return userAccount;
	}

	private List<Configuration> getConfiguration() {
		return Arrays.asList(new Configuration("USER_KEY",
				TestConstants.USER_KEY), new Configuration("USER_SECRET",
				TestConstants.USER_SECRET));
	}
}
