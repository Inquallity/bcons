package com.example.inquallity.beacons.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Maksim Radko
 */
public class NamespacesResponse {

    @SerializedName("namespaces")
    private List<BconNamespace> mBconNamespaces;

    public List<BconNamespace> getBconNamespaces() {
        return mBconNamespaces;
    }
}
