package io.github.akotu235.pixymsg;

import java.awt.image.BufferedImage;
import java.util.BitSet;

public class PixelReader {
    private final BufferedImage image;

    public PixelReader(BufferedImage image) {
        this.image = image;
    }

    public String getMsg() {
        Coordinator coordinator = new Coordinator(image.getWidth(), image.getHeight());

        //Preamble check.
        /*TODO: Do something about this ugly exception.. */
        for (int i = 0; i < 10; i++) {
            if (Math.abs(image.getRGB(coordinator.getX(), coordinator.getY())) % 2 == 0) {
                throw new RuntimeException();
            }
            coordinator.next();
        }

        //Length reading.
        boolean[] booleanArray = new boolean[32];
        for (int i = 0; i < 32; i++) {
            booleanArray[i] = Math.abs(image.getRGB(coordinator.getX(), coordinator.getY())) % 2 != 0;
            coordinator.next();
        }
        int length = 0;
        for (int i = 0; i < 32; i++) {
            if (booleanArray[i]) {
                length |= 1 << (31 - i);
            }
        }

        //Reading messages
        BitSet bitMsg = new BitSet();
        for (int i = 0; i < length; i++) {
            if (Math.abs(image.getRGB(coordinator.getX(), coordinator.getY())) % 2 == 1) {
                bitMsg.set(i);
            }
            coordinator.next();
        }
        return new String(bitMsg.toByteArray());
    }

    public boolean hasMessage() {
        Coordinator coordinator = new Coordinator(image.getWidth(), image.getHeight());

        //Preamble check.
        for (int i = 0; i < 10; i++) {
            if (Math.abs(image.getRGB(coordinator.getX(), coordinator.getY())) % 2 == 0) {
                return false;
            }
            coordinator.next();
        }
        return true;
    }

    public BufferedImage getImage() {
        return image;
    }
}
