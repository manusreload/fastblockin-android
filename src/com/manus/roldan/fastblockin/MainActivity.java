package com.manus.roldan.fastblockin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	String pref = "com.manus.roldan.fastblockin";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		((TextView)findViewById(R.id.editText1)).setText(getSharedPreferences(pref, 0).getString("ip", ""));
		((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, FastBlockinActivity.class);
				getSharedPreferences(pref, 0).edit().putString("ip", ((EditText)findViewById(R.id.editText1)).getText().toString()).commit();
				i.putExtra("ip", ((TextView)findViewById(R.id.editText1)).getText().toString());
				startActivity(i);
			}
		});
	}
	
}
