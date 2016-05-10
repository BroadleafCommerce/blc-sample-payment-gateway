/*
 * #%L
 * BroadleafCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2013 Broadleaf Commerce
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

package org.broadleafcommerce.payment.service.gateway;

import org.apache.commons.lang.ArrayUtils;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentAdditionalFieldType;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayWebResponseService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;
import org.broadleafcommerce.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This is an example implementation of a {@link PaymentGatewayWebResponseService}.
 * This will translate the Post information back from
 * {@link org.broadleafcommerce.vendor.nullPaymentGateway.web.controller.mock.processor.NullMockProcessorController}
 * into a PaymentResponseDTO for processing in the Broadleaf System.
 *
 * Replace with a real Payment Gateway Integration like Braintree or PayPal PayFlow.
 *
 * In order to use load this demo service, you will need to component scan
 * the package "com.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayWebResponseService")
public class NullPaymentGatewayWebResponseServiceImpl extends AbstractPaymentGatewayWebResponseService {

    @Resource(name = "blPaymentGatewayWebResponsePrintService")
    protected PaymentGatewayWebResponsePrintService webResponsePrintService;

    @Resource(name = "blNullPaymentGatewayConfiguration")
    protected NullPaymentGatewayConfiguration configuration;

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
                NullPaymentGatewayType.NULL_GATEWAY)
                .rawResponse(webResponsePrintService.printRequest(request));

        Map<String,String[]> paramMap = request.getParameterMap();

        Money amount = Money.ZERO;
        if (paramMap.containsKey(NullPaymentGatewayConstants.TRANSACTION_AMT)) {
            String amt = paramMap.get(NullPaymentGatewayConstants.TRANSACTION_AMT)[0];
            amount = new Money(amt);
        }

        boolean approved = false;
        if (paramMap.containsKey(NullPaymentGatewayConstants.RESULT_SUCCESS)) {
            String[] msg = paramMap.get(NullPaymentGatewayConstants.RESULT_SUCCESS);
            if (ArrayUtils.contains(msg, "true")) {
                approved = true;
            }
        }

        PaymentTransactionType type = PaymentTransactionType.AUTHORIZE_AND_CAPTURE;
        if (!configuration.isPerformAuthorizeAndCapture()) {
            type = PaymentTransactionType.AUTHORIZE;
        }

        responseDTO.successful(approved)
                .amount(amount)
                .paymentTransactionType(type)
                .orderId(parse(paramMap, NullPaymentGatewayConstants.ORDER_ID))
                .customer()
                    .customerId(parse(paramMap, NullPaymentGatewayConstants.CUSTOMER_ID))
                    .done()
                .paymentToken(parse(paramMap, NullPaymentGatewayConstants.PAYMENT_TOKEN_ID))
                .responseMap(NullPaymentGatewayConstants.GATEWAY_TRANSACTION_ID,
                        parse(paramMap, NullPaymentGatewayConstants.GATEWAY_TRANSACTION_ID))
                .responseMap(NullPaymentGatewayConstants.RESULT_MESSAGE,
                        parse(paramMap, NullPaymentGatewayConstants.RESULT_MESSAGE))
                .responseMap(PaymentAdditionalFieldType.TOKEN.getType(),
                        parse(paramMap, NullPaymentGatewayConstants.PAYMENT_TOKEN_ID))
                .responseMap(PaymentAdditionalFieldType.LAST_FOUR.getType(),
                        parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_LAST_FOUR))
                .responseMap(PaymentAdditionalFieldType.CARD_TYPE.getType(),
                        parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_TYPE))
                .responseMap(PaymentAdditionalFieldType.NAME_ON_CARD.getType(),
                        parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_NAME))
                .responseMap(PaymentAdditionalFieldType.EXP_DATE.getType(),
                        parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_EXP_DATE))
                .billTo()
                    .addressFirstName(parse(paramMap, NullPaymentGatewayConstants.BILLING_FIRST_NAME))
                    .addressLastName(parse(paramMap, NullPaymentGatewayConstants.BILLING_LAST_NAME))
                    .addressLine1(parse(paramMap, NullPaymentGatewayConstants.BILLING_ADDRESS_LINE1))
                    .addressLine2(parse(paramMap, NullPaymentGatewayConstants.BILLING_ADDRESS_LINE2))
                    .addressCityLocality(parse(paramMap, NullPaymentGatewayConstants.BILLING_CITY))
                    .addressStateRegion(parse(paramMap, NullPaymentGatewayConstants.BILLING_STATE))
                    .addressPostalCode(parse(paramMap, NullPaymentGatewayConstants.BILLING_ZIP))
                    .addressCountryCode(parse(paramMap, NullPaymentGatewayConstants.BILLING_COUNTRY))
                    .addressPhone(parse(paramMap, NullPaymentGatewayConstants.BILLING_PHONE))
                    .addressEmail(parse(paramMap, NullPaymentGatewayConstants.BILLING_EMAIL))
                    .addressCompanyName(parse(paramMap, NullPaymentGatewayConstants.BILLING_COMPANY_NAME))
                    .done()
                .creditCard()
                    .creditCardHolderName(parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_NAME))
                    .creditCardLastFour(parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_LAST_FOUR))
                    .creditCardType(parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_TYPE))
                    .creditCardExpDate(parse(paramMap, NullPaymentGatewayConstants.CREDIT_CARD_EXP_DATE))
                    .done();

        return responseDTO;

    }

    protected String parse(Map<String,String[]> paramMap, String param) {
        if (paramMap.containsKey(param)) {
            return paramMap.get(param)[0];
        }

        return null;
    }


}
