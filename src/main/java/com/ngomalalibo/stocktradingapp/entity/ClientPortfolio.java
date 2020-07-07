package com.ngomalalibo.stocktradingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientPortfolio extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    @NotNull
    private String username;
    private Set<String> stocks = new HashSet<>();
    private List<ClientTransaction> transactions = new ArrayList<>();
    private Double totalAmountInvested;
    private Double currentValueOfPortfolio;
    private Double profitFromSales;
    private LocalDateTime dateOfAcquisition;
    
    @Length(max = 100, message = "Evaluation should not me more than 100 characters.")
    private String evaluation;
    
    public ClientPortfolio(String username, Set<String> stocks, List<ClientTransaction> transactions, Double totalAmountInvested, Double currentValueOfPortfolio, Double profitFromSales, LocalDateTime dateOfAcquisition, String evaluation)
    {
        this.username = username;
        this.stocks = stocks;
        this.transactions = transactions;
        this.totalAmountInvested = totalAmountInvested;
        this.currentValueOfPortfolio = currentValueOfPortfolio;
        this.profitFromSales = profitFromSales;
        this.dateOfAcquisition = dateOfAcquisition;
        this.evaluation = evaluation;
    }
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    public ClientPortfolio()
    {
    }
}
