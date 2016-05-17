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

package org.broadleafcommerce.vendor.passthrough.web.processor;

import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.web.payment.processor.AbstractTRCreditCardExtensionHandler;
import org.broadleafcommerce.common.web.payment.processor.TRCreditCardExtensionManager;
import org.broadleafcommerce.vendor.passthrough.service.payment.PassthroughPaymentGatewayConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * This sample handler will add itself to the {@link TRCreditCardExtensionManager}
 * and will add some default hidden parameters/form POST URL for our fake
 * {@link org.broadleafcommerce.vendor.passthrough.web.controller.mock.processor.PassthroughMockProcessorController}
 *
 * Note, we don't want this loaded into the extension manager
 * if a real payment gateway is used, so make sure to not scan this class when
 * using a real implementation. This is for demo purposes only.
 *
 * In order to use this sample extension handler, you will need to component scan
 * the package "com.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blPassthroughPaymentGatewayTRExtensionHandler")
public class PassthroughPaymentGatewayTRExtensionHandler extends AbstractTRCreditCardExtensionHandler {

    public static final String FORM_ACTION_URL = PassthroughPaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
    public static final String FORM_HIDDEN_PARAMS = "FORM_HIDDEN_PARAMS";

    @Resource(name = "blTRCreditCardExtensionManager")
    protected TRCreditCardExtensionManager extensionManager;

    @Resource(name = "blPassthroughPaymentGatewayTransparentRedirectService")
    protected PaymentGatewayTransparentRedirectService transparentRedirectService;

    @Resource(name = "blPassthroughPaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration configuration;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public String getFormActionURLKey() {
        return FORM_ACTION_URL;
    }

    @Override
    public String getHiddenParamsKey() {
        return FORM_HIDDEN_PARAMS;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return transparentRedirectService;
    }

    @Override
    public void populateFormParameters(Map<String, Map<String, String>> formParameters, PaymentResponseDTO responseDTO) {
        String actionUrl = (String) responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.TRANSPARENT_REDIRECT_URL);
        Map<String, String> actionValue = new HashMap<String, String>();
        actionValue.put(getFormActionURLKey(), actionUrl);
        formParameters.put(getFormActionURLKey(), actionValue);

        Map<String, String> hiddenFields = new HashMap<String, String>();

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL,
                    responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL).toString());
        }
        
        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.TRANSACTION_AMT) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.TRANSACTION_AMT,
                    responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.TRANSACTION_AMT).toString());
        }
        
        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.ORDER_ID) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.ORDER_ID,
                    responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.ORDER_ID).toString());
        }
        
        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.CUSTOMER_ID) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.CUSTOMER_ID,
                    responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.CUSTOMER_ID).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_FIRST_NAME) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_FIRST_NAME,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_LAST_NAME) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_LAST_NAME,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_ADDRESS_LINE1) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_ADDRESS_LINE1,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_ADDRESS_LINE2) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_ADDRESS_LINE2,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_CITY) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_CITY,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_STATE) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_STATE,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_ZIP) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_ZIP,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_COUNTRY) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.BILLING_COUNTRY,
                    responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.BILLING_COUNTRY).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_FIRST_NAME) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_FIRST_NAME,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_LAST_NAME) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_LAST_NAME,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_CITY) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_CITY,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_STATE) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_STATE,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_ZIP) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_ZIP,
                responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_COUNTRY) != null) {
            hiddenFields.put(PassthroughPaymentGatewayConstants.SHIPPING_COUNTRY,
                    responseDTO.getResponseMap().get(PassthroughPaymentGatewayConstants.SHIPPING_COUNTRY).toString());
        }

        formParameters.put(getHiddenParamsKey(), hiddenFields);
    }
}
