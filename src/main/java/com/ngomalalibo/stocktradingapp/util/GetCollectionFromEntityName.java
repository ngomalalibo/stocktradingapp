package com.ngomalalibo.stocktradingapp.util;

import com.mongodb.client.MongoCollection;
import com.ngomalalibo.stocktradingapp.database.MongoConnectionImpl;
import org.springframework.stereotype.Component;

@Component
public class GetCollectionFromEntityName
{
    private static MongoConnectionImpl database = new MongoConnectionImpl();
    
    public static MongoCollection getCollectionFromEntityName(String entityName)
    {
        
        return database.mapCollectionsAndEntities().get(entityName);
    }
}
