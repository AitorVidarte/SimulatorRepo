package Modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import Modelo.Role;

@SuppressWarnings("serial")
@Entity
@Table(name = "User")
public class User implements Serializable {

	@SuppressWarnings("unused")
	private static final int serialVersionUID = 5;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;
	private String nombre;
	private String apellido;
	private String username;
	private String password;
	@ManyToOne
	private Role role;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "role", columnDefinition = "longblob")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
