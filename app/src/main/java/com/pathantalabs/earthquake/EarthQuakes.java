package com.pathantalabs.earthquake;


public class EarthQuakes {

    private Long mTimeMilliSeconds;
    private String earthQuakePlace;
    private double mMagnitude;
    private String mUrl;

    public EarthQuakes (Long timeMilliSeconds,String place,double mag,String url){
        this.mTimeMilliSeconds = timeMilliSeconds;
        this.earthQuakePlace = place;
        this.mMagnitude = mag;
        this.mUrl = url;
    }

    public Long getEarthQuakeDate() {
        return mTimeMilliSeconds;
    }

    public String getEarthQuakePlace() {
        return earthQuakePlace;
    }

    public double getEarthQuakeRating() {
        return mMagnitude;
    }

    public String getUrl(){
        return mUrl;
    }

    @Override
    public String toString() {
        return "EarthQuake{" +
                "earthQuakeDate='" + mTimeMilliSeconds + '\'' +
                ", earthQuakePlace='" + earthQuakePlace + '\'' +
                ", earthQuakeRating='" + mMagnitude + '\'' +
                '}';
    }
}
