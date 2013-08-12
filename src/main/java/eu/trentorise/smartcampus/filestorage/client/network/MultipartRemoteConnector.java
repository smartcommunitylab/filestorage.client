package eu.trentorise.smartcampus.filestorage.client.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;

public class MultipartRemoteConnector extends RemoteConnector {

	public static String postJSON(String host, String service, String body,
			String token) throws SecurityException, RemoteException {
		return postJSON(host, service, body, token, null);
	}

	public static String postJSON(String host, String service, String token,
			FileParam... fileparam) throws RemoteException {
		return postJSON(host, service, null, token, null, fileparam);
	}

	public static String postJSON(String host, String service, String token,
			Map<String, Object> parameters, FileParam... fileparam)
			throws RemoteException {
		return postJSON(host, service, null, token, parameters, fileparam);
	}

	public static String postJSON(String host, String service, String body,
			String token, Map<String, Object> parameters)
			throws SecurityException, RemoteException {

		return postJSON(host, service, body, token, parameters,
				(FileParam[]) null);
	}

	private static String postJSON(String host, String service, String body,
			String token, Map<String, Object> parameters,
			FileParam... fileparam) throws RemoteException {
		String queryString = generateQueryString(parameters);
		final HttpResponse resp;
		final HttpPost post = new HttpPost(host + service + queryString);

		post.setHeader(RH_ACCEPT, "application/json");
		post.setHeader(RH_AUTH_TOKEN, bearer(token));

		try {

			attachHttpEntity(post, body, fileparam);
			// StringEntity input = new StringEntity(body);
			// input.setContentType("application/json");
			// post.setEntity(input);

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

	private static HttpEntity createHttpEntity(FileParam... entity) {
		MultipartEntity httpEntity = new MultipartEntity();
		for (FileParam param : entity) {
			httpEntity.addPart(param.getParamName(),
					new FileBody(param.getFile()));
		}
		return httpEntity;
	}

	private static HttpEntityEnclosingRequest attachHttpEntity(
			HttpEntityEnclosingRequest request, String body,
			FileParam... fileparam) throws UnsupportedEncodingException {
		if (body != null) {
			request.setEntity(createHttpEntity(body));
		}

		if (fileparam != null) {
			request.setEntity(createHttpEntity(fileparam));
		}

		return request;
	}
}
