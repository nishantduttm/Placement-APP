package com.example.placementapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class FormStatus implements Parcelable {
    public String processRound;
    public String processDate;


    protected FormStatus(Parcel in) {
        processRound = in.readString();
        processDate = in.readString();
    }

    public static final Creator<FormStatus> CREATOR = new Creator<FormStatus>() {
        @Override
        public FormStatus createFromParcel(Parcel in) {
            return new FormStatus(in);
        }

        @Override
        public FormStatus[] newArray(int size) {
            return new FormStatus[size];
        }
    };

    public String getProcessRound() {
        return processRound;
    }

    public void setProcessRound(String processRound) {
        this.processRound = processRound;
    }


    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public FormStatus() {
    }

    public FormStatus(String processRound, String processDate) {
        this.processRound = processRound;
        this.processDate = processDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof FormStatus))
            return false;
        FormStatus second = (FormStatus) o;
        return this.getProcessRound().equals(second.getProcessRound());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(processRound);
        dest.writeString(processDate);
    }
}
