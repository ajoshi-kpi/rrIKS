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
import java.util.Date;
import java.util.List;

public class FrameFactory implements Constants {

    private static final String PASSWORD_FILE_NAME = "storage/password.txt";

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
                    FileHelper.appendToFile(TIMES_PRESSED_FILE_NAME, calculateSuitable(timesPressed));
                    FileHelper.appendToFile(PAUSES_FILE_NAME, calculateSuitable(pauses));
                    savePassword();
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
                    if (isCorrectPassword()) {
                        try {
                            if (isValid()) {
                                FileHelper.appendToFile(TIMES_PRESSED_FILE_NAME, calculateSuitable(timesPressed));
                                FileHelper.appendToFile(PAUSES_FILE_NAME, calculateSuitable(pauses));
                                System.out.println("VALID");
                            } else {
                                System.out.println("NOT VALID");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        System.out.println("Password is incorrect");
                    }
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

    private List<Long> calculateSuitable(List<Long> originList) {
        List<Long> result = new ArrayList<>();
        for (Long currentElement : originList) {
            List<Long> currentList = new ArrayList<>();
            for (Long el : originList) {
                currentList.add(el);
            }
            currentList.remove(currentElement);
            double expectation = MathHelper.calculateExpectation(currentList);
            double dispersion = MathHelper.calculateDispersion(currentList);
            double currentStudentCriterion = Math.abs((currentElement - expectation) / dispersion);

            if (currentStudentCriterion > Constants.studentCriterion(originList.size())) {
                System.out.println("Value " + currentElement + " ignored");
            } else {
                result.add(currentElement);
            }
        }
        return result;
    }

    private boolean isValid() throws IOException {
        List<Long> defaultTimesPressed = FileHelper.readFromFile(TIMES_PRESSED_FILE_NAME);
        List<Long> defaultPauses = FileHelper.readFromFile(PAUSES_FILE_NAME);

        double defaultPausesDispersion = MathHelper.calculateDispersion(defaultPauses);
        double currentPausesDispersion = MathHelper.calculateDispersion(pauses);

        double pausesMaxDispersion = Math.max(Math.pow(defaultPausesDispersion, 2.0), Math.pow(currentPausesDispersion, 2.0));
        double pausesMinDispersion = Math.min(Math.pow(defaultPausesDispersion, 2.0), Math.pow(currentPausesDispersion, 2.0));
        double fisherCriterion = pausesMaxDispersion / pausesMinDispersion;

        if (fisherCriterion > Constants.fisherCriterion(defaultPauses.size(), pauses.size())) {
            return false;
        }
        return true;
    }

    private boolean isCorrectPassword() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String letter : letters) {
            sb.append(letter);
        }
        String currentPassword = sb.toString().trim();
        return currentPassword.equalsIgnoreCase(FileHelper.getPasswordFromFile(PASSWORD_FILE_NAME));
    }

    private void savePassword() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String letter : letters) {
            sb.append(letter);
        }
        String currentPassword = sb.toString().trim();
        FileHelper.savePasswordToFile(PASSWORD_FILE_NAME, currentPassword);
    }
}
