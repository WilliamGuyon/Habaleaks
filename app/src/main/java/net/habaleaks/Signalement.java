package net.habaleaks;

public class Signalement
{
    private int idSignalement;
    public int getidSignalement() {return this.idSignalement;}

    private int idDefaut;
    public int getidDefaut() {return this.idDefaut;}
    public void setidDefaut(int value) {this.idDefaut = value;}

    private int idLieu;
    public int getidLieu() {return this.idLieu;}
    public void setidLieu(int value) {this.idLieu = value;}

    private int idPersonnel;
    public int getidPersonnel() {return this.idPersonnel;}
    public void setidPersonnel(int value) {this.idPersonnel = value;}

    private java.sql.Date dateCreation;
    public java.sql.Date getdateCreation() {return this.dateCreation;}
    public void setdateCreation(java.sql.Date value) {this.dateCreation = value;}

    private int gravite;
    public int getgravite() {return this.gravite;}
    public void setgravite(int value) {this.gravite = value;}

    private String commentaire;
    public String getcommentaire() {return this.commentaire;}
    public void setcommentaire(String value) {this.commentaire = value;}

    private boolean resolu;
    public boolean getresolu() {return this.resolu;}
    public void setresolu(boolean value) {this.resolu = value;}

    private String latitude;
    public String getlatitude() {return this.latitude;}
    public void setlatitude(String value) {this.latitude = value;}

    private String longitude;
    public String getlongitude() {return this.longitude;}
    public void setlongitude(String value) {this.longitude = value;}

    private String photo;
    public String getphoto() {return this.photo;}
    public void setphoto(String value) {this.photo = value;}


    public Signalement(int idSignalement_, int idDefaut_, int idLieu_, int idPersonnel_, java.sql.Date dateCreation_, int gravite_, String commentaire_, boolean resolu_, String latitude_, String longitude_, String photo_)
    {
        this.idSignalement = idSignalement_;
        this.idDefaut = idDefaut_;
        this.idLieu = idLieu_;
        this.idPersonnel = idPersonnel_;
        this.dateCreation = dateCreation_;
        this.gravite = gravite_;
        this.commentaire = commentaire_;
        this.resolu = resolu_;
        this.latitude = latitude_;
        this.longitude = longitude_;
        this.photo = photo_;
    }
}
