package com.android.kswxdashboard;

import com.android.kswxdashboard.cgutman.adblib.AdbBase64;
import com.android.kswxdashboard.cgutman.adblib.AdbConnection;
import com.android.kswxdashboard.cgutman.adblib.AdbCrypto;
import com.android.kswxdashboard.cgutman.adblib.AdbStream;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public class PmAdbManager {
    public static AdbBase64 getBase64Impl() {
        return new AdbBase64() {
            @Override
            public String encodeToString(byte[] data) {
                return Base64.encodeBase64String(data);
            }
        };
    }

    private static AdbCrypto setupCrypto(File fileDir, String pubKeyFile, String privKeyFile) throws NoSuchAlgorithmException, IOException {
        File publicKey = new File(fileDir, pubKeyFile);
        File privateKey = new File(fileDir, privKeyFile);
        AdbCrypto c = null;

        if (publicKey.exists() && privateKey.exists())
        {
            try {
                c = AdbCrypto.loadAdbKeyPair(getBase64Impl(), privateKey, publicKey);
            } catch (Exception e) {
                c = null;
            }
        }

        if (c == null)
        {
            c = AdbCrypto.generateAdbKeyPair(getBase64Impl());
            c.saveAdbKeyPair(privateKey, publicKey);
        }

        return c;
    }

    public static void tryGrantingPermissionOverAdb(final File fileDir, final String pm) throws Exception {
        final AtomicReference<Exception> outerexception = new AtomicReference<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    AdbCrypto adbCrypto = setupCrypto(fileDir, "public.key", "private.key");
                    Socket socket = new Socket ();
                    socket.connect(new InetSocketAddress("localhost", 5555), 5000);
                    AdbConnection adbConnection = AdbConnection.create(socket, adbCrypto);
                    adbConnection.connect();

                    AdbStream shellStream = adbConnection.open("shell:");
                    shellStream.write("pm grant " + BuildConfig.APPLICATION_ID + " " + pm + "\n");
                    shellStream.close();
                    adbConnection.close();
                    socket.close();
                }
                catch (Exception innerException){
                    outerexception.set(innerException);
                }
            }
        };

        thread.start();
        thread.join();

        if (outerexception.get() != null)
            throw outerexception.get();
    }
}
