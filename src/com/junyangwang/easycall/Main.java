package com.junyangwang.easycall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.finish();
        Intent i = new Intent("android.intent.action.EASYCALL");
		startActivity(i); 
	}
}
