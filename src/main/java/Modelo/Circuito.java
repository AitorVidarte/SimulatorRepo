package Modelo;

import java.util.ArrayList;

import DAO.RailDAO;

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
		RailDAO railDao = new RailDAO();

		while (rail.isOccupied()) {

			try {
				System.out.println("Tren bloqueado!");
				wait();

			} catch (InterruptedException e) {
			}
		}
		rail.setOccupied(true);
		railDao.edit(rail, rail.getRailID() - 1);
		System.out.println("cogiendo rail");
		return rail;
	}

	public synchronized void soltarRail(Rail rail) {

		RailDAO railDao = new RailDAO();
		rail.setOccupied(false);
		railDao.edit(rail, rail.getRailID() - 1);
		System.out.println("soltando rail");
		notify();

	}

	public ArrayList<Station> getEstaciones() {
		return estaciones;
	}

	public void setEstaciones(ArrayList<Station> estaciones) {
		this.estaciones = estaciones;
	}

}
