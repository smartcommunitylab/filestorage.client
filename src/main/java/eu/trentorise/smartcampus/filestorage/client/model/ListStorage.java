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
package eu.trentorise.smartcampus.filestorage.client.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class to be complaint with internal protocol of list representation
 * 
 * @author mirko perillo
 * 
 */
@XmlRootElement(name = "storages")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListStorage {

	@XmlElement(name = "storages")
	private List<Storage> storages;

	public List<Storage> getStorages() {
		return storages;
	}

	public void setStorages(List<Storage> storages) {
		this.storages = storages;
	}

	public static ListStorage toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			ListStorage listStorage = new ListStorage();
			listStorage
					.setStorages(Storage.toList(object.getString("storages")));
			return listStorage;
		} catch (JSONException e) {
			return null;
		}
	}
}
