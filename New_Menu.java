/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class New_Menu extends Activity{

	Context ctx = this;
	Button createButton, generateButton;
	TextView logo, betaLogo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_menu);
		
		setupVariables();
			
		createButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				try
				{	
					Class ourClass = Class.forName("com.alpha.crawl.Main_Menu");
					Intent ourIntent = new Intent(ctx, ourClass);
					ourIntent.putExtra("pointer", "create");
					startActivity(ourIntent);
				}
				catch(ClassNotFoundException e)
				{
					//	error_box.setTitle("Application Error");
					//	error_box.setNeutralButton("OK!", null);
					//	error_box.setMessage("Could not direct to desired page.\nPlease try again!");
					//	error_box.show();
				}
			} 
		});
		
		generateButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				try
				{	
					Class ourClass = Class.forName("com.alpha.crawl.Main_Menu");
					Intent ourIntent = new Intent(ctx, ourClass);
					ourIntent.putExtra("pointer", "generate");
					startActivity(ourIntent);
				}
				catch(ClassNotFoundException e)
				{
					//	error_box.setTitle("Application Error");
					//	error_box.setNeutralButton("OK!", null);
					//	error_box.setMessage("Could not direct to desired page.\nPlease try again!");
					//	error_box.show();
				}
			} 
		});
	
	}
	
	private void setupVariables() {
		
		createButton    = (Button) findViewById(R.id.createButton);
		generateButton  = (Button) findViewById(R.id.generateButton);
		
    	logo         	= (TextView) findViewById(R.id.Logo); 
    	betaLogo        = (TextView) findViewById(R.id.betaLogo);
    	
    	Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Stencil Cargo Army.ttf");
    	logo.setTypeface(tf);
    	betaLogo.setTypeface(tf);	 
	}
}
