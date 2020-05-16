package com.ngomalalibo.stocktradingapp.dataproviders;

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