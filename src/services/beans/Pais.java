package services.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="paises")
public class Pais {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private int id;
	
	@Column
	private String nombre;
	
	@Column
	private int poblacion;
	
	@Column
	private float pbi;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(int poblacion) {
		this.poblacion = poblacion;
	}

	public float getPbi() {
		return pbi;
	}

	public void setPbi(float pbi) {
		this.pbi = pbi;
	}

	public Pais(String nombre, int poblacion, float pbi) {
		super();
		this.nombre = nombre;
		this.poblacion = poblacion;
		this.pbi = pbi;
	}

	public Pais() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
