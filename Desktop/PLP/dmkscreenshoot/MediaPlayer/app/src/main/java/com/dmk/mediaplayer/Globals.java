package com.dmk.mediaplayer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dmk.mediaplayer.adapter.PlayListVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BSILIND\parthiban.m on 18/2/16.
 */
public class Globals {

   static Context con;

    public static List<PlayListVO> list=new ArrayList<>();
    public static boolean isConnected(Context context) {
        con = context;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
