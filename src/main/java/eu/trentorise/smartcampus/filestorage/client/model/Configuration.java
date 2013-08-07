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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.network.JsonUtils;

/**
 * <i>Configuration</i> is the representation of a configuration in a
 * user/application storage account
 * 
 * @author mirko perillo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
	/**
	 * name of configuration
	 */
	private String name;
	/**
	 * value of configuration
	 */
	private String value;

	public Configuration() {

	}

	public Configuration(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static Configuration toObject(String json) {
		JSONObject object = new JSONObject();
		try {
			Configuration conf = new Configuration();
			conf.setName(object.getString("name"));
			conf.setValue(object.getString("value"));
			return conf;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<Configuration> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<Configuration> listElements = new ArrayList<Configuration>();
			for (int i = 0; array.optString(i).length() > 0; i++) {
				String subElement = array.getString(i);
				if (subElement != null) {
					listElements.add(toObject(subElement));
				}
			}
			return listElements;
		} catch (JSONException e) {
			return null;
		}
	}

	public static String toJson(Configuration conf) {
		try {
			StringWriter writer = new StringWriter();
			writer.write("{");
			writer.write(JSONObject.quote("name") + ":"
					+ JsonUtils.toJson(conf.getName()) + ",");
			writer.write(JSONObject.quote("value") + ":"
					+ JsonUtils.toJson(conf.getValue()));
			writer.write("}");
			return writer.toString();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
