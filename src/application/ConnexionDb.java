package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import Model.EmpruntModel;

public class ConnexionDb {
    public static Connection Connect() {
        String BDD = "tpjava";
        String url = "jdbc:mysql://localhost:3306/" + BDD;
        String username = "root";
        String password = "";
        String requete1 = "SELECT * FROM livre";
        String requete2="select * from livre where titre like 'L%' ";
        String requete3="select * from lecteur where age<30";
        String requete4="select * from emprunt where cin =12345678";
    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("connected");
            Statement stmt = con.createStatement();
            ResultSet resultats1 = stmt.executeQuery(requete1);
            System.out.println("****************** les livres ************");
            while(resultats1.next()) {
            	System.out.println("code: "+resultats1.getLong(1));
            	System.out.println("titre: "+resultats1.getNString(2));
            	System.out.println("auteur: "+resultats1.getNString(3));
            	System.out.println("code ISBN: "+resultats1.getLong(4));
            	System.out.println("********************************");
            }
            ResultSet resultats2 = stmt.executeQuery(requete2);
            System.out.println("****************** les livres dont le titre commence par L************");
            while (resultats2.next()) {
            	System.out.println("code: "+resultats2.getLong(1));
           	    System.out.println("titre: "+resultats2.getNString(2));
            	System.out.println("auteur: "+resultats2.getNString(3));
            	System.out.println("code ISBN: "+resultats2.getLong(4));
                System.out.println("********************************");
            }

            ResultSet resultats3 = stmt.executeQuery(requete3);
            System.out.println("****************** Les lecteurs dont l'age est inferieure a 30************");
            while (resultats3.next()) {
            	 System.out.println("cin: "+resultats3.getLong(1));
               System.out.println("nom: "+resultats3.getString(2));
               System.out.println("prenom: "+resultats3.getString(3));
               System.out.println("age: "+resultats3.getInt(4));
                System.out.println("********************************");
            }

            ResultSet resultats4 = stmt.executeQuery(requete4);
            System.out.println("****************** les emprunts du client qui a le CIN : 12345678 ************");
            while (resultats4.next()) {
            	 System.out.println("id_emprunt: "+resultats4.getInt(1));
               System.out.println("date_emprunt: "+resultats4.getDate(2));
               System.out.println("date_retour: "+resultats4.getDate(3));
               System.out.println("cin du lecteur: "+resultats4.getLong(4));
               System.out.println("code livre : "+resultats4.getInt(5));
                System.out.println("********************************");
            }
            /*********************************************************************************/
            /*********************************************************************************/
            /*********************************************************************************/

          
            
            
            return con;

          
          
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("error");
            return null;
        }
     


        
    }
   
  
}
