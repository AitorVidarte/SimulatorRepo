package Modelo;

/**
 * @file Station.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief Station
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import Controller.ResourcesPool;
import Modelo.Train;


@SuppressWarnings("serial")
@Entity
@Table(name = "Station")
public class Station implements Serializable {

	/**	The station id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int stationID;
	/** The description. */
	private String description;
	/** The next station. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station nextStation;
	/** The previous station. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station previousStation;
	/** The exit switch. */
	private int exitSwitch;
	/** The entry switch. */
	private int entrySwitch;
	/** The parks. */
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<Train> parks = new ArrayList<>();
	/** Latitude coordinates. */
	private double coordinatesLat;
	/** Longitude coordinates. */
	private double coordinatesLng;
	/** The send package list. */
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<Package> sendPackageList = new ArrayList<>();
	/** The delivered package list. */
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<Package> deliveredPackageList = new ArrayList<>();

	public Station() {}
	
	public Station(String description) {
		this.description = description;
	}

	/**
	 * The constructor.
	 * @param stationID
	 * The stationID
	 * @param description
	 * The description
	 * @param exitSwitch
	 * The exitSwitch
	 * @param entrySwitch
	 * The entrySwitch
	 * @param coordinatesLat
	 * The coordinatesLat
	 * @param coordinatesLng
	 * The coordinatesLng
	 * @param nextStation
	 * The nextStation
	 * @param previousStation
	 * The previousStation
	 * @param nextExitSwitch
	 * switch1
	 * @param previousExitSwitch
	 * switch2
	 */
	public Station(int stationID, double coordinatesLat, double coordinatesLng, String description, int nextExitSwitch,
			int previousExitSwitch, int exitSwitch, int entrySwitch, Station nextStation,
			Station previousStation) {
		this.stationID = stationID;
		this.coordinatesLat = coordinatesLat;
		this.coordinatesLng = coordinatesLng;
		this.description = description;
		this.exitSwitch = exitSwitch;
		this.entrySwitch = entrySwitch;
		this.nextStation = nextStation;
		this.previousStation = previousStation;
	}
	/**
	 * Gets the stationID.
	 * @return stationID
	 */
	public int getStationID() {
		return stationID;
	}

	/**
	 * Sets the stationID.
	 * @param stationID
	 * The stationID
	 */
	public void setStationID(int stationID) {
		this.stationID = stationID;
	}


	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description
	 * The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the nextStation.
	 * @return nextStation
	 */
	public Station getNextStation() {
		return nextStation;
	}

	/**
	 * Sets the nextStation.
	 * @param nextStation
	 * The nextStation
	 */
	public void setNextStation(Station nextStation) {
		this.nextStation = nextStation;
	}

	public Station getPreviousStation() {
		return previousStation;
	}

	public void setPreviousStation(Station previousStation) {
		this.previousStation = previousStation;
	}

	public int getExitSwitch() {
		return exitSwitch;
	}

	public void setExitSwitch(int exitSwitch) {
		this.exitSwitch = exitSwitch;
	}

	public int getEntrySwitch() {
		return entrySwitch;
	}

	public void setEntrySwitch(int entrySwitch) {
		this.entrySwitch = entrySwitch;
	}

	public Collection<Train> getParks() {
		return parks;
	}

	public void setParks(Collection<Train> parks) {
		this.parks = parks;
	}

	public double getCoordinatesLat() {
		return coordinatesLat;
	}

	public void setCoordinatesLat(double coordinatesLat) {
		this.coordinatesLat = coordinatesLat;
	}

	public double getCoordinatesLng() {
		return coordinatesLng;
	}

	public void setCoordinatesLng(double coordinatesLng) {
		this.coordinatesLng = coordinatesLng;
	}

	public Collection<Package> getSendPackageList() {
		return sendPackageList;
	}

	public void setSendPackageList(Collection<Package> sendPackageList) {
		this.sendPackageList = sendPackageList;
	}

	public Collection<Package> getDeliveredPackageList() {
		return deliveredPackageList;
	}

	public void setDeliveredPackageList(Collection<Package> deliveredPackageList) {
		this.deliveredPackageList = deliveredPackageList;
	}
	
	/**
	 * This method is synchronized to check if there are free parking spaces, but to block the train's thread.
	 * @param resourcePool
	 * this parameter is to notify resources pool that wakes up a train
	 * @return boolean 
	 */
	public synchronized boolean obtenerPaking(ResourcesPool resourcePool) {
		boolean haySitio = true;
		if (parks.size() == 4) {
			try {
				System.out.println("######### tren bloqueado!!!");
				resourcePool.moverTrenesParados(this);
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			haySitio=false;
		}
		
		return haySitio;
	}
	
	
	public synchronized void despertarTren() {
		notify();
	}

	public void aparcarTren(Train train) {
		parks.add(train);
	}

	public void quitarTren(Train train) {
		this.parks.remove(train);
	}

	public void addNewPackageToSend(Package paquete) {
		sendPackageList.add(paquete);
		
	}
	public void addDeliveredPackageList(Package paquete) {
		deliveredPackageList.add(paquete);
		
	}
}