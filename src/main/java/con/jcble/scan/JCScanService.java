package con.jcble.scan;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import con.jcble.scan.device.JCBeaconDevice;
import con.jcble.scan.device.JCBleDevice;
import con.jcble.scan.device.JCBluetoothDevice;
import con.jcble.scan.device.ScanResultParse;
import con.jcble.scan.scanner.BleScanner;
import con.jcble.scan.strategy.JCBLEScanCallback;
import con.jcble.scan.utils.Util;

/**
 * Created by duanzw on 2017/8/7.
 */

public class JCScanService extends Service implements JCBLEScanCallback
{
    private BleScanner scanner = null;

    private Timer timer;
    private TimerTask timerTask;

//    private Map<String,JCBleDevice> devicesMap = new HashMap<String, JCBleDevice>();

    private Map<String,JCBeaconDevice> beaconsMap = new HashMap<String, JCBeaconDevice>();
    private Map<String,JCBluetoothDevice> blesMap = new HashMap<String, JCBluetoothDevice>();

//    private List<JCBleScanResultListener> resultListeners = new ArrayList<JCBleScanResultListener>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        scanner = new BleScanner(this,this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(scanner != null && scanner.isScanning() == false)
        {
            startScan();
        }
        Log.e("scan","JCScanService startCommand....");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScan();
    }

    public void startScan()
    {
        if(scanner == null)
        {
            scanner = new BleScanner(this,this);
        }

        scanner.startBleScan();
        startTimer();
    }

    public void stopScan()
    {
        if(scanner != null)
        {
//            Log.e("scan","ScanStop....");
            scanner.stopBleScan();
        }
        stopTimer();
    }


    private void startTimer()
    {
        if(timerTask == null)
        {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    stopScan();
                    reportDevice();

                    //清空数据
                    beaconsMap.clear();
                    blesMap.clear();
//                    devicesMap.clear();

                    startScan();
                }
            };
        }

        if(timer == null)
        {
            timer = new Timer();
            if(timerTask != null)
            {
                timer.schedule(timerTask,1000);
            }
        }
    }
    private void stopTimer()

    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
        if(timerTask != null)
        {
            timerTask.cancel();
            timerTask = null;
        }
    }
    @Override
    public void onBleScanGetDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {


//            if(device.getAddress().equals("C9:AE:D9:A9:48:5B"))
//            {
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < scanRecord.length; i++)
                {
                    sb.append(String.format("%02x ",scanRecord[i]));
                }
//                Log.e("scan","ScanResult bytes:" + sb.toString() + " name = " + device.getName() + "  rssi = " + rssi);
                ScanResultParse parse = new ScanResultParse(device.getAddress(),rssi,scanRecord);
//                Log.e("scan","bytes length = " + devicesMap.size());
                addDevice(parse.bleDevice);
//            }
    }


    private void addDevice(JCBleDevice device)
    {
        JCBluetoothDevice bleDevice = device.getBluetoothDevice();
//        if(bleDevice == null || !bleDevice.getSn().startsWith("0a04"))
//        {
//            return;
//        }
        if(bleDevice != null)
        {
            blesMap.put(bleDevice.getSn(),bleDevice);
        }

        JCBeaconDevice beacon = device.getBeaconDevice();

        if(beacon != null)
        {
            if(beacon.getRssi() > -85)
            {
                beaconsMap.put(beacon.getMac(),beacon);
            }

//            Log.e("scan","beacons length = " + beaconsMap.size());
        }


    }

    private void reportDevice()
    {
        ArrayList<JCBeaconDevice> beacons = new ArrayList<JCBeaconDevice>();
        Collection<JCBeaconDevice> values = beaconsMap.values();
        for (JCBeaconDevice beacon : values)
        {
            beacons.add(beacon);
        }

        ArrayList<JCBluetoothDevice> bles = new ArrayList<JCBluetoothDevice>();
        Collection<JCBluetoothDevice> bleValues = blesMap.values();
        for(JCBluetoothDevice ble : bleValues)
        {
            bles.add(ble);
        }

        beaconsMap.clear();
        blesMap.clear();


        Log.e("scan","result: " + " Beacon length = " + beacons.size() + " Ble length = " + bles.size());
//        if(resultListeners != null)
//        {
//            for(int i = 0; i < resultListeners.size(); i++)
//            {
//                JCBleScanResultListener listener = resultListeners.get(i);
//                listener.onBeaconScanResult(beacons);
//                listener.onBleScanResult(bles);
//            }
//        }

        Intent intent = new Intent();
        intent.setAction(Util.SCAN_RESULT_ACTION);
        intent.putExtra("beacons",(Serializable) beacons);
        intent.putExtra("bles",(Serializable)bles);
        this.sendBroadcast(intent);
    }
}
