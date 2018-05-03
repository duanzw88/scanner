package con.jcble.scan.listener;

import java.util.List;

import con.jcble.scan.device.JCBeaconDevice;
import con.jcble.scan.device.JCBleDevice;
import con.jcble.scan.device.JCBluetoothDevice;

/**
 * Created by duanzw on 2017/8/8.
 */

public interface JCBleScanResultListener
{
    void onBeaconScanResult(List<JCBeaconDevice> devices);
    void onBleScanResult(List<JCBluetoothDevice> devices);
}
