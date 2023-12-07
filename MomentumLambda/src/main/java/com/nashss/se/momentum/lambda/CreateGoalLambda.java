package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.CreateGoalRequest;
import com.nashss.se.momentum.activity.results.CreateGoalResult;

public class CreateGoalLambda
        extends LambdaActivityRunner<CreateGoalRequest, CreateGoalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateGoalRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateGoalRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateGoalRequest unauthenticatedRequest = input.fromBody(CreateGoalRequest.class);
                    return input.fromUserClaims(claims ->
                            CreateGoalRequest.builder()
                                    .withGoalName(unauthenticatedRequest.getGoalName())
                                    .withUserId(claims.get("email"))
                                    .withStartDate(unauthenticatedRequest.getStartDate())
                                    .withGoalCritTarget(unauthenticatedRequest.getGoalCritTarget())
                                    .withGoalCritTimeperiod(unauthenticatedRequest.getGoalCritTimeperiod())
                                    .withGoalCritUnit(unauthenticatedRequest.getGoalCritUnit())
                                    .withGoalCritEffectiveDate(unauthenticatedRequest.getGoalCritEffectiveDate())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateGoalActivity().handleRequest(request)
        );
    }
}
