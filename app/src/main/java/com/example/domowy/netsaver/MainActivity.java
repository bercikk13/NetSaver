package com.example.domowy.netsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    PowerConnectionReceiver PowerStatusReceiver = new PowerConnectionReceiver();


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /////


        // register our power status receivers
        IntentFilter powerConnectedFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(PowerStatusReceiver, powerConnectedFilter);

        IntentFilter powerDisconnectedFilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(PowerStatusReceiver, powerDisconnectedFilter);

        /////
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            int scale = -1;
            int level = -1;
            int voltage = -1;
            int temp = -1;
            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                Log.e("BatteryManager", "level is "+level+"/"+scale+", temp is "+temp+", voltage is "+voltage);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        Context context = getApplicationContext();
        // checking if phone is chargnig if yes then
        if(PowerStatusReceiver.equals(powerConnectedFilter))
        {

            //checking if phone have conection with network if yes then
            if(isNetworkAvaliable(context))
            {

                Toast.makeText(context, "Do you want to close internet connection?", Toast.LENGTH_LONG).show();



            }
            else
            {
                Toast.makeText(context, "Send Sleep cuz net is not avaliable", Toast.LENGTH_LONG).show();
            }
        }

        else
        {
            Toast.makeText(context, "Send Sleep cuz itr isnt charging", Toast.LENGTH_LONG).show();
        }


    }
    public class PowerConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        }
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
