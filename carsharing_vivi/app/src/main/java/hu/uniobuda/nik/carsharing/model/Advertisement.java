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

    private String uid;                     // owner user id
    private List<String> acceptedUids;      // who pressed accepted
    private String chosenUid;               // mutual
    private TravelMode mode;   // enum
    private long when;
    private String from;    // GPS coordinate
    private String to;
    private List<String> nodes;
    private Integer seats;     // ez a típus menthető közvetlenül a Firebase DB-be


    public Advertisement() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Advertisement(String uid, TravelMode mode, Date when, String from, String to, /*List<String> nodes,*/ Integer seats) {
        this.uid = uid;
        this.mode = mode;
        this.when = when.getTime();
        this.from = from;
        this.to = to;
        this.seats=seats;
    }
    public Advertisement(String uid, List<String> acceptedUids, String chosenUid, TravelMode mode, Date when, String from, String to, List<String> nodes, Integer seats) {
        this.uid = uid;
        this.acceptedUids = acceptedUids;
        this.chosenUid = chosenUid;
        this.mode = mode;
        this.when = when.getTime();
        this.from = from;
        this.to = to;
        this.nodes = nodes;
        this.seats = seats;
    }

    protected Advertisement(Parcel in) {
        uid = in.readString();
        acceptedUids = in.createStringArrayList();
        chosenUid = in.readString();
        mode = TravelMode.valueOf(in.readString());//TODO
        when = in.readLong();//TODO
        from = in.readString();
        to = in.readString();
        nodes = in.createStringArrayList();
        seats = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeStringList(acceptedUids);
        dest.writeString(chosenUid);
        dest.writeString(mode.name());//TODO
        dest.writeLong(when);//TODO
        dest.writeString(from);
        dest.writeString(to);
        dest.writeStringList(nodes);
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
