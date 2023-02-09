package com.projektzaliczeniowy.projektzaliczeniowy;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Reprezentuje wypożyczenie w bazie danych.
 */
@Entity
@Table(name = "wydania")
public class Wydanie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    @Column(name = "id_sprzetu")
    private Integer idSprzetu;
    @Column(name = "id_klienta")
    private Integer idKlienta;
    @Column(name = "data_wydania")
    private LocalDate dataWydania;
    @Column(name = "data_zwrotu")
    private LocalDate dataZwrotu;

    /**
     * Pusty konstruktor do klasy Wypozczyenia.
     */
    public Wydanie() {}

    /**
     * Konstruktor do klasy Wypozczyenia nadającym wszytskim polom wartosci.
     *
     * @param id   Automatycznie generowanie Id wypozyczenia.
     * @param idKlienta   Id klienta wypozyczającego.
     * @param idSprzetu   id sprzętu wypożyczanego.
     * @param dataWydania Data wypożyczenia w formacie LocalDate.
     * @param dataZwrotu  Data zwrotu w formacie LocalDate.
     */
    public Wydanie(Integer id, Integer idSprzetu, Integer idKlienta, LocalDate dataWydania, LocalDate dataZwrotu) {

        this.id = id;
        this.idSprzetu = idSprzetu;
        this.idKlienta = idKlienta;
        this.dataWydania = dataWydania;
        this.dataZwrotu = dataZwrotu;

    }
    /** Gettery i settery */

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getIdSprzetu() { return idSprzetu; }

    public void setIdSprzetu(Integer idSprzetu) { this.idSprzetu = idSprzetu; }

    public Integer getIdKlienta() { return idKlienta; }

    public void setIdKlienta(Integer idKlienta) { this.idKlienta = idKlienta; }

    public LocalDate getDataWydania() { return dataWydania; }

    public void setDataWydania(LocalDate dataWydania) { this.dataWydania = dataWydania; }

    public LocalDate getDataZwrotu() { return dataZwrotu; }

    public void setDataZwrotu(LocalDate dataZwrotu) { this.dataZwrotu = dataZwrotu; }

    /**
     * Wyświetla wszystkie dane wypożyczeń w formie tekstu
     */
    @Override
    public String toString() {

        return "Wydanie{" + "id_wydania=" + id + ", id_ksiazki=" + idSprzetu + ", id_klienta=" + idKlienta + ", data_wypozyczenia=" + dataWydania +
               ", data_zwrotu=" + dataZwrotu + '}';

    }

}
