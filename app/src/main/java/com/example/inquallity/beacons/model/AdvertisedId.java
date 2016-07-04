package com.example.inquallity.beacons.model;

/**
 * @author Maksim Radko
 */
public class AdvertisedId {

    public static final String TYPE_UNSPECIFIED = "TYPE_UNSPECIFIED";

    public static final String EDDYSTONE = "EDDYSTONE";

    public static final String IBEACON = "IBEACON";

    public static final String ALTBEACON = "ALTBEACON";

    public static final String EDDYSTONE_EID = "EDDYSTONE_EID";

    private String mId;

    private String mType;
}
