package Modelo;

/**
 * @file Circuito.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief Circuit
 */


import java.util.ArrayList;
import java.util.List;

public class Circuito {

	/** rails */
	List<Rail> railes;
	/** stations */
	List<Station> estaciones;

	
	static boolean usingStations[]= {false,false,false,false,false,false};

	/**
	 * The constructor of Package Controller. 
	 */
	public Circuito() {
		railes = new ArrayList<Rail>();
		estaciones = new ArrayList<Station>();
	}

	/**
	 * This method return rails
	 * @return
	 */
	public List<Rail> getRailes() {
		return railes;
	}
	/**
	 * this method update rails
	 * @param rails
	 *  update rails of station.
	 */
	public void setRailes(List<Rail> rails) {
		this.railes = rails;
	} 
	
	/**
	 * This method acts as a monitor, if the rail that wants the train is occupied the train will be blocked.
	 * @param rail
	 * @return
	 */

	public synchronized Rail cogerRail(Rail rail) {
		while (rail.isOccupied()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		rail.setOccupied(true);
		return rail;
	}
	
	/**
	 * This method frees the rail and warns blocked trains.
	 * @param rail
	 */
	public synchronized void soltarRail(Rail rail) {
		rail.setOccupied(false);
		notify();
	}
	/**
	 * this method return station list
	 * @return
	 */
	public List<Station> getEstaciones(){
		return estaciones;
	}

	/**
	 * This method update stations of circuit.
	 * @param stations
	 * update stations.
	 */
	public void setEstaciones(List<Station> stations) {
		this.estaciones = stations;
	}

	/**
	 * 
	 * @param index
	 * @param using
	 * @return
	 */
	public synchronized Station reservarEstacion(int index,boolean using) {
		
		if (usingStations[index] == false) {
			usingStations[index] = using;
		}
		else  {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return estaciones.get(index);
	}

	/**
	 * This method checks if there is any train using the packets of a station, if there is any modifying the data it blocks the train.
	 * @param index
	 */
	public synchronized void despertarTrenes(int index) {
		usingStations[index] = false;
		notify();
		
	}

}
