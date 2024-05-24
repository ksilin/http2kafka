package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.soabase.recordbuilder.core.RecordBuilder;
@RecordBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CamEventRequest( String carID,
        String roiID,
        String carState,
        String datetime,
        String plateList,
        String plateText,
        String plateUTF8,
        String profileID,
        String capture_ts,
        String plateASCII,
        String plateCountry,
        String plateUnicode,
        String timeProcessing,
        String plateConfidence,
        String carMoveDirection,
        String sensorProviderID,
        String capture_timestamp) implements CamEventRequestBuilder.With {

        public static final String STATE_NEW = "new";
        public static final String STATE_UPDATE = "update";
        public static final String STATE_LOST = "lost";
        public static final String STATE_UNKNOWN = "unknown";

        public static final String DIRECTION_IN = "in";
        public static final String DIRECTION_OUT = "out";


        }
