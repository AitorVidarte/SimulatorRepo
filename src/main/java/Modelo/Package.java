package Modelo;
/**
 * @file Package.java
 * @author Alex
 * @date 15/12/2017
 * @brief Package bean
 */
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "Package")
public class Package implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 1;
	
	/** The packageID. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int packageID;
	/** The origin station of the package. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station origin;
	/** The destination station of the package. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Station destination;
	/** The description of the package. */
	private String description;
	/** The package's state. */
	private int packageState;
	/** The package's send date. */
	private Date sendDate;
	/** If the package is assigned to a train or not. */
	private boolean asignadoTren;
	/** The train where the package is. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Train takeTrain;

	
	public Package() {
	}

	/**
	 * The constructor.
	 * @param origin
	 * The origin
	 * @param destination
	 * The destination
	 * @param description
	 * The description
	 */
	public Package(Station origin, Station destination, String description) {
		this.origin = origin;
		this.destination = destination;
		this.description = description;
		this.sendDate = null;
		this.packageState = 0;
		this.asignadoTren = false;
	}

	/**
	 * Gets the packageID.
	 * @return packageID
	 */
	public int getPackageID() {
		return packageID;
	}

	/**
	 * Sets the packageID.
	 * @param packageID
	 * The packageID
	 */
	public void setPackageID(int packageID) {
		this.packageID = packageID;
	}

	/**
	 * Gets the origin.
	 * @return origin
	 */
	public Station getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin.
	 * @param origin
	 * The origin
	 */
	public void setOrigin(Station origin) {
		this.origin = origin;
	}

	/**
	 * Gets the destination.
	 * @return destination
	 */
	public Station getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 * @param destination
	 * The destination
	 */
	public void setDestination(Station destination) {
		this.destination = destination;
	}

	/**
	 * Gets the description.
	 * @return description
	 */
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
	 * Gets the packageState.
	 * @return packageState
	 */
	public int getPackageState() {
		return packageState;
	}

	/**
	 * Sets the packageState.
	 * @param packageState
	 * The packageState
	 */
	public void setPackageState(int packageState) {
		this.packageState = packageState;
	}

	/**
	 * Gets the sendDate.
	 * @return sendDate
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * Sets the sendDate.
	 * @param sendDate
	 * The sendDate
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * Gets if the package is assigned.
	 * @return asignadoTren
	 */
	public boolean isAsignadoTren() {
		return asignadoTren;
	}

	/**
	 * Sets if the package is assigned.
	 * @param asignadoTren
	 * The assigned train
	 */
	public void setAsignadoTren(boolean asignadoTren) {
		this.asignadoTren = asignadoTren;
	}

	/**
	 * Gets the takeTrain.
	 * @return takeTrain
	 */
	public Train getTakeTrain() {
		return takeTrain;
	}

	/**
	 * Sets the takeTrain.
	 * @param takeTrain
	 * The takeTrain
	 */
	public void setTakeTrain(Train takeTrain) {
		this.takeTrain = takeTrain;
	}
}