package edu.gatech.johndoe.carecoordinator.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import edu.gatech.johndoe.carecoordinator.community.Community;
import edu.gatech.johndoe.carecoordinator.util.ParsedLatLong.LatLongResponse;
import edu.gatech.johndoe.carecoordinator.util.ParsedLatLong.Location;

/**
 * Created by colton on 4/12/16.
 */
public class LatLongUpdate extends AsyncTask<String, Void, List<Double>> {

    HttpURLConnection urlConnection;

    @Override
    protected List<Double> doInBackground(String... args) {

        String urlString = ("http://maps.google.com/maps/api/geocode/json?address=" + args[0] + "&sensor=false");
        List<Double> latLongResult = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString.replaceAll("\\s", "%20"));
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Gson latLongGson = new Gson();
            LatLongResponse response = latLongGson.fromJson(result.toString(), LatLongResponse.class);
            latLongResult.add(response.getResults().get(0).getGeometry().getLocation().getLat());
            latLongResult.add(response.getResults().get(0).getGeometry().getLocation().getLng());

        }catch( Exception e) {
            e.printStackTrace();
            latLongResult.add(-1.0);
        }
        finally {
            urlConnection.disconnect();
        }

        return latLongResult;
    }

}
