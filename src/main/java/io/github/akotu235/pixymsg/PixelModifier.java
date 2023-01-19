package io.github.akotu235.pixymsg;

import java.awt.image.BufferedImage;
import java.util.BitSet;

public class PixelModifier {
    private final BufferedImage image;
    private final BitSet bitsMsg;
    private final Coordinator coordinator;

    public PixelModifier(BufferedImage image, String msg) {
        this.image = image;
        byte[] bytes = msg.getBytes();
        this.bitsMsg = BitSet.valueOf(bytes);
        this.coordinator = new Coordinator(image.getWidth(), image.getHeight());
        insertMessage();
    }

    private void insertMessage() {
        setPreamble();
        setMsgLength();
        int index = 0;
        while (index < bitsMsg.length()) {
            int rgb = image.getRGB(coordinator.getX(), coordinator.getY());
            setPixel(coordinator.getX(), coordinator.getY(), rgb, index);
            coordinator.next();
            index++;
        }
    }

    private void setPixel(int x, int y, int rgb, int index) {
        if (!(Math.abs(rgb) % 2 == (bitsMsg.get(index) ? 1 : 0))) {
            image.setRGB(x, y, rgb + 1);
        }
    }

    private void setPreamble() {
        for (int i = 0; i < 10; i++) {
            if (Math.abs(image.getRGB(coordinator.getX(), coordinator.getY())) % 2 != i % 2) {
                updatePixel();
            }
            coordinator.next();
        }
    }

    private void setMsgLength() {
        boolean[] booleanArray = new boolean[32];
        for (int i = 0; i < 32; i++) {
            booleanArray[31 - i] = (bitsMsg.length() & (1 << i)) != 0;
        }
        for (int i = 0; i < 32; i++) {
            if (booleanArray[i]) {
                if (image.getRGB(coordinator.getX(), coordinator.getY()) % 2 == 0) {
                    updatePixel();
                }
            } else {
                if (Math.abs(image.getRGB(coordinator.getX(), coordinator.getY())) % 2 == 1) {
                    updatePixel();
                }
            }
            coordinator.next();
        }
    }

    private void updatePixel() {
        image.setRGB(coordinator.getX(), coordinator.getY(), (image.getRGB(coordinator.getX(), coordinator.getY()) + 1));
    }

    public BufferedImage getImage() {
        return image;
    }
}
