package com.ngomalalibo.stocktradingapp.repository;

import com.mongodb.client.MongoDatabase;
import com.ngomalalibo.stocktradingapp.database.DatabaseConnection;
import com.ngomalalibo.stocktradingapp.database.MongoConnectionImpl;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectionTest
{
    @Autowired
    MongoConnectionImpl con;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        
    }
    
    @Test
    public void testDatabaseConnectionString()
    {
        String url = "mongodb+srv://stocks:stocks@mflix-lfo1z.mongodb.net/test?retryWrites=true&w=majority";
        
        assertEquals(con.getDBSTR(), url);
    }
    
    @Test
    public void testDBStartup()
    {
        assertEquals(con.getDBNAME(), ((MongoDatabase) con.startDB()).getName());
    }
    
    @Test
    public void testCreationOfCollections()
    {
        assertEquals(7, con.createAllCollections());
    }
    
    @Test
    public void testDBStats()
    {
        Document rDoc = con.getDBStats();
        assertEquals(rDoc.getString("db"), "stocks");
        assertEquals(Integer.parseInt(rDoc.getInteger("collections").toString()), 7);
    }
}
