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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Storage;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.socialservice.beans.Entity;
import eu.trentorise.smartcampus.socialservice.beans.EntityType;
import eu.trentorise.smartcampus.socialservice.beans.Visibility;

public class FilestorageClientUserTest {

	private static Filestorage filestorage;
	private static SocialService socialservice;

	@BeforeClass
	public static void init() {
		filestorage = new Filestorage(TestConstants.FILESTORAGE_ENDPOINT,
				TestConstants.FS_APPID);
		socialservice = new SocialService(TestConstants.SOCIALSERVICE_ENDPOINT);
	}

	@Test
	public void sharing() throws SecurityException, FilestorageException,
			URISyntaxException, SocialServiceException {

		Storage storage = TestUtils.createStorage(TestConstants.FS_APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils.createAccount(storage,
				TestConstants.USERID1);

		account = filestorage.createAccountByApp(TestConstants.APP_AUTH_TOKEN,
				account);

		File resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME);

		Metadata meta = filestorage.storeResourceByUser(resource,
				TestConstants.USER_AUTH_TOKEN, account.getId());

		try {
			filestorage.getResourceByUser(TestConstants.USER2_AUTH_TOKEN,
					meta.getResourceId());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		try {
			filestorage.getSharedResourceByUser(TestConstants.USER2_AUTH_TOKEN,
					meta.getResourceId());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		/**
		 * backend creates social entity and bind it to resource
		 */

		// create or retrieve an entitytype
		EntityType type = new EntityType("fs file", "application/octet-stream");
		type = socialservice.createEntityType(TestConstants.APP_AUTH_TOKEN,
				type);

		// creation social entity1 owned by user1
		Entity entity = new Entity();
		entity.setName("FS Client Test " + System.currentTimeMillis());
		entity.setLocalId("AAGG" + System.currentTimeMillis());
		entity.setType(type.getId());
		entity = socialservice.createOrUpdateUserEntityByApp(
				TestConstants.APP_AUTH_TOKEN, TestConstants.SOCIAL_APPID,
				TestConstants.USERID1, entity);

		try {
			filestorage.getSharedResourceByUser(TestConstants.USER2_AUTH_TOKEN,
					meta.getResourceId());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		meta = filestorage.updateSocialDataByApp(TestConstants.APP_AUTH_TOKEN,
				meta.getResourceId(), entity.getUri());

		try {
			filestorage.getSharedResourceByUser(TestConstants.USER2_AUTH_TOKEN,
					meta.getResourceId());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		// user2 try to update social data of user1 entity
		try {
			meta = filestorage.updateSocialDataByUser(
					TestConstants.USER2_AUTH_TOKEN, meta.getResourceId(),
					entity.getUri());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		// user2 try to bind user1 resource with dummy entity
		try {
			meta = filestorage.updateSocialDataByUser(
					TestConstants.USER2_AUTH_TOKEN, meta.getResourceId(),
					"entityDummyURI");
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		// creation social entity2 owned by user2
		Entity entity2 = new Entity();
		entity2.setName("FS Client Test " + System.currentTimeMillis());
		entity2.setLocalId("EEGG" + System.currentTimeMillis());
		entity2.setType(type.getId());
		entity2 = socialservice.createOrUpdateUserEntityByApp(
				TestConstants.APP_AUTH_TOKEN, TestConstants.SOCIAL_APPID,
				TestConstants.USERID2, entity2);

		// user2 try to bind user1 resource with owned entity
		try {
			meta = filestorage.updateSocialDataByUser(
					TestConstants.USER2_AUTH_TOKEN, meta.getResourceId(),
					entity2.getUri());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

		// resource shared with user2
		entity.setVisibility(new Visibility(Arrays
				.asList(TestConstants.USERID2), null, null));
		socialservice.updateUserEntityByApp(TestConstants.APP_AUTH_TOKEN,
				TestConstants.SOCIAL_APPID, TestConstants.USERID1, entity);

		Assert.assertNotNull(filestorage.getSharedResourceByUser(
				TestConstants.USER2_AUTH_TOKEN, meta.getResourceId()));

		// resource shared public
		entity.setVisibility(new Visibility(true));
		socialservice.updateUserEntityByUser(TestConstants.USER_AUTH_TOKEN,
				TestConstants.SOCIAL_APPID, entity);

		Assert.assertNotNull(filestorage.getSharedResourceByUser(
				TestConstants.USER2_AUTH_TOKEN, meta.getResourceId()));

		// resource no more shared
		entity.setVisibility(new Visibility());
		socialservice.updateUserEntityByUser(TestConstants.USER_AUTH_TOKEN,
				TestConstants.SOCIAL_APPID, entity);

		try {
			filestorage.getSharedResourceByUser(TestConstants.USER2_AUTH_TOKEN,
					meta.getResourceId());
			Assert.fail("SecurityException not thrown");
		} catch (FilestorageException e) {
		}

	}

	@Test
	public void resourceUpload() throws SecurityException,
			FilestorageException, URISyntaxException, IOException {
		Storage storage = TestUtils.createStorage(TestConstants.FS_APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils.createAccount(storage,
				TestConstants.USERID1);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		int metadataSize = filestorage.getAllResourceMetadataByApp(
				TestConstants.APP_AUTH_TOKEN, null, null).size();
		File resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME);
		Metadata metadata = filestorage.storeResourceByUser(resource,
				TestConstants.USER_AUTH_TOKEN, account.getId());
		Assert.assertEquals(TestConstants.FS_APPID, metadata.getAppId());
		Assert.assertEquals(account.getId(), metadata.getAccountId());
		Assert.assertTrue(metadata.getResourceId() != null);
		boolean exceptionThrown = false;
		try {
			metadata = filestorage.storeResourceByUser(resource,
					TestConstants.USER_AUTH_TOKEN, account.getId());
		} catch (FilestorageException e) {
			exceptionThrown = true;
		}

		Assert.assertTrue(exceptionThrown);

		byte[] resourceContent = FileUtils.readFileToByteArray(TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME_UPDATE));

		Metadata metadata1 = filestorage.storeResourceByUser(resourceContent,
				TestConstants.RESOURCE_CONTENT_TYPE,
				TestConstants.RESOURCE_NAME_UPDATE,
				TestConstants.USER_AUTH_TOKEN, account.getId());

		Assert.assertTrue(metadata1.getResourceId() != null);
	}

	@Test
	public void resources() throws SecurityException, FilestorageException,
			URISyntaxException, IOException {
		Storage storage = TestUtils.createStorage(TestConstants.FS_APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils.createAccount(storage,
				TestConstants.USERID1);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		int metadataSize = filestorage.getAllResourceMetadataByApp(
				TestConstants.APP_AUTH_TOKEN, null, null).size();
		File resource = TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME);
		Metadata metadata = filestorage.storeResourceByUser(resource,
				TestConstants.USER_AUTH_TOKEN, account.getId());
		Assert.assertEquals(TestConstants.FS_APPID, metadata.getAppId());
		Assert.assertEquals(account.getId(), metadata.getAccountId());
		Assert.assertTrue(metadata.getResourceId() != null);
		boolean exceptionThrown = false;
		try {
			metadata = filestorage.storeResourceByUser(resource,
					TestConstants.USER_AUTH_TOKEN, account.getId());
		} catch (FilestorageException e) {
			exceptionThrown = true;
		}

		Assert.assertTrue(exceptionThrown);

		byte[] resourceContent = FileUtils.readFileToByteArray(TestUtils
				.getResourceSample(TestConstants.RESOURCE_NAME_UPDATE));

		Metadata metadata1 = filestorage.storeResourceByUser(resourceContent,
				TestConstants.RESOURCE_CONTENT_TYPE,
				TestConstants.RESOURCE_NAME_UPDATE,
				TestConstants.USER_AUTH_TOKEN, account.getId());

		Assert.assertTrue(metadata1.getResourceId() != null);

		filestorage.deleteResourceByUser(TestConstants.USER_AUTH_TOKEN,
				metadata1.getResourceId());

		Resource res = filestorage.getResourceByUser(
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

		Assert.assertEquals(
				metadataSize + 1,
				filestorage.getAllResourceMetadataByApp(
						TestConstants.APP_AUTH_TOKEN, -1, null).size());
		filestorage.deleteResourceByUser(TestConstants.USER_AUTH_TOKEN,
				metadata.getResourceId());
	}

	@Before
	public void cleanup() throws FilestorageException {

		Storage storage = filestorage.getStorage(TestConstants.APP_AUTH_TOKEN);
		Account account = filestorage
				.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		if (account != null) {
			filestorage.deleteAccountByUser(TestConstants.USER_AUTH_TOKEN);
		}
		if (storage != null) {
			filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		}

	}

	@Test
	public void account() throws SecurityException, FilestorageException {
		Storage storage = TestUtils.createStorage(TestConstants.FS_APPID);
		storage = filestorage.createStorage(TestConstants.APP_AUTH_TOKEN,
				storage);
		Account account = TestUtils.createAccount(storage,
				TestConstants.USERID1);
		account = filestorage.createAccountByUser(
				TestConstants.USER_AUTH_TOKEN, account);
		Assert.assertTrue(account != null && account.getId() != null);
		account = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertNotNull(account);

		Account reloaded = filestorage
				.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertTrue(reloaded.getId().equals(account.getId()));

		String newName = "Change name";
		account.setName(newName);
		filestorage.updateAccountByUser(TestConstants.USER_AUTH_TOKEN, account);
		reloaded = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertEquals(newName, reloaded.getName());
		filestorage.deleteAccountByUser(TestConstants.USER_AUTH_TOKEN);
		filestorage.deleteStorage(TestConstants.APP_AUTH_TOKEN);
		account = filestorage.getAccountByUser(TestConstants.USER_AUTH_TOKEN);
		Assert.assertNull(account);

	}

}
