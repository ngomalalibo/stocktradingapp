package com.ngomalalibo.stocktradingapp.dataprovider;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.util.CustomEmailValidator;
import com.ngomalalibo.stocktradingapp.util.CustomNullChecker;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class GetObjectByID
{
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    public static <T extends PersistingBaseEntity> T getObjectById(String objectId, MongoCollection<T> collection)
    {
        AtomicReference<T> object = new AtomicReference<>();
        Bson idQuery;
        try
        {
            if (!CustomNullChecker.emptyNullStringChecker(objectId))
            {
                idQuery = Aggregates.match(Filters.eq("_id", objectId));
                Optional.ofNullable(collection.aggregate(Collections.singletonList(idQuery))).ifPresent(a -> object.set(a.first()));
//                System.out.println(objectId + " retrieved object " + object);
            }
        }
        catch (Exception e)
        {
//            System.out.println("#### Handling Exception in getObjectById #######");
            e.printStackTrace();
            e.getMessage();
            e.getCause();
        }
        
        return object.get();
    }
    
    public static <T extends PersistingBaseEntity> T getObjectByEmail(String email, MongoCollection<T> collection)
    {
        T object = null;
        Bson idQuery;
        try
        {
            if (CustomEmailValidator.validEmailValue(email))
            {
                idQuery = Filters.eq("email", email);
                object = collection.find(idQuery).first();
            }
        }
        catch (Exception e)
        {
            e.getMessage();
            e.getCause();
        }
//        System.out.println(email + " retrieved object " + object);
        return object;
    }
    
    /*public static <T extends PersistingBaseEntity> List<T> getObjectsByProperty(String property, String propertyValue, MongoCollection<T> collection)
    {
        if (CustomEmailValidator.validEmailValue(propertyValue))
        {
            return getListOfEntity(property, propertyValue, collection);
        }
        if (!CustomNullChecker.nullStringChecker(propertyValue))
        {
            return getListOfEntity(property, propertyValue, collection);
        }
        return null;
    }*/
    
    private static <T extends PersistingBaseEntity> List<T> getListOfEntity(String property, String propertyValue, MongoCollection<T> collection)
    {
        Bson idQuery = Filters.eq(property, propertyValue);
        List<T> entity = new ArrayList<>();
        collection.find(idQuery).iterator().forEachRemaining(entity::add);
        
        return entity;
    }
}
