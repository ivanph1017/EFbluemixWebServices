package services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;

import services.beans.Departamento;
import services.beans.Distrito;
import services.beans.Pais;
import services.beans.Provincia;

public class DepartamentoDAO {
	private EntityManager em;
	public DepartamentoDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public void registrar(Departamento dpto) throws ServletException{
		
		em.getTransaction().begin();
		em.persist(dpto);
		em.getTransaction().commit();
	}
	
	public void editar(Departamento dpto) throws ServletException{
		em.getTransaction().begin();
		em.merge(dpto);
		em.getTransaction().commit();
	}
	
	public void eliminar(int id) throws ServletException{
		
		ProvinciaDAO provinciaDAO=new ProvinciaDAO(em);
		DistritoDAO distritoDAO=new DistritoDAO(em);
		List<Provincia> listaProvincias=provinciaDAO.listaFiltro(id);
		List<Distrito> listaDistritos;
		
		for(Provincia provincia : listaProvincias){
			listaDistritos=distritoDAO.listaFiltro(provincia.getId());
			for(Distrito distrito : listaDistritos){
				distritoDAO.eliminar(distrito.getId());
			}
			provinciaDAO.eliminar(provincia.getId());
		}
		em.getTransaction().begin();
		Departamento dpto=obtenerDepartamento(id);
		em.remove(dpto);
		em.getTransaction().commit();
	}
	
	public Departamento obtenerDepartamento(int id) throws ServletException{
		Query query=em.createQuery("SELECT d FROM Departamento d where d.id=:id");
		query.setParameter("id", id);
		Departamento dpto=(Departamento)query.getSingleResult();
		return dpto;
	}
	
	public List<Departamento> lista() throws ServletException{
		Query query=em.createQuery("SELECT d FROM Departamento d");				
		List<Departamento> lista=(List<Departamento>)query.getResultList();
		return lista;
	}
	
	public List<Departamento> listaFiltro(int id) throws ServletException{
		Query query=em.createQuery("SELECT d FROM Departamento d where d.pais.id=:id");
		query.setParameter("id", id);		
		List<Departamento> lista=(List<Departamento>)query.getResultList();
		return lista;
	}
	
	public void cargaMasiva(String sql) throws ServletException{
		em.getTransaction().begin();
		Query query=em.createNativeQuery(sql);
		query.executeUpdate();
		em.getTransaction().commit();
	}
}
