/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

import org.scribe.model.Token;
import org.scribe.builder.api.DefaultApi10a;

/**
 * Service provider for "2-legged" OAuth10a for Yelp API (version 2).
 */
public class Yelp2 extends DefaultApi10a {

  @Override
  public String getAccessTokenEndpoint() {
    return null;
  }

  @Override
  public String getAuthorizationUrl(Token arg0) {
    return null;
  }

  @Override
  public String getRequestTokenEndpoint() {
    return null;
  }

}