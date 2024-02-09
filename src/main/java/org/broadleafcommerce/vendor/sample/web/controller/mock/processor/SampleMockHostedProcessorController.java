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
package org.broadleafcommerce.vendor.sample.web.controller.mock.processor;

import org.broadleafcommerce.common.util.StringUtil;
import org.broadleafcommerce.payment.service.gateway.SamplePaymentGatewayHostedConfiguration;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This is a sample implementation of a Hosted Payment Gateway Processor.
 * This mimics the flow of a real hosted service like PayPal Express Checkout.
 *
 * In order to use this sample controller, you will need to component scan
 * the package "org.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Controller("blSampleMockHostedProcessorController")
public class SampleMockHostedProcessorController {

    @Resource(name = "blSamplePaymentGatewayHostedConfiguration")
    protected SamplePaymentGatewayHostedConfiguration paymentGatewayConfiguration;

    @RequestMapping(value = "/hosted/sample-checkout", method = RequestMethod.POST)
    public @ResponseBody String retrieveHostedEndpoint(HttpServletRequest request){

        Map<String,String[]> paramMap = request.getParameterMap();

        String transactionAmount = "";
        String orderId="";
        String completeCheckoutOnCallback = "true";
        String resultMessage = "Hosted Call Successful";

        String[] transactionAmountParam = paramMap.get(SamplePaymentGatewayConstants.TRANSACTION_AMT);
        if (transactionAmountParam != null && transactionAmountParam.length > 0) {
            transactionAmount = StringUtil.sanitize(transactionAmountParam[0]);
        }

        String[] orderIdParam = paramMap.get(SamplePaymentGatewayConstants.ORDER_ID);
        if (orderIdParam != null && orderIdParam.length > 0) {
            orderId = StringUtil.sanitize(orderIdParam[0]);
        }

        String[] completeCheckoutOnCallbackParam = paramMap.get(SamplePaymentGatewayConstants.COMPLETE_CHECKOUT_ON_CALLBACK);
        if (completeCheckoutOnCallbackParam != null && completeCheckoutOnCallbackParam.length > 0) {
            completeCheckoutOnCallback = StringUtil.sanitize(completeCheckoutOnCallbackParam[0]);
        }

        StringBuffer response = new StringBuffer();
        response.append("<!DOCTYPE HTML>");
        response.append("<!--[if lt IE 7]> <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\" lang=\"en\"> <![endif]-->");
        response.append("<!--[if IE 7]> <html class=\"no-js lt-ie9 lt-ie8\" lang=\"en\"> <![endif]-->");
        response.append("<!--[if IE 8]> <html class=\"no-js lt-ie9\" lang=\"en\"> <![endif]-->");
        response.append("<!--[if gt IE 8]><!--> <html class=\"no-js\" lang=\"en\"> <!--<![endif]-->");
        response.append("<body>");
        response.append("<h1>Mock Hosted Checkout</h1>");
        response.append("<p>This is an example that demonstrates the flow of a Hosted Third Party Checkout Integration (e.g. PayPal Express Checkout)</p>");
        response.append("<p>This customer will be prompted to either enter their credentials or fill out their payment information. Once complete, " +
                "they will be redirected back to either a confirmation page or a review page to complete checkout.</p>");
        response.append("<form action=\"" +
                paymentGatewayConfiguration.getHostedRedirectReturnUrl() +
                "\" method=\"GET\" id=\"SamplePaymentGatewayRedirectForm\" name=\"SamplePaymentGatewayRedirectForm\">");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.TRANSACTION_AMT
                +"\" value=\"" + transactionAmount + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.ORDER_ID
                +"\" value=\"" + orderId + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.COMPLETE_CHECKOUT_ON_CALLBACK
                +"\" value=\"" + completeCheckoutOnCallback + "\"/>");
        response.append("<input type=\"hidden\" name=\"" + SamplePaymentGatewayConstants.RESULT_MESSAGE
                +"\" value=\"" + resultMessage + "\"/>");

        response.append("<input type=\"submit\" value=\"Please Click Here To Complete Checkout\"/>");
        response.append("</form>");
        response.append("</body>");
        response.append("</html>");

        return response.toString();
    }
}
