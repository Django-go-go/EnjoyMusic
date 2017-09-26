package com.example.administrator.httpdemo.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by HaKr on 05/09/16.
 */

public class ConnectionUtils {

    public static final String TAG = ConnectionUtils.class.getSimpleName();


    public static int FTP_SERVER_PORT = 2211;

    public static boolean isConnectedToLocalNetwork(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        connected = ni != null
                && ni.isConnected()
                && (ni.getType() & (ConnectivityManager.TYPE_WIFI
                | ConnectivityManager.TYPE_ETHERNET)) != 0;

        if (!connected) {
            Log.d(TAG, "isConnectedToLocalNetwork: see if it is an WIFI AP");
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            try {
                Method method = wm.getClass().getDeclaredMethod("isWifiApEnabled");
                connected = (Boolean) method.invoke(wm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!connected) {
            Log.d(TAG, "isConnectedToLocalNetwork: see if it is an USB AP");
            try {
                for (NetworkInterface netInterface : Collections.list(NetworkInterface
                        .getNetworkInterfaces())) {
                    if (netInterface.getDisplayName().startsWith("rndis")) {
                        connected = true;
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return connected;
    }


    public static boolean isConnectedToWifi(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected()
                && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static InetAddress getLocalInetAddress(Context context) {
        if (!isConnectedToLocalNetwork(context)) {
            Log.e(TAG, "getLocalInetAddress called and no connection");
            return null;
        }

        if (isConnectedToWifi(context)) {

            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int ipAddress = wm.getConnectionInfo().getIpAddress();
            if (ipAddress == 0)
                return null;
            return intToInet(ipAddress);
        }

        try {
            Enumeration<NetworkInterface> netinterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (netinterfaces.hasMoreElements()) {
                NetworkInterface netinterface = netinterfaces.nextElement();
                Enumeration<InetAddress> adresses = netinterface.getInetAddresses();
                while (adresses.hasMoreElements()) {
                    InetAddress address = adresses.nextElement();
                    // this is the condition that sometimes gives problems
                    if (!address.isLoopbackAddress()
                            && !address.isLinkLocalAddress())
                        return address;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InetAddress intToInet(int value) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = byteOfInt(value, i);
        }
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            // This only happens if the byte array has a bad length
            return null;
        }
    }

    public static byte byteOfInt(int value, int which) {
        int shift = which * 8;
        return (byte) (value >> shift);
    }

    public static boolean isPortAvailable(int port) {

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }

    public static String getFTPAddress(Context context){
        InetAddress inetAddress = getLocalInetAddress(context);
        if(null != inetAddress) {
            return "ftp://" + inetAddress.getHostAddress() + ":" + FTP_SERVER_PORT;
        }
        return "";
    }

    public static int getAvailablePortForFTP(){
        int port = 0;
        for(int i = FTP_SERVER_PORT ;i<65000;i++){
            if(isPortAvailable(i)) {
                port = i;
                break;
            }
        }
        return port;
    }

}
