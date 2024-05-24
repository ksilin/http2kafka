package com.example;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class MockDataFactory {

    private static final PodamFactory mockPojoFactory = new PodamFactoryImpl();

    public static final String sensorProviderId_notEntryExit = "BXA_0_Service_Links";
    public static final String sensorProviderId_entryExit = "BXA_1_Einfahrt_Front";


    // need a 'real plate to pass the regex check
    public static final String plateWithAppointment = "HAL LO123";
    public static final String plateWithoutAppointment = "XXX";

    public static final String branchId = "456";
    public static final String wrongBranchId = "WRONG_BRANCH";
    public static final String wrongSite = "WRONG_SITE";

    public static CamEventRequest makeCamEventRequest(String timestampString, String carID, String plate, String sensorProviderID){
        CamEventRequest rawEventRoot = mockPojoFactory.manufacturePojoWithFullData(CamEventRequest.class);
        CamEventRequest validBuffer = rawEventRoot.withCapture_ts(timestampString).withSensorProviderID(sensorProviderID).withPlateConfidence("0.8").withCapture_timestamp(timestampString);
        return validBuffer.withCarID(carID).withPlateUTF8(plate).withCarState(CamEventRequest.STATE_NEW).withCarMoveDirection(CamEventRequest.DIRECTION_IN);
    }

    public static CamEventRequest makeCamEventRequest(String timestampString, String carID, String plate){
        return makeCamEventRequest(timestampString, carID, plate, sensorProviderId_notEntryExit);
    }

}
