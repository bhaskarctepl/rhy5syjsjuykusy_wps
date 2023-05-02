package com.hillspet.wearables.response;

import com.hillspet.wearables.dto.AssignedUser;

import java.util.List;

public class AssignedUserResponse {
    List<AssignedUser> assignedUserList;

    public List<AssignedUser> getAssignedUserList() {
        return assignedUserList;
    }

    public void setAssignedUserList(List<AssignedUser> assignedUserList) {
        this.assignedUserList = assignedUserList;
    }
}
