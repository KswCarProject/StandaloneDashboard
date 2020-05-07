package com.android.kswxdashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Calendar c;
    SimpleDateFormat simpleTimeFormat;
    private LogcatRecorder logcatRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSystemUI();

        simpleTimeFormat = new SimpleDateFormat("HH:mm");

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        c = Calendar.getInstance();
                        TextView txtTime = (TextView)findViewById(R.id.txtTime);
                        txtTime.setText(simpleTimeFormat.format(c.getTime()));
                    }

                });

            }
        }, 0, 1000);

        //Initialize the LogcatRecorder
        logcatRecorder = new LogcatRecorder(new OnLogcatRecorderListener() {

            @Override
            public void onNewLogEntry(final String logEntry) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String line = logEntry;

                        if (line.contains("IPowerManagerAppService") & line.contains("acData")) {


                            String[] cardata = line.split("airTemperature")[1].split(":");

                            String temperature = cardata[1].split(",")[0];
                            String cardoor = cardata[3].split(",")[0];
                            String rpm = cardata[5].split(",")[0];
                            String handbreak = cardata[6].split(",")[0];
                            String mileage = cardata[7].split(",")[0];
                            String gas = cardata[8].split(",")[0];
                            String seatbelt = cardata[11].split(",")[0];
                            String speed = cardata[12].split(",")[0];


                            Integer speedvalue = Integer.valueOf(speed);
                            Integer rpmvalue = Integer.valueOf(rpm);

                            ImageView imgSpeedneedle = (ImageView)findViewById(R.id.imgSpeedneedle);

                            if (speedvalue<21) {
                                imgSpeedneedle.setRotation((float) (speedvalue - 70));
                            } else if (speedvalue>20 & speedvalue<31) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-20)*1.5) - 50);
                            } else if (speedvalue>30 & speedvalue<41) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-20)*1.5) - 50);
                            } else if (speedvalue>40 & speedvalue<51) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-40)*1.2) - 20);
                            } else if (speedvalue>50 & speedvalue<61) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-50)*1.1) - 8);
                            } else if (speedvalue>60 & speedvalue<71) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-60)*0.7) + 3);
                            } else if (speedvalue>70 & speedvalue<81) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-70)*0.8) + 10);
                            } else if (speedvalue>80 & speedvalue<91) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-80)*0.7) + 18);
                            } else if (speedvalue>90 & speedvalue<101) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-90)*0.5) + 25);
                            } else if (speedvalue>100 & speedvalue<111) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-100)*1.0) + 30);
                            } else if (speedvalue>110 & speedvalue<121) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-110)*1.0) + 40);
                            } else if (speedvalue>120 & speedvalue<131) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-120)*0.7) + 50);
                            } else if (speedvalue>130 & speedvalue<141) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-130)*0.8) + 57);
                            } else if (speedvalue>140 & speedvalue<151) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-140)*0.8) + 65);
                            } else if (speedvalue>150 & speedvalue<161) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-150)*0.7) + 73);
                            } else if (speedvalue>160 & speedvalue<171) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-160)*0.8) + 80);
                            } else if (speedvalue>170 & speedvalue<181) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-170)*0.5) + 88);
                            } else if (speedvalue>180 & speedvalue<191) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-180)*0.3) + 93);
                            } else if (speedvalue>190 & speedvalue<201) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-190)*0.4) + 96);
                            } else if (speedvalue>200 & speedvalue<211) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-200)*0.5) + 100);
                            } else if (speedvalue>210 & speedvalue<221) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-210)*0.5) + 105);
                            } else if (speedvalue>220 & speedvalue<231) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-220)*0.5) + 110);
                            } else if (speedvalue>230 & speedvalue<241) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-230)*0.5) + 115);
                            } else if (speedvalue>240 & speedvalue<251) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-240)*0.5) + 120);
                            } else if (speedvalue>250 & speedvalue<261) {
                                imgSpeedneedle.setRotation((float) ((speedvalue-250)*0.5) + 125);
                            }


                            ImageView imgRpmneedle = (ImageView)findViewById(R.id.imgRpmneedle);

                            if (rpmvalue<1001) {
                                imgRpmneedle.setRotation((float) (70 - ((rpmvalue/1000.0) * 20)));
                            } else if (rpmvalue>1000 & rpmvalue<2001) {
                                imgRpmneedle.setRotation((float) (50 - (((rpmvalue/1000.0)-1) * 30)));
                            } else if (rpmvalue>2000 & rpmvalue<3001) {
                                imgRpmneedle.setRotation((float) (20 - (((rpmvalue/1000.0)-2) * 23)));
                            } else if (rpmvalue>3000 & rpmvalue<4001) {
                                imgRpmneedle.setRotation((float) (-3 - (((rpmvalue/1000.0)-3) * 27)));
                            } else if (rpmvalue>4000 & rpmvalue<5001) {
                                imgRpmneedle.setRotation((float) (-30 - (((rpmvalue/1000.0)-4) * 35)));
                            } else if (rpmvalue>5000 & rpmvalue<6001) {
                                imgRpmneedle.setRotation((float) (-60 - (((rpmvalue/1000.0)-5) * 35)));
                            } else if (rpmvalue>6000 & rpmvalue<7001) {
                                imgRpmneedle.setRotation((float) (-100 - (((rpmvalue/1000.0)-6) * 30)));
                            }


                            TextView txtTemperature = (TextView)findViewById(R.id.txtTemperature);
                            txtTemperature.setText(temperature);

                            TextView txtMileage = (TextView)findViewById(R.id.txtMileage);
                            txtMileage.setText(mileage);

                            TextView txtGas = (TextView)findViewById(R.id.txtGas);
                            txtGas.setText(gas);

                            TextView txtSpeed = (TextView)findViewById(R.id.txtSpeed);
                            txtSpeed.setText(speed);

                            TextView txtRpm = (TextView)findViewById(R.id.txtRpm);
                            txtRpm.setText(rpm);

                            ImageView imgDoor = (ImageView)findViewById(R.id.imgDoor);
                            try {
                                imgDoor.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.class.getField("door" + cardoor).getInt(null)));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            }

                            ImageView imgHandbreak = (ImageView)findViewById(R.id.imgHandbreak);
                            if (handbreak.contains("false")) {
                                imgHandbreak.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.handbreakoffok));
                            }else{
                                imgHandbreak.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.handbreakonok));
                            }

                            ImageView imgSeatbelt = (ImageView)findViewById(R.id.imgSeatbelt);
                            if (seatbelt.contains("false")) {
                                imgSeatbelt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seatbeltoffok));
                            }else{
                                imgSeatbelt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seatbeltonok));
                            }

                        }



                    }
                });
            }

        });


        try {
            logcatRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            logcatRecorder.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public void testlog(View view) {
        Log.i("IPowerManagerAppService", "sendStatus Msg: {\"jsonArg\":\"{\\\"acData\\\":{\\\"AC_Switch\\\":false,\\\"autoSwitch\\\":false,\\\"backMistSwitch\\\":false,\\\"frontMistSwitch\\\":false,\\\"isOpen\\\":false,\\\"leftTmp\\\":0.0,\\\"loop\\\":0,\\\"mode\\\":0,\\\"rightTmp\\\":0.0,\\\"speed\\\":0.0,\\\"sync\\\":false},\\\"benzData\\\":{\\\"airBagSystem\\\":false,\\\"airMaticStatus\\\":0,\\\"auxiliaryRadar\\\":false,\\\"highChassisSwitch\\\":false,\\\"light1\\\":0,\\\"light2\\\":0,\\\"pressButton\\\":0},\\\"carData\\\":{\\\"airTemperature\\\":22.0,\\\"averSpeed\\\":0.0,\\\"carDoor\\\":192,\\\"distanceUnitType\\\":0,\\\"engineTurnS\\\":7000,\\\"handbrake\\\":false,\\\"mileage\\\":309,\\\"oilSum\\\":27,\\\"oilUnitType\\\":0,\\\"oilWear\\\":0.0,\\\"safetyBelt\\\":true,\\\"speed\\\":150,\\\"temperatureUnitType\\\":0},\\\"mcuVerison\\\":\\\"615065dALS-ID5-X1-GT-190508-B18\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\\u0000\\\",\\\"systemMode\\\":1}\",\"type\":5}");
    }

}


