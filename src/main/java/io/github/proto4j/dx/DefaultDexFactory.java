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

package io.github.proto4j.dx; //@date 28.01.2023

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.file.DexFile;
import io.github.proto4j.dx.file.DexInputStream;
import io.github.proto4j.dx.file.DexOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

final class DefaultDexFactory extends DexFactory {

    DefaultDexFactory() {super(DxVersion.DX16);}

    @Override
    public DexFile createDexFile() {
        return createDexFile(DxVersion.SDK26);
    }

    @Override
    public DexFile createDexFile(DexOptions dexOptions) {
        return new DexFile(dexOptions);
    }

    @Override
    public DexOptions createPreferredDexOptions() {
        return createDexOptions(DxVersion.SDK26);
    }

    @Override
    public DexOptions createDexOptions(int sdkVersion) {
        DexOptions options = new DexOptions(System.err);
        options.minSdkVersion = sdkVersion;
        return options;
    }

    @Override
    public DexOutputStream newOutputStream() {
        return newOutputStream(createDexFile(), createClassParser());
    }

    @Override
    public DexOutputStream newOutputStream(DexFile dexFile, ClassParser classParser) {
        return new DexOutputStream(dexFile, classParser);
    }

    @Override
    public ClassParser createClassParser() {
        return new DefaultClassParser();
    }

    @Override
    public DexInputStream newInputStream() {
        return new DexInputStream();
    }

    @Override
    public DexInputStream newInputStream(InputStream source, boolean close) throws IOException {
        return new DexInputStream(source, close);
    }

    private static class DefaultClassParser extends ClassParser {

        private DefaultClassParser() {super(DX16);}

        @Override
        public DirectClassFile read(String filename, InputStream inputStream) throws IOException, ParseException {
            Objects.requireNonNull(filename, "filename");
            Objects.requireNonNull(inputStream, "input");

            return read(filename, inputStream.readAllBytes());
        }

        @Override
        public DirectClassFile read(String filename, byte[] content) throws ParseException {
            DirectClassFile classFile = new DirectClassFile(content, filename, useStrictMode);
            classFile.setAttributeFactory(attributeFactory);
            classFile.getMagic();//parse
            return classFile;
        }
    }
}
