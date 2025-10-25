package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Model.LecteurModel;
import Model.LivreModel;
import application.ConnexionDb;
import javafx.application.Application;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LecteurController implements Initializable {
	 @FXML
	    private Button page_accueil;

	    @FXML
	    private Button page_livres;

	    @FXML
	    private Button page_lecteurs;

	    @FXML
	    private Button page_emprunts;
	    @FXML
	    private TableView<LecteurModel> tab_lecteur;

	    @FXML
	    private TableColumn<LecteurModel, Long> colonne_cin;

	    @FXML
	    private TableColumn<LecteurModel, String> colonne_nom;

	    @FXML
	    private TableColumn<LecteurModel, String> colonne_prenom;

	    @FXML
	    private TableColumn<LecteurModel, Integer> colonne_age;

	    @FXML
	    private TableColumn<LecteurModel, String> colonne_action;

	    @FXML
	    private TextField input_cin;
	    @FXML
	    private TextField input_nom;


	    @FXML
	    private TextField input_prenom;

	    @FXML
	    private TextField input_age;

	    @FXML
	    private Button ajouter_lecteur;
	    @FXML
	    private TextField input_recherche;


	    
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
		public ObservableList<LecteurModel> data = FXCollections.observableArrayList();
	
		public void showLecteur() {
			data.clear();

			tab_lecteur.getItems().clear();
	    	String requete = "SELECT * FROM lecteur";
	    	try (Connection con = ConnexionDb.Connect();
	              Statement stmt = con.createStatement();
	              ResultSet resultats = stmt.executeQuery(requete)) {
	             while (resultats.next()) {
	             	
	                data.add(new LecteurModel(resultats.getLong("cin"), resultats.getString("nom"),  resultats.getString("prenom"),  resultats.getInt("age")));
	                 
	             }
	         } catch (SQLException e) {
	             e.printStackTrace();
	             System.out.println("Erreur SQL lors de la récupération des lecteurs.");
	         }
	    	colonne_cin.setCellValueFactory(new PropertyValueFactory<LecteurModel,Long>("cin"));
	    	colonne_nom.setCellValueFactory(new PropertyValueFactory<LecteurModel,String>("nom"));
	    	colonne_prenom.setCellValueFactory(new PropertyValueFactory<LecteurModel,String>("prenom"));
	    	colonne_age.setCellValueFactory(new PropertyValueFactory<LecteurModel,Integer>("age"));
	    	tab_lecteur.setItems(data);
	    	
	    	colonne_action.setCellFactory(col -> new TableCell<LecteurModel, String>() {
	            final Button deleteButton = new Button("Supprimer");
	            final Button editButton = new Button("Modifier");


	            {
	                deleteButton.setOnAction(event -> {
	                    LecteurModel lecteur = (LecteurModel) getTableView().getItems().get(getIndex());
	                    supprimerLecteur(lecteur);

	                });
	                deleteButton.setStyle("-fx-background-color: #FF0800 !important; -fx-text-fill: white !important;");

	                editButton.setOnAction(event -> {
	                    LecteurModel lecteur = (LecteurModel) getTableView().getItems().get(getIndex());
	                    modifierLecteur(lecteur);
	                });
	                editButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #3B82F6, #2563EB) !important; -fx-text-fill: white !important;");
	            }

	            @Override
	            protected void updateItem(String item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty) {
	                    setGraphic(null);
	                    setText(null);
	                } else {
	                    HBox buttons = new HBox(deleteButton, editButton);
	                    buttons.setSpacing(5);
	                    setGraphic(buttons);
	                    setText(null);
	                }
	            }
	        });

	    	
	    }
		@FXML
		private void modifierLecteur(LecteurModel lecteur) {
		    try {
		        FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("/Scenes/modifier_lecteur.fxml"));
		        
		        Parent root = loader.load();
		        
		        ModifierLecteurController modifierLecteurController = loader.getController();
		        
		        modifierLecteurController.setLecteurAEditer(lecteur);

		        Stage stage = new Stage();
		        stage.setScene(new Scene(root));
		        stage.setTitle("Modifier Lecteur");
		        stage.show();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}

		
		private void supprimerLecteur(LecteurModel lecteur) {
		    String query = "DELETE FROM lecteur WHERE cin = ?";
		    
		    try (Connection connection = ConnexionDb.Connect();
		         PreparedStatement pstmt = connection.prepareStatement(query)) {

		        pstmt.setLong(1, lecteur.getCin());
		        pstmt.executeUpdate();
		        
		        showAlert("Succès", "Lecteur supprimé avec succès.", AlertType.INFORMATION);
		        showLecteur();

		    } catch (SQLException e) {
		        e.printStackTrace();
		        showAlert("Erreur", "Erreur SQL lors de la suppression du lecteur.", AlertType.ERROR);
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

		 @FXML
		 void ajouterLecteur(MouseEvent event) {
			    String cin = input_cin.getText();
			    String nom = input_nom.getText();
			    String prenom = input_prenom.getText();
			    String ageText = input_age.getText();

			    if (cin.isEmpty() || nom.isEmpty() || prenom.isEmpty() || ageText.isEmpty()) {
			        showAlert("Erreur", "Veuillez remplir tous les champs.", AlertType.ERROR);
			        return;
			    }

			    if (cin.length() != 8) {
			        showAlert("Erreur", "Le CIN doit être un nombre de 8 chiffres.", AlertType.ERROR);
			        return;
			    }

			    Long cinLong;
			    try {
			        cinLong = Long.parseLong(cin);
			    } catch (NumberFormatException e) {
			        showAlert("Erreur de saisie", "Le CIN doit être un nombre entier.", AlertType.ERROR);
			        return;
			    }

			    if (lecteurExists(cinLong)) {
			        showAlert("Erreur", "Ce lecteur existe déjà dans la base de données.", AlertType.ERROR);
			        return;
			    }

			    int age;
			    try {
			        age = Integer.parseInt(ageText);
			    } catch (NumberFormatException e) {
			        showAlert("Erreur de saisie", "L'âge doit être un nombre entier.", AlertType.ERROR);
			        return;
			    }

			    String insertQuery = "INSERT INTO lecteur (cin, nom, prenom, age) VALUES (?, ?, ?, ?)";
			    try (Connection con = ConnexionDb.Connect();
			         PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
			        pstmt.setLong(1, cinLong);
			        pstmt.setString(2, nom);
			        pstmt.setString(3, prenom);
			        pstmt.setInt(4, age);
			        pstmt.executeUpdate();

			        showLecteur();

			        showAlert("Succès", "Lecteur ajouté avec succès.", AlertType.CONFIRMATION);
			        input_cin.clear();
			        input_nom.clear();
			        input_prenom.clear();
			        input_age.clear();
			    } catch (SQLException e) {
			        e.printStackTrace();
			        showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du lecteur.", AlertType.ERROR);
			    }
			}

		    
		 private static void showAlert(String title, String content, AlertType alertType) {
	    	 Alert alert = new Alert(alertType);
	    	    alert.setTitle(title);
	    	    alert.setHeaderText(null);
	    	    alert.setContentText(content);
	    	    alert.showAndWait();
			
		}

	
		 @FXML
		    private void rechercherLecteur(KeyEvent event) {
		        String searchText = input_recherche.getText().toLowerCase().trim();
		        if (searchText.isEmpty()) {

		            showLecteur();
		        } else {
		            List<LecteurModel> filteredList = data.stream()
		                    .filter(lecteur ->
		                            lecteur.getNom().toLowerCase().contains(searchText) ||
		                            lecteur.getPrenom().toLowerCase().contains(searchText))
		                    .collect(Collectors.toList());
		            tab_lecteur.setItems(FXCollections.observableArrayList(filteredList));
		        }
				

		    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showLecteur();
		
	}

}