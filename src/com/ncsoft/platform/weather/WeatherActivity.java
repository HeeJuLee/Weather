package com.ncsoft.platform.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ncsoft.platform.weather.manager.GeocodeManager;
import com.ncsoft.platform.weather.manager.WeatherManager;


public class WeatherActivity extends Activity {
	
	private static final String TAG = "WeatherActivity";
    
	private TextView mTestView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		mTestView = (TextView) findViewById(R.id.test_view);
		
		WorkerThread thread = new WorkerThread(new WeatherHandler());
		thread.start();
		
		/*
		// Create Inner Thread Class
        Thread background = new Thread(new Runnable() {
             
            //private String URL = "http://androidexample.com/media/webservice/getPage.php";
             
            // After call for background.start this run method call
            public void run() {
                try {
					String result = "결과값";
					
					Bundle b = new Bundle();
                    b.putString("message", result);
                    
					Message msgObj = handler.obtainMessage();                    
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);

                } catch (Throwable t) {
                    // just end the background thread
                    Log.i("Animation", "Thread  exception " + t);
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
	}	
		
	
	@SuppressLint("HandlerLeak")
	class WeatherHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	
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
				
				// 주소로 위경도 읽어오기
				String address = "용인시%20마북동";
				
				GeocodeManager gm = new GeocodeManager();
				gm.getLocationInfo(address);
				
				
				// 위경도로 날씨 데이터 읽기오기
				WeatherManager wm = WeatherManager.getInstance(getApplicationContext());
				wm.getWeatherData(gm.getLatitude(), gm.getLongitude());
				


				/*
				APIRequest.setAppKey("12f6c0f5-f4f9-3268-af97-f3c9a65a7f36");
				
				String URL = "http://apis.skplanetx.com/weather/current/minutely";
								 
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("version", "1");
				param.put("lat", "37.566535");
				param.put("lon", "126.977969");
				 
				RequestBundle req = new RequestBundle();
				req.setUrl(URL);
				req.setParameters(param);
				req.setResponseType(CONTENT_TYPE.JSON);
				 
				APIRequest api = new APIRequest();
			    ResponseMessage result = api.request(req);
			    
				
				Message.obtain(mHandler, 1, result).sendToTarget();
				*/
			} catch(Exception e) {
			    e.printStackTrace();
			    
			    mHandler.sendEmptyMessage(0);
			}
		}

	}
}
