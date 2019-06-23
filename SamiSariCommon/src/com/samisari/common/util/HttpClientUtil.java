package com.samisari.common.util; 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

public class HttpClientUtil {
	public static class HttpState {
		public String uri;
		public String responseData;
		public int responseStatusCode;
		public String responseStatusPhrase;
		public Header[] responseHeaders;
		public List<Cookie> cookies;
		public List<NameValuePair> parameters;
		public byte[] responseBytes;
	}

	public static HttpContext getNewLocalContext() {
		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		// Create local HTTP context
		HttpContext localContext = new BasicHttpContext();
		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		return localContext;
	}

	public static HttpState goGet(String url, HttpClient httpclient)
			throws ClientProtocolException, IOException {
		HttpState state = new HttpState();
		state.uri=url;
		HttpContext localContext = getNewLocalContext();

		ByteArrayBuffer bf = new ByteArrayBuffer(0);
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget, localContext);
		state.cookies = ((BasicCookieStore) localContext
				.getAttribute("http.cookie-store")).getCookies();

		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();
		state.responseHeaders = response.getAllHeaders();
		state.parameters = null;
		HttpEntity loginPageEntity = response.getEntity();
		if (loginPageEntity != null) {
			InputStream instream = loginPageEntity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httpget.abort();
		}
		DebugUtils.dumpResponse("get_"+ url, state);
		return state;
	}

	public static HttpState goPost(String url, HttpClient httpclient,
			List<NameValuePair> formparams) throws ClientProtocolException,
			IOException {
		HttpState state = new HttpState();
		state.parameters = formparams;

		state.uri=url;
		// HttpContext localContext = getNewLocalContext();
		UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(
				formparams, "UTF-8");
		HttpPost httppost = new HttpPost(url);
		
		HttpResponse response;
		httppost.setEntity(requestEntity);
		response = httpclient.execute(httppost);
		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();
		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			ByteArrayBuffer bf = new ByteArrayBuffer(0);
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httppost.abort();
		}
		DebugUtils.dumpResponse("post_"+ url, state);
		return state;
	}

	public static HttpState goGet(String url, HttpClient httpclient,
			HttpContext context) throws ClientProtocolException, IOException {
		HttpState state = new HttpState();
		state.uri=url;
		HttpContext localContext = context == null ? getNewLocalContext()
				: context;

		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget, localContext);

		state.cookies = ((BasicCookieStore) localContext
				.getAttribute("http.cookie-store")).getCookies();
		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			ByteArrayBuffer bf = new ByteArrayBuffer(0);
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httpget.abort();
		}
		DebugUtils.dumpResponse("get_"+ url, state);
		return state;
	}


	public static void goGet(File file, String url, HttpClient httpclient) throws ClientProtocolException, IOException {
		HttpContext localContext = getNewLocalContext();

		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget, localContext);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			int size = (int) entity.getContentLength();
			FileOutputStream out = new FileOutputStream(file);
			InputStream instream = entity.getContent();
			byte[] tmp = new byte[20480];
			int nb = 0;
			int total = 0;
			while ((nb = instream.read(tmp)) != -1) {
				out.write(tmp, 0, nb);
				total += nb;
			}
			out.flush();
			out.close();
			httpget.abort();
		}

	}
	
	public static HttpState goGet(String url, HttpClient httpclient,
			HttpContext context, String addedCookie)
			throws ClientProtocolException, IOException {
		HttpState state = new HttpState();
		state.uri=url;
		HttpContext localContext = context == null ? getNewLocalContext()
				: context;

		HttpGet httpget = new HttpGet(url);
		/*
		 * String cookieHeader=addedCookie + "; "; for (Cookie
		 * cookie:((CookieStore
		 * )context.getAttribute(ClientContext.COOKIE_STORE)).getCookies()) {
		 * cookieHeader += cookie.getName() + "=" + cookie.getValue(); }
		 * httpget.addHeader("Cookie", cookieHeader);
		 */
		HttpResponse response = httpclient.execute(httpget, localContext);

		state.cookies = ((BasicCookieStore) localContext
				.getAttribute("http.cookie-store")).getCookies();
		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();

		HttpEntity loginPageEntity = response.getEntity();
		if (loginPageEntity != null) {
			InputStream instream = loginPageEntity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			ByteArrayBuffer bf = new ByteArrayBuffer(0);
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httpget.abort();
		}
		DebugUtils.dumpResponse("get_"+ url, state);
		return state;
	}

	public static HttpState goPost(String url, HttpClient httpclient,
			List<NameValuePair> formparams, HttpContext context)
			throws ClientProtocolException, IOException {
		HttpState state = new HttpState();
		state.parameters = formparams;
		state.uri=url;
		HttpContext localContext = context == null ? getNewLocalContext()
				: context;

		UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(
				formparams, "UTF-8");
		HttpPost httppost = new HttpPost(url);
		HttpResponse response;
		httppost.setEntity(requestEntity);
		response = httpclient.execute(httppost, localContext);
		state.cookies = ((BasicCookieStore) localContext
				.getAttribute("http.cookie-store")).getCookies();
		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();

		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			ByteArrayBuffer bf = new ByteArrayBuffer(0);
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httppost.abort();
		}
		DebugUtils.dumpResponse("post_"+ url, state);
		return state;
	}
	
	public static HttpState goGet(String url, HttpClient httpclient,
			HttpContext context, List<NameValuePair> headers) throws ClientProtocolException, IOException {
		HttpState state = new HttpState();
		state.uri=url;
		HttpContext localContext = context == null ? getNewLocalContext()
				: context;

		HttpGet httpget = new HttpGet(url);
		for (NameValuePair header:headers) {
			httpget.addHeader(header.getName(),header.getValue());
		}
		HttpResponse response = httpclient.execute(httpget, localContext);

		state.cookies = ((BasicCookieStore) localContext
				.getAttribute("http.cookie-store")).getCookies();
		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			ByteArrayBuffer bf = new ByteArrayBuffer(0);
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httpget.abort();
		}
		DebugUtils.dumpResponse("get_"+ url, state);
		return state;
	}
	public static HttpState goPost(String url, HttpClient httpclient,
			List<NameValuePair> formparams, HttpContext context, List<NameValuePair> headers)
			throws ClientProtocolException, IOException {
		HttpState state = new HttpState();
		state.parameters=formparams;
		state.uri=url;
		HttpContext localContext = context == null ? getNewLocalContext()
				: context;

		UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(
				formparams, "UTF-8");
		HttpPost httppost = new HttpPost(url);
		for (NameValuePair header:headers) {
			httppost.addHeader(header.getName(),header.getValue());
		}
		HttpResponse response;
		httppost.setEntity(requestEntity);
		response = httpclient.execute(httppost, localContext);
		state.cookies = ((BasicCookieStore) localContext
				.getAttribute("http.cookie-store")).getCookies();
		state.responseStatusCode = response.getStatusLine().getStatusCode();
		state.responseStatusPhrase = response.getStatusLine().getReasonPhrase();

		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			byte[] tmp = new byte[2048];
			int nb = 0;
			ByteArrayBuffer bf = new ByteArrayBuffer(0);
			while ((nb = instream.read(tmp)) != -1) {
				bf.append(tmp,0, nb);
				nb = 0;
			}
			state.responseBytes=bf.buffer();
			state.responseData = new String(state.responseBytes, Charset.forName(DebugUtils.getContentEncoding(state)));
			httppost.abort();
		}
		DebugUtils.dumpResponse("post_"+ url, state);
		return state;
	}
}
