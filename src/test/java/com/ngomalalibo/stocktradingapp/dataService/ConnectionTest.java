package com.ngomalalibo.stocktradingapp.dataService;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectionTest
{
    @Mock
    Connection con;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testDatabaseConnectionString()
    {
        String url = "mongodb+srv://stocks:stocks@mflix-lfo1z.mongodb.net/test?retryWrites=true&w=majority";
        
        assertEquals(Connection.DBSTR, url);
        
    }
    
    @Test
    public void testDBStartup()
    {
        assertEquals(Connection.DBNAME, Connection.startDB().getName());
    }
    
    @Test
    public void testCreationOfCollections()
    {
        assertEquals(7, Connection.createAllCollections());
    }
    
    @Test
    public void testDBStats()
    {
        Document doc = new Document("db", "stocks");
        doc.append("collections", 8);
        
        Mockito.when(con.getDBStats()).thenReturn(doc);
        
        Document rDoc = con.getDBStats();
        assertEquals(rDoc.getString("db"), "stocks");
        assertEquals(Integer.parseInt(rDoc.getInteger("collections").toString()), 8);
    }
}
