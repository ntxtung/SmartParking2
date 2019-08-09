package SmartParkingApp.Domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "vehicle", schema = "SmartParking", catalog = "")
public class Vehicle {
    private int id;
    private String vehicleName;
    private String plateNumber;
    private String attachRfid;
    private Timestamp registerTime;
    private int type;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "vehicle_name")
    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    @Basic
    @Column(name = "plate_number")
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Basic
    @Column(name = "attach_rfid")
    public String getAttachRfid() {
        return attachRfid;
    }

    public void setAttachRfid(String attachRfid) {
        this.attachRfid = attachRfid;
    }

    @Basic
    @Column(name = "register_time")
    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    @Basic
    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle that = (Vehicle) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(vehicleName, that.vehicleName) &&
                Objects.equals(plateNumber, that.plateNumber) &&
                Objects.equals(attachRfid, that.attachRfid) &&
                Objects.equals(registerTime, that.registerTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vehicleName, plateNumber, attachRfid, registerTime, type);
    }
}
