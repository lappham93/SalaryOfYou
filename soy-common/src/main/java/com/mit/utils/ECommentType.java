/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mit.utils;

/**
 *
 * @author nghiatc
 * @since Apr 19, 2016
 */
public enum ECommentType {
    FEED(1), PRODUCT(2), SHOP_VIDEO(3);
    
    private final int value;
    
    private ECommentType(int value){
        this.value = value;
    }
    
    public static ECommentType getCommentType(int value) {
    	for (ECommentType e : ECommentType.values())
    		if (e.value == value)
    			return e;
    	return null;
    }
    
    public int getValue(){
        return this.value;
    }
}
