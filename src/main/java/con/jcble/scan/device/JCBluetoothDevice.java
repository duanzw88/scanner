package con.jcble.scan.device;

import java.io.Serializable;

/**
 * Created by duanzw on 2017/8/7.
 */

public class JCBluetoothDevice implements Serializable
{
    String sn = null;
    int power = Integer.MAX_VALUE;
    String version = null;

    public JCBluetoothDevice()
    {

    }
    public JCBluetoothDevice(String sn,int power,String version)
    {
        this.sn = sn;
        this.power = power;
        this.version = version;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }
    public void setPower(int power)
    {
        this.power = power;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getSn()
    {
        return sn;
    }
    public int getPower()
    {
        return power;
    }
    public String getVersion()
    {
        return version;
    }
}
