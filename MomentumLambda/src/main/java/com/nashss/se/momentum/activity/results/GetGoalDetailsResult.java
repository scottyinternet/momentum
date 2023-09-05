package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.PlaylistModel;

public class GetGoalDetailsResult {

    private GetGoalDetailsResult(){

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public GetGoalDetailsResult build() {
            return new GetGoalDetailsResult();
        }
    }
}
