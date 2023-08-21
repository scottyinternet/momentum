package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.UpdatePlaylistRequest;
import com.nashss.se.musicplaylistservice.activity.results.UpdatePlaylistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdatePlaylistLambda
        extends LambdaActivityRunner<UpdatePlaylistRequest, UpdatePlaylistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdatePlaylistRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdatePlaylistRequest> input, Context context) {
        return super.runActivity(
            () -> {
                UpdatePlaylistRequest unauthenticatedRequest = input.fromBody(UpdatePlaylistRequest.class);
                return input.fromUserClaims(claims ->
                        UpdatePlaylistRequest.builder()
                                .withId(unauthenticatedRequest.getId())
                                .withName(unauthenticatedRequest.getName())
                                .withCustomerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdatePlaylistActivity().handleRequest(request)
        );
    }
}

