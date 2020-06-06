package com.ngomalalibo.stocktradingapp.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.ngomalalibo.stocktradingapp.codec.IDPrefixCodec;
import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.IDPrefixes;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import com.ngomalalibo.stocktradingapp.util.CustomNullChecker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.jersey.internal.guava.Iterators;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Repository
@Getter
public class MongoConnectionImpl implements DatabaseConnection
{
    
    private final String DBNAME = "stocks";
    private static final String DB_ORGANIZATION = "Stock Trading Inc.";
    private static final String DB_ACTIVITYLOG = "activitylogs";
    private static final String DB_CLIENT = "clients";
    private static final String DB_CLIENT_ACCOUNT = "account";
    private static final String DB_CLIENT_PORTFOLIO = "portfolios";
    private static final String DB_CLIENT_TRANSACTION = "transactions";
    private static final String DB_NOTE = "notes";
    private static final String DB_STOCK = "stocks";
    private static final String DB_USER = "users";
    
    // @Value("${spring.data.mongodb.uri}")
    private String DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
    
    private MongoClient mongo = null;
    private MongoDatabase db;
    
    public static MongoCollection<ActivityLog> activityLog;
    public static MongoCollection<Client> client;
    public static MongoCollection<ClientAccount> clientAccount;
    public static MongoCollection<ClientPortfolio> portfolio;
    public static MongoCollection<ClientTransaction> transaction;
    public static MongoCollection<StockQuote> stockQuote;
    public static MongoCollection<User> user;
    
    public <T extends PersistingBaseEntity> MongoCollection getPersistingCollectionFromClass(T t)
    {
        if (!CustomNullChecker.nullObjectChecker(t))
        {
            String simpleName = t.getClass().getSimpleName();
            //System.out.println("IDPrefixes.valueOf(simpleName): " + IDPrefixes.valueOf(simpleName));
            
            Map<IDPrefixes, MongoCollection<?>> map = mapCollectionsAndIDPrefixes();
            IDPrefixes idPrefix = IDPrefixes.valueOf(simpleName);
            return map.get(idPrefix);
        }
        
        else
        {
            throw new NullPointerException("no collection with provided entity");
        }
        
    }
    
    public static CodecRegistry getCodecRegistry()
    {
        final CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        final CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                                                                 .register("com.ngomalalibo.stocktradingapp.entity", "com.ngomalalibo.stocktradingapp.enumeration").automatic(true).build();
        final CodecRegistry cvePojoCodecRegistry = CodecRegistries.fromProviders(pojoCodecProvider);
        final CodecRegistry customEnumCodecs = CodecRegistries.fromCodecs(new IDPrefixCodec());
        return CodecRegistries.fromRegistries(defaultCodecRegistry, customEnumCodecs, cvePojoCodecRegistry);
    }
    
    @Override
    public MongoDatabase startDB()
    {
        log.warn("---------------------------- Starting Database");
        
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
            getDBStats();
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
    
    public static void main(String[] args)
    {
        MongoConnectionImpl mongo = new MongoConnectionImpl();
        mongo.startDB();
        GenericDataRepository use = new GenericDataRepository(new User());
        User username = (User) use.getRecordByEntityProperty("username", "ngomalalibo@yahoo.com");
        System.out.println("username = " + username);
        mongo.stopDB();
    }
    
    @Override
    public HashMap<String, MongoCollection> mapCollectionsAndEntities()
    {
        return new HashMap<String, MongoCollection>()
        {{
            put("ActivityLog", activityLog);
            put("Client", client);
            put("ClientAccount", clientAccount);
            put("ClientPortFolio", portfolio);
            put("ClientTransaction", transaction);
            put("StockQuote", stockQuote);
            put("User", user);
        }};
    }
    
    @Override
    public HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames()
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
    
    @Override
    public HashMap<String, Class<?>> mapObjectAndClazzNames()
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
    
    @Override
    public HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes()
    {
        return new HashMap<IDPrefixes, MongoCollection<?>>()
        {{
            put(IDPrefixes.ActivityLog, activityLog);
            put(IDPrefixes.Client, client);
            put(IDPrefixes.ClientAccount, clientAccount);
            put(IDPrefixes.ClientPortfolio, portfolio);
            put(IDPrefixes.ClientTransaction, transaction);
            put(IDPrefixes.StockQuote, stockQuote);
            put(IDPrefixes.User, user);
        }};
    }
    
    @Override
    public void stopDB()
    {
        log.warn("---------------------------- Stopping Database");
        if (mongo != null)
        {
            mongo.close();
        }
        mongo = null;
        db = null;
    }
    
    /*@Override
    public void contextInitialized(ServletContextEvent s)
    {
        log.info(" -> contextInitialized");
        //startDB();
        
        */
    
    /**
     * TODO> uncomment 4 lines below to createCollections, initialize database, load defaults and get database statistics
     * createAllCollections();
     * initializeDatabase();
     * DataInitialization.reloadDefaults();
     * getDBStats();
     *//*
    }*/
    
    /*@Override
    public void contextDestroyed(ServletContextEvent s)
    {
        log.info(" -> contextDestroyed");
        stopDB();
    }*/
    @Override
    public MongoDatabase getDBConnection()
    {
        DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
        if (db == null || mongo == null)
        {
            mongo = MongoClients.create(DBSTR);
            db = mongo.getDatabase(DBNAME);
        }
        return db;
    }
    
    @Override
    public Document getDBStats()
    {
        MongoDatabase ds = getDBConnection();
        Document stats = ds.runCommand(new Document("dbstats", 1024));
        System.out.println("DBStats: " + stats.toJson());
        
        return stats;
    }
    
    @Override
    public int createAllCollections()
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
        
        
       /* if (!cols.contains(DB_ADDRESS))
        {
            db.createCollection(DB_ADDRESS);
        }*/
        log.warn("---------------------------- Collections Created");
        return Iterators.size(db.listCollections().iterator());
    }
    
    @Override
    public void createCollection(HashSet<String> hash, String collection)
    {
        if (!hash.contains(collection))
        {
            db.createCollection(collection);
        }
    }
    
}
