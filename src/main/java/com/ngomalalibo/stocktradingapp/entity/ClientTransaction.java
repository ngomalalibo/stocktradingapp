package com.ngomalalibo.stocktradingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientTransaction extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private TransactionType transactionType; // buy/sell
    @Min(value = 1, message = "No of units cannot be less than 1")
    private int noOfUnits;
    
    private StockQuote stockQuote;
    
    private Double transactionAmount;
    
    @NotNull
    @NotEmpty
    private String username;
    private ClientTransactionStatus transactionStatus;
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    public ClientTransaction(TransactionType transactionType, int noOfUnits, StockQuote stockQuote, Double transactionAmount, String username, ClientTransactionStatus transactionStatus)
    {
        this.transactionType = transactionType;
        this.noOfUnits = noOfUnits;
        this.stockQuote = stockQuote;
        this.transactionAmount = transactionAmount;
        this.username = username;
        this.transactionStatus = transactionStatus;
    }
    
    public ClientTransaction()
    {
    }
}
