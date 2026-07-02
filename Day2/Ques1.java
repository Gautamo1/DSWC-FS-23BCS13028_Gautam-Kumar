abstract class SmartDevice {
    protected String deviceId;
    protected String deviceName;

    SmartDevice(String id, String name) {
        deviceId = id;
        deviceName = name;
    }

    abstract void runDiagnostic();
}

interface BatteryOperated {
    int getBatteryLevel();
    void triggerRechargeAlert();
}

class SmartLight extends SmartDevice {

    SmartLight(String id, String name) {
        super(id, name);
    }

    void runDiagnostic() {
        System.out.println(deviceName + " Light Checked");
    }
}

class SmartCamera extends SmartDevice implements BatteryOperated {

    int battery;

    SmartCamera(String id, String name, int battery) {
        super(id, name);
        this.battery = battery;
    }

    void runDiagnostic() {
        System.out.println(deviceName + " Camera Checked");
    }

    public int getBatteryLevel() {
        return battery;
    }

    public void triggerRechargeAlert() {
        System.out.println(deviceName + " Battery Low");
    }
}

class SmartLock extends SmartDevice implements BatteryOperated {

    int battery;

    SmartLock(String id, String name, int battery) {
        super(id, name);
        this.battery = battery;
    }

    void runDiagnostic() {
        System.out.println(deviceName + " Lock Checked");
    }

    public int getBatteryLevel() {
        return battery;
    }

    public void triggerRechargeAlert() {
        System.out.println(deviceName + " Battery Low");
    }
}

class HomeHub {

    public void executeNightlyRoutine(SmartDevice[] devices) {

        for (int i = 0; i < devices.length; i++) {

            devices[i].runDiagnostic();

            if (devices[i] instanceof BatteryOperated) {

                BatteryOperated b = (BatteryOperated) devices[i];

                if (b.getBatteryLevel() < 20) {
                    b.triggerRechargeAlert();
                }
            }
        }
    }
}

public class Ques1 {

    public static void main(String[] args) {

        SmartDevice[] d = {
                new SmartLight("1", "Hall"),
                new SmartCamera("2", "Door", 15),
                new SmartLock("3", "Gate", 50)
        };

        HomeHub h = new HomeHub();
        h.executeNightlyRoutine(d);
    }
}