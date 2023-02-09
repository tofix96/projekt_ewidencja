package com.projektzaliczeniowy.projektzaliczeniowy;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Optional;

public class KlientTest {

    @Test
    public void testKonstruktorBezParametrow(){
        //given
        Klient klient;

        //when
        klient = new Klient();

        //then
        assertNotNull(klient);
    }

    @Test
    public void testKonstruktorZParametrami(){
        //given
        Klient klient;

        //when
        klient = new Klient(1,"Jan", "Kowalski", 123456789);

        //then
        assertNotNull(klient);
        assertEquals("Jan", klient.getImie());
        assertEquals("Kowalski", klient.getNazwisko());
        assertEquals(123456789, klient.getNumerTel());
    }

    @Test
    public void testSetId(){
        //given
        Klient klient;
        klient = new Klient();

        //when
        klient.setId(1);

        //then

    }

    @Test
    public void testSetImie(){
        //given
        Klient klient;
        klient = new Klient();

        //when
        klient.setImie("Jan");

    }

    @Test
    public void testSetNazwisko(){
        //given
        Klient klient;
        klient = new Klient();

        //when
        klient.setNazwisko("Kowalski");

        //then
        assertEquals("Kowalski", klient.getNazwisko());
    }

    @Test
    public void testSetNumerTel(){
        //given
        Klient klient;
        klient = new Klient();

        //when
        klient.setNumerTel(123456789);

        //then
        assertEquals(123456789, klient.getNumerTel());
    }
}