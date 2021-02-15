<p align="left">
    <img height=80 src="web/logo_github.png"/>
</p>

---

![Platform](https://img.shields.io/badge/platform-Android-green.svg?style=flat)
![Version](https://img.shields.io/badge/version-1.0.0-orange.svg?style=flat)
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
    private DeviceInfo deviceInfo; // You have to create device info with necessary daya
    private Straal.Config config = new Straal.Config(MERCHANT_API_URL, headers, deviceInfo);
    private Straal straal = new Straal(config);

    // ...
}
```
> Note: Once your app needs to change the authorization headers (user logs out or in), you need to create a new `Straal` object. Neither `Straal` nor `Straal.Configuration` objects are meant to be reused or changed.

Once you have your `Straal` object, you can submit objects of type `StraalOperation` to it.

### Operations

#### Create a transaction with a card
##### Add required Manifest.xml entries
`CreateTransactionWithCardOperation` requires 3DS authentication in external browser.  A URL scheme must be defined to return to your app from the browser.
Edit your AndroidManifest.xml to include Auth3dsBrowserActivity and set the `android:scheme`and` android:host`.


---
You can use built in `StraalTheme.Activity.Invisible` for `Auth3dsBrowserActivity` or create your own with custom atributes values such as `android:windowBackground` to display your placeholder.

---
```xml
<manifest>
    <application>
    	...
        <activity
            android:name="com.straal.sdk.view.auth3ds.Auth3dsBrowserActivity"
            android:launchMode="singleTop"
            android:theme="@style/StraalTheme.Activity.Invisible"
            >
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="${applicationId}" android:host="sdk.straal.com"/>
            </intent-filter>
        </activity>
        ...
    </application>
</manifest>
```
##### Make payment
To make payment you have to create instance of `Straal3dsTransactionHandler` in your `Activity` or `Fragment` as soon as possible and pass it as success consumer to `Straal.performAsync(...)` method.
`Straal3dsTransactionHandler` will handle response from Straal backend, perform 3D-Secure authentication using `Auth3dsBrowserActivity` and at the end inform you about the final result.

> :warning: **Application id passed to `CreateTransactionWithCardOperation` must be the same as declared in your application Manifest.xml**


```java
class StraalPayment {
    // ...
    //initialize authenticationHandler as soon as possible
    private Straal3dsTransactionHandler authenticationHandler = new Straal3dsTransactionHandler(lifecycleOwner, activityResultRegistry, this::handleAuthenticationResult);

    private void makePayment() {
        // first, create credit card as before...
        CreditCard card = new CreditCard(cardholderName, cardNumber, cvv, expiryDate);
        // create transaction object with your order's details
        Transaction transaction = new Transaction(999, CurrencyCode.USD, "order:bI124dP2an");
        CreateTransactionWithCardOperation operation = new CreateTransactionWithCardOperation(transaction, card, BuildConfig.APPLICATION_ID);
        straal.performAsync(
                operation,
                authenticationHandler, //pass authenticationHandler as onSuccess consumer
                straalException -> handleError(straalException)
        );
    }

    public void handleAuthenticationResult(int resultCode) {
            //handle final authentication result
            switch (resultCode) {
                case Auth3dsResult.AUTH_3DS_SUCCESS: {
                    //handle success
                }
                case Auth3dsResult.AUTH_3DS_FAILURE: {
                    //handle failure
                }
                case Auth3dsResult.AUTH_3DS_CANCEL: {
                    //handle cancel
                }
                case Auth3dsResult.AUTH_3DS_RESULT_NOT_CAPTURED: {
                    //handle result not delivered
                }
            }
    }

    // ...
}
```

> We first fetch your `cryptkeys` endpoint to fetch a `CryptKey`. This time with JSON:

```json
{
  "permission": "v1.transactions.create_with_card",
  "transaction": {
    "amount": 999,
    "currency": "usd",
    "reference": "order:bI124dP2an",
    "authentication_3ds": {
        "success_url": "https://sdk.straal.com/successs",
        "failure_url": "https://sdk.straal.com/failure"
     }
  }
}
```

> It is your back end's responsibility to verify the transaction's amount (possibly pairing it with an order using `reference`) and to authorize the user using request headers.

After that our SDK will use fetched `cryptkey` to encrypt payment request send to Straal backend.

## Validation

> Coming soon

## Support

Any suggestions or reports of technical issues are welcome! Contact us via [email](mailto:devteam@straal.com).

## License

This library is released under Apache License 2.0. See [LICENSE](LICENSE) for more info.
