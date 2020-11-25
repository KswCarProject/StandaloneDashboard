package com.android.kswxdashboard.Converter.Imperial;

import com.android.kswxdashboard.Converter.GasConverter;

public class GasConverterImperial implements GasConverter {
    @Override
    public String getGas(String gas) {

        return Integer.toString((int)(Integer.valueOf(gas) / 3.785));
    }

    @Override
    public String getGasUnit() {
        return "gal";
    }
}
