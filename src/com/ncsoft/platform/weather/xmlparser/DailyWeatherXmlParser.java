package com.ncsoft.platform.weather.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class DailyWeatherXmlParser {
    private static final String ns = null;
    private static Logger logger = Logger.getLogger("XMLPARSER");
    private DailyWeatherXmlHeader mHeader = null;
    
    public DailyWeatherXmlHeader getHeader() {
		return mHeader;
	}

	public class DailyWeatherData {
        public final int mDay;
        public final int mHour;
        public final float mTemp;
        public final float mMinTemp;
        public final float mMaxTemp;
        public final String mWfKor;
        public final int mSky;
        public final int mPty;
        public final int mReh;
        public final float mWs;

        private DailyWeatherData(int day, int hour, float temp, float minTemp, float maxTemp, String wfKor, int sky, int pty, int reh, float ws) {
            mDay = day;
            mHour = hour;
            mTemp = temp;
            mMinTemp = minTemp;
            mMaxTemp = maxTemp;
            mWfKor = wfKor;
            mSky = sky;
            mPty = pty;
            mReh = reh;
            mWs = ws;
        }
    }
    
    public class DailyWeatherXmlHeader {
        public final String mDate;

        private DailyWeatherXmlHeader(String date) {
            mDate = date;
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
    
    private String readDay(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "day");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "day");
    	return title;
    }
    
    private String readHour(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "hour");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "hour");
    	return title;
    }
    
    private String readTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "temp");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "temp");
    	return title;
    }
    
    private String readMaxTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "tmx");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "tmx");
    	return title;
    }
    
    private String readMinTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "tmn");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "tmn");
    	return title;
    }
    
    private String readWfKor(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "wfKor");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "wfKor");
    	return title;
    }
    
    private String readSky(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "sky");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "sky");
    	return title;
    }
    
    private String readPty(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "pty");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "pty");
    	return title;
    }
    
    private String readTm(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "tm");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "tm");
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
    
    //For the tags title and summary, extracts their text values.
    private String readReh(XmlPullParser parser) throws IOException, XmlPullParserException {
    	String result = "";
    	if (parser.next() == XmlPullParser.TEXT) {
    		result = parser.getText();
    		parser.nextTag();
    	}
    	return result;
    }
    
    private String readWs(XmlPullParser parser) throws IOException, XmlPullParserException {
    	String result = "";
    	if (parser.next() == XmlPullParser.TEXT) {
    		result = parser.getText();
    		parser.nextTag();
    	}
    	return result;
    }
   
    //Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    //to their respective "read" methods for processing. Otherwise, skips the tag.
    private DailyWeatherData readData(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, ns, "data");
    	int hour = 0;
    	int day = 0;
    	float temp = 0.0f;
    	float minTemp = 0.0f;
    	float maxTemp = 0.0f;
    	String weather = null;
    	int sky = 0;
    	int pty = 0;
    	int reh = 0;
    	float ws = 0;
    	
    	while (parser.next() != XmlPullParser.END_TAG) {
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
    			continue;
    		}
    		String name = parser.getName();
    		if (name.equals("hour")) {
    			hour = Integer.parseInt(readHour(parser));
    		} else if (name.equals("day")) {
    			day = Integer.parseInt(readDay(parser));
    		} else if (name.equals("temp")) {
    			temp = Float.parseFloat(readTemp(parser));
    		} else if (name.equals("wfKor")) {
    			weather = readWfKor(parser);
    		} else if (name.equals("tmx")) {
    			maxTemp = Float.parseFloat(readMaxTemp(parser));
    		} else if (name.equals("tmn")) {
    			minTemp = Float.parseFloat(readMinTemp(parser));
    		} else if (name.equals("sky")) {
    			sky = Integer.parseInt(readSky(parser));
    		} else if (name.equals("pty")) {
    			pty = Integer.parseInt(readPty(parser));
    		} else if (name.equals("reh")) {
    			reh = Integer.parseInt(readReh(parser));
    		} else if (name.equals("ws")) {
    			ws = Float.parseFloat(readWs(parser));
    		} else {
    			skip(parser);
    		}
    	}
    	//logger.log(Level.INFO, "date:" + date + " minTemp:" + minTemp);
    	return new DailyWeatherData(day, hour, temp, minTemp, maxTemp, weather, sky, pty, reh, ws);
    }
    
    private List<DailyWeatherData> readBody(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//logger.log(Level.INFO, "readBody");
    	List<DailyWeatherData> entries = new ArrayList<DailyWeatherData>();
        parser.require(XmlPullParser.START_TAG, ns, "body");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("data")) {
            	entries.add(readData(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }
    
    private DailyWeatherXmlHeader readHeader(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//logger.log(Level.INFO, "readBody");
    	String tm = "";
        parser.require(XmlPullParser.START_TAG, ns, "header");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("tm")) {
            	tm = readTm(parser);
            } else {
                skip(parser);
            }
        }
        return new DailyWeatherXmlHeader(tm); 
    }
    
    private List<DailyWeatherData> readWid(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//logger.log(Level.INFO, "readWid");
        List<DailyWeatherData> list = null;

        parser.require(XmlPullParser.START_TAG, ns, "wid");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("header")) {
            	mHeader = readHeader(parser);
            } else if (name.equals("body")) {
            	list = readBody(parser);
            } else {
                skip(parser);
            }
        }  
        return list;
    }
    
    public List<DailyWeatherData> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
        	//logger.log(Level.INFO, "parse");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readWid(parser);
        } finally {
            in.close();
        }
    }
    
    // Given a string representation of a URL, sets up a connection and gets
 	// an input stream.
    public List<DailyWeatherData> parseFromUrl(String urlString) throws IOException, XmlPullParserException {
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
		return parse(is);
 	}
}


  
