package com.junyangwang.easycall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class AddNumber extends Activity implements OnClickListener{
	
	Button bSave, bCancel, bAddExtension;
	EditText etName, etNumber, etExtension;
	private int ext;
	
	Drawable llBg;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnumber);
        
        bSave = (Button)findViewById(R.id.bSave);
        bCancel = (Button)findViewById(R.id.bCancel);
        bAddExtension = (Button)findViewById(R.id.bAddExtension);
        
        etName = (EditText)findViewById(R.id.etName);
        etNumber = (EditText)findViewById(R.id.etNumber);
        
        bSave.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        bAddExtension.setOnClickListener(this);
        
        ext = 0;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bSave:
			
			String name = etName.getText().toString();
			String number = etNumber.getText().toString();
			String extension = "";
			if (ext > 0){
				for (int i=0; i<ext; i++){
					int idExtension = 200 + i;
					etExtension = (EditText)findViewById(idExtension);
					if (!etExtension.getText().toString().contentEquals("")){
						extension += ",";
						extension += etExtension.getText().toString();
					}
				}
			}
			String numbers = number + extension;
			
			Database entrySave = new Database(this);
			entrySave.open();
			if (name.contentEquals("") || number.contentEquals("")){
				showAlertDialog();
				entrySave.close();
			} else {
				entrySave.createEntry(name, numbers);
				entrySave.close();
				this.finish();
				startActivity(new Intent("android.intent.action.EASYCALL"));
			}
	
			break;
		case R.id.bCancel:
			this.finish();
			startActivity(new Intent("android.intent.action.EASYCALL"));
			break;
		case R.id.bAddExtension:
  			
  			LayoutParams paramET = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
  			
  			EditText et = new EditText(this);
  			et.setLayoutParams(paramET);
  			int idExtension = 200 + ext;
  			et.setId(idExtension);
  			ext++;
  			
  			LayoutParams paramTV = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
  			
  			TextView tv = new TextView(this, null, android.R.attr.textAppearanceLarge);
  			tv.setLayoutParams(paramTV);
  			tv.setText("Ext Number " + ext + ": ");
  			
  			LayoutParams paramLL = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
  			
  			LinearLayout ll = new LinearLayout(this);
  			ll.setOrientation(LinearLayout.HORIZONTAL);
  			ll.setPadding(10, 10, 10, 5);
  			paramLL.setMargins(0, 5, 0, 0);
  			ll.setLayoutParams(paramLL);
  			llBg = this.getResources().getDrawable(R.drawable.textview);
  			ll.setBackgroundDrawable(llBg);
  			ll.addView(tv);
  			ll.addView(et);
  			
			LinearLayout llE = (LinearLayout)findViewById(R.id.llExtension);
			llE.addView(ll);
			break;
		}
	}
	
	 private void showAlertDialog() {
			// TODO Auto-generated method stub
	    	AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setTitle("Error");
			alert.setMessage("Please fill both the name and the first number.");	
			
			alert.show();
	}
}
