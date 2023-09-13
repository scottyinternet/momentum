package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.UpdateGoalRequest;
import com.nashss.se.momentum.activity.results.UpdateGoalResult;

public class UpdateGoalLambda extends LambdaActivityRunner<UpdateGoalRequest, UpdateGoalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateGoalRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateGoalRequest> input, Context context) {
        return super.runActivity(
                () -> {UpdateGoalRequest unauthenticatedRequest = input.fromBody(UpdateGoalRequest.class);
                    return input.fromUserClaims(claims ->
                            UpdateGoalRequest.builder()
                                    .withGoalName(unauthenticatedRequest.getGoalName())
                                    .withUserId(claims.get("email"))
                                    .build());
    },(request, serviceComponent) ->
                        serviceComponent.provideUpdateGoalActivity().handleRequest(request));
    }
}
