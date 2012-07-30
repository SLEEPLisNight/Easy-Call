package com.junyangwang.easycall;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class EasyCall extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	private int NumberOfContacts;
	
	Button bAdd, bUpdate, bCancelUpdate, bAddExtensionUpdate, bDelete;
	EditText etNameUpdate, etNumberUpdate, etExtensionUpdate;
	TextView tvIDUpdate;
	
	Drawable bCallBg, bEditBg, llBg;
	
	private int ext;
	
	List<EsayCallModel> list = new ArrayList<EsayCallModel>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easycall);
        
        ListView lvEasyCall = (ListView) findViewById(R.id.lvEasyCall);
        
		Database entry = new Database(this);
		entry.open();
		NumberOfContacts = entry.getHighestRow();
		entry.close();
        
			//set up all the ImageButtons and TextViews in TableRow
      		for(int i = 0; i <= NumberOfContacts; i++){      			
      			String username, numbers;
      			
      			entry.open();
      			username = entry.getName(i);
      			numbers = entry.getNumbers(i);
      			entry.close();
      			
      			if (username != null){
      				list.add(new EsayCallModel(i, username, numbers));
      			}
      		}
      		
      		EsayCallArrayAdapter adapter = new EsayCallArrayAdapter(this, this, list);
      		lvEasyCall.setAdapter(adapter);
      			
      		bAdd = (Button)findViewById(R.id.ibPlus);
      		bAdd.setOnClickListener(this);   
    }
    
    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
    	if (arg0.getId() == R.id.ibPlus){
    		Intent addIntent = new Intent("android.intent.action.ADDNUMBER");
			startActivity(addIntent);
    	}
    }

}