package com.share.crm.settings.test;

import com.share.crm.utils.MD5Util;
import org.junit.Test;

public class testSettings {
    @Test
    public void test01(){
        String md5 = MD5Util.getMD5("cqy860820");
        System.out.println(md5);
    }
}
