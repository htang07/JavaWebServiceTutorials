package com.api.rest.api.helper;

import java.util.HashMap;
import java.util.Map;

import com.api.rest.api.model.RestResponse;

public class TestTempMain {
	public static void main(String[] args) {
		String url = "http://localhost:8080/laptop-bag/webapi/api/all";
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		RestResponse response = RestApiHelper.performDeleteRequest(url, headers);
		
		//RestResponse response = RestApiHelper.performDeleteRequest(url, headers)
	}
}
