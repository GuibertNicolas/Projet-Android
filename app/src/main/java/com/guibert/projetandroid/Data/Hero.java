package com.guibert.projetandroid.Data;

import java.io.Serializable;

public class Hero implements Serializable {

    private int id;
    private String name;
    private String img;
    private int nbComics;
    private String description;
    private String comicsUrl;

    public Hero(int id, String name, String img, int nbComcis, String description, String comicsUrl){
        this.id = id;
        this.name = name;
        this.img = img;
        this.nbComics = nbComcis;
        this.description = description;
        this.comicsUrl = comicsUrl;
    }


    public int getNbComics() {
        return nbComics;
    }

    public String getImg() {
        return img;
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

    public String getDescription() {
        return description;
    }

    public String getComicsUrl() {
        return comicsUrl;
    }

}
