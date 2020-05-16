package com.ngomalalibo.stocktradingapp.enums;

public enum ActivityLogType
{
    INFO("Info"), WARN("Warning"), ERROR("Error");
    
    private final String value;
    
    ActivityLogType(String v)
    {
        value = v;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public static String getDisplayText(ActivityLogType i)
    {
        switch (i)
        {
            case INFO:
                return "Info";
            case WARN:
                return "Warning";
            case ERROR:
                return "Error";
            default:
                return "Unknown";
        }
    }
    
    public String displayText()
    {
        return getDisplayText(this);
    }
    
    public static ActivityLogType fromValue(String v)
    {
        for (ActivityLogType c : ActivityLogType.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    
}
