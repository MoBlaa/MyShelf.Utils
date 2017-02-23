package org.myshelf.utils.plugins;

import org.myshelf.tests.Plugin;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author moblaa
 * @since 23.02.2017
 */
public class PluginLoaderTest {

    private PluginLoader loader;

    @BeforeTest
    public void setUp() {
        loader = new PluginLoader();
    }

    @Test
    public void testTest1() throws Exception {
        Iterable<Plugin> plugins = loader.loadPlugins(Plugin.class);
        Assert.assertTrue(plugins.iterator().hasNext());
    }
}