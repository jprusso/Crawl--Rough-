/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

  OAuthService service;
  Token accessToken;

  /**
   * Setup the Yelp API OAuth credentials.
   *
   * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
   *
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service = new ServiceBuilder().provider(Yelp2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }

  /**
   * Search with term and location.
   *
   * @param term Search term
   * @param latitude Latitude
   * @param longitude Longitude
   * @return JSON string response
   */
  public String search(double latitude, double longitude, String barName) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("category_filter", "bars,restaurants");
    //request.addQuerystringParameter("category_filter", "Restaurants");
    request.addQuerystringParameter("sort", "0");
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("term", barName);
    request.addQuerystringParameter("radius_filter","100");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

}
