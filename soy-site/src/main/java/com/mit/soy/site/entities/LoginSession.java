/*
 * Copyright 2015 nghiatc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mit.soy.site.entities;

import java.math.BigDecimal;

/**
 * @author nghiatc
 * @since Nov 10, 2015
 */
public class LoginSession {
    private boolean isLogin = false;
    private int role = -1;
    private String name;
    private long userId = -1;
    private int type;
    private String resetPassword;
    private long shippingAddressId;
    private int shippingOptionId;
    private int paymentOptionId;
    private long billingAddressId;
    private String promoCode;
    private BigDecimal totalCost;


    public LoginSession() {
        shippingAddressId = 0;
        billingAddressId = 0;
        paymentOptionId = 0;
        shippingOptionId = 0;
        promoCode = "";
        totalCost = new BigDecimal(0);
        resetPassword = "";
    }

    public LoginSession(boolean isLogin, int role, String name, long userId, int type) {
        this();
        this.isLogin = isLogin;
        this.role = role;
        this.name = name;
        this.userId = userId;
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LoginSession{" + "isLogin=" + isLogin + ", role=" + role + ", name=" + name + ", userId=" + userId + '}';
    }


    public int getShippingOptionId() {
        return shippingOptionId;
    }

    public void setShippingOptionId(int shippingOptionId) {
        this.shippingOptionId = shippingOptionId;
    }

    public long getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(long billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public int getPaymentOptionId() {
        return paymentOptionId;
    }

    public void setPaymentOptionId(int paymentOptionId) {
        this.paymentOptionId = paymentOptionId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public void clearCheckoutInfo() {
        setBillingAddressId(0);
        setShippingAddressId(0);
        setPromoCode("");
        setShippingOptionId(0);
        setPaymentOptionId(0);
        setTotalCost(new BigDecimal(0));
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }
}
