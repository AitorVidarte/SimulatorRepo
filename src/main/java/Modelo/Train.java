package Modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import Modelo.Rail;
import Modelo.Station;
import Modelo.Package;

@SuppressWarnings("serial")
@Entity
@Table(name = "Train")
public class Train implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 4;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int trainID;
	@ManyToOne
	private Station station;
	@ManyToOne
	private Rail rail;
	private int direction;
	@OneToMany
	private Set<Package> packageList;
	private boolean onGoing;

	public Train() {
	}

	public Train(int trainID, Station station, int direction) {
		this.trainID = trainID;
		this.station = station;
		this.direction = direction;
		this.packageList = new HashSet<Package>();
	}

	public int getTrainID() {
		return trainID;
	}

	public void setTrainID(int trainID) {
		this.trainID = trainID;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Rail getRail() {
		return rail;
	}

	public void setRail(Rail rail) {
		this.rail = rail;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Set<Package> getPackageList() {
		return packageList;
	}

	public void setPackageList(Set<Package> packageList) {
		this.packageList = packageList;
	}

	public boolean isOnGoing() {
		return onGoing;
	}

	public void setOnGoing(boolean onGoing) {
		this.onGoing = onGoing;
	}
}
