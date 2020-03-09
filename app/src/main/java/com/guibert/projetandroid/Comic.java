package com.guibert.projetandroid;

import java.io.Serializable;
import java.util.ArrayList;

public class Comic implements Serializable {
    private int id;
    private String name;
    private String description;
    private String img;
    private String charactersUrl;
    private int nbCharacters;
    private int nbPage;
    private String purchaseUrl;
    private String format;

    public Comic(int id, String n, String d, String u, String charact, int nbCharacters, int pages, String purchase, String f) {
        this.id = id;
        this.name = n;
        this.description = d;
        this.img = u;
        this.charactersUrl = charact;
        this.nbCharacters = nbCharacters;
        this.nbPage = pages;
        this.purchaseUrl = purchase;
        this.format = f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCharactersUrl() {
        return charactersUrl;
    }

    public void setCharactersUrl(String charactersUrl) {
        this.charactersUrl = charactersUrl;
    }

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbPage() {
        return nbPage;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getNbCharacters() {
        return nbCharacters;
    }

    public void setNbCharacters(int nbCharacters) {
        this.nbCharacters = nbCharacters;
    }
}
