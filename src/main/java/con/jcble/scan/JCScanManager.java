package con.jcble.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import con.jcble.scan.device.JCBeaconDevice;
import con.jcble.scan.device.JCBluetoothDevice;
import con.jcble.scan.listener.JCBleScanResultListener;
import con.jcble.scan.utils.Util;

/**
 * Created by duanzw on 2017/8/7.
 */

public class JCScanManager
{
    private Context _context = null;
    private static JCScanManager instance = null;
    protected boolean isScanning = false;

    private List<JCBleScanResultListener> listeners = new ArrayList<JCBleScanResultListener>();

    private IntentFilter filter;

    private JCScanManager(Context context)
    {
        _context = context;
        filter = new IntentFilter();
        filter.addAction(Util.SCAN_RESULT_ACTION);
    }
    public static JCScanManager getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new JCScanManager(context);
        }
        return instance;
    }

    public void onResume()
    {
        _context.registerReceiver(receiver,filter);
    }
    public void onPause()
    {
        _context.unregisterReceiver(receiver);
    }
    public void addListener(JCBleScanResultListener listener)
    {
        if(listener != null)
        {
            listeners.add(listener);
        }
    }
    public void startScan()
    {
        Intent intent = new Intent(_context,JCScanService.class);
        _context.startService(intent);
    }
    public void stopScan()
    {
        Intent intent = new Intent(_context,JCScanService.class);
        _context.stopService(intent);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<JCBeaconDevice> beacons = (List<JCBeaconDevice>)intent.getSerializableExtra("beacons");
            List<JCBluetoothDevice> bles = (List<JCBluetoothDevice>)intent.getSerializableExtra("bles");
//            if(beacons != null)
//            {
//                Log.e("scan","beacons = " + beacons.size());
//            }
//            if(bles != null)
//            {
//                Log.e("scan","bles = " + bles.size());
//                for(int i = 0; i < bles.size(); i++)
//                {
//                    JCBluetoothDevice device = bles.get(i);
//                    Log.e("scan","SN:" + device.getSn() + " Power:" + device.getPower() + "%");
//                }
//            }

            if(listeners.size() > 0)
            {
                for(JCBleScanResultListener listener: listeners)
                {
                    listener.onBeaconScanResult(beacons);
                    listener.onBleScanResult(bles);
                }
            }

        }
    };
}
