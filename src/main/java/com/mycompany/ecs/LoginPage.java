package com.mycompany.ecs;

import java.sql.*;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginPage.class.getName());

    public LoginPage() {
        initComponents();
        setResizable(false);
        setSize(510, 400);

        ImageIcon img = new ImageIcon(getClass().getResource("/com/mycompany/ecs/Icons/mainLogo.png"));
        this.setIconImage(img.getImage());
    }

    private void loginECS() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        String query = "SELECT * FROM user_appliances WHERE username = ? AND password = ?";

        try (Connection con = JDBCConnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                java.awt.EventQueue.invokeLater(() -> new ECSFrame(user).setVisible(true));
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openRegister() {
        java.awt.EventQueue.invokeLater(() -> new RegisterPage().setVisible(true));
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        setTitle("ECS LOGIN");
        setPreferredSize(new java.awt.Dimension(520, 400));

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
        loginBTN.setFocusPainted(false);
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
        pageInfo.setText("LOGIN PAGE");
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
                .addContainerGap(84, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(baseplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(baseplate, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed

    }//GEN-LAST:event_usernameFieldActionPerformed

    private void registerBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBTNActionPerformed
        openRegister();
    }//GEN-LAST:event_registerBTNActionPerformed

    private void loginBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBTNActionPerformed
        loginECS();
    }//GEN-LAST:event_loginBTNActionPerformed

    public static void main(String args[]) {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel baseplate;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel heading;
    private javax.swing.JButton loginBTN;
    private javax.swing.JLabel pageInfo;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton registerBTN;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
