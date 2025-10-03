package com.mycompany.ecs;

public class SimulatorCanvas extends javax.swing.JFrame {

//    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SimulatorCanvas.class.getName());

    public SimulatorCanvas() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Energy Consumption Simulator"); //Title
        setDefaultCloseOperation(SimulatorCanvas.EXIT_ON_CLOSE); //Completely closes the jframe application
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 753, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 707, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new SimulatorCanvas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
