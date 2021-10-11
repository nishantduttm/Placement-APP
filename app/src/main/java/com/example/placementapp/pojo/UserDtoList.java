package com.example.placementapp.pojo;

import java.io.Serializable;
import java.util.List;

public class UserDtoList implements Serializable {
    private List<UserDto> users;

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
