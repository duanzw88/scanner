package con.jcble.scan.device;

import android.util.Log;

/**
 * Created by duanzw on 2017/8/8.
 */

public class ScanResultParse
{
    private static final int DATA_TYPE_SERVICE_DATA = 0x16;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xFF;

    public JCBleDevice bleDevice;

    public ScanResultParse(String mac,int rssi,byte[] scanRecord)
    {
        bleDevice = new JCBleDevice();
        parseFromBytes(mac,rssi,scanRecord);
    }

    public void parseFromBytes(String mac,int rssi,byte[] scanRecord)
    {
//        Log.e("scan","parseFromBytes")

        if(scanRecord == null)
        {
            return;
        }

        int currentPos = 0;
        while (currentPos < scanRecord.length)
        {
            int length = scanRecord[currentPos++] & 0xFF;
            if(length == 0)
            {
                break;
            }
            int dataLength = length - 1;
            int fieldType = scanRecord[currentPos++] & 0xFF;
//            Log.e("scan ","bytes length = " + length +" fileType = " + fieldType);
            switch (fieldType)
            {
                case DATA_TYPE_SERVICE_DATA:
                    //读取串号和电量
                    parseBleInfo(scanRecord,currentPos,dataLength);
                    break;
                case DATA_TYPE_MANUFACTURER_SPECIFIC_DATA:
                    //读取ibeacon信息
                    parseBeaconInfo(mac,rssi,scanRecord,currentPos,dataLength);
                    break;
            }

            currentPos += dataLength;
        }

    }

    private void parseBeaconInfo(String mac,int rssi,byte[] scanRecord,int start,int length)
    {
        byte[] beaconData = new byte[length];


        System.arraycopy(scanRecord,start,beaconData,0,length);


        if(!checkBeaconData(beaconData))
        {
            return;
        }
        JCBeaconDevice device = new JCBeaconDevice(mac,
                parseBeaconUUID(beaconData),
                parseBeaconMajor(beaconData),
                parseBeaconMinor(beaconData),
                rssi);


        bleDevice.setBeaconDevice(device);

//        StringBuffer info = new StringBuffer();
//        for(int i = 0; i < beaconData.length; i++)
//        {
//            info.append(String.format("%02x",beaconData[i]));
//        }
//        Log.e("scan ","beaconInfo bytes= " + info.toString());
    }

    private void parseBleInfo(byte[] scanRecord,int start,int length)
    {
        byte[] bleData = new byte[length];


        System.arraycopy(scanRecord,start,bleData,0,length);
        if(bleData.length != 11 && bleData[1] != 0x0a)
        {
            return;
        }
        if(!checkBleData(bleData))
        {
            return;
        }

//        Log.e("scan","bleDevice sn = " + parseSn(bleData));
        JCBluetoothDevice device = new JCBluetoothDevice(parseSn(bleData),
                parsePower(bleData),
                parseVersion(bleData));
        bleDevice.setBluetoothDevice(device);
//        StringBuffer info = new StringBuffer();
//        for(int i = 0; i < bleData.length; i++)
//        {
//            info.append(String.format("%02x",bleData[i]));
//        }
//        Log.e("scan ","bleInfo bytes= " + info.toString());
    }
    private String parseBeaconUUID(byte[] beaconData)
    {
        StringBuffer uuid = new StringBuffer();
        for(int i = 4; i < 20;i++)
        {
            uuid.append(String.format("%02x",beaconData[i]));
        }

//        Log.e("scan","uuid bytes = " + uuid.toString());
        return uuid.toString();
    }
    private int parseBeaconMajor(byte[] beaconData)
    {
        int major = ((int)beaconData[20] & 0xff) << 8 | ((int)beaconData[21] & 0xff);
//        Log.e("scan","major bytes = " + String.format("%x",major));
        return major;
    }
    private int parseBeaconMinor(byte[] beaconData)
    {
        int minor = ((int)beaconData[22] & 0xff) << 8 | ((int)beaconData[23] & 0xff);
//        Log.e("scan","minor bytes = " + String.format("%x",minor));
        return minor;
    }

    private String parseSn(byte[] bleData)
    {
        StringBuffer sn = new StringBuffer();
        sn.append(String.format("%02x",bleData[1]));
        sn.append(String.format("%02x",bleData[0]));
        for(int i = 2; i < 8;i++)
        {
            sn.append(String.format("%02x",bleData[i]));
        }

//        Log.e("scan","sn bytes = " + sn.toString());
        return sn.toString();
    }
    private int parsePower(byte[] bleData)
    {
//        if(bleData.length < 9)
//        {
//            return -1;
//        }
        int power = ((int)bleData[8]) & 0xff;

//        Log.e("scan","power bytes = " + String.format("%d",power) +"%");
        return power;
    }
    private String parseVersion(byte[] bleData)
    {
//        if(bleData.length < 11)
//        {
//            return null;
//        }

        int vb = (int)(bleData[10] & 0xf0) >> 4;
        int vl = (int)(bleData[10] & 0x0f);

        String version = String.format("%d.%d",vb,vl);

//        Log.e("scan","version bytes = " + version);
        return version;

    }
    private boolean checkBeaconData(byte[] beaconData)
    {
        byte flag1 = beaconData[0];
        byte flag2 = beaconData[1];
        byte flag3 = beaconData[2];
        byte flag4 = beaconData[3];

        if(flag1 == 0x4c && flag2 == 0x00 && flag3 ==  0x02 && flag4 == 0x15)
        {
            return true;
        }
        return  false;
    }
    private boolean checkBleData(byte[] bleData)
    {
        if(bleData[1] == 0x0a)
        {
            return true;
        }

        return  false;
    }
}
