package com.android.kswxdashboard.Converter.Imperial;

import com.android.kswxdashboard.Converter.TemperatureConverter;

public class TemperatureConverterImperial implements TemperatureConverter {
    @Override
    public String getTemperature(String temperature) {

        return Integer.toString((int)(Double.valueOf(temperature) * 9/5.0) + 32);
    }

    @Override
    public String getTemperatureUnit() {
        return "°F";
    }
}
