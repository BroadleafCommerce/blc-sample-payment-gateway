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

import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayHostedService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * This is an example implementation of a {@link PaymentGatewayHostedService}.
 * This is just a sample that mimics what URL a real hosted payment gateway implementation
 * might generate. This mimics implementations like PayPal Express Checkout
 *
 * In order to use load this demo service, you will need to component scan
 * the package "com.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blSamplePaymentGatewayHostedService")
public class SamplePaymentGatewayHostedServiceImpl extends AbstractPaymentGatewayHostedService {

    @Resource(name = "blSamplePaymentGatewayHostedConfiguration")
    protected SamplePaymentGatewayHostedConfiguration configuration;

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO requestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                SamplePaymentGatewayType.NULL_HOSTED_GATEWAY)
                .completeCheckoutOnCallback(requestDTO.isCompleteCheckoutOnCallback())
                .responseMap(SamplePaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId())
                .responseMap(SamplePaymentGatewayConstants.TRANSACTION_AMT, requestDTO.getTransactionTotal())
                .responseMap(SamplePaymentGatewayConstants.HOSTED_REDIRECT_URL,
                        configuration.getHostedRedirectUrl());
        return responseDTO;
    }

}
