package com.android.kswxdashboard.Converter.Metric;

import com.android.kswxdashboard.Converter.TimeConverter;

public class TimeConverter24 implements TimeConverter {
    @Override
    public String getTimeFormat() {
        return "HH:mm";
    }
}
