package services.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
		MongoClient mongoClient = new MongoClient( "mongodb://ivan:sistemas2@ds011903.mlab.com:11903");
		MongoDatabase database = mongoClient.getDatabase("peru20133037");
		
		MongoCollection<Document> colPaises = database.getCollection("paises");
		MongoCollection<Document> colDepartamentos = database.getCollection("departamentos");
		MongoCollection<Document> colProvincias = database.getCollection("provincias");
		MongoCollection<Document> colDistritos = database.getCollection("distritos");
		
		List<Departamento> departamentosMySQL=new DepartamentoDAO(em).listaFiltro(id);
		List<Provincia> provinciasMySQL;
		List<Distrito> distritosMySQL;
			
		Document docDepartamento;
		Document docProvincia;
		Document docDistrito;
		
		for(Departamento depa : departamentosMySQL){
			docDepartamento=new Document("id", depa.getId())
					.append("nombre", depa.getNombre())
					.append("pais_id", depa.getPais().getId());				
			
			provinciasMySQL=new ProvinciaDAO(em).listaFiltro(depa.getId());
			for(Provincia prov : provinciasMySQL){
				docProvincia=new Document("id", prov.getId())
						.append("departamento_id", prov.getDepartamento().getId())
						.append("nombre", prov.getNombre());				
				
				distritosMySQL=new DistritoDAO(em).listaFiltro(prov.getId());
				for(Distrito dist : distritosMySQL){
					docDistrito=new Document("id", dist.getId())
							.append("provincia_id", dist.getProvincia().getId())
							.append("nombre", dist.getNombre())
							.append("poblacion", dist.getPoblacion());
					colDistritos.insertOne(docDistrito);
					
				}
				
				colProvincias.insertOne(docProvincia);
			}
			
			colDepartamentos.insertOne(docDepartamento);
		}
		
		Pais pais=obtenerPais(id);
		Document doc=new Document("id", pais.getId())
				.append("nombre", pais.getNombre())
				.append("poblacion", pais.getPoblacion())
				.append("pbi", pais.getPbi());
		colPaises.insertOne(doc);
		
		mongoClient.close();
		
	}
	
}
