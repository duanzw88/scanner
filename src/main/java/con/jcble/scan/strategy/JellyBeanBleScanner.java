package con.jcble.scan.strategy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import con.jcble.scan.scanner.BaseBleScanner;

/**
 * Created by duanzw on 2017/8/7.
 */

public class JellyBeanBleScanner extends BaseBleScanner
{
    private BluetoothAdapter bluetoothAdapter = null;
    private JCBLEScanCallback scanCallback = null;

    public JellyBeanBleScanner(Context context, JCBLEScanCallback callback)
    {
        scanCallback = callback;

        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter  = manager.getAdapter();
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {

            if(scanCallback != null)
            {
                scanCallback.onBleScanGetDevice(bluetoothDevice,rssi,scanRecord);
            }

        }
    };
    @Override
    public void startBleScan() {

        if(bluetoothAdapter != null)
        {
            isScanning = bluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    @Override
    public void stopBleScan()
    {
        if(bluetoothAdapter != null && leScanCallback != null)
        {
            bluetoothAdapter.stopLeScan(leScanCallback);
            isScanning = false;
        }
    }
}
