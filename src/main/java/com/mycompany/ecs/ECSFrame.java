package com.mycompany.ecs;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

class JDBCConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/escdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "divyanshu2006";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

public class ECSFrame extends javax.swing.JFrame {

    private String username;
    private int userId;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ECSFrame.class.getName());
    private final JPanel[] gridCells;
    private final int[] panelOccupied;
    private final JButton[] deviceButtons;
    private final int[] devicePlaced;
    private int deviceCount;
    private double totalCost = 0.0;
    private final int[] deviceWattage = {60, 1500, 2000, 10, 200, 1000, 1500, 75, 250, 120, 500, 1200, 500, 1000};
    private final double[] deviceCostPerHour = {0.48, 12, 16, 0.08, 1.6, 8, 12, 0.6, 0.96, 4, 9.6, 8, 4, 8};
    private final String[] applianceNames = {"Fan", "Air_Conditioner", "Heater", "Led", "Fridge", "Microwave", "Kettle", "Laptop", "Television", "Washing_Machine", "Iron", "Vaccum_Cleaner", "Blender", "Toaster"};

    public ECSFrame(String username) {
        initComponents();
        this.username = username;
        user.setText(username);
        setResizable(false);
        setSize(1225, 850);

        gridCells = new JPanel[]{device1, device2, device3, device4, device5, device6, device7, device8, device9};
        panelOccupied = new int[9];
        deviceButtons = new JButton[]{fanAdd, acAdd, heaterAdd, ledAdd, fridgeAdd, microwaveAdd, kettleAdd, laptopAdd, tvAdd, washingAdd, ironAdd, vaccumAdd, blenderAdd, toasterAdd};
        devicePlaced = new int[14];
        deviceCount = 0;

        ImageIcon img = new ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\mainLogo.png");
        this.setIconImage(img.getImage());

        preExistingDevices();
    }

    private void preExistingDevices() {
        try (Connection con = JDBCConnection.getConnection()) {
            String query = "SELECT * FROM user_appliances WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, this.username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                for (int i = 0; i < applianceNames.length; i++) {
                    String col = applianceNames[i];
                    if (rs.getInt(col) == 1) {
                        Icon icon = getDeviceIcon(applianceNames[i]);
                        placeDevice(icon, applianceNames[i], i);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Icon getDeviceIcon(String deviceName) {
        switch (deviceName) {
            case "Fan":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/fanIcon.png"));
            case "Air_Conditioner":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/acIcon.png"));
            case "Heater":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/heaterIcon.png"));
            case "Led":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/ledIcon.png"));
            case "Fridge":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/fridgeIcon.png"));
            case "Microwave":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/microwaveIcon.png"));
            case "Kettle":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/kettleIcon.png"));
            case "Laptop":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/laptopIcon.png"));
            case "Television":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/tvIcon.png"));
            case "Washing_Machine":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/washingmachineIcon.png"));
            case "Iron":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/ironIcon.png"));
            case "Vaccum_Cleaner":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/vaccumIcon.png"));
            case "Blender":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/blenderIcon.png"));
            case "Toaster":
                return new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/toasterIcon.png"));
            default:
                return null;
        }
    }

    private void placeDevice(Icon icon, String name, int deviceIndex) {
        if (devicePlaced[deviceIndex] == 1) {
            return;
        }

        if (deviceCount == gridCells.length) {
            return;
        }

        for (int i = 0; i < gridCells.length; i++) {
            if (panelOccupied[i] == 0) {
                JPanel cell = gridCells[i];
                cell.setLayout(new BorderLayout());

                JPanel devicePanel = createDevicePanel(icon, name.replace("_", " "), deviceIndex, cell);

                cell.add(devicePanel, BorderLayout.CENTER);

                panelOccupied[i] = 1;
                devicePlaced[deviceIndex] = 1;
                deviceCount++;

                totalCost += deviceCostPerHour[deviceIndex];
                updateGlobalCost();

                markDeviceOwned(applianceNames[deviceIndex]);

                cell.revalidate();
                cell.repaint();
                return;
            }
        }
    }

    public void markDeviceOwned(String columnName) {
        String query = "UPDATE user_appliances SET `" + columnName + "` = 1 WHERE username = ?";

        try (Connection con = JDBCConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDeviceOwned(String columnName) {
        String query = "UPDATE user_appliances SET `" + columnName + "` = 0 WHERE username = ?";

        try (Connection con = JDBCConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, this.username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void logOut() {
        RegisterPage reg = new RegisterPage();
        reg.setVisible(true);
        this.dispose();
    }

    private JPanel createDevicePanel(Icon icon, String name, int deviceIndex, JPanel cell) {
        JPanel panel = new JPanel();

        JLabel appIcon = new JLabel(icon);
        appIcon.setHorizontalAlignment(SwingConstants.CENTER);
        appIcon.setPreferredSize(new Dimension(128, 128));

        JLabel appName = new JLabel(name, SwingConstants.CENTER);

        JButton infoButton = new JButton("Info");
        JButton removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(120, 30));

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(appIcon, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                        .addComponent(appName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(infoButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addComponent(removeButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(appIcon, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(appName)
                        .addGap(15)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(infoButton, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addComponent(removeButton, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                        .addGap(10)
        );
        infoButton.addActionListener(e -> showDeviceInfo(deviceIndex));
        removeButton.addActionListener(e -> {
            removeDevice(cell, deviceIndex);
        });
        return panel;
    }

    private void showDeviceInfo(int deviceIndex) {
        String name = applianceNames[deviceIndex].replace("_", " ");
        int wattage = deviceWattage[deviceIndex];
        double cost = deviceCostPerHour[deviceIndex];

        deviceName.setText(name);
        deviceWatt.setText(wattage + " W");
        deviceCost.setText("₹" + cost + " / hr");
        deviceUsageTime.setText("");
        deviceCostPerUsage.setText("");
        for (ActionListener al : calculate.getActionListeners()) {
            calculate.removeActionListener(al);
        }
        calculate.addActionListener(e -> calculateApplianceCost(deviceIndex));
    }

    private void calculateApplianceCost(int deviceIndex) {
        try {
            double usageMinutes = Double.parseDouble(deviceUsageTime.getText().trim());

            double usageHours = usageMinutes / 60.0;

            double costPerHour = deviceCostPerHour[deviceIndex];
            double applianceCost = usageHours * costPerHour;

            deviceCostPerUsage.setText("₹ " + String.format("%.2f", applianceCost));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for usage time (in minutes).");
        }
    }

    private void removeDevice(JPanel cell, int deviceIndex) {
        if (devicePlaced[deviceIndex] == 0) {
            return;
        }

        cell.removeAll();
        cell.revalidate();
        cell.repaint();

        for (int i = 0; i < gridCells.length; i++) {
            if (gridCells[i] == cell) {
                panelOccupied[i] = 0;
                break;
            }
        }

        devicePlaced[deviceIndex] = 0;
        deviceCount--;

        removeDeviceOwned(applianceNames[deviceIndex]);

        totalCost -= deviceCostPerHour[deviceIndex];
        updateGlobalCost();
    }

    private void updateGlobalCost() {
        globalCostPerHr.setText("₹ "+String.format("%.2f", totalCost) + " / hr");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        heading = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        logOut = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        user = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        devicearea = new javax.swing.JPanel();
        deviceplace = new javax.swing.JPanel();
        device1 = new javax.swing.JPanel();
        device2 = new javax.swing.JPanel();
        device3 = new javax.swing.JPanel();
        device4 = new javax.swing.JPanel();
        device5 = new javax.swing.JPanel();
        device6 = new javax.swing.JPanel();
        device7 = new javax.swing.JPanel();
        device8 = new javax.swing.JPanel();
        device9 = new javax.swing.JPanel();
        deviceinfo = new javax.swing.JPanel();
        deviceNameLabel = new javax.swing.JLabel();
        deviceName = new javax.swing.JLabel();
        deviceWattLabel = new javax.swing.JLabel();
        deviceWatt = new javax.swing.JLabel();
        deviceCostLabel = new javax.swing.JLabel();
        deviceCost = new javax.swing.JLabel();
        deviceUsage = new javax.swing.JLabel();
        deviceUsageTime = new javax.swing.JTextField();
        calculate = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        deviceCostPerUsageLabel = new javax.swing.JLabel();
        deviceCostPerUsage = new javax.swing.JLabel();
        globalCostPerHrLabel = new javax.swing.JLabel();
        globalCostPerHr = new javax.swing.JLabel();
        sidebar = new javax.swing.JScrollPane();
        devicelist = new javax.swing.JPanel();
        fanPanel = new javax.swing.JPanel();
        fanIcon = new javax.swing.JLabel();
        fanName = new javax.swing.JLabel();
        fanAdd = new javax.swing.JButton();
        acPanel = new javax.swing.JPanel();
        acIcon = new javax.swing.JLabel();
        acName = new javax.swing.JLabel();
        acAdd = new javax.swing.JButton();
        heaterPanel = new javax.swing.JPanel();
        heaterIcon = new javax.swing.JLabel();
        heaterName = new javax.swing.JLabel();
        heaterAdd = new javax.swing.JButton();
        ledPanel = new javax.swing.JPanel();
        ledIcon = new javax.swing.JLabel();
        ledName = new javax.swing.JLabel();
        ledAdd = new javax.swing.JButton();
        fridgePanel = new javax.swing.JPanel();
        fridgeIcon = new javax.swing.JLabel();
        fridgeName = new javax.swing.JLabel();
        fridgeAdd = new javax.swing.JButton();
        microwavePanel = new javax.swing.JPanel();
        microwaveIcon = new javax.swing.JLabel();
        microwaveName = new javax.swing.JLabel();
        microwaveAdd = new javax.swing.JButton();
        kettlePanel = new javax.swing.JPanel();
        kettleIcon = new javax.swing.JLabel();
        kettleName = new javax.swing.JLabel();
        kettleAdd = new javax.swing.JButton();
        laptopPanel = new javax.swing.JPanel();
        laptopIcon = new javax.swing.JLabel();
        laptopName = new javax.swing.JLabel();
        laptopAdd = new javax.swing.JButton();
        tvPanel = new javax.swing.JPanel();
        tvIcon = new javax.swing.JLabel();
        tvName = new javax.swing.JLabel();
        tvAdd = new javax.swing.JButton();
        washingPanel = new javax.swing.JPanel();
        washingIcon = new javax.swing.JLabel();
        washingName = new javax.swing.JLabel();
        washingAdd = new javax.swing.JButton();
        ironPanel = new javax.swing.JPanel();
        ironIcon = new javax.swing.JLabel();
        ironName = new javax.swing.JLabel();
        ironAdd = new javax.swing.JButton();
        vaccumPanel = new javax.swing.JPanel();
        vaccumIcon = new javax.swing.JLabel();
        vaccumName = new javax.swing.JLabel();
        vaccumAdd = new javax.swing.JButton();
        blenderPanel = new javax.swing.JPanel();
        blenderIcon = new javax.swing.JLabel();
        blenderName = new javax.swing.JLabel();
        blenderAdd = new javax.swing.JButton();
        toasterPanel = new javax.swing.JPanel();
        toasterIcon = new javax.swing.JLabel();
        toasterName = new javax.swing.JLabel();
        toasterAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Energy Consumption Simulator");

        heading.setBackground(new java.awt.Color(102, 153, 255));

        name.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        name.setText("ENERGY CONSUMPTION SIMULATOR");

        logOut.setBackground(new java.awt.Color(255, 102, 102));
        logOut.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        logOut.setText("Log Out");
        logOut.setFocusPainted(false);
        logOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\subLogo.png")); // NOI18N

        user.setFont(new java.awt.Font("SimSun", 1, 24)); // NOI18N
        user.setText("dadawd");

        jLabel1.setFont(new java.awt.Font("SimSun", 0, 24)); // NOI18N
        jLabel1.setText("User:");

        javax.swing.GroupLayout headingLayout = new javax.swing.GroupLayout(heading);
        heading.setLayout(headingLayout);
        headingLayout.setHorizontalGroup(
            headingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 404, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logOut, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        headingLayout.setVerticalGroup(
            headingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headingLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(headingLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(headingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(logOut)
                        .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicearea.setBackground(new java.awt.Color(0, 0, 0));
        devicearea.setLayout(new java.awt.GridLayout(3, 3, 10, 10));
        devicearea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        deviceplace.setBackground(new java.awt.Color(204, 204, 204));
        deviceplace.setLayout(new java.awt.GridLayout(3, 3, 5, 5));

        device1.setOpaque(false);

        javax.swing.GroupLayout device1Layout = new javax.swing.GroupLayout(device1);
        device1.setLayout(device1Layout);
        device1Layout.setHorizontalGroup(
            device1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device1Layout.setVerticalGroup(
            device1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device1);

        device2.setOpaque(false);

        javax.swing.GroupLayout device2Layout = new javax.swing.GroupLayout(device2);
        device2.setLayout(device2Layout);
        device2Layout.setHorizontalGroup(
            device2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device2Layout.setVerticalGroup(
            device2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device2);

        device3.setOpaque(false);

        javax.swing.GroupLayout device3Layout = new javax.swing.GroupLayout(device3);
        device3.setLayout(device3Layout);
        device3Layout.setHorizontalGroup(
            device3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device3Layout.setVerticalGroup(
            device3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device3);

        device4.setOpaque(false);

        javax.swing.GroupLayout device4Layout = new javax.swing.GroupLayout(device4);
        device4.setLayout(device4Layout);
        device4Layout.setHorizontalGroup(
            device4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device4Layout.setVerticalGroup(
            device4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device4);

        device5.setOpaque(false);

        javax.swing.GroupLayout device5Layout = new javax.swing.GroupLayout(device5);
        device5.setLayout(device5Layout);
        device5Layout.setHorizontalGroup(
            device5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device5Layout.setVerticalGroup(
            device5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device5);

        device6.setOpaque(false);

        javax.swing.GroupLayout device6Layout = new javax.swing.GroupLayout(device6);
        device6.setLayout(device6Layout);
        device6Layout.setHorizontalGroup(
            device6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device6Layout.setVerticalGroup(
            device6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device6);

        device7.setOpaque(false);

        javax.swing.GroupLayout device7Layout = new javax.swing.GroupLayout(device7);
        device7.setLayout(device7Layout);
        device7Layout.setHorizontalGroup(
            device7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device7Layout.setVerticalGroup(
            device7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device7);

        device8.setOpaque(false);

        javax.swing.GroupLayout device8Layout = new javax.swing.GroupLayout(device8);
        device8.setLayout(device8Layout);
        device8Layout.setHorizontalGroup(
            device8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device8Layout.setVerticalGroup(
            device8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device8);

        device9.setOpaque(false);

        javax.swing.GroupLayout device9Layout = new javax.swing.GroupLayout(device9);
        device9.setLayout(device9Layout);
        device9Layout.setHorizontalGroup(
            device9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        device9Layout.setVerticalGroup(
            device9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        deviceplace.add(device9);

        devicearea.add(deviceplace, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 700, 650));

        deviceinfo.setBackground(new java.awt.Color(255, 153, 255));

        deviceNameLabel.setText("App Name:");

        deviceWattLabel.setText("Wattage:");

        deviceCostLabel.setText("Cost / Hr:");

        deviceUsage.setText("Usage Time (in Minutes):");

        deviceUsageTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deviceUsageTimeActionPerformed(evt);
            }
        });

        calculate.setText("Calculate");

        reset.setText("Reset");

        deviceCostPerUsageLabel.setText("Appliance Cost / Usage Time:");

        globalCostPerHrLabel.setText("Global Cost / Hr:");

        javax.swing.GroupLayout deviceinfoLayout = new javax.swing.GroupLayout(deviceinfo);
        deviceinfo.setLayout(deviceinfoLayout);
        deviceinfoLayout.setHorizontalGroup(
            deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deviceinfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deviceinfoLayout.createSequentialGroup()
                        .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deviceWattLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deviceNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deviceCostLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deviceName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(deviceinfoLayout.createSequentialGroup()
                                .addComponent(deviceWatt, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(deviceCost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, deviceinfoLayout.createSequentialGroup()
                        .addComponent(deviceUsage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(deviceUsageTime, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(deviceinfoLayout.createSequentialGroup()
                        .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(deviceinfoLayout.createSequentialGroup()
                                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(deviceCostPerUsageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(deviceinfoLayout.createSequentialGroup()
                                        .addComponent(calculate)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(reset)))
                                .addGap(10, 10, 10)
                                .addComponent(deviceCostPerUsage, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(deviceinfoLayout.createSequentialGroup()
                                .addComponent(globalCostPerHrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(globalCostPerHr, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(269, 269, 269))
        );
        deviceinfoLayout.setVerticalGroup(
            deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deviceinfoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deviceNameLabel)
                    .addComponent(deviceName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deviceWattLabel)
                    .addComponent(deviceWatt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deviceCostLabel)
                    .addComponent(deviceCost, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deviceUsage)
                    .addComponent(deviceUsageTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculate)
                    .addComponent(reset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(deviceCostPerUsageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deviceCostPerUsage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(deviceinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deviceinfoLayout.createSequentialGroup()
                        .addComponent(globalCostPerHrLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(globalCostPerHr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        sidebar.setBackground(new java.awt.Color(255, 255, 255));
        sidebar.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebar.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        devicelist.setBackground(new java.awt.Color(255, 255, 102));
        devicelist.setLayout(new java.awt.GridLayout(14, 1, 0, 5));

        fanPanel.setBackground(new java.awt.Color(0, 255, 204));
        fanPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        fanIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\fanIcon.png")); // NOI18N

        fanName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        fanName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fanName.setText("Fan");
        fanName.setToolTipText("");

        fanAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        fanAdd.setText("ADD");
        fanAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fanAdd.setInheritsPopupMenu(true);
        fanAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fanAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fanPanelLayout = new javax.swing.GroupLayout(fanPanel);
        fanPanel.setLayout(fanPanelLayout);
        fanPanelLayout.setHorizontalGroup(
            fanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fanIcon)
                .addGroup(fanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fanPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fanName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fanPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fanAdd)
                        .addGap(115, 115, 115))))
        );
        fanPanelLayout.setVerticalGroup(
            fanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fanPanelLayout.createSequentialGroup()
                        .addComponent(fanName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fanAdd))
                    .addComponent(fanIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(fanPanel);

        acPanel.setBackground(new java.awt.Color(0, 255, 204));
        acPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        acIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\acIcon.png")); // NOI18N

        acName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        acName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acName.setText("Air Conditioner");
        acName.setToolTipText("");

        acAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        acAdd.setText("ADD");
        acAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        acAdd.setInheritsPopupMenu(true);
        acAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout acPanelLayout = new javax.swing.GroupLayout(acPanel);
        acPanel.setLayout(acPanelLayout);
        acPanelLayout.setHorizontalGroup(
            acPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(acPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(acIcon)
                .addGroup(acPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(acPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(acName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, acPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acAdd)
                        .addGap(115, 115, 115))))
        );
        acPanelLayout.setVerticalGroup(
            acPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(acPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(acPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(acPanelLayout.createSequentialGroup()
                        .addComponent(acName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(acAdd)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(acIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                .addContainerGap())
        );

        devicelist.add(acPanel);

        heaterPanel.setBackground(new java.awt.Color(0, 255, 204));
        heaterPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        heaterIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\heaterIcon.png")); // NOI18N

        heaterName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        heaterName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heaterName.setText("Heater");
        heaterName.setToolTipText("");

        heaterAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        heaterAdd.setText("ADD");
        heaterAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        heaterAdd.setInheritsPopupMenu(true);
        heaterAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heaterAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout heaterPanelLayout = new javax.swing.GroupLayout(heaterPanel);
        heaterPanel.setLayout(heaterPanelLayout);
        heaterPanelLayout.setHorizontalGroup(
            heaterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(heaterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(heaterIcon)
                .addGroup(heaterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(heaterPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(heaterName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, heaterPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(heaterAdd)
                        .addGap(115, 115, 115))))
        );
        heaterPanelLayout.setVerticalGroup(
            heaterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(heaterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(heaterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(heaterPanelLayout.createSequentialGroup()
                        .addComponent(heaterName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(heaterAdd))
                    .addComponent(heaterIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(heaterPanel);

        ledPanel.setBackground(new java.awt.Color(0, 255, 204));
        ledPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        ledIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\ledIcon.png")); // NOI18N

        ledName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        ledName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ledName.setText("Led");
        ledName.setToolTipText("");

        ledAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        ledAdd.setText("ADD");
        ledAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ledAdd.setInheritsPopupMenu(true);
        ledAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ledPanelLayout = new javax.swing.GroupLayout(ledPanel);
        ledPanel.setLayout(ledPanelLayout);
        ledPanelLayout.setHorizontalGroup(
            ledPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ledPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ledIcon)
                .addGroup(ledPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ledPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ledName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ledPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ledAdd)
                        .addGap(115, 115, 115))))
        );
        ledPanelLayout.setVerticalGroup(
            ledPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ledPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ledPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ledPanelLayout.createSequentialGroup()
                        .addComponent(ledName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ledAdd))
                    .addComponent(ledIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(ledPanel);

        fridgePanel.setBackground(new java.awt.Color(0, 255, 204));
        fridgePanel.setPreferredSize(new java.awt.Dimension(448, 150));

        fridgeIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\fridgeIcon.png")); // NOI18N

        fridgeName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        fridgeName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fridgeName.setText("Fridge");
        fridgeName.setToolTipText("");

        fridgeAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        fridgeAdd.setText("ADD");
        fridgeAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fridgeAdd.setInheritsPopupMenu(true);
        fridgeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fridgeAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fridgePanelLayout = new javax.swing.GroupLayout(fridgePanel);
        fridgePanel.setLayout(fridgePanelLayout);
        fridgePanelLayout.setHorizontalGroup(
            fridgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fridgePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fridgeIcon)
                .addGroup(fridgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fridgePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fridgeName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fridgePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fridgeAdd)
                        .addGap(115, 115, 115))))
        );
        fridgePanelLayout.setVerticalGroup(
            fridgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fridgePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fridgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fridgePanelLayout.createSequentialGroup()
                        .addComponent(fridgeName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fridgeAdd))
                    .addComponent(fridgeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(fridgePanel);

        microwavePanel.setBackground(new java.awt.Color(0, 255, 204));
        microwavePanel.setPreferredSize(new java.awt.Dimension(448, 150));

        microwaveIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\microwaveIcon.png")); // NOI18N

        microwaveName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        microwaveName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        microwaveName.setText("Microwave");
        microwaveName.setToolTipText("");

        microwaveAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        microwaveAdd.setText("ADD");
        microwaveAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        microwaveAdd.setInheritsPopupMenu(true);
        microwaveAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                microwaveAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout microwavePanelLayout = new javax.swing.GroupLayout(microwavePanel);
        microwavePanel.setLayout(microwavePanelLayout);
        microwavePanelLayout.setHorizontalGroup(
            microwavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(microwavePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(microwaveIcon)
                .addGroup(microwavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(microwavePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(microwaveName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, microwavePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(microwaveAdd)
                        .addGap(115, 115, 115))))
        );
        microwavePanelLayout.setVerticalGroup(
            microwavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(microwavePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(microwavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(microwavePanelLayout.createSequentialGroup()
                        .addComponent(microwaveName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(microwaveAdd))
                    .addComponent(microwaveIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(microwavePanel);

        kettlePanel.setBackground(new java.awt.Color(0, 255, 204));
        kettlePanel.setPreferredSize(new java.awt.Dimension(448, 150));

        kettleIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\kettleIcon.png")); // NOI18N

        kettleName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        kettleName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kettleName.setText("Kettle");
        kettleName.setToolTipText("");

        kettleAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        kettleAdd.setText("ADD");
        kettleAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kettleAdd.setInheritsPopupMenu(true);
        kettleAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kettleAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kettlePanelLayout = new javax.swing.GroupLayout(kettlePanel);
        kettlePanel.setLayout(kettlePanelLayout);
        kettlePanelLayout.setHorizontalGroup(
            kettlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kettlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kettleIcon)
                .addGroup(kettlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kettlePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(kettleName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kettlePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kettleAdd)
                        .addGap(115, 115, 115))))
        );
        kettlePanelLayout.setVerticalGroup(
            kettlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kettlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kettlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kettlePanelLayout.createSequentialGroup()
                        .addComponent(kettleName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kettleAdd))
                    .addComponent(kettleIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(kettlePanel);

        laptopPanel.setBackground(new java.awt.Color(0, 255, 204));
        laptopPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        laptopIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\laptopIcon.png")); // NOI18N

        laptopName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        laptopName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        laptopName.setText("Laptop");
        laptopName.setToolTipText("");

        laptopAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        laptopAdd.setText("ADD");
        laptopAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        laptopAdd.setInheritsPopupMenu(true);
        laptopAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laptopAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout laptopPanelLayout = new javax.swing.GroupLayout(laptopPanel);
        laptopPanel.setLayout(laptopPanelLayout);
        laptopPanelLayout.setHorizontalGroup(
            laptopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laptopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(laptopIcon)
                .addGroup(laptopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(laptopPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(laptopName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, laptopPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(laptopAdd)
                        .addGap(115, 115, 115))))
        );
        laptopPanelLayout.setVerticalGroup(
            laptopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laptopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(laptopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(laptopPanelLayout.createSequentialGroup()
                        .addComponent(laptopName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(laptopAdd))
                    .addComponent(laptopIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(laptopPanel);

        tvPanel.setBackground(new java.awt.Color(0, 255, 204));
        tvPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        tvIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\tvIcon.png")); // NOI18N

        tvName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        tvName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tvName.setText("Television");
        tvName.setToolTipText("");

        tvAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        tvAdd.setText("ADD");
        tvAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tvAdd.setInheritsPopupMenu(true);
        tvAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tvAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tvPanelLayout = new javax.swing.GroupLayout(tvPanel);
        tvPanel.setLayout(tvPanelLayout);
        tvPanelLayout.setHorizontalGroup(
            tvPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tvPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tvIcon)
                .addGroup(tvPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tvPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tvName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tvPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tvAdd)
                        .addGap(115, 115, 115))))
        );
        tvPanelLayout.setVerticalGroup(
            tvPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tvPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tvPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tvPanelLayout.createSequentialGroup()
                        .addComponent(tvName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tvAdd))
                    .addComponent(tvIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(tvPanel);

        washingPanel.setBackground(new java.awt.Color(0, 255, 204));
        washingPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        washingIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\washingmachineIcon.png")); // NOI18N

        washingName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        washingName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        washingName.setText("Washing Machine");
        washingName.setToolTipText("");

        washingAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        washingAdd.setText("ADD");
        washingAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        washingAdd.setInheritsPopupMenu(true);
        washingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                washingAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout washingPanelLayout = new javax.swing.GroupLayout(washingPanel);
        washingPanel.setLayout(washingPanelLayout);
        washingPanelLayout.setHorizontalGroup(
            washingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(washingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(washingIcon)
                .addGroup(washingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(washingPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(washingName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, washingPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(washingAdd)
                        .addGap(115, 115, 115))))
        );
        washingPanelLayout.setVerticalGroup(
            washingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(washingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(washingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(washingPanelLayout.createSequentialGroup()
                        .addComponent(washingName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(washingAdd))
                    .addComponent(washingIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(washingPanel);

        ironPanel.setBackground(new java.awt.Color(0, 255, 204));
        ironPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        ironIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\ironIcon.png")); // NOI18N

        ironName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        ironName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ironName.setText("Iron");
        ironName.setToolTipText("");

        ironAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        ironAdd.setText("ADD");
        ironAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ironAdd.setInheritsPopupMenu(true);
        ironAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ironAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ironPanelLayout = new javax.swing.GroupLayout(ironPanel);
        ironPanel.setLayout(ironPanelLayout);
        ironPanelLayout.setHorizontalGroup(
            ironPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ironPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ironIcon)
                .addGroup(ironPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ironPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ironName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ironPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ironAdd)
                        .addGap(115, 115, 115))))
        );
        ironPanelLayout.setVerticalGroup(
            ironPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ironPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ironPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ironPanelLayout.createSequentialGroup()
                        .addComponent(ironName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ironAdd))
                    .addComponent(ironIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(ironPanel);

        vaccumPanel.setBackground(new java.awt.Color(0, 255, 204));
        vaccumPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        vaccumIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\vaccumIcon.png")); // NOI18N

        vaccumName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        vaccumName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        vaccumName.setText("Vaccum Cleaner");
        vaccumName.setToolTipText("");

        vaccumAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        vaccumAdd.setText("ADD");
        vaccumAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        vaccumAdd.setInheritsPopupMenu(true);
        vaccumAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vaccumAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout vaccumPanelLayout = new javax.swing.GroupLayout(vaccumPanel);
        vaccumPanel.setLayout(vaccumPanelLayout);
        vaccumPanelLayout.setHorizontalGroup(
            vaccumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vaccumPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(vaccumIcon)
                .addGroup(vaccumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vaccumPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(vaccumName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vaccumPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vaccumAdd)
                        .addGap(115, 115, 115))))
        );
        vaccumPanelLayout.setVerticalGroup(
            vaccumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vaccumPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vaccumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vaccumPanelLayout.createSequentialGroup()
                        .addComponent(vaccumName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vaccumAdd))
                    .addComponent(vaccumIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(vaccumPanel);

        blenderPanel.setBackground(new java.awt.Color(0, 255, 204));
        blenderPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        blenderIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\mixerIcon.png")); // NOI18N

        blenderName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        blenderName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        blenderName.setText("Blender");
        blenderName.setToolTipText("");

        blenderAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        blenderAdd.setText("ADD");
        blenderAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        blenderAdd.setInheritsPopupMenu(true);
        blenderAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blenderAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout blenderPanelLayout = new javax.swing.GroupLayout(blenderPanel);
        blenderPanel.setLayout(blenderPanelLayout);
        blenderPanelLayout.setHorizontalGroup(
            blenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blenderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blenderIcon)
                .addGroup(blenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(blenderPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(blenderName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, blenderPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(blenderAdd)
                        .addGap(115, 115, 115))))
        );
        blenderPanelLayout.setVerticalGroup(
            blenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blenderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(blenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(blenderPanelLayout.createSequentialGroup()
                        .addComponent(blenderName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(blenderAdd))
                    .addComponent(blenderIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(blenderPanel);

        toasterPanel.setBackground(new java.awt.Color(0, 255, 204));
        toasterPanel.setPreferredSize(new java.awt.Dimension(448, 150));

        toasterIcon.setIcon(new javax.swing.ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\toasterIcon.png")); // NOI18N

        toasterName.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        toasterName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        toasterName.setText("Toaster");
        toasterName.setToolTipText("");

        toasterAdd.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        toasterAdd.setText("ADD");
        toasterAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toasterAdd.setInheritsPopupMenu(true);
        toasterAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toasterAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout toasterPanelLayout = new javax.swing.GroupLayout(toasterPanel);
        toasterPanel.setLayout(toasterPanelLayout);
        toasterPanelLayout.setHorizontalGroup(
            toasterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toasterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toasterIcon)
                .addGroup(toasterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toasterPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(toasterName, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, toasterPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(toasterAdd)
                        .addGap(115, 115, 115))))
        );
        toasterPanelLayout.setVerticalGroup(
            toasterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toasterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toasterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toasterPanelLayout.createSequentialGroup()
                        .addComponent(toasterName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(toasterAdd))
                    .addComponent(toasterIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        devicelist.add(toasterPanel);

        sidebar.setViewportView(devicelist);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(heading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sidebar)
                            .addComponent(deviceinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(devicearea, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(heading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deviceinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(devicearea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        sidebar.getVerticalScrollBar().setUnitIncrement(16);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fanAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fanAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/fanIcon.png")), "Fan", 0);
    }//GEN-LAST:event_fanAddActionPerformed

    private void acAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/acIcon.png")), "Air_Conditioner", 1);
    }//GEN-LAST:event_acAddActionPerformed

    private void heaterAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heaterAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/heaterIcon.png")), "Heater", 2);
    }//GEN-LAST:event_heaterAddActionPerformed

    private void ledAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/ledIcon.png")), "Led", 3);
    }//GEN-LAST:event_ledAddActionPerformed

    private void fridgeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fridgeAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/fridgeIcon.png")), "Fridge", 4);
    }//GEN-LAST:event_fridgeAddActionPerformed

    private void microwaveAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_microwaveAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/microwaveIcon.png")), "Microwave", 5);
    }//GEN-LAST:event_microwaveAddActionPerformed

    private void kettleAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kettleAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/kettleIcon.png")), "Kettle", 6);
    }//GEN-LAST:event_kettleAddActionPerformed

    private void laptopAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laptopAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/laptopIcon.png")), "Laptop", 7);
    }//GEN-LAST:event_laptopAddActionPerformed

    private void tvAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tvAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/tvIcon.png")), "Television", 8);
    }//GEN-LAST:event_tvAddActionPerformed

    private void washingAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_washingAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/washingmachineIcon.png")), "Washing_Machine", 9);
    }//GEN-LAST:event_washingAddActionPerformed

    private void ironAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ironAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/ironIcon.png")), "Iron", 10);
    }//GEN-LAST:event_ironAddActionPerformed

    private void vaccumAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vaccumAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/vaccumIcon.png")), "Vaccum_Cleaner", 11);
    }//GEN-LAST:event_vaccumAddActionPerformed

    private void blenderAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blenderAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/mixerIcon.png")), "Blender", 12);
    }//GEN-LAST:event_blenderAddActionPerformed

    private void toasterAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toasterAddActionPerformed
        placeDevice(new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/toasterIcon.png")), "Toaster", 13);
    }//GEN-LAST:event_toasterAddActionPerformed

    private void deviceUsageTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deviceUsageTimeActionPerformed

    }//GEN-LAST:event_deviceUsageTimeActionPerformed

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutActionPerformed
        logOut();
    }//GEN-LAST:event_logOutActionPerformed

    public static void main(String args[]) {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acAdd;
    private javax.swing.JLabel acIcon;
    private javax.swing.JLabel acName;
    private javax.swing.JPanel acPanel;
    private javax.swing.JButton blenderAdd;
    private javax.swing.JLabel blenderIcon;
    private javax.swing.JLabel blenderName;
    private javax.swing.JPanel blenderPanel;
    private javax.swing.JButton calculate;
    private javax.swing.JPanel device1;
    private javax.swing.JPanel device2;
    private javax.swing.JPanel device3;
    private javax.swing.JPanel device4;
    private javax.swing.JPanel device5;
    private javax.swing.JPanel device6;
    private javax.swing.JPanel device7;
    private javax.swing.JPanel device8;
    private javax.swing.JPanel device9;
    private javax.swing.JLabel deviceCost;
    private javax.swing.JLabel deviceCostLabel;
    private javax.swing.JLabel deviceCostPerUsage;
    private javax.swing.JLabel deviceCostPerUsageLabel;
    private javax.swing.JLabel deviceName;
    private javax.swing.JLabel deviceNameLabel;
    private javax.swing.JLabel deviceUsage;
    private javax.swing.JTextField deviceUsageTime;
    private javax.swing.JLabel deviceWatt;
    private javax.swing.JLabel deviceWattLabel;
    private javax.swing.JPanel devicearea;
    private javax.swing.JPanel deviceinfo;
    private javax.swing.JPanel devicelist;
    private javax.swing.JPanel deviceplace;
    private javax.swing.JButton fanAdd;
    private javax.swing.JLabel fanIcon;
    private javax.swing.JLabel fanName;
    private javax.swing.JPanel fanPanel;
    private javax.swing.JButton fridgeAdd;
    private javax.swing.JLabel fridgeIcon;
    private javax.swing.JLabel fridgeName;
    private javax.swing.JPanel fridgePanel;
    private javax.swing.JLabel globalCostPerHr;
    private javax.swing.JLabel globalCostPerHrLabel;
    private javax.swing.JPanel heading;
    private javax.swing.JButton heaterAdd;
    private javax.swing.JLabel heaterIcon;
    private javax.swing.JLabel heaterName;
    private javax.swing.JPanel heaterPanel;
    private javax.swing.JButton ironAdd;
    private javax.swing.JLabel ironIcon;
    private javax.swing.JLabel ironName;
    private javax.swing.JPanel ironPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton kettleAdd;
    private javax.swing.JLabel kettleIcon;
    private javax.swing.JLabel kettleName;
    private javax.swing.JPanel kettlePanel;
    private javax.swing.JButton laptopAdd;
    private javax.swing.JLabel laptopIcon;
    private javax.swing.JLabel laptopName;
    private javax.swing.JPanel laptopPanel;
    private javax.swing.JButton ledAdd;
    private javax.swing.JLabel ledIcon;
    private javax.swing.JLabel ledName;
    private javax.swing.JPanel ledPanel;
    private javax.swing.JButton logOut;
    private javax.swing.JButton microwaveAdd;
    private javax.swing.JLabel microwaveIcon;
    private javax.swing.JLabel microwaveName;
    private javax.swing.JPanel microwavePanel;
    private javax.swing.JLabel name;
    private javax.swing.JButton reset;
    private javax.swing.JScrollPane sidebar;
    private javax.swing.JButton toasterAdd;
    private javax.swing.JLabel toasterIcon;
    private javax.swing.JLabel toasterName;
    private javax.swing.JPanel toasterPanel;
    private javax.swing.JButton tvAdd;
    private javax.swing.JLabel tvIcon;
    private javax.swing.JLabel tvName;
    private javax.swing.JPanel tvPanel;
    private javax.swing.JLabel user;
    private javax.swing.JButton vaccumAdd;
    private javax.swing.JLabel vaccumIcon;
    private javax.swing.JLabel vaccumName;
    private javax.swing.JPanel vaccumPanel;
    private javax.swing.JButton washingAdd;
    private javax.swing.JLabel washingIcon;
    private javax.swing.JLabel washingName;
    private javax.swing.JPanel washingPanel;
    // End of variables declaration//GEN-END:variables
}
