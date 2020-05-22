#stocktradingapp

The Java application has been built using the Spring framework. The backend is a NoSQL MongoDB database hosted remotely on Atlas.

The application retrieves current stock price information from the IEX Cloud service API and uses that information to manage client portfolios in real-time. Clients can fund their accounts, buy & sell securities and view their portfolio details for a selected time period.

The application has the following tests. All of which are currently passing. Security has been implemented and is being tested: 
Ten (10) Unit tests for the Database services (ServicesTest.java)
Nine (13) Integration tests for the database services in (StockController.java and ConnectionTest.java).

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


It has the main controller as StockController.java in the controllers package with all the endpoints implemented.
All the database services are in Services.java in the dataservice package. 
The uploaded diagram helps to visualize the software system. It is in root of the project folder.