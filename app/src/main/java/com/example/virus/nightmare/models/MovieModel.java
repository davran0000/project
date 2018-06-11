package com.example.virus.nightmare.models;

import java.util.List;

public class MovieModel {

    private String movie;
    private int year;
    private double rating;
    private String duration;
    private String direction;
    private String tagline;
    private String image;
    private String story;

    public List<Cast> getCastList() {
        return castList;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    private List<Cast> castList;


   public static class Cast {
       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       private String name;
    }
    public String getMovie(){
        return movie;
    }
    public void setMovie(String movie){
        this.movie = movie;
    }
    public int getYear(){
        return year;
    }
    public void setYear(int year){
        this.year = year;
    }
    public double getRating(){
        return rating;
    }
    public void setRating(double rating){
        this.rating = rating;
    }
    public String getDuration(){
        return duration;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public String getDirection(){
        return direction;
    }
    public void setDirection(String direction){
        this.direction = direction;
    }
    public String getTagline(){
        return tagline;
    }
    public void setTagline(String tagline){
        this.tagline = tagline;
    }
    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getStory(){
        return story;
    }
    public void setStory(String story){
        this.story = story;
    }
}