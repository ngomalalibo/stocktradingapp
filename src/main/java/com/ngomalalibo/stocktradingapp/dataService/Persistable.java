package com.ngomalalibo.stocktradingapp.dataService;

import com.ngomalalibo.stocktradingapp.entities.PersistingBaseEntity;

import java.util.List;

public interface Persistable
{
    
    public <T extends PersistingBaseEntity> void prepersist(T t);
    
    public <T extends PersistingBaseEntity> PersistingBaseEntity save(T t);
    
    public <T extends PersistingBaseEntity> boolean delete(String collectionName, T t);
    
    public <T extends PersistingBaseEntity> boolean deleteMany(List<T> t, String collectionName);
}
