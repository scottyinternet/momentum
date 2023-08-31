package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.CreateEventRequest;
import com.nashss.se.momentum.activity.results.CreateEventResult;

public class CreateEventLambda
        extends LambdaActivityRunner<CreateEventRequest, CreateEventResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateEventRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateEventRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateEventRequest unauthenticatedRequest = input.fromBody(CreateEventRequest.class);
                    return input.fromUserClaims(claims ->
                            CreateEventRequest.builder()
                                    .withGoalID(unauthenticatedRequest.getGoalId())
                                    .withDateOfEvent(unauthenticatedRequest.getDateOfEvent())
                                    .withMeasurement(unauthenticatedRequest.getMeasurement())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateEventActivity().handleRequest(request)
        );
    }
}
