package SmartParkingApp.Infrastructure.SoftwareSerial;
// https://sourceforge.net/projects/javaarduinolibrary/files/

import com.fazecast.jSerialComm.SerialPort;

public class JSerialMainTest {
    private static JSerial js3 = new JSerial("COM8", 9600);

    public static void main(String[] agrs) {
        System.out.println(js3);
        SerialPort[] portNames = SerialPort.getCommPorts();
        System.out.println("All available port: ");
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i].getSystemPortName());
        }
        System.out.println("Try to open port: ");

        if (js3.openConnection()) {
            System.out.println("Open Successful");
            while (true) {
                String s = js3.serialRead();
                if (s.length() > 0)
                    System.out.println(s);
            }
        } else {
            System.out.println("Open Failed");
        }
    }
}
