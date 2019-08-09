package SmartParkingApp.Domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "vehicle_type", schema = "SmartParking", catalog = "")
public class VehicleType {
    private int id;
    private String typeName;
    private int costPerHourDay;
    private int costPerHourNight;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "cost_per_hour_day")
    public int getCostPerHourDay() {
        return costPerHourDay;
    }

    public void setCostPerHourDay(int costPerHourDay) {
        this.costPerHourDay = costPerHourDay;
    }

    @Basic
    @Column(name = "cost_per_hour_night")
    public int getCostPerHourNight() {
        return costPerHourNight;
    }

    public void setCostPerHourNight(int costPerHourNight) {
        this.costPerHourNight = costPerHourNight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleType that = (VehicleType) o;
        return id == that.id &&
                costPerHourDay == that.costPerHourDay &&
                costPerHourNight == that.costPerHourNight &&
                Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName, costPerHourDay, costPerHourNight);
    }
}
