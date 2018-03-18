package com.example.princ.midterm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class App implements Serializable{

    String appName,artistName,releaseDate,artworkURL,copyRight;
    ArrayList<String> genres=new ArrayList<>();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getArtworkURL() {
        return artworkURL;
    }

    public void setArtworkURL(String artworkURL) {
        this.artworkURL = artworkURL;
    }

    public String getGenres() {
        Collections.sort(genres, new Comparator<String>(){

            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2) ;
            }
        });
        StringBuilder genreList=new StringBuilder();
        String[] genresArray = genres.toArray(new String[genres.size()]);
        for(String genre:genresArray){
            genreList.append(genre+",");
        }
        String genreList1=genreList.toString();
        genreList1=genreList1.substring(0,genreList1.length()-1);
        return genreList1;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }
}
