package com.ngomalalibo.stocktradingapp.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class Connection
{
    /*public static final String DBNAME = "stocks";
    public static final String DB_ORGANIZATION = "Stock Trading Inc.";
    public static final String DB_ACTIVITYLOG = "activitylogs";
    public static final String DB_CLIENT = "clients";
    public static final String DB_CLIENT_ACCOUNT = "account";
    public static final String DB_CLIENT_PORTFOLIO = "portfolios";
    public static final String DB_CLIENT_TRANSACTION = "transactions";
    public static final String DB_NOTE = "notes";
    public static final String DB_STOCK = "stocks";
    public static final String DB_USER = "users";
    
    
    // @Value("${spring.data.mongodb.uri}")
    public static String DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
    
    private static MongoClient mongo = null;
    private static MongoDatabase db;
    
    public static MongoCollection<ActivityLog> activityLog;
    public static MongoCollection<Client> client;
    public static MongoCollection<ClientAccount> clientAccount;
    public static MongoCollection<ClientPortfolio> portfolio;
    public static MongoCollection<ClientTransaction> transaction;
    public static MongoCollection<StockQuote> stockQuote;
    public static MongoCollection<User> user;
    
    public static MongoDatabase startDB()
    {
        log.warn("---------------------------- Starting Database");
        DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
        log.info("Database url -> " + DBSTR);
        ConnectionString connectionString = new ConnectionString(DBSTR);
        
        
        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyConnectionString(connectionString)
                                                          .retryWrites(true)
                                                          .codecRegistry(getCodecRegistry())
                                                          .build();
        CodecRegistry pojoCodecRegistry = getCodecRegistry();
        
        if (db == null)
        {
            //mongo = MongoClients.create(DBSTR);
            mongo = MongoClients.create(settings);
            db = mongo.getDatabase(DBNAME);
            //createAllCollections();
            //getDBStats();
        }
        
        activityLog = db.getCollection(DB_ACTIVITYLOG, ActivityLog.class).withCodecRegistry(pojoCodecRegistry);
        client = db.getCollection(DB_CLIENT, Client.class).withCodecRegistry(pojoCodecRegistry);
        clientAccount = db.getCollection(DB_CLIENT_ACCOUNT, ClientAccount.class).withCodecRegistry(pojoCodecRegistry);
        portfolio = db.getCollection(DB_CLIENT_PORTFOLIO, ClientPortfolio.class).withCodecRegistry(pojoCodecRegistry);
        transaction = db.getCollection(DB_CLIENT_TRANSACTION, ClientTransaction.class).withCodecRegistry(pojoCodecRegistry);
        stockQuote = db.getCollection(DB_STOCK, StockQuote.class).withCodecRegistry(pojoCodecRegistry);
        user = db.getCollection(DB_USER, User.class).withCodecRegistry(pojoCodecRegistry);
        
        return db;
    }
    
    public static HashMap<String, MongoCollection<?>> mapCollectionsAndEntities()
    {
        return new HashMap<String, MongoCollection<?>>()
        {{
            put("ActivityLog", Connection.activityLog);
            put("Client", Connection.client);
            put("ClientAccount", Connection.clientAccount);
            put("ClientPortFolio", Connection.portfolio);
            put("ClientTransaction", Connection.transaction);
            put("StockQuote", Connection.stockQuote);
            put("User", Connection.user);
        }};
    }
    
    public static HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames()
    {
        return new HashMap<String, PersistingBaseEntity>()
        {{
            put("ActivityLog", new ActivityLog());
            put("Client", new Client());
            put("ClientAccount", new ClientAccount());
            put("ClientPortFolio", new ClientPortfolio());
            put("ClientTransaction", new ClientTransaction());
            put("StockQuote", new StockQuote());
            put("User", new User());
            
        }};
    }
    
    public static HashMap<String, Class<?>> mapObjectAndClazzNames()
    {
        // return new HashMap<String, Class<?>>()
        return new HashMap<String, Class<?>>()
        {{
            put("ActivityLog", ActivityLog.class);
            put("Client", Client.class);
            put("ClientAccount", ClientAccount.class);
            put("ClientPortFolio", ClientPortfolio.class);
            put("ClientTransaction", ClientTransaction.class);
            put("StockQuote", StockQuote.class);
            put("User", User.class);
            
        }};
    }
    
    
    public static HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes()
    {
        return new HashMap<IDPrefixes, MongoCollection<?>>()
        {{
            put(IDPrefixes.ActivityLog, Connection.activityLog);
            put(IDPrefixes.Client, Connection.client);
            put(IDPrefixes.ClientAccount, Connection.clientAccount);
            put(IDPrefixes.ClientPortfolio, Connection.portfolio);
            put(IDPrefixes.ClientTransaction, Connection.transaction);
            put(IDPrefixes.StockQuote, Connection.stockQuote);
            put(IDPrefixes.User, Connection.user);
        }};
    }
    
    public static void stopDB()
    {
        log.warn("---------------------------- Stopping Database");
        if (mongo != null)
        {
            mongo.close();
        }
        mongo = null;
        db = null;
    }
    
   *//* @Override
    public void contextInitialized(ServletContextEvent s)
    {
        log.info(" -> contextInitialized");
        //startDB();
        
        
        *//*
 
 *//**
 * TODO> uncomment 4 lines below to createCollections, initialize database, load defaults and get database statistics
 * createAllCollections();
 * initializeDatabase();
 * DataInitialization.reloadDefaults();
 * getDBStats();
 *//**//*
     
    }*//*
 *//*
    @Override
    public void contextDestroyed(ServletContextEvent s)
    {
        log.info(" -> contextDestroyed");
        stopDB();
    }*//*
    public static MongoDatabase getDBConnection()
    {
        DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
        if (db == null || mongo == null)
        {
            mongo = MongoClients.create(DBSTR);
            db = mongo.getDatabase(DBNAME);
        }
        return db;
    }
    
    
    public Document getDBStats()
    {
        MongoDatabase ds = getDBConnection();
        Document stats = ds.runCommand(new Document("dbstats", 1024));
        System.out.println("DBStats: " + stats.toJson());
        
        return stats;
    }
    
    public static int createAllCollections()
    {
        log.warn("---------------------------- Creating Collections");
        MongoDatabase db = getDBConnection();
//        MongoIterable<String> colls = dataService.listCollectionNames();
        MongoIterable<String> colls = db.listCollectionNames();
        
        HashSet<String> cols = new HashSet<>();
        for (String j : colls)
        {
            cols.add(j);
        }
        createCollection(cols, DB_ACTIVITYLOG);
        createCollection(cols, DB_CLIENT);
        createCollection(cols, DB_CLIENT_ACCOUNT);
        createCollection(cols, DB_CLIENT_PORTFOLIO);
        createCollection(cols, DB_CLIENT_TRANSACTION);
        createCollection(cols, DB_STOCK);
        createCollection(cols, DB_USER);
        
        
        *//*if (!cols.contains(DB_ADDRESS))
        {
            db.createCollection(DB_ADDRESS);
        }*//*
        log.warn("---------------------------- Collections Created");
        return Iterators.size(db.listCollections().iterator());
    }
    
    public static void createCollection(HashSet<String> hash, String collection)
    {
        if (!hash.contains(collection))
        {
            db.createCollection(collection);
        }
    }*/
    
}
