package con.jcble.scan.device;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by duanzw on 2017/8/8.
 */

public class JCBleDevice implements Serializable
{
    JCBeaconDevice beaconDevice = null;
    JCBluetoothDevice bluetoothDevice = null;

    public JCBleDevice()
    {
    }
    public JCBleDevice(String mac,String uuid,int major,int minor,int rssi,String sn,int power,String version)
    {
        beaconDevice = new JCBeaconDevice(mac,uuid,major,minor,rssi);
        bluetoothDevice = new JCBluetoothDevice(sn,power,version);
    }

    public void setBeaconDevice(JCBeaconDevice device)
    {
//        Log.e("scan","setBeacon.....");
        this.beaconDevice = device;
    }
    public void setBluetoothDevice(JCBluetoothDevice device)
    {
        this.bluetoothDevice = device;
    }

    public JCBeaconDevice getBeaconDevice()
    {
//        if(beaconDevice == null)
//        {
//            Log.e("scan","neaconDevice is still null");
//        }
        return beaconDevice;
    }
    public JCBluetoothDevice getBluetoothDevice()
    {
        return bluetoothDevice;
    }

}
