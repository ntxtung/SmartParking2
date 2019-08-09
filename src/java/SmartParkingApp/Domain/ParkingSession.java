package SmartParkingApp.Domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "parking_session", schema = "SmartParking", catalog = "")
public class ParkingSession {
    private int id;
    private Timestamp inTime;
    private Timestamp outTime;
    private Integer emotionScore;
    private String frontImgIn;
    private String backImgIn;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "in_time")
    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    @Basic
    @Column(name = "out_time")
    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
    }

    @Basic
    @Column(name = "emotion_score")
    public Integer getEmotionScore() {
        return emotionScore;
    }

    public void setEmotionScore(Integer emotionScore) {
        this.emotionScore = emotionScore;
    }

    @Basic
    @Column(name = "front_img_in")
    public String getFrontImgIn() {
        return frontImgIn;
    }

    public void setFrontImgIn(String frontImgIn) {
        this.frontImgIn = frontImgIn;
    }

    @Basic
    @Column(name = "back_img_in")
    public String getBackImgIn() {
        return backImgIn;
    }

    public void setBackImgIn(String backImgIn) {
        this.backImgIn = backImgIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSession that = (ParkingSession) o;
        return id == that.id &&
                Objects.equals(inTime, that.inTime) &&
                Objects.equals(outTime, that.outTime) &&
                Objects.equals(emotionScore, that.emotionScore) &&
                Objects.equals(frontImgIn, that.frontImgIn) &&
                Objects.equals(backImgIn, that.backImgIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inTime, outTime, emotionScore, frontImgIn, backImgIn);
    }
}
