package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.GetGoalDetailsRequest;
import com.nashss.se.momentum.activity.results.GetGoalDetailsResult;

public class GetGoalDetailsLambda
        extends LambdaActivityRunner<GetGoalDetailsRequest, GetGoalDetailsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetGoalDetailsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetGoalDetailsRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    GetGoalDetailsRequest unauthenticatedRequest = input.fromBody(GetGoalDetailsRequest.class);
                    return input.fromUserClaims(claims ->
                            GetGoalDetailsRequest.builder()
                                    .withGoalName(unauthenticatedRequest.getGoalName())
                                    .withUserId(claims.get("email"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideGetGoalDetailsActivity().handleRequest(request)
        );
    }
}
