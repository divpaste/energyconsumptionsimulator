package com.mycompany.ecs;

import javax.swing.*;

import java.sql.*;

public class RegisterPage extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegisterPage.class.getName());

    public RegisterPage() {
        initComponents();
        setResizable(false);
        setSize(510, 400);

        ImageIcon img = new ImageIcon("C:\\Coding\\NetBeans\\ECS\\src\\main\\resources\\com\\mycompany\\ecs\\Icons\\mainLogo.png");
        this.setIconImage(img.getImage());
    }

    private void registerECS() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String query = "INSERT INTO user_appliances (username, password) VALUES (?, ?)";

        try (
                Connection con = JDBCConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            int row = pstmt.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Register successfull!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openLogin() {
        java.awt.EventQueue.invokeLater(() -> new LoginPage().setVisible(true));
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        baseplate = new javax.swing.JPanel();
        heading = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        loginBTN = new javax.swing.JButton();
        registerBTN = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        pageInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ECS REGISTER");

        baseplate.setBackground(new java.awt.Color(255, 255, 102));
        baseplate.setPreferredSize(new java.awt.Dimension(500, 500));

        heading.setBackground(new java.awt.Color(204, 255, 102));
        heading.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        heading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heading.setText("ENERGY CONSUMPTION SIMULATOR");

        usernameLabel.setFont(new java.awt.Font("Swis721 BT", 1, 18)); // NOI18N
        usernameLabel.setText("USERNAME:");

        usernameField.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        usernameField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        passwordLabel.setFont(new java.awt.Font("Swis721 BT", 1, 18)); // NOI18N
        passwordLabel.setText("PASSWORD:");

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new java.awt.GridLayout(1, 2, 5, 10));

        loginBTN.setBackground(new java.awt.Color(204, 255, 102));
        loginBTN.setFont(new java.awt.Font("Swis721 Ex BT", 1, 18)); // NOI18N
        loginBTN.setText("LOGIN");
        loginBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 153, 0), 5, true));
        loginBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBTNActionPerformed(evt);
            }
        });
        buttonPanel.add(loginBTN);

        registerBTN.setBackground(new java.awt.Color(255, 102, 102));
        registerBTN.setFont(new java.awt.Font("Swis721 Ex BT", 1, 18)); // NOI18N
        registerBTN.setText("REGISTER");
        registerBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 0, 51), 5, true));
        registerBTN.setFocusPainted(false);
        registerBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBTNActionPerformed(evt);
            }
        });
        buttonPanel.add(registerBTN);

        passwordField.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        passwordField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        pageInfo.setFont(new java.awt.Font("Segoe UI Variable", 1, 24)); // NOI18N
        pageInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageInfo.setText("REGISTER PAGE");
        pageInfo.setToolTipText("");

        javax.swing.GroupLayout baseplateLayout = new javax.swing.GroupLayout(baseplate);
        baseplate.setLayout(baseplateLayout);
        baseplateLayout.setHorizontalGroup(
            baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(baseplateLayout.createSequentialGroup()
                .addGroup(baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, baseplateLayout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addComponent(heading, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(baseplateLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(baseplateLayout.createSequentialGroup()
                                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addGroup(baseplateLayout.createSequentialGroup()
                                .addGroup(baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(usernameLabel)
                                    .addComponent(passwordLabel))
                                .addGap(18, 18, 18)
                                .addGroup(baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(baseplateLayout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(pageInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        baseplateLayout.setVerticalGroup(
            baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(baseplateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(heading, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pageInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(baseplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(184, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(baseplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(baseplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed

    }//GEN-LAST:event_usernameFieldActionPerformed

    private void loginBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBTNActionPerformed
        openLogin();
    }//GEN-LAST:event_loginBTNActionPerformed

    private void registerBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBTNActionPerformed
        registerECS();
    }//GEN-LAST:event_registerBTNActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new RegisterPage().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel baseplate;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel heading;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loginBTN;
    private javax.swing.JLabel pageInfo;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton registerBTN;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
