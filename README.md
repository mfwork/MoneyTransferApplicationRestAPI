# MoneyTransferApplicationRestAPI

1. Application is for initial design for general banking transfers with selling buying exchange rates.

2. Only Between my account transfer and Internal Transfer is supported now in V1 

2. You are allowed to do transaction with PLN,USD,EGP and EUR which is only sample data for my testing 
   so for new transaction with another currency kinldy add exchange rate for it using below Rest Documentation   
 
3. Start application with attached runnable jar with command 
   java -jar /Users/mfadel/Downloads/moneyTransferApp.jar 
   
4. java 1.8 is required.

5. Sample request and response as below  as Money Transfer RestAPI Documentation reference. 

6. Logs appenders is set to console and File under logs/moneytransfer.log for development purpose for now.

7. "arc-data-export-24-12-2018" File can import it using Google advanced rest client, its good tool to get all saved calls with samples
   so file is already there on GitHub as well.


# Quick calls 

1. Loaded data sample when server started - get all users
Get Request:
http://localhost:9998/user/all

Response:
[
  {
"userId": 1,
"email": "user1@mail.com",
"firstName": "Joan",
"lastName": "Smith"
},
  {
"userId": 2,
"email": "user2@mail.com",
"firstName": "Agata",
"lastName": "kacik"
}
],

2. Get all accounts 

Get Request:
http://localhost:9998/account/all

Response:
[
  {
"id": 1,
"accountNumber": "USD22029551373481",
"accountCurrency": "USD",
"accountBalance": 500
},
  {
"id": 2,
"accountNumber": "PLN22029551373482",
"accountCurrency": "PLN",
"accountBalance": 1000
},
  {
"id": 3,
"accountNumber": "USD22029551373483",
"accountCurrency": "USD",
"accountBalance": 500
},
  {
"id": 4,
"accountNumber": "EGP22029551373486",
"accountCurrency": "EGP",
"accountBalance": 500
},
  {
"id": 5,
"accountNumber": "PLN22029551373485",
"accountCurrency": "PLN",
"accountBalance": 1000
},
  {
"id": 6,
"accountNumber": "USD22029551373484",
"accountCurrency": "USD",
"accountBalance": 500
}
],


3. Create new transfer - http://localhost:9998/transfer/create

Body Request:
{
  "userId": 1,
  "fromAccount": "USD22029551373481",
  "toAccount": "USD22029551373483",
  "amount": "25",
  "transferCurrency":"USD",
  "comment": "thanks alot"
}

{
"message": "Record added succfully for more details::http://localhost:9998/transfer/1"
}

4. Get user accounts - Get - http://localhost:9998/user/<userId>/accounts


5. Create Account Post - http://localhost:9998/account/create

Body Request: // for automatic generation account number
{
  "userId": "1",
  "accountCurrency":"USD",
  "accountBalance":5000
}
or for predefined account number
{
  "userId": "1",
  "accountNumber":"EUR020i84933333223838376",
  "accountCurrency":"USD",
  "accountBalance":90
}

Response:
{
"message": "Account is added successfully for more details::http://localhost:9998/account/7"
}


# User Service

1. Get all users - GET - http://localhost:9998/user/all

2. Create User - Post -  http://localhost:9998/user/create

Body Request:
{
  "firstName": "Mohamed",
  "lastName": "Fadel",
  "email": "m@m.com"
}

Response:
{
"message": "User added successfully for more details::http://localhost:9998/user/3"
}

3. Update User - Put - http://localhost:9998/user/update

Body Request:
{
  "id":3,
  "firstName": "Mohamed",
  "lastName": "Fadel",
  "email": "UpdateMail@m.com"
}

Response:
{
"message": "User updated successfully for more details::http://localhost:9998/user/3"
}

4. Get user accounts by userId - http://localhost:9998/user/<userId>/accounts

Response:
[
  {
"id": 1,
"accountNumber": "USD22029551373481",
"accountCurrency": "USD",
"accountBalance": 500
},
  {
"id": 2,
"accountNumber": "PLN22029551373482",
"accountCurrency": "PLN",
"accountBalance": 1000
},
  {
"id": 3,
"accountNumber": "USD22029551373483",
"accountCurrency": "USD",
"accountBalance": 500
}
]

5. Get user details - http://localhost:9998/user/<id>

{
"userId": 1,
"email": "user1@mail.com",
"firstName": "Joan",
"lastName": "Smith"
}

6. Get user details - http://localhost:9998/user/delete/1

{
"message": "User is deleted"
}


# Account Service

1. reate Account - http://localhost:9998/account/create

Post Request:
http://localhost:9998/account/create

Body Request: // for automatic generation account number
{
  "userId": "1",
  "accountCurrency":"USD",
  "accountBalance":5000
}
or for predefined account number
{
  "userId": "1",
  "accountNumber":"EUR020i84933333223838376",
  "accountCurrency":"USD",
  "accountBalance":90
}

Response:
{
"message": "Account is added successfully for more details::http://localhost:9998/account/7"
}

2. Update Account balance - http://localhost:9998/account/update  - balance, balance and currency only can be updated

Put Request:
http://localhost:9998/account/update

Body Request:
{
  "id":1,
  "userId": "1",
  "accountNumber":"USD22029551373481",
  "accountCurrency":"USD",
  "accountBalance":123444
}

Response:
{
"message": "Account is updated successfully for more details::http://localhost:9998/account/1"
}


3. Get Account details by account number - http://localhost:9998/account/<AccNo>/details

Get Request: 
http://localhost:9998/account/PLN22029551373485/details

Response:
{
"id": 5,
"userId": 2,
"accountCurrency": "PLN",
"accountNumber": "PLN22029551373485",
"accountBalance": 1000
}


4. Get Account details by ID - http://localhost:9998/account/<id>

Get Request: 
http://localhost:9998/account/1

Response:
{
"id": 1,
"userId": 1,
"accountCurrency": "USD",
"accountNumber": "USD22029551373483",
"accountBalance": 123444
}

5. delete req Account details by ID - http://localhost:9998/account/1

{
"message": "Account is deleted"
}


# Transfer Service


1. Create new transfer - http://localhost:9998/transfer/create

Post Request: 


Body Request:
{
  "userId": 1,
  "fromAccount": "USD22029551373481",
  "toAccount": "USD22029551373483",
  "amount": "25",
  "transferCurrency":"USD",
  "comment": "thanks alot"
}

{
"message": "Record added succfully for more details::http://localhost:9998/transfer/1"
}

2. Get transfer by ID - http://localhost:9998/transfer/<id>

Post Request: 
http://localhost:9998/transfer/1

Response:
{
"transactionId": 1,
"userId": 1,
"fromAccount": "USD22029551373481",
"toAccount": "USD22029551373483",
"amount": 25,
"transferCurrency": "USD",
"toAccountCurrency": "USD",
"fromAccountCurrency": "USD",
"benficaryName": "Own-Transfer",
"comment": null
}

3. Get all transfers for userId - http://localhost:9998/transfer/usertrans/<userId>

get Request:
http://localhost:9998/transfer/usertrans/1

[
  {
"id": 1,
"fromAccount": "USD22029551373481",
"toAccount": "USD22029551373483",
"amount": 25,
"transferCurrency": "USD",
"fromAccountCurrency": "USD",
"toAccountCurrency": "USD",
"benficaryName": "Own-Transfer",
"comment": null,
"exchangeRate": 1
},
  {
"id": 2,
"fromAccount": "USD22029551373481",
"toAccount": "USD22029551373483",
"amount": 25,
"transferCurrency": "USD",
"fromAccountCurrency": "USD",
"toAccountCurrency": "USD",
"benficaryName": "Own-Transfer",
"comment": null,
"exchangeRate": 1
}
]

# ExchangeRate Service

1. Get by ID - http://localhost:9998/exchangerate/all


[
  {
"id": 1,
"curreny": "PLN",
"sellingRate": 1,
"buyingRate": 1
},
  {
"id": 2,
"curreny": "USD",
"sellingRate": 0.27,
"buyingRate": 0.25
},
  {
"id": 3,
"curreny": "EGP",
"sellingRate": 4.74,
"buyingRate": 4.7
},
  {
"id": 4,
"curreny": "EUR",
"sellingRate": 0.23,
"buyingRate": 0.2
}
],


2. Get http://localhost:9998/exchangerate/<curr_code>     ex - http://localhost:9998/exchangerate/USD

3. Post http://localhost:9998/exchangerate/create


Body
{
  "curreny": "CHF",
  "sellingRate": 0.27,
  "buyingRate": 0.25
}

{
"message": "Exchange Rate for 'CHF'is added successfully for more details::http://localhost:9998/exchangerate/5"
}


4. Put http://localhost:9998/exchangerate/update


{
  "curreny": "CHF",
  "sellingRate": 0.28,
  "buyingRate": 0.26
}

{
"message": "Exchange Rate is updated succfully for more details::http://localhost:9998/exchangerate/5"
}



