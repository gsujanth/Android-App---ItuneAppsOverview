package com.example.princ.midterm;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetAppsListTask extends AsyncTask<String, Void, ArrayList<App>> {

    IApp iApp;
    Context ctx;
    ProgressDialog pDlg;
    ArrayList<String> uniqueGenresList=new ArrayList<>();

    public GetAppsListTask(IApp iApp, Context ctx) {
        this.iApp = iApp;
        this.ctx = ctx;
        pDlg = new ProgressDialog(ctx);
        pDlg.setMessage("Loading Apps...");
        pDlg.setCancelable(false);
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDlg.show();
    }

    HttpURLConnection con;

    @Override
    protected ArrayList<App> doInBackground(String... strings) {
        ArrayList<App> result = new ArrayList<>();

        try {
            URL url = new URL(strings[0]);
            Log.d("demo", "doInBackground: " + url.toString());
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(con.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONObject feed = root.getJSONObject("feed");

                JSONArray results = feed.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject appJson = results.getJSONObject(i);
                    App app = new App();
                    app.appName = appJson.getString("name");
                    app.artistName = appJson.getString("artistName");
                    app.artworkURL = appJson.getString("artworkUrl100");
                    app.releaseDate = appJson.getString("releaseDate");
                    app.copyRight = appJson.getString("copyright");
                    JSONArray genres = appJson.getJSONArray("genres");
                    for (int k=0;k<genres.length();k++){
                        JSONObject tGenre = genres.getJSONObject(k);
                        String gName = tGenre.getString("name");
                        app.genres.add(gName);

                        if(!uniqueGenresList.contains(gName))
                            uniqueGenresList.add(gName);
                    }
                    result.add(app);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<App> s) {
        iApp.handleApps(s,uniqueGenresList);
        pDlg.dismiss();
    }

    public static interface IApp {
        public void handleApps(ArrayList<App> s,ArrayList<String> g);
    }
}
