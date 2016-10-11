package com.example.domowy.netsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /////

        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        // checking if phone is charging if yes then
        if(isCharging)
        {

            //checking if phone have conection with network if yes then
            if(isNetworkAvaliable(context))
            {
                Toast.makeText(context, "Do you want to close internet connection?", Toast.LENGTH_LONG).show();

                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
               // Context context = getApplicationContext();
                try {
                    setMobileConnectionDisabled(context, false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                Toast.makeText(context, "Send Sleep cuz net is not avaliable", Toast.LENGTH_LONG).show();
            }
        }
        else
        {

            Toast.makeText(context, "Send Sleep cuz itr isnt charging", Toast.LENGTH_LONG).show();
           finish();
        }

    }

    private void setMobileConnectionDisabled(Context context, boolean disabled) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {
        final ConnectivityManager mConnectivityManager = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class mClass = Class.forName(mConnectivityManager.getClass().getName());
        final Field mField = mClass.getDeclaredField("mService");
        mField.setAccessible(true);
        final Object mObject = mField.get(mConnectivityManager);
        final Class mConnectivityManagerClass =  Class.forName(mObject.getClass().getName());
        final Method setMobileDataEnabledMethod = mConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(false);

        setMobileDataEnabledMethod.invoke(mObject, disabled);
    }

   public static boolean isNetworkAvaliable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                // connected to wifi
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        return true;
        }
        else
        {
            // not connected to the internet
            return false;
        }
    }

}
