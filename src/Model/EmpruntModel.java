package Model;

import java.sql.Date;

public class EmpruntModel {
	private Long idEmprunt;
    private Date dateEmprunt;
    private Date dateRetour;
    private Long cin;
    private Long code;

    private String lecteur;
    private String livre;
    private boolean retour ; 

    public boolean isRetour() {
		return retour;
	}
	public void setRetour(boolean retour) {
		this.retour = retour;
	}
	public EmpruntModel(Long idEmprunt, Date dateEmprunt, Date dateRetour, Long cin, Long code, String lecteur, String livre) {
        this.idEmprunt = idEmprunt;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.cin = cin;
        this.code = code;
        this.lecteur = lecteur;
        this.livre = livre;
    }
    public EmpruntModel(Long idEmprunt, Date dateEmprunt, Date dateRetour, String lecteur, String livre) {
        this.idEmprunt = idEmprunt;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
       
        this.lecteur = lecteur;
        this.livre = livre;
    }
    public EmpruntModel(Long idEmprunt, Date dateEmprunt, Date dateRetour, Long cin, Long code) {
        this.idEmprunt = idEmprunt;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.cin = cin;
        this.code = code;
       
    }
    

		public EmpruntModel(Long idEmprunt2, java.util.Date dateEmprunt2, java.util.Date dateRetour2, String lecteur2,
			String livre2) {
			  this.idEmprunt = idEmprunt2;
		        this.dateEmprunt = (Date) dateEmprunt2;
		        this.dateRetour = (Date) dateRetour2;
		       
		        this.lecteur = lecteur2;
		        this.livre = livre2;
	}

		public Long getIdEmprunt() {
			return idEmprunt;
		}
		public void setIdEmprunt(Long idEmprunt) {
			this.idEmprunt = idEmprunt; 
		}
		public Date getDateEmprunt() {
			return dateEmprunt;
		}
		public void setDateEmprunt(Date dateEmprunt) {
			this.dateEmprunt = dateEmprunt;
		}
		public Date getDateRetour() {
			return dateRetour;
		}
		public void setDateRetour(Date dateRetour) {
			this.dateRetour = dateRetour;
		}
		public Long getCin() {
			return cin;
		}
		public void setCin(Long cin) {
			this.cin = cin;
		}
		public Long getCode() {
			return code;
		}
		public void setCode(Long code) {
			this.code = code;
		}

		public String getLecteur() {
			return lecteur;
		}

		public void setLecteur(String lecteur) {
			this.lecteur = lecteur;
		}

		public String getLivre() {
			return livre;
		}

		public void setLivre(String livre) {
			this.livre = livre;
		}

	    

}
