package com.nashss.se.momentum.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.nashss.se.momentum.utils.UnitOfMeasurement;

public class UnitOfMeasurementConverter  implements DynamoDBTypeConverter<String, UnitOfMeasurement> {
    @Override
    public String convert(UnitOfMeasurement unitOfMeasurement) {
        // Convert UnitOfMeasurement to its String representation.
        // For example, you might convert it to a unique identifier.
        return unitOfMeasurement.toString(); // You may need to adjust this part.
    }

    @Override
    public UnitOfMeasurement unconvert(String stringValue) {
        // Convert the String representation back to UnitOfMeasurement.
        // Parse the unique identifier and return the corresponding UnitOfMeasurement.
        return UnitOfMeasurement.valueOf(stringValue); // You may need to adjust this part.
    }

}
