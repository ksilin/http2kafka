package com.example;

import io.soabase.recordbuilder.core.RecordBuilder;
@RecordBuilder
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
        }
