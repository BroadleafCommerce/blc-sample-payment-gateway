/*-
 * #%L
 * BroadleafCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2024 Broadleaf Commerce
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

import org.apache.commons.lang3.ArrayUtils;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentAdditionalFieldType;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayWebResponseService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This is an example implementation of a {@link PaymentGatewayWebResponseService}.
 * This will translate the Post information back from
 * {@link org.broadleafcommerce.vendor.sample.web.controller.mock.processor.SampleMockProcessorController}
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
@Service("blSamplePaymentGatewayWebResponseService")
public class SamplePaymentGatewayWebResponseServiceImpl extends AbstractPaymentGatewayWebResponseService {

    @Resource(name = "blPaymentGatewayWebResponsePrintService")
    protected PaymentGatewayWebResponsePrintService webResponsePrintService;

    @Resource(name = "blSamplePaymentGatewayConfiguration")
    protected SamplePaymentGatewayConfiguration configuration;

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
                SamplePaymentGatewayType.NULL_GATEWAY)
                .rawResponse(webResponsePrintService.printRequest(request));

        Map<String,String[]> paramMap = request.getParameterMap();

        Money amount = Money.ZERO;
        if (paramMap.containsKey(SamplePaymentGatewayConstants.TRANSACTION_AMT)) {
            String amt = paramMap.get(SamplePaymentGatewayConstants.TRANSACTION_AMT)[0];
            amount = new Money(amt);
        }

        boolean approved = false;
        if (paramMap.containsKey(SamplePaymentGatewayConstants.RESULT_SUCCESS)) {
            String[] msg = paramMap.get(SamplePaymentGatewayConstants.RESULT_SUCCESS);
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
                .orderId(parse(paramMap, SamplePaymentGatewayConstants.ORDER_ID))
                .customer()
                    .customerId(parse(paramMap, SamplePaymentGatewayConstants.CUSTOMER_ID))
                    .done()
                .paymentToken(parse(paramMap, SamplePaymentGatewayConstants.PAYMENT_TOKEN_ID))
                .responseMap(SamplePaymentGatewayConstants.GATEWAY_TRANSACTION_ID,
                        parse(paramMap, SamplePaymentGatewayConstants.GATEWAY_TRANSACTION_ID))
                .responseMap(SamplePaymentGatewayConstants.RESULT_MESSAGE,
                        parse(paramMap, SamplePaymentGatewayConstants.RESULT_MESSAGE))
                .responseMap(PaymentAdditionalFieldType.TOKEN.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.PAYMENT_TOKEN_ID))
                .responseMap(PaymentAdditionalFieldType.LAST_FOUR.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_LAST_FOUR))
                .responseMap(PaymentAdditionalFieldType.CARD_TYPE.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_TYPE))
                .responseMap(PaymentAdditionalFieldType.NAME_ON_CARD.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_NAME))
                .responseMap(PaymentAdditionalFieldType.EXP_DATE.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE))
                .billTo()
                    .addressFirstName(parse(paramMap, SamplePaymentGatewayConstants.BILLING_FIRST_NAME))
                    .addressLastName(parse(paramMap, SamplePaymentGatewayConstants.BILLING_LAST_NAME))
                    .addressLine1(parse(paramMap, SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1))
                    .addressLine2(parse(paramMap, SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2))
                    .addressCityLocality(parse(paramMap, SamplePaymentGatewayConstants.BILLING_CITY))
                    .addressStateRegion(parse(paramMap, SamplePaymentGatewayConstants.BILLING_STATE))
                    .addressPostalCode(parse(paramMap, SamplePaymentGatewayConstants.BILLING_ZIP))
                    .addressCountryCode(parse(paramMap, SamplePaymentGatewayConstants.BILLING_COUNTRY))
                    .addressPhone(parse(paramMap, SamplePaymentGatewayConstants.BILLING_PHONE))
                    .addressEmail(parse(paramMap, SamplePaymentGatewayConstants.BILLING_EMAIL))
                    .addressCompanyName(parse(paramMap, SamplePaymentGatewayConstants.BILLING_COMPANY_NAME))
                    .done()
                .creditCard()
                    .creditCardHolderName(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_NAME))
                    .creditCardLastFour(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_LAST_FOUR))
                    .creditCardType(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_TYPE))
                    .creditCardExpDate(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE))
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
