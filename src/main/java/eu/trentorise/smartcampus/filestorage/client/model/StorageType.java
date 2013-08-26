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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Types of storage
 * 
 * @author mirko perillo
 * 
 */
public enum StorageType {
	DROPBOX;

	public static StorageType toObject(String json) {
		JSONObject object;
		try {
			object = new JSONObject(json);
			return StorageType.valueOf(object.getString("storageType"));
		} catch (JSONException e) {
			return null;
		}
	}

	public static String toJson(StorageType storageType) {
		return JSONObject.quote(storageType.name());
	}
}
