#stocktradingapp

URLs (accept json): 

https://stockportfoliomgr.herokuapp.com/test

https://stockportfoliomgr.herokuapp.com/registration (params: user & pass)

https://stockportfoliomgr.herokuapp.com/transaction (params:  companyname, deposit, transactiontype, username, units, user )

https://stockportfoliomgr.herokuapp.com/portfolio (param: username)

https://stockportfoliomgr.herokuapp.com/stockprice/{companyname} eg. nflx for Netflix

https://stockportfoliomgr.herokuapp.com/me

The REST API is implemented using the Java Spring framework. The database is a NoSQL MongoDB database hosted remotely on Atlas.
The application retrieves current stock price information from the IEX Cloud service API and uses that information to manage client portfolios in real-time. 
Clients can fund their accounts, buy & sell securities and view their portfolio details for a selected time period using the /transaction endpoint.

The application has a suite of unit and integration tests. All of which are currently passing. Token-based Security has been fully implemented: 
Ten (10) Unit tests for the Database services
Thirteen (13) Integration tests for the controllers and database services

The details of the implementation are as follows:
Domain Models (Entities) and the data points are below: 
1.PersistingBaseEntity (Base Model)
* activityLog
* archivedBy
* archivedDate
* collection
* createdBy
* createdDate
* deleteFilter
* deleteResult
* modifiedBy
* modifiedDate
* organization
* uuid

2. Stocks
* securityName
* unitSharePrice

3. Clients
* clientAccountID
* contactAddress
* DOB
* email
* firstName
* GSM
* lastName
* middleName
* NOKAddress
* NOKEmail
* NOKGSM
* NOKName
* occupation
* registrationDate
* religion
* serialVersionUID
* sex
* typeOfClient

4. ClientsAccount
* balance
* previousBalance
* clientID

5. Client Portfolio
* currentValueOfPortfolio
* dateOfAcquisition
* evaluation
* profitFromSales
* serialVersionUID
* stocks
* totalAmountInvested
* transactions
* username

6. Client Transactions
* noOfUnits
* serialVersionUID
* stock
* transactionAmount
* transactionStatus
* transactionType
* username

7. User
* clientID
* password
* role
* username

The controllers are in the controllers package with all the implemented endpoints.

All the database services are in the repository package. 

The uploaded diagram helps to visualize the software system. It is in root of the project folder.
