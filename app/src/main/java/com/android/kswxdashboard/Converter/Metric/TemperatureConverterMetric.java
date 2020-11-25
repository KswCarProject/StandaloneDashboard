package com.android.kswxdashboard.Converter.Metric;

import com.android.kswxdashboard.Converter.TemperatureConverter;

public class TemperatureConverterMetric implements TemperatureConverter {
    @Override
    public String getTemperature(String temperature) {
        return temperature;
    }

    @Override
    public String getTemperatureUnit() {
        return "°C";
    }
}
