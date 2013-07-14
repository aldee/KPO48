package com.olala.android.kpo48;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FullActivity extends Activity {
	
	ListView list;
	Intent i;
	
	String angkatan;
	String prodi;
	
	private ProgressDialog pDialog;
	public static DefaultHttpClient httpClient;
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String, String>> dataList;
	
	private static final String url_all_data = "http://kpo48.webege.com/get_data.php";
	private static final String TAG_ANGKATAN = "angkatan";
	private static final String TAG_PRODI = "prodi";
	private static final String TAG_NAMA = "nama";
	private static final String TAG_URL = "url";
	
	JSONArray datas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full);
		
		list = (ListView) findViewById(R.id.listView1);
		i = getIntent();
		
		FullActivity.httpClient = LoginActivity.httpClient;
		
		dataList = new ArrayList<HashMap<String, String>>();		
		angkatan = i.getStringExtra(TAG_ANGKATAN);
		prodi = i.getStringExtra(TAG_PRODI);
		pDialog = new ProgressDialog(FullActivity.this);
		
		new GetData().execute();
	}
	
	class GetData extends AsyncTask<String, String, String> {
		
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Lagi men-download data. Tunggu gan...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("angkatan", angkatan));
			params.add(new BasicNameValuePair("prodi", prodi));
			
			JSONObject json = jParser.makeHttpRequest(url_all_data, "GET", params, httpClient);
			
			try {
				int success = json.getInt("success");
				if (success == 1) {
					datas = json.getJSONArray("data");
					
					for (int i = 0; i < datas.length(); i++) {
						JSONObject c = datas.getJSONObject(i);
						
						String nama = c.getString(TAG_NAMA);
						String url = c.getString(TAG_URL);
						String ip = getIP(url);

						HashMap<String, String> map = new HashMap<String, String>();
						
						map.put(TAG_NAMA, nama);
						map.put(TAG_URL, url);
						map.put("IP", ip);
						
						dataList.add(map);
					}
					
					Collections.sort(dataList, new Comparator<HashMap<String, String>>() {

						@Override
						public int compare(HashMap<String, String> lhs,
								HashMap<String, String> rhs) {
							// TODO Auto-generated method stub
							String lh = lhs.get(TAG_NAMA);
							String rh = rhs.get(TAG_NAMA);
							return lh.compareTo(rh);
						}
						
					});
				}
				else {
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String[] from = new String[] {
						TAG_NAMA,
						"IP"
					};
					
					int[] to = new int[] {
						R.id.textView1,
						R.id.textView2
					};
					ListAdapter adapter = new SimpleAdapter(FullActivity.this, dataList, R.layout.data, from, to);
					list.setAdapter(adapter);
				}
				
			});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	public String getIP(String url) {
		// TODO Auto-generated method stub
		HttpGet httpGet = new HttpGet(url);
		String response = null;
		String float1 = null;
		try {
			response = httpClient.execute(httpGet, new BasicResponseHandler());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File input = new File(getApplicationContext().getFilesDir(), "data.html");
		
		FileOutputStream output;
		try {
			output = openFileOutput("data.html", Context.MODE_PRIVATE);
			output.write(response.getBytes());
			output.close();
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements data = doc.getElementsByClass("tabel-info");
			
			String tabel = data.toString();
			
			String re1=".*?";
			String re2="([0-9]\\.[0-9][0-9])";
			Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		    Matcher m = p.matcher(tabel);
		    if (m.find())
		    {
		        float1=m.group(1);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return float1.toString();
	}

}
