package com.ngomalalibo.stocktradingapp.repository;

import com.google.common.base.Strings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.ngomalalibo.stocktradingapp.dataprovider.GetObjectByID;
import com.ngomalalibo.stocktradingapp.dataprovider.SortProperties;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.util.AppConstants;
import com.ngomalalibo.stocktradingapp.util.CustomNullChecker;
import com.ngomalalibo.stocktradingapp.util.GetCollectionFromEntityName;
import com.ngomalalibo.stocktradingapp.util.GetEntityNamesFromPackage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Repository
@Getter
public class GenericDataRepository
{
    private MongoCollection collection;
    private String simpleName;
    
    public GenericDataRepository()
    {
        super();
    }
    
    public boolean isCollectionNullorEmpty()
    {
        if (collection == null)
        {
            return true;
        }
        return collection.countDocuments() <= 0;
    }
    
    public <B extends PersistingBaseEntity> GenericDataRepository(B bean)
    {
        super();
        
        if (!CustomNullChecker.nullObjectChecker(bean))
        {
            Set<String> entities = GetEntityNamesFromPackage.retrieveEntityNamesFromPackage(AppConstants.ENTITY_PACKAGE);
            if (entities.size() <= 0)
            {
                throw new EntityNotFoundException("Entity not found");
            }
            String name = bean.getClass().getSimpleName();
            entities.forEach(ent ->
                             {
                                 if (ent.equals(name))
                                 {
                                     collection = GetCollectionFromEntityName.getCollectionFromEntityName(name);
                                     //System.out.println("collection = " + collection);
                                     simpleName = name;
                                 }
                                 else
                                 {
                                     simpleName = name;
                                     try
                                     {
                                         throw new EntityNotFoundException("Entity not found");
                                     }
                                     catch (EntityNotFoundException e)
                                     {
                                         e.getMessage();
                                     }
                                 }
                             }
            );
        }
        else
        {
            throw new CustomNullPointerException("attempting to instantiate generic data service for null object");
        }
        /*}
        else
        {
            throw new CustomNullPointerException("Collection does not exist or is empty");
        }*/
    }
    
    public <B extends PersistingBaseEntity> List<B> getRecordsByEntityKey(String key, String value, List<SortProperties> sortOrder)
    {
        if (!isCollectionNullorEmpty())
        {
            int e = 0;
            // System.out.println("***getRecordsByEntityKey called***");
            List<B> searchResult = new ArrayList<>();
            Bson filter;
            if (CustomNullChecker.emptyNullStringChecker(value.trim()))
            {
                filter = Aggregates.match(new Document());
            }
            else
            {
                filter = Aggregates.match(Filters.eq(key, value));
            }
            LinkedList<Bson> pipeline = new LinkedList<>();
            pipeline.add(filter);
            sortOrder.forEach(ps -> pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName()))));
//        Optional<AggregateIterable> aggregate = Optional.of((AggregateIterable) GetCollectionFromEntityName.getCollectionFromEntityName(entity).aggregate(Collections.singletonList(filter)));
        /*Optional<AggregateIterable<B>> aggregate = Optional.of(collection.aggregate(pipeline));
        aggregate.get().iterator().forEachRemaining(searchResult::add);*/
            
            
            // Optional<AggregateIterable<B>> aggregate = Optional.of(collection.aggregate(pipeline));
            Optional<AggregateIterable<B>> aggregate = Optional.of(collection.aggregate(pipeline));
            aggregate.get().iterator().forEachRemaining(searchResult::add);
            
            return searchResult;
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public int getEntityByKeyCount(String key, String value)
    {
        if (!isCollectionNullorEmpty())
        {
            if (!CustomNullChecker.emptyNullStringChecker(key) && !CustomNullChecker.emptyNullStringChecker(value))
            {
                List<SortProperties> sortOrder = new ArrayList<>();
                return getRecordsByEntityKey(key, value, sortOrder).size();
            }
            else
            {
                throw new CustomNullPointerException("attempting to get Entity By key count on non-existent key/value");
            }
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public <B extends PersistingBaseEntity> PersistingBaseEntity getRecordByEntityProperty(String property, String value)
    {
        if (!isCollectionNullorEmpty())
        {
//            log.info("property -> " + property);
//            log.info("value -> " + value);
            if (!Strings.isNullOrEmpty(property) && !Strings.isNullOrEmpty(value))
            {
                AtomicReference<PersistingBaseEntity> returnB = new AtomicReference<>();
                Bson filter = Filters.eq(property.trim(), value.trim());
                Optional.ofNullable(collection.find(filter).first()).ifPresent(d -> returnB.set((B) d));
                
                return returnB.get();
            }
            else
            {
                log.info("Null pointer exception");
                throw new CustomNullPointerException("attempting to retrieve record with non-existent key/value");
            }
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
    }
    
    //implementation retrieves a one-many data mapping. eg. Does returns Person with List<Addresses>
    public <B extends PersistingBaseEntity, S extends PersistingBaseEntity> Map<B, List<S>> getRecordAndEmbeddedObjectList(String mainEntity, String subEntity, String key, String keyValue, String foreignKey, String embeddedKey)
    {
        if (!isCollectionNullorEmpty())
        {
            if (!CustomNullChecker.nullStringSChecker(subEntity, key, keyValue, foreignKey, embeddedKey))
            {
                List<B> mainResult = new ArrayList<>();
                List<S> subResult = new ArrayList<>();
                
                Map<B, List<S>> mainAndSub = new HashMap<>();
                Bson filter = Aggregates.match(Filters.eq(key, keyValue));
                Optional.of(collection.aggregate(Collections.singletonList(filter)))
                        .ifPresent(d ->
                                   {
                                       d.iterator().forEachRemaining(mr ->
                                                                     {
                                                                         try
                                                                         {
                                                                             String simpleName = mr.getClass().getSimpleName();
                                                                             Field field = mr.getClass().getDeclaredField(embeddedKey);
                                                                             field.setAccessible(true);
                            
                                                                             Class<?> type = field.getType();
                                                                             List<Object> subIds = new ArrayList<>();
                                                                             if (List.class.isAssignableFrom(type))
                                                                             {
                                                                                 // subIds = List.of(field.get(mr));
                                                                                 subIds = Collections.singletonList(field.get(mr));
                                                                             }
                            
                                                                             Bson subFilter = Aggregates.match(Filters.in(foreignKey, subIds));
                                                                             Optional.of((S) GetCollectionFromEntityName.getCollectionFromEntityName(subEntity)
                                                                                                                        .aggregate(Collections.singletonList(subFilter)))
                                                                                     .ifPresent(s ->
                                                                                                {
                                                                                                    ((AggregateIterable) s)
                                                                                                            .iterator().forEachRemaining(v ->
                                                                                                                                         {
                                                                                                                                             subResult.add((S) v);
                                                                                                                                         });
                                                                                                    mainAndSub.put((B) mr, subResult);
                                
                                                                                                });
                                                                         }
                                                                         catch (NoSuchFieldException | IllegalAccessException e)
                                                                         {
                                                                             e.printStackTrace();
                                                                         }
                                                                     });
                                   });
                
                
                //Commented to try code above. Revert here if there are issues
        /*Optional.of(collection.aggregate(Collections.singletonList(filter)).iterator()).get()
                .forEachRemaining(mr ->
                                  {
                                      mainResult.add((B) mr);
            
                                      Bson subFilter = Aggregates.match(Filters.eq(foreignKey, foreignKeyValue));
                                      Optional.of((S) GetCollectionFromEntityName.getCollectionFromEntityName(subEntity)
                                                                                 .aggregate(Collections.singletonList(subFilter)).first()).ifPresent(
                                              sub ->
                                              {
                                                  mainAndSub.put((B) mr, sub);
                                              }
                                      );
                                  });*/
                return mainAndSub;
            }
            else
            {
                throw new CustomNullPointerException("attempting to get map of entity and sub entity");
            }
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
    }
    
    //    public static Stream<Author> getAuthors()
    public <B extends PersistingBaseEntity> Stream<B> getEntitiesSorted(String sort, boolean asc)
    {
        if (!isCollectionNullorEmpty())
        {
            if (!CustomNullChecker.emptyNullStringChecker(sort))
            {
                Bson bsort = asc ? Aggregates.sort(Sorts.ascending(sort)) : Aggregates.sort(Sorts.descending(sort));
                
                List<B> allRecords = new ArrayList<>();
                Optional.of(collection.aggregate(Collections.singletonList(bsort)).iterator()).get().forEachRemaining(d -> allRecords.add((B) d));
                
                return allRecords.stream();
            }
            else
            {
                throw new CustomNullPointerException("attempting to sort with empty sort criteria");
            }
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
    }
    
    //    public static Stream<Author> getAllOfEntity()
    public <B extends PersistingBaseEntity> Stream<B> getAllOfEntity()
    {
        if (!isCollectionNullorEmpty())
        {
            //collection is already set by constructor
            List<B> allRecords = new ArrayList<>();
            Optional.of(collection.find()).ifPresent(s ->
                                                     {
                                                         s.iterator().forEachRemaining(d -> allRecords.add((B) d));
                                                     });
            return allRecords.stream();
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    
    public <B extends PersistingBaseEntity> List<B> getEntityByTwoFilterSearch(List<SortProperties> sort, String filterOne, String filterTwo, String name)
    {
        /*System.out.println("filterOne = " + filterOne);
        System.out.println("filterTwo = " + filterTwo);
        System.out.println("name = " + name);*/
        if (!isCollectionNullorEmpty())
        {
            List<B> searchResult = new ArrayList<>();
            Bson match;
            if (CustomNullChecker.emptyNullStringChecker(name.trim()))
            {
                //fetch all documents. Empty filter
                match = Aggregates.match(new Document());
            }
            else
            {
                
                Pattern ptrn = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                /**db.getCollection("persons").find({$or: [{"firstName": {$regex: /kenz/i}}, {"lastName": {$regex: /kenz/i}}]})*/
                // match = Aggregates.match(Filters.or(Filters.regex(filterOne, ptrn), Filters.regex(filterTwo, ptrn)));
                match = Aggregates.match(Filters.or(Filters.regex(filterOne, ptrn), Filters.regex(filterTwo, ptrn)));
            }
            LinkedList<Bson> pipeline = new LinkedList<>();
            pipeline.add(match);
            sort.forEach(ps -> pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName()))));
            
            Optional<AggregateIterable<B>> aggregate = Optional.of(collection.aggregate(pipeline));
            aggregate.get().iterator().forEachRemaining(searchResult::add);
            
            return searchResult;
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    //formerly PersonDataService.getPersonAndAuthorCount()
    //implementation does not retrieve a one-many data mapping. It returns a 1:1 mapping. eg. Does not return Person with List<Addresses>. It returns Person with singletonList List<Address>
    public <B extends PersistingBaseEntity, S extends PersistingBaseEntity> Map<B, List<S>> getEntityJoin(String name, String foreignId, String value, String foreignEntity, String filterOne, String filterTwo)
    {
        if (!isCollectionNullorEmpty())
        {
            List<S> searchResult2 = new ArrayList<>();
            
            Map<B, List<S>> mainAndSub = new HashMap<>();
            
            LinkedList<Bson> pipeline = new LinkedList<>();
            Bson match;
            if (name.trim().equals(""))
            {
                //fetch all documents
                match = Aggregates.match(new Document());
            }
            else
            {//gds.getEntityJoin("kenz", "_id", personId, "Person", "firstName", "lastName");
                Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                match = Aggregates.match(Filters.or(Filters.regex(filterOne, pattern), Filters.regex(filterTwo, pattern)));
            }
            //genericDataService.getEntityJoin(filter, "_id", id, "Address", "firstName", "lastName");
            pipeline.add(match);
            
            Optional.of((collection.aggregate(pipeline)).iterator())
                    .ifPresent(g ->
                               {
                                   g.forEachRemaining(searchResultItem ->
                                                      {
                                                          Bson foreignFilter = Aggregates.match(Filters.eq(foreignId, value));
                                                          MongoCollection collection = GetCollectionFromEntityName.getCollectionFromEntityName(foreignEntity);
                    
                                                          Optional.of(collection.aggregate(Collections.singletonList(foreignFilter))).ifPresent(d -> d.iterator().forEachRemaining(e -> searchResult2.add((S) e)));
                    
                                                          mainAndSub.put((B) searchResultItem, searchResult2);
                                                      });
                               });
            return mainAndSub;
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public <B extends PersistingBaseEntity, S extends PersistingBaseEntity> Map<B, List<S>> getOneEntityFromJoinById(String name, String foreignId, String value, String foreignEntity, String filterOne, String filterTwo, String bean, String primaryId)
    {
        if (!isCollectionNullorEmpty())
        {
            Map<B, List<S>> entityJoin = getEntityJoin(name, foreignId, value, foreignEntity, filterOne, filterTwo);
            
            B objectById = (B) GetObjectByID.getObjectById(primaryId, GetCollectionFromEntityName.getCollectionFromEntityName(bean));
            B bb = entityJoin.keySet().stream().filter(d -> d.getUuid().equals(objectById.getUuid())).findAny().get();
            return Collections.singletonMap(bb, entityJoin.get(bb));
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
    }
}
