/**
 * @file Rail.java
 * @author Alex
 * @date 18/12/2017
 * @brief Rail bean
 */

package Modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@SuppressWarnings("serial")
@Entity
@Table(name = "Rail")
public class Rail implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 2;

	/** The rail id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int railID;
	/** Rail occupied or not. */
	private boolean occupied;
	/** The previous station. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station previousStation;
	/** The next station. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station nextStation;

	public Rail() {
	}

	/**
	 * The constructor.
	 * @param railID
	 * The railID
	 * @param previousStation
	 * The previousStation
	 * @param nextStation
	 * The nextStation
	 * @param occupied
	 * The occupied
	 */
	public Rail(int railID, Station previousStation, Station nextStation, boolean occupied) {
		this.railID = railID;
		this.previousStation = previousStation;
		this.nextStation = nextStation;
		this.occupied = occupied;
	}

	/**
	 * Gets the railID.
	 * @return railID
	 */
	public int getRailID() {
		return railID;
	}

	/**
	 * Sets the railID.
	 * @param railID
	 * The railID
	 */
	public void setRailID(int railID) {
		this.railID = railID;
	}

	/**
	 * Gets if the rail is occupied.
	 * @return occupied
	 */
	public boolean isOccupied() {
		return occupied;
	}

	/**
	 * Sets if the rail is occupied.
	 * @param occupied
	 * The occupied
	 */
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	/**
	 * Gets the previousStation.
	 * @return previousStation
	 */
	public Station getPreviousStation() {
		return previousStation;
	}

	/**
	 * Sets the previousStation.
	 * @param previousStation
	 * The previousStation
	 */
	public void setPreviousStation(Station previousStation) {
		this.previousStation = previousStation;
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
}
