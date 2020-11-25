package com.android.kswxdashboard.Converter.Imperial;

import com.android.kswxdashboard.Converter.SpeedConverter;

public class SpeedConverterImperial implements SpeedConverter {
    @Override
    public String getSpeedUnit() {
        return "mph";
    }

    @Override
    public String getSpeed(String speedInKmh) {

        return Integer.toString((int)(Integer.valueOf(speedInKmh) / 1.609));
    }

    @Override
    public String getIndiSpeed8() {
        return "160";
    }

    @Override
    public String getIndiSpeed7() {
        return "125";
    }

    @Override
    public String getIndiSpeed6() {
        return "85";
    }

    @Override
    public String getIndiSpeed5() {
        return "60";
    }

    @Override
    public String getIndiSpeed4() {
        return "35";
    }

    @Override
    public String getIndiSpeed3() {
        return "25";
    }

    @Override
    public String getIndiSpeed2() {
        return "10";
    }

    @Override
    public String getIndiSpeed1() {
        return "0";
    }
}
