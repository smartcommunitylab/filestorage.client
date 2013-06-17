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

import eu.trentorise.smartcampus.filestorage.client.model.AppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Configuration;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.StorageType;
import eu.trentorise.smartcampus.filestorage.client.model.UserAccount;

public class FilestorageTest {

	private static Filestorage filestorage;

	@BeforeClass
	public static void init() {
		filestorage = new Filestorage(TestConstants.BASEURL,
				TestConstants.APPNAME);
	}

	@Test
	public void apiTest() throws FilestorageException, URISyntaxException,
			IOException {

		// getting the applications accounts
		List<AppAccount> appAccounts = filestorage.getAppAccounts();

		UserAccount userAccount = createUserAccount(appAccounts.get(0));
		userAccount = filestorage.storeUserAccount(TestConstants.AUTH_TOKEN,
				userAccount);

		// getting the user accounts
		List<UserAccount> userAccounts = filestorage
				.getUserAccounts(TestConstants.AUTH_TOKEN);

		// store resource
		File sample = getFileSample(TestConstants.RESOURCE_NAME);
		Metadata meta = filestorage.storeResource(sample,
				TestConstants.AUTH_TOKEN, userAccount.getId(), true);
		Assert.assertNotNull(meta.getSocialId());

		// getting resource informations
		meta = filestorage.getResourceMetadata(TestConstants.AUTH_TOKEN,
				meta.getRid());
		Assert.assertEquals(TestConstants.RESOURCE_NAME, meta.getName());
		Assert.assertEquals(TestConstants.APPNAME, meta.getAppName());
		// getting a resource
		Resource res = filestorage.getMyResource(TestConstants.AUTH_TOKEN,
				meta.getRid());
		Assert.assertEquals(TestConstants.RESOURCE_NAME, res.getName());
		Assert.assertNotNull(res.getContent());

		// update resource
		filestorage.updateResource(TestConstants.AUTH_TOKEN,
				userAccount.getId(), meta.getRid(),
				getFileSample(TestConstants.RESOURCE_NAME_UPDATE));

		// delete resource
		filestorage.deleteResource(TestConstants.AUTH_TOKEN,
				userAccount.getId(), meta.getRid());
	}

	private File getFileSample(String filename) throws URISyntaxException {
		return new File(getClass().getResource("/" + filename).toURI());
	}

	private UserAccount createUserAccount(AppAccount appAccount) {
		UserAccount userAccount = new UserAccount();
		userAccount.setAppAccountId(appAccount.getId());
		userAccount.setAppName(appAccount.getAppName());
		userAccount.setAccountName("test dropbox user account");
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
