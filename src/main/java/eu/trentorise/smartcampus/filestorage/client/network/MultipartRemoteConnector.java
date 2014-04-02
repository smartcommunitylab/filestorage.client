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

package eu.trentorise.smartcampus.filestorage.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;

public class MultipartRemoteConnector extends RemoteConnector {

	public static InputStream getBinaryStream(String host, String service,
			String token) throws RemoteException {
		return getBinaryStream(host, service, token, null);
	}

	public static InputStream getBinaryStream(String host, String service,
			String token, Map<String, Object> parameters)
			throws RemoteException {
		String queryString = generateQueryString(parameters);
		final HttpResponse resp;
		final HttpGet get = new HttpGet(host + service + queryString);

		get.setHeader(RH_ACCEPT, "application/json");
		get.setHeader(RH_AUTH_TOKEN, bearer(token));

		try {
			resp = getHttpClient().execute(get);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return resp.getEntity().getContent();
			}
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN
					|| resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				throw new SecurityException();
			}
		} catch (ClientProtocolException e) {
			throw new RemoteException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
		return null;
	}

	public static String postJSON(String host, String service, String body,
			String token) throws SecurityException, RemoteException {
		return postJSON(host, service, body, token, null);
	}

	public static String postJSON(String host, String service, String token,
			MultipartParam... param) throws RemoteException {
		return postJSON(host, service, null, token, null, param);
	}

	public static String postJSON(String host, String service, String token,
			Map<String, Object> parameters, MultipartParam... param)
			throws RemoteException {
		return postJSON(host, service, null, token, parameters, param);
	}

	public static String postJSON(String host, String service, String body,
			String token, Map<String, Object> parameters)
			throws SecurityException, RemoteException {

		return postJSON(host, service, body, token, parameters,
				(MultipartParam[]) null);
	}

	private static String postJSON(String host, String service, String body,
			String token, Map<String, Object> parameters,
			MultipartParam... param) throws RemoteException {
		String queryString = generateQueryString(parameters);
		final HttpResponse resp;
		final HttpPost post = new HttpPost(host + service + queryString);

		post.setHeader(RH_ACCEPT, "application/json");
		post.setHeader(RH_AUTH_TOKEN, bearer(token));

		try {

			attachHttpEntity(post, body, param);

			resp = getHttpClient().execute(post);
			String response = EntityUtils.toString(resp.getEntity());
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN
					|| resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				throw new SecurityException();
			}

			String msg = "";
			try {
				msg = response.substring(response.indexOf("<h1>") + 4,
						response.indexOf("</h1>", response.indexOf("<h1>")));
			} catch (Exception e) {
				msg = resp.getStatusLine().toString();
			}
			throw new RemoteException(msg);
		} catch (ClientProtocolException e) {
			throw new RemoteException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RemoteException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	private static HttpEntity createHttpEntity(String entity)
			throws UnsupportedEncodingException {
		StringEntity httpEntity = new StringEntity(entity);
		httpEntity.setContentType("application/json");
		return httpEntity;
	}

	private static HttpEntity createHttpEntity(MultipartParam... entity) {
		MultipartEntity httpEntity = new MultipartEntity();
		for (MultipartParam param : entity) {
			if (param instanceof FileParam) {
				FileParam fileParam = (FileParam) param;
				httpEntity.addPart(fileParam.getParamName(), new FileBody(
						fileParam.getFile()));
			}

			if (param instanceof ByteArrayParam) {
				ByteArrayParam byteArrayParam = (ByteArrayParam) param;
				httpEntity.addPart(
						byteArrayParam.getParamName(),
						new ByteArrayBody(byteArrayParam.getContent(),
								byteArrayParam.getContentType(), byteArrayParam
										.getFilename()));
			}
		}
		return httpEntity;
	}

	private static HttpEntityEnclosingRequest attachHttpEntity(
			HttpEntityEnclosingRequest request, String body,
			MultipartParam... param) throws UnsupportedEncodingException {
		if (body != null) {
			request.setEntity(createHttpEntity(body));
		}

		if (param != null) {
			request.setEntity(createHttpEntity(param));
		}

		return request;
	}
}
