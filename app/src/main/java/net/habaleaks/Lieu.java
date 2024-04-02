package net.habaleaks;

public class Lieu
{
    private int idLieu;
    public int getidLieu()
    {
        return this.idLieu;
    }

    private String lieu;
    public String getlieu()
    {
        return this.lieu;
    }
    public void setlieu(String value)
    {
        this.lieu = value;
    }


    public Lieu(int idLieu_,String lieu_)
    {
        this.idLieu = idLieu_;
        this.lieu = lieu_;
    }
}