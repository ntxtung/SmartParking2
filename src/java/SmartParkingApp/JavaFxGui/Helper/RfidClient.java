package SmartParkingApp.JavaFxGui.Helper;

import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;
import java.util.*;

public class RfidClient {
    //region Properties
    private volatile Boolean connectionStatus = false;
    private volatile Boolean cancelled = false;
    private volatile SerialPort comPort;
    private volatile String portDescription;
    private volatile int baudRate = 9600;
    //endregion

    //region Constructor
    public RfidClient(String portDescription, int baudRate) {
        this.portDescription = portDescription;
        this.baudRate = baudRate;

        comPort = SerialPort.getCommPort(this.portDescription);
        comPort.setBaudRate(this.baudRate);
    }

    public RfidClient() {
    }
    //endregion

    //region Methods
//    Not usable
//    public SerialPort[] getAvailablePortList() {
//        return SerialPort.getCommPorts();
//    }

    public boolean openConnection() {
        System.out.println(this.getClass().getName() + " >> Get into open connection");
        this.comPort = SerialPort.getCommPort(this.portDescription);
        if (this.comPort.openPort()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.connectionStatus = true;
        } else {
            this.connectionStatus = false;
        }
        System.out.println(this.getClass().getName() + " >> Go out of open connection");
        return this.connectionStatus;
    }

    public boolean closeConnection() {
        return this.comPort.closePort();
    }

    public String serialRead() {
        //will be an infinite loop if incoming data is not bound
//        this.comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        String out = "";
        Scanner in = new Scanner(this.comPort.getInputStream());
        try {
            while (in.hasNext()){
                out += (in.next() + " ");
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public void serialWrite(String s) {
        //writes the entire string at once.
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        try {
            Thread.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        pout.print(s);
        pout.flush();
    }

    //region Getter - Setter

    public SerialPort getComPort() {
        return comPort;
    }

    public void setComPort(SerialPort comPort) {
        this.comPort = comPort;
    }

    public String getPortDescription() {
        return portDescription;
    }

    public void setPortDescription(String portDescription) {
        this.comPort = SerialPort.getCommPort(portDescription);
        this.portDescription = portDescription;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public Boolean getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    //endregion
    //region POJO
    @Override
    public String toString() {
        return "RFID_Client | PORT: " + this.portDescription + " | BaudRate: " + this.baudRate;
    }
    //endregion
    //endregion
}
