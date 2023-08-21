package com.nashss.se.musicplaylistservice.metrics;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import javax.inject.Inject;

/**
 * Contains operations for publishing metrics.
 */
public class MetricsPublisher {

    private AmazonCloudWatch cloudWatch;

    /**
     * Creates a metrics publisher with the given AmazonCloudWatch.
     * @param cloudWatch AmazonCloudWatch
     */
    @Inject
    public MetricsPublisher(final AmazonCloudWatch cloudWatch) {
        this.cloudWatch = cloudWatch;
    }

    /**
     * Publishes a count to CloudWatch.
     *
     * @param metricName name of metric to publish.
     * @param value value of metric.
     */
    public void addCount(final String metricName, final double value) {
        addMetric(metricName, value, StandardUnit.Count);
    }

    /**
     * Publishes a count to CloudWatch.
     *
     * @param metricName name of metric to publish.
     * @param value value of metric (in milliseconds).
     */
    public void addTime(final String metricName, final double value) {
        addMetric(metricName, value, StandardUnit.Milliseconds);
    }

    /**
     * Publishes the given metric to CloudWatch.
     *
     * @param metricName name of metric to publish.
     * @param value value of metric.
     * @param unit unit of metric.
     */
    public void addMetric(final String metricName, final double value, final StandardUnit unit) {
        final PutMetricDataRequest request = buildMetricDataRequest(metricName, value, unit);
        cloudWatch.putMetricData(request);
    }

    /**
     * Helper method that builds the PutMetricDataRequest object used to publish to CloudWatch.
     *
     * @param metricName name of metric
     * @param value value of metric
     * @param unit unit of metric.
     * @return PutMetricDataRequest
     */
    private PutMetricDataRequest buildMetricDataRequest(final String metricName, final double value,
                                                        final StandardUnit unit) {

        final Dimension service = new Dimension()
            .withName(MetricsConstants.SERVICE)
            .withValue(MetricsConstants.SERVICE_NAME);

        final MetricDatum datum = new MetricDatum()
            .withMetricName(metricName)
            .withUnit(unit)
            .withValue(value)
            .withDimensions(service);

        final PutMetricDataRequest request = new PutMetricDataRequest()
            .withNamespace(MetricsConstants.NAMESPACE_NAME)
            .withMetricData(datum);

        return request;
    }
}
