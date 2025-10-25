package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import Model.EmpruntModel;
import application.ConnexionDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EmpruntController implements Initializable {
	 @FXML
	    private Button page_accueil;

	    @FXML
	    private Button page_livres;

	    @FXML
	    private Button page_lecteurs;

	    @FXML
	    private Button page_emprunts;
	    @FXML
	    private TableView<EmpruntModel> tab_emprunt;

	    @FXML
	    private TableColumn<EmpruntModel, Long> colonne_id_emprunt;

	    @FXML
	    private TableColumn<EmpruntModel, Date> colonne_date_emprunt;

	    @FXML
	    private TableColumn<EmpruntModel, Date> colonne_date_retour;

	    @FXML
	    private TableColumn<EmpruntModel, String> colonne_lecteur;

	    @FXML
	    private TableColumn<EmpruntModel, String> colonne_livre;
	    
	  

	    @FXML
	    private TableColumn<EmpruntModel, String> colonne_action;

	    @FXML
	    private TextField input_date_emprunt;

	    @FXML
	    private TextField input_date_retour;

	    @FXML
	    private TextField input_lecteur;

	    @FXML
	    private TextField input_livre;

	    @FXML
	    private Button ajouter_emprunt;

	   

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
	    @SuppressWarnings("exports")
	  		public ObservableList<EmpruntModel> data = FXCollections.observableArrayList();
	    
	    @FXML
	    private void showEmprunt() {
	        tab_emprunt.getItems().clear();

	        String requete = "SELECT e.id_emprunt, e.date_emprunt, e.date_retour,l.cin,li.code, l.nom, l.prenom, li.titre, li.codeISBN " +
	                         "FROM emprunt e " +
	                         "INNER JOIN lecteur l ON e.cin = l.cin " +
	                         "INNER JOIN livre li ON e.code = li.code " +
	                         "WHERE li.disponible like 'Non disponible' ";

	        try (Connection con = ConnexionDb.Connect();
	             PreparedStatement pstmt = con.prepareStatement(requete);
	             ResultSet resultSet = pstmt.executeQuery()) {

	        	while (resultSet.next()) {
	        		Long idEmprunt = resultSet.getLong("id_emprunt");
	        	    Date dateEmprunt = resultSet.getDate("date_emprunt");
	        	    Date dateRetour = resultSet.getDate("date_retour");
	        	    Long cin = resultSet.getLong("cin"); 
	        	    Long code = resultSet.getLong("code"); 
	        	    String lecteur = resultSet.getString("nom") + " " + resultSet.getString("prenom");
	        	    String livre = resultSet.getString("titre") + " (ISBN: " + resultSet.getLong("codeISBN") + ")";

	        	    EmpruntModel emprunt = new EmpruntModel(idEmprunt, dateEmprunt, dateRetour, lecteur, livre);
	        	    data.add(emprunt);
	        	    
	        	}

	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Erreur SQL lors de la récupération des emprunts.");
	        }
	        colonne_id_emprunt.setCellValueFactory(new PropertyValueFactory<EmpruntModel,Long>("idEmprunt"));
  	    	colonne_date_emprunt.setCellValueFactory(new PropertyValueFactory<EmpruntModel,Date>("dateEmprunt"));
  	    	colonne_date_retour.setCellValueFactory(new PropertyValueFactory<EmpruntModel,Date>("dateRetour"));
  	    	colonne_lecteur.setCellValueFactory(new PropertyValueFactory<EmpruntModel,String>("lecteur"));
  	    	colonne_livre.setCellValueFactory(new PropertyValueFactory<EmpruntModel,String>("livre"));

  	    	tab_emprunt.setItems(data);
  	    	 colonne_action.setCellFactory(col -> new TableCell<EmpruntModel, String>() {
		            final Button retourneButton = new Button("Retourner livre");

		            {
		            	retourneButton.setOnAction(event -> {
		            		EmpruntModel emprunt = (EmpruntModel) getTableView().getItems().get(getIndex());
		            		retournerLivre(emprunt);
		                });
		            	retourneButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #3B82F6, #2563EB) !important; -fx-text-fill: white !important;");
		            }

		            @Override
		            protected void updateItem(String item, boolean empty) {
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                    setGraphic(retourneButton);
		                    setText(null);
		                }
		            }
		        });
  	    	
  	    	
	    }
	    

	    private void retournerLivre(EmpruntModel emprunt) {
	        String selectQuery = "SELECT code FROM emprunt WHERE id_emprunt=?";
	        String updateQuery = "UPDATE livre SET disponible='Disponible' WHERE code=?";
	        
	        try (Connection connection = ConnexionDb.Connect();
	             PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
	             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

	            selectStmt.setLong(1, emprunt.getIdEmprunt());
	            
	            try (ResultSet resultSet = selectStmt.executeQuery()) {
	                if (resultSet.next()) {
	                    Long code = resultSet.getLong("code");

	                    updateStmt.setLong(1, code);
	                    
	                    updateStmt.executeUpdate();

	                    showAlert("Succès", "Livre retourné avec succès.", AlertType.INFORMATION);
	                    showEmprunt();
	                } else {
	                    showAlert("Erreur", "Aucun enregistrement trouvé pour cet emprunt.", AlertType.ERROR);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Erreur SQL lors du retour du livre.", AlertType.ERROR);
	        }
	    }

	    
	    
	    
	    
	   
	    private boolean isLivreDisponible(long codeLivre) {
	        String requete = "SELECT disponible FROM livre WHERE code = ?";
	        try (Connection con = ConnexionDb.Connect();
	             PreparedStatement pstmt = con.prepareStatement(requete)) {

	            pstmt.setLong(1, codeLivre);

	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                if (resultSet.next()) {
	                    String disponible = resultSet.getString("disponible");
	                    return "Disponible".equalsIgnoreCase(disponible);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Erreur SQL lors de la vérification de la disponibilité du livre.");
	        }
	        return false;
	    }
	    @FXML
	    private void ajouterEmprunt(MouseEvent event) {
	    	 String inputDateEmprunt = input_date_emprunt.getText();
	            String inputDateRetour = input_date_retour.getText();
	            String inputLivre = input_livre.getText();
	            String inputLecteur = input_lecteur.getText();

	            if (inputDateEmprunt.isEmpty() || inputDateRetour.isEmpty() || inputLivre.isEmpty() || inputLecteur.isEmpty()) {
	                showAlert("Erreur", "Veuillez remplir tous les champs.", AlertType.ERROR);
	                return;
	            }

	        try {
	            long codeLivre = Long.parseLong(input_livre.getText());
	            long cinLecteur = Long.parseLong(input_lecteur.getText());
	            
	           
	            if (!livreExists(codeLivre)) {
	                showAlert("Erreur", "Le livre n'existe pas dans la base de données.", AlertType.ERROR);
	                return;
	            }

	            if (!lecteurExists(cinLecteur)) {
	                showAlert("Erreur", "Le lecteur n'existe pas dans la base de données.", AlertType.ERROR);
	                return;
	            }

	            if (!isLivreDisponible(codeLivre)) {
	                showAlert("Erreur", "Le livre n'est pas disponible.", AlertType.ERROR);
	                return;
	            }

	            String maxIdQuery = "SELECT MAX(id_emprunt) AS maxId FROM emprunt";
	            String insertQuery = "INSERT INTO emprunt (id_emprunt, date_emprunt, date_retour, cin, code) VALUES (?, ?, ?, ?, ?)";

	            try (Connection con = ConnexionDb.Connect();
	                 PreparedStatement maxIdStmt = con.prepareStatement(maxIdQuery);
	                 ResultSet maxIdResultSet = maxIdStmt.executeQuery()) {

	                long nouveauId = 1;

	                if (maxIdResultSet.next()) {
	                    nouveauId = maxIdResultSet.getLong("maxId") + 1;
	                }

	                try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
	                    pstmt.setLong(1, nouveauId);
	                    pstmt.setString(2, input_date_emprunt.getText());
	                    pstmt.setString(3, input_date_retour.getText());
	                    pstmt.setLong(4, cinLecteur);
	                    pstmt.setLong(5, codeLivre);

	                    int rowsAffected = pstmt.executeUpdate();

	                    if (rowsAffected > 0) {
	                        String updateLivreQuery = "UPDATE livre SET disponible = 'Non disponible' WHERE code = ?";
	                        try (PreparedStatement updateLivreStmt = con.prepareStatement(updateLivreQuery)) {
	                            updateLivreStmt.setLong(1, codeLivre);
	                            updateLivreStmt.executeUpdate();
	                        }

	                        showAlert("Succès", "L'emprunt a été ajouté avec succès.", AlertType.INFORMATION);
	                        input_date_emprunt.clear();
	                        input_date_retour.clear();
	                        input_lecteur.clear();
	                        input_livre.clear();
	                        showEmprunt();
	                    } else {
	                        showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'emprunt.", AlertType.ERROR);
	                    }
	                }
	            } catch (NumberFormatException e) {
	                showAlert("Erreur", "Veuillez saisir un code de livre valide ou un numéro de lecteur valide.", AlertType.ERROR);
	            } catch (SQLException e) {
	                e.printStackTrace();
	                showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'emprunt.", AlertType.ERROR);
	            }
	        } catch (NumberFormatException e) {
	            showAlert("Erreur", "Veuillez saisir un code de livre et un numéro de lecteur valides.", AlertType.ERROR);
	        }
	    }


	    private boolean lecteurExists(long cinLecteur) {
	        String query = "SELECT COUNT(*) AS count FROM lecteur WHERE cin = ?";
	        try (Connection con = ConnexionDb.Connect();
	             PreparedStatement pstmt = con.prepareStatement(query)) {
	            pstmt.setLong(1, cinLecteur);
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                return resultSet.next() && resultSet.getInt("count") > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    private boolean livreExists(long codeLivre) {
	        String query = "SELECT COUNT(*) AS count FROM livre WHERE code = ?";
	        try (Connection con = ConnexionDb.Connect();
	             PreparedStatement pstmt = con.prepareStatement(query)) {
	            pstmt.setLong(1, codeLivre);
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                return resultSet.next() && resultSet.getInt("count") > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    

	    private static void showAlert(String title, String content, AlertType alertType) {
	    	 Alert alert = new Alert(alertType);
	    	    alert.setTitle(title);
	    	    alert.setHeaderText(null);
	    	    alert.setContentText(content);
	    	    alert.showAndWait();
			
		}

	  
	  	
	 

	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showEmprunt();
		
	}

}
