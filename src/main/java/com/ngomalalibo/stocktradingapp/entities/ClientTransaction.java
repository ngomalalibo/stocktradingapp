package com.ngomalalibo.stocktradingapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ngomalalibo.stocktradingapp.enums.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

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
    private Stock stock = new Stock();
    private Double transactionAmount;
    
    @NotNull
    @NotEmpty
    private String username;
    private ClientTransactionStatus transactionStatus;
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    public ClientTransaction(TransactionType transactionType, int noOfUnits, Stock stock, Double transactionAmount, String username, ClientTransactionStatus transactionStatus)
    {
        this.transactionType = transactionType;
        this.noOfUnits = noOfUnits;
        this.stock = stock;
        this.transactionAmount = transactionAmount;
        this.username = username;
        this.transactionStatus = transactionStatus;
    }
    
    public ClientTransaction()
    {
        super();
    }
}
