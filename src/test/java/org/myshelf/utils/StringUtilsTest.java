package org.myshelf.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author moblaa
 */
public class StringUtilsTest {
    @Test
    public void testRepeat() throws Exception {
        Assert.assertEquals("HelloHello", StringUtils.repeat("Hello", 2));
    }

    @Test
    public void testZeroRepeat() {
        Assert.assertEquals("", StringUtils.repeat("Hello", 0));
    }

    @Test
    public void testEmptyRepeat() {
        Assert.assertEquals("", StringUtils.repeat("", 10));
    }

}