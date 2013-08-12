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
package eu.trentorise.smartcampus.filestorage.client.retriever;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import eu.trentorise.smartcampus.filestorage.client.Filestorage;
import eu.trentorise.smartcampus.filestorage.client.FilestorageException;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.Resource;
import eu.trentorise.smartcampus.filestorage.client.model.Token;
import eu.trentorise.smartcampus.filestorage.client.utils.Utils;

/**
 * Abstract utility class to download a resource given the {@link Token}
 * 
 * @author mirko perillo
 * 
 */
public abstract class ResourceRetriever {

	private Filestorage filestorage;

	public ResourceRetriever(String baseUrl, String appName) {
		filestorage = new Filestorage(baseUrl, appName);

	}

	public Resource getResource(String authToken, String resourceId,
			Token resourceToken, String operationType)
			throws ClientProtocolException, IOException, FilestorageException {

		Resource resource = new Resource();
		resource.setContent(getFileContent(resourceToken));

		Metadata metadata = null;
		if (operationType.equals(Filestorage.USER_OPERATION)) {
			metadata = filestorage.getResourceMetadataByUser(authToken,
					resourceId);
		}
		if (operationType.equals(Filestorage.APP_OPERATION)) {
			metadata = filestorage.getResourceMetadataByApp(authToken,
					resourceId);
		}
		resource.setId(metadata.getResourceId());
		resource.setContentType(metadata.getContentType());
		resource.setName(metadata.getName());
		return resource;
	}

	private byte[] getFileContent(Token token) throws ClientProtocolException,
			IOException {
		HttpUriRequest request = null;
		if (token.getUrl() != null && token.getMethodREST() != null) {
			if (token.getMethodREST().equals("GET")) {
				request = new HttpGet(token.getUrl());
			} else if (token.getMethodREST().equals("POST")) {
				request = new HttpPost(token.getUrl());
			} else if (token.getMethodREST().equals("PUT")) {
				request = new HttpPut(token.getUrl());
			} else if (token.getMethodREST().equals("DELETE")) {
				request = new HttpDelete(token.getUrl());
			}

			if (token.getHttpHeaders() != null) {
				for (Entry<String, String> entry : token.getHttpHeaders()
						.entrySet()) {
					request.setHeader(entry.getKey(), entry.getValue());
				}
			}
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream is = response.getEntity().getContent();
				return Utils.read(is);
			}
		} else if (token.getMetadata() != null) {
			return retrieveContent(token.getMetadata());
		}
		return null;
	}

	protected abstract byte[] retrieveContent(Map<String, Object> tokenMetadata);
}
