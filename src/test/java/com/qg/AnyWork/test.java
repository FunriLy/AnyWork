package com.qg.AnyWork;

import com.qg.AnyWork.exception.OrganizationException;

/**
 * Created by logan on 2017/7/10.
 */
public class test {
    public static void main(String[] args){
        if (1==1) throw new OrganizationException("用户已加入该组织");
        System.out.println(11111);
    }
}
