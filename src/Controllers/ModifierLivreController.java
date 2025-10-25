package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

public class ModifierLivreController {

    @FXML
    private Button page_accueil;

    @FXML
    private Button page_livres;

    @FXML
    private Button page_lecteurs;

    @FXML
    private Button page_emprunts;

    @FXML
    private TextField input_titre;

    @FXML
    private TextField input_auteur;

    @FXML
    private TextField input_isbn;

    @FXML
    private Button modifier_livre;
    
    private LivreModel livreAEditer;

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
    public void setLivreAEditer(LivreModel livre) {
        this.livreAEditer = livre;

        input_titre.setText(livre.getTitre());
        input_auteur.setText(livre.getAuteur());
        input_isbn.setText(livre.getCodeISBN());
    }

    @FXML
    void modifierLivre(MouseEvent event) {
        String nouveauTitre = input_titre.getText().trim();
        String nouveauAuteur = input_auteur.getText().trim();
        String nouveauISBN = input_isbn.getText().trim();

        if (nouveauTitre.isEmpty() || nouveauAuteur.isEmpty() || nouveauISBN.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        String query = "UPDATE livre SET titre = ?, auteur = ?, codeISBN = ? WHERE code = ?";
        try (Connection connection = ConnexionDb.Connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, nouveauTitre);
            pstmt.setString(2, nouveauAuteur);
            pstmt.setString(3, nouveauISBN);
            pstmt.setLong(4, livreAEditer.getCode());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Succès", "Livre modifié avec succès.", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Erreur", "La mise à jour du livre a échoué.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur SQL lors de la modification du livre.", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) modifier_livre.getScene().getWindow();
        stage.close();

        input_titre.clear();
        input_auteur.clear();
        input_isbn.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

  
   


}

	
	

