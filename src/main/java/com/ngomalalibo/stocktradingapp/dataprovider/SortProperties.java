package com.ngomalalibo.stocktradingapp.dataprovider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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