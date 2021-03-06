package com.ncsoft.platform.weather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import android.content.Context;

public  class FileUtils {

	public static String getStringFromAssets(Context context, String fileName) {
		StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } 
	
	public static ArrayList<String> getStringArrayFromAssets(Context context, String fileName) {
		ArrayList<String> array = new ArrayList<String>();
		
		try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                array.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return array;
    } 

	public static void writeToFile(Context context, String content, String filename) throws IOException {
		Writer writer = null;
		try {
			OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(content);
		} finally {
			if(writer != null)
				writer.close();
		}
	}
}
