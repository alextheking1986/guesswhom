/**
 * 
 */
package com.sameer.gupshap;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author sameer
 * to avoid android.os.NetworkOnMainThreadException we have to run network operations in Async Mode
 */

public class GetJSONFromUrl extends AsyncTask<String, Void, InputStream> {

    InputStream is = null;
    String result = "";
    String error_text="";
    JSONObject j = null;

    protected InputStream doInBackground(String... urls) {

    // http post
    try {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urls[0]);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        HttpParams myParams = null;
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);

        is = entity.getContent();

    } catch (Exception e) {
        Log.e("log_tag", "Error in http connection " + e.toString());
    }
	return is;
    }
}

