package com.example.placementapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Statistics implements Parcelable {
    private int placedCount = 0;
    private int notPlacedCount = 0;

    @Override
    public String toString() {
        return "Statistics{" +
                "placedCount=" + placedCount +
                ", notPlacedCount=" + notPlacedCount +
                ", inProcessCount=" + inProcessCount +
                ", onHoldCount=" + onHoldCount +
                '}';
    }

    private int inProcessCount = 0;
    private int onHoldCount = 0;

    public Statistics(int placedCount, int notPlacedCount, int inProcessCount, int onHoldCount) {
        this.placedCount = placedCount;
        this.notPlacedCount = notPlacedCount;
        this.inProcessCount = inProcessCount;
        this.onHoldCount = onHoldCount;
    }

    public Statistics() {
    }

    protected Statistics(Parcel in) {
        placedCount = in.readInt();
        notPlacedCount = in.readInt();
        inProcessCount = in.readInt();
        onHoldCount = in.readInt();
    }

    public static final Creator<Statistics> CREATOR = new Creator<Statistics>() {
        @Override
        public Statistics createFromParcel(Parcel in) {
            return new Statistics(in);
        }

        @Override
        public Statistics[] newArray(int size) {
            return new Statistics[size];
        }
    };

    public int getPlacedCount() {
        return placedCount;
    }

    public void setPlacedCount(int placedCount) {
        this.placedCount = placedCount;
    }

    public int getNotPlacedCount() {
        return notPlacedCount;
    }

    public void setNotPlacedCount(int notPlacedCount) {
        this.notPlacedCount = notPlacedCount;
    }

    public int getInProcessCount() {
        return inProcessCount;
    }

    public void setInProcessCount(int inProcessCount) {
        this.inProcessCount = inProcessCount;
    }

    public int getOnHoldCount() {
        return onHoldCount;
    }

    public void setOnHoldCount(int onHoldCount) {
        this.onHoldCount = onHoldCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getPlacedCount());
        parcel.writeInt(getNotPlacedCount());
        parcel.writeInt(getInProcessCount());
        parcel.writeInt(getOnHoldCount());
    }
}
