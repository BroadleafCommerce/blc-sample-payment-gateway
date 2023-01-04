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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.CreditCardValidator;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.CreditCardType;
import org.broadleafcommerce.common.payment.PaymentAdditionalFieldType;
import org.broadleafcommerce.common.payment.PaymentGatewayRequestType;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.CreditCardDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayTransactionService;
import org.broadleafcommerce.common.payment.service.FailureCountExposable;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.vendor.service.type.ServiceStatusType;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * This is an example implementation of a {@link org.broadleafcommerce.common.payment.service.PaymentGatewayTransactionService}.
 * This handles the scenario where the implementation is PCI-Compliant and
 * the server directly handles the Credit Card PAN. If so, this service should make
 * a server to server call to charge the card against the configured gateway.
 *
 * In order to use load this demo service, you will need to component scan
 * the package "com.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blSamplePaymentGatewayTransactionService")
public class SamplePaymentGatewayTransactionServiceImpl extends AbstractPaymentGatewayTransactionService implements FailureCountExposable {

    protected Integer failureCount = 0;
    protected Boolean isUp = true;
    
    @Override
    public PaymentResponseDTO authorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        return commonCreditCardProcessing(paymentRequestDTO, PaymentTransactionType.AUTHORIZE);
    }

    @Override
    public PaymentResponseDTO capture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(PaymentTransactionType.CAPTURE)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .rawResponse("Successful Capture")
                .successful(true);

        return responseDTO;
    }

    @Override
    public PaymentResponseDTO authorizeAndCapture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        return commonCreditCardProcessing(paymentRequestDTO, PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
    }

    @Override
    public PaymentResponseDTO reverseAuthorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .rawResponse("Successful Reverse Authorization")
                .successful(true);

        return responseDTO;
    }

    @Override
    public PaymentResponseDTO refund(PaymentRequestDTO paymentRequestDTO) throws PaymentException {

        //This null gateway implementation will mimic the ability to support a DETACHED_CREDIT refund.
        if (paymentRequestDTO.getAdditionalFields().containsKey(PaymentGatewayRequestType.DETACHED_CREDIT_REFUND.getType())){
            PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
            responseDTO.valid(true)
                    .paymentTransactionType(PaymentTransactionType.DETACHED_CREDIT)
                    .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                    .rawResponse("Successful Detached Credit")
                    .successful(true);

            return responseDTO;
        } else {
            //This is a normal "follow-on" refund request
            PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
            responseDTO.valid(true)
                    .paymentTransactionType(PaymentTransactionType.REFUND)
                    .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                    .rawResponse("Successful Refund")
                    .successful(true);

            return responseDTO;
        }

    }

    @Override
    public PaymentResponseDTO voidPayment(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(PaymentTransactionType.VOID)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .rawResponse("Successful Reverse Authorization")
                .successful(true);

        return responseDTO;
    }

    /**
     * Does minimal Credit Card Validation (luhn check and expiration date is after today).
     * Mimics the Response of a real Payment Gateway.
     *
     * @param requestDTO
     * @param paymentTransactionType
     * @return
     */
    protected PaymentResponseDTO commonCreditCardProcessing(PaymentRequestDTO requestDTO, PaymentTransactionType paymentTransactionType) {
        setupNoncePaymentRequest(requestDTO);
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(paymentTransactionType);

        CreditCardDTO creditCardDTO = requestDTO.getCreditCard();
        String transactionAmount = requestDTO.getTransactionTotal();
        String paymentToken = (String) requestDTO.getAdditionalFields().get(PaymentAdditionalFieldType.TOKEN.getType());

        CreditCardValidator visaValidator = new CreditCardValidator(CreditCardValidator.VISA);
        CreditCardValidator amexValidator = new CreditCardValidator(CreditCardValidator.AMEX);
        CreditCardValidator mcValidator = new CreditCardValidator(CreditCardValidator.MASTERCARD);
        CreditCardValidator discoverValidator = new CreditCardValidator(CreditCardValidator.DISCOVER);

        if (StringUtils.isNotBlank(transactionAmount) && StringUtils.isNotBlank(paymentToken)) {
            // auto assume that if a token is passed in it is valid and will mock a success.
            responseDTO.amount(new Money(requestDTO.getTransactionTotal()))
                    .rawResponse("Success!")
                    .successful(true);
        } else if (StringUtils.isNotBlank(transactionAmount) &&
                creditCardDTO != null &&
                StringUtils.isNotBlank(creditCardDTO.getCreditCardNum()) &&
                (StringUtils.isNotBlank(creditCardDTO.getCreditCardExpDate()) ||
                        (StringUtils.isNotBlank(creditCardDTO.getCreditCardExpMonth()) &&
                                StringUtils.isNotBlank(creditCardDTO.getCreditCardExpYear())))) {

            boolean validCard = false;
            if (visaValidator.isValid(creditCardDTO.getCreditCardNum())){
                validCard = true;
            } else if (amexValidator.isValid(creditCardDTO.getCreditCardNum())) {
                validCard = true;
            } else if (mcValidator.isValid(creditCardDTO.getCreditCardNum())) {
                validCard = true;
            } else if (discoverValidator.isValid(creditCardDTO.getCreditCardNum())) {
                validCard = true;
            }

            boolean validDateFormat = false;
            boolean validDate = false;
            String[] parsedDate = null;
            if (StringUtils.isNotBlank(creditCardDTO.getCreditCardExpDate())) {
                parsedDate = creditCardDTO.getCreditCardExpDate().split("/");
            } else {
                parsedDate = new String[2];
                parsedDate[0] = creditCardDTO.getCreditCardExpMonth();
                parsedDate[1] = creditCardDTO.getCreditCardExpYear();
            }

            if (parsedDate.length == 2) {
                String expMonth = parsedDate[0];
                String expYear = parsedDate[1];
                ZoneId zone = ZoneId.systemDefault();
                try {
                    ZonedDateTime expirationDate = ZonedDateTime.of((Integer.parseInt("20"+expYear)), Integer.parseInt(expMonth), 1, 0, 0, 0, 0, zone);
                    LocalDateTime expDate = expirationDate.toInstant().atZone(zone).toLocalDateTime();
                    validDate = expDate.isAfter(LocalDateTime.now());
                    validDateFormat = true;
                } catch (Exception e) {
                    //invalid date format
                }
            }

            if (!validDate || !validDateFormat) {
                responseDTO.amount(new Money(0))
                        .rawResponse("cart.payment.expiration.invalid")
                        .successful(false);
            } else if (!validCard) {
                responseDTO.amount(new Money(0))
                        .rawResponse("cart.payment.card.invalid")
                        .successful(false);
            } else {
                populateResponseDTO(responseDTO, requestDTO);

                responseDTO.amount(new Money(requestDTO.getTransactionTotal()))
                        .rawResponse("Success!")
                        .successful(true);
            }

        } else {
            responseDTO.amount(new Money(0))
                    .rawResponse("cart.payment.invalid")
                    .successful(false);
        }

        return responseDTO;
    }

    protected void setupNoncePaymentRequest(PaymentRequestDTO requestDTO) {
        String nonce = (String) requestDTO.getAdditionalFields().get(SamplePaymentGatewayConstants.PAYMENT_METHOD_NONCE);
        if (nonce != null) {
            String[] fields = nonce.split("#");

            String lastFour = (fields[0] == null) ? null : fields[0].substring(fields[0].length() - 4);

            requestDTO.creditCard()
                    .creditCardType(CreditCardType.MASTERCARD.getType())
                    .creditCardNum(fields[0])
                    .creditCardLastFour(lastFour)
                    .creditCardHolderName(fields[1])
                    .creditCardExpDate(fields[2])
                    .creditCardCvv(fields[3]);
        }
    }

    protected void populateResponseDTO(PaymentResponseDTO responseDTO, PaymentRequestDTO requestDTO) {
        Map<String, String> additionalResponseItems = new HashMap<>();

        CreditCardDTO<PaymentRequestDTO> creditCardDTO = requestDTO.getCreditCard();
        additionalResponseItems.put(PaymentAdditionalFieldType.CARD_TYPE.getType(), creditCardDTO.getCreditCardType());
        additionalResponseItems.put(PaymentAdditionalFieldType.NAME_ON_CARD.getType(), creditCardDTO.getCreditCardHolderName());
        additionalResponseItems.put(PaymentAdditionalFieldType.LAST_FOUR.getType(), creditCardDTO.getCreditCardLastFour());
        additionalResponseItems.put(PaymentAdditionalFieldType.EXP_DATE.getType(), creditCardDTO.getCreditCardExpDate());

        responseDTO.getResponseMap().putAll(additionalResponseItems);
    }

    @Override
    public ServiceStatusType getServiceStatus() {
        if (isUp) {
            return ServiceStatusType.UP;
        } else {
            return ServiceStatusType.DOWN;
        }
    }

    @Override
    public synchronized void clearStatus() {
        isUp = true;
        failureCount = 0;
    }

    /**
     * arbitrarily set a failure threshold value of "3"
     */
    @Override
    public synchronized void incrementFailure() {
        if (failureCount >= getFailureReportingThreshold()) {
            isUp = false;
        } else {
            failureCount++;
        }
    }

    @Override
    public Integer getFailureReportingThreshold() {
        return 3;
    }

}
