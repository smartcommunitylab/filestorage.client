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

import java.util.Map;

import eu.trentorise.smartcampus.filestorage.client.model.Token;

/**
 * Utility class to download a resource if in the {@link Token} are setted the
 * fields to perform a REST invocation
 * 
 * @author mirko perillo
 * 
 */
public class HttpResourceRetriever extends ResourceRetriever {

	public HttpResourceRetriever(String baseUrl, String appName) {
		super(baseUrl, appName);
	}

	@Override
	protected byte[] retrieveContent(Map<String, Object> tokenMetadata) {
		return null;
	}

}
