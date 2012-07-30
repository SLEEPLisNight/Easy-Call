package com.junyangwang.easycall;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class EsayCallArrayAdapter extends ArrayAdapter<EsayCallModel> {
	private final Context context;
	private final Activity parentAct;
	private final List<EsayCallModel> list;
	
	Button bAdd, bUpdate, bCancelUpdate, bAddExtensionUpdate, bDelete;
	EditText etNameUpdate, etNumberUpdate, etExtensionUpdate;
	TextView tvIDUpdate;
	
	Drawable bCallBg, bEditBg, llBg;
	
	private int ext;
	
	static class ViewHolder {
		public Button bCall;
		public Button bEdit;
		public TextView tvName;
	}

	public EsayCallArrayAdapter(Context context, Activity parentAct, List<EsayCallModel> list) {
		super(context, R.layout.easycallrow, list);
		this.parentAct = parentAct;
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.easycallrow, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) rowView.findViewById(R.id.tvName);
			viewHolder.bCall = (Button) rowView.findViewById(R.id.bCall);
			viewHolder.bEdit = (Button) rowView.findViewById(R.id.bEdit);
			
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		holder.tvName.setText(null);
		
		holder.tvName.setText(list.get(position).username);
		
		holder.bCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	call(list.get(position).numbers);
            }
        }); 
		
		holder.bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	updateNumber(list.get(position).id);
            }
        }); 
		
		//Log.i("display thread: ", rowView.toString());
		return rowView;
	}
	
	public void call(String numbers) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ numbers));
            context.startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("Esay call", "Call failed", e);
        }
    }
	
	private void updateNumber(int i) {
		// TODO Auto-generated method stub
    	ext = 0;
    	
    	String numbers, name;
    	
		Database entry = new Database(context);
		entry.open();
		numbers = entry.getNumbers(i);
		name = entry.getName(i);
		entry.close();
		
		parentAct.setContentView(R.layout.updatenumber);
		
		etNameUpdate = (EditText) parentAct.findViewById(R.id.etNameUpdate);
	    etNumberUpdate = (EditText) parentAct.findViewById(R.id.etNumberUpdate);
	    tvIDUpdate = (TextView) parentAct.findViewById(R.id.tvIDUpdate);
	    
	    etNameUpdate.setText(name);
	    tvIDUpdate.setText(String.valueOf(i));
	    
	    String[] extNumbers = numbers.split(",");
	    etNumberUpdate.setText(extNumbers[0].trim()); 
	    
	    for (int j = 1; j<extNumbers.length; j++){
	    	addExtensionUpdate(extNumbers[j].trim());
	    }
	    
	    bUpdate = (Button) parentAct.findViewById(R.id.bUpdate);
        bCancelUpdate = (Button) parentAct.findViewById(R.id.bCancelUpdate);
        bDelete = (Button) parentAct.findViewById(R.id.bDelete);
        bAddExtensionUpdate = (Button) parentAct.findViewById(R.id.bAddExtensionUpdate);
        
        bUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	String name = etNameUpdate.getText().toString();
    			String number = etNumberUpdate.getText().toString();
    			String extension = "";
    			if (ext > 0){
    				for (int i=0; i<ext; i++){
    					int idExtension = 300 + i;
    					etExtensionUpdate = (EditText) parentAct.findViewById(idExtension);
    					if (!etExtensionUpdate.getText().toString().contentEquals("")){
    						extension += ",";
    						extension += etExtensionUpdate.getText().toString();
    					}
    				}
    			}
    			String numbers = number + extension;
    			
    			Database entrySave = new Database(context);
    			entrySave.open();
    			if (name.contentEquals("") || number.contentEquals("")){
    				showAlertDialog();
    				entrySave.close();
    			} else {
    				entrySave.updateEntry(Integer.parseInt(tvIDUpdate.getText().toString()), name, numbers);
    				entrySave.close();
    				parentAct.finish();
    				parentAct.startActivity(new Intent("android.intent.action.EASYCALL"));
    			}
            }
        }); 
        bCancelUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	parentAct.finish();
            	parentAct.startActivity(new Intent("android.intent.action.EASYCALL"));
            }
        }); 
        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Database entry = new Database(context);
    			entry.open();
    			entry.deleteUser(Integer.parseInt(tvIDUpdate.getText().toString()));
    			entry.close();
    			
    			parentAct.finish();
    			parentAct.startActivity(new Intent("android.intent.action.EASYCALL"));
            }
        }); 
        bAddExtensionUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	addExtensionUpdate(null);
            }
        }); 
	}
	
	public void addExtensionUpdate(String extNumber){
		LayoutParams paramET = new LinearLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT);
		
		EditText et = new EditText(context);
		et.setText(extNumber);
		et.setLayoutParams(paramET);
		int idExtension = 300 + ext;
		et.setId(idExtension);
		ext++;
		
		LayoutParams paramTV = new LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
		
		TextView tv = new TextView(context, null, android.R.attr.textAppearanceLarge);
		tv.setLayoutParams(paramTV);
		tv.setText("Ext Number " + ext + ": ");
		
		LayoutParams paramLL = new LinearLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT);
		
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setPadding(10, 10, 10, 5);
			paramLL.setMargins(0, 5, 0, 0);
		ll.setLayoutParams(paramLL);
		llBg = context.getResources().getDrawable(R.drawable.textview);
			ll.setBackgroundDrawable(llBg);
		ll.addView(tv);
		ll.addView(et);
		
		LinearLayout llE = (LinearLayout) parentAct.findViewById(R.id.llExtensionUpdate);
		llE.addView(ll);
	}
	
	private void showAlertDialog() {
		// TODO Auto-generated method stub
    	AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setTitle("Error");
		alert.setMessage("Please fill both the name and the first number.");	
		
		alert.show();
	}
	
}
