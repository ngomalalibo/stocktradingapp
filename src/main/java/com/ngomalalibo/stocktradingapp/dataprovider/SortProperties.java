package com.ngomalalibo.stocktradingapp.dataprovider;

import lombok.Data;

@Data
public class SortProperties
{
    String propertyName;
    boolean ascending;
    
    public SortProperties(String propertyName, boolean ascending)
    {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }
    
    public SortProperties()
    {
        super();
    }
    
}