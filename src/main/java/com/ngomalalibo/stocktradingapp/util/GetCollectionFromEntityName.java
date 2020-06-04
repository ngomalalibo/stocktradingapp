package com.ngomalalibo.stocktradingapp.util;

import com.mongodb.client.MongoCollection;
import com.ngomalalibo.stocktradingapp.database.MongoConnectionImpl;

public class GetCollectionFromEntityName
{
    public static MongoCollection getCollectionFromEntityName(String entityName)
    {
        
        return new MongoConnectionImpl().mapCollectionsAndEntities().get(entityName);
    }
}
