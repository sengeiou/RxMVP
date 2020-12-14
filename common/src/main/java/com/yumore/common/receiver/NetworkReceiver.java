package com.yumore.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.yumore.common.callback.OnNetworkListener;
import com.yumore.common.utility.EmptyUtils;
import com.yumore.common.utility.LoggerUtils;
import com.yumore.common.utility.NetworkUtils;

/**
 * @author Nathaniel
 * @date 2019/4/25 - 21:30
 */
public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkReceiver.class.getSimpleName();
    private OnNetworkListener onNetworkListener;

    public NetworkReceiver() {
    }

    public NetworkReceiver(OnNetworkListener onNetworkListener) {
        this.onNetworkListener = onNetworkListener;
    }

    public void setOnNetworkListener(OnNetworkListener onNetworkListener) {
        this.onNetworkListener = onNetworkListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int connectedType = NetworkUtils.getAPNType(context);
            // 接口回调传过去状态的类型
            if (!EmptyUtils.isEmpty(onNetworkListener)) {
                onNetworkListener.onStatusChanged(connectedType);
            }
        }

        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                LoggerUtils.logger(TAG, "WIFI已连接,移动数据已连接");
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                LoggerUtils.logger(TAG, "WIFI已连接,移动数据已断开");
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                LoggerUtils.logger(TAG, "WIFI已断开,移动数据已连接");
            } else {
                LoggerUtils.logger(TAG, "WIFI已断开,移动数据已断开");
            }
            //API大于23时使用下面的方式进行网络监听
        } else {
            //获得ConnectivityManager对象
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connectivityManager.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder stringBuilder = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (Network network : networks) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                stringBuilder.append(networkInfo.getTypeName()).append(" connect is ").append(networkInfo.isConnected());
            }
            LoggerUtils.logger(TAG, stringBuilder.toString());
        }
    }
}
