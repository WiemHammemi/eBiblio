package Model;


public class LivreModel {
    private Long code;
    private String titre;
    private String auteur;
    private String codeISBN;
    private String disponible;


    public String isDisponible() {
		return disponible;
	}



	public void setDisponible(String disponible) {
		this.disponible = disponible;
	}



	public LivreModel(Long code, String titre, String auteur, String codeISBN, String disponible) {
		super();
		this.code = code;
		this.titre = titre;
		this.auteur = auteur;
		this.codeISBN = codeISBN;
		this.disponible=disponible;
	}
    
    

	public Long getCode() {
		return code;
	}



	public void setCode(Long code) {
		this.code = code;
	}



	public String getTitre() {
		return titre;
	}



	public void setTitre(String titre) {
		this.titre = titre;
	}



	public String getAuteur() {
		return auteur;
	}



	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}



	public String getCodeISBN() {
		return codeISBN;
	}



	public void setCodeISBN(String codeISBN) {
		this.codeISBN = codeISBN;
	}



	}


