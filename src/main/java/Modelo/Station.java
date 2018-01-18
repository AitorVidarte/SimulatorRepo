package Modelo;

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
import Modelo.Train;

@SuppressWarnings("serial")
@Entity
@Table(name = "Station")
public class Station implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int stationID;
	private String description;
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station nextStation;
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station previousStation;
	private int exitSwitch;
	private int entrySwitch;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<Train> parks = new ArrayList<Train>();
	private double coordinatesLat;
	private double coordinatesLng;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<Package> sendPackageList = new ArrayList<Package>();
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<Package> deliveredPackageList = new ArrayList<Package>();

	public Station() {}
	
	public Station(String description) {
		this.description = description;
	}

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

	public int getStationID() {
		return stationID;
	}

	public void setStationID(int stationID) {
		this.stationID = stationID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Station getNextStation() {
		return nextStation;
	}

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
	
	//CAMBIAR ESTA MIERDA!!
	public synchronized int obtenerPaking() {

		int pos = 0;

		for (Train tren : parks) {
			tren.getDirection();
			pos++;
		}
		if (pos == 4) {
			try {
				wait();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pos;
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