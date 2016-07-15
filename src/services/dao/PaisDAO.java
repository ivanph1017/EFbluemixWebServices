package services.dao;

import java.net.UnknownHostException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import services.beans.Departamento;
import services.beans.Distrito;
import services.beans.Pais;
import services.beans.Provincia;

public class PaisDAO {
	private EntityManager em;
	public PaisDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public void registrar(Pais pais) throws ServletException{
		
		em.getTransaction().begin();
		em.persist(pais);
		em.getTransaction().commit();
	}
	
	public void editar(Pais pais) throws ServletException{
		em.getTransaction().begin();
		em.merge(pais);
		em.getTransaction().commit();
	}
	
	public void eliminar(int id) throws ServletException{
		
		DepartamentoDAO departamentoDAO=new DepartamentoDAO(em);
		ProvinciaDAO provinciaDAO=new ProvinciaDAO(em);
		DistritoDAO distritoDAO=new DistritoDAO(em);
		List<Departamento> listaDepartamentos=departamentoDAO.listaFiltro(id);
		List<Provincia> listaProvincias;
		List<Distrito> listaDistritos;
		
		for(Departamento departamento : listaDepartamentos){
			listaProvincias=provinciaDAO.listaFiltro(departamento.getId());
			for(Provincia provincia : listaProvincias){
				listaDistritos=distritoDAO.listaFiltro(provincia.getId());
				for(Distrito distrito : listaDistritos){
					distritoDAO.eliminar(distrito.getId());
				}
				provinciaDAO.eliminar(provincia.getId());
			}
			departamentoDAO.eliminar(departamento.getId());
		}
		em.getTransaction().begin();
		Pais pais=obtenerPais(id);
		em.remove(pais);
		em.getTransaction().commit();
	}
	
	public Pais obtenerPais(int id) throws ServletException{
		Query query=em.createQuery("SELECT p FROM Pais p where p.id=:id");
		query.setParameter("id", id);
		Pais pais=(Pais)query.getSingleResult();
		return pais;
	}
	
	public List<Pais> lista() throws ServletException{
		Query query=em.createQuery("SELECT p FROM Pais p");				
		List<Pais> lista=(List<Pais>)query.getResultList();
		return lista;
	}
	
	public void cargaMasiva(String sql) throws ServletException{
		em.getTransaction().begin();
		Query query=em.createNativeQuery(sql);
		query.executeUpdate();
		em.getTransaction().commit();
	}
	
	public void cargaMongo(int id) throws ServletException{
		MongoClientURI uri = new MongoClientURI("mongodb://root:root@ds036638.mlab.com:36638/paises20133037");
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(uri);
			DB db = mongoClient.getDB("paises20133037");
			
			DBCollection colPaises = db.getCollection("paises");
			DBCollection colDepartamentos = db.getCollection("departamentos");
			DBCollection colProvincias = db.getCollection("provincias");
			DBCollection colDistritos = db.getCollection("distritos");
			
			List<Departamento> departamentosMySQL=new DepartamentoDAO(em).listaFiltro(id);
			List<Provincia> provinciasMySQL;
			List<Distrito> distritosMySQL;
			
			for(Departamento depa : departamentosMySQL){						
				
				provinciasMySQL=new ProvinciaDAO(em).listaFiltro(depa.getId());
				for(Provincia prov : provinciasMySQL){							
					
					distritosMySQL=new DistritoDAO(em).listaFiltro(prov.getId());				
					for(Distrito dist : distritosMySQL){
						
						DBObject docDistrito=new BasicDBObject();
						docDistrito.put("id", dist.getId());
						docDistrito.put("provincia_id", dist.getProvincia().getId());
						docDistrito.put("nombre", dist.getNombre());
						docDistrito.put("poblacion", dist.getPoblacion());
						
						colDistritos.insert(docDistrito);
						
					}
					
					DBObject docProvincia=new BasicDBObject();
					docProvincia.put("id", prov.getId());
					docProvincia.put("departamento_id", prov.getDepartamento().getId());
					docProvincia.put("nombre", prov.getNombre());				
					
					colProvincias.insert(docProvincia);
				}
				
				DBObject docDepartamento=new BasicDBObject();
				docDepartamento.put("id", depa.getId());
				docDepartamento.put("nombre", depa.getNombre());
				docDepartamento.put("pais_id", depa.getPais().getId());
							
				colDepartamentos.insert(docDepartamento);
			}
			
			DBObject docPais=new BasicDBObject();
					
			Pais pais=obtenerPais(id);
			
			docPais.put("id", pais.getId());
			docPais.put("nombre", pais.getNombre());
			docPais.put("poblacion", pais.getPoblacion());
			docPais.put("pbi", pais.getPbi());
			
			colPaises.insert(docPais);
			
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
