//package Controllers;
//
//
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//import javafx.scene.Parent;
//import javafx.scene.input.MouseEvent;
//
//
//public class DashboardController implements Initializable {
// 
//	  @FXML
//	    private Button page_accueil;
//
//	    @FXML
//	    private Button page_livres;
//
//	    @FXML
//	    private Button page_lecteurs;
//
//	    @FXML
//	    private Button page_emprunts;
//	    @FXML
//	    private void handleMenuButtonAction(MouseEvent event) throws IOException {
//	        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//	        Parent myNewScene = null;
//
//	        if (event.getSource() == page_accueil) {
//	            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Dashboard.fxml"));
//	            Scene scene = new Scene(myNewScene);
//		        stage.setScene(scene);
//		        stage.setTitle("Accueil");
//		        stage.show();
//	        } else if (event.getSource() == page_livres) {
//	            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Livre.fxml"));
//	            Scene scene = new Scene(myNewScene);
//		        stage.setScene(scene);
//		        stage.setTitle("Livres");
//		        stage.show();
//	        } else if (event.getSource() == page_lecteurs) {
//	            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Lecteur.fxml"));
//	            Scene scene = new Scene(myNewScene);
//		        stage.setScene(scene);
//		        stage.setTitle("Lecteurs");
//		        stage.show();
//	        } else if (event.getSource() == page_emprunts) {
//	            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Emprunt.fxml"));
//	            Scene scene = new Scene(myNewScene);
//		        stage.setScene(scene);
//		        stage.setTitle("Emprunts");
//		        stage.show();
//	        } 
//
//	       
//	    }
//
//	   
//
//	    
//	@Override
//	    public void initialize(URL arg0, ResourceBundle arg1) {
//	     
//	    }
//
//}

package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.ConnexionDb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DashboardController implements Initializable {
    
    @FXML
    private Button page_accueil;

    @FXML
    private Button page_livres;

    @FXML
    private Button page_lecteurs;

    @FXML
    private Button page_emprunts;
    
    // Textes pour les statistiques
    @FXML
    private Text totalLivresText;
    
    @FXML
    private Text lecteursActifsText;
    
    @FXML
    private Text empruntsEnCoursText;
    
    @FXML
    private Text variationLivresText;
    
    @FXML
    private Text variationLecteursText;
    
    @FXML
    private Text variationEmpruntsText;

    @FXML
    private void handleMenuButtonAction(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent myNewScene = null;

        if (event.getSource() == page_accueil) {
            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Dashboard.fxml"));
            Scene scene = new Scene(myNewScene);
            stage.setScene(scene);
            stage.setTitle("Accueil");
            stage.show();
        } else if (event.getSource() == page_livres) {
            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Livre.fxml"));
            Scene scene = new Scene(myNewScene);
            stage.setScene(scene);
            stage.setTitle("Livres");
            stage.show();
        } else if (event.getSource() == page_lecteurs) {
            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Lecteur.fxml"));
            Scene scene = new Scene(myNewScene);
            stage.setScene(scene);
            stage.setTitle("Lecteurs");
            stage.show();
        } else if (event.getSource() == page_emprunts) {
            myNewScene = FXMLLoader.load(getClass().getResource("/Scenes/Emprunt.fxml"));
            Scene scene = new Scene(myNewScene);
            stage.setScene(scene);
            stage.setTitle("Emprunts");
            stage.show();
        }
    }
    
    /**
     * Charge les statistiques réelles depuis la base de données
     */
    private void chargerStatistiques() {
        try (Connection con = ConnexionDb.Connect()) {
            
            // Statistique 1: Total des livres
            int totalLivres = getTotalLivres(con);
            if (totalLivresText != null) {
                totalLivresText.setText(String.valueOf(totalLivres));
            }
            
            // Statistique 2: Lecteurs actifs (tous les lecteurs)
            int totalLecteurs = getTotalLecteurs(con);
            if (lecteursActifsText != null) {
                lecteursActifsText.setText(String.valueOf(totalLecteurs));
            }
            
            // Statistique 3: Emprunts en cours (livres non disponibles)
            int empruntsEnCours = getEmpruntsEnCours(con);
            if (empruntsEnCoursText != null) {
                empruntsEnCoursText.setText(String.valueOf(empruntsEnCours));
            }
            
            // Calcul des variations (simulation pour l'exemple)
            calculerVariations(totalLivres, totalLecteurs, empruntsEnCours);
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des statistiques");
            
            // Valeurs par défaut en cas d'erreur
            if (totalLivresText != null) totalLivresText.setText("0");
            if (lecteursActifsText != null) lecteursActifsText.setText("0");
            if (empruntsEnCoursText != null) empruntsEnCoursText.setText("0");
        }
    }
    
    /**
     * Récupère le nombre total de livres
     */
    private int getTotalLivres(Connection con) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM livre";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    /**
     * Récupère le nombre total de lecteurs
     */
    private int getTotalLecteurs(Connection con) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM lecteur";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    /**
     * Récupère le nombre d'emprunts en cours
     */
    private int getEmpruntsEnCours(Connection con) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM livre WHERE disponible = 'Non disponible'";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    /**
     * Calcule les variations en pourcentage (simulation)
     * Dans une vraie application, vous compareriez avec le mois précédent
     */
    private void calculerVariations(int livres, int lecteurs, int emprunts) {
        // Simulation de variations (à remplacer par de vraies données historiques)
        if (variationLivresText != null) {
            int variationLivres = (int)(Math.random() * 20) - 5; // Entre -5 et +15%
            String signe = variationLivres >= 0 ? "↑" : "↓";
            variationLivresText.setText(signe + " " + Math.abs(variationLivres) + "% ce mois");
            
            // Changer la couleur selon la variation
            if (variationLivres >= 0) {
                variationLivresText.setStyle("-fx-fill: #10B981;");
            } else {
                variationLivresText.setStyle("-fx-fill: #EF4444;");
            }
        }
        
        if (variationLecteursText != null) {
            int variationLecteurs = (int)(Math.random() * 15) - 3; // Entre -3 et +12%
            String signe = variationLecteurs >= 0 ? "↑" : "↓";
            variationLecteursText.setText(signe + " " + Math.abs(variationLecteurs) + "% ce mois");
            
            if (variationLecteurs >= 0) {
                variationLecteursText.setStyle("-fx-fill: #10B981;");
            } else {
                variationLecteursText.setStyle("-fx-fill: #EF4444;");
            }
        }
        
        if (variationEmpruntsText != null) {
            // Pour les emprunts, une augmentation peut être négative
            int variationEmprunts = (int)(Math.random() * 10) - 5; // Entre -5 et +5%
            String signe = variationEmprunts >= 0 ? "↑" : "↓";
            variationEmpruntsText.setText(signe + " " + Math.abs(variationEmprunts) + "% ce mois");
            
            // Pour les emprunts, on inverse: augmentation = rouge, diminution = vert
            if (variationEmprunts >= 0) {
                variationEmpruntsText.setStyle("-fx-fill: #EF4444;");
            } else {
                variationEmpruntsText.setStyle("-fx-fill: #10B981;");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger les statistiques au démarrage
        chargerStatistiques();
    }
}