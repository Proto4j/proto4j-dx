# Proto4j-dx

A module for working with the Android-dx tool. All actions and classes can be created by using an instance of the `DexFactory`. Releases published on this repository will be named according to the used Android-dx tool version. With this small library you can create Dex-files and read/import them.

**Important:** The released JAR files contain the dx-tool source code. Therefore, there is no need to include dependencies when using this library.

## Components

### `DexOutputStream`

Use a simple `DexOutputStream` to create a new `.dex` file. Before creating a new stream, make sure you have access to Java bytecode:

```java
DexFactory factory = DexFactory.getDefault();

try (DexOutputStream dos = factory.newDexOutputStream()) {
    dos.putNextClass("Foo.class");
    dos.write(fooClassBytes); // Java bytecode
    dos.closeClass();
    
    byte[] dexFile = dos.toByteArray();
    // The raw byte content can be transferred into 
    // an OutputStream directly:
    dos.transferTo(new FileOutputStream("classes.dex"));
}
```

### `DexInputStream`

Use a simple `DexInputStream` to read/import `*.dex` files. The usage is rather simple:

```java
DexFactory factory = DexFactory.getDefault();

try (DexInputStream dis = factory.newDexInputStream()) {
    // By adding 'true' to the method call, the provided
    // FileInputStream will be closed afterwards.
    dis.read(new FileInputStream("classes.dex"), true);
    
    Dex dexFile = dis.toDex();
}
```

## Download

Download the latest JAR file from the releases tab. This framework requires a minimum of Java 8+ for developing and running.

## License

    MIT License
    
    Copyright (c) 2023 Proto4j
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.