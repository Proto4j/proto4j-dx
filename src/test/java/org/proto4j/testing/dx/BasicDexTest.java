package org.proto4j.testing.dx; //@date 28.01.2023

import com.android.dex.Dex;
import org.junit.jupiter.api.Test;
import org.proto4j.dx.DexFactory;
import org.proto4j.dx.file.DexInputStream;

import java.io.FileInputStream;
import java.io.IOException;

public class BasicDexTest {

    @Test
    public void testFactory() throws IOException {
        DexFactory factory = DexFactory.getDefault();

        try (DexInputStream dis = factory.newInputStream()) {
            dis.read(new FileInputStream("classes.dex"), true);

            Dex dex = dis.toDex();
        }
    }
}
