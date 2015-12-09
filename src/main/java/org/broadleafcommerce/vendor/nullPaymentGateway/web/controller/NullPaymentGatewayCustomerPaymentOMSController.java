package org.broadleafcommerce.vendor.nullPaymentGateway.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.web.payment.controller.CustomerPaymentGatewayAbstractController;
import org.broadleafcommerce.payment.service.gateway.NullPaymentGatewayConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller("blNullPaymentGatewayCustomerPaymentOMSController")
@RequestMapping("/" + NullPaymentGatewayCustomerPaymentOMSController.GATEWAY_CONTEXT_KEY)
public class NullPaymentGatewayCustomerPaymentOMSController extends CustomerPaymentGatewayAbstractController {

    protected static final Log LOG = LogFactory.getLog(NullPaymentGatewayCustomerPaymentOMSController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "null-customer-payment-oms";

    protected static final String CUSTOMER_PAYMENT_ERROR = "CUSTOMER_PAYMENT_OMS_ERROR";
    protected static String customerPaymentErrorMessage = "customerPaymentOMSErrorMessage";

    @Resource(name = "blNullPaymentGatewayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "blNullPaymentGatewayConfiguration")
    protected NullPaymentGatewayConfiguration paymentGatewayConfiguration;

    @Override
    public String getGatewayContextKey() {
        return GATEWAY_CONTEXT_KEY;
    }

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
        return super.getErrorViewRedirect();
    }

}
