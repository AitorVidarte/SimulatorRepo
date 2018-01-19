/**
 * @file Role.java
 * @author Alex
 * @date 18/12/2017
 * @brief Role bean
 */

package Modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "Role")
public class Role implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 6;

	/** The role id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleID;
	/** The description. */
	private String description;

	/**
	 * Gets the roleID.
	 * @return roleID
	 */
	public int getRoleID() {
		return roleID;
	}

	/**
	 * Sets the roleID
	 * @param roleID
	 * The roleID
	 */
	public void setRoleID(int roleID) {
		this.roleID = roleID;
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
}
