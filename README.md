# Straal
![Platform](https://img.shields.io/badge/platform-Android-green.svg?style=flat)
[![Twitter](https://img.shields.io/badge/twitter-@Straal-blue.svg?style=flat)](http://twitter.com/straal_)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat)](LICENSE)
[![Build Status](https://travis-ci.org/straal/straal-android.svg?branch=master&style=flat)](https://travis-ci.org/straal/straal-android)
[![codebeat badge](https://codebeat.co/badges/37b43d1a-4be0-40f7-b774-d26f077d9fdf?style=flat)](https://codebeat.co/projects/github-com-straal-straal-android-master)
![Version](https://img.shields.io/badge/version-0.1.0-orange.svg?style=flat)

> Straal SDK for Android written in Java. A brilliant payment solution for disruptive businesses.

- [Features](#features)
- [Requirements](#requirements)
    - [Backend](#backend)
- [Installation](#installation)
- [Usage](#usage)
	- [Initial configuration](#initial-configuration)
	- [Operations](#operations)
		- [Create card](#create-a-card)
		- [Create transaction with card](#create-transaction-with-a-card)
- [Validation](#validation)
- [Support](#support)
- [License](#license)

## Features
> Straal for Android is a helper library to make it easier
  to make API requests directly from merchant's mobile Android App.
  It utilizes client-side encryption and sends data
  over HTTPS to make secure requests creating transactions and adding cards.

## Requirements
Straal for Android framework is implemented in Java and requires:
- androidSdkVersion 17+
- ```android.permission.INTERNET```

**IMPORTANT:** In order to successfully build and use this SDK you need to have the Java Cryptography Extension installed with an appropriate JCE Policy.

### Backend

You also need a backend service which will handle payment information and issue `CryptKeys` for the app. For more see [here](https://api-reference.straal.com).

You backend service needs to implement **at least one endpoint** at *https://_base_url_/straal/v1/cryptkeys*. This endpoint is used by this SDK to fetch cryptkeys that encrypt sensitive user data.

> It is your backend's job to authorize the user and reject the cryptkey fetch if need be.

## Installation

Currently you can integrate Straal into your Android project by:
- adding this repo as a git submodule into your project
- downloading this repo, building an AAR file and adding it to libs/ folder in your project

## Usage

To use [Straal](https://straal.com/) for Android you need an Android App (in which you want to accept payments), as well as your own backend service.

This SDK lets you implement a secure payment process in your app. User's sensitive data (as credit card numbers) is sent directly from mobile application, so no card data will hit your servers, which results in improved security and fewer PCI compliance requirements.

The security of this process is ensured by a `CryptKey` mechanism. Your merchant backend service is responsible for **authorizing** the app user for a specific CryptKey operation. This is done via `headers` in configuration.

### Initial configuration

First, create a `Straal.Config` object (which contains your Merchant URL and **authorization headers**). Then create your `Straal` object using the configuration.
```java
class StraalPayment {
    private static final String MERCHANT_API_URL = "https://my-merchant-backend-url.com";
    private Map<String, String> headers;  // You have to authorize your user on cryptkeys endpoint using these headers!
    private Straal.Config config = new Straal.Config(MERCHANT_API_URL, headers);
    private Straal straal = new Straal(config);
    
    // ...
}
```
> Note: Once your app needs to change the authorization headers (user logs out or in), you need to create a new Straal object. Neither Straal nor Straal.Configuration objects are meant to be reused or changed.

Once you have your `Straal` object, you can submit objects of type `StraalOperation` to it.
### Operations

#### Create a card
```java
class StraalPayment {
    // ...
    
    private void addCard() {
        // get card data from your UI
        String cardholderName = "John Smith";
        String cardNumber = "4111111111111111";
        String cvv = "123";
        ExpiryDate expiryDate = new ExpiryDate("01", "21");
        // now create a CreditCard object...
        CreditCard card = new CreditCard(cardholderName, cardNumber, cvv, expiryDate);
        // ...pass it to CreateCardOperation...
        CreateCardOperation operation = new CreateCardOperation(card);
        // ...and submit operation to Straal object for execution 
        straal.performAsync(
                operation,
                straalResponse -> handleSuccess(straalResponse),
                straalException -> handleError(straalException)
        );
    }
    
    // ...
}
```
> Note what happens under the hood when you call this last method. First, your merchant backend is fetched on `cryptkeys` endpoint with this `POST` request:

```json
{
  "permission": "v1.cards.create"
}
```

Your backend's job is to authenticate this request (using headers passed to `Straal.Config`), append this json with `customer_uuid` key-value pair and forward it to Straal using [this method](https://api-reference.straal.com/#resources-cryptkeys-create-a-cryptkey).

Then, the credit card data is encrypted, sent to Straal directly, processed by Straal, and responded to.
#### Create transaction with a card
```java
class StraalPayment {
    // ...
    
    private void makePayment() {
        // first, create credit card as before...
        CreditCard card = new CreditCard(cardholderName, cardNumber, cvv, expiryDate);
        // create transaction object with your order's details
        Transaction transaction = new Transaction(999, CurrencyCode.USD, "order:bI124dP2an");
        CreateTransactionWithCardOperation operation = new CreateTransactionWithCardOperation(transaction, card);
        straal.performAsync(
                operation,
                straalResponse -> handleSuccess(straalResponse),
                straalException -> handleError(straalException)
        );
    }
    
    // ...
}
```

> Again, we first fetch your `cryptkeys` endpoint to fetch a crypt key. This time with JSON like this:

```json
{
  "permission": "v1.transactions.create_with_card",
  "transaction": {
    "amount": 999,
    "currency": "usd",
    "reference": "order:bI124dP2an"
  }
}
```

> It is your backend's responsibility to verify the transaction's amount (possibly pairing it with an order using `reference`) and to authorize the user using request headers.

## Validation

> Coming soon

## Support

Any suggestions or reports of technical issues are welcome! Contact us via [email](mailto:devteam@straal.com).

## License

This library is released under Apache License 2.0. See [LICENSE](LICENSE) for more info.