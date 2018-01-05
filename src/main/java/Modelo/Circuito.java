package Modelo;

import java.util.ArrayList;

public class Circuito {

    ArrayList<Rail> railes;
    ArrayList<Station> estaciones;

    public Circuito() {

	railes = new ArrayList<Rail>();
	estaciones = new ArrayList<Station>();

    }

    public ArrayList<Rail> getRailes() {
	return railes;
    }

    public void setRailes(ArrayList<Rail> ralies) {
	this.railes = ralies;
    }

    public synchronized Rail cogerRail(Rail rail) {
	while (rail.isOccupied()) {

	    try {
		System.out.println("Tren bloqueado!");
		wait(); // Se sale cuando estaLlena cambia a false

	    } catch (InterruptedException e) {
		;
	    }
	}
	rail.setOccupied(true);
	
	return rail;
    }

    public synchronized void soltarRail(Rail rail) {
	rail.setOccupied(false);
	notify();
    }

    public ArrayList<Station> getEstaciones() {
	return estaciones;
    }

    public void setEstaciones(ArrayList<Station> estaciones) {
	this.estaciones = estaciones;
    }

}
