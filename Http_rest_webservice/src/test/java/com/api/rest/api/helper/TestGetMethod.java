package com.api.rest.api.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
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
		RestResponse response = RestApiHelper.performGetRequest(url,null);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		Assert.assertEquals("Hi! hello", response.getResponseBody());
		System.out.println(response.getResponseBody());
	}
	
	@Test
	public void testGetAll() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/all";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		RestResponse response = RestApiHelper.performGetRequest(url,headers);
		Assert.assertTrue("Expected status Code not found",(HttpStatus.SC_OK == response.getStatusCode()) || (HttpStatus.SC_NO_CONTENT == response.getStatusCode()));
		System.out.println(response.getResponseBody());
	}
	
	@Test
	public void testGetFindwithId() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/find/127";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		RestResponse response = RestApiHelper.performGetRequest(url,headers);
		Assert.assertTrue("Expected status Code not found",(HttpStatus.SC_OK == response.getStatusCode()) || (HttpStatus.SC_NOT_FOUND == response.getStatusCode()));
		//System.out.println(response.getResponseBody());
		/**
		 * Step 1 :- Use the GSON builder class to get the instance of GSON class
		 * Step 2 :- Use the Gson object to deseralize the json
		 * */
		if(HttpStatus.SC_NOT_FOUND != response.getStatusCode()){
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
		//headers.put("Authorization", "Basic YWRtaW46d2VsY29tZQ==");
		headers.put("Authorization", Base64.encodeBase64String("admin:welcome".getBytes()));
		RestResponse response = RestApiHelper.performGetRequest("http://localhost:8080/laptop-bag/webapi/secure/all", headers);
		System.out.println(response.toString());
	}
	

}
