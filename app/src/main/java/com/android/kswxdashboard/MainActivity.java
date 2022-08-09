package com.android.kswxdashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.kswxdashboard.Converter.GasConverter;
import com.android.kswxdashboard.Converter.Imperial.GasConverterImperial;
import com.android.kswxdashboard.Converter.Imperial.MileageConverterImperial;
import com.android.kswxdashboard.Converter.Imperial.SpeedConverterImperial;
import com.android.kswxdashboard.Converter.Imperial.TemperatureConverterImperial;
import com.android.kswxdashboard.Converter.Imperial.TimeConverter12;
import com.android.kswxdashboard.Converter.Metric.GasConverterMetric;
import com.android.kswxdashboard.Converter.Metric.MileageConverterMetric;
import com.android.kswxdashboard.Converter.Metric.SpeedConverterMetric;
import com.android.kswxdashboard.Converter.Metric.TemperatureConverterMetric;
import com.android.kswxdashboard.Converter.Metric.TimeConverter24;
import com.android.kswxdashboard.Converter.MilageConverter;
import com.android.kswxdashboard.Converter.SpeedConverter;
import com.android.kswxdashboard.Converter.TemperatureConverter;
import com.android.kswxdashboard.Converter.TimeConverter;
import com.wits.pms.ICmdListener;
import com.wits.pms.IContentObserver;
import com.wits.pms.statuscontrol.McuStatus;
import com.wits.pms.statuscontrol.PowerManagerApp;
import com.wits.pms.statuscontrol.WitsStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Calendar c;
    SimpleDateFormat simpleTimeFormat;

    final SettingsFragment settingsFragment = new SettingsFragment();
    SharedPreferenceListener preferenceListener = new SharedPreferenceListener();
    Context mContext;
    SharedPreferences preferences;
    GasConverter gasConverter;
    MilageConverter milageConverter;
    SpeedConverter speedConverter;
    TemperatureConverter temperatureConverter;
    TimeConverter timeConverter;

    private ICmdListener cmdListener = new ICmdListener.Stub() {
        @Override
        public boolean handleCommand(String str) {
            return false;
        }

        @Override
        public void updateStatusInfo(String str) {
            if (!str.isEmpty()) {
                WitsStatus status = WitsStatus.getWitsStatusFormJson(str);
                if (status.type == 5) {
                    updateUI(status.jsonArg);
                }
            }
        }
    };

    private IContentObserver contentObserver = new IContentObserver.Stub() {
        @Override
        public void onChange() throws RemoteException {
            updateUI(PowerManagerApp.getStatusString("mcuJson"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        try {
            findViewById(R.id.dashboardRoot).setOnClickListener(new View.OnClickListener() {
                boolean isShown = false;
                @Override
                public void onClick(View v) {
                    if (!isShown) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left)
                                .replace(R.id.settingsFragFrame, settingsFragment)
                                .commit();
                        ObjectAnimator.ofInt(findViewById(R.id.dashboardRoot).getBackground(), "alpha", 50).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.txtRpm), "alpha", 0.20f).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.imgDoor), "alpha", 0.20f).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.imgRpmneedle), "alpha", 0.20f).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.imgSpeedneedle), "alpha", 0.20f).setDuration(250).start();
                        isShown = true;
                    }
                    else {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left)
                                .remove(settingsFragment)
                                .commit();
                        settingsFragment.getView().setBackgroundColor(Color.TRANSPARENT);
                        ObjectAnimator.ofFloat(findViewById(R.id.imgSpeedneedle), "alpha", 1f).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.imgRpmneedle), "alpha", 1f).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.imgDoor), "alpha", 1f).setDuration(250).start();
                        ObjectAnimator.ofFloat(findViewById(R.id.txtRpm), "alpha", 1f).setDuration(250).start();
                        ObjectAnimator.ofInt(findViewById(R.id.dashboardRoot).getBackground(), "alpha", 255).setDuration(250).start();
                        isShown= false;
                    }
                }
            });

            hideSystemUI();

            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            initUnits(preferences);

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                c = Calendar.getInstance();
                                TextView txtTime = (TextView) findViewById(R.id.txtTime);
                                txtTime.setText(simpleTimeFormat.format(c.getTime()));
                            }
                            catch (Exception e) {
                                new AlertDialog.Builder(mContext).setTitle("Oh uh").setMessage(e.toString() + "\n\n\n" + e.getMessage() + "\n\n\n" + e.getCause() + "\n\n\n" + e.getStackTrace()).show();
                            }
                        }

                    });

                }
            }, 0, 1000);
        }
        catch (Exception e) {
            new AlertDialog.Builder(mContext).setTitle("Oh uh").setMessage("This App was designed only for Snapdragon 625 Android HUs!\n\n" + e.toString() + "\n\n\n" + e.getMessage() + "\n\n\n" + e.getCause() + "\n\n\n" + e.getStackTrace()).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        preferences.registerOnSharedPreferenceChangeListener(preferenceListener);
        try {
            PowerManagerApp.registerICmdListener(cmdListener);
            PowerManagerApp.registerIContentObserver("mcuJson", contentObserver);

            updateUI(PowerManagerApp.getStatusString("mcuJson"));
        } catch (Exception e) {
            new AlertDialog.Builder(mContext).setTitle("Oh uh").setMessage("This App was designed only for Snapdragon 625 Android HUs!\n\n" + e.toString() + "\n\n\n" + e.getMessage() + "\n\n\n" + e.getCause() + "\n\n\n" + e.getStackTrace()).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        preferences.unregisterOnSharedPreferenceChangeListener(preferenceListener);
        try {
            PowerManagerApp.unRegisterIContentObserver(contentObserver);
            PowerManagerApp.unRegisterICmdListener(cmdListener);
        } catch (Exception e) {
            new AlertDialog.Builder(mContext).setTitle("Oh uh").setMessage("This App was designed only for Snapdragon 625 Android HUs!\n\n" + e.toString() + "\n\n\n" + e.getMessage() + "\n\n\n" + e.getCause() + "\n\n\n" + e.getStackTrace()).show();
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

    private void initUnits(SharedPreferences preferences) {
        try {
            if (preferences.getBoolean("speed_imp", false))
                speedConverter = new SpeedConverterImperial();
            else
                speedConverter = new SpeedConverterMetric();

            if (preferences.getBoolean("milage_imp", false))
                milageConverter = new MileageConverterImperial();
            else
                milageConverter = new MileageConverterMetric();

            if (preferences.getBoolean("gas_imp", false))
                gasConverter = new GasConverterImperial();
            else
                gasConverter = new GasConverterMetric();

            if (preferences.getBoolean("temp_imp", false))
                temperatureConverter = new TemperatureConverterImperial();
            else
                temperatureConverter = new TemperatureConverterMetric();

            if (preferences.getBoolean("clock_imp", false))
                timeConverter = new TimeConverter12();
            else
                timeConverter = new TimeConverter24();

            simpleTimeFormat = new SimpleDateFormat(timeConverter.getTimeFormat());

            TextView txtTemperatureUnit = (TextView) findViewById(R.id.txtTemperatureUnit);
            txtTemperatureUnit.setText(temperatureConverter.getTemperatureUnit());

            TextView txtMileageUnit = (TextView) findViewById(R.id.txtMilageUnit);
            txtMileageUnit.setText(milageConverter.getMilageUnit());

            TextView txtGasUnit = (TextView) findViewById(R.id.txtGasUnit);
            txtGasUnit.setText(gasConverter.getGasUnit());

            TextView txtSpeedUnit = (TextView) findViewById(R.id.txtSpeedUnit);
            txtSpeedUnit.setText(speedConverter.getSpeedUnit());
            TextView txtSpeedUnitT = (TextView) findViewById(R.id.txtSpeedUnitT);
            txtSpeedUnitT.setText(speedConverter.getSpeedUnit());
            TextView txtSpeedIndi1 = (TextView) findViewById(R.id.IndiSpeed1);
            txtSpeedIndi1.setText(speedConverter.getIndiSpeed1());
            TextView txtSpeedIndi2 = (TextView) findViewById(R.id.IndiSpeed2);
            txtSpeedIndi2.setText(speedConverter.getIndiSpeed2());
            TextView txtSpeedIndi3 = (TextView) findViewById(R.id.IndiSpeed3);
            txtSpeedIndi3.setText(speedConverter.getIndiSpeed3());
            TextView txtSpeedIndi4 = (TextView) findViewById(R.id.IndiSpeed4);
            txtSpeedIndi4.setText(speedConverter.getIndiSpeed4());
            TextView txtSpeedIndi5 = (TextView) findViewById(R.id.IndiSpeed5);
            txtSpeedIndi5.setText(speedConverter.getIndiSpeed5());
            TextView txtSpeedIndi6 = (TextView) findViewById(R.id.IndiSpeed6);
            txtSpeedIndi6.setText(speedConverter.getIndiSpeed6());
            TextView txtSpeedIndi7 = (TextView) findViewById(R.id.IndiSpeed7);
            txtSpeedIndi7.setText(speedConverter.getIndiSpeed7());
            TextView txtSpeedIndi8 = (TextView) findViewById(R.id.IndiSpeed8);
            txtSpeedIndi8.setText(speedConverter.getIndiSpeed8());
        } catch (Exception e) {
            new AlertDialog.Builder(mContext).setTitle("Oh uh").setMessage("Something went wrong with initialising Units!\n\n" + e.toString() + "\n\n\n" + e.getMessage() + "\n\n\n" + e.getCause() + "\n\n\n" + e.getStackTrace()).show();
        }
    }

    private void updateUI(String mcuJson) {
        try {
            McuStatus mcuStatus = McuStatus.getStatusFromJson(mcuJson);

            try {
                Integer speedvalue = mcuStatus.carData.speed;
                ImageView imgSpeedneedle = (ImageView) findViewById(R.id.imgSpeedneedle);
                if (speedvalue < 21) {
                    imgSpeedneedle.setRotation((float) (speedvalue - 70));
                } else if (speedvalue > 20 & speedvalue < 31) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 20) * 1.5) - 50);
                } else if (speedvalue > 30 & speedvalue < 41) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 20) * 1.5) - 50);
                } else if (speedvalue > 40 & speedvalue < 51) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 40) * 1.2) - 20);
                } else if (speedvalue > 50 & speedvalue < 61) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 50) * 1.1) - 8);
                } else if (speedvalue > 60 & speedvalue < 71) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 60) * 0.7) + 3);
                } else if (speedvalue > 70 & speedvalue < 81) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 70) * 0.8) + 10);
                } else if (speedvalue > 80 & speedvalue < 91) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 80) * 0.7) + 18);
                } else if (speedvalue > 90 & speedvalue < 101) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 90) * 0.5) + 25);
                } else if (speedvalue > 100 & speedvalue < 111) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 100) * 1.0) + 30);
                } else if (speedvalue > 110 & speedvalue < 121) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 110) * 1.0) + 40);
                } else if (speedvalue > 120 & speedvalue < 131) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 120) * 0.7) + 50);
                } else if (speedvalue > 130 & speedvalue < 141) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 130) * 0.8) + 57);
                } else if (speedvalue > 140 & speedvalue < 151) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 140) * 0.8) + 65);
                } else if (speedvalue > 150 & speedvalue < 161) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 150) * 0.7) + 73);
                } else if (speedvalue > 160 & speedvalue < 171) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 160) * 0.8) + 80);
                } else if (speedvalue > 170 & speedvalue < 181) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 170) * 0.5) + 88);
                } else if (speedvalue > 180 & speedvalue < 191) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 180) * 0.3) + 93);
                } else if (speedvalue > 190 & speedvalue < 201) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 190) * 0.4) + 96);
                } else if (speedvalue > 200 & speedvalue < 211) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 200) * 0.5) + 100);
                } else if (speedvalue > 210 & speedvalue < 221) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 210) * 0.5) + 105);
                } else if (speedvalue > 220 & speedvalue < 231) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 220) * 0.5) + 110);
                } else if (speedvalue > 230 & speedvalue < 241) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 230) * 0.5) + 115);
                } else if (speedvalue > 240 & speedvalue < 251) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 240) * 0.5) + 120);
                } else if (speedvalue > 250 & speedvalue < 261) {
                    imgSpeedneedle.setRotation((float) ((speedvalue - 250) * 0.5) + 125);
                }
                TextView txtSpeed = (TextView) findViewById(R.id.txtSpeed);
                txtSpeed.setText(speedConverter.getSpeed(Integer.toString(speedvalue)));
            } catch (Exception e) {
            }

            try {
                ImageView imgRpmneedle = (ImageView) findViewById(R.id.imgRpmneedle);
                Integer rpmvalue = mcuStatus.carData.engineTurnS;
                if (rpmvalue < 1001) {
                    imgRpmneedle.setRotation((float) (70 - ((rpmvalue / 1000.0) * 20)));
                } else if (rpmvalue > 1000 & rpmvalue < 2001) {
                    imgRpmneedle.setRotation((float) (50 - (((rpmvalue / 1000.0) - 1) * 30)));
                } else if (rpmvalue > 2000 & rpmvalue < 3001) {
                    imgRpmneedle.setRotation((float) (20 - (((rpmvalue / 1000.0) - 2) * 23)));
                } else if (rpmvalue > 3000 & rpmvalue < 4001) {
                    imgRpmneedle.setRotation((float) (-3 - (((rpmvalue / 1000.0) - 3) * 27)));
                } else if (rpmvalue > 4000 & rpmvalue < 5001) {
                    imgRpmneedle.setRotation((float) (-30 - (((rpmvalue / 1000.0) - 4) * 35)));
                } else if (rpmvalue > 5000 & rpmvalue < 6001) {
                    imgRpmneedle.setRotation((float) (-60 - (((rpmvalue / 1000.0) - 5) * 35)));
                } else if (rpmvalue > 6000 & rpmvalue < 7001) {
                    imgRpmneedle.setRotation((float) (-100 - (((rpmvalue / 1000.0) - 6) * 30)));
                }
                TextView txtRpm = (TextView) findViewById(R.id.txtRpm);
                txtRpm.setText(rpmvalue.toString());
            } catch (Exception e) {
            }

            try {
                String temperature = Float.toString(mcuStatus.carData.airTemperature);
                TextView txtTemperature = (TextView) findViewById(R.id.txtTemperature);
                txtTemperature.setText(temperatureConverter.getTemperature(temperature));
            } catch (Exception e) {
            }

            try {
                String mileage = Integer.toString(mcuStatus.carData.mileage);
                TextView txtMileage = (TextView) findViewById(R.id.txtMileage);
                txtMileage.setText(milageConverter.getMilage(mileage));
            } catch (Exception e) {
            }

            try {
                String gas = Integer.toString(mcuStatus.carData.oilSum);
                TextView txtGas = (TextView) findViewById(R.id.txtGas);
                txtGas.setText(gasConverter.getGas(gas));
            } catch (Exception e) {
            }

            try {
                ImageView imgDoor = (ImageView) findViewById(R.id.imgDoor);
                String cardoor = Integer.toString(mcuStatus.carData.carDoor);
                imgDoor.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.class.getField("door" + cardoor).getInt(null)));
            } catch (Exception e) {
            }

            try {
                String handbreak = Boolean.toString(mcuStatus.carData.handbrake);
                ImageView imgHandbreak = (ImageView) findViewById(R.id.imgHandbreak);
                if (handbreak.contains("false")) {
                    imgHandbreak.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.handbreakoffok));
                } else {
                    imgHandbreak.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.handbreakonok));
                }
            } catch (Exception e) {
            }

            try {
                String seatbelt = Boolean.toString(mcuStatus.carData.safetyBelt);
                ImageView imgSeatbelt = (ImageView) findViewById(R.id.imgSeatbelt);
                if (seatbelt.contains("false")) {
                    imgSeatbelt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seatbeltoffok));
                } else {
                    imgSeatbelt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seatbeltonok));
                }
            } catch (Exception e) {
            }

        } catch (Exception e) {
            new AlertDialog.Builder(mContext).setTitle("Oh uh").setMessage("Something went wrong updating UI!\n\n" + e.toString() + "\n\n\n" + e.getMessage() + "\n\n\n" + e.getCause() + "\n\n\n" + e.getStackTrace()).show();
        }
    }

    class SharedPreferenceListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            initUnits(sharedPreferences);
        }
    }
}


