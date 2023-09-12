package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.DeleteGoalRequest;
import com.nashss.se.momentum.activity.results.DeleteGoalResult;


public class DeleteGoalLambda
        extends LambdaActivityRunner<DeleteGoalRequest, DeleteGoalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteGoalRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteGoalRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    DeleteGoalRequest unauthenticatedRequest = input.fromPath(path -> DeleteGoalRequest.builder()
                            .withGoalName(path.get("goalName"))
                            .build());

                    return input.fromUserClaims(claims ->
                            DeleteGoalRequest.builder()
                                    .withUserId(claims.get("email"))
                                    .withGoalName(unauthenticatedRequest.getGoalName())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteGoalActivity().handleRequest(request)
        );
    }
}