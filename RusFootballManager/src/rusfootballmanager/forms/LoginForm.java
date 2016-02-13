package rusfootballmanager.forms;

import rusfootballmanager.Trainer;

/**
 *
 * @author Алексей
 */
public class LoginForm extends javax.swing.JDialog {

    private Trainer trainer;

    public LoginForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfLogin = new javax.swing.JTextField();
        bEnter = new javax.swing.JButton();
        bNewCareer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Вход");

        jLabel1.setText("Введите имя:");

        tfLogin.setText("alex");

        bEnter.setText("Войти");
        bEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEnterActionPerformed(evt);
            }
        });

        bNewCareer.setText("Новая карьера");
        bNewCareer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNewCareerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfLogin)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(bEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bNewCareer, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(tfLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bEnter)
                .addGap(18, 18, 18)
                .addComponent(bNewCareer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEnterActionPerformed
        trainer = Trainer.load(tfLogin.getText());
    }//GEN-LAST:event_bEnterActionPerformed

    private void bNewCareerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNewCareerActionPerformed
        trainer = Trainer.newInstance(tfLogin.getText());
    }//GEN-LAST:event_bNewCareerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEnter;
    private javax.swing.JButton bNewCareer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField tfLogin;
    // End of variables declaration//GEN-END:variables

    public Trainer getTrainer() {
        return trainer;
    }

}
