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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class to be complaint with internal protocol of list representation
 * 
 * @author mirko perillo
 * 
 */
public class ListAccount {
	private List<Account> accounts;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public static ListAccount toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			ListAccount listAccount = new ListAccount();
			listAccount
					.setAccounts(Account.toList(object.getString("accounts")));
			return listAccount;
		} catch (JSONException e) {
			return null;
		}
	}
}
