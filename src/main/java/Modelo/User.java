/**
 * @file User.java
 * @author Alex
 * @date 18/12/2017
 * @brief User bean
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
@Table(name = "User")
public class User implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 5;
	
	/** The user id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;
	/** The name. */
	private String nombre;
	/** The last name. */
	private String apellido;
	/** The username. */
	private String username;
	/** The password. */
	private String password;
	/** The role. */
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Role role;

	/**
	 * Gets the userID.
	 * @return userID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Sets the userID.
	 * @param userID
	 * The userID
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * Gets the name.
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the name.
	 * @param nombre
	 * The name
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the last name.
	 * @return apellido
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * Sets the last name.
	 * @param apellido
	 * The last name
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * Gets the username.
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * @param username
	 * The username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password
	 * The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the role
	 * @return role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 * @param role
	 * The role
	 */
	public void setRole(Role role) {
		this.role = role;
	}
}
