package com.graphhopper.routing.util;

import com.graphhopper.reader.ReaderNode;
import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.ev.*;
import com.graphhopper.storage.IntsRef;
import com.graphhopper.util.PMap;


public class RoadsTagParser extends VehicleTagParser {
    public static final double ROADS_MAX_SPEED = 254;

    public RoadsTagParser(EncodedValueLookup lookup, PMap properties) {
        super(
                lookup.getBooleanEncodedValue(VehicleAccess.key("roads")),
                lookup.getDecimalEncodedValue(VehicleSpeed.key("roads")),
                lookup.getBooleanEncodedValue(Roundabout.KEY),
                TransportationMode.valueOf(properties.getString("transportation_mode", "VEHICLE")),
                lookup.getDecimalEncodedValue(VehicleSpeed.key("roads")).getNextStorableValue(ROADS_MAX_SPEED)
        );
    }

    public RoadsTagParser(BooleanEncodedValue accessEnc, DecimalEncodedValue speedEnc) {
        super(accessEnc, speedEnc, null, TransportationMode.VEHICLE, speedEnc.getNextStorableValue(ROADS_MAX_SPEED));
    }

    @Override
    public IntsRef handleWayTags(IntsRef edgeFlags, ReaderWay way) {
        // let's make it high and let it be reduced in the custom model
        double speed = maxPossibleSpeed;
        accessEnc.setBool(true, edgeFlags, true);
        accessEnc.setBool(false, edgeFlags, true);
        setSpeed(false, edgeFlags, speed);
        if (avgSpeedEnc.isStoreTwoDirections())
            setSpeed(true, edgeFlags, speed);
        return edgeFlags;
    }

    @Override
    public boolean isBarrier(ReaderNode node) {
        return false;
    }
}
