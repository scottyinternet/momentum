package com.nashss.se.momentum.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.DeleteEventRequest;
import com.nashss.se.momentum.activity.results.DeleteEventResult;


public class DeleteEventLambda
        extends LambdaActivityRunner<DeleteEventRequest, DeleteEventResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteEventRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteEventRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    DeleteEventRequest unauthenticatedRequest = input.fromPath(path -> DeleteEventRequest.builder()
                            .withGoalId("goalId")
                            .withEventId("eventId")
                            .build());

                    return input.fromUserClaims(claims ->
                            DeleteEventRequest.builder()
                                    .withGoalId(unauthenticatedRequest.getGoalId())
                                    .withEventId(unauthenticatedRequest.getEventId())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteEventActivity().handleRequest(request)
        );
    }
}
