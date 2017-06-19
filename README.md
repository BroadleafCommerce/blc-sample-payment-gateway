blc-sample-payment-gateway
=============

Broadleaf Sample Payment Gateway Integration - a sample payment gateway integration that does not integrate with an actual payment gateway. Its sole purpose is to demonstrate how the framework may integrate with a real gateway and demonstrate a sample implementation for many of the gateway interfaces.

### Sample Nonce Payment Submission

In order to set up the payment submission using a payment nonce and ajax request, you will need to include a CheckoutEndpoint
like the following:

```java
@RestController
@RequestMapping(value = "/cart/checkout",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class SamplePaymentNonceCheckoutEndpoint extends com.broadleafcommerce.rest.api.endpoint.checkout.CheckoutEndpoint {
    
    @RequestMapping(method = RequestMethod.POST, value = "/complete")
    public String performCheckout(HttpServletRequest request,
                                        @RequestParam("cartId") Long cartId,
                                        @RequestParam("payment_method_nonce") String nonce) {
        Order cart = orderService.findOrderById(cartId);

        if (cart != null) {
            try {
                //Create a new PAYMENT_NONCE Order Payment
                OrderPayment paymentNonce = orderPaymentService.create();
                paymentNonce.setType(PaymentType.CREDIT_CARD);
                paymentNonce.setPaymentGatewayType(SamplePaymentGatewayType.NULL_GATEWAY);
                paymentNonce.setAmount(cart.getTotalAfterAppliedPayments());
                paymentNonce.setOrder(cart);

                //Populate Billing Address per UI requirements
                //For this example, we'll copy the address from the temporary Credit Card's Billing address and archive the payment,
                // (since Heat Clinic's checkout template saves and validates the address in a previous section).
                OrderPayment tempPayment = null;
                for (OrderPayment payment : cart.getPayments()) {
                    if (PaymentGatewayType.TEMPORARY.equals(payment.getGatewayType()) &&
                            PaymentType.CREDIT_CARD.equals(payment.getType())) {
                        tempPayment = payment;
                        break;
                    }
                }
                if (tempPayment != null){
                    paymentNonce.setBillingAddress(addressService.copyAddress(tempPayment.getBillingAddress()));
                    orderService.removePaymentFromOrder(cart, tempPayment);
                }

                // Create the UNCONFIRMED transaction for the payment
                PaymentTransaction transaction = orderPaymentService.createTransaction();
                transaction.setAmount(cart.getTotalAfterAppliedPayments());
                transaction.setRawResponse("Sample Payment Nonce");
                transaction.setSuccess(true);
                transaction.setType(PaymentTransactionType.UNCONFIRMED);
                transaction.getAdditionalFields().put(SamplePaymentGatewayConstants.PAYMENT_METHOD_NONCE, nonce);

                transaction.setOrderPayment(paymentNonce);
                paymentNonce.addTransaction(transaction);
                orderService.addPaymentToOrder(cart, paymentNonce, null);

                cart = orderService.save(cart, true);

                return paymentGatewayCheckoutService.initiateCheckout(cart.getId());
            } catch (Exception e) {
                throw BroadleafWebServicesException.build(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                        .addMessage(BroadleafWebServicesException.CHECKOUT_PROCESSING_ERROR);
            }
        }

        throw BroadleafWebServicesException.build(HttpStatus.SC_NOT_FOUND)
                .addMessage(BroadleafWebServicesException.CART_NOT_FOUND);
    }
}
```

Here is an example of a checkout request using the `superagent` javascript library:

```javascript
import request from 'core/util/superagent'

const SamplePaymentService = {
    
    tokenizeCard: (paymentForm) => {
        // This is where you would tokenize the card
        const nonce = `${paymentForm.creditCardNumber}|${paymentForm.creditCardName}|${paymentForm.expirationDate}|${paymentForm.cvv}`
        return Promise.resolve(nonce)
    },

    performCheckout: (order, payment_method_nonce) => {
        return new Promise((resolve, reject) => {
            request.post('/api/cart/checkout/complete')
                .query({ cartId: order.id, payment_method_nonce })
                .end((err, response) => {
                    if (err) {
                        reject(err)
                    }

                    if (response.body) {
                        resolve(response.body)
                    }
                })
        })
    }
}

export default SamplePaymentService
```