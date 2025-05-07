/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eduardolemus
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
// define the fields for the tracker 
public class StudyTrackerGUI extends JFrame {
    private JTextField totalHoursField, examDateField, 
            daysRemainingField, hoursStudiedField, todayHoursField;
    private JTextArea outputArea;
    private static final String FILE_NAME = "study_data.txt";
// constructor 
    public StudyTrackerGUI() {
        setTitle("📚 Study Progress Tracker");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));
        
        //obtain user input

        totalHoursField = new JTextField();
        examDateField = new JTextField();
        daysRemainingField = new JTextField();
        hoursStudiedField = new JTextField();
        todayHoursField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        
        //buttons

        JButton loadButton = new JButton("Load Progress");
        JButton saveButton = new JButton("Save Progress");
        JButton updateButton = new JButton("Update Study");

        add(new JLabel("Total Study Hours:")); add(totalHoursField);
        add(new JLabel("Exam Date:")); add(examDateField);
        add(new JLabel("Days Remaining:")); add(daysRemainingField);
        add(new JLabel("Hours Studied So Far:")); add(hoursStudiedField);
        add(new JLabel("Hours Studied Today:")); add(todayHoursField);
        add(updateButton);
        add(saveButton);
        add(loadButton);
        add(new JScrollPane(outputArea));

        loadButton.addActionListener(e -> loadProgress());
        saveButton.addActionListener(e -> saveProgress());
        updateButton.addActionListener(e -> updateStudy());

        loadProgress(); // Auto-load if file exists
    }

    private void loadProgress() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            totalHoursField.setText(reader.readLine());
            examDateField.setText(reader.readLine());
            daysRemainingField.setText(reader.readLine());
            hoursStudiedField.setText(reader.readLine());
            outputArea.setText("✅ Loaded previous progress.\n");
        } catch (IOException ex) {
            outputArea.setText("⚠️ Error loading data.\n");
        }
    }

    private void saveProgress() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(totalHoursField.getText() + "\n");
            writer.write(examDateField.getText() + "\n");
            writer.write(daysRemainingField.getText() + "\n");
            writer.write(hoursStudiedField.getText() + "\n");
            outputArea.append("📁 Progress saved.\n");
        } catch (IOException ex) {
            outputArea.setText("⚠️ Error saving data.\n");
        }
    }

    private void updateStudy() {
        try {
            int totalHours = Integer.parseInt(totalHoursField.getText());
            int daysRemaining = Integer.parseInt(daysRemainingField.getText());
            int hoursStudied = Integer.parseInt(hoursStudiedField.getText());
            int hoursToday = Integer.parseInt(todayHoursField.getText());

            hoursStudied += hoursToday;
            daysRemaining = Math.max(0, daysRemaining - 1);
            int hoursLeft = Math.max(0, totalHours - hoursStudied);
            double perDay = (daysRemaining > 0) ? (double) hoursLeft / daysRemaining : hoursLeft;

            hoursStudiedField.setText(String.valueOf(hoursStudied));
            daysRemainingField.setText(String.valueOf(daysRemaining));

            outputArea.append("\n📊 Updated Study Stats:\n");
            outputArea.append("Hours Remaining: " + hoursLeft + "\n");
            outputArea.append(String.format("You need to study %.2f hours/day.\n", perDay));

            if (hoursLeft <= 0) {
                outputArea.append("🎉 You're done studying! Good luck on the exam!\n");
            }

        } catch (NumberFormatException ex) {
            outputArea.setText("⚠️ Please enter valid numbers.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudyTrackerGUI().setVisible(true));
    }
}
