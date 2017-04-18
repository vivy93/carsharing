package hu.uniobuda.nik.carsharing;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class Add implements Parcelable{
    private String from,to;
    private Date when;
    private int freeSeats;

    protected Add(Parcel in) {
        from = in.readString();
        to = in.readString();
        freeSeats = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
        dest.writeInt(freeSeats);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Add> CREATOR = new Creator<Add>() {
        @Override
        public Add createFromParcel(Parcel in) {
            return new Add(in);
        }

        @Override
        public Add[] newArray(int size) {
            return new Add[size];
        }
    };

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Date getWhen() {
        return when;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public Add(String from, String to, Date when, int freeSeats) {
        this.from = from;
        this.to = to;
        this.when = when;
        this.freeSeats = freeSeats;
    }
}
