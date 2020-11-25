package com.android.kswxdashboard.Converter.Imperial;

import com.android.kswxdashboard.Converter.MilageConverter;

public class MileageConverterImperial implements MilageConverter {
    @Override
    public String getMilage(String milage) {

        return Integer.toString((int)(Double.valueOf(milage) / 1.609));
    }

    @Override
    public String getMilageUnit() {
        return "miles";
    }
}
