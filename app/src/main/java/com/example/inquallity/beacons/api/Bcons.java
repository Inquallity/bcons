package com.example.inquallity.beacons.api;

import android.support.annotation.NonNull;

import com.example.inquallity.beacons.model.Attachment;
import com.example.inquallity.beacons.model.AttachmentCreateResult;
import com.example.inquallity.beacons.model.AttachmentDeleteResult;
import com.example.inquallity.beacons.model.BatchDeleteResult;
import com.example.inquallity.beacons.model.BconActivate;
import com.example.inquallity.beacons.model.BconDeactivate;
import com.example.inquallity.beacons.model.BconDecommission;
import com.example.inquallity.beacons.model.BconDelete;
import com.example.inquallity.beacons.model.BconDiagnostics;
import com.example.inquallity.beacons.model.BconNamespace;
import com.example.inquallity.beacons.model.BeaconInfo;
import com.example.inquallity.beacons.model.BeaconResponse;
import com.example.inquallity.beacons.model.EidParams;
import com.example.inquallity.beacons.model.NamespacesResponse;
import com.example.inquallity.beacons.model.Observation;
import com.example.inquallity.beacons.model.Observed;
import com.example.inquallity.beacons.model.ObservedRequest;
import com.example.inquallity.beacons.model.RegBcon;
import com.example.inquallity.beacons.model.UpdateBcon;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Maksim Radko
 */
public interface Bcons {

    @GET("/v1beta1/eidparams")
    Observable<EidParams> fetchEidParams();

    @POST("/v1beta1/beaconinfo:getforobserved")
    Observable<Observed> getForObserved(@Body @NonNull ObservedRequest body, @Query("key") String apiKey);

    @GET("/v1beta1/beacons?q=status:active")
    Observable<Observation> fetchObservations();

    @POST("/v1beta1/beacons/{beaconName}:activate")
    Observable<BconActivate> activateBeacon(@Path("beaconName") String beaconName);

    @POST("/v1beta1/beacons/{beaconName}:deactivate")
    Observable<BconDeactivate> deactivateBeacon(@Path("beaconName") String beaconName);

    @POST("/v1beta1/beacons/{beaconName}:decommission")
    Observable<BconDecommission> decommissionBeacon(@Path("beaconName") String beaconName);

    @DELETE("/v1beta1/beacons/{beaconName}")
    Observable<BconDelete> deleteBeacon(@Path("beaconName") String beaconName);

    @GET("/v1beta1/beacons/{beaconName}")
    Observable<BeaconInfo> getBeacon(@Path("beaconName") String beaconName);

    @GET("/v1beta1/beacons")
    Observable<BeaconResponse> getBeaconsList();

    @POST("/v1beta1/beacons:register")
    Observable<RegBcon> registerBeacon();

    @PUT("/v1beta1/beaconName")
    Observable<UpdateBcon> updateBeacon(@Path("beaconName") String beaconName);

    @POST("/v1beta1/beacons/{beaconName}/attachments:batchDelete")
    Observable<BatchDeleteResult> batchDeleteAttachments();

    @POST("/v1beta1/{beaconName}/attachments")
    Observable<AttachmentCreateResult> createAttachment();

    @DELETE("/v1beta1/{beaconName}")
    Observable<AttachmentDeleteResult> deleteAttachment(@Path("beaconName") String beaconName);

    @GET("/v1beta1/{beaconName}/attachments")
    Observable<List<Attachment>> getAttachments(@Path("beaconName") String beaconName);

    @GET("/v1beta1/{beaconName}/diagnostics")
    Observable<BconDiagnostics> getDiagnostics(@Path("beaconName") String beaconName);

    @GET("/v1beta1/namespaces")
    Observable<NamespacesResponse> getNamespaces();

    @PUT("/v1beta/{namespaceName}")
    Observable<BconNamespace> updateNamespace(@Path("namespaceName") String namespaceName);
}
