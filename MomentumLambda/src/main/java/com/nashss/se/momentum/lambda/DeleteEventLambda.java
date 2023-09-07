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
                () -> input.fromPath(path ->
                        DeleteEventRequest.builder()
                                .withGoalId(path.get("goalId"))
                                .withEventId(path.get("eventId"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteEventActivity().handleRequest(request)
        );
    }
}
