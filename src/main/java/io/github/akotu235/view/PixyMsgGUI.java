package io.github.akotu235.view;

import io.github.akotu235.pixymsg.PixelModifier;
import io.github.akotu235.pixymsg.PixelReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PixyMsgGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel addMsgPanel;
    private JPanel readMsgPanel;
    private JButton imageLoadButton;
    private JTextField msgLoadTextField;
    private JButton msgLoadButton;
    private JLabel label;
    private JTextArea msg;

    private PixelReader pixelReader;
    private PixelModifier pixelModifier;
    private String filePath;
    private String fileName;

    public PixyMsgGUI() {
        setTitle("PixyMsg");
        setSize(400, 120);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        addMsgPanel = new JPanel();
        readMsgPanel = new JPanel();
        label = new JLabel();
        imageLoadButton = new JButton("Load image");
        msgLoadTextField = new JTextField(20);
        msgLoadButton = new JButton("Hide text in image");
        msg = new JTextArea();

        imageLoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    String fileNameWithExtension = fileChooser.getSelectedFile().getName();

                    int index = fileNameWithExtension.lastIndexOf(".");
                    fileName = fileNameWithExtension.substring(0, index);

                    try {
                        pixelReader = new PixelReader(ImageIO.read(new File(filePath)));
                    } catch (IOException ex) {
                        readMsgPanel.setVisible(false);
                        addMsgPanel.setVisible(false);
                        throw new RuntimeException(ex);
                    }
                    if (pixelReader.hasMessage()) {
                        label.setText("Hidden message in " + fileName);
                        label.setForeground(Color.BLACK);
                        msg.setText(pixelReader.getMsg());
                        readMsgPanel.setVisible(true);
                        addMsgPanel.setVisible(false);
                    } else {
                        label.setText("No message");
                        label.setForeground(Color.BLACK);
                        addMsgPanel.setVisible(true);
                        readMsgPanel.setVisible(false);
                    }
                }
            }
        });

        msgLoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pixelModifier = new PixelModifier(pixelReader.getImage(), msgLoadTextField.getText());
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select directory to save the file");
                fileChooser.setApproveButtonText("Save");
                fileChooser.setCurrentDirectory(new File(filePath));
                fileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String directoryPath = selectedFile.getAbsolutePath();
                    File outputFile = new File(directoryPath + "\\" + fileName + "[PixyMsg].bmp");
                    try {
                        ImageIO.write(pixelModifier.getImage(), "bmp", outputFile);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (outputFile.exists()) {
                        label.setText("Created: " + fileName + "[PixyMsg].bmp");
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setText("Failed to hide text in " + fileName + " image");
                        label.setForeground(Color.RED);
                    }
                    label.setVisible(true);
                    addMsgPanel.setVisible(false);
                    readMsgPanel.setVisible(false);
                }
            }
        });

        mainPanel.setLayout(new BorderLayout());
        msg.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(msg);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(350, 100));
        scrollPane.setBorder(null);

        mainPanel.add(imageLoadButton, BorderLayout.NORTH);
        mainPanel.add(label, BorderLayout.SOUTH);
        mainPanel.add(addMsgPanel, BorderLayout.EAST);
        mainPanel.add(readMsgPanel, BorderLayout.CENTER);
        addMsgPanel.add(msgLoadTextField);
        addMsgPanel.add(msgLoadButton);
        readMsgPanel.add(scrollPane);

        addMsgPanel.setVisible(false);
        readMsgPanel.setVisible(false);

        add(mainPanel);
    }
}