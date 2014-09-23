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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Starting_line extends ListActivity{

	TextView logo, logo2, tt, bt, bt2;
    private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<Bar> m_bars = null;
    private BarAdapter m_adapter;
    private Runnable viewOrders;
    Context ctx = this;
	SharedPreferences someData;
	final String filename = "MySharedString";
	Typeface top_logo, detail;
	
	String location;
	double latitude, longitude;
	
	private static final String API_KEY = "AIzaSyCxz4mANPnVKNn61AWOfRHegltMppkEcww";
	URL googlePlaces;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starting_line);
		setupVariables();
		
		m_bars = new ArrayList<Bar>();
		this.m_adapter = new BarAdapter(this, R.layout.item_list,  m_bars);
		setListAdapter(this.m_adapter);
		
		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				Bar newbar = m_adapter.getItem(position);			
				try
				{	
				//	getSelectionDetails(newbar);
					
				//	if(newbar.getBarStatus() == true) {
						Class ourClass = Class.forName("com.alpha.crawl.Generate_Map");
						Intent ourIntent = new Intent(ctx, ourClass);
						ourIntent.putExtra("latitude", newbar.getBarLatitude());
						ourIntent.putExtra("longitude", newbar.getBarLongitude());
						startActivity(ourIntent);
				/*	} else {
						final AlertDialog.Builder error_box = new AlertDialog.Builder(ctx);
						error_box.setTitle("Get a Watch...");
						error_box.setNeutralButton("OK!", null);
						error_box.setMessage("Bar Not Open!");
						error_box.show(); 
					} */
					
				}
			    catch(ClassNotFoundException e)
			    {
			    /*	error_box.setTitle("Application Error");
					error_box.setNeutralButton("OK!", null);
					error_box.setMessage("Could not direct to desired page.\nPlease try again!");
					error_box.show(); */
			    } 	
				
			} });
		 
        viewOrders = new Runnable(){
            @Override
            public void run() {
                getOrders();
            }
        };
        
        
        m_ProgressDialog = ProgressDialog.show(Starting_line.this,    
                "Are you ready...", "Finding Closest Bars ...", true);
        m_ProgressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
                thread.start();
            }   
        }, 1500);       
    }

	private void setupVariables() {	
		Intent intent = getIntent();
		someData = getSharedPreferences(filename, 0);	
		location = intent.getStringExtra("location");
		latitude = intent.getDoubleExtra("latitude", 0);
		longitude = intent.getDoubleExtra("longitude", 0);
		
    	logo = (TextView) findViewById(R.id.Logo); 
    	logo2 = (TextView) findViewById(R.id.Logo2);;
    	   	
    	top_logo = Typeface.createFromAsset(getAssets(),
          "fonts/Stencil Cargo Army.ttf");
    	detail = Typeface.createFromAsset(getAssets(),
    	  "fonts/lvnm.ttf");
    			
    	logo.setTypeface(top_logo);
    	logo2.setTypeface(top_logo); 
	}
	
	private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
        	if(m_bars != null && m_bars.size() > 0) {
        		m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_bars.size();i++)
                	m_adapter.add(m_bars.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    private void getOrders(){
    	try {
    		getJSONresults();
        } 
    	catch (Exception e) { 
    		Log.e("BACKGROUND_PROC", e.getMessage());
        }
        runOnUiThread(returnRes);
    }
    
    private class BarAdapter extends ArrayAdapter<Bar> {

    	private ArrayList<Bar> items;

        public BarAdapter(Context context, int textViewResourceId, ArrayList<Bar> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	View v = convertView;
        	if (v == null) {
        		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		v = vi.inflate(R.layout.item_list, null);
            }
        	
            Bar o = items.get(position);
            
            if (o != null) {
            	ImageView barImage = (ImageView) v.findViewById(R.id.icon);
            	ImageView barStatus = (ImageView) v.findViewById(R.id.status);
              	TextView barName   = (TextView) v.findViewById(R.id.barName);
             	TextView barStreet = (TextView) v.findViewById(R.id.barStreet);
             	TextView barCity   = (TextView) v.findViewById(R.id.barCity);
             	if (barName != null) {
             		barName.setText(o.getBarName());   
             		barName.setTypeface(detail);
             	}
             	if(barStreet != null && barCity != null){
             		barStreet.setText(o.getBarStreetAddress());
             		//barCity.setText(o.getBarCityAddress());
             	}
             	if(barImage != null){
             	//	Picasso.with(ctx).load(o.getBarImage()).into(barImage);
             	}
             	
             	getSelectionDetails(o);
             	
             	/*
             	if(o.getBarStatus() == true) {
             		barStatus.setImageResource(R.drawable.traffic_green_light);
             	} else {
             		barStatus.setImageResource(R.drawable.traffic_red_light);
             	}          	*/
            }
            
            return v;
        }
        
        @Override
        public int getCount() {
            return items.size();
        }
        
        @Override
        public Bar getItem(int position) {
            return items.get(position);
        }
	}

    private void getJSONresults() {
    	try {		
    		Yelp yelp = new Yelp("Zr5yD3ALWggPC9xb6kO1VQ","m6uAe6CxwIDfeeLBzqDcVXuA_30","xSpfNksNk4mJn4uo34TqcotULL1lkJoz","iFK-MGMzk0InP5OC75zZdcmfoJE");
    		String test = yelp.search(latitude,longitude, "test");

			JSONObject predictions = new JSONObject(test);
			JSONArray ja = new JSONArray(predictions.getString("businesses"));
			//sendEmail(ja.toString());
			m_bars = new ArrayList<Bar>();
			
			/* Create New Bar Object for Each Search Result (Think about other info to store) */
	        for (int i = 0; i < ja.length(); i++) {
	        	
	        	/* Create New Bar Object */
	        	JSONObject jo = (JSONObject) ja.get(i);
	    		Bar o1 = new Bar();
	    		
	    		/* Save Bar Information to Bar Object */
	            o1.setBarName(jo.getString("name"));
	            o1.setBarAddress(jo.getString("vicinity"));
	            o1.setBarLatitude(jo.getJSONObject("result")
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat"));
	            o1.setBarLongitude(jo.getJSONObject("result")
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng"));
	            
	            String s = jo.getJSONObject("location")
     				   .getString("address");

	            /*
	            s = s.replace("]", " ");
	            s = s.replace("[", " ");
	            s = s.replace("\"", "");
	            o1.setBarStreetAddress(s);
	            o1.setBarCityAddress(" " + jo.getJSONObject("location") 
	            				   .getString("city") +  ", " + 
	            				   jo.getJSONObject("location")
	            				   .getString("state_code") + " " +
	            				   jo.getJSONObject("location")
	            				   .getString("postal_code")
	            				   );
	            o1.setBarImage(jo.getString("image_url"));
	            
	            m_bars.add(o1); */
	            
	            o1 = null;
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }  

    private boolean getSelectionDetails(Bar name) {
    	try {
    		String fmtBarName = name.getBarName();
    		if(fmtBarName.contains(" ")){
    			fmtBarName = name.getBarName().replace(" ", "%20");}
    			
			googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="  + 
									String.valueOf(latitude) + ',' + String.valueOf(longitude) 				 +
									"&rankby=distance&sensor=false&name=" + fmtBarName    +
									"&key=" + API_KEY);
			
			URLConnection tc = googlePlaces.openConnection(); 
			BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
			
			String line;
			StringBuffer sb = new StringBuffer();

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			
			JSONObject predictions = new JSONObject(sb.toString());
			JSONArray ja = new JSONArray(predictions.getString("results"));
			
			JSONObject jo = (JSONObject) ja.get(0);
			
			/* Initiate Latitude/Longitude/Bar Status (Open/Closed) */
			String chkName = jo.getString("name");

	        name.setBarLatitude(jo.getJSONObject("geometry")
	           					.getJSONObject("location")
	          			 		.getDouble("lat"));
	        name.setBarLongitude(jo.getJSONObject("geometry")
	            				.getJSONObject("location")
	           					.getDouble("lng")); 
	        name.setBarStatus(jo.getJSONObject("opening_hours")		
	        					.getBoolean("open_now"));
	         
			//Toast.makeText(getApplicationContext(),chkName,
   		        //    Toast.LENGTH_LONG).show();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	return true;
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
