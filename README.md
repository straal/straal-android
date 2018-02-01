<p align="left">
    <img height=80 src="web/logo_github.png"/>
</p>

---

![Platform](https://img.shields.io/badge/platform-Android-green.svg?style=flat)
![Version](https://img.shields.io/badge/version-0.1.0-orange.svg?style=flat)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat)](LICENSE)
[![Build Status](https://travis-ci.org/straal/straal-android.svg?branch=master&style=flat)](https://travis-ci.org/straal/straal-android)
[![codebeat badge](https://codebeat.co/badges/37b43d1a-4be0-40f7-b774-d26f077d9fdf?style=flat)](https://codebeat.co/projects/github-com-straal-straal-android-master)
[![Twitter](https://img.shields.io/badge/twitter-@straal-blue.svg?style=flat)](http://twitter.com/straal_)

> Straal SDK for Android written in Java. A brilliant payment solution for disruptive businesses.

- [Features](#features)
- [Requirements](#requirements)
    - [Back end](#back-end)
- [Installation](#installation)
- [Usage](#usage)
    - [Initial configuration](#initial-configuration)
    - [Operations](#operations)
        - [Create card](#create-a-card)
        - [Create a transaction with a card](#create-a-transaction-with-a-card)
- [Validation](#validation)
- [Support](#support)
- [License](#license)

## Features

> Straal for Android is a helper library to make it easier
  to make API requests directly from merchant's mobile Android app.
  It utilises client-side encryption and sends data
  over HTTPS to make secure requests creating transactions and adding cards.

## Requirements

Straal for Android framework is implemented in Java and requires:

- androidSdkVersion 17+
- `android.permission.INTERNET`

**IMPORTANT:** In order to build and use this SDK you need to have the Java Cryptography Extension installed with an appropriate JCE Policy.
Since JDK 6u181, 7u171, 8u161, 9b148 unlimited cryptographic policy is enabled by default ([see explanation](https://bugs.java.com/view_bug.do?bug_id=JDK-8170157)).

### Back end

You also need a back-end service which will handle payment information and create `CryptKeys` for the app. For more see [Straal API Reference](https://api-reference.straal.com).

Your back-end service needs to implement **at least one endpoint** at `https://_base_url_/straal/v1/cryptkeys`. This endpoint is used by this SDK to fetch `CryptKeys` that encrypt sensitive user data.

> It is your back end's job to authorize the user and reject the `CryptKey` fetch if necessary.

## Installation

Currently, you can integrate Straal into your Android project by:

- adding this repository as a git submodule into your project
- downloading this repository, building an AAR file and adding it to the `libs` folder in your project

## Usage

To use [Straal](https://straal.com/) for Android, you need your own back-end service and an Android app which you want to use to accept payments.

This SDK lets you implement a secure payment process into your app. Your user's sensitive data (such as credit card numbers) is sent **directly** from the mobile application, so no card data will hit your servers. It results in improved security and fewer PCI compliance requirements.

The security of this process is ensured by a `CryptKey` mechanism. Your merchant back-end service is responsible for **authorizing** the app user for a specific `CryptKey` operation. This is done via `headers` in configuration.

### Initial configuration

First, create a `Straal.Config` object (which contains your Merchant URL and **authorization headers**). Then, create your `Straal` object using the configuration.

```java
class StraalPayment {
    private static final String MERCHANT_API_URL = "https://my-merchant-back-end-url.com";
    private Map<String, String> headers;  // You have to authorize your user on cryptkeys endpoint using these headers!
    private Straal.Config config = new Straal.Config(MERCHANT_API_URL, headers);
    private Straal straal = new Straal(config);

    // ...
}
```
> Note: Once your app needs to change the authorization headers (user logs out or in), you need to create a new `Straal` object. Neither `Straal` nor `Straal.Configuration` objects are meant to be reused or changed.

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
> Note what happens under the hood when you call this last method. First, your merchant back end is fetched on `cryptkeys` endpoint with this `POST` request:

```json
{
  "permission": "v1.cards.create"
}
```

Your back end's job is to authenticate this request (using headers passed to `Straal.Config`), append this JSON with `customer_uuid` key-value pair and forward it to Straal using [this method](https://api-reference.straal.com/#resources-cryptkeys-create-a-cryptkey).

Then, the credit card data is encrypted, sent to Straal directly, processed by Straal, and responded to.

#### Create a transaction with a card

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

> Again, we first fetch your `cryptkeys` endpoint to fetch a `CryptKey`. This time with JSON:

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

> It is your back end's responsibility to verify the transaction's amount (possibly pairing it with an order using `reference`) and to authorize the user using request headers.

## Validation

> Coming soon

## Support

Any suggestions or reports of technical issues are welcome! Contact us via [email](mailto:devteam@straal.com).

## License

This library is released under Apache License 2.0. See [LICENSE](LICENSE) for more info.
