package io.github.akotu235.pixymsg;

class Coordinator {
    private int x = 0;
    private int y = 0;
    private final int width;
    private final int height;

    Coordinator(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    void next() {
        if (x < width) {
            x += 1;
        } else if (y < height) {
            y += 1;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }
}