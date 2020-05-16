package com.ngomalalibo.stocktradingapp.enums;

public enum IDPrefixes
{
    ActivityLog, Client, ClientAccount, ClientPortfolio, ClientTransaction, Stock, Transaction, User;
    
    public static String getDisplayText(IDPrefixes idPrefix)
    {
        switch (idPrefix)
        {
            case ActivityLog:
                return "LOG-";
            case Client:
                return "CLI-";
            case ClientAccount:
                return "CLI_A";
            case ClientPortfolio:
                return "CLI_P";
            case ClientTransaction:
                return "CLI_T";
            case Stock:
                return "ST";
            case Transaction:
                return "TR";
            case User:
                return "USR";
            
            default:
                return "";
        }
    }
    
    public static <T> String getIdPrefix(T t)
    {
        
        String simpleName = t.getClass().getSimpleName();
        //System.out.println("Simple Class Name: " + t.getClass().getSimpleName());
        
        IDPrefixes idPrefixes = IDPrefixes.valueOf(simpleName);
        switch (idPrefixes)
        {
            case ActivityLog:
                return IDPrefixes.getDisplayText(IDPrefixes.ActivityLog);
            case Client:
                return IDPrefixes.getDisplayText(IDPrefixes.Client);
            case ClientTransaction:
                return IDPrefixes.getDisplayText(IDPrefixes.ClientTransaction);
            case ClientPortfolio:
                return IDPrefixes.getDisplayText(IDPrefixes.ClientPortfolio);
            case Stock:
                return IDPrefixes.getDisplayText(IDPrefixes.Stock);
            case Transaction:
                return IDPrefixes.getDisplayText(IDPrefixes.Transaction);
            case User:
                return IDPrefixes.getDisplayText(IDPrefixes.User);
            
            default:
                return "";
        }
    }
}
