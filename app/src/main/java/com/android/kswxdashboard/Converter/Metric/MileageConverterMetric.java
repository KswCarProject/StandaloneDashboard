package com.android.kswxdashboard.Converter.Metric;

import com.android.kswxdashboard.Converter.MilageConverter;

public class MileageConverterMetric implements MilageConverter {
    @Override
    public String getMilage(String milage) {
        return milage;
    }

    @Override
    public String getMilageUnit() {
        return "km";
    }
}
