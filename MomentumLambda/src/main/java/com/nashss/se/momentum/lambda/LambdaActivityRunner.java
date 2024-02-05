package com.nashss.se.momentum.lambda;

//import com.nashss.se.momentum.dependency.DaggerServiceComponent;
import com.nashss.se.momentum.dependency.DaggerServiceComponent;
import com.nashss.se.momentum.dependency.ServiceComponent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LambdaActivityRunner<TRequest, TResult> {
    private ServiceComponent service;
    private final Logger log = LogManager.getLogger();

    /**
     * Handles running the activity and returning a LambdaResponse (either success or failure).
     * @param requestSupplier Provides the activity request.
     * @param handleRequest Runs the activity and provides a response.
     * @return A LambdaResponse
     */
    protected LambdaResponse runActivity(
            Supplier<TRequest> requestSupplier,
            BiFunction<TRequest, ServiceComponent, TResult> handleRequest) {

        TRequest request;
        try {
            log.info("Attempting to build activity request object...");

            request = requestSupplier.get();

            log.info("Successfully built activity request object of type: {}.", request.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("ERROR! Unable to build activity request object!", e);
            return LambdaResponse.error(e);
        }

        try {
            log.info("Attempting to execute activity...");

            ServiceComponent serviceComponent = getService();
            TResult result = handleRequest.apply(request, serviceComponent);

            log.info("Successfully executed activity. Received result of type: {}.", result.getClass().getSimpleName());
            return LambdaResponse.success(result);
        } catch (Exception e) {
            log.error("ERROR! An exception occurred while executing activity!", e);
            return LambdaResponse.error(e);
        }
    }

    private ServiceComponent getService() {
        if (service == null) {
            service = DaggerServiceComponent.create();
        }
        return service;
    }
}
