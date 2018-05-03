package con.jcble.scan.scanner;

import android.content.Context;
import android.os.Build;

import con.jcble.scan.strategy.JCBLEScanCallback;
import con.jcble.scan.strategy.JellyBeanBleScanner;
import con.jcble.scan.strategy.LollipopBleScanner;

/**
 * Created by duanzw on 2017/8/7.
 */

public class BleScanner
{
    public BaseBleScanner scanner = null;

    public BleScanner(Context context,JCBLEScanCallback callback)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            scanner = new LollipopBleScanner(context,callback);
        }
        else
        {
            scanner = new JellyBeanBleScanner(context,callback);
        }
    }

    public boolean isScanning()
    {
        return scanner.isScanning;
    }
    public void startBleScan()
    {
        if(scanner != null && scanner.isScanning == false)
        {
            scanner.startBleScan();
        }
    }

    public void stopBleScan()
    {
        if(scanner != null && scanner.isScanning == true)
        {
            scanner.stopBleScan();
        }
    }
}
