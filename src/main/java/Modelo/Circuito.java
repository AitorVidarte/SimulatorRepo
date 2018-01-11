package Modelo;

import java.util.ArrayList;
import java.util.List;

import DAO.RailDAO;

public class Circuito {

	List<Rail> railes;
	List<Station> estaciones;

	public Circuito() {

		railes = new ArrayList<Rail>();
		estaciones = new ArrayList<Station>();

	}

	public List<Rail> getRailes() {
		return railes;
	}

	public void setRailes(List<Rail> rails) {
		this.railes = rails;
	}

	public synchronized Rail cogerRail(Rail rail) {
		RailDAO railDao = new RailDAO();
		System.out.println("Entra Coger Rail");
		while (rail.isOccupied()) {
			
			try {
				System.out.println("bloqueo");
				wait();
			} catch (InterruptedException e) {
			}
		}
		rail.setOccupied(true);
		railDao.edit(rail);
		return rail;
	}

	public synchronized void soltarRail(Rail rail) {

		RailDAO railDao = new RailDAO();
		rail.setOccupied(false);
		railDao.edit(rail);
		notify();

	}

	public List<Station> getEstaciones() {
		return estaciones;
	}

	public void setEstaciones(List<Station> stations) {
		this.estaciones = stations;
	}

}
