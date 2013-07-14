package com.olala.android.kpo48;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	private Spinner spinnerP, spinnerA;
	private Button submit;
	public static final String TAG_PRODI = "prodi";
	public static final String TAG_ANGKATAN = "angkatan";
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		spinnerP = (Spinner) findViewById(R.id.spinner2);
		spinnerA = (Spinner) findViewById(R.id.spinner1);
		submit = (Button) findViewById(R.id.button1);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String prodi = String.valueOf(spinnerP.getSelectedItem());
				String angkatan = String.valueOf(spinnerA.getSelectedItem());
				
				i = new Intent(getApplicationContext(), FullActivity.class);
				
				i.putExtra(TAG_ANGKATAN, angkatan);
				i.putExtra(TAG_PRODI, prodi);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
