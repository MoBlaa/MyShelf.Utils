package org.myshelf.utils.properties;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream representing a state machine (right sided grammar).
 * This state machine has 4 States:
 * 1. WAITING: No Matched start pattern was found, not inside a pattern.
 * 2. PRE_MATCHED: The x-th character of the pattern was found.
 * 3. MATCHED: The second character of the start pattern found, inside a match.
 * 4. PRE_END: The x-th character of the end pattern was found, waiting for the end of the pattern.
 *
 * @author moblaa
 * @version 0.0.1-SNAPSHOT
 * @since 02.03.2017
 */
public class TemplateFilterStream extends FilterInputStream {

    /**
     *
     */
    private enum STATE {WAITING, PRE_MATCHED, MATCHED, PRE_END}

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected TemplateFilterStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }
}
