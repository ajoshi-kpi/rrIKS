package app.frame;

import app.Constants;
import app.utils.FileHelper;
import app.utils.MathHelper;
import app.utils.Md5Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    public JFrame getLearningFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new GridLayout(0, 1));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Label mainLabel = new Label("Enter learning phrase: ");
        Button confirmButton = new Button("Confirm");
        Button back = new Button("Back");
        final JTextField textField = new JTextField(50);

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

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDifference();
                try {
                    System.out.println(timesPressed);
                    System.out.println(pauses);
                    FileHelper.appendToFile(TIMES_PRESSED_FILE_NAME, calculateSuitable(timesPressed));
                    FileHelper.appendToFile(PAUSES_FILE_NAME, calculateSuitable(pauses));
                    savePassword(textField.getText());
                    textField.setText("");
                    textField.requestFocusInWindow();
                    starts.clear();
                    ends.clear();
                    timesPressed.clear();
                    pauses.clear();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                getMainFrame().setVisible(true);
            }
        });

        panel.add(mainLabel);
        panel.add(textField);
        panel.add(confirmButton);
        panel.add(back);
        jFrame.getContentPane().add(panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        return jFrame;
    }

    public JFrame getAuthenticationFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new GridLayout(0, 1));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Label mainLabel = new Label("Enter passphrase: ");
        Button confirmButton = new Button("Confirm");
        Button back = new Button("Back");
        Label result = new Label();
        final JTextField textField = new JTextField(50);

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

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDifference();
                try {
                    if (isCorrectPassword(textField.getText())) {
                        try {
                            if (isValid()) {
                                FileHelper.appendToFile(TIMES_PRESSED_FILE_NAME, calculateSuitable(timesPressed));
                                FileHelper.appendToFile(PAUSES_FILE_NAME, calculateSuitable(pauses));
                                result.setText("VALID");
                            } else {
                                result.setText("NOT VALID");
                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        result.setText("Password is incorrect");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                textField.setText("");
                textField.requestFocusInWindow();
                starts.clear();
                ends.clear();
                timesPressed.clear();
                pauses.clear();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                getMainFrame().setVisible(true);
            }
        });

        panel.add(mainLabel);
        panel.add(textField);
        panel.add(confirmButton);
        panel.add(back);
        panel.add(result);
        jFrame.getContentPane().add(panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        return jFrame;
    }

    public JFrame getMainFrame() {
        JFrame jFrame = new JFrame();
        JPanel panel = new JPanel();
        Label mainLabel = new Label("Chose application mode: ");
        Button learningButton = new Button();
        learningButton.setLabel("Learning mode");
        Button authenticateButton = new Button();
        authenticateButton.setLabel("Authentication mode");

        learningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                getLearningFrame().setVisible(true);
            }
        });

        authenticateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                getAuthenticationFrame().setVisible(true);
            }
        });

        panel.add(mainLabel);
        panel.add(learningButton);
        panel.add(authenticateButton);

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

    private boolean isCorrectPassword(String currentPassword) throws IOException {
        return Md5Helper.md5Custom(currentPassword).equalsIgnoreCase(FileHelper.getPasswordFromFile(PASSWORD_FILE_NAME));
    }

    private void savePassword(String currentPassword) throws IOException {
        FileHelper.savePasswordToFile(PASSWORD_FILE_NAME, Md5Helper.md5Custom(currentPassword));
    }
}
