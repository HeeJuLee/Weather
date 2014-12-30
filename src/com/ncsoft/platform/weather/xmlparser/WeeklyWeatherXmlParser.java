package com.ncsoft.platform.weather.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class WeeklyWeatherXmlParser {
	// We don't use namespaces
    private static final String ns = null;
	private static final String TAG = "WWW";
    
    public class WeeklyWeatherXmlData {
        public final Date mDate;
        public final int mMinTemp;
        public final int mMaxTemp;
        public final String mWeather;

        private WeeklyWeatherXmlData(Date date, int minTemp, int maxTemp, String weather) {
        	mDate = date;
        	mMinTemp = minTemp;
        	mMaxTemp = maxTemp;
            mWeather = weather;
        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
    
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "tmEf");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "tmEf");
    	return title;
    }
    
    private String readMinTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "tmn");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "tmn");
    	return title;
    }
    
    private String readMaxTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "tmx");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "tmx");
    	return title;
    }
    
    private String readWeather(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "wf");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "wf");
    	return title;
    }

    //For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    	String result = "";
    	if (parser.next() == XmlPullParser.TEXT) {
    		result = parser.getText();
    		parser.nextTag();
    	}
    	return result;
    }
   
    //Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    //to their respective "read" methods for processing. Otherwise, skips the tag.
    private WeeklyWeatherXmlData readData(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
    	parser.require(XmlPullParser.START_TAG, ns, "data");
    	Date date = null;
    	int minTemp = 0;
    	int maxTemp = 0;
    	String weather = null;
    	while (parser.next() != XmlPullParser.END_TAG) {
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
    			continue;
    		}
    		String name = parser.getName();
    		if (name.equals("tmEf")) {
    			String dateStr = readDate(parser);
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			date = sdf.parse(dateStr);	
    		} else if (name.equals("tmn")) {
    			minTemp = Integer.parseInt(readMinTemp(parser));
    		} else if (name.equals("tmx")) {
    			maxTemp = Integer.parseInt(readMaxTemp(parser));
    		} else if (name.equals("wf")) {
    			weather = readWeather(parser);
    		} else {
    			skip(parser);
    		}
    	}
    	//logger.log(Level.INFO, "date:" + date + " minTemp:" + minTemp);
    	return new WeeklyWeatherXmlData(date, minTemp, maxTemp, weather);
    }
    
    private List<WeeklyWeatherXmlData> readLocation(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
    	//logger.log(Level.INFO, "readLocation"); 
        parser.require(XmlPullParser.START_TAG, ns, "location");
        List<WeeklyWeatherXmlData> entries = new ArrayList<WeeklyWeatherXmlData>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("data")) {
            	entries.add(readData(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }
    
    private List<WeeklyWeatherXmlData> readBody(XmlPullParser parser, String cityCode) throws XmlPullParserException, IOException, ParseException {
    	//logger.log(Level.INFO, "readBody");
    	List<WeeklyWeatherXmlData> list = null;
        parser.require(XmlPullParser.START_TAG, ns, "body");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue; 
            }
            
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("location")) {
            	String city = parser.getAttributeValue(0);
            	//Log.d(TAG, "city:" + city); 
            	if(city.equals(cityCode)){
            		list = readLocation(parser);
            		return list;
            	}
            	else {
            		skip(parser);
            	}
            } else {
                skip(parser);
            }
        }
        return list;
    }
    
    private List<WeeklyWeatherXmlData> readWid(XmlPullParser parser, String cityCode) throws XmlPullParserException, IOException, ParseException {
    	//logger.log(Level.INFO, "readWid");
        List<WeeklyWeatherXmlData> list = null;

        parser.require(XmlPullParser.START_TAG, ns, "wid");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("body")) {
            	list = readBody(parser, cityCode);
            } else {
                skip(parser);
            }
        }  
        return list;
    }
    
    public List<WeeklyWeatherXmlData> parse(InputStream in, String cityCode) throws XmlPullParserException, IOException, ParseException {
        try {
        	//logger.log(Level.INFO, "parse");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readWid(parser, cityCode);
        } finally {
            in.close();
        }
    }
    
    // Given a string representation of a URL, sets up a connection and gets
 	// an input stream.
    public List<WeeklyWeatherXmlData> parseFromUrl(String urlString, String cityCode) throws IOException, XmlPullParserException, ParseException {
		InputStream is;
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		is = conn.getInputStream();
		return parse(is, cityCode);
 	}
}


  
