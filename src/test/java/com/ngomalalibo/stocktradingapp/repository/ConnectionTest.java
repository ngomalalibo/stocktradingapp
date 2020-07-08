package com.ngomalalibo.stocktradingapp.repository;

import com.mongodb.client.MongoDatabase;
import com.ngomalalibo.stocktradingapp.database.DatabaseConnection;
import com.ngomalalibo.stocktradingapp.database.MongoConnectionImpl;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectionTest
{
    @Autowired
    DatabaseConnection database;
    
    @BeforeEach
    public void setup()
    {
        database = new MongoConnectionImpl();
        database.startDB();
        MockitoAnnotations.initMocks(this);
        
    }
    
    @AfterEach
    public void tearDown()
    {
        database.stopDB();
    }
    
    @Test
    public void testDatabaseConnectionString()
    {
        String url = "mongodb+srv://stocks:stocks@mflix-lfo1z.mongodb.net/test?retryWrites=true&w=majority";
        
        assertEquals(database.getDBSTR(), url);
    }
    
    @Test
    public void testDBStartup()
    {
        assertEquals(database.getDBNAME(), ((MongoDatabase) database.startDB()).getName());
    }
    
    @Test
    public void testCreationOfCollections()
    {
        assertEquals(7, database.createAllCollections());
    }
    
    @Test
    public void testDBStats()
    {
        Document rDoc = database.getDBStats();
        assertEquals(rDoc.getString("db"), "stocks");
        assertEquals(Integer.parseInt(rDoc.getInteger("collections").toString()), 7);
    }
}
