package com.demo.exam.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The service class that makes call to the open source api
 * https://api.quotable.io/random and reads in the response
 *
 */
@Service
public class APICaller {

	protected JsonElement content;
	protected JsonElement tags;
	protected JsonElement auth;
	// flag that tells if the similar quote has been pulled from the api or not
	private boolean similarQuoteFlag;

	/**
	 * Default constructor
	 */
	public APICaller() {
		similarQuoteFlag = false;
		init(null);

	}

	/**
	 * Constructor with a parameter that corresponds to the previously liked quote
	 * tags
	 * 
	 * @param quoteType the liked quote tags
	 */
	public void newCall(String quoteType) {
		if (similarQuoteFlag) {
			// if a similar quote has been pulled; set flag to false
			// the similar quote from in-memory will be displayed on the view
			similarQuoteFlag = false;
		} else {
			// get a new quote from the api
			init(quoteType);
		}
	}

	/**
	 * Method responsible for making call to the quotes api and getting a response
	 * from it
	 * 
	 * @param quoteType the liked quote tags
	 */
	private void init(String quoteType) {
		try {
			URL url = null;

			// if user liked the quote, get a similar quote from the api and hold it in
			// memory till user presses display new quote button
			if (quoteType != null) {

				similarQuoteFlag = true;

				String requestedQuoteType = quoteType.substring(2, quoteType.length() - 2);
				requestedQuoteType = requestedQuoteType.replace("\"", "");
				// call the api with the filter tags
				url = new URL("https://api.quotable.io/random?tags=" + requestedQuoteType);
				// System.out.println("\n \n url with filters: " + url.toString() + "\n \n");
			} else {
				url = new URL("https://api.quotable.io/random");
			}

			// open connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// set request method
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			// register a response in case the connection to the api fails
			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			// instantiate a buffer reader to read the response
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

			StringBuilder apiResponse = new StringBuilder();
			String line;
			// store response to string
			while ((line = br.readLine()) != null) {
				apiResponse.append(line);
			}

			// close the connection
			connection.disconnect();

			//
			extractInformation(apiResponse.toString());

			// System.out.println("output: " + apiResponse);
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	/**
	 * Method responsible for parsing out the 'author', 'content' and 'tags' from
	 * the response
	 * 
	 * @param apiResponse response from the quotes api
	 */
	private void extractInformation(String apiResponse) {

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(apiResponse);
		JsonObject obj = element.getAsJsonObject();
		auth = obj.get("author");
		// System.out.println("auth: " + auth);

		content = obj.get("content");
		// System.out.println("content: " + content);

		tags = obj.get("tags");
		System.out.println("tags: " + tags);

	}

}
