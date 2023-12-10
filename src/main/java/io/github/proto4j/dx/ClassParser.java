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
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.dex.cf.CfTranslator;

import java.io.IOException;
import java.io.InputStream;

/**
 * An abstract class used to parse raw .class files. Note that objects of this
 * class return {@code DirectClassFile} objects to be able to translate them
 * later with the {@link CfTranslator}.
 *
 * @see DirectClassFile
 */
public abstract class ClassParser implements DxVersion {

    /**
     * The library version.
     */
    protected final int version;

    /**
     * Indicates that this parse will perform a strict version check on the
     * input class-file. When enabled, a {@code ParseException} will be thrown
     * on malformed input.
     * <p>
     * This property is <code>false</code> by default.
     *
     * @see com.android.dx.dex.DexOptions#minSdkVersion
     */
    protected boolean useStrictMode;

    /**
     * The attribute factory to use
     */
    protected AttributeFactory attributeFactory;

    /**
     * Creates a new parser with the given library version
     *
     * @param version the version to use
     */
    protected ClassParser(int version) {
        this.version     = version;
        useStrictMode    = false;
        attributeFactory = StdAttributeFactory.THE_ONE;
    }

    /**
     * Reads all bytes from the given <code>InputStream</code> and parses the
     * class-file data into a <code>DirectClassFile</code> object.
     * <p>
     * Note that there may be a class-file version check before parsing the
     * data due to the {@link #useStrictMode} property. Implementations should
     * through a simple {@code ParseException} on failure.
     *
     * @param filename the class filename <b>with</b> .class at the end
     * @param inputStream the source to read the raw bytes from
     * @return the parsed class file as a <code>DirectClassFile</code> object
     * @throws IOException    if an error occurs while trying to read the input
     * @throws ParseException if an error occurs while parsing
     * @see #read(String, byte[])
     */
    public abstract DirectClassFile read(String filename, InputStream inputStream)
            throws IOException, ParseException;

    /**
     * Reads the input bytes and parses the class-file data into a
     * <code>DirectClassFile</code> object.
     * <p>
     * Note that there may be a class-file version check before parsing the
     * data due to the {@link #useStrictMode} property. Implementations should
     * through a simple {@code ParseException} on failure.
     *
     * @param filename the class filename <b>with</b> .class at the end
     * @param content the raw byte content
     * @return the parsed class file as a <code>DirectClassFile</code> object
     *
     * @throws ParseException if an error occurs while parsing
     */
    public abstract DirectClassFile read(String filename, byte[] content)
            throws ParseException;

    /**
     * Sets whether a class-file version check should be done before parsing.
     *
     * @param useStrictMode <code>true</code> if a version check should be done
     */
    public void setUseStrictMode(boolean useStrictMode) {
        this.useStrictMode = useStrictMode;
    }

    /**
     * Sets the attribute factory to use.
     *
     * @param attributeFactory the attribute factory to use.
     */
    public void setAttributeFactory(AttributeFactory attributeFactory) {
        this.attributeFactory = attributeFactory;
    }
}
