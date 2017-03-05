package org.myshelf.utils.properties;

import com.sun.javafx.fxml.PropertyNotFoundException;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by morit on 21.02.2017.
 */
public class TemplatedProperties extends HashMap<String, String> {

    final String MATCH_PATTERN_START;
    final String MATCH_PATTERN_END;
    private final Pattern PLACEHOLDER_PATTERN;

    private static final String START_PATTERN_CONFIG_KEY = "templates.pattern.start";
    private static final String END_PATTERN_CONFIG_KEY = "templates.pattern.end";
    private static final String INCOMPLETE_TEMPLATE_PROPERTIES_MSSG =
            "The System properties have to contain both or non of the templates.pattern keys";

    private final Hashtable<String, String> unresolved;

    private TemplatedProperties() {
        super();
        Properties props = System.getProperties();
        props.stringPropertyNames().forEach((key) -> super.put(key, String.valueOf(props.getProperty(key))));
        unresolved = new Hashtable<>();

        if (this.containsKey(START_PATTERN_CONFIG_KEY)) {
            if (this.containsKey(END_PATTERN_CONFIG_KEY)) {
                this.MATCH_PATTERN_START = super.get(START_PATTERN_CONFIG_KEY);
                this.MATCH_PATTERN_END = super.get(END_PATTERN_CONFIG_KEY);
            } else {
                throw new PropertyNotFoundException(INCOMPLETE_TEMPLATE_PROPERTIES_MSSG);
            }
        } else {
            if (this.containsKey(END_PATTERN_CONFIG_KEY))
                throw new PropertyNotFoundException(INCOMPLETE_TEMPLATE_PROPERTIES_MSSG);

            Pattern.compile("\\{\\{(\\w+)}}");
        }

    }

    public static TemplatedProperties load(InputStream in) throws IOException {
        Properties props = new Properties();
        props.load(in);
        return load(props);
    }

    public static TemplatedProperties load(File propertiesFile) throws IOException {
        return load(new FileInputStream(propertiesFile));
    }

    public static TemplatedProperties load(Properties props) {
        TemplatedProperties loader = new TemplatedProperties();
        loader.reloadProperties(props);
        return loader;
    }

    @Override
    public synchronized String put(String key, String value) {
        String result;
        if (PLACEHOLDER_PATTERN.matcher(value).matches()) {
            if (super.containsKey(key)) {
                result = super.remove(key);
                this.unresolved.put(key, value);
            } else {
                result = this.unresolved.put(key, value);
            }

            this.reloadProperties(this.unresolved);
        } else {
            //Add to completed values
            result = super.put(key,value);
        }
        return result;
    }

    private void reloadProperties(final Map<?, ?> props) {
        //Iterate over all properties
        props.keySet().forEach((key) -> super.put((String) key, this.getProperty((String) key, props)));
    }

    private String getProperty(String key, Map<?, ?> raw) {
        //Already processed
        if (super.containsKey(key)) return super.get(key);
        if (this.unresolved.containsKey(key)) throw new IllegalArgumentException("Circular dependency: " + key);

        String val = (String) raw.get(key);

        //Invalid
        if (val == null) return null;

        //For detecting loops
        this.unresolved.put(key, val);

        //Build
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(val);
        StringBuilder builder = new StringBuilder(val.length());
        //Append from placeholder to placeholder
        int index = 0;
        boolean resolved = true;
        while (matcher.find()) {
            //Replace placeholder if possible
            int start = matcher.start();
            int end = matcher.end();
            String templateKey = val.substring(start + PLACEHOLDER_START_LENGTH,
                                                end - PLACEHOLDER_END_LENGTH);
            //Recursive call for nested property
            String templateValue = this.getProperty(templateKey, raw);
            //Insert
            if (templateValue == null) {
                resolved = false;
                builder.append(val.substring(index, end));
            } else {
                builder.append(val.substring(index, start));
                builder.append(templateValue);
            }
            index = end;
        }
        //Add the tail
        builder.append(val.substring(index));
        String result = builder.toString();
        if (resolved)  {
            this.unresolved.remove(key);
            super.put(key, result);
        }
        return result;
    }

    public Properties toProperties() {
        Properties props = new Properties();
        props.putAll(this);
        return props;
    }
}
