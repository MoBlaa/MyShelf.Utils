package org.myshelf.utils.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * Wrapper class for the ServiceLoader utility of default java api.
 * Uses custom {@link URLClassLoader}s and the default classloader to load Plugin classes.
 *
 * @author moblaa
 * @since 23.02.2017
 */
public class PluginLoader {

    private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private final Stream<URI> plugins;

    /**
     * Initializes a PluginLoader with the `jar`-Files inside the `pluginDirectory`.
     * If pluginDirectory is empty or null the default classloader will be used for the ServiceLoader.
     *
     * @param pluginDirectory Directory containing the jar-Files with plugin classes.
     */
    public PluginLoader(File pluginDirectory) {
        this(pluginDirectory == null ? null : pluginDirectory.listFiles(pathname -> pathname.getName().endsWith(".jar")));
    }

    /**
     * Initializes a PluginLoader with the given `jar`-Files.
     *
     * @param pluginJars Jar files to search for plugins.
     * @throws IllegalArgumentException Will be thrown if pluginJars is null
     */
    public PluginLoader(File... pluginJars) {
        if (pluginJars == null) {
            throw new IllegalArgumentException("Plugin directory has to be a valid directory and readable");
        }
        Stream<File> filtered = Arrays.stream(pluginJars).filter(file -> {
            if (file == null) {
                logger.info("Found a null file; continuing");
            } else if (!file.canRead()) {
                logger.info("Found a not readable file: <" + file.getPath() + ">; excluding file from serviceloader");
                return false;
            }
            return true;
        });
        this.plugins = filtered.map(File::toURI);
    }

    /**
     * Loads the implementing plugin classes for the given `pluginsToLoad` class.
     *
     * @param pluginsToLoad Class to load implementations/subclasses from.
     * @param <T> Interface/class to load plugins for.
     * @return Plugins found in the classpath and given files.
     */
    public <T> Iterable<T> loadPlugins(Class<T> pluginsToLoad) {
        //Converting uri's to urls
        Stream<URL> url = this.plugins.flatMap(uri -> {
            try {
                return Arrays.stream(new URL[]{uri.toURL()});
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        });
        //Loading services
        URLClassLoader classloader = new URLClassLoader(url.toArray(URL[]::new), getClass().getClassLoader());
        return ServiceLoader.load(pluginsToLoad, classloader);
    }

}
