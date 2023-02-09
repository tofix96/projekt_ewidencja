package com.projektzaliczeniowy.projektzaliczeniowy;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Reprezentuje książke w bazie danych.
 */
@Entity
@Table(name = "sprzety")
public class Sprzet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    @Column
    private String kategoria;
    @Column
    private String model;
    @Column
    private String opis;

    /**
     * Pusty konstruktor do klasy Ksiazki.
     */
    public Sprzet() {}


    public Sprzet(Integer id, String kategoria, String model, String opis) {

        this.id = id;
        this.kategoria = kategoria;
        this.model = model;
        this.opis = opis;

    }
    /** Gettery i settery */
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getKategoria() { return kategoria; }

    public void setKategoria(String kategoria) { this.kategoria = kategoria; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    /**
     * Wyświetla wszystkie dane sprzętu w formie tekstu
     */
    @Override
    public String toString() {

        return "Sprzet{" + ", id_sprzetu='" + id + "'" + ", kategoria='" + kategoria + "'" + ", model='" + model + "'" + ", opis='" + opis + "'" + '}';

    }

}
