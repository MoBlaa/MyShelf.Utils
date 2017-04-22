package org.myshelf.utils;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

/**
 * @author moblaa
 * @version 0.0.1-SNAPSHOT
 * @since 25.02.2017
 */
public class FileUtilsTest {

    private File target;
    private File source;

    @Test
    public void testRecursiveCopy() throws Exception {
        URL url = getClass().getClassLoader().getResource("templates");

        Assert.assertNotNull(url);

        source = new File(url.toURI());

        Assert.assertTrue(source.exists());
        Assert.assertTrue(source.canRead());
        Assert.assertTrue(source.isDirectory());

        target = new File(System.getProperty("user.home") + File.separator + source.getName());

        Assert.assertFalse(target.exists());

        FileUtils.copyRecursively(false, source.toURI(), target.toURI());

        Assert.assertTrue(Files.exists(new File(target, "example_test.md").toPath()));
        Assert.assertTrue(Files.exists(new File(target, "example_style.css").toPath()));
        Assert.assertTrue(Files.exists(new File(target, "org").toPath()));

        File subOrg = new File(target, "org");

        Assert.assertTrue(Files.exists(new File(subOrg, "example_html.html").toPath()));
        Assert.assertTrue(Files.exists(new File(subOrg, "myshelf").toPath()));
        Assert.assertTrue(Files.exists(new File(new File(subOrg, "myshelf"), "tests").toPath()));

        FileUtils.deleteRecursively(target);

        Assert.assertFalse(target.exists());
    }

    @Test
    public void testCopy() throws Exception {
        URL url = getClass().getClassLoader().getResource("test.properties");

        Assert.assertNotNull(url);

        source = new File(url.toURI());

        Assert.assertNotNull(source);
        Assert.assertTrue(source.exists());
        Assert.assertTrue(source.canRead());

        target = new File(System.getProperty("user.home") + File.separatorChar + source.getName());

        Assert.assertFalse(target.exists());
        FileUtils.copy(false, source, target);

        Assert.assertTrue(target.exists());

        List<String> content = Files.readAllLines(source.toPath());
        List<String> targetContent = Files.readAllLines(target.toPath());

        Assert.assertEquals(content, targetContent);
    }

    @Test
    public void testOverwriteCopy() throws Exception {
        this.testCopy();

        Assert.assertNotNull(target);
        Assert.assertNotNull(source);
        Assert.assertTrue(source.exists());
        Assert.assertTrue(target.exists());

        List<String> oldContent = Files.readAllLines(source.toPath());

        URL otherSource = getClass().getClassLoader().getResource("templates/example_test.md");

        Assert.assertNotNull(otherSource);

        source = new File(otherSource.toURI());

        FileUtils.copy(true, source, target);

        List<String> sourceContent = Files.readAllLines(source.toPath());
        List<String> targetContent = Files.readAllLines(target.toPath());

        Assert.assertNotEquals(oldContent, targetContent);
        Assert.assertEquals(sourceContent, targetContent);
    }

    @AfterMethod
    public void tearDown() throws IOException {
        if (target != null && target.exists()) {
            FileUtils.deleteRecursively(target);
        }
        target = null;
        source = null;
    }

}