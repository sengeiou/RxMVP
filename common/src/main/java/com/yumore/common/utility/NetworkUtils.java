package com.yumore.common.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author Nathaniel
 * @date 2019/4/13 - 20:16
 */
public class NetworkUtils {
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_4G = 4;
    public static final int NETWORK_TYPE_3G = 3;
    public static final int NETWORK_TYPE_2G = 2;

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * 获取活动网络信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager contextSystemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return contextSystemService.getActiveNetworkInfo();
    }

    /**
     * 判断当前网络是否是移动网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断当前网络是否是wifi网络
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断当前网络是否是2G网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is2G(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                .getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA);
    }

    /**
     * wifi是否打开
     */
    public static boolean isWifiEnabled(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telephonyManager
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(@NonNull Context context) {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @param context
     * @return
     */
    public static boolean isMobileConnected(@NonNull Context context) {
        //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     * 原生
     *
     * @param context
     * @return
     */
    public static int getConnectedType(@NonNull Context context) {
        //获取手机所有连接管理对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return networkInfo.getType();
        }
        return -1;
    }

    /**
     * 获取当前的网络状态
     * 没有网络-0
     * WIFI网络-1
     * 4G网络-4
     * 3G网络-3
     * 2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    public static int getAPNType(@NonNull Context context) {
        int networkType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return networkType;
        }
        int networkInfoType = networkInfo.getType();
        if (networkInfoType == ConnectivityManager.TYPE_WIFI) {
            // WIFI
            networkType = 1;
        } else if (networkInfoType == ConnectivityManager.TYPE_MOBILE) {
            int subtype = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (subtype == TelephonyManager.NETWORK_TYPE_LTE && !telephonyManager.isNetworkRoaming()) {
                networkType = 4;
            } else if (subtype == TelephonyManager.NETWORK_TYPE_UMTS
                    || subtype == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subtype == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                // 3G联通的3G为UMTS或HSDPA 电信的3G为EVDO
                networkType = 3;
            } else if (subtype == TelephonyManager.NETWORK_TYPE_GPRS
                    || subtype == TelephonyManager.NETWORK_TYPE_EDGE
                    || subtype == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                // 2G移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
                networkType = 2;
            } else {
                networkType = 2;
            }
        }
        return networkType;
    }

    /**
     * 判断GPS是否打开
     * ACCESS_FINE_LOCATION权限
     *
     * @param context
     * @return
     */
    public static boolean isGPSEnabled(@NonNull Context context) {
        //获取手机所有连接LOCATION_SERVICE对象
        LocationManager locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 获得本机ip地址
     *
     * @return
     */
    public static String getHostIp() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机串号imei
     *
     * @param context
     * @return
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /***
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     *
     * @return true/false
     */
    public static boolean ping() {
        String result = null;
        try {
            // ping 的地址，可以换成任何一种可靠的外网
            String ip = "www.baidu.com";
            // ping网址3次
            Process process = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
            // 读取ping的内容，可以不加
            InputStream input = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuilder.append(content);
            }
            LoggerUtils.e("------ping-----", "result content : " + stringBuilder.toString());
            // ping的状态
            int status = process.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    /**
     * 获取网络的时时网速，使用方法是：
     * 每隔一段时间读取一次总流量，然后用本次和前一次的差除以间隔时间来获取平均速度，再换算为 K/s M/s
     * 等单位，显示即可。
     *
     * @return 实时的网速（单位byte）
     */
    public static int getNetworkSpeedBytes() {
        String line;
        String[] strings;
        int received = 0;
        int tmp = 0;
        boolean isNumber;
        try {
            FileReader fileReader = new FileReader("/proc/net/dev");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 500);
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("rmnet") || line.startsWith("eth") || line.startsWith("wlan")) {
                    strings = line.split(":")[1].split(" ");
                    for (int i = 0; i < strings.length; i++) {
                        isNumber = true;
                        try {
                            tmp = Integer.parseInt(strings[i]);
                        } catch (Exception e) {
                            isNumber = false;
                        }
                        if (isNumber) {
                            received = received + tmp;
                            break;
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            return -1;
        }
        return received;
    }
}
