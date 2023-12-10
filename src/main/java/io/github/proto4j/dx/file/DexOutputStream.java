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

package io.github.proto4j.dx.file;//@date 28.01.2023

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.command.dexer.DxContext;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import io.github.proto4j.dx.ClassParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Use a simple <code>DexOutputStream</code> to create a new *.dex file. Before
 * creating a new stream, make sure you have access to Java bytecode.
 * <pre>
 * DexFactory factory = DexFactory.getDefault();
 *
 * try (DexOutputStream dos = factory.newDexOutputStream()) {
 *     dos.putNextClass("Foo.class");
 *     dos.write(fooClassBytes); // Java bytecode
 *     dos.closeClass();
 *
 *     byte[] dexFile = dos.toByteArray();
 *     // The raw byte content can be transferred into
 *     // an OutputStream directly:
 *     dos.transferTo(new FileOutputStream("classes.dex"));
 * }
 * </pre>
 *
 * @see DexInputStream
 */
public class DexOutputStream extends OutputStream {

    /**
     * The file that will store all internal class entries.
     */
    private final DexFile file;

    /**
     * The Java bytecode parser.
     */
    private final ClassParser classParser;

    /**
     * The context to use when translating.
     */
    private final DxContext dxContext;

    /**
     * The entry's name.
     */
    private String filename;

    /**
     * The translation options.
     */
    private CfOptions cfOptions;

    /**
     * Indicates whether a class file is currently set as the next entry.
     */
    private boolean entrySet = false;

    /**
     * Creates a new <code>DexOutputStream</code> with the given dex-file and
     * class parser.
     *
     * @param file the internal file to use
     * @param classParser the parser to use
     */
    public DexOutputStream(DexFile file, ClassParser classParser) {
        this.file        = file;
        this.classParser = classParser;
        this.dxContext   = new DxContext();
    }

    /**
     * Not supported by this <code>OutputStream</code> class.
     */
    @Override
    public void write(int b) {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes all bytes from the given input. The source stream won't be closed
     * afterwards.
     *
     * @param inputStream the source to read from
     * @throws IOException if an I/O Error occurs
     */
    public void write(InputStream inputStream) throws IOException {
        write(inputStream, false);
    }

    /**
     * Writes all bytes from the given input.
     *
     * @param inputStream the source to read from
     * @param close whether the provided <code>InputStream</code> should be
     *         closed afterwards
     * @throws IOException if an I/O Error occurs
     */
    public void write(InputStream inputStream, boolean close) throws IOException {
        write(inputStream.readAllBytes());
        if (close) {
            inputStream.close();
        }
    }

    /**
     * Writes the provided class data.
     *
     * @param b the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IllegalStateException if {@link #putNextClass(String)} has not
     *                               been called yet
     */
    @Override
    public void write(byte[] b, int off, int len) {
        byte[] data = Arrays.copyOfRange(b, off, off + len);

        if (data.length == 0) {
            return;
        }

        if (filename == null || cfOptions == null || !entrySet) {
            throw new IllegalStateException("write() call before putNextClass()");
        }

        try {
            DirectClassFile dcf = classParser.read(filename, data);
            ClassDefItem cdi = CfTranslator.translate(
                    dxContext, dcf, data, cfOptions, file.getDexOptions(), file);

            file.add(cdi);
        } catch (ParseException e) {
            DexOptions dexOptions = file.getDexOptions();
            dexOptions.err.println("Error processing: " + e);
        } finally {
            Arrays.fill(data, (byte) 0);
        }
    }

    /**
     * Writes all class files that are stored in the given input zip-file
     *
     * @param inputStream the source stream
     * @throws IOException if an I/O Error occurs
     */
    public void writeAll(ZipInputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);

        ZipEntry entry = null;
        while ((entry = inputStream.getNextEntry()) != null) {
            if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                continue;
            }

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                int    len;
                byte[] buf = new byte[2048];
                while ((len = inputStream.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }

                putNextClass(entry.getName());
                write(bos.toByteArray());
                closeClass();
            }
        }
    }

    /**
     * Writes the current {@link DexFile} to the given output.
     *
     * @param outputStream the destination
     * @throws IOException if an I/O Error occurs
     */
    public void transferTo(OutputStream outputStream) throws IOException {
        file.writeTo(outputStream, null, false);
    }

    /**
     * Starts the next class entry.
     *
     * @param filename the filename to use
     */
    public void putNextClass(String filename) {
        putNextClass(filename, this.cfOptions == null ? new CfOptions() : null);
    }

    /**
     * Starts the next class entry.
     *
     * @param filename the filename to use
     * @param options the translation options to use
     */
    public void putNextClass(String filename, CfOptions options) {
        if (entrySet) {
            throw new IllegalStateException("putNextClass() called before closeClass()");
        }
        this.filename = filename;
        if (options != null && options != this.cfOptions) {
            this.cfOptions = options;
        }
        this.entrySet = true;
    }

    /**
     * Closes the current class entry
     */
    public void closeClass() {
        if (!entrySet) {
            throw new IllegalStateException("closeClass() called before putNextClass()");
        }
        filename = null;
        entrySet = false;
    }

    /**
     * Converts the internal dex-file into a byte array.
     *
     * @return the raw dex-file data
     */
    public byte[] toByteArray() {
        try {
            return file.toDex(null, false);
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
