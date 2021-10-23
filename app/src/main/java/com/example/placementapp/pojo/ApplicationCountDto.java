package com.example.placementapp.pojo;

import java.io.Serializable;

public class ApplicationCountDto implements Serializable {
    private int onHoldCount;
    private int inProcessCount;
    private int placedCount;
    private int notPlacedCount;

    public ApplicationCountDto() {
    }

    public int getOnHoldCount() {
        return onHoldCount;
    }

    public void setOnHoldCount(int onHoldCount) {
        this.onHoldCount = onHoldCount;
    }

    public int getInProcessCount() {
        return inProcessCount;
    }

    public void setInProcessCount(int inProcessCount) {
        this.inProcessCount = inProcessCount;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApplicationCountDto{");
        sb.append("onHoldCount=").append(onHoldCount);
        sb.append(", inProcessCount=").append(inProcessCount);
        sb.append(", placedCount=").append(placedCount);
        sb.append(", notPlacedCount=").append(notPlacedCount);
        sb.append('}');
        return sb.toString();
    }
}
