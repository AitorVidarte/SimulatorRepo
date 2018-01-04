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
	
	private int roleID;
	private String description;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
