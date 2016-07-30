package com.example.inquallity.beacons.model;

import java.util.List;

/**
 * @author Maksim Radko
 */
public class ObservedRequest {

    private List<Observation> mObservations;

    private List<String> mNamespaces;

    public void setObservations(List<Observation> observations) {
        mObservations = observations;
    }

    public void setNamespaces(List<String> namespaces) {
        mNamespaces = namespaces;
    }
}
