package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.GetAllGoalsSummaryRequest;
import com.nashss.se.momentum.activity.results.GetAllGoalsSummaryResult;

public class GetAllGoalsSummaryLambda
        extends LambdaActivityRunner<GetAllGoalsSummaryRequest, GetAllGoalsSummaryResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllGoalsSummaryRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllGoalsSummaryRequest> input, Context context) {
        GetAllGoalsSummaryRequest queryParamsRequest = input.fromQuery(query -> GetAllGoalsSummaryRequest.builder()
                    .withDate(query.get("date"))
                    .build()
        );
        return super.runActivity(
                () -> input.fromUserClaims(claims ->
                        GetAllGoalsSummaryRequest.builder()
                                .withUserId(claims.get("email"))
                                .withDate(queryParamsRequest.getDate())
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllGoalsSummaryActivity().handleRequest(request)
        );
    }
}
