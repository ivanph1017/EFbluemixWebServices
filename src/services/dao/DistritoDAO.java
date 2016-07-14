package services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;

import services.beans.Distrito;
import services.beans.Pais;
import services.beans.Provincia;

public class DistritoDAO {
	private EntityManager em;	
	
	public DistritoDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public void registrar(Distrito distrito) throws ServletException{
		
		em.getTransaction().begin();
		em.persist(distrito);
		em.getTransaction().commit();
	}
	
	public void editar(Distrito distrito) throws ServletException{
		em.getTransaction().begin();
		em.merge(distrito);
		em.getTransaction().commit();
	}
	
	public void eliminar(int id) throws ServletException{
		em.getTransaction().begin();
		Distrito distrito = obtenerDistrito(id);
		em.remove(distrito);
		em.getTransaction().commit();
	}
	
	public Distrito obtenerDistrito(int id) throws ServletException{
		Query query=em.createQuery("SELECT d FROM Distrito d where d.id=:id");
		query.setParameter("id", id);
		Distrito distrito=(Distrito)query.getSingleResult();
		return distrito;
	}
	
	public List<Distrito> lista() throws ServletException{
		Query query=em.createQuery("SELECT d FROM Distrito d");				
		List<Distrito> lista=(List<Distrito>)query.getResultList();
		return lista;
	}
	
	public List<Distrito> listaFiltro(int id) throws ServletException{
		Query query=em.createQuery("SELECT d FROM Distrito d where d.provincia.id=:id");
		query.setParameter("id", id);		
		List<Distrito> lista=(List<Distrito>)query.getResultList();
		return lista;
	}
	
	public void cargaMasiva(String sql) throws ServletException{
		em.getTransaction().begin();
		Query query=em.createNativeQuery(sql);
		query.executeUpdate();
		em.getTransaction().commit();
	}
}
