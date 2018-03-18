package com.example.princ.midterm;
/*Author: Sujanth Babu Guntupalli*/
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class AppDetailsActivity extends AppCompatActivity {

    TextView dAppNameTV,dReleaseDateTV,dGenresTV,dArtistNameTV,dCopyRightTV;
    ImageView dImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);
        setTitle("App Details");

        dAppNameTV=(TextView) findViewById(R.id.dAppNameTV);
        dArtistNameTV=(TextView) findViewById(R.id.dArtistnameTV);
        dReleaseDateTV=(TextView) findViewById(R.id.dReleaseDateTV);
        dGenresTV=(TextView) findViewById(R.id.dGenresTV);
        dCopyRightTV=(TextView) findViewById(R.id.dCopyRightTV);
        dImageView=(ImageView) findViewById(R.id.dImageView);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("appDetails")) {
                App presentApp = (App) getIntent().getExtras().getSerializable("appDetails");
                dAppNameTV.setText(presentApp.getAppName());
                dArtistNameTV.setText(presentApp.getArtistName());
                dReleaseDateTV.setText(presentApp.getReleaseDate());
                dCopyRightTV.setText(presentApp.getCopyRight());
                dGenresTV.setText(presentApp.getGenres());
                if(isConnected()) {
                    Picasso.with(AppDetailsActivity.this).load(presentApp.getArtworkURL()).into(dImageView);
                }else{
                    Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
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
}
