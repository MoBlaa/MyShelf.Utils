package org.myshelf.tests;

import java.util.ServiceLoader;

/**
 * @author moblaa
 * @since 23.02.2017
 */
public class TestPlugin implements Plugin{

    public void test() {
        System.out.println("Testing plugins");
    }
}
