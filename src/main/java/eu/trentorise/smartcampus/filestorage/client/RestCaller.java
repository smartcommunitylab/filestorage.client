package eu.trentorise.smartcampus.filestorage.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Utility class to perfom REST invocations
 * 
 * @author mirko perillo
 * 
 */
public class RestCaller {

	private static final Logger logger = Logger.getLogger(RestCaller.class);

	ObjectMapper mapper = new ObjectMapper();

	public static enum RequestType {
		GET, POST, PUT, DELETE
	};

	public <T> T callOneResult(RequestType type, String url,
			List<HttpHeader> headers, Class<T> responseClass)
			throws JsonGenerationException, JsonMappingException,
			UnsupportedEncodingException, IOException {
		HttpResponse response = call(type, url, headers, null, null, null);

		if (response.getStatusLine().getStatusCode() == 200) {
			String responseValue = extractResponseValue(response.getEntity()
					.getContent());
			return convertObject(responseValue, responseClass);
		}

		return null;
	}

	public <T> T callOneResult(RequestType type, String url,
			List<HttpHeader> headers, T bodyObject, Class<T> responseClass)
			throws JsonGenerationException, JsonMappingException,
			UnsupportedEncodingException, IOException {
		HttpResponse response = call(type, url, headers, bodyObject, null, null);

		if (response.getStatusLine().getStatusCode() == 200) {
			String json = extractResponseValue(response.getEntity()
					.getContent());
			if (json != null && json.length() > 0) {
				return convertObject(json, responseClass);
			}
		}

		return null;
	}

	public <T> T callOneResult(RequestType type, String url,
			List<HttpHeader> headers, File resource, String multipartParamName,
			Class<T> responseClass) throws JsonGenerationException,
			JsonMappingException, UnsupportedEncodingException, IOException {
		HttpResponse response = call(type, url, headers, null, resource,
				multipartParamName);

		if (response.getStatusLine().getStatusCode() == 200) {
			String json = extractResponseValue(response.getEntity()
					.getContent());
			return convertObject(json, responseClass);
		}

		return null;
	}

	public <T> List<T> callListResult(RequestType type, String url,
			List<HttpHeader> headers, Class<T> responseClass)
			throws JsonGenerationException, JsonMappingException,
			UnsupportedEncodingException, IOException {
		HttpResponse response = call(type, url, headers, null, null, null);

		if (response.getStatusLine().getStatusCode() == 200) {
			String json = extractResponseValue(response.getEntity()
					.getContent());
			return convertListObject(json, responseClass);
		}

		return null;
	}

	public <T> List<T> callListResult(RequestType type, String url,
			List<HttpHeader> headers, T bodyObject, Class<T> responseClass)
			throws JsonGenerationException, JsonMappingException,
			UnsupportedEncodingException, IOException {
		HttpResponse response = call(type, url, headers, bodyObject, null, null);

		if (response.getStatusLine().getStatusCode() == 200) {
			String json = extractResponseValue(response.getEntity()
					.getContent());
			return convertListObject(json, responseClass);
		}

		return null;
	}

	private <T> HttpResponse call(RequestType type, String url,
			List<HttpHeader> headers, T bodyObject, File resource,
			String multipartParamName) throws JsonGenerationException,
			JsonMappingException, UnsupportedEncodingException, IOException {

		HttpUriRequest request = convertType(type, url);
		request = attachEntity(request, bodyObject, resource,
				multipartParamName);

		if (headers != null) {
			for (HttpHeader header : headers) {
				request.addHeader(header.getName(), header.getValue());
			}
		}
		// add Accept: application/json header
		if (!request.containsHeader("Accept")) {
			request.addHeader("Accept", "application/json");
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		return response;
	}

	private <T> List<T> convertListObject(String json, Class<T> classType)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, mapper.getTypeFactory()
				.constructCollectionType(List.class, classType));
	}

	private <T> T convertObject(String json, Class<T> classType)
			throws JsonParseException, JsonMappingException, IOException {
		if (json.startsWith("{") || json.startsWith("[")) {
			return mapper.readValue(json, classType);
		} else {
			return (T) ConvertUtils.convert(json, classType);
		}
	}

	private String extractResponseValue(InputStream is) {
		String line = "";
		StringBuilder result = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			logger.error("Exception extracting json from http response", e);
		}

		return result.toString();
	}

	private <T> HttpUriRequest attachEntity(HttpUriRequest request,
			T bodyObject, File resource, String multipartParamName)
			throws JsonGenerationException, JsonMappingException,
			UnsupportedEncodingException, IOException {

		HttpEntity entity = null;
		if (bodyObject != null) {
			request.addHeader("Content-type", "application/json");
			entity = createHttpEntity(bodyObject);
		} else if (resource != null) {
			entity = createHttpEntity(resource, multipartParamName);
		}

		if (request instanceof HttpEntityEnclosingRequest && entity != null) {
			((HttpEntityEnclosingRequest) request).setEntity(entity);
		} else {
			logger.warn(String.format(
					"HttpRequest is invalid to attach entity: ", request
							.getClass().toString()));
		}

		return request;
	}

	private <T> HttpEntity createHttpEntity(T object)
			throws JsonGenerationException, JsonMappingException,
			UnsupportedEncodingException, IOException {
		return new StringEntity(convertToJson(object));
	}

	private HttpEntity createHttpEntity(File resource, String multipartParamName)
			throws IOException {
		MultipartEntity entity = new MultipartEntity();
		entity.addPart(multipartParamName, new FileBody(resource));
		return entity;
	}

	private <T> String convertToJson(T o) throws JsonGenerationException,
			JsonMappingException, IOException {
		return mapper.writeValueAsString(o);
	}

	private <T> HttpUriRequest convertType(RequestType type, String url) {
		if (url == null) {
			throw new IllegalArgumentException("URL cannot be null");
		}
		HttpUriRequest request = null;
		switch (type) {
		case GET:
			request = new HttpGet(url);
			break;
		case POST:
			request = new HttpPost(url);
			break;
		case PUT:
			request = new HttpPut(url);
			break;
		case DELETE:
			request = new HttpDelete(url);
			break;
		default:
			throw new IllegalArgumentException("RequestType doesn't exist");
		}
		return request;
	}
}
