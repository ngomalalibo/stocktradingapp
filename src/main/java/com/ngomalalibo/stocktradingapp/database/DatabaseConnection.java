package com.ngomalalibo.stocktradingapp.database;

import org.bson.Document;

import java.util.HashSet;


public interface DatabaseConnection
{
    void createCollection(HashSet<String> hash, String collection);
    
    Object startDB();
    
    void stopDB();
    
    Object getDBConnection();
    
    int createAllCollections();
    
    Document getDBStats();
}
