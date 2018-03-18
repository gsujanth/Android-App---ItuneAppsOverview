package com.example.princ.midterm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AppAdapter extends ArrayAdapter<App> {

    App app;
    TextView tvAppName, tvArtistName,tvGenres,tvReleaseDate;
    ImageView imageView;
    Context ctx;

    public AppAdapter(@NonNull Context context, int resource, List<App> objects) {
        super(context, resource, objects);
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       app = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_layout_file, parent, false);
        tvAppName = (TextView) convertView.findViewById(R.id.tvAppName);
        tvArtistName=(TextView) convertView.findViewById(R.id.tvArtistName);
        tvGenres=(TextView) convertView.findViewById(R.id.tvGenres);
        tvReleaseDate = (TextView) convertView.findViewById(R.id.tvReleaseDate);
        imageView = (ImageView) convertView.findViewById(R.id.imageView);

        tvAppName.setText(app.appName);
        tvArtistName.setText(app.artistName);
        tvGenres.setText(app.getGenres());
        tvReleaseDate.setText(app.releaseDate);
        if (isConnected()) {
            Picasso.with(convertView.getContext()).load(app.getArtworkURL()).into(imageView);
        } else {
            Toast.makeText(ctx, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return convertView;

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
