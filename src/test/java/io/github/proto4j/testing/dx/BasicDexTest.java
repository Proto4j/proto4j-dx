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

package io.github.proto4j.testing.dx; //@date 28.01.2023

import com.android.dex.Dex;
import io.github.proto4j.dx.DexFactory;
import io.github.proto4j.dx.file.DexInputStream;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

public class BasicDexTest {

    @Test
    public void testFactory() throws IOException {
        DexFactory factory = DexFactory.getDefault();

        try (DexInputStream dis = factory.newInputStream()) {
            dis.read(new FileInputStream("classes.dex"), true);

            Dex dex = dis.toDex();
        } catch (IOException e) {

        }
    }
}
