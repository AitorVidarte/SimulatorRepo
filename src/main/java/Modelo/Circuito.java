package Modelo;

import java.util.ArrayList;
import java.util.List;

import DAO.RailDAO;

public class Circuito {

	List<Rail> railes;
	List<Station> estaciones;
	
	static boolean usingStations[]= {false,false,false,false,false,false};

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
	public List<Station> getEstaciones(){
		return estaciones;
	}

	
	public void setEstaciones(List<Station> stations) {
		this.estaciones = stations;
	}

	public synchronized Station reservarEstacion(int index,boolean using) {
		
		if (usingStations[index] == false) {
			usingStations[index] = using;
		}
		else  {
			try {
				System.out.println("Tren bloqueado! EStacion!ª!!! ");
				Thread.sleep(10000);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return estaciones.get(index);
	}

	
	public synchronized void despertarTrenes(int index) {
		usingStations[index] = false;
		notify();
		
	}

}
