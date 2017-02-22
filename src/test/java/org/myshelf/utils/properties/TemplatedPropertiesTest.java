package org.myshelf.utils.properties;


import org.myshelf.utils.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author moblaa
 * @since 2017/02/22
 */
public class TemplatedPropertiesTest {

    private static final String FIRST_TEST_KEY = "FirstTest";
    private static final String SECOND_TEST_KEY = "SecondTest";
    private static final String THIRD_TEST_KEY = "ThirdTest";
    private static final String FOURTH_TEST_KEY = "ChainOne";

    private static final String PLACEHOLDER_ONE = "PlaceholderOne";

    private static TemplatedProperties loader;

    @BeforeTest
    public void setUp() throws IOException {
        loader = TemplatedProperties.load(TemplatedPropertiesTest.class.getClassLoader().getResourceAsStream("test.properties"));
    }

    @Test
    public void singleRelaceTest() throws Exception {
        Assert.assertEquals(loader.get(FIRST_TEST_KEY), loader.get(PLACEHOLDER_ONE));
    }

    @Test
    public void doubleReplaceTest() {
        Assert.assertEquals(loader.get(SECOND_TEST_KEY), StringUtils.repeat(loader.get(PLACEHOLDER_ONE), 2));
    }

    @Test
    public void inTextTest() {
        String expected = "I like the First Test but i think the Second Test is also a nice one.";
        Assert.assertEquals(loader.get(THIRD_TEST_KEY), expected);
    }

    @Test
    public void multiples() {
        String expected = "Circular: Circular: Circle finished";
        Assert.assertEquals(loader.get(FOURTH_TEST_KEY), expected);
    }
}