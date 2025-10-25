package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.LecteurModel;
import Model.LivreModel;
import application.ConnexionDb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ModifierLecteurController {

    @FXML
    private Button page_accueil;

    @FXML
    private Button page_livres;

    @FXML
    private Button page_lecteurs;

    @FXML
    private Button page_emprunts;

    @FXML
    private TextField input_nom;

    @FXML
    private TextField input_prenom;

    @FXML
    private TextField input_age;

    @FXML
    private Button modifier_lecteur;
    
    private LecteurModel lecteurAEditer;


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
    public void setLecteurAEditer(LecteurModel lecteur) {
        this.lecteurAEditer = lecteur;

         input_nom.setText(lecteur.getNom());
         input_prenom.setText(lecteur.getPrenom());
        int age= lecteur.getAge();
        //input_age.setText(lecteur.getAge());
         input_age.setText(String.valueOf(age));
    }

    @FXML
    void modifierLecteur(MouseEvent event) {
        String nouveauNom = input_nom.getText().trim();
        String nouveauPrenom = input_prenom.getText().trim();
        String nouveauAge = input_age.getText().trim();

        if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty() || nouveauAge.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        String query = "UPDATE lecteur SET nom = ?, prenom = ?, age = ? WHERE cin = ?";
        try (Connection connection = ConnexionDb.Connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, nouveauNom);
            pstmt.setString(2, nouveauPrenom);
            pstmt.setInt(3, Integer.parseInt(nouveauAge));
            pstmt.setLong(4, lecteurAEditer.getCin());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Succès", "Lecteur modifié avec succès.", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Erreur", "La mise à jour du lecteur a échoué.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur SQL lors de la modification du lecteur.", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) modifier_lecteur.getScene().getWindow();
        stage.close();

        input_nom.clear();
        input_prenom.clear();
        input_age.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

  
   



}
