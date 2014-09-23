/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Menu extends Activity {
	
	Context ctx = this;
	Button searchButton;
	AutoCompleteTextView searchBar;
	TextView logo, betaLogo;
	CheckBox barNameChkBox, addressChkBox;
	final String filename = "MySharedString";
	String location, pointer;
	URL googlePlaces;
	
	double latitude = 0, longitude = 0;
	
	boolean flag;
	
	public ArrayAdapter<String> adapter;
	
	private static final String API_KEY = "AIzaSyCxz4mANPnVKNn61AWOfRHegltMppkEcww";
	
	SharedPreferences someData;
	File f = new File("/data/data/com.alpha.crawl/shared_prefs/MySharedString.xml");
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
			
		//final AlertDialog.Builder error_box = new AlertDialog.Builder(ctx);
		setupVariables();
		
		adapter = new ArrayAdapter<String>(this,R.layout.item_list);
		adapter.setNotifyOnChange(true);
		searchBar.setAdapter(adapter);
		searchBar.setThreshold(3);
		
		searchBar.setOnItemClickListener(new OnItemClickListener() { 

		    @Override
		    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		    	InputMethodManager imm = (InputMethodManager)getSystemService(
		    		      Context.INPUT_METHOD_SERVICE);
		    	imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
		    	searchBar.setDropDownHeight(0);
		    	searchButton.requestFocus();
		    }	   		    
		}); 
		
		searchBar.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub		
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub		
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					searchBar.setDropDownHeight(LayoutParams.WRAP_CONTENT);
					GetPlaces task = new GetPlaces();
					task.execute(searchBar.getText().toString());		
			}	
		});
							
	    searchButton.setOnClickListener(new View.OnClickListener() 
	    {
			public void onClick(View v) 
			{
				location = searchBar.getText().toString();
				getCoordinates(location);
				
				try
				{	
					if(pointer.equals("create")) {
						Class ourClass = Class.forName("com.alpha.crawl.Generate_Map");
						Intent ourIntent = new Intent(Main_Menu.this, ourClass);
						ourIntent.putExtra("latitude", latitude);
						ourIntent.putExtra("longitude", longitude);
						startActivity(ourIntent);
					}
				}
			    catch(ClassNotFoundException e)
			    {
				//	error_box.setTitle("Application Error");
				//	error_box.setNeutralButton("OK!", null);
				//	error_box.setMessage("Could not direct to desired page.\nPlease try again!");
				//	error_box.show();
			    } 
					//String test = yelp.search(latitude,longitude);
					//sendEmail(test);

			} 
		}); 
	    
	    
	   final OnCheckedChangeListener listener = new OnCheckedChangeListener() {
	    	 
	    	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
	    	if(isChecked){
	    	switch(arg0.getId())
	    	  {
	    	    case R.id.barNameChkBox:
	    	    	 addressChkBox.setChecked(false);
	    	         break;
	    	    case R.id.addressChkBox:
	    	    	 barNameChkBox.setChecked(false);
	    	         break;
	    	  }
	    	}
	    	}	 		    
	    };
	    
    	barNameChkBox.setOnCheckedChangeListener(listener);
    	addressChkBox.setOnCheckedChangeListener(listener);
    	
	}
    private void setupVariables() 
	{   	
    	searchButton  = (Button) findViewById(R.id.searchButton);
    	searchBar     = (AutoCompleteTextView) findViewById(R.id.searchBar); 
    	logo          = (TextView) findViewById(R.id.Logo); 
    	betaLogo      = (TextView) findViewById(R.id.betaLogo);
    	barNameChkBox = (CheckBox) findViewById(R.id.barNameChkBox);
    	addressChkBox = (CheckBox) findViewById(R.id.addressChkBox);
    	
    	Intent intent = getIntent();
    	someData = getSharedPreferences(filename, 0);	
		pointer = intent.getStringExtra("pointer");
    	
		GPSTracker gps = new GPSTracker(Main_Menu.this);
        if(gps.canGetLocation()){
        	latitude = gps.getLatitude();
        	longitude = gps.getLongitude(); 
        }
        else{
        	gps.showSettingsAlert();    
        }
   
    	Typeface tf = Typeface.createFromAsset(getAssets(),
	                "fonts/Stencil Cargo Army.ttf");
    	logo.setTypeface(tf);
		betaLogo.setTypeface(tf);
	}
	
	class GetPlaces extends AsyncTask<String, Void, ArrayList<String>>
	{
		@Override
		protected ArrayList<String> doInBackground(String... args)
		{
			Log.d("gottaGo", "doInBackground");

			ArrayList<String> predictionsArr = new ArrayList<String>();
			
			try
			{		   
				if(addressChkBox.isChecked()) {
					googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="  + 
											URLEncoder.encode(searchBar.getText().toString(), "UTF-8")            +
											"&types=geocode&location=" + String.valueOf(latitude) 	 		      +
											',' + String.valueOf(longitude) 							          +
											"&language=en&components=country:us" 							 	  +
											"&radius=1000&sensor=true&rankby=distance&key=" + API_KEY);
				} else {
					googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="  + 
							URLEncoder.encode(searchBar.getText().toString(), "UTF-8")       					  +
							"&types=establishment&location=" + String.valueOf(latitude) 	 			   		  +
							',' + String.valueOf(longitude) 							      				      +
							"&language=en&components=country:us" 							  					  +
							"&radius=1000&sensor=true&rankby=distance&key=" + API_KEY);
					
				}
				
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
				 
				String line;
		        StringBuffer sb = new StringBuffer();
		         
		        while ((line = in.readLine()) != null) {
		        	sb.append(line);
		        }
		         
		        JSONObject predictions = new JSONObject(sb.toString());
		        JSONArray ja = new JSONArray(predictions.getString("predictions"));

		        for (int i = 0; i < ja.length(); i++) {
		        	JSONObject jo = (JSONObject) ja.get(i);
		         	predictionsArr.add(jo.getString("description"));
		        }	 
			}
			catch (IOException e)
			{
				Log.e("YourApp", "GetPlaces : doInBackground", e);
			} 
			catch (JSONException e)
			{
				Log.e("YourApp", "GetPlaces : doInBackground", e);
			} 
			
			return predictionsArr;			
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			Log.d("YourApp", "onPostExecute : " + result.size());
			//update the adapter
			adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.auto_results);
			adapter.setNotifyOnChange(true);
			//attach the adapter to textview
			searchBar.setAdapter(adapter);
			for (String string : result) {
				Log.d("YourApp", "onPostExecute : result = " + string);
				adapter.add(string);
				adapter.notifyDataSetChanged();
			}

			Log.d("YourApp", "onPostExecute : autoCompleteAdapter" + adapter.getCount());
		}

	}	
				
    
	
    private void getCoordinates(String location){
    	
    	try
    	{	
    		if(addressChkBox.isChecked()) {
    			googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + 
    									URLEncoder.encode(location, "UTF-8")       							 +
    									"&types=geocode&language=en&components=country:us&sensor=true&key="  + API_KEY);
    		} else {
    			googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="       + 
										URLEncoder.encode(location, "UTF-8")       								   +
										"&types=establishment&language=en&components=country:us&sensor=true&key="  + API_KEY);
    			
    		}
    			

			URLConnection tc = googlePlaces.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			JSONObject predictions = new JSONObject(sb.toString());
			JSONArray ja = new JSONArray(predictions.getString("predictions"));

			JSONObject jo = (JSONObject) ja.get(0);
	        			
	    	googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/details/json?reference=" +
	    							jo.getString("reference") + "&sensor=true&key=" + API_KEY);
	    	
	    	URLConnection tc2 = googlePlaces.openConnection(); 
	    	BufferedReader in2 = new BufferedReader(new InputStreamReader(tc2.getInputStream()));
	    		
			String line2;
			StringBuffer sb2 = new StringBuffer();

			while ((line2 = in2.readLine()) != null) {
				sb2.append(line2);
			}
				
			JSONObject predictions2 = new JSONObject(sb2.toString());
					
			latitude =  predictions2.getJSONObject("result")
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
			longitude = predictions2.getJSONObject("result")
					.getJSONObject("geometry").getJSONObject("location")
		               .getDouble("lng");
    	}
		catch (IOException e)
		{
			Log.e("YourApp", "GetPlaces : doInBackground", e);
		} 
		catch (JSONException e)
		{
			Log.e("YourApp", "GetPlaces : doInBackground", e);
		}	
    } 
    
    protected void sendEmail(String body){
    	String me = "jprusso0@gmail.com";
    	Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
    	
    	email.setType("message/rfc822");
    	email.putExtra(Intent.EXTRA_EMAIL, me);
    	email.putExtra(Intent.EXTRA_SUBJECT, "test");
    	email.putExtra(Intent.EXTRA_TEXT, body);
    	startActivity(Intent.createChooser(email, "Choose an email client from..."));


    }
}