/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/

package com.alpha.crawl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

public class Splash_Screen extends Activity 
{
	Context ctx = this;
	/*
	final File path = Environment.getExternalStorageDirectory(); 
	final File ward_haffey_menu = new File(path + "/ward_haffey.pdf");
	final File murphy_menu = new File(path + "/murphy.pdf");
	final File cyber_menu = new File(path + "/cyber.pdf");
	final File cardinal_menu = new File(path + "/cardinal.pdf");
	final File fishbowl_menu = new File(path + "/fishbowl.pdf");
	final File promo_calendar = new File(path + "/promo.pdf");
	*/
	URL url = null;
	HttpURLConnection c = null;
	String fileName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        
        final AlertDialog.Builder error_box = new AlertDialog.Builder(ctx);
        
        if(isOnline() == true)
	    {
	       //  new downloadfile().execute("");
	    }
        else
        {
            error_box.setTitle("Internet Connection Error");
    		error_box.setNeutralButton("OK!", null);
    		error_box.setMessage("No internet connection detected.\n\n" +
    					                   "**Menus may be unavailable or out-of-date.");
    		error_box.show();
        }
        
		Thread timer = new Thread() 
		{
			public void run()
			{
				try
				{
					sleep(2000);
				} 
				catch(InterruptedException e) 
				{
					error_box.setTitle("Application Error");
					error_box.setNeutralButton("OK!", null);
					error_box.setMessage("Could not direct to desired page.\nPlease try again!");
					error_box.show();
				} 
				finally
				{
					//downloadYelpImages();
					Intent intent = new Intent("com.alpha.crawl.New_Menu");	
					startActivity(intent);
				}		
			}			
		};
	
		timer.start();
	}
	
	protected void onPause() 
	{
		super.onPause();
		finish();
	}
	/*
	private class downloadfile extends AsyncTask<String, Void, String>
	{
		String s;
		
		@Override
		protected String doInBackground(String... arg0) 
		{
			final AlertDialog.Builder error_box = new AlertDialog.Builder(ctx);
			
			try 
	        {
			    BufferedInputStream inStream;
			    BufferedOutputStream outStream;
			    FileOutputStream fileStream;
	    		
			    url = new URL("http://www.sjfc.edu/dotAsset/86815.pdf");
		        HttpURLConnection c = (HttpURLConnection)  url.openConnection();
		        c.setRequestMethod("GET");
		        c.setDoOutput(true);
		        c.connect();	
		        fileName = "ward_haffey.pdf";
		        
		        inStream = new BufferedInputStream(c.getInputStream());
			    fileStream = new FileOutputStream(ward_haffey_menu);
			    outStream = new BufferedOutputStream(fileStream, 1024);
			    
			    byte[] data = new byte[1024];
			    int bytesRead = 0;
			    
			    while((bytesRead = inStream.read(data, 0, data.length)) >= 0)
		        {
		            outStream.write(data, 0, bytesRead);
		        }
			    
			    outStream.flush();
			    outStream.close();
		        fileStream.close();
		        inStream.close(); 
		        
		        url = new URL("http://www.sjfc.edu/dotAsset/86823.pdf");
		        c = (HttpURLConnection)  url.openConnection();
		        c.setRequestMethod("GET");
		        c.setDoOutput(true);
		        c.connect();	
		        fileName = "murphy.pdf";
		        
		        inStream = new BufferedInputStream(c.getInputStream());
			    fileStream = new FileOutputStream(murphy_menu);
			    outStream = new BufferedOutputStream(fileStream, 1024);
			    
			    byte[] data2 = new byte[1024];
			    bytesRead = 0;
			    
			    while((bytesRead = inStream.read(data2, 0, data2.length)) >= 0)
		        {
		            outStream.write(data2, 0, bytesRead);
		        }
			    
			    outStream.flush();
			    outStream.close();
		        fileStream.close();
		        inStream.close();
		        
		        url = new URL("http://www.sjfc.edu/dotAsset/86819.pdf");
		        c = (HttpURLConnection)  url.openConnection();
		        c.setRequestMethod("GET");
		        c.setDoOutput(true);
		        c.connect();	
		        fileName = "cyber.pdf";
		        
		        inStream = new BufferedInputStream(c.getInputStream());
			    fileStream = new FileOutputStream(cyber_menu);
			    outStream = new BufferedOutputStream(fileStream, 1024);
			    
			    byte[] data3 = new byte[1024];
			    bytesRead = 0;
			    
			    while((bytesRead = inStream.read(data3, 0, data3.length)) >= 0)
		        {
		            outStream.write(data3, 0, bytesRead);
		        }
			    
			    outStream.flush();
			    outStream.close();
		        fileStream.close();
		        inStream.close();
		        
		        url = new URL("http://www.sjfc.edu/dotAsset/86817.pdf");
		        c = (HttpURLConnection)  url.openConnection();
		        c.setRequestMethod("GET");
		        c.setDoOutput(true);
		        c.connect();	
		        fileName = "cardinal.pdf";
		        
		        inStream = new BufferedInputStream(c.getInputStream());
			    fileStream = new FileOutputStream(cardinal_menu);
			    outStream = new BufferedOutputStream(fileStream, 1024);
			    
			    byte[] data4 = new byte[1024];
			    bytesRead = 0;
			    
			    while((bytesRead = inStream.read(data4, 0, data4.length)) >= 0)
		        {
		            outStream.write(data4, 0, bytesRead);
		        }
			    
			    outStream.flush();
			    outStream.close();
		        fileStream.close();
		        inStream.close();
		        
		        url = new URL("http://www.sjfc.edu/dotAsset/86821.pdf");
		        c = (HttpURLConnection)  url.openConnection();
		        c.setRequestMethod("GET");
		        c.setDoOutput(true);
		        c.connect();	
		        fileName = "fishbowl.pdf";
		        
		        inStream = new BufferedInputStream(c.getInputStream());
			    fileStream = new FileOutputStream(fishbowl_menu);
			    outStream = new BufferedOutputStream(fileStream, 1024);
			    
			    byte[] data5 = new byte[1024];
			    bytesRead = 0;
			    
			    while((bytesRead = inStream.read(data5, 0, data5.length)) >= 0)
		        {
		            outStream.write(data5, 0, bytesRead);
		        }
			    
			    outStream.flush();
			    outStream.close();
		        fileStream.close();
		        inStream.close();
		        
			} 
	        catch (MalformedURLException e) 
			{
				error_box.setTitle("Server Connection Error");
				error_box.setIcon(R.drawable.app_icon);
				error_box.setNeutralButton("OK!", null);
				error_box.setMessage("Server is unavailable.\n\n" +
						                   "**Menus may be unavailable or out-of-date.");
				error_box.show();
			} 
	        catch (IOException e) 
	        {
				error_box.setTitle("Server Connection Error");
				error_box.setIcon(R.drawable.app_icon);
				error_box.setNeutralButton("OK!", null);
				error_box.setMessage("Server is unavailable.\n\n" +
						                   "**Menus may be unavailable or out-of-date.");
				error_box.show();
			}   	
 
			return s;
		}

	}
	*/
    public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		
		if(netinfo != null && netinfo.isConnectedOrConnecting())
		{
			return true;
		}
		
		return false;
	}

    public void downloadYelpImages() {
    	String[] urls = new String [9];
    	urls[0] = "http://s3-media3.ak.yelpcdn.com/assets/2/www/img/cc5d90a21966/ico/stars/v1/stars_large_1.png";
    	urls[1] = "http://s3-media1.ak.yelpcdn.com/assets/2/www/img/4b88f27b8b84/ico/stars/v1/stars_large_1_half.png";
    	urls[7] = "http://s3-media4.ak.yelpcdn.com/assets/2/www/img/c00926ee5dcb/ico/stars/v1/stars_large_2.png";
    	urls[2] = "http://s3-media2.ak.yelpcdn.com/assets/2/www/img/d63e3add9901/ico/stars/v1/stars_large_2_half.png";
    	urls[3] = "http://s3-media1.ak.yelpcdn.com/assets/2/www/img/e8b5b79d37ed/ico/stars/v1/stars_large_3.png";
    	urls[4] = "http://s3-media3.ak.yelpcdn.com/assets/2/www/img/bd9b7a815d1b/ico/stars/v1/stars_large_3_half.png";
    	urls[5] = "http://s3-media2.ak.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png";
    	urls[6] = "http://s3-media4.ak.yelpcdn.com/assets/2/www/img/9f83790ff7f6/ico/stars/v1/stars_large_4_half.png";
    	urls[7] = "http://s3-media3.ak.yelpcdn.com/assets/2/www/img/cc5d90a21966/ico/stars/v1/stars_large_1.png";
    	urls[8] = "http://s3-media3.ak.yelpcdn.com/assets/2/www/img/22affc4e6c38/ico/stars/v1/stars_large_5.pn";
    	
    	for(int i = 0; i < urls.length; i++)
    		Picasso.with(ctx).load(urls[i]);
    	
    }
}


