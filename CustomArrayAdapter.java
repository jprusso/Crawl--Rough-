/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter <String> {
	
	private Typeface tf;
	private LayoutInflater inflater;
	private int resource;
	private int textViewResourceId;
	private ArrayList<String> objects;
	
	public CustomArrayAdapter(Context context, int resource, int textViewResourceId,
	        ArrayList<String> favorites, Typeface tf) {
	    super(context, resource, textViewResourceId, favorites);
	    this.tf = tf;
	    this.resource = resource;
	    this.textViewResourceId = textViewResourceId;
	    this.objects = favorites;
	    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    if (convertView == null) {
	        convertView = inflater.inflate(resource, parent, false);    
	    }

	    TextView text = (TextView) convertView.findViewById(textViewResourceId);
	    text.setTypeface(tf);

	    return convertView;
	}

}
