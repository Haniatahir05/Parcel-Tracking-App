import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

// ============ LOGIN FRAME ============
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private ParcelTrackingSystem system;
    
    public LoginFrame(ParcelTrackingSystem system) {
        this.system = system;
        setTitle("Parcel Tracking System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("PARCEL TRACKING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        mainPanel.add(statusLabel, gbc);
        
        // Default credentials info
        JLabel infoLabel = new JLabel("<html><center>Default Login:<br>admin/admin123<br>customer1/pass123</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        gbc.gridy = 5;
        mainPanel.add(infoLabel, gbc);
        
        add(mainPanel);
        
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Enter key to login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            User user = system.authenticateUser(username, password);
            statusLabel.setText("Login Successful!");
            statusLabel.setForeground(new Color(0, 128, 0));
            
            // Open main dashboard
            SwingUtilities.invokeLater(() -> {
                MainDashboard dashboard = new MainDashboard(system, user);
                dashboard.setVisible(true);
                dispose();
            });
            
        } catch (UserNotFoundException ex) {
            statusLabel.setText(ex.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
}

// ============ MAIN DASHBOARD ============
class MainDashboard extends JFrame {
    private ParcelTrackingSystem system;
    private User currentUser;
    private JTabbedPane tabbedPane;
    
    public MainDashboard(ParcelTrackingSystem system, User user) {
        this.system = system;
        this.currentUser = user;
        
        setTitle("Parcel Tracking System - Dashboard (" + user.getRole().toUpperCase() + ")");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Add panels based on user role
        if (user.getRole().equals("admin")) {
            tabbedPane.addTab("Add Parcel", createAddParcelPanel());
            tabbedPane.addTab("View Parcels", createViewParcelsPanel());
            tabbedPane.addTab("Track Parcel", createTrackParcelPanel());
            tabbedPane.addTab("Manage Agents", createManageAgentsPanel());
            tabbedPane.addTab("Statistics", createStatisticsPanel());
        } else if (user.getRole().equals("customer")) {
            tabbedPane.addTab("Track Parcel", createTrackParcelPanel());
            tabbedPane.addTab("View All Parcels", createViewParcelsPanel());
        } else if (user.getRole().equals("agent")) {
            tabbedPane.addTab("My Deliveries", createAgentDeliveriesPanel());
            tabbedPane.addTab("Update Status", createUpdateStatusPanel());
        }
        
        add(tabbedPane);
        
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        menu.add(logoutItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
    
    // ============ ADD PARCEL PANEL ============
    private JPanel createAddParcelPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField trackingField = new JTextField(20);
        JTextField senderField = new JTextField(20);
        JTextField receiverField = new JTextField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        JTextField weightField = new JTextField(20);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Standard", "Express", "Fragile"});
        JCheckBox priorityCheck = new JCheckBox("Priority Handling");
        JTextField instructionsField = new JTextField(20);
        
        // Add components
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tracking Number:"), gbc);
        gbc.gridx = 1;
        panel.add(trackingField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Sender Name:"), gbc);
        gbc.gridx = 1;
        panel.add(senderField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Receiver Name:"), gbc);
        gbc.gridx = 1;
        panel.add(receiverField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Receiver Address:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(addressArea), gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Weight (kg):"), gbc);
        gbc.gridx = 1;
        panel.add(weightField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Parcel Type:"), gbc);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);
        
        row++;
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(priorityCheck, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Special Instructions:"), gbc);
        gbc.gridx = 1;
        panel.add(instructionsField, gbc);
        
        row++;
        JButton addButton = new JButton("Add Parcel");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(addButton, gbc);
        
        addButton.addActionListener(e -> {
            try {
                String tracking = trackingField.getText();
                String sender = senderField.getText();
                String receiver = receiverField.getText();
                String address = addressArea.getText();
                double weight = Double.parseDouble(weightField.getText());
                String type = (String) typeCombo.getSelectedItem();
                
                Parcel parcel = null;
                if (type.equals("Standard")) {
                    parcel = new StandardParcel(tracking, sender, receiver, address, weight);
                } else if (type.equals("Express")) {
                    parcel = new ExpressParcel(tracking, sender, receiver, address, weight, priorityCheck.isSelected());
                } else if (type.equals("Fragile")) {
                    parcel = new FragileParcel(tracking, sender, receiver, address, weight, instructionsField.getText());
                }
                
                system.addParcel(parcel);
                JOptionPane.showMessageDialog(panel, "Parcel added successfully!\nCost: $" + 
                    String.format("%.2f", parcel.calculateDeliveryCost()));
                
                // Clear fields
                trackingField.setText("");
                senderField.setText("");
                receiverField.setText("");
                addressArea.setText("");
                weightField.setText("");
                instructionsField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid weight format!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidTrackingNumberException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
    
    // ============ VIEW PARCELS PANEL ============
    private JPanel createViewParcelsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Tracking No", "Sender", "Receiver", "Type", "Status", "Cost"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            for (Parcel p : system.getAllParcels()) {
                model.addRow(new Object[]{
                    p.getTrackingNumber(),
                    p.getSenderName(),
                    p.getReceiverName(),
                    p.getParcelType(),
                    p.getStatus(),
                    String.format("$%.2f", p.calculateDeliveryCost())
                });
            }
        });
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        // Initial load
        refreshButton.doClick();
        
        return panel;
    }
    
    // ============ TRACK PARCEL PANEL ============
    private JPanel createTrackParcelPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField trackingField = new JTextField(20);
        JButton trackButton = new JButton("Track");
        inputPanel.add(new JLabel("Tracking Number:"));
        inputPanel.add(trackingField);
        inputPanel.add(trackButton);
        
        JTextArea resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        trackButton.addActionListener(e -> {
            try {
                Parcel p = system.trackParcel(trackingField.getText());
                StringBuilder info = new StringBuilder();
                info.append("═══════════════════════════════════════\n");
                info.append("         PARCEL INFORMATION\n");
                info.append("═══════════════════════════════════════\n\n");
                info.append("Tracking Number: ").append(p.getTrackingNumber()).append("\n");
                info.append("Type: ").append(p.getParcelType()).append("\n");
                info.append("Status: ").append(p.getStatus()).append("\n");
                info.append("Sender: ").append(p.getSenderName()).append("\n");
                info.append("Receiver: ").append(p.getReceiverName()).append("\n");
                info.append("Address: ").append(p.getReceiverAddress()).append("\n");
                info.append("Weight: ").append(p.getWeight()).append(" kg\n");
                info.append("Delivery Cost: $").append(String.format("%.2f", p.calculateDeliveryCost())).append("\n");
                
                if (p.getAssignedAgent() != null) {
                    info.append("\nAssigned Agent: ").append(p.getAssignedAgent().getName()).append("\n");
                    info.append("Agent Phone: ").append(p.getAssignedAgent().getPhone()).append("\n");
                }
                
                resultArea.setText(info.toString());
            } catch (ParcelNotFoundException ex) {
                resultArea.setText("ERROR: " + ex.getMessage());
            }
        });
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============ MANAGE AGENTS PANEL ============
    private JPanel createManageAgentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField agentIdField = new JTextField();
        JTextField vehicleField = new JTextField();
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Agent ID:"));
        formPanel.add(agentIdField);
        formPanel.add(new JLabel("Vehicle Number:"));
        formPanel.add(vehicleField);
        
        JButton addAgentButton = new JButton("Add Agent");
        addAgentButton.addActionListener(e -> {
            DeliveryAgent agent = new DeliveryAgent(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                agentIdField.getText(),
                vehicleField.getText()
            );
            system.addDeliveryAgent(agent);
            JOptionPane.showMessageDialog(panel, "Agent added successfully!");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            agentIdField.setText("");
            vehicleField.setText("");
        });
        
        formPanel.add(new JLabel(""));
        formPanel.add(addAgentButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // ============ STATISTICS PANEL ============
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel totalParcelsLabel = new JLabel();
        JLabel totalRevenueLabel = new JLabel();
        JLabel deliveredLabel = new JLabel();
        JLabel pendingLabel = new JLabel();
        
        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(e -> {
            totalParcelsLabel.setText("Total Parcels: " + system.getAllParcels().size());
            totalRevenueLabel.setText("Total Revenue: $" + String.format("%.2f", system.calculateTotalRevenue()));
            deliveredLabel.setText("Delivered: " + system.getParcelsByStatus("Delivered").size());
            pendingLabel.setText("Pending: " + system.getParcelsByStatus("Order Placed").size());
        });
        
        panel.add(totalParcelsLabel);
        panel.add(totalRevenueLabel);
        panel.add(deliveredLabel);
        panel.add(pendingLabel);
        panel.add(refreshButton);
        
        refreshButton.doClick();
        
        return panel;
    }
    
    // ============ AGENT DELIVERIES PANEL ============
    private JPanel createAgentDeliveriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea deliveriesArea = new JTextArea();
        deliveriesArea.setEditable(false);
        deliveriesArea.setText("Agent deliveries panel - View your assigned parcels here");
        panel.add(new JScrollPane(deliveriesArea), BorderLayout.CENTER);
        return panel;
    }
    
    // ============ UPDATE STATUS PANEL ============
    private JPanel createUpdateStatusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField trackingField = new JTextField(20);
        String[] statuses = {"Order Placed", "In Transit", "Out for Delivery", "Delivered"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        JButton updateButton = new JButton("Update Status");
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tracking Number:"), gbc);
        gbc.gridx = 1;
        panel.add(trackingField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("New Status:"), gbc);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(updateButton, gbc);
        
        updateButton.addActionListener(e -> {
            try {
                system.updateParcelStatus(trackingField.getText(), (String)statusCombo.getSelectedItem());
                JOptionPane.showMessageDialog(panel, "Status updated successfully!");
            } catch (ParcelNotFoundException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", 
                                                   "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame(system).setVisible(true);
            dispose();
        }
    }
}

// ============ MAIN CLASS ============
public class ParcelTrackingApp {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize system
        ParcelTrackingSystem system = new ParcelTrackingSystem();
        
        // Add sample data
        system.addCustomer(new Customer("John Doe", "john@email.com", "1234567890", "C001", "123 Main St"));
        system.addDeliveryAgent(new DeliveryAgent("Mike Smith", "mike@email.com", "9876543210", "A001", "VH-1234"));
        
        try {
            StandardParcel p1 = new StandardParcel("TRK001", "Alice Brown", "Bob White", "456 Elm St", 2.5);
            ExpressParcel p2 = new ExpressParcel("TRK002", "Carol Green", "Dave Black", "789 Oak Ave", 1.2, true);
            system.addParcel(p1);
            system.addParcel(p2);
        } catch (InvalidTrackingNumberException e) {
            e.printStackTrace();
        }
        
        // Launch login screen
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(system);
            loginFrame.setVisible(true);
        });
    }
}

// ============ SUPPORTING STUB CLASSES ============
class User implements Serializable {
    private String username;
    private String role;
    private String name;

    private static final long serialVersionUID = 1L;

    public User(String username, String role, String name) {
        this.username = username;
        this.role = role;
        this.name = name;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getName() { return name; }
}

class UserNotFoundException extends Exception {
    public UserNotFoundException(String msg) { super(msg); }
}

class InvalidTrackingNumberException extends Exception {
    public InvalidTrackingNumberException(String msg) { super(msg); }
}

class ParcelNotFoundException extends Exception {
    public ParcelNotFoundException(String msg) { super(msg); }
}

abstract class Parcel implements Serializable {
    protected String trackingNumber;
    protected String senderName;
    protected String receiverName;
    protected String receiverAddress;
    protected double weight;
    protected String status = "Order Placed";
    protected DeliveryAgent assignedAgent = null;

    private static final long serialVersionUID = 1L;

    public Parcel(String trackingNumber, String senderName, String receiverName, String receiverAddress, double weight) {
        this.trackingNumber = trackingNumber;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.weight = weight;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getSenderName() { return senderName; }
    public String getReceiverName() { return receiverName; }
    public String getReceiverAddress() { return receiverAddress; }
    public double getWeight() { return weight; }
    public String getStatus() { return status; }
    public void setStatus(String s) { status = s; }
    public DeliveryAgent getAssignedAgent() { return assignedAgent; }
    public void setAssignedAgent(DeliveryAgent a) { assignedAgent = a; }

    public abstract double calculateDeliveryCost();
    public abstract String getParcelType();
}

class StandardParcel extends Parcel {
    public StandardParcel(String trackingNumber, String senderName, String receiverName, String receiverAddress, double weight) {
        super(trackingNumber, senderName, receiverName, receiverAddress, weight);
    }

    @Override
    public double calculateDeliveryCost() {
        return Math.max(5.0, weight * 5.0);
    }

    @Override
    public String getParcelType() { return "Standard"; }
}

class ExpressParcel extends Parcel {
    private boolean priority;
    public ExpressParcel(String trackingNumber, String senderName, String receiverName, String receiverAddress, double weight, boolean priority) {
        super(trackingNumber, senderName, receiverName, receiverAddress, weight);
        this.priority = priority;
    }

    @Override
    public double calculateDeliveryCost() {
        return Math.max(10.0, weight * 8.0 + (priority ? 10.0 : 0.0));
    }

    @Override
    public String getParcelType() { return "Express"; }
}

class FragileParcel extends Parcel {
    private String instructions;
    public FragileParcel(String trackingNumber, String senderName, String receiverName, String receiverAddress, double weight, String instructions) {
        super(trackingNumber, senderName, receiverName, receiverAddress, weight);
        this.instructions = instructions;
    }

    @Override
    public double calculateDeliveryCost() {
        return Math.max(8.0, weight * 6.0 + 5.0);
    }

    @Override
    public String getParcelType() { return "Fragile"; }
}

class DeliveryAgent implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String agentId;
    private String vehicleNumber;

    private static final long serialVersionUID = 1L;

    public DeliveryAgent(String name, String email, String phone, String agentId, String vehicleNumber) {
        this.name = name; this.email = email; this.phone = phone; this.agentId = agentId; this.vehicleNumber = vehicleNumber;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAgentId() { return agentId; }
    public String getVehicleNumber() { return vehicleNumber; }
}

class Customer implements Serializable {
    private String name, email, phone, id, address;
    private static final long serialVersionUID = 1L;
    public Customer(String name, String email, String phone, String id, String address) {
        this.name = name; this.email = email; this.phone = phone; this.id = id; this.address = address;
    }
}

class ParcelTrackingSystem implements Serializable {
    private ArrayList<Parcel> parcels = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<DeliveryAgent> agents = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "parcel_data.ser";

    public ParcelTrackingSystem() {
        loadData();
    }

    public User authenticateUser(String username, String password) throws UserNotFoundException {
        if ("admin".equals(username) && "admin123".equals(password)) {
            return new User(username, "admin", "Administrator");
        }
        if ("customer1".equals(username) && "pass123".equals(password)) {
            return new User(username, "customer", "Customer One");
        }
        if ("agent1".equals(username) && "agentpass".equals(password)) {
            return new User(username, "agent", "Agent One");
        }
        throw new UserNotFoundException("Invalid username or password.");
    }

    public synchronized void addParcel(Parcel p) throws InvalidTrackingNumberException {
        if (p == null || p.getTrackingNumber() == null || p.getTrackingNumber().trim().isEmpty()) {
            throw new InvalidTrackingNumberException("Tracking number is invalid.");
        }
        parcels.add(p);
        saveData();
    }

    public synchronized java.util.List<Parcel> getAllParcels() { return parcels; }

    public Parcel trackParcel(String tracking) throws ParcelNotFoundException {
        for (Parcel p : parcels) {
            if (p.getTrackingNumber().equals(tracking)) return p;
        }
        throw new ParcelNotFoundException("Parcel not found: " + tracking);
    }

    public synchronized void addCustomer(Customer c) { customers.add(c); saveData(); }
    public synchronized void addDeliveryAgent(DeliveryAgent a) { agents.add(a); saveData(); }

    public synchronized void updateParcelStatus(String tracking, String status) throws ParcelNotFoundException {
        Parcel p = trackParcel(tracking);
        p.setStatus(status);
        saveData();
    }

    public java.util.List<Parcel> getParcelsByStatus(String status) {
        java.util.List<Parcel> out = new ArrayList<>();
        for (Parcel p : parcels) if (p.getStatus().equals(status)) out.add(p);
        return out;
    }

    public double calculateTotalRevenue() {
        double sum = 0.0;
        for (Parcel p : parcels) sum += p.calculateDeliveryCost();
        return sum;
    }

    private synchronized void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(parcels);
            oos.writeObject(customers);
            oos.writeObject(agents);
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object pobj = ois.readObject();
            Object cobj = ois.readObject();
            Object aobj = ois.readObject();
            if (pobj instanceof ArrayList) parcels = (ArrayList<Parcel>) pobj;
            if (cobj instanceof ArrayList) customers = (ArrayList<Customer>) cobj;
            if (aobj instanceof ArrayList) agents = (ArrayList<DeliveryAgent>) aobj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }
}