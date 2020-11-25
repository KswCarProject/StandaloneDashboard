package com.android.kswxdashboard.Converter.Metric;

import com.android.kswxdashboard.Converter.GasConverter;

public class GasConverterMetric implements GasConverter {
    @Override
    public String getGas(String gas) {
        return gas;
    }

    @Override
    public String getGasUnit() {
        return "l";
    }
}
