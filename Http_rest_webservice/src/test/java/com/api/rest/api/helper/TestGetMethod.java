package com.api.rest.api.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.api.rest.api.model.ResponseBody;
import com.api.rest.api.model.RestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestGetMethod {

	@Test
	public void testGetPingAlive() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/ping/hello";
		RestResponse response = RestApiHelper.performGetRequest(url, null);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		Assert.assertEquals("Hi! hello", response.getResponseBody());
		System.out.println(response.getResponseBody());
	}

	@Test
	public void testGetAll() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/all";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		RestResponse response = RestApiHelper.performGetRequest(url, headers);
		System.out.println(response.getResponseBody());
		Assert.assertTrue("Expected status Code not found", (HttpStatus.SC_OK == response.getStatusCode())
				|| (HttpStatus.SC_NO_CONTENT == response.getStatusCode()));
		System.out.println(response.getResponseBody());
	}

	@Test
	public void testGetFindwithId() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/find/127";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		RestResponse response = RestApiHelper.performGetRequest(url, headers);
		System.out.println(response.getResponseBody());
		Assert.assertTrue("Expected status Code not found", (HttpStatus.SC_OK == response.getStatusCode())
				|| (HttpStatus.SC_NOT_FOUND == response.getStatusCode()));
		// System.out.println(response.getResponseBody());
		/**
		 * Step 1 :- Use the GSON builder class to get the instance of GSON class Step 2
		 * :- Use the Gson object to deseralize the json
		 */
		if (HttpStatus.SC_NOT_FOUND != response.getStatusCode()) {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.serializeNulls().setPrettyPrinting().create();
			ResponseBody body = gson.fromJson(response.getResponseBody(), ResponseBody.class);
			Assert.assertEquals("Dell", body.getBrandName());
			Assert.assertEquals("127", body.getId());
			Assert.assertEquals("Latitude", body.getLaptopName());
		}

	}

	@Test
	public void testSecureGet() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		// headers.put("Authorization", "Basic YWRtaW46d2VsY29tZQ==");
		headers.put("Authorization", Base64.encodeBase64String("admin:welcome".getBytes()));
		RestResponse response = RestApiHelper.performGetRequest("http://localhost:8080/laptop-bag/webapi/secure/all",
				headers);
		System.out.println(response.toString());
	}

	/*
	 * Step 1: Create the HTTP Get method Step 2: Create the HTTP CLient Step 3:
	 * Execute the HTTP method using the client Step 4: Catch the response of
	 * execution Step 5: Display the response at the console
	 */
	@Test
	public void testGetSimpleGetRequest() {
		try {
			HttpGet get = new HttpGet("http://localhost:8080/laptop-bag/webapi/api/ping/hello");
			CloseableHttpClient client = HttpClientBuilder.create().build();
			CloseableHttpResponse response = client.execute(get);
			StatusLine status = response.getStatusLine();
			System.out.println(status.getStatusCode());
			System.out.println(status.getProtocolVersion());
			client.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHttpGetPingAlive() {
		HttpGet get = new HttpGet("http://localhost:8080/laptop-bag/webapi/api/ping/hello");
		try (CloseableHttpClient client = HttpClientBuilder.create().build();
				CloseableHttpResponse response = client.execute(get);) {
			StatusLine status = response.getStatusLine();
			System.out.println(status.getStatusCode());
			System.out.println(status.getProtocolVersion());
			ResponseHandler<String> body = new BasicResponseHandler();
			String getBody = body.handleResponse(response);
			System.out.println(getBody);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGetRestJsonResponseGetAll() {
		//Return 204 No content
		HttpGet get = new HttpGet("http://localhost:8080/laptop-bag/webapi/api/all");
		//HttpGet get = new HttpGet("http://localhost:8080/JSON-Support-Jersey-Example/rest/json/metallica/get");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		for (String str : headers.keySet()) {
			get.addHeader(str, headers.get(str));
		}

		CloseableHttpResponse response = null;
		try (CloseableHttpClient client = HttpClientBuilder.create().build();) {
			response = client.execute(get);
			ResponseHandler<String> body = new BasicResponseHandler();
			RestResponse restResponse = new RestResponse(response.getStatusLine().getStatusCode(),
					body.handleResponse(response));

			System.out.println(restResponse.getResponseBody());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
