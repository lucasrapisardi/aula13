import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Tela extends JFrame implements ActionListener {

   private JLabel lblPesquisa = new JLabel("Digite Quantas Temperaturas:");
   private JFrame frame = new JFrame("Temperaturas");;
   private JTextArea lista = new JTextArea(25,33);
   private JPanel painelDePesquisa = new JPanel(new FlowLayout());
   private JTextField pesquisa = new JTextField(10);
   private JButton botaoP = new JButton("Listar");
   private JScrollPane scroll = new JScrollPane(lista);
   private Temperatura[] temp;
   private Connection conn;
   private Temperatura temperatura;

  public Tela(){
      lista.setBackground(new Color(200,215,230));
      lista.setLineWrap(true);
      lista.setWrapStyleWord(true);
      lista.setEditable(false);
      scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      painelDePesquisa.add(lblPesquisa);
      painelDePesquisa.add(pesquisa);
      botaoP.addActionListener(this);
      painelDePesquisa.add(botaoP);
      painelDePesquisa.add(scroll);
      frame.add(painelDePesquisa);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setSize(400, 500);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setResizable(false);
      
   }
   
   public void actionPerformed(ActionEvent e){
      if(e.getSource() == botaoP){
         try{
            ConexaoBD bd = new ConexaoBD();
            conn = bd.conectar();
            conn.setAutoCommit(false);
            int numero = Integer.parseInt(pesquisa.getText());
            for(int i = 0; i < numero; i++){
               temperatura = new Temperatura();
               temperatura.setValor(40*Math.random());
               temperatura.incluir(conn);
            }
            conn.commit();
            Termometro t = new Termometro();
            Temperatura[] temp = t.ultimosDias(conn, numero);
            for(int i = 0; i < temp.length; i++){
               lista.append(temp[i] + "\n");
            }
            lista.append("\nMaior:  " + t.maior(temp));
            lista.append("\n\nMenor:  " + t.menor(temp));
            lista.append("\n\nMedia:  " + t.media(temp));
         } catch (Exception e1){
            e1.printStackTrace();
            if (conn != null) {
               try {
                  conn.rollback();
               }catch (Exception e2){
                  System.out.print(e2.getStackTrace());
               }
            }
         }
         finally{
            if(conn != null){
               try{
                  conn.close();
               }
               catch(SQLException e1){
                  System.out.print(e1.getStackTrace());
               }
            }
         }  
      }
   }
   public static void main (String [] args){
      new Tela();
   }
}