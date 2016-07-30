package com.example.inquallity.beacons.model;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Maksim Radko
 */
public class BconNamespace {

    public static final String VISIBILITY_UNSPECIFIED = "VISIBILITY_UNSPECIFIED";

    public static final String UNLISTED = "UNLISTED";

    public static final String PUBLIC = "PUBLIC";

    @SerializedName("namespaceName")
    private String mNamespaceName;

    @Visibility
    @SerializedName("servingVisibility")
    private String mVisibility;

    public String getNamespaceName() {
        return mNamespaceName;
    }

    @Visibility
    public String getVisibility() {
        return mVisibility;
    }

    @StringDef({VISIBILITY_UNSPECIFIED, UNLISTED, PUBLIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {

    }

}
