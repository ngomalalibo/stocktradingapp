package com.ngomalalibo.stocktradingapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = false)
public class Stock extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @JsonProperty("companyName")
    private String securityName;
    
    @NotNull
    @NumberFormat
    @JsonProperty("latestPrice")
    private Double unitSharePrice;
    
    public Stock()
    {
        super();
    }
    
    public Stock(String securityName, Double unitSharePrice)
    {
        this.securityName = securityName;
        this.unitSharePrice = unitSharePrice;
    }
    
    /*
    @JsonProperty("mostRecentSharePrice")
    private Double mostRecentSharePrice;
    
    @JsonProperty("iexBidPrice")
    public Integer iexBidPrice;
    
    @JsonProperty("iexAskPrice")
    public Integer iexAskPrice;
    
    @JsonProperty("iexAskSize")
    public Integer iexAskSize;*/
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
