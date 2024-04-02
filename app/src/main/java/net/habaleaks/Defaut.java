package net.habaleaks;

public class Defaut
{
    private int idDefaut;
    public int getidDefaut() { return this.idDefaut;}

    private String description;
    public String getdescription() { return this.description; }
    public void setdescription(String value) { this.description = value; }

    private String nom;
    public String getnom() {return this.nom;}
    public void setnom(String value) {this.nom = value;}


    public Defaut(int idDefaut, String description, String nom)
    {
        this.idDefaut = idDefaut;
        this.description = description;
        this.nom = nom;
    }
}