package Model;

public class LecteurModel {
	private Long cin;
    private String nom;
    private String prenom;
    private Integer age;
	public LecteurModel(Long cin, String nom, String prenom, Integer age) {
		super();
		this.cin = cin;
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
	}
	public Long getCin() {
		return cin;
	}
	public void setCin(Long cin) {
		this.cin = cin;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
    

}
