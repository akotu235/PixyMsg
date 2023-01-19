package io.github.akotu235.pixymsg;

class Coordinator {
    private int x;
    private int y;
    private final int width;
    private final int height;

    Coordinator(final int width, final int height) {
        this.x = 1;
        this.y = 0;
        this.width = width;
        this.height = height;
    }

    void next() {
        if (x < width) {
            x += 1;
        } else if (y < height) {
            y += 1;
            x = 0;
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