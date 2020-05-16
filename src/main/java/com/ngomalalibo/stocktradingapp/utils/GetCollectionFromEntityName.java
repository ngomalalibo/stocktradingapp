package com.ngomalalibo.stocktradingapp.utils;

import com.mongodb.client.MongoCollection;
import com.ngomalalibo.stocktradingapp.dataService.Connection;

public class GetCollectionFromEntityName
{
    public static MongoCollection getCollectionFromEntityName(String entityName)
    {
        
        return Connection.mapCollectionsAndEntities().get(entityName);
    }
}
