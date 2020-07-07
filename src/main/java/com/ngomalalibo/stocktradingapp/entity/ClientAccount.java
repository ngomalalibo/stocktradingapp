package com.ngomalalibo.stocktradingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientAccount extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private Double balance;
    private Double previousBalance;
    private String clientID;
    
    public ClientAccount()
    {
    }
    
    public ClientAccount(Double balance, Double previousBalance, String clientID)
    {
        this.balance = balance;
        this.previousBalance = previousBalance;
        this.clientID = clientID;
    }
    
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
