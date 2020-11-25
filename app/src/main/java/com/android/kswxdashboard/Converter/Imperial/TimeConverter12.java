package com.android.kswxdashboard.Converter.Imperial;

import com.android.kswxdashboard.Converter.TimeConverter;

public class TimeConverter12 implements TimeConverter {
    @Override
    public String getTimeFormat() {
        return "hh:mm a";
    }
}
