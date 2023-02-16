import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TicketBookingForm extends JFrame{
    private JComboBox comboTicket;
    private JTextField textName;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JRadioButton otherRadioButton;
    private JComboBox comboClass;
    private JTextField textEmail;
    private JButton newButton;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JPanel BookingForm;

    public TicketBookingForm() {
        this.add(BookingForm);
        this.pack();
        refresh();

    cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/ticket_booking","root","password");
                    PreparedStatement pst = con.prepareStatement("insert into ticket (name,gen,class,email) values (?,?,?,?)");
                    pst.setString(1,textName.getText().trim());
                    String gen = null;
                    if(maleRadioButton.isSelected())
                        gen="Male";
                    else if (femaleRadioButton.isSelected())
                        gen="Female";
                    else
                        gen="Other";

                    pst.setString(2,gen);
                    pst.setString(3,comboClass.getSelectedItem().toString().trim());
                    pst.setString(4,textEmail.getText().trim());

                    pst.executeUpdate();
                    clear();
                    JOptionPane.showMessageDialog(null,"Data Saved");
                    comboTicket.removeAllItems();
                    refresh();

                }catch (Exception ex){
                    System.out.println("Error"+ex);
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/ticket_booking", "root", "password");
                    PreparedStatement pst = con.prepareStatement("update ticket set name=?,gen=?,class=?,email=? where num="+Integer.parseInt(comboTicket.getSelectedItem().toString().trim()));
                    pst.setString(1,textName.getText().trim());
                    String gen = null;
                    if(maleRadioButton.isSelected())
                        gen="male";
                    else if (femaleRadioButton.isSelected()) {
                        gen="female";
                    }
                    else gen="others";
                    pst.setString(2,gen);
                    pst.setString(3,comboClass.getSelectedItem().toString().trim());
                    pst.setString(4, textEmail.getText().trim());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Data Updated");
                    clear();

                }catch(Exception ex){
                    System.out.println("Error"+ex);
                }
            }
        });
        comboTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/ticket_booking", "root", "password");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("Select * from ticket where num="+Integer.parseInt(comboTicket.getSelectedItem().toString().trim()));
                    while(rs.next()){
                        textName.setText(rs.getString(2));
                        String gen = rs.getString(3);
                        if(gen.equals("Male"))
                            maleRadioButton.setSelected(true);
                        else if (gen.equals("female"))
                            femaleRadioButton.setSelected(true);
                        else
                            otherRadioButton.setSelected(true);
                        comboClass.setSelectedItem(rs.getString(4));
                        textEmail.setText(rs.getString(5));

                    }

                }catch(Exception ex){
                    System.out.println("Error"+ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/ticket_booking", "root", "password");
                    PreparedStatement pst = con.prepareStatement("delete from ticket where num="+Integer.parseInt(comboTicket.getSelectedItem().toString().trim()));
                    pst.executeUpdate();
                    clear();
                    comboTicket.removeAllItems();
                    refresh();

                }catch(Exception ex){
                    System.out.println("Error"+ex);
                }
            }
        });
    }

    public void refresh(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/ticket_booking","root","password");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from ticket");
            while(rs.next()){  //until empty
                comboTicket.addItem(rs.getInt(1));

            }
        }catch(Exception ex){
            System.out.println("Error "+ex);
        }
    }
    public void clear() {
        comboTicket.setSelectedItem("");
        textName.setText("");
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        otherRadioButton.setSelected(false);
        comboClass.setSelectedItem("");
        textEmail.setText("");
    }

    public static void main(String[] args) {
        TicketBookingForm obj = new TicketBookingForm();
        obj.setVisible(true);
    }
}
