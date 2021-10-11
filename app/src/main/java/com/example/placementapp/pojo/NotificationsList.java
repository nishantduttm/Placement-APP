package com.example.placementapp.pojo;

import java.io.Serializable;
import java.util.List;

public class NotificationsList implements Serializable {
    private List<NotificationDto> notifications;

    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDto> notifications) {
        this.notifications = notifications;
    }
}
