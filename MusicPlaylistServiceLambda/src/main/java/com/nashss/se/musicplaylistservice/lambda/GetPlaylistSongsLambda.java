package com.nashss.se.musicplaylistservice.lambda;

import com.nashss.se.musicplaylistservice.activity.requests.GetPlaylistSongsRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetPlaylistSongsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPlaylistSongsLambda
        extends LambdaActivityRunner<GetPlaylistSongsRequest, GetPlaylistSongsResult>
        implements RequestHandler<LambdaRequest<GetPlaylistSongsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetPlaylistSongsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPathAndQuery((path, query) ->
                    GetPlaylistSongsRequest.builder()
                            .withId(path.get("id"))
                            .withOrder(query.get("order"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetPlaylistSongsActivity().handleRequest(request)
        );
    }
}

