package com.ngomalalibo.stocktradingapp.util;

import com.mongodb.client.MongoCollection;
import com.ngomalalibo.stocktradingapp.repository.Connection;

public class GetCollectionFromEntityName
{
    public static MongoCollection getCollectionFromEntityName(String entityName)
    {
        
        return Connection.mapCollectionsAndEntities().get(entityName);
    }
}
