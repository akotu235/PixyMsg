package io.github.akotu235;

import io.github.akotu235.pixymsg.PixelModifier;
import io.github.akotu235.pixymsg.PixelReader;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input file: ");
        String inputFilePath = scanner.nextLine();

        System.out.print("Message: ");
        String msg = scanner.nextLine();

        String outputFileName = "image";
        outputFileName += ".bmp";

        //Creating an image with a hidden message.
        PixelModifier pixelModifier = new PixelModifier(ImageIO.read(new File(inputFilePath)), msg);

        //Saving the picture.
        File outputfile = new File(outputFileName);
        ImageIO.write(pixelModifier.getImage(), "bmp", outputfile);

        //Reading a hidden message.
        PixelReader pixelReader = new PixelReader(ImageIO.read(new File(outputFileName)));
        System.out.println("Hidden message:");
        System.out.println(pixelReader.getMsg());
    }
}