package com.android.kswxdashboard.Converter.Metric;

import com.android.kswxdashboard.Converter.SpeedConverter;

public class SpeedConverterMetric implements SpeedConverter {
    @Override
    public String getSpeedUnit() {
        return "km/h";
    }

    @Override
    public String getSpeed(String speedInKmh) {
        return speedInKmh;
    }

    @Override
    public String getIndiSpeed8() {
        return "260";
    }

    @Override
    public String getIndiSpeed7() {
        return "200";
    }

    @Override
    public String getIndiSpeed6() {
        return "140";
    }

    @Override
    public String getIndiSpeed5() {
        return "100";
    }

    @Override
    public String getIndiSpeed4() {
        return "60";
    }

    @Override
    public String getIndiSpeed3() {
        return "40";
    }

    @Override
    public String getIndiSpeed2() {
        return "20";
    }

    @Override
    public String getIndiSpeed1() {
        return "0";
    }
}
