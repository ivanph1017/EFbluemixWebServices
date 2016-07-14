package services.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Conexion {
	private static Conexion instancia = null;
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public static Conexion getInstancia(){
		if (instancia == null){
			instancia = new Conexion();
		}
		return instancia;
	}
	
	public EntityManager getEntityManager(){
		return em;
	}
	
	public void conectarse(){
		emf = Persistence.createEntityManagerFactory("SergioSandraIvanWebServices");
		em = emf.createEntityManager();
	}
	
	public void desconectarse(){
		em.close();
		emf.close();
	}
}
