package con.jcble.scan.device;

import java.io.Serializable;

/**
 * Created by duanzw on 2017/8/7.
 */

public class JCBeaconDevice implements Serializable
{
    String mac = null;
    String uuid = null;
    int major = Integer.MAX_VALUE;
    int minor = Integer.MIN_VALUE;

    int rssi = 0;

    public JCBeaconDevice()
    {

    }
    public JCBeaconDevice(String mac,String uuid,int major,int minor,int rssi)
    {
        this.mac = mac;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;
    }

    public void setMac(String mac)
    {
        this.mac = mac;
    }
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
    public void setMajor(int major)
    {
        this.major = major;
    }
    public void setMinor(int minor)
    {
        this.minor = minor;
    }
    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    public String getMac()
    {
        return mac;
    }
    public String getUuid()
    {
        return uuid;
    }
    public int getMajor()
    {
        return major;
    }
    public int getMinor()
    {
        return minor;
    }
    public int getRssi()
    {
        return rssi;
    }

}
