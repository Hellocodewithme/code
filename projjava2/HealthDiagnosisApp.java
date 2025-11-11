import java.awt.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import javax.swing.*;

// Main App
public class HealthDiagnosisApp extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HealthDiagnosisApp().setVisible(true));
    }

    private JTextField nameField, ageField, locationField;
    private JComboBox<String> genderBox, symptom1Box, symptom2Box, symptom3Box;
    private JTextArea resultArea;

    private final String[] symptoms = {
        "Fever", "Cough", "Body Ache", "Shortness of breath", "Rash", "Dizziness",
        "Loss of taste/smell", "Muscle pain", "Fatigue", "Headache", "Chills", "Sweating",
        "Abdominal pain", "Facial pain", "Nasal congestion", "Weight loss", "Chest discomfort",
        "Sore throat", "Difficulty swallowing", "Sneezing", "Vomiting", "Diarrhea", "Stomach cramps",
        "Red eyes", "Itching", "Tearing", "Runny nose", "Nausea", "Burning urination", "Frequent urge",
        "Pelvic pain"
    };

    private final List<Disease> dataset = DiseaseData.getSampleDataset();

    public HealthDiagnosisApp() {
        setTitle("Smart Health Diagnosis System");
        setSize(800, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(245, 250, 255);
        Color headerColor = new Color(46, 134, 222);
        Color inputColor = Color.WHITE;
        Color accentColor = new Color(39, 174, 96);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);
        setContentPane(mainPanel);

        JLabel header = new JLabel("Smart Health Diagnosis System", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(headerColor);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        int row = 0;
        formPanel.add(createLabel("Name:", labelFont, bgColor), getGbc(0, row));
        nameField = createField(fieldFont, inputColor);
        formPanel.add(nameField, getGbc(1, row++));

        formPanel.add(createLabel("Age:", labelFont, bgColor), getGbc(0, row));
        ageField = createField(fieldFont, inputColor);
        formPanel.add(ageField, getGbc(1, row++));

        formPanel.add(createLabel("Gender:", labelFont, bgColor), getGbc(0, row));
        genderBox = new JComboBox<>(new String[]{"", "Male", "Female", "Other"});
        genderBox.setFont(fieldFont);
        genderBox.setBackground(inputColor);
        formPanel.add(genderBox, getGbc(1, row++));

        symptom1Box = createSymptomBox(fieldFont, inputColor);
        symptom2Box = createSymptomBox(fieldFont, inputColor);
        symptom3Box = createSymptomBox(fieldFont, inputColor);
        symptom1Box.addActionListener(e -> disableDuplicates());

        formPanel.add(createLabel("Symptom 1:", labelFont, bgColor), getGbc(0, row));
        formPanel.add(symptom1Box, getGbc(1, row++));

        formPanel.add(createLabel("Symptom 2 (Optional):", labelFont, bgColor), getGbc(0, row));
        formPanel.add(symptom2Box, getGbc(1, row++));

        formPanel.add(createLabel("Symptom 3 (Optional):", labelFont, bgColor), getGbc(0, row));
        formPanel.add(symptom3Box, getGbc(1, row++));

        formPanel.add(createLabel("Location:", labelFont, bgColor), getGbc(0, row));
        locationField = createField(fieldFont, inputColor);
        formPanel.add(locationField, getGbc(1, row++));

        JButton diagnoseButton = new JButton("Diagnose");
        diagnoseButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        diagnoseButton.setBackground(accentColor);
        diagnoseButton.setForeground(Color.WHITE);
        diagnoseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        diagnoseButton.setFocusPainted(false);
        diagnoseButton.addActionListener(e -> diagnose());

        gbc = getGbc(0, row++);
        gbc.gridwidth = 2;
        formPanel.add(diagnoseButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Result area
        resultArea = new JTextArea(10, 40);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultPane = new JScrollPane(resultArea);
        resultPane.setBorder(BorderFactory.createTitledBorder("Diagnosis Result"));
        resultPane.setBackground(bgColor);

        // Add result area + footer panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bgColor);
        bottomPanel.add(resultPane, BorderLayout.CENTER);

        JLabel footer = new JLabel("Made by Rahul Ranjan Pathak | Using Java Swing", JLabel.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        footer.setForeground(new Color(100, 100, 100));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        bottomPanel.add(footer, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text, Font font, Color bg) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(33, 47, 61));
        label.setBackground(bg);
        return label;
    }

    private JTextField createField(Font font, Color bg) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBackground(bg);
        return field;
    }

    private JComboBox<String> createSymptomBox(Font font, Color bg) {
        JComboBox<String> box = new JComboBox<>();
        box.setFont(font);
        box.setBackground(bg);
        box.addItem("");
        for (String s : symptoms) box.addItem(s);
        return box;
    }

    private GridBagConstraints getGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void disableDuplicates() {
        String selected = (String) symptom1Box.getSelectedItem();
        JComboBox<String>[] boxes = new JComboBox[]{symptom2Box, symptom3Box};
        for (JComboBox<String> box : boxes) {
            box.removeAllItems();
            box.addItem("");
            for (String s : symptoms) {
                if (!s.equals(selected)) {
                    box.addItem(s);
                }
            }
        }
    }

    private void diagnose() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String location = locationField.getText().trim();
        List<String> selectedSymptoms = Arrays.asList(
            (String) symptom1Box.getSelectedItem(),
            (String) symptom2Box.getSelectedItem(),
            (String) symptom3Box.getSelectedItem()
        ).stream().filter(s -> s != null && !s.isEmpty()).toList();

        if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || selectedSymptoms.isEmpty() || location.isEmpty()) {
            resultArea.setText("Please fill all required fields including at least one symptom.");
            return;
        }

        List<Disease> matched = dataset.stream()
                .filter(d -> selectedSymptoms.stream().allMatch(sym -> d.symptoms.contains(sym)))
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(name).append(", based on your input:\n\n");

        if (!matched.isEmpty()) {
            for (Disease d : matched) {
                sb.append("• Disease: ").append(d.name).append("\n");
                sb.append("  Treatment: ").append(d.treatment).append("\n");
                sb.append("  Prevention: ").append(d.prevention).append("\n\n");
            }

            String query = selectedSymptoms.size() >= 2 ? "hospitals" : "medical shops";
            try {
                String mapLink = "https://www.google.com/maps/search/" + URLEncoder.encode(query + " near " + location, "UTF-8");
                sb.append("Nearby ").append(query).append(": ").append(mapLink).append("\n");
            } catch (Exception ex) {
                sb.append("Error generating map link.\n");
            }
        } else {
            sb.append("No matching disease found. Please consult a doctor.\n");
        }

        resultArea.setText(sb.toString());
    }
}

// Disease class
class Disease {
    String name;
    List<String> symptoms;
    String treatment;
    String prevention;

    public Disease(String name, List<String> symptoms, String treatment, String prevention) {
        this.name = name;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.prevention = prevention;
    }
}

// Dataset
class DiseaseData {
    public static List<Disease> getSampleDataset() {
        return Arrays.asList(
            new Disease("Flu", Arrays.asList("Fever", "Cough", "Body Ache"), "Rest, fluids, Paracetamol", "Avoid cold environments, get vaccinated annually"),
            new Disease("Covid-19", Arrays.asList("Fever", "Cough", "Loss of taste/smell"), "Isolation, Antivirals, Monitoring", "Wear masks, maintain hygiene, vaccination"),
            new Disease("Dengue", Arrays.asList("Fever", "Muscle pain", "Rash"), "Hydration, Painkillers (not aspirin)", "Avoid mosquito bites, eliminate standing water"),
            new Disease("Anemia", Arrays.asList("Fatigue", "Dizziness"), "Iron-rich diet, supplements", "Regular checkups, include leafy greens & iron foods"),
            new Disease("Asthma", Arrays.asList("Shortness of breath", "Cough"), "Inhalers, Avoid triggers", "Use air purifiers, avoid allergens"),
            new Disease("Migraine", Arrays.asList("Headache", "Dizziness"), "Rest, Pain relievers", "Regular sleep, avoid triggers like caffeine"),
            new Disease("Chickenpox", Arrays.asList("Fever", "Rash", "Fatigue"), "Isolation, Calamine lotion, Fluids", "Vaccination, avoid contact with infected individuals"),
            new Disease("Malaria", Arrays.asList("Fever", "Chills", "Sweating"), "Antimalarial medication", "Use mosquito nets, insect repellents"),
            new Disease("Pneumonia", Arrays.asList("Fever", "Cough", "Shortness of breath"), "Antibiotics, rest, fluids", "Vaccination, hygiene"),
            new Disease("Typhoid", Arrays.asList("Fever", "Fatigue", "Abdominal pain"), "Antibiotics, fluids", "Drink clean water, hygiene"),
            new Disease("Sinusitis", Arrays.asList("Headache", "Facial pain", "Nasal congestion"), "Decongestants, steam inhalation", "Avoid allergens, use humidifiers"),
            new Disease("Tuberculosis", Arrays.asList("Cough", "Fever", "Weight loss"), "Antibiotics (6-month course)", "Vaccination, early detection"),
            new Disease("Bronchitis", Arrays.asList("Cough", "Chest discomfort", "Fatigue"), "Rest, cough medicine", "Avoid smoke and dust"),
            new Disease("Tonsillitis", Arrays.asList("Sore throat", "Fever", "Difficulty swallowing"), "Rest, saltwater gargle", "Avoid infected individuals"),
            new Disease("Allergy", Arrays.asList("Sneezing", "Rash", "Cough"), "Antihistamines", "Avoid allergens"),
            new Disease("Food Poisoning", Arrays.asList("Vomiting", "Diarrhea", "Stomach cramps"), "Hydration, rest", "Avoid unhygienic food"),
            new Disease("Conjunctivitis", Arrays.asList("Red eyes", "Itching", "Tearing"), "Eye drops, hygiene", "Avoid touching eyes, personal items"),
            new Disease("Measles", Arrays.asList("Fever", "Rash", "Runny nose"), "Rest, fluids, vitamin A", "Vaccination"),
            new Disease("Appendicitis", Arrays.asList("Abdominal pain", "Fever", "Nausea"), "Surgery", "Early diagnosis"),
            new Disease("UTI", Arrays.asList("Burning urination", "Frequent urge", "Pelvic pain"), "Antibiotics", "Drink water, hygiene")
        );
    }
}
