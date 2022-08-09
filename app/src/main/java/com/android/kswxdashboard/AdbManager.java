package com.android.kswxdashboard;

import android.content.Context;
import android.net.TrafficStats;

import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbCrypto;
import com.cgutman.adblib.AdbStream;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class AdbManager {
    private static Boolean isConnected = false;
    private static Socket socket;
    private static AdbConnection adbConnection;
    private static AdbStream shellStream;

    public static void sendCommand(String command, Context context) throws Exception {
        if (isConnected) {
            writeCommand(command);
        } else {
            connect(context);
            if (isConnected) {
                writeCommand(command);
            }
            disconnect();
        }
    }

    private static void writeCommand(final String command) throws InterruptedException {
        Thread writerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (shellStream != null) {
                        shellStream.write(" " + command +"\n");
                    } else {
                        isConnected = false;
                    }
                }
                catch (Exception e) {
                    isConnected = false;
                }
            }
        });
        writerThread.start();
        writerThread.join();
    }

    static void connect(Context context) throws Exception {
        connect(context, "localhost");
    }

    private static void connect(final Context context, final String address) throws Exception {
        if (isConnected) {
            disconnect();
        }
        final Exception[] outerException = {null};
        Thread setupThread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        TrafficStats.setThreadStatsTag((int) Thread.currentThread().getId());
                        AdbCrypto adbCrypto = setupCrypto(context.getFilesDir());
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(address, 5555), 5000);
                        adbConnection = AdbConnection.create(socket, adbCrypto);
                        adbConnection.connect();
                        shellStream = adbConnection.open("shell:");
                    } catch (Exception e) {
                        outerException[0] = e;
                    }
                }

                private AdbCrypto setupCrypto(File fileDir) throws IOException, NoSuchAlgorithmException {
                    File publicKey = new File(fileDir, "public.key");
                    File privateKey = new File(fileDir, "private.key");
                    AdbCrypto adbCrypto = null;
                    if (publicKey.exists() && privateKey.exists()) {
                        try {
                            adbCrypto = AdbCrypto.loadAdbKeyPair(getBase64Impl(), privateKey, publicKey);
                        } catch (Exception e) { }
                    }
                    if (adbCrypto == null) {
                        adbCrypto = AdbCrypto.generateAdbKeyPair(getBase64Impl());
                        adbCrypto.saveAdbKeyPair(privateKey, publicKey);
                    }
                    return adbCrypto;
                }

                private AdbBase64 getBase64Impl() {
                    return new AdbBase64() {
                        @Override
                        public String encodeToString(byte[] data) {
                            return Base64.encodeBase64String(data);
                        }
                    };
                }
            }
        );
        setupThread.start();
        setupThread.join();

        if (outerException[0] != null) {
            throw outerException[0];
        }

        isConnected = shellStream != null;
    }

    static void disconnect() throws InterruptedException {
        Thread disconnecterThread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        if (shellStream != null) {
                            shellStream.close();
                        }
                        if (adbConnection != null) {
                            adbConnection.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    }
                    catch (Exception e) { }
                }
            }
        );
        disconnecterThread.start();
        disconnecterThread.join();
        isConnected = false;
    }
}
