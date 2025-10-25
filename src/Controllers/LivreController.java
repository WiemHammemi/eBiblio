package Controllers;
import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Model.LivreModel;
import application.ConnexionDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class LivreController implements Initializable {
	
	 @FXML
	    private Button page_accueil;

	    @FXML
	    private Button page_livres;

	    @FXML
	    private Button page_lecteurs;

	    @FXML
	    private Button page_emprunts;
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

	

	    @FXML
	    private TableView<LivreModel> tab_livre;

	    @FXML
	    private TableColumn<LivreModel, Long> colonne_code;

	    @FXML
	    private TableColumn<LivreModel, String> colonne_titre;

	    @FXML
	    private TableColumn<LivreModel, String> colonne_auteur;

	    @FXML
	    private TableColumn<LivreModel, String> colone_isbn;
	    @FXML
	    private TableColumn<LivreModel, String> colonne_dispo;

	    @FXML
	    private TableColumn<LivreModel, String> colonne_action;

	    @FXML
	    private TextField input_recherche;

	    @FXML
	    private ImageView rechercher;

	    @FXML
	    private TextField input_titre;

	    @FXML
	    private TextField input_auteur;

	    @FXML
	    private TextField input_isbn;

	    @FXML
	    private Button ajouter_livre;
	    @FXML
	    private Button export;
	    
	    
	    @SuppressWarnings("exports")
		public ObservableList<LivreModel> data = FXCollections.observableArrayList();

	 
	    private static void showAlert(String title, String content, AlertType alertType) {
	    	 Alert alert = new Alert(alertType);
	    	    alert.setTitle(title);
	    	    alert.setHeaderText(null);
	    	    alert.setContentText(content);
	    	    alert.showAndWait();
			
		}
	    
	  


		public void showLivre() {
			data.clear();
			tab_livre.getItems().clear();
	    	String requete = "SELECT * FROM livre";
	    	try (Connection con = ConnexionDb.Connect();
	              Statement stmt = con.createStatement();
	              ResultSet resultats = stmt.executeQuery(requete)) {
	             while (resultats.next()) {
	             	
	                data.add(new LivreModel(resultats.getLong("code"), resultats.getString("titre"),  resultats.getString("auteur"),  resultats.getString("codeISBN"), resultats.getString("disponible")));
	                 
	             }
	         } catch (SQLException e) {
	             e.printStackTrace();
	             System.out.println("Erreur SQL lors de la récupération des livres.");
	         }
	    	colonne_code.setCellValueFactory(new PropertyValueFactory<LivreModel,Long>("code"));
	    	colonne_titre.setCellValueFactory(new PropertyValueFactory<LivreModel,String>("titre"));
	    	colonne_auteur.setCellValueFactory(new PropertyValueFactory<LivreModel,String>("auteur"));
	    	colone_isbn.setCellValueFactory(new PropertyValueFactory<LivreModel,String>("codeISBN"));
	    	colonne_dispo.setCellValueFactory(new PropertyValueFactory<LivreModel,String>("disponible"));
	    	tab_livre.setItems(data);
	    	 colonne_action.setCellFactory(col -> new TableCell<LivreModel, String>() {
		            final Button deleteButton = new Button("Supprimer");
		            final Button editButton = new Button("Modifier");
		            {
		                deleteButton.setOnAction(event -> {
		                    LivreModel livre = (LivreModel) getTableView().getItems().get(getIndex());
		                    supprimerLivre(livre);
		                });
		                deleteButton.setStyle("-fx-background-color: #FF0800 !important; -fx-text-fill: white !important;");
		                editButton.setOnAction(event -> {
		                    LivreModel livre = (LivreModel) getTableView().getItems().get(getIndex());
		                    modifierLivre(livre);
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
		private void modifierLivre(LivreModel livre) {
		    try {
		        FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("/Scenes/modifier_livre.fxml"));
		        
		        Parent root = loader.load();
		        
		        ModifierLivreController modifierLivreController = loader.getController();
		        
		        modifierLivreController.setLivreAEditer(livre);

		        Stage stage = new Stage();
		        stage.setScene(new Scene(root));
		        stage.setTitle("Modifier Livre");
		        stage.show();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}


		private void supprimerLivre(LivreModel livre) {
		    String query = "DELETE FROM livre WHERE code = ?";
		    
		    try (Connection connection = ConnexionDb.Connect();
		         PreparedStatement pstmt = connection.prepareStatement(query)) {

		        pstmt.setLong(1, livre.getCode());
		        pstmt.executeUpdate();
		        
		        showAlert("Succès", "Livre supprimé avec succès.", AlertType.INFORMATION);
		        showLivre();

		    } catch (SQLException e) {
		        e.printStackTrace();
		        showAlert("Erreur", "Erreur SQL lors de la suppression du livre.", AlertType.ERROR);
		    }
		}
		
		 private boolean livreExists(String codeLivre) {
		        String query = "SELECT COUNT(*) AS count FROM livre WHERE codeISBN = ?";
		        try (Connection con = ConnexionDb.Connect();
		             PreparedStatement pstmt = con.prepareStatement(query)) {
		            pstmt.setString(1, codeLivre);
		            try (ResultSet resultSet = pstmt.executeQuery()) {
		                return resultSet.next() && resultSet.getInt("count") > 0;
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		            return false;
		        }
		    }
	  

     

		@FXML
	
		void ajouterLivre(MouseEvent event) {
		   
		    String auteur = input_auteur.getText().trim();

		    String isbn = input_isbn.getText().trim();

		    String titre = input_titre.getText().trim();

		    if (titre.isEmpty() || auteur.isEmpty() || isbn.isEmpty()) {
		        showAlert("Erreur", "Veuillez remplir tous les champs.", AlertType.ERROR);
		        return;
		    }
		    if (livreExists(isbn)) {
                showAlert("Erreur", "Ce livre existe déjà dans la base de données.", AlertType.ERROR);
                return;
            }

		    String requeteMaxCode = "SELECT MAX(code) AS maxCode FROM livre";
		    String requeteInsert = "INSERT INTO livre (code, titre, auteur, codeISBN, disponible) VALUES (?, ?, ?, ?,?)";

		    try (Connection con = ConnexionDb.Connect();
		         Statement stmt = con.createStatement();
		         ResultSet resultats = stmt.executeQuery(requeteMaxCode);
		         PreparedStatement pstmt = con.prepareStatement(requeteInsert)) {

		        long nouveauCode = 1; 

		        if (resultats.next()) {
		            nouveauCode = resultats.getLong("maxCode") + 1;
		        }

		        pstmt.setLong(1, nouveauCode);
		        pstmt.setString(2, titre);
		        pstmt.setString(3, auteur);
		        pstmt.setString(4, isbn);
		        pstmt.setString(5, "Disponible");


		        pstmt.executeUpdate();

		        showLivre();

		        input_titre.clear();
		        input_auteur.clear();
		        input_isbn.clear();

		        showAlert("Succès", "Livre ajouté avec succès.", AlertType.INFORMATION);

		    } catch (SQLException e) {
		        e.printStackTrace();
		        showAlert("Erreur", "Erreur SQL lors de l'ajout du livre.", AlertType.ERROR);
		    }
		}
		
		

		
		@FXML
	    private void rechercherLivre(KeyEvent event) {
	        String searchText = input_recherche.getText().toLowerCase().trim();
	        
	        if (searchText.isEmpty()) {
	            showLivre();
	        }  else {
	            List<LivreModel> filteredList = data.stream()
	                    .filter(livre ->
	                            livre.getTitre().toLowerCase().contains(searchText) ||
	                            livre.getAuteur().toLowerCase().contains(searchText))
	                    .collect(Collectors.toList());
	            tab_livre.setItems(FXCollections.observableArrayList(filteredList));

	        }
	     }
		
//		


        	   



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showLivre();
		
		
		
	}

}