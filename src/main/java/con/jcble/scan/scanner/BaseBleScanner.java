package con.jcble.scan.scanner;

/**
 * Created by duanzw on 2017/8/7.
 */

public abstract class BaseBleScanner
{
    public boolean isScanning = false;
    public abstract void startBleScan();
    public abstract void stopBleScan();
}
