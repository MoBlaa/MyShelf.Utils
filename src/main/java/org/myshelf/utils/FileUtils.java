package org.myshelf.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * @author moblaa
 * @version 0.0.1-SNAPSHOT
 * @since 24.02.2017
 */
public final class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static void copyRecursively(boolean overwrite,
                                       @NotNull URI sourceURL,
                                       @NotNull URI targetURL) throws IOException, URISyntaxException {
        File sourceDirectory = new File(sourceURL);
        File targetDirectory = new File(targetURL);
        //Check for directories valid
        if (!sourceDirectory.exists() || !sourceDirectory.canRead())
            throw new IllegalArgumentException("Source directory doesn't exist or couldn't be read");
        //Check if exists/create if not
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdir())
                throw new IllegalStateException("Target directory doesn't exist and couldn't be written.");
        }
        //Check if directories aren't files
        if (!sourceDirectory.isDirectory())
            throw new IllegalArgumentException("Source isn't a directory");
        if (!targetDirectory.isDirectory())
            throw new IllegalArgumentException("Target isn't a directory");

        //Get Content files
        File[] sourceFiles = sourceDirectory.listFiles();
        if (sourceFiles == null) return;

        //create/overwrite recursively
        for (File sourceFile : sourceFiles) {
            String fileName = sourceFile.getName();
            File targetFile = new File(targetDirectory.getAbsolutePath() + File.separator + fileName);
            if (sourceFile.isDirectory()) {
                FileUtils.copyRecursively(overwrite, sourceFile.toURI(), targetFile.toURI());
                logger.debug("SUCCESS: Copied directory to <" + targetFile.getAbsolutePath() + ">");
            } else {
                FileUtils.copy(overwrite, sourceFile, targetFile);
            }
        }
    }

    public static void copy(boolean overwrite,
                            @NotNull File sourceFile,
                            @NotNull File targetFile) throws IOException {
        if (!sourceFile.exists())
            throw new IllegalArgumentException("Source file doesn't exist");
        if (!targetFile.exists()) {
            try {
                Files.copy(sourceFile.toPath(), targetFile.toPath());
                logger.debug("SUCCESS: Copied file to <" + targetFile.getAbsolutePath() + ">");
            } catch (IOException e) {
                logger.error("FAILURE: Failed to copy file <" + sourceFile.getAbsolutePath() + ">");
                throw e;
            }
        } else {
            if (overwrite) {
                try {
                    Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    logger.debug("SUCCESS: Overwritten file at <" + targetFile.getAbsolutePath() + ">");
                } catch (IOException e) {
                    logger.error("FAILURE: Failed to overwrite <" + targetFile.getAbsolutePath() + ">");
                    throw e;
                }
            }
        }
    }

    public static void deleteRecursively(@NotNull File file) {
        if (file.isFile()) {
            try {
                Files.delete(file.toPath());
                logger.debug("SUCCESS: Deleted file: <" + file.getAbsolutePath() + ">");
            } catch (IOException e) {
                logger.error("FAILURE: Couldn't delete file: <" + file.getAbsolutePath() + ">");
            }
        } else {
            File[] contents = file.listFiles();
            if (contents == null)
                throw new IllegalStateException("Couldn't find contents of directory: <" + file.getAbsolutePath() + ">");
            for (File content : contents) {
                FileUtils.deleteRecursively(content);
            }
            if (!file.delete())
                throw new IllegalArgumentException("Couldn't delete directory: <" + file.getAbsolutePath() + ">");
        }
    }
}
