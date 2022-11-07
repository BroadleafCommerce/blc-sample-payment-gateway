/*-
 * #%L
 * BroadleafCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.broadleafcommerce.vendor.sample.service.payment;

public class MessageConstants {
    public static final String ENV_DEVELOPMENT = "DEVELOPMENT";
    public static final String ENV_SANDBOX = "SANDBOX";
    public static final String ENV_PRODUCTION = "PRODUCTION";

    public static final String CLIENT_TOKEN = "CLIENT_TOKEN";
    public static final String PAYMENT_NONCE = "PAYMENT_NONCE";

    //Request Parameters
    public static final String QUERYSTRING = "QUERYSTRING";
    public static final String BRAINTREEID = "BRAINTREEID";
    public static final String CARDTYPE = "CARD_TYPE";
    public static final String EXPIRATIONMONTH = "EXP_MONTH";
    public static final String EXPIRATIONYEAR = "EXP_YEAR";
    public static final String LASTFOUR = "LAST_FOUR";
    public static final String MESSAGE = "MESSAGE";
    public static final String CARDHOLDERNAME = "NAME_ON_CARD";
    public static final String BIN = "BIN";
    public static final String CUSTOMERID = "CUSTOMER_ID";
    public static final String CUSTOMERLOCATION = "CUSTOMER_LOCATION";
    public static final String EXPIRATIONDATE = "EXP_DATE";
    public static final String MASKEDNUMBER = "MASKED_NUMBER";
    public static final String TOKEN = "TOKEN";
    public static final String COUNTRY = "COUNTRY";
    public static final String STATE = "STATE";
    public static final String TR_DATA = "tr_data";
    public static final String TR_URL = "tr_url";
    public static final String HTTP_REQUEST = "HTTP_REQUEST";

    public static final String ERRORS = "errors";
    public static final String ERRORS_ALLVALIDATIONERRORS = "allValidationErrors";
    public static final String ERROR_ATTRIBUTE = "attribute";
    public static final String ERROR_CODE = "code";
    public static final String ERROR_MESSAGE = "message";
    public static final String ADDBILLINGADDRESSTOPAYMENTMETHOD = "addBillingAddressToPaymentMethod";
    public static final String HOLDINESCROW = "holdInEscrow";
    public static final String STOREINVAULT = "storeInVault";
    public static final String STOREINVAULTONSUCCESS = "storeInVaultOnSuccess";
    public static final String STORESHIPPINGADDRESSINVAULT = "storeShippingAddressInVault";
    public static final String DEVICEDATA = "deviceData";
    public static final String DEVICESESSIONID = "deviceSessionId";
    public static final String FRAUDMERCHANTID = "fraudMerchantId";
    public static final String SHIPPINGADDRESSID = "shippingAddressId";

    //Response Parameters
    public static final String ADDONS = "addOns";
    public static final String ADDON_NAME = "name";
    public static final String ADDON_AMOUNT = "amount";
    public static final String ADDON_DESCRIPTION = "description";
    public static final String ADDON_ID = "id";
    public static final String ADDON_KIND = "kind";
    public static final String ADDON_NUMBEROFBILLINGCYCLES = "numberOfBillingCycles";
    public static final String ADDON_PLANID = "planId";
    public static final String ADDON_QUANTITY = "quantity";
    public static final String ADDRESS_COMPANY = "company";
    public static final String ADDRESS_COUNTRYCODEALPHA2 = "countryCodeAlpha2";
    public static final String ADDRESS_COUNTRYCODEALPHA3 = "countryCodeAlpha3";
    public static final String ADDRESS_COUNTRYCODENUMERIC = "countryCodeNumeric";
    public static final String ADDRESS_COUNTRYNAME = "countryName";
    public static final String ADDRESS_CREATEDAT = "createdAt";
    public static final String ADDRESS_CUSTOMERID = "customerId";
    public static final String ADDRESS_EXTENDEDADDRESS = "extendedAddress";
    public static final String ADDRESS_FIRSTNAME = "firstName";
    public static final String ADDRESS_ID = "id";
    public static final String ADDRESS_LASTNAME = "lastName";
    public static final String ADDRESS_LOCALITY = "locality";
    public static final String ADDRESS_POSTALCODE = "postalCode";
    public static final String ADDRESS_REGION = "region";
    public static final String ADDRESS_STREETADDRESS = "streetAddress";
    public static final String ADDRESS_UPDATEDAT = "updatedAt";
    public static final String AVSERRORRESPONSECODE = "avsErrorResponseCode";
    public static final String AVSPOSTALCODERESPONSECODE = "avsPostalCodeResponseCode";
    public static final String AVSSTREETADDRESSRESPONSECODE = "avsStreetAddressResponseCode";
    public static final String BILLING_ADDRESS = "billingAddress";
    public static final String CHANNEL = "channel";
    public static final String CREATEDAT = "createdAt";
    public static final String CREDIT_CARD = "creditCard";
    public static final String CREDIT_CARD_BIN = "bin";
    public static final String CREDIT_CARD_CARDHOLDERNAME = "cardholderName";
    public static final String CREDIT_CARD_CARDTYPE = "cardType";
    public static final String CREDIT_CARD_CREATEDAT = "createdAt";
    public static final String CREDIT_CARD_CUSTOMERID = "customerId";
    public static final String CREDIT_CARD_CUSTOMERLOCATION = "customerLocation";
    public static final String CREDIT_CARD_EXPIRATIONDATE = "expirationDate";
    public static final String CREDIT_CARD_EXPIRATIONMONTH = "expirationMonth";
    public static final String CREDIT_CARD_EXPIRATIONYEAR = "expirationYear";
    public static final String CREDIT_CARD_IMAGEURL = "imageUrl";
    public static final String CREDIT_CARD_INSTRUMENT = "credit_card";
    public static final String CREDIT_CARD_LAST4 = "last4";
    public static final String CREDIT_CARD_MASKEDNUMBER = "maskedNumber";
    public static final String CREDIT_CARD_COMMERCIAL = "commercial";
    public static final String CREDIT_CARD_DEBIT = "debit";
    public static final String CREDIT_CARD_DURBINREGULATED = "durbinRegulated";
    public static final String CREDIT_CARD_HEALTHCARE = "healthcare";
    public static final String CREDIT_CARD_PAYROLL = "payroll";
    public static final String CREDIT_CARD_PREPAID = "prepaid";
    public static final String CREDIT_CARD_COUNTRYOFISSUANCE = "countryOfIssuance";
    public static final String CREDIT_CARD_ISSUINGBANK = "issuingBank";
    public static final String CREDIT_CARD_UNIQUENUMBERIDENTIFIER = "uniqueNumberIdentifier";
    public static final String CREDIT_CARD_SUBSCRIPTIONS = "subscriptions";
    public static final String CREDIT_CARD_TOKEN = "token";
    public static final String CREDIT_CARD_UPDATEDAT = "updatedAt";
    public static final String CREDIT_CARD_DEFAULT = "default";
    public static final String CREDIT_CARD_FAIL_ON_DUPLICATE = "failOnDuplicate";
    public static final String CREDIT_CARD_VERIFY = "verifyCard";
    public static final String CREDIT_CARD_VENMOSDK = "venmoSdk";
    public static final String CREDIT_CARD_EXPIRED = "expired";
    public static final String CURRENCYISOCODE = "currencyIsoCode";
    public static final String CUSTOMER = "customer";
    public static final String CUSTOMER_CREATEDAT = "createdAt";
    public static final String CUSTOMER_UPDATEDAT = "updatedAt";
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_COMPANY = "company";
    public static final String CUSTOMER_CUSTOMFIELDS = "customFields";
    public static final String CUSTOMER_FIRSTNAME = "firstName";
    public static final String CUSTOMER_LASTNAME = "lastName";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_FAX = "fax";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_WEBSITE = "website";
    public static final String CUSTOMER_ADDRESSES = "addresses";
    public static final String CUSTOMER_CREDITCARDS = "creditCards";
    public static final String CUSTOMER_PAYMENTMETHODS = "paymentMethods";
    public static final String CUSTOMFIELDS = "customFields";
    public static final String CVVRESPONSECODE = "cvvResponseCode";
    public static final String DISBURSEMENTDETAILS = "disbursementDetails";
    public static final String DD_DISBURSEMENTDATE = "disbursementDate";
    public static final String DD_SETTLEMENTAMOUNT = "settlementAmount";
    public static final String DD_SETTLEMENTCURRENCYEXCHANGERATE = "settlementCurrencyExchangeRate";
    public static final String DD_SETTLEMENTCURRENCYISOCODE = "settlementCurrencyIsoCode";
    public static final String DESCRIPTOR = "descriptor";
    public static final String DESCRIPTOR_NAME = "name";
    public static final String DESCRIPTOR_PHONE = "phone";
    public static final String DISCOUNTS = "discounts";
    public static final String DISCOUNT_NAME = "name";
    public static final String DISCOUNT_QUANTITY = "quantity";
    public static final String DISCOUNT_PLANID = "planId";
    public static final String DISCOUNT_NUMBEROFBILLINGCYCLES = "numberOfBillingCycles";
    public static final String DISCOUNT_AMOUNT = "amount";
    public static final String DISCOUNT_DESCRIPTION = "description";
    public static final String DISCOUNT_ID = "id";
    public static final String DISCOUNT_KIND = "kind";
    public static final String ESCROWSTATUS = "escrowStatus";
    public static final String GATEWAYREJECTIONREASON = "gatewayRejectionReason";
    public static final String ID = "id";
    public static final String MERCHANTACCOUNTID = "merchantAccountId";
    public static final String ORDERID = "orderId";
    public static final String PARAMETERS = "parameters";
    public static final String PAYMENT_INSTRUMENT_TYPE = "paymentInstrumentType";
    public static final String PAYMENT_METHOD_IMAGE_URL = "paymentMethodImageURL";
    public static final String PAYMENT_METHOD_TOKEN = "paymentMethodToken";
    public static final String PAYMENT_METHOD_IS_DEFAULT = "paymentMethodIsDefault";
    public static final String PAYPAL_ACCOUNT = "paypal_account";
    public static final String PLANID = "planId";
    public static final String PROCESSORAUTHORIZATIONCODE = "processorAuthorizationCode";
    public static final String PROCESSORRESPONSECODE = "processorResponseCode";
    public static final String PROCESSORRESPONSETEXT = "processorResponseText";
    public static final String VOICEREFERRALNUMBER = "voiceReferralNumber";
    public static final String PURCHASEORDERNUMBER = "purchaseOrderNumber";
    public static final String REFUNDEDTRANSACTIONID = "refundedTransactionId";
    public static final String REFUNDIDS = "refundIds";
    public static final String RESULT_MESSAGE = "message";
    public static final String SERVICEFEEAMOUNT = "serviceFeeAmount";
    public static final String SETTLEMENTBATCHID = "settlementBatchId";
    public static final String SHIPPING_ADDRESS = "shippingAddress";
    public static final String STATUS = "status";
    public static final String STATUSHISTORY = "statusHistory";
    public static final String SE_AMOUNT = "amount";
    public static final String SE_SOURCE = "source";
    public static final String SE_STATUS = "status";
    public static final String SE_TIMESTAMP = "timestamp";
    public static final String SE_USER = "user";
    public static final String SUBSCRIPTION = "subscription";
    public static final String SUBSCRIPTIONID = "subscriptionId";
    public static final String SUBSCRIPTION_TRANSACTIONS = "transactions";
    public static final String SUBSCRIPTION_BALANCE = "balance";
    public static final String SUBSCRIPTION_BILLINGDAYOFMONTH = "billingDayOfMonth";
    public static final String SUBSCRIPTION_BILLINGPERIODENDDATE = "billingPeriodEndDate";
    public static final String SUBSCRIPTION_BILLINGPERIODSTARTDATE = "billingPeriodStartDate";
    public static final String SUBSCRIPTION_CURRENTBILLINGCYCLE = "currentBillingCycle";
    public static final String SUBSCRIPTION_DAYSPASTDUE = "daysPastDue";
    public static final String SUBSCRIPTION_FAILURECOUNT = "failureCount";
    public static final String SUBSCRIPTION_FIRSTBILLINGDATE = "firstBillingDate";
    public static final String SUBSCRIPTION_ID = "id";
    public static final String SUBSCRIPTION_MERCHANTACCOUNTID = "merchantAccountId";
    public static final String SUBSCRIPTION_NEXTBILLINGDATE = "nextBillingDate";
    public static final String SUBSCRIPTION_NEXTBILLINGPERIODAMOUNT = "nextBillingPeriodAmount";
    public static final String SUBSCRIPTION_NUMBEROFBILLINGCYCLES = "numberOfBillingCycles";
    public static final String SUBSCRIPTION_PAIDTHROUGHDATE = "paidThroughDate";
    public static final String SUBSCRIPTION_PAYMENTMETHODTOKEN = "paymentMethodToken";
    public static final String SUBSCRIPTION_PAYMENTMETHODNONCE = "paymentMethodNonce";
    public static final String SUBSCRIPTION_PLANID = "planId";
    public static final String SUBSCRIPTION_PRICE = "price";
    public static final String SUBSCRIPTION_STATUS = "status";
    public static final String SUBSCRIPTION_TRIALDURATION = "trialDuration";
    public static final String SUBSCRIPTION_TRIALDURATIONUNIT = "trialDurationUnit";
    public static final String SUBSCRIPTION_TRIALPERIOD = "trialPeriod";
    public static final String SUBSCRIPTION_NEVEREXPIRES = "neverExpires";
    public static final String TAXAMOUNT = "taxAmount";
    public static final String TYPE = "type";
    public static final String RECURRING = "recurring";
    public static final String UPDATEDAT = "updatedAt";
    public static final String VAULTBILLINGADDRESS = "vaultBillingAddress";
    public static final String VAULTCREDITCARD = "vaultCreditCard";
    public static final String VAULTCUSTOMER = "vaultCustomer";
    public static final String VAULTSHIPPINGADDRESS = "vaultShippingAddress";
    public static final String TAXEXEMPT = "taxExempt";

    public static final String DECLINE_CODE = "declineCode";
    public static final String DECLINE_TEXT = "declineText";
    public static final String DECLINE_TYPE = "declineType";

    public static final String PAYMENT_METHOD_EXISTS = "paymentMethod";

    public static final String BROADLEAF_CHANNEL = "BroadleafCommerce_Cart_EC";
}
