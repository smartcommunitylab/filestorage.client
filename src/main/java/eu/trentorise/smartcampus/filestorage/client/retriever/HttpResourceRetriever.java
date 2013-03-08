package eu.trentorise.smartcampus.filestorage.client.retriever;

import java.util.Map;

public class HttpResourceRetriever extends ResourceRetriever {

	public HttpResourceRetriever(String baseUrl, String appName) {
		super(baseUrl, appName);
	}

	@Override
	protected byte[] retrieveContent(Map<String, Object> tokenMetadata) {
		return null;
	}

}
