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

package io.github.proto4j.dx;//@date 28.01.2023

import com.android.dx.cf.direct.AttributeFactory;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.file.DexFile;
import io.github.proto4j.dx.file.DexInputStream;
import io.github.proto4j.dx.file.DexOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * <code>DexFactory</code> classes can be used to create objects of different
 * utility classes of the <code>proto4j-dx</code> module.
 *
 * @see DexOutputStream
 * @see DexInputStream
 * @see ClassParser
 */
public abstract class DexFactory {

    /**
     * The default factory instance. (lazily initialized)
     */
    private static DexFactory defaultFactory;

    /**
     * Returns the default <code>DexFactory</code> instance used for object
     * creation.
     *
     * @return the default factory instance
     */
    public static DexFactory getDefault() {
        if (defaultFactory == null) {
            // Lazily initialize the default factory instance
            synchronized (DexFactory.class) {
                defaultFactory = new DefaultDexFactory();
            }
        }
        return defaultFactory;
    }

    /**
     * The library version to use
     */
    protected final int version;

    /**
     * Creates a new <code>DexFactory</code> with the given library version.
     *
     * @param version the version to use
     */
    protected DexFactory(int version) {this.version = version;}

    /**
     * Creates a new empty <code>DexFile</code> with the default SDK version,
     * which is {@link DxVersion#SDK13}.
     *
     * @return a new empty <code>DexFile</code>
     * @see #createDexFile(int)
     */
    public DexFile createDexFile() {
        return createDexFile(DxVersion.SDK13);
    }

    /**
     * Creates a new empty <code>DexFile</code> with the provided minimum
     * Android SDK version
     *
     * @param sdkVersion the SDK version to use
     * @return a new empty <code>DexFile</code>
     * @see #createDexFile(DexOptions)
     */
    public DexFile createDexFile(int sdkVersion) {
        return createDexFile(createDexOptions(sdkVersion));
    }

    /**
     * Creates a new empty <code>DexFile</code> with the provided options. These
     * options usually provide an <code>PrintWriter</code> for error reporting
     * and the minimum required SDK version.
     *
     * @param dexOptions the options to use
     * @return a new empty <code>DexFile</code>
     * @see DexFile
     * @see DexOptions
     */
    public abstract DexFile createDexFile(DexOptions dexOptions);

    /**
     * Creates a new instance of <code>DexOptions</code> with the preferred
     * minimum Android SDK version.
     *
     * @return the newly created options
     */
    public abstract DexOptions createPreferredDexOptions();

    /**
     * Creates a new instance of <code>DexOptions</code> with the provided
     * minimum Android SDK version.
     *
     * @param sdkVersion the SDK version to use
     * @return the newly created options
     */
    public abstract DexOptions createDexOptions(int sdkVersion);

    /**
     * Creates a new simple <code>DexOutputStream</code> with a default DexFile
     * returned from {@link #createDexFile()}.
     *
     * @return the newly created output stream
     */
    public abstract DexOutputStream newOutputStream();

    /**
     * Creates a new <code>DexOutputStream</code> with the provided dex file.
     *
     * @param dexFile the file to use
     * @return the newly created output stream
     */
    public DexOutputStream newOutputStream(DexFile dexFile) {
        return newOutputStream(dexFile, null);
    }

    /**
     * Creates a new <code>DexOutputStream</code> with the provided dex file
     * and <code>ClassParser</code>.
     *
     * @param dexFile the file to use
     * @param classParser the parser to use
     * @return the newly created output stream
     */
    public abstract DexOutputStream newOutputStream(DexFile dexFile, ClassParser classParser);

    /**
     * Creates a new simple <code>DexInputStream</code> with no input to read
     * from.
     *
     * @return the newly created input stream
     */
    public abstract DexInputStream newInputStream();

    /**
     * Creates a new simple <code>DexInputStream</code> with an input stream
     * to read from. This method does not close the provided source stream.
     *
     * @param source the <code>InputStream</code> to read from
     * @return the newly created input stream with all bytes from the source
     *         stream
     */
    public DexInputStream newInputStream(InputStream source) throws IOException {
        return newInputStream(source, false);
    }

    /**
     * Creates a new simple <code>DexInputStream</code> with an input stream
     * to read from.
     *
     * @param close whether the provided <code>InputStream</code> should be
     *         closed after all bytes have been read.
     * @param source the <code>InputStream</code> to read from
     * @return the newly created input stream with all bytes from the source
     *         stream
     */
    public abstract DexInputStream newInputStream(InputStream source, boolean close) throws IOException;

    /**
     * Creates a new <code>ClassParser</code> with its default options.
     *
     * @return the newly created parser
     */
    public abstract ClassParser createClassParser();

    /**
     * Creates a new <code>ClassParser</code> with the provided attribute factory
     * to use.
     *
     * @param attributeFactory the factory to use
     * @return the newly created parser
     */
    public ClassParser createClassParser(AttributeFactory attributeFactory) {
        Objects.requireNonNull(attributeFactory);

        ClassParser parser = createClassParser();
        parser.setAttributeFactory(attributeFactory);
        return parser;
    }

}
