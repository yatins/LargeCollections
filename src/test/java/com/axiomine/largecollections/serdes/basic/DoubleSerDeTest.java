package com.axiomine.largecollections.serdes.basic;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.axiomine.largecollections.serdes.DoubleSerDes;

public class DoubleSerDeTest {
    
    @Test
    public void test() {
        DoubleSerDes.SerFunction ser = new DoubleSerDes.SerFunction();
        DoubleSerDes.DeSerFunction deser = new DoubleSerDes.DeSerFunction();
        
        double d = 1d;
        byte[] ba = ser.apply(d);
        double dd = deser.apply(ba);
        Assert.assertEquals(d, dd);
    }
    
}
