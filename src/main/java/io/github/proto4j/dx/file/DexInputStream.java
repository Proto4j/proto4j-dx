/*
 * MIT License
 *
 * Copyright (c) 2023 Proto4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.proto4j.dx.file; //@date 28.01.2023

import com.android.dex.Dex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Use a simple <code>DexInputStream</code> to read/import "*.dex" files. The
 * usage is rather simple:
 * <pre>
 * DexFactory factory = DexFactory.getDefault();
 *
 * try (DexInputStream dis = factory.newDexInputStream()) {
 *     // By adding 'true' to the method call, the provided
 *     // FileInputStream will be closed afterwards.
 *     dis.read(new FileInputStream("classes.dex"), true);
 *
 *     Dex dexFile = dis.toDex();
 * }
 * </pre>
 *
 * @see DexOutputStream
 */
public class DexInputStream extends InputStream {

    /**
     * Internal buffer that stores all bytes.
     */
    private final ByteArrayOutputStream buffer;

    /**
     * Creates a new <code>DexInputStream</code> with no underlying source.
     */
    public DexInputStream() {
        this.buffer = new ByteArrayOutputStream();
    }

    /**
     * Creates a new <code>DexInputStream</code> and reads all bytes from the
     * given source stream.
     *
     * @param source the source to read from
     * @param close whether the provided <code>InputStream</code> should be
     *         closed afterwards
     * @throws IOException if an I/O Error occurs
     */
    public DexInputStream(InputStream source, boolean close) throws IOException {
        this.buffer = new ByteArrayOutputStream();
        if (source != null) {
            read(source, close);
        }
    }

    /**
     * Not supported by this <code>InputStream</code> class.
     */
    @Override
    public int read() {
        throw new UnsupportedOperationException();
    }

    /**
     * Reads the data from the given input stream and writes it to the internal
     * buffer. This action won't close the provided stream.
     *
     * @param inputStream the source to read from
     * @throws IOException if an I/O Error occurs
     */
    public void read(InputStream inputStream) throws IOException {
        read(inputStream, false);
    }

    /**
     * Reads the data from the given input stream and writes it to the internal
     * buffer.
     *
     * @param inputStream the source to read from
     * @param close whether the provided <code>InputStream</code> should be
     *         closed afterwards
     * @throws IOException if an I/O Error occurs
     */
    public void read(InputStream inputStream, boolean close) throws IOException {
        Objects.requireNonNull(inputStream, "source");

        if (buffer.size() != 0) {
            buffer.reset();
        }

        inputStream.transferTo(buffer);
        if (close) {
            inputStream.close();
        }
    }

    /**
     * Copies data from the internal buffer into the given byte array.
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset in array <code>b</code>
     *         at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the amount of bytes stored.
     * @throws IOException              if the internal buffer is empty
     * @throws IllegalArgumentException if the offset or length parameter
     *                                  is negative
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (buffer.size() == 0) {
            throw new IOException("Empty source");
        }

        if (off < 0 || len < 0) {
            throw new IllegalArgumentException("Illegal negative number");
        }

        byte[] buf    = buffer.toByteArray();
        int    length = Math.min(off + len, buf.length);

        System.arraycopy(buf, 0, b, off, length);
        return length;
    }

    /**
     * Returns the internal buffer size.
     *
     * @return the current size of the internal buffer
     */
    public int size() {
        return buffer.size();
    }

    /**
     * Resets the internal buffer.
     */
    @Override
    public synchronized void reset() {
        buffer.reset();
    }

    /**
     * Converts the internal buffer into a {@link Dex} object.
     *
     * @return the newly created object
     * @throws IOException if the stored buffer contains malformed data
     */
    public final Dex toDex() throws IOException {
        if (buffer.size() == 0) {
            throw new IOException("Empty source");
        } else {
            return new Dex(buffer.toByteArray());
        }
    }

}
