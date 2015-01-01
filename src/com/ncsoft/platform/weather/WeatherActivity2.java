package com.ncsoft.platform.weather;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.ncsoft.platform.weather.util.HttpUtils;


public class WeatherActivity2 extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		TextView testView = (TextView) findViewById(R.id.test_view);
		
		testView.setText(new String("test 입니다").toString());
		
		/*
		// Create Inner Thread Class
        Thread background = new Thread(new Runnable() {
             
            private final HttpClient Client = new DefaultHttpClient();
            //private String URL = "http://androidexample.com/media/webservice/getPage.php";
             
            // After call for background.start this run method call
            public void run() {
                try {

                	String query = "용인시 마북동";
    	    		String queryUTF8 = java.net.URLEncoder.encode(query, "UTF-8");
    	    		StringBuilder sb = new StringBuilder();
    	    		sb.append("http://maps.google.com/maps/api/geocode/json?sensor=true&language=ko&address=").append(queryUTF8);
    	    		String URL = sb.toString();
    	    		
                    String SetServerString = "";
                    HttpGet httpget = new HttpGet(URL);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    SetServerString = Client.execute(httpget, responseHandler);
                    threadMsg(SetServerString);

                } catch (Throwable t) {
                    // just end the background thread
                    Log.i("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            @SuppressLint("HandlerLeak")
			private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {
                     
                    String aResponse = msg.getData().getString("message");

                    if ((null != aResponse)) {

                        // ALERT MESSAGE
                        Toast.makeText(
                                getBaseContext(),
                                "Server Response: "+aResponse,
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                            // ALERT MESSAGE
                            Toast.makeText(
                                    getBaseContext(),
                                    "Not Got Response From Server.",
                                    Toast.LENGTH_SHORT).show();
                    }    

                }
            };

        });
        // Start Thread
        background.start();  //After call start method thread called run Method
        */
		
		WorkerThread thread = new WorkerThread(new WeatherHandler());
		thread.start();
	}
	
	@SuppressLint("HandlerLeak")
	class WeatherHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

        	if(msg.what == 0)
        		Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
        	else if(msg.what == 1)
        		//Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
        		Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
        	
            super.handleMessage(msg);
        }
	}
	
	class WorkerThread extends Thread {
		Handler mHandler;
		
		public WorkerThread(Handler handler) {
			mHandler = handler;
		}
		
		@Override
		public void run() {
			
			try {
				String query = "용인시 마북동";
	    		String queryUTF8 = java.net.URLEncoder.encode(query, "UTF-8");
	    		
				String result = HttpUtils.getByHttpClient(getApplicationContext(), 
						"http://maps.google.com/maps/api/geocode/json", 
						new BasicNameValuePair("address", queryUTF8),
						new BasicNameValuePair("sensor", "true"),
						new BasicNameValuePair("language", "ko"));
				
				Message.obtain(mHandler, 1, result).sendToTarget();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				mHandler.sendEmptyMessage(0);
			}
		}

	}
}
