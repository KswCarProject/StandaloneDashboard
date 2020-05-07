# StandaloneDashboard
A Standalone dashboard as an alternative

It reads the car data from Logcat, where this line appears from IPowerManagerAppService everytime a change is happening:

"sendStatus Msg: {\"jsonArg\":\"{\\\"acData\\\":{\\\"AC_Switch\\\":false,\\\"autoSwitch\\\":false,\\\"backMistSwitch\\\":false,\\\"frontMistSwitch\\\":false,\\\"isOpen\\\":false,\\\"leftTmp\\\":0.0,\\\"loop\\\":0,\\\"mode\\\":0,\\\"rightTmp\\\":0.0,\\\"speed\\\":0.0,\\\"sync\\\":false},\\\"benzData\\\":{\\\"airBagSystem\\\":false,\\\"airMaticStatus\\\":0,\\\"auxiliaryRadar\\\":false,\\\"highChassisSwitch\\\":false,\\\"light1\\\":0,\\\"light2\\\":0,\\\"pressButton\\\":0},\\\"carData\\\":{\\\"airTemperature\\\":22.0,\\\"averSpeed\\\":0.0,\\\"carDoor\\\":192,\\\"distanceUnitType\\\":0,\\\"engineTurnS\\\":7000,\\\"handbrake\\\":false,\\\"mileage\\\":309,\\\"oilSum\\\":27,\\\"oilUnitType\\\":0,\\\"oilWear\\\":0.0,\\\"safetyBelt\\\":true,\\\"speed\\\":150,\\\"temperatureUnitType\\\":0},\\\"mcuVerison\\\":\\\"615065dALS-ID5-X1-GT-190508-B18\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\",\\\"systemMode\\\":1}\",\"type\":5}"

Remember that this is only happening on Snapdragon Devices. On Rockchip platforms this wont appear.
Take notice the layout was built for 1280x480 devices. Other resolutions need to adjust the layout file (activity_main.xml)



The app needs READ_LOGS permission.

Grant them by installing the app and running this command in adb:

adb shell pm grant com.android.kswxdashboard android.permission.READ_LOGS

app-debug.apk is a compiled version

Theme was provided by Rudy

Take care 
