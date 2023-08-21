package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.CreatePlaylistRequest;
import com.nashss.se.musicplaylistservice.activity.results.CreatePlaylistResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreatePlaylistLambda
        extends LambdaActivityRunner<CreatePlaylistRequest, CreatePlaylistResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreatePlaylistRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreatePlaylistRequest> input, Context context) {
        return super.runActivity(
            () -> {
                CreatePlaylistRequest unauthenticatedRequest = input.fromBody(CreatePlaylistRequest.class);
                return input.fromUserClaims(claims ->
                        CreatePlaylistRequest.builder()
                                .withName(unauthenticatedRequest.getName())
                                .withTags(unauthenticatedRequest.getTags())
                                .withCustomerId(claims.get("email"))
                                .withCustomerName(claims.get("name"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreatePlaylistActivity().handleRequest(request)
        );
    }
}

