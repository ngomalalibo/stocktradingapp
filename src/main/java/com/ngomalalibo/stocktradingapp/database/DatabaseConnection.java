package com.ngomalalibo.stocktradingapp.database;

import com.mongodb.client.MongoCollection;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.enumeration.IDPrefixes;
import org.bson.Document;

import java.util.HashMap;
import java.util.HashSet;


public interface DatabaseConnection
{
    void createCollection(HashSet<String> hash, String collection);
    
    Object startDB();
    
    void stopDB();
    
    Object getDBConnection();
    
    int createAllCollections();
    
    HashMap<String, ?> mapCollectionsAndEntities();
    
    HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames();
    
    HashMap<String, Class<?>> mapObjectAndClazzNames();
    
    HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes();
    
    Document getDBStats();
}
