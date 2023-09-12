package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

<<<<<<< HEAD
=======
import java.util.Objects;

>>>>>>> main
public class GoalSummary {

    private String goalName;

<<<<<<< HEAD
    private StatusEnum goalStatus;
=======
    private String goalStatus;
>>>>>>> main

    public String getGoalName() {
        return goalName;
    }

<<<<<<< HEAD
    public StatusEnum getGoalStatus() {
=======
    public String getGoalStatus() {
>>>>>>> main
        return goalStatus;
    }

    public GoalSummary(String goalName, StatusEnum goalStatus) {
        this.goalName = goalName;
<<<<<<< HEAD
        this.goalStatus = goalStatus;
=======
        this.goalStatus = goalStatus.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalSummary that = (GoalSummary) o;
        return Objects.equals(getGoalName(), that.getGoalName()) && Objects.equals(getGoalStatus(), that.getGoalStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGoalName(), getGoalStatus());
>>>>>>> main
    }
}
