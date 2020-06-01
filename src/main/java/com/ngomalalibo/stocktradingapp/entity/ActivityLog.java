package com.ngomalalibo.stocktradingapp.entity;

import com.ngomalalibo.stocktradingapp.enumeration.ActivityLogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity(value = MDB.DB_ACTIVITYLOG, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityLog extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String user;
    private String eventName;
    private String eventDescription;
    private ActivityLogType activityLogType;
    
    /**
     * Circular Reference
     */
    private String entity;
    
    public ActivityLog(String user, String eventName, String eventDescription, ActivityLogType activityLogType, String entity)
    {
        super.prepersist(this);
        this.user = user;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.activityLogType = activityLogType;
        this.entity = entity;
    }
    
    public ActivityLog()
    {
        super();
    }
}
