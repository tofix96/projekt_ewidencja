package com.projektzaliczeniowy.projektzaliczeniowy;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Reprezentuje klienta w bazie danych.
 */
@Entity
@Table(name = "klienci")
public class Klient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    @Column
    private String imie;
    @Column
    private String nazwisko;
    @Column(name = "numer_tel", length = 12)
    private int numerTel;

    /**
     * Pusty konstruktor do klasy Klienci.
     */
    public Klient() {}


    public Klient(Integer id, String imie, String nazwisko, int numerTel) {

        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.numerTel = numerTel;

    }
    /** Gettery i settery */
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getImie() { return imie; }

    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }

    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public int getNumerTel() { return numerTel; }

    public void setNumerTel(int numerTel) { this.numerTel = numerTel; }

    /**
     * Wyświetla wszystkie dane klienta w formie tekstu
     */
    @Override
    public String toString() {

        return "Klient{" + "id_klienta=" + id + ", imie='" + imie + "'" + ", nazwisko='" + nazwisko + "'" + ", numer_tel=" + numerTel + '}';

    }

}
