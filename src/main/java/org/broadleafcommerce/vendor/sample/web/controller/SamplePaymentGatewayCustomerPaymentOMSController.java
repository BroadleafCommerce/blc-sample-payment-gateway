/*
 * #%L
 * BroadleafCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2015 Broadleaf Commerce
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
package org.broadleafcommerce.vendor.sample.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.web.payment.controller.CustomerPaymentGatewayAbstractController;
import org.broadleafcommerce.payment.service.gateway.SamplePaymentGatewayConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller("blSamplePaymentGatewayCustomerPaymentOMSController")
@RequestMapping("/" + SamplePaymentGatewayCustomerPaymentOMSController.GATEWAY_CONTEXT_KEY)
public class SamplePaymentGatewayCustomerPaymentOMSController extends CustomerPaymentGatewayAbstractController {

    protected static final Log LOG = LogFactory.getLog(SamplePaymentGatewayCustomerPaymentOMSController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "sample-customer-payment-oms";

    protected static final String CUSTOMER_PAYMENT_ERROR = "CUSTOMER_PAYMENT_OMS_ERROR";
    protected static String customerPaymentErrorMessage = "customerPaymentOMSErrorMessage";

    @Resource(name = "blSamplePaymentGatewayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "blSamplePaymentGatewayConfiguration")
    protected SamplePaymentGatewayConfiguration paymentGatewayConfiguration;

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return paymentGatewayWebResponseService;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return paymentGatewayConfiguration;
    }

    @Override
    public String getCustomerPaymentViewRedirect(String customerPaymentId) {
        return "redirect:/oms-csrtools/checkout";
    }

    @Override
    public String getCustomerPaymentErrorRedirect() {
        return "redirect:/oms-csrtools/payment-methods/add";
    }

    @Override
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("A Processing Exception Occurred for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(CUSTOMER_PAYMENT_ERROR, customerPaymentErrorMessage);
    }

    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public String returnEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                 Map<String, String> pathVars) throws PaymentException {
        return super.createCustomerPayment(model, request, redirectAttributes);
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Map<String, String> pathVars) throws PaymentException {
        redirectAttributes.addAttribute(CUSTOMER_PAYMENT_ERROR,
                request.getParameter(CUSTOMER_PAYMENT_ERROR));
        return getCustomerPaymentErrorRedirect();
    }

}
