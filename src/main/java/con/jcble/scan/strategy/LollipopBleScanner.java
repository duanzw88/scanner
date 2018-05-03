package con.jcble.scan.strategy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;

import con.jcble.scan.scanner.BaseBleScanner;

/**
 * Created by duanzw on 2017/8/7.
 */

public class LollipopBleScanner extends BaseBleScanner {

    private BluetoothAdapter    bluetoothAdapter = null;
    private BluetoothLeScanner  bluetoothLeScanner = null;
    private JCBLEScanCallback   jcScanCallback = null;

    public LollipopBleScanner(Context context,JCBLEScanCallback callback)
    {
        jcScanCallback = callback;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null)
        {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {
            if(bytes != null && bytes.length >0 && jcScanCallback != null)
            {
                if(jcScanCallback != null)
                {
                    jcScanCallback.onBleScanGetDevice(bluetoothDevice,rssi,bytes);
                }
            }
        }
    };
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);


            if(result.getScanRecord() != null && jcScanCallback != null)
            {
                jcScanCallback.onBleScanGetDevice(result.getDevice(),result.getRssi(),result.getScanRecord().getBytes());
            }
//            BluetoothDevice device = result.getDevice();
            ScanRecord record = result.getScanRecord();
            byte[] data = record.getBytes();
//
//            Log.e("scan","rssi = " + result.getRssi());
//
//            if(device.getAddress().equals("C9:AE:D9:A9:48:5B"))
//            {
//                StringBuffer sb = new StringBuffer();
//                for(int i = 0; i < data.length; i++)
//                {
//                    sb.append(String.format("%02x ",data[i]));
//                }
//                Log.e("scan","ScanResult bytes:" + sb.toString() + "  rssi = " + result.getRssi());
//                ScanResultParse parse = new ScanResultParse(data);
//            }


        }
    };

    @Override
    public void startBleScan() {
        if(bluetoothLeScanner != null &&
                bluetoothAdapter != null &&
                bluetoothAdapter.isEnabled())
        {
//            Log.e("scan","LollipopBleScanner start....");

//            ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
//            bluetoothLeScanner.startScan(null,scanSettings,scanCallback);

            bluetoothAdapter.startLeScan(leScanCallback);
            isScanning = true;
        }
    }

    @Override
    public void stopBleScan() {
        if(bluetoothLeScanner != null &&
                bluetoothAdapter != null &&
                bluetoothAdapter.isEnabled())
        {
            bluetoothLeScanner.stopScan(scanCallback);
            isScanning = false;
        }
    }
}
