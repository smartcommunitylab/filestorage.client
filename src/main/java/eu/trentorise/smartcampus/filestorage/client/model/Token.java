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

package eu.trentorise.smartcampus.filestorage.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <i>Token</i> contains all the informations to download a resource. A resource
 * can be download using REST if fields url and methodREST are setted. Field
 * httpHeaders is setted if REST invocation MUST declare some HTTP headers. If
 * REST invocation is not available to download the resource, map metadata
 * contains all parameters to perform download using storage type APIs
 * 
 * @author mirko perillo
 * 
 */
public class Token {
	/* Token data */
	/**
	 * the set of security information to access the resource
	 */
	private Map<String, Object> metadata;

	/* parameters for REST invocation */

	/**
	 * HTTP method to access the resource
	 */
	private String methodREST;
	/**
	 * direct URL to access the resource
	 */
	private String url;
	/**
	 * optional HTTP headers to access the resource
	 */
	private Map<String, String> httpHeaders;

	/**
	 * storage which stores the resource
	 */
	private StorageType storageType;

	public Map<String, Object> getMetadata() {
		return metadata;

	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	public String getMethodREST() {
		return methodREST;
	}

	public void setMethodREST(String methodREST) {
		this.methodREST = methodREST;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
	}

	@SuppressWarnings("unchecked")
	public static Token toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Token result = new Token();
			result.setUrl(object.getString("url"));
			result.setStorageType(StorageType.valueOf(object
					.getString("storageType")));
			result.setMethodREST(object.getString("methodREST"));
			result.setMetadata(valueOf(object.getString("metadata")));
			result.setHttpHeaders(valueOf(object.getString("httpHeaders")));

			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map valueOf(String json) throws JSONException {
		if (JSONObject.stringToValue(json) == JSONObject.NULL) {
			return null;
		}
		Map object = new HashMap();
		JSONObject jsonObj = new JSONObject(json);
		Iterator<String> iter = jsonObj.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			object.put(key, jsonObj.get(key));
		}
		return object;

	}

	public static List<Token> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<Token> listElements = new ArrayList<Token>();
			for (int i = 0; array.optString(i).length() > 0; i++) {
				String subElement = array.getString(i);
				if (subElement != null) {
					listElements.add(toObject(subElement));
				}
			}
			return listElements;
		} catch (Exception e) {
			return null;
		}
	}
}
