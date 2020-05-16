package com.ngomalalibo.stocktradingapp.utils;

import com.google.gson.Gson;
import com.ngomalalibo.stocktradingapp.entities.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.entities.Stock;
import org.bson.Document;

public class POJOToDocumentConverter
{
    public static <T extends PersistingBaseEntity> Document pojoToDocumentConverter(T t)
    {
        // convert pojo to json using Gson and parse using Document.parse()
        
        Gson gson = new Gson();
        Document parse = Document.parse(gson.toJson(t));
        System.out.println("parse = " + parse);
        return parse;
    }
    
    public static void main(String[] args)
    {
        Stock note = new Stock("nflx", 500D);
        Document doc = POJOToDocumentConverter.pojoToDocumentConverter(note);
        System.out.println("doc = " + doc);
    }
}
