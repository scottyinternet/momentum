import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.requests.GetAllGoalsSummaryRequest;
import com.nashss.se.momentum.activity.requests.GetGoalDetailsRequest;
import com.nashss.se.momentum.activity.results.GetAllGoalsSummaryResult;
import com.nashss.se.momentum.lambda.AuthenticatedLambdaRequest;
import com.nashss.se.momentum.lambda.LambdaActivityRunner;
import com.nashss.se.momentum.lambda.LambdaResponse;

public class GetAllGoalsSummaryLambda
        extends LambdaActivityRunner<GetAllGoalsSummaryRequest, GetAllGoalsSummaryResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllGoalsSummaryRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllGoalsSummaryRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims ->
                        GetAllGoalsSummaryRequest.builder()
                                .withUserId(claims.get("email"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllGoalsSummaryActivity().handleRequest(request)
        );
    }
}
