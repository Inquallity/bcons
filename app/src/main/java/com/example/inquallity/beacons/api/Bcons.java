package com.example.inquallity.beacons.api;

import com.example.inquallity.beacons.Observations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Maksim Radko
 */
public interface Bcons {

//    @POST("/v1beta1/beaconinfo:getforobserved?key=AIzaSyDtplvoeAkWCZkYGPil0bMBDzfTHmG-w0k")
//    Call<Observations> fetchObservations();
    @GET("/v1beta1/beacons?q=status:active")
    Call<Observations> fetchObservations();
}
