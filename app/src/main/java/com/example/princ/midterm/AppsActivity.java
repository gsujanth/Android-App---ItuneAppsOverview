package com.example.princ.midterm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class AppsActivity extends AppCompatActivity implements GetAppsListTask.IApp{

    ArrayList<App> appsList=new ArrayList<>();
    ArrayList<String> genresList=new ArrayList<>();
    AppAdapter appAdapter;
    AlertDialog.Builder builder;
    TextView genreTV,genreDTV;
    Button filterButton;
    ListView appListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        setTitle("Apps");

        //genresList.add("All");

        genreTV=(TextView) findViewById(R.id.genreTV);
        genreDTV=(TextView) findViewById(R.id.genreDTV);
        filterButton=(Button) findViewById(R.id.filterButton);
        appListView=(ListView) findViewById(R.id.appListView);
        builder=new AlertDialog.Builder(this);
        genresList.add("All");
        genreDTV.setText("All");
        if(isConnected()){
            new GetAppsListTask(AppsActivity.this,AppsActivity.this).execute("https://rss.itunes.apple.com/api/v1/us/ios-apps/top-grossing/all/50/explicit.json");
        }else{
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getBaseContext(),AppDetailsActivity.class);
                intent.putExtra("appDetails",appsList.get(i));
                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] genresArray=genresList.toArray(new String[genresList.size()]);
                builder.setTitle("Choose Genre")
                        .setCancelable(false)
                        .setItems(genresArray,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int item){
                        genreDTV.setText(genresArray[item]);
                        if(genresArray[item].equals("All")){
                            if (appsList != null && !appsList.isEmpty()) {
                                Log.d("appsList size: ", "onClick: " + appsList.size());
                                appAdapter = new AppAdapter(AppsActivity.this, R.layout.app_layout_file, appsList);
                                appListView.setAdapter(appAdapter);
                            }else{
                                Toast.makeText(AppsActivity.this, "No App Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            ArrayList<App> y = new ArrayList<>();
                            for (App a : appsList) {
                                y.add(a);
                            }
                            Log.d("copy array size: ", "onClick:1 " + y.size());
                            ArrayList<App> tAppsList = filter(y, genresArray[item]);
                            Log.d("copy array size: ", "onClick:2 " + y.size());
                            if (tAppsList != null && !tAppsList.isEmpty()) {
                                appAdapter = new AppAdapter(AppsActivity.this, R.layout.app_layout_file, tAppsList);
                                appListView.setAdapter(appAdapter);
                            } else {
                                Toast.makeText(AppsActivity.this, "No App Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).create().show();
            }
        });

    }

    @Override
    public void handleApps(ArrayList<App> s,ArrayList<String> g) {
        genresList=g;
        sortGenres(genresList);
        genresList.add(0,"All");
        appsList.clear();
        if(s!=null&&!s.isEmpty()) {
            appsList = s;
            appAdapter = new AppAdapter(this, R.layout.app_layout_file, appsList);
            appListView.setAdapter(appAdapter);
        }else{
            Toast.makeText(this, "No App Data Found", Toast.LENGTH_SHORT).show();
            appAdapter = new AppAdapter(this, R.layout.app_layout_file, appsList);
            appListView.setAdapter(appAdapter);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private void sortGenres(ArrayList<String> g){
        Collections.sort(g, new Comparator<String>(){
            @Override
            public int compare(String g1, String g2) {
                return g1.compareTo(g2);
            }
        });
    }

    private ArrayList<App> filter(ArrayList<App> s,String x){
        for (Iterator<App> it = s.iterator(); it.hasNext();){
            String[] genres=it.next().getGenres().split(",");
            //Set<String> set = new HashSet<String>(Arrays.asList(genres));
            //genres = set.toArray(new String[set.size()]);
            if(!Arrays.asList(genres).contains(x)){
                it.remove();
            }
            /*if (!it.next().getGenres().contains(x))
                it.remove();
            */
        }
        return s;
    }
}
