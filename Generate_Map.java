/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Generate_Map extends FragmentActivity {

	Activity context;
	private static final String API_KEY = "AIzaSyCxz4mANPnVKNn61AWOfRHegltMppkEcww";

	SharedPreferences someData;
	final String filename = "MySharedString";
	
	String location;
	double latitude, longitude;
	
    final HashMap<String, Bar> markerMap = new HashMap<String, Bar>();
	
	Context ctx = this;
	
	URL googlePlaces;
	String flag = "N";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate__map);

		
		Intent intent = getIntent();
		someData = getSharedPreferences(filename, 0);	
		latitude = intent.getDoubleExtra("latitude", 0);
		longitude = intent.getDoubleExtra("longitude", 0);
	
		if(checkIfMapsIsOk(this)) {
			GoogleMap map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map))
	               .getMap();
			LatLng origin = new LatLng(latitude, longitude);
			CameraUpdate panToOrigin = CameraUpdateFactory.newLatLng(origin);
			map.moveCamera(panToOrigin);
			map.animateCamera(CameraUpdateFactory.zoomTo(16), 400, null);
			
			/* Map Starting Point */
			
			map.addMarker(new MarkerOptions()
		    .position(new LatLng(latitude, longitude))
		    .title("Starting Line"))
		    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
			
			getJSONresults(map);
			
			/*
			map.addMarker(new MarkerOptions()
		    .position(new LatLng(latitude, longitude))
		    .title("Starting Line"))
		    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bar)); */
			
			
		/*	Begin Drawing Route - Needs to be done
		 *
			PolylineOptions rectOptions = new PolylineOptions()
			.add(origin)
			.add(new LatLng(40.675866, -73.950248)).width(10);
			
			Polyline polyline = map.addPolyline(rectOptions) */
		}		  	  
	}
	
	public static boolean checkIfMapsIsOk(Activity context) {
	    int checkGooglePlayServices =   GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	    
	    if (checkGooglePlayServices != ConnectionResult.SUCCESS) { 
	        GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices, context, 1122).show();
	        return false;
	    } else {
	        return true;
	    }
	}
	
	private void getJSONresults(GoogleMap map) {
    	try {		
    		
    		final GoogleMap map_marker = map;

    		googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
    								"location=" + latitude + ',' + longitude +
    								"&rankby=distance&types=bar&sensor=false&key=" + API_KEY);
    		
    		URLConnection tc = googlePlaces.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
			 
			String line;
	        StringBuffer sb = new StringBuffer();
	        
	        while ((line = in.readLine()) != null) {
	        	sb.append(line);
	        }
	         
	        JSONObject predictions = new JSONObject(sb.toString());
			JSONArray ja = new JSONArray(predictions.getString("results"));

			/* Create New Bar Object for Each Search Result (Think about other info to store) */
	        for (int i = 0; i < ja.length(); i++) {
	        	
	        	/* Create New Bar Object */
	        	JSONObject jo = (JSONObject) ja.get(i);
	    		Bar o1 = new Bar();
	    		
	    		/* Save Bar Information to Bar Object */
	            o1.setBarName(jo.getString("name"));
	            o1.setBarAddress(jo.getString("vicinity"));
	            o1.setBarLatitude(jo.getJSONObject("geometry").getJSONObject("location")
	            					.getDouble("lat"));
	           
	            o1.setBarLongitude(jo.getJSONObject("geometry").getJSONObject("location")
    								 .getDouble("lng"));
	            
	            o1.setBarReference(jo.getString("reference"));
	            
	            // Gettting Google Place Photo Reference * NEEDS WORK *
	            //if(jo.getJSONArray("photos") != null) {}
	            //	o1.setBarImage(jo.getJSONArray("photos").toString());
	            	
	            o1.setDetailSaved(false);
	            		
	            		//jo.getString("image_url"));
       
	            markerMap.put(o1.getBarReference(), o1);
	            
				//if(!o1.getDetailSaved()) {
				//	getBarDetails(o1);
				//}
	            
	           // map_marker.setInfoWindowAdapter(new MyInfoWindowAdapter());
	            
                LatLng origin = new LatLng(o1.getBarLatitude(), o1.getBarLongitude());
                
				map_marker.addMarker(new MarkerOptions()
			    .position(new LatLng(o1.getBarLatitude(), o1.getBarLongitude()))
			    .title(o1.getBarName())
			    .position(origin)
			    .snippet(o1.getBarReference()))
			    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bar));
				
				map_marker.setOnMarkerClickListener(new OnMarkerClickListener()
				{
					@Override
					public boolean onMarkerClick(Marker marker) {
					   CameraUpdate panToOrigin = CameraUpdateFactory.newLatLng(marker.getPosition());
	       			   map_marker.moveCamera(panToOrigin);
	       			   
	       			   /* Option 1 - Use Google Maps Custom Info Adapter to Display Bar */
	       		       map_marker.setInfoWindowAdapter(new MyInfoWindowAdapter());
	       			   /* End Option 1													*/
	       			   
	       			   /* Option 2 - Custom Dialog Box - STILL IN PROGRESS				*/
	       			   /*
	       	       		final Dialog d = new Dialog(ctx);
	       	       		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
	       	       		d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
	       	       		d.setContentView(R.layout.custom_info_layout);
	       	       		
	       	       		Bar selectedBar = markerMap.get(marker.getSnippet());
	       	       		TextView tvTitle = (TextView)d.findViewById(R.id.barName);
	       	       		TextView tvSnippet = (TextView)d.findViewById(R.id.barStreet);
	       	       		ImageView ratingImage = (ImageView)d.findViewById(R.id.barRating);
	       	       		
	       				tvTitle.setText(selectedBar.getBarName());
	       				tvSnippet.setText(selectedBar.getBarAddress());
	       				Picasso.with(ctx).load(R.drawable.yelp_4).into(ratingImage);
	       				
	       	       		d.show(); */
	       	       		/* End Option 2 */
	       	       	
	       			   test(marker);
	       			   return true;
					}
					
				}); 
	            
	     /*     o1.setBarCityAddress(" " + jo.getJSONObject("location") 
	            				   .getString("city") +  ", " + 
	            				   jo.getJSONObject("location")
	            				   .getString("state_code") + " " +
	            				   jo.getJSONObject("location")
	            				   .getString("postal_code")
	            				   ); 
	            o1.setBarImage(jo.getString("image_url")); */
	                      
	            o1 = null;
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
	
	class MyInfoWindowAdapter implements InfoWindowAdapter{
	
		private final View myContentsView;
		TextView tvTitle; 
		TextView tvSnippet;
		ImageView ratingImage;
		ImageView barImage; 
	
		MyInfoWindowAdapter(){
			  myContentsView = getLayoutInflater().inflate(R.layout.custom_info_layout, null);
			  tvTitle = ((TextView)myContentsView.findViewById(R.id.barName));
			  tvSnippet = ((TextView)myContentsView.findViewById(R.id.barStreet));
			  ratingImage = ((ImageView)myContentsView.findViewById(R.id.barRating));
			  barImage = ((ImageView)myContentsView.findViewById(R.id.icon));
			
		}

		@Override
		public View getInfoContents(Marker marker) {		
			Bar selectedBar = markerMap.get(marker.getSnippet());

			// No need to resend to Yelp twice
		//	if(!selectedBar.getDetailSaved()) {
			//	getBarDetails(selectedBar);
		//	}
			
			if(selectedBar.getBarName() != null && selectedBar.getBarAddress() != null) {
				tvTitle.setText(selectedBar.getBarName());
				tvSnippet.setText(selectedBar.getBarAddress());
			}
			
			/*Uri image = Uri.parse(selectedBar.getBarRatingImage());
			Picasso.with(ctx).load(image).into(ratingImage);
			if(!selectedBar.getBarRatingImage().equals("")) {
				Picasso.with(ctx).load(selectedBar.getBarRatingImage()).into(ratingImage);	
				setRatingImage(selectedBar.getBarRating(), ratingImage);
				Picasso.with(ctx).load(R.drawable.yelp_4).into(ratingImage);
			}
			*/
			//sendEmail(selectedBar.getBarImage());
			return myContentsView;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private void getBarDetails(Bar selectedBar) {
		
		// Yelp API
		
		Yelp yelp = new Yelp("Zr5yD3ALWggPC9xb6kO1VQ","m6uAe6CxwIDfeeLBzqDcVXuA_30","xSpfNksNk4mJn4uo34TqcotULL1lkJoz","iFK-MGMzk0InP5OC75zZdcmfoJE");
		String test = yelp.search(selectedBar.getBarLatitude(),selectedBar.getBarLongitude(), selectedBar.getBarName());
		
		try {
			JSONObject predictions = new JSONObject(test);
			
			if((!predictions.getString("businesses").equals("[]"))) {
				JSONArray ja = new JSONArray(predictions.getString("businesses"));
				JSONObject jo = (JSONObject) ja.get(0);
				selectedBar.setBarRatingImage(jo.getString("rating_img_url_large"));
				selectedBar.setBarRating(jo.getString("rating"));
				selectedBar.setDetailSaved(true); 
				//sendEmail(jo.getString("rating"));
				//selectedBar.setBarImage(jo.getString("image_url"));
			} else {
				selectedBar.setBarRatingImage("");
				//selectedBar.setBarImage("");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		/* 
		 *  Google Place DETAILS API
		 * 
		 * 
		try {
			googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/details/json?reference=" 
									+ selectedBar.getBarReference() + "&sensor=false&key=" + API_KEY);
			
    		URLConnection tc = googlePlaces.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
			 
			String line;
	        StringBuffer sb = new StringBuffer();
	        
	        while ((line = in.readLine()) != null) {
	        	sb.append(line);
	        }
	         
	        JSONObject barDetails = new JSONObject(sb.toString());
			
	        if(barDetails.getJSONObject("result").getJSONArray("photos") != null)
	        	selectedBar.setBarName(barDetails.getJSONObject("result").getString("id"));		
					
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		*/
	}
	
	// Test Email for Yelp API
    protected void sendEmail(String body){
    	String me = "jprusso0@gmail.com";
    	Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
    	
    	email.setType("message/rfc822");
    	email.putExtra(Intent.EXTRA_EMAIL, me);
    	email.putExtra(Intent.EXTRA_SUBJECT, "test");
    	email.putExtra(Intent.EXTRA_TEXT, body);
    	startActivity(Intent.createChooser(email, "Choose an email client from..."));
    } 
    
    private void setRatingImage(String rating, ImageView ratingImage) {
    	
    	if(rating.equals("1"))
    		Picasso.with(ctx).load(R.drawable.yelp_1).into(ratingImage);
    	else if(rating.equals("1.5"))
    		Picasso.with(ctx).load(R.drawable.yelp_1_5).into(ratingImage);
    	else if(rating.equals("2"))
    		Picasso.with(ctx).load(R.drawable.yelp_2).into(ratingImage);
    	else if(rating.equals("2.5"))
    		Picasso.with(ctx).load(R.drawable.yelp_2_5).into(ratingImage);
    	else if(rating.equals("3"))
    		Picasso.with(ctx).load(R.drawable.yelp_3).into(ratingImage);
    	else if(rating.equals("3.5"))
    		Picasso.with(ctx).load(R.drawable.yelp_3_5).into(ratingImage);
    	else if(rating.equals("4"))
    		Picasso.with(ctx).load(R.drawable.yelp_4).into(ratingImage);
    	else if(rating.equals("4.5"))
    		Picasso.with(ctx).load(R.drawable.yelp_4_5).into(ratingImage);
    	else if(rating.equals("5"))
    		Picasso.with(ctx).load(R.drawable.yelp_4).into(ratingImage); 
    }
       
    public void test(Marker marker) {
    	marker.showInfoWindow();
    }
}
