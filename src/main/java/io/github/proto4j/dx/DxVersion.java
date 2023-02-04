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

/**
 * Public version definitions of this library.
 */
public interface DxVersion {

    /**
     * Specifies the minimum required Android SDK version when creating the
     * dex-files. This version is set by default on any <code>ClassParser</code>
     * created by the default <code>DexFactory</code>.
     *
     * @see DexFactory
     */
    int SDK13 = 13;

    /**
     * Specifies the preferred Android SDK API level.
     * <p>
     * A <code>ClassParser</code> with the preferred version number can be
     * retrieved by creating the preferred <code>DexOptions</code> with
     * {@link DexFactory#createPreferredDexOptions()}.
     *
     * @see DexFactory
     */
    int SDK26 = 26;

    /**
     * Specifies the version of the Android-dx tool. (1.16)
     */
    int DX16 = "1.16".hashCode();
}
