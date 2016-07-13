package com.beto4812.airqueue.aws;

import com.amazonaws.regions.Regions;

public class AWSConstants {

    public static final String IDENTITY_POOL_ID = "us-east-1:c25d2776-c1af-4ff5-98e6-c28d8321a548";
    public static final Regions COGNITO_REGION = Regions.US_EAST_1;
    public static final Regions DYNAMO_REGION = Regions.EU_CENTRAL_1;
    public static final String SENSOR_READINGS_TABLE_NAME = "airquality";
    public static final String POLLUTANT_CATEGORY_INFO_TABLE_NAME = "airquality_particles_info";
    public static final String POLLUTANT_THESHOLD_TABLE_NAME = "airquality_pollutant_threshold";
    public static final String SENSOR_COORDINATES_TABLE_NAME = "airquality_sourceID_coordinates";

}
