package com.wits.pms.statuscontrol;

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.gson.Gson;
import com.wits.pms.ICmdListener;
import com.wits.pms.IContentObserver;
import com.wits.pms.IPowerManagerAppService;
import java.util.HashMap;

public class PowerManagerApp {

    public static IPowerManagerAppService getManager() {
        return IPowerManagerAppService.Stub.asInterface(getService("wits_pms"));
    }

    public static void registerICmdListener(ICmdListener listener) {
        try {
            getManager().registerCmdListener(listener);
        } catch (RemoteException e) {
        }
    }

    public static void registerIContentObserver(String key, IContentObserver contentObserver) {
        Log.i("IPowerManagerService", contentObserver.getClass().getName());
        try {
            getManager().registerObserver(key, contentObserver);
        } catch (RemoteException e) {
        }
    }

    public static void unRegisterIContentObserver(IContentObserver contentObserver) {
        try {
            getManager().unregisterObserver(contentObserver);
        } catch (RemoteException e) {
        }
    }

    public static void unRegisterICmdListener(ICmdListener cmdListener) {
        try {
            getManager().unregisterCmdListener(cmdListener);
        } catch (RemoteException e) {
        }
    }

    @SuppressLint({"PrivateApi"})
    public static IBinder getService(String serviceName) {
        try {
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            return (IBinder) serviceManager.getMethod("getService", new Class[]{String.class}).invoke(serviceManager, new Object[]{serviceName});
        } catch (Exception e) {
            String name = PowerManagerApp.class.getName();
            Log.e(name, "error service init - " + serviceName, e);
            return null;
        }
    }

    public static boolean sendCommand(String jsonMsg) {
        try {
            return getManager().sendCommand(jsonMsg);
        } catch (RemoteException e) {
            Log.i(getManager().getClass().getName(), "error sendCommand", e);
            return false;
        }
    }

    public static void sendStatus(WitsStatus witsStatus) {
        if (getManager() != null) {
            try {
                getManager().sendStatus(new Gson().toJson((Object) witsStatus));
            } catch (RemoteException e) {
            }
        }
    }

    public static void setBooleanStatus(String key, boolean value) throws RemoteException {
        getManager().addBooleanStatus(key, value);
    }

    public static void setStatusString(String key, String value) throws RemoteException {
        getManager().addStringStatus(key, value);
    }

    public static void setStatusInt(String key, int value) throws RemoteException {
        getManager().addIntStatus(key, value);
    }

    public static boolean getStatusBoolean(String key) throws RemoteException {
        return getManager().getStatusBoolean(key);
    }

    public static String getStatusString(String key) throws RemoteException {
        return getManager().getStatusString(key);
    }

    public static int getStatusInt(String key) throws RemoteException {
        return getManager().getStatusInt(key);
    }

    public static int getSettingsInt(String key) throws RemoteException {
        return getManager().getSettingsInt(key);
    }

    public static String getSettingsString(String key) throws RemoteException {
        return getManager().getSettingsString(key);
    }

    public static void setSettingsInt(String key, int value) throws RemoteException {
        getManager().setSettingsInt(key, value);
    }

    public static void setSettingsString(String key, String value) throws RemoteException {
        getManager().setSettingsString(key, value);
    }
}
