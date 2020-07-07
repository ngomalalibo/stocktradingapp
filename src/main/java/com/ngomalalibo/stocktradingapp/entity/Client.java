package com.ngomalalibo.stocktradingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class Client extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    @NotEmpty
    @NotNull
    private String firstName;
    
    @NotEmpty
    @NotNull
    private String lastName;
    private String middleName;
    private int age;
    private String sex;
    private String GSM;
    @NotNull
    @Email
    private String email;
    private String contactAddress;
    private String religion;
    private String occupation;
    private LocalDate DOB;
    private LocalDate registrationDate;
    private String NOKName;
    private String NOKAddress;
    private String NOKEmail;
    private String NOKGSM;
    private String typeOfClient;
    private String clientAccountID;
    
    @BsonIgnore
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    public Client(String firstName, String lastName, String email)
    {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public Client()
    {
    }
}
