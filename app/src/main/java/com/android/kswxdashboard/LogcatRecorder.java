package com.android.kswxdashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LogcatRecorder {

    private Process continuousLogging;
    private StringBuilder log;
    private boolean recording;

    private OnLogcatRecorderListener onLogcatRecorderListener;

    /**
     * LogcatSpy constructor with a predefined OnLogcatRecorderListener.
     *
     * @param onLogcatRecorderListener OnLogcatRecorderListener to handle recorder states.
     */
    public LogcatRecorder(OnLogcatRecorderListener onLogcatRecorderListener) {
        this.recording = false;
        this.onLogcatRecorderListener = onLogcatRecorderListener;
    }


    public void start() throws IllegalStateException {

        if (!recording) {
            recording = true;

            Thread thread = new Thread() {
                public void run() {
                    log = new StringBuilder();
                    String line;
                    try {
                        //Clear all logcat entries, up until this point.
                        continuousLogging = Runtime.getRuntime().exec("logcat -c");
                        continuousLogging = Runtime.getRuntime().exec("logcat IPowerManagerAppService:I *:S");

                        //Get the Runtime Process' output.
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(continuousLogging.getInputStream()));

                        while ((line = bufferedReader.readLine()) != null) {
                            final String logEntry = line + "\n";

                            log.append(logEntry);

                            if (onLogcatRecorderListener != null) {
                                onLogcatRecorderListener.onNewLogEntry(logEntry);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recording = false;
                    }
                }

            };
            thread.start();
        } else {
            throw new IllegalStateException("Unable to call start(): Already recording.");
        }
    }


    public void stop() throws IllegalStateException {
        if (recording) {
            if (onLogcatRecorderListener != null) {
                if (log == null || log.toString().length() == 0) {
                    log = new StringBuilder("n/a");
                }
            }

            if (continuousLogging != null) {
                //Kill the Runtime Process.
                continuousLogging.destroy();
            }

            recording = false;
        } else {
            throw new IllegalStateException("Unable to call stop(): Currently not recording.");
        }
    }
}