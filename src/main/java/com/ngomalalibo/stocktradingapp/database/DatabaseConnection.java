package com.ngomalalibo.stocktradingapp.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ngomalalibo.stocktradingapp.entity.*;
import lombok.Getter;
import org.bson.Document;

import java.util.HashSet;

@Getter
public class DatabaseConnection
{
    
    
    /*public abstract void createCollection(HashSet<String> hash, String collection);
    
    public abstract Object startDB();
    
    public abstract void stopDB();
    
    public abstract Object getDBConnection();
    
    public abstract int createAllCollections();
    
    public abstract Document getDBStats();
    
    public abstract <T extends PersistingBaseEntity> MongoCollection getPersistingCollectionFromClass(T t);*/
}
