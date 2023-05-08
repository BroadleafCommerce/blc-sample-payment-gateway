/*-
 * #%L
 * BroadleafCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2023 Broadleaf Commerce
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

package org.broadleafcommerce.vendor.sample.web.controller.mock.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.CreditCardValidator;
import org.broadleafcommerce.common.util.StringUtil;
import org.broadleafcommerce.payment.service.gateway.SamplePaymentGatewayConfiguration;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This is a sample implementation of a Payment Gateway Processor.
 * This mimics what a real payment gateway might do in order to process
 * credit card information. So, this controller will handle the POST
 * from your credit card form on your checkout page and do some
 * minimal Credit Card Validation (luhn check and expiration date is after today).
 * In production, that form should securely POST to your third party payment gateway and not this controller.
 *
 * In order to use this sample controller, you will need to component scan
 * the package "org.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Controller("blSampleMockProcessorController")
public class SampleMockProcessorController {

    @Resource(name = "blSamplePaymentGatewayConfiguration")
    protected SamplePaymentGatewayConfiguration paymentGatewayConfiguration;

    @RequestMapping(value = "/sample-checkout/process", method = RequestMethod.POST)
    public @ResponseBody String processTransparentRedirectForm(HttpServletRequest request){
        Map<String,String[]> paramMap = request.getParameterMap();

        String transactionAmount = "";
        String orderId="";
        String billingFirstName = "";
        String billingLastName = "";
        String billingAddressLine1 = "";
        String billingAddressLine2 = "";
        String billingCity = "";
        String billingState = "";
        String billingZip = "";
        String billingCountry = "";
        String shippingFirstName = "";
        String shippingLastName = "";
        String shippingAddressLine1 = "";
        String shippingAddressLine2 = "";
        String shippingCity = "";
        String shippingState = "";
        String shippingZip = "";
        String shippingCountry = "";
        String creditCardName = "";
        String creditCardNumber = "";
        String creditCardExpDate = "";
        String creditCardCVV = "";
        String cardType = "UNKNOWN";

        String resultMessage = "";
        String resultSuccess = "";
        String gatewayTransactionId = UUID.randomUUID().toString();

        String[] transactionAmountParam = paramMap.get(SamplePaymentGatewayConstants.TRANSACTION_AMT);
        if (transactionAmountParam != null && transactionAmountParam.length > 0) {
            transactionAmount = StringUtil.sanitize(transactionAmountParam[0]);
        }

        String[] orderIdParam = paramMap.get(SamplePaymentGatewayConstants.ORDER_ID);
        if (orderIdParam != null && orderIdParam.length > 0) {
            orderId = StringUtil.sanitize(orderIdParam[0]);
        }

        String[] billingFirstNameParam = paramMap.get(SamplePaymentGatewayConstants.BILLING_FIRST_NAME);
        if (billingFirstNameParam != null && billingFirstNameParam.length > 0) {
            billingFirstName = StringUtil.sanitize(billingFirstNameParam[0]);
        }

        String[] billingLastNameParam = paramMap.get(SamplePaymentGatewayConstants.BILLING_LAST_NAME);
        if (billingLastNameParam != null && billingLastNameParam.length > 0) {
            billingLastName = StringUtil.sanitize(billingLastNameParam[0]);
        }

        String[] billingAddressLine1Param = paramMap.get(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1);
        if (billingAddressLine1Param != null && billingAddressLine1Param.length > 0) {
            billingAddressLine1 = StringUtil.sanitize(billingAddressLine1Param[0]);
        }

        String[] billingAddressLine2Param = paramMap.get(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2);
        if (billingAddressLine2Param != null && billingAddressLine2Param.length > 0) {
            billingAddressLine2 = StringUtil.sanitize(billingAddressLine2Param[0]);
        }

        String[] billingCityParam = paramMap.get(SamplePaymentGatewayConstants.BILLING_CITY);
        if (billingCityParam != null && billingCityParam.length > 0) {
            billingCity = StringUtil.sanitize(billingCityParam[0]);
        }

        String[] billingStateParam = paramMap.get(SamplePaymentGatewayConstants.BILLING_STATE);
        if (billingStateParam != null && billingStateParam.length > 0) {
            billingState = StringUtil.sanitize(billingStateParam[0]);
        }

        String[] billingZipParam = paramMap.get(SamplePaymentGatewayConstants.BILLING_ZIP);
        if (billingZipParam != null && billingZipParam.length > 0) {
            billingZip = StringUtil.sanitize(billingZipParam[0]);
        }

        String[] billingCountryParam = paramMap.get(SamplePaymentGatewayConstants.BILLING_COUNTRY);
        if (billingCountryParam != null && billingCountryParam.length > 0) {
            billingCountry = StringUtil.sanitize(billingCountryParam[0]);
        }

        String[] shippingFirstNameParam = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_FIRST_NAME);
        if (shippingFirstNameParam != null && shippingFirstNameParam.length > 0) {
            shippingFirstName = StringUtil.sanitize(shippingFirstNameParam[0]);
        }

        String[] shippingLastNameParam = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_LAST_NAME);
        if (shippingLastNameParam != null && shippingLastNameParam.length > 0) {
            shippingLastName = StringUtil.sanitize(shippingLastNameParam[0]);
        }

        String[] shippingAddressLine1Param = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1);
        if (shippingAddressLine1Param != null && shippingAddressLine1Param.length > 0) {
            shippingAddressLine1 = StringUtil.sanitize(shippingAddressLine1Param[0]);
        }

        String[] shippingAddressLine2Param = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2);
        if (shippingAddressLine2Param != null && shippingAddressLine2Param.length > 0) {
            shippingAddressLine2 = StringUtil.sanitize(shippingAddressLine2Param[0]);
        }

        String[] shippingCityParam = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_CITY);
        if (shippingCityParam != null && shippingCityParam.length > 0) {
            shippingCity = StringUtil.sanitize(shippingCityParam[0]);
        }

        String[] shippingStateParam = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_STATE);
        if (shippingStateParam != null && shippingStateParam.length > 0) {
            shippingState = StringUtil.sanitize(shippingStateParam[0]);
        }

        String[] shippingZipParam = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_ZIP);
        if (shippingZipParam != null && shippingZipParam.length > 0) {
            shippingZip = StringUtil.sanitize(shippingZipParam[0]);
        }

        String[] shippingCountryParam = paramMap.get(SamplePaymentGatewayConstants.SHIPPING_COUNTRY);
        if (shippingCountryParam != null && shippingCountryParam.length > 0) {
            shippingCountry = StringUtil.sanitize(shippingCountryParam[0]);
        }

        String[] creditCardNameParam = paramMap.get(SamplePaymentGatewayConstants.CREDIT_CARD_NAME);
        if (creditCardNameParam != null && creditCardNameParam.length > 0) {
            creditCardName = StringUtil.sanitize(creditCardNameParam[0]);
        }

        String[] creditCardNumberParam = paramMap.get(SamplePaymentGatewayConstants.CREDIT_CARD_NUMBER);
        if (creditCardNumberParam != null && creditCardNumberParam.length > 0) {
            creditCardNumber = StringUtil.sanitize(creditCardNumberParam[0]);
        }

        String[] creditCardExpDateParam = paramMap.get(SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE);
        if (creditCardExpDateParam != null && creditCardExpDateParam.length > 0) {
            creditCardExpDate = creditCardExpDateParam[0];
        }

        String[] creditCardCvvParam = paramMap.get(SamplePaymentGatewayConstants.CREDIT_CARD_CVV);
        if (creditCardCvvParam != null && creditCardCvvParam.length > 0) {
            creditCardCVV = StringUtil.sanitize(creditCardCvvParam[0]);
        }

        CreditCardValidator visaValidator = new CreditCardValidator(CreditCardValidator.VISA);
        CreditCardValidator amexValidator = new CreditCardValidator(CreditCardValidator.AMEX);
        CreditCardValidator mcValidator = new CreditCardValidator(CreditCardValidator.MASTERCARD);
        CreditCardValidator discoverValidator = new CreditCardValidator(CreditCardValidator.DISCOVER);

        if (StringUtils.isNotBlank(transactionAmount) &&
                StringUtils.isNotBlank(creditCardNumber) &&
                StringUtils.isNotBlank(creditCardExpDate)) {

            boolean validCard = false;
            if (visaValidator.isValid(creditCardNumber)){
                validCard = true;
                cardType = "VISA";
            } else if (amexValidator.isValid(creditCardNumber)) {
                validCard = true;
                cardType = "AMEX";
            } else if (mcValidator.isValid(creditCardNumber)) {
                validCard = true;
                cardType = "MASTERCARD";
            } else if (discoverValidator.isValid(creditCardNumber)) {
                validCard = true;
                cardType = "DISCOVER";
            }

            boolean validDateFormat = false;
            boolean validDate = false;
            String[] parsedDate = creditCardExpDate.split("/");
            if (parsedDate.length == 2) {
                String expMonth = StringUtil.sanitize(parsedDate[0]);
                String expYear = StringUtil.sanitize(parsedDate[1]);
                ZoneId zone = ZoneId.systemDefault();
                try {
                    ZonedDateTime expirationDate = ZonedDateTime.of((Integer.parseInt(expYear)), Integer.parseInt(expMonth), 1, 0, 0, 0, 0, zone);
                    LocalDateTime expDate = expirationDate.toInstant().atZone(zone).toLocalDateTime();
                    validDate = expDate.isAfter(LocalDateTime.now());
                    validDateFormat = true;
                } catch (Exception e) {
                    //invalid date format
                }
            }

            if (!validDate || !validDateFormat) {
                transactionAmount = "0";
                resultMessage = "cart.payment.expiration.invalid";
                resultSuccess = "false";
            } else if (!validCard) {
                transactionAmount = "0";
                resultMessage = "cart.payment.card.invalid";
                resultSuccess = "false";
            } else {
                resultMessage = "Success!";
                resultSuccess = "true";
            }

        } else {
            transactionAmount = "0";
            resultMessage = "cart.payment.invalid";
            resultSuccess = "false";
        }

        StringBuffer response = new StringBuffer();
        response.append("<!DOCTYPE HTML>");
        response.append("<!--[if lt IE 7]> <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\" lang=\"en\"> <![endif]-->");
        response.append("<!--[if IE 7]> <html class=\"no-js lt-ie9 lt-ie8\" lang=\"en\"> <![endif]-->");
        response.append("<!--[if IE 8]> <html class=\"no-js lt-ie9\" lang=\"en\"> <![endif]-->");
        response.append("<!--[if gt IE 8]><!--> <html class=\"no-js\" lang=\"en\"> <!--<![endif]-->");
        response.append("<body>");
        response.append("<form action=\"" +
                paymentGatewayConfiguration.getTransparentRedirectReturnUrl() +
                "\" method=\"POST\" id=\"SamplePaymentGatewayRedirectForm\" name=\"SamplePaymentGatewayRedirectForm\">");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.TRANSACTION_AMT
                +"\" value=\"" + transactionAmount + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.ORDER_ID
                +"\" value=\"" + orderId + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.GATEWAY_TRANSACTION_ID
                +"\" value=\"" + gatewayTransactionId + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.RESULT_MESSAGE
                +"\" value=\"" + resultMessage + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.RESULT_SUCCESS
                +"\" value=\"" + resultSuccess + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_FIRST_NAME
                +"\" value=\"" + billingFirstName + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_LAST_NAME
                +"\" value=\"" + billingLastName + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1
                +"\" value=\"" + billingAddressLine1 + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2
                +"\" value=\"" + billingAddressLine2 + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_CITY
                +"\" value=\"" + billingCity + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_STATE
                +"\" value=\"" + billingState + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_ZIP
                +"\" value=\"" + billingZip + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.BILLING_COUNTRY
                +"\" value=\"" + billingCountry + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_FIRST_NAME
                +"\" value=\"" + shippingFirstName + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_LAST_NAME
                +"\" value=\"" + shippingLastName + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1
                +"\" value=\"" + shippingAddressLine1 + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2
                +"\" value=\"" + shippingAddressLine2 + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_CITY
                +"\" value=\"" + shippingCity + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_STATE
                +"\" value=\"" + shippingState + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_ZIP
                +"\" value=\"" + shippingZip + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.SHIPPING_COUNTRY
                +"\" value=\"" + shippingCountry + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.CREDIT_CARD_NAME
                +"\" value=\"" + creditCardName + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.CREDIT_CARD_LAST_FOUR
                +"\" value=\"" + StringUtils.right(creditCardNumber, 4) + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.CREDIT_CARD_TYPE
                +"\" value=\"" + cardType + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE
                +"\" value=\"" + creditCardExpDate + "\"/>");

        response.append("<input type=\"submit\" value=\"Please Click Here To Complete Checkout\"/>");
        response.append("</form>");
        response.append("<script type=\"text/javascript\">");
        response.append("document.getElementById('SamplePaymentGatewayRedirectForm').submit();");
        response.append("</script>");
        response.append("</body>");
        response.append("</html>");

        return response.toString();
    }

}
