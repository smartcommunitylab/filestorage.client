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
