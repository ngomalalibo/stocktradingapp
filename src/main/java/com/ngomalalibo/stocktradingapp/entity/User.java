package com.ngomalalibo.stocktradingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    @Email(message = "Kindly provide a valid email")
    private String username;
    
    private String password;
    private String role;
    private String clientID;
    private String token;
    
    public User()
    {
    }
    
    public User(String username, String password, String role, String clientID)
    {
        this.username = username;
        this.password = password;
        this.role = role;
        this.clientID = clientID;
    }
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
