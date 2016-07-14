package services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;

import services.beans.Departamento;
import services.beans.Distrito;
import services.beans.Provincia;

public class ProvinciaDAO {
private EntityManager em;	
	
	public ProvinciaDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public void registrar(Provincia provincia) throws ServletException{
		
		em.getTransaction().begin();
		em.persist(provincia);
		em.getTransaction().commit();
	}
	public void editar(Provincia provincia) throws ServletException{
		em.getTransaction().begin();
		em.merge(provincia);
		em.getTransaction().commit();
	}
	
	public void eliminar(int id) throws ServletException{
				
		DistritoDAO distritoDAO=new DistritoDAO(em);
		List<Distrito> listaDistritos=distritoDAO.listaFiltro(id);
		
		for(Distrito distrito : listaDistritos){
			distritoDAO.eliminar(distrito.getId());
		}
		em.getTransaction().begin();
		Provincia provincia=obtenerProvincia(id);
		em.remove(provincia);
		em.getTransaction().commit();
	}
	
	public Provincia obtenerProvincia(int id) throws ServletException{
		Query query=em.createQuery("SELECT p FROM Provincia p where p.id=:id");
		query.setParameter("id", id);
		Provincia provincia=(Provincia)query.getSingleResult();
		return provincia;
	}
	
	public List<Provincia> lista() throws ServletException{
		Query query=em.createQuery("SELECT p FROM Provincia p");				
		List<Provincia> lista=(List<Provincia>)query.getResultList();
		return lista;
	}
	
	public List<Provincia> listaFiltro(int id) throws ServletException{
		Query query=em.createQuery("SELECT p FROM Provincia p where p.departamento.id=:id");
		query.setParameter("id", id);		
		List<Provincia> lista=(List<Provincia>)query.getResultList();
		return lista;
	}
	
	public void cargaMasiva(String sql) throws ServletException{
		em.getTransaction().begin();
		Query query=em.createNativeQuery(sql);
		query.executeUpdate();
		em.getTransaction().commit();
	}
}
