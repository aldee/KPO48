package com.olala.android.kpo48;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private EditText mUser;
	private EditText mPassword;
	private Button mLogin;
	private ProgressDialog pDialog;
	public static DefaultHttpClient httpClient;
	JSONParser jParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mUser = (EditText) findViewById(R.id.editText1);
		mPassword = (EditText) findViewById(R.id.editText2);
		mLogin = (Button) findViewById(R.id.button1);
		
		httpClient = new DefaultHttpClient();
		pDialog = new ProgressDialog(LoginActivity.this);
		
		mLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Login().execute();
			}
		});
	}
	
	class Login extends AsyncTask<String, String, String> {
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Logging In");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String niu = mUser.getText().toString();
			String pass = mPassword.getText().toString();
			String url = "https://akademika.ugm.ac.id/index.php?pModule=b3JqbHE=&pSub=b3JqbHE=&pAct=c3Vydmh2";
			
			HttpPost httpPost = new HttpPost(url);
			String response = null;
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("username", niu));
			param.add(new BasicNameValuePair("password", pass));
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				response = httpClient.execute(httpPost, new BasicResponseHandler());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String url) {
			pDialog.dismiss();
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
