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

/**
 * <b>proto4j-dx</b> <p>
 * A module for working with the Android-dx tool. All actions and classes
 * can be created by using an instance of the {@code DexFactory}.
 * <ul>
 * <li>
 *     <code>DexOutputStream</code>: A class with <code>OutputStream</code>
 *     logic and the behaviour of a <code>ZipOutputStream</code>. An example
 *     usage could be the following:
 *     <pre>
 *  DexFactory factory = DexFactory.getDefault();
 *  try (DexOutputStream dos = factory.newDexOutputStream()) {
 *      dos.putNextClass("Foo.class");
 *      dos.write(fooClassBytes); // Java bytecode
 *      dos.closeClass();
 *
 *      byte[] dexFile = dos.toByteArray();
 *  }
 *     </pre>
 * </li>
 * <li>
 *     <code>DexInputStream</code>: A class with <code>InputStream</code>
 *     logic. It can be used to read '.dex' files stored in the filesystem:
 *     <pre>
 *  DexFactory factory = DexFactory.getDefault();
 *  try (DexInputStream dis = factory.newDexInputStream()) {
 *      // with 'true' the provided InputStream will be closed
 *      // after reading all bytes
 *      dis.read(new FileInputStream("classes.dex"), true);
 *
 *      Dex dexFile = dis.toDex();
 *  }
 *     </pre>
 * </li>
 * <li>
 *     <code>ClassParser</code>: Parsers are used to read Java bytecode in
 *     order to add a returned <code>DirectClassFile</code> to an internal
 *     <code>DexFile</code>.
 * </li>
 * </ul>
 *
 * @see io.github.proto4j.dx.DexFactory
 **/
package io.github.proto4j.dx;