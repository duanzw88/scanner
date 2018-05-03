package con.jcble.scan.strategy;

import android.bluetooth.BluetoothDevice;

/**
 * Created by duanzw on 2017/8/7.
 */

public interface JCBLEScanCallback
{
    void onBleScanGetDevice(BluetoothDevice device,int rssi,byte[] scanRecord);
}
