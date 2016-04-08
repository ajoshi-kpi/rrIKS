package app.frame;

import app.Constants;
import app.utils.FileHelper;
import app.utils.MathHelper;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FrameFactory implements Constants {

    private final List<String> letters;
    private final List<Long> starts;
    private final List<Long> ends;
    private List<Long> timesPressed;
    private List<Long> pauses;

    public FrameFactory(List<String> letters, List<Long> starts, List<Long> ends, List<Long> timesPressed, List<Long> pauses) {
        this.letters = letters;
        this.starts = starts;
        this.ends = ends;
        this.timesPressed = timesPressed;
        this.pauses = pauses;
    }

    public JFrame getLerningFrame() {
        JFrame jFrame = new JFrame();
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                calculateDifference();
                try {
                    FileHelper.appendToFile(TIMES_PRESSED_FILE_NAME, timesPressed);
                    FileHelper.appendToFile(PAUSES_FILE_NAME, pauses);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(50);
        textField.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                starts.add(new Date().getTime());
            }

            public void keyReleased(KeyEvent e) {
                String currentLetter = e.getKeyText(e.getKeyCode());
                letters.add(currentLetter);
                ends.add(new Date().getTime());
            }

            public void keyTyped(KeyEvent e) {

            }
        });
        panel.add(textField);
        jFrame.getContentPane().add(panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        return jFrame;
    }

    public JFrame getAuthenticationFrame() {
        JFrame jFrame = new JFrame();
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                calculateDifference();
                try {
                    FileHelper.appendToFile(TIMES_PRESSED_FILE_NAME, timesPressed);
                    FileHelper.appendToFile(PAUSES_FILE_NAME, pauses);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(50);
        textField.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                starts.add(new Date().getTime());
            }

            public void keyReleased(KeyEvent e) {
                String currentLetter = e.getKeyText(e.getKeyCode());
                letters.add(currentLetter);
                ends.add(new Date().getTime());
            }

            public void keyTyped(KeyEvent e) {

            }
        });
        panel.add(textField);
        jFrame.getContentPane().add(panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        return jFrame;
    }


    private void calculateDifference() {
        for (int i = 0; i < starts.size(); i++) {
            timesPressed.add(Math.abs(ends.get(i) - starts.get(i)));
        }
        for (int i = 0; i < starts.size() - 1; i++) {
            pauses.add(Math.abs(ends.get(i + 1) - starts.get(i)));
        }
    }
}
