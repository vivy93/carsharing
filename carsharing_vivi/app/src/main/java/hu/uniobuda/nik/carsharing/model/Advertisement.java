package hu.uniobuda.nik.carsharing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

/**
 * Created by pat on 2017.03.27..
 */

@IgnoreExtraProperties
public class Advertisement implements Parcelable {

    // private String uid;                     // owner user id
    private List<String> acceptedUids;      // who pressed accepted
    private String chosenUid;               // mutual

    private TravelMode mode;
    private long when;
    private String from;
    private String to;
    private String node1;
    private String node2;
    private Integer seats;


    public Advertisement() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Advertisement(TravelMode mode, Date when, String from, String to, String node1, String node2, Integer seats) {
        // this.uid = uid;
        this.mode = mode;
        this.when = when.getTime();
        this.from = from;
        this.to = to;
        this.node1 = node1;
        this.node2 = node2;
        this.seats=seats;
    }


    protected Advertisement(Parcel in) {
        // uid = in.readString();
        acceptedUids = in.createStringArrayList();
        chosenUid = in.readString();
        mode = TravelMode.valueOf(in.readString());//TODO
        when = in.readLong();//TODO
        from = in.readString();
        to = in.readString();
        node1 = in.readString();
        node2 = in.readString();
        seats = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeString(uid);
        dest.writeStringList(acceptedUids);
        dest.writeString(chosenUid);
        dest.writeString(mode.name());//TODO
        dest.writeLong(when);//TODO
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(node1);
        dest.writeString(node2);
        dest.writeInt(seats);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel in) {
            return new Advertisement(in);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };

    /*
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    */

    public List<String> getAcceptedUids() {
        return acceptedUids;
    }

    public void setAcceptedUids(List<String> acceptedUids) {
        this.acceptedUids = acceptedUids;
    }

    public String getChosenUid() {
        return chosenUid;
    }

    public void setChosenUid(String chosenUid) {
        this.chosenUid = chosenUid;
    }

    public TravelMode getMode() {
        return mode;
    }

    public void setMode(TravelMode mode) {
        this.mode = mode;
    }

    public Date getWhen() {
        return new Date(when);
    }

    public void setWhen(Date when) {
        this.when = when.getTime();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNode1() {
        return node1;
    }

    public void setNode1(String node1) {
        this.node1 = node1;
    }

    public String getNode2() {
        return node2;
    }

    public void setNode2(String node2) {
        this.node2 = node2;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
