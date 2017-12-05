/*
 * #%L
 * BroadleafCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2017 Broadleaf Commerce
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

import org.broadleafcommerce.common.payment.CreditCardType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.GatewayCustomerDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayCustomerService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.sample.service.payment.PaymentTransactionType;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Random;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Service("blSamplePaymentGatewayCustomerService")
public class SamplePaymentGatewayCustomerServiceImpl extends AbstractPaymentGatewayCustomerService {

    public static final String RESPONSE_MAP_KEY_PREFEIX = "customer";

    @Override
    public PaymentResponseDTO createGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException {
        Assert.isTrue(requestDTO.customerPopulated(),
                "The Gateway Customer information on the Payment Request DTO must not be null and must be populated");

        // Items to do in realistic implementation:
        // 1. Use requestDTO#getCustomer() to populate a request to the payment gateway
        // 2. Add payment nonce to the request
        // 3. Make the request
        // 4. Parse the request into a PaymentResponseDTO

        return buildSampleGatewayResponse(requestDTO, PaymentTransactionType.CREATE_CUSTOMER);
    }

    @Override
    public PaymentResponseDTO updateGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException {
        Assert.isTrue(requestDTO.customerPopulated()
                        && requestDTO.getCustomer().getCustomerId() != null,
                "The Gateway Customer information on the Payment Request DTO must not be null and must be populated");

        // Items to do in realistic implementation:
        // 1. Use requestDTO#getCustomer() to populate a request to the payment gateway
        // 2. Add payment nonce to the request
        // 3. Make the request
        // 4. Parse the request into a PaymentResponseDTO

        return buildSampleGatewayResponse(requestDTO, PaymentTransactionType.UPDATE_CUSTOMER);
    }

    @Override
    public PaymentResponseDTO deleteGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException {
        Assert.isTrue(requestDTO.getCustomer() != null && requestDTO.getCustomer().getCustomerId() != null,
                "The Gateway Customer Customer ID on the Payment Request DTO must not be null and must be populated");

        // Items to do in realistic implementation:
        // 1. Use requestDTO#getCustomer() to populate a request to the payment gateway
        // 2. Make the request
        // 3. Parse the request into a PaymentResponseDTO

        return buildSampleGatewayResponse(requestDTO, PaymentTransactionType.DELETE_CUSTOMER);
    }

    protected PaymentResponseDTO buildSampleGatewayResponse(PaymentRequestDTO requestDTO, PaymentTransactionType transactionType) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY)
                .paymentTransactionType(transactionType)
                .successful(true);

        parseCustomer(responseDTO, requestDTO);
        parseCreditCard(responseDTO, requestDTO);
        parsePaymentToken(responseDTO);

        return responseDTO;
    }

    protected void parseCustomer(PaymentResponseDTO responseDTO, PaymentRequestDTO requestDTO) {
        GatewayCustomerDTO<PaymentRequestDTO> customer = requestDTO.getCustomer();

        responseDTO.customer().customerId(customer.getCustomerId());

        responseDTO.responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "id", customer.getCustomerId())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "firstName", customer.getFirstName())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "lastName", customer.getLastName())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "emailAddress", customer.getEmail())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "phoneNumber", customer.getPhone())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "company", customer.getCompanyName())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "website", customer.getWebsite());
    }

    protected void parseCreditCard(PaymentResponseDTO responseDTO, PaymentRequestDTO requestDTO) {
        GatewayCustomerDTO<PaymentRequestDTO> customer = requestDTO.getCustomer();

        responseDTO.creditCard()
                .creditCardHolderName(customer.getFirstName() + " " + customer.getLastName())
                .creditCardLastFour("1111")
                .creditCardType(CreditCardType.MASTERCARD.getType())
                .creditCardExpDate("01/99");
    }

    protected void parsePaymentToken(PaymentResponseDTO responseDTO) {
        Random rnd = new Random();
        int randomNumber = 100000 + rnd.nextInt(90000000);

        responseDTO.paymentToken("SAMPLE_PAYMENT_GATEWAY_MULTI_USE_TOKEN_" + randomNumber);
    }
}
