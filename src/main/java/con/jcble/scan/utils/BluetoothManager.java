package con.jcble.scan.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by duanzw on 17/1/10.
 */
public class BluetoothManager
{
    /**
     * 当前Android设备是否支持Bluetooth
     * @return true: 支持Bluetooth
     *         false: 不支持Bluetooth
     */
    public static boolean isBluetoothSupported()
    {
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }

    /**
     * 当前Android设备的bluetooth是否已经开启
     * @return  true: Bluetooth已经开启
     *          false: Bluetooth未开启
     */
    public static boolean isBluetoothEnable()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null)
        {
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    /**
     * 强制开启当前Android设备的Bluetooth
     * @return  true:   强制打开Bluetooth成功
     *          false:  强制打开Bluetooth失败
     */
    public static boolean turnOnBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null)
        {
            return bluetoothAdapter.enable();
        }

        return false;
    }

    public static boolean turnOffBluetooth()
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter != null)
        {
            return adapter.disable();
        }

        return false;
    }

    /**
     * 校验蓝牙权限
     */
    public static void checkBluetoothPermission(Context context,int result)
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        result);
            }
        }
    }
}
