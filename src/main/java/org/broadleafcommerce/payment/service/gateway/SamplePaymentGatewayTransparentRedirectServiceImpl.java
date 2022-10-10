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

package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.payment.PaymentGatewayRequestType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.TransparentRedirectConstants;
import org.broadleafcommerce.common.payment.dto.AddressDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * This is an example implementation of a {@link PaymentGatewayTransparentRedirectService}.
 * This is just a sample that mimics what hidden fields a real payment gateway implementation
 * might put on your transparent redirect credit card form on your checkout page.
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
@Service("blSamplePaymentGatewayTransparentRedirectService")
public class SamplePaymentGatewayTransparentRedirectServiceImpl extends AbstractPaymentGatewayTransparentRedirectService {

    @Resource(name = "blSamplePaymentGatewayConfiguration")
    protected SamplePaymentGatewayConfiguration configuration;

    @Override
    public String getCreateCustomerPaymentTokenReturnURLFieldKey(PaymentResponseDTO responseDTO) {
        return SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL;
    }

    @Override
    public String getCreateCustomerPaymentTokenCancelURLFieldKey(PaymentResponseDTO responseDTO) {
        return SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL;
    }

    @Override
    public String getUpdateCustomerPaymentTokenReturnURLFieldKey(PaymentResponseDTO responseDTO) {
        return SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL;
    }

    @Override
    public String getUpdateCustomerPaymentTokenCancelURLFieldKey(PaymentResponseDTO responseDTO) {
        return SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL;
    }

    @Override
    public PaymentResponseDTO createAuthorizeForm(PaymentRequestDTO requestDTO) throws PaymentException {
        return createCommonTRFields(requestDTO);
    }

    @Override
    public PaymentResponseDTO createAuthorizeAndCaptureForm(PaymentRequestDTO requestDTO) throws PaymentException {
        return createCommonTRFields(requestDTO);
    }

    @Override
    public PaymentResponseDTO createCustomerPaymentTokenForm(PaymentRequestDTO requestDTO) throws PaymentException {
        return createCommonTRFields(requestDTO);
    }

    @Override
    public PaymentResponseDTO updateCustomerPaymentTokenForm(PaymentRequestDTO requestDTO) throws PaymentException {
        return createCommonTRFields(requestDTO);
    }

    protected PaymentResponseDTO createCommonTRFields(PaymentRequestDTO requestDTO) {
        if (PaymentGatewayRequestType.CREATE_CUSTOMER_PAYMENT_TR.equals(requestDTO.getGatewayRequestType()) ||
                PaymentGatewayRequestType.UPDATE_CUSTOMER_PAYMENT_TR.equals(requestDTO.getGatewayRequestType())) {
            Assert.isTrue(requestDTO.getCustomer() != null,
                    "The Customer on the Payment Request DTO must not be null for a Customer Payment tokenization request.");
        } else {
            Assert.isTrue(requestDTO.getTransactionTotal() != null,
                    "The Transaction Total on the Payment Request DTO must not be null");
            Assert.isTrue(requestDTO.getOrderId() != null,
                    "The Order ID on the Payment Request DTO must not be null");
        }

        //Put The shipping, billing, and transaction amount fields as hidden fields on the form
        //In a real implementation, the gateway will probably provide some API to tokenize this information
        //which you can then put on your form as a secure token. For this sample,
        // we will just place them as plain-text hidden fields on the form
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
                SamplePaymentGatewayType.NULL_GATEWAY);

        //If - this is a Customer Payment Transparent Redirect request, then
        //populate the response DTO with the appropriate Customer information
        //as well as the correct transparent redirect URL and return URL.
        //Else - this is a normal Payment Transaction Transparent Redirect request which would normally be
        //called during a checkout flow and the appropriate Order information will be put on the response DTO
        if (PaymentGatewayRequestType.CREATE_CUSTOMER_PAYMENT_TR.equals(requestDTO.getGatewayRequestType()) ||
                PaymentGatewayRequestType.UPDATE_CUSTOMER_PAYMENT_TR.equals(requestDTO.getGatewayRequestType())){
            responseDTO.responseMap(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL,
                    configuration.getCustomerPaymentTransparentRedirectUrl());
            responseDTO.responseMap(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL,
                configuration.getCustomerPaymentTransparentRedirectReturnUrl());

            responseDTO.responseMap(SamplePaymentGatewayConstants.CUSTOMER_ID, requestDTO.getCustomer().getCustomerId());

        } else {
            // This is a normal checkout payment transaction request
            responseDTO.responseMap(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL,
                    configuration.getTransparentRedirectUrl());
            responseDTO.responseMap(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL,
                    configuration.getTransparentRedirectReturnUrl());

            responseDTO.responseMap(SamplePaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId());
            responseDTO.responseMap(SamplePaymentGatewayConstants.TRANSACTION_AMT, requestDTO.getTransactionTotal());
        }

        AddressDTO billTo = requestDTO.getBillTo();
        if (billTo != null)  {
            responseDTO.responseMap(SamplePaymentGatewayConstants.BILLING_FIRST_NAME, billTo.getAddressFirstName())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_LAST_NAME, billTo.getAddressLastName())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1, billTo.getAddressLine1())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2, billTo.getAddressLine2())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_CITY, billTo.getAddressCityLocality())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_STATE, billTo.getAddressStateRegion())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_ZIP, billTo.getAddressPostalCode())
                    .responseMap(SamplePaymentGatewayConstants.BILLING_COUNTRY, billTo.getAddressCountryCode());
        }

        AddressDTO shipTo = requestDTO.getShipTo();
        if (shipTo != null) {
            responseDTO.responseMap(SamplePaymentGatewayConstants.SHIPPING_FIRST_NAME, shipTo.getAddressFirstName())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_LAST_NAME, shipTo.getAddressLastName())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1, shipTo.getAddressLine1())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2, shipTo.getAddressLine2())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_CITY, shipTo.getAddressCityLocality())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_STATE, shipTo.getAddressStateRegion())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_ZIP, shipTo.getAddressPostalCode())
                    .responseMap(SamplePaymentGatewayConstants.SHIPPING_COUNTRY, shipTo.getAddressCountryCode());
        }

        return responseDTO;

    }

}
