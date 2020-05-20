package com.example.android.soonami;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class QueryUtils {
    private static final String LOG_TAG = "error";


    private QueryUtils() {
    }

    private static URL createURL(String s) throws MalformedURLException {
        URL url=new URL(s);
        return url;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link Event} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Event> extractEarthquakes(String earthquakeJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Event> earthquakes = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject jsonObject = new JSONObject(earthquakeJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("features");
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item= jsonArray.getJSONObject(i);
                JSONObject prop = item.getJSONObject("properties");
                earthquakes.add(new Event(
                        prop.getString("place"),
                        Long.valueOf(prop.getString("time")),
                        Integer.valueOf(prop.getString("tsunami")),Double.valueOf(prop.getString("mag"))));
                
            }
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
    public static ArrayList<Event> fetchEarthquakeData(String url_string) throws IOException {
        URL url = createURL(url_string);
        String jsonResponse=makeHttpRequest(url);
        return extractEarthquakes(jsonResponse);

    }
}
