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

package org.broadleafcommerce.vendor.sample.service.payment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chris Kittrell (ckittrell)
 */
public class PaymentTransactionType extends org.broadleafcommerce.common.payment.PaymentTransactionType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PaymentTransactionType> TYPES = new LinkedHashMap<>();

    public static final PaymentTransactionType CREATE_CUSTOMER = new PaymentTransactionType("CREATE_CUSTOMER", "Create Customer");
    public static final PaymentTransactionType UPDATE_CUSTOMER = new PaymentTransactionType("UPDATE_CUSTOMER", "Update Customer");
    public static final PaymentTransactionType DELETE_CUSTOMER = new PaymentTransactionType("DELETE_CUSTOMER", "Delete Customer");

    public static PaymentTransactionType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public PaymentTransactionType() {
        //do nothing
    }

    public PaymentTransactionType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        PaymentTransactionType other = (PaymentTransactionType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
