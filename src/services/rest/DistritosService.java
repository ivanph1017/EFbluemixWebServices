package services.rest;

import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import services.beans.Distrito;
import services.dao.Conexion;
import services.dao.DistritoDAO;

@Path("/distritos")
public class DistritosService {
	@GET
	@Consumes("text/plain; charset=utf-8")
	@Produces("application/json; charset=utf-8")
	public Distrito verDistrito(
			@QueryParam("id") int id){
			try {
				Conexion.getInstancia().conectarse();
				Distrito dist=new DistritoDAO(Conexion.getInstancia().getEntityManager()).obtenerDistrito(id);
				Conexion.getInstancia().desconectarse();
				return dist;
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}
	
	
	@Path("/filtro/lista")
	@GET
	@Produces("application/json; charset=utf-8")
	public List<Distrito> listaDistritosFiltro(
			@QueryParam("idProvincia") int idProvincia){	
		 try {
			 Conexion.getInstancia().conectarse();
			 List<Distrito> lista=new DistritoDAO(Conexion.getInstancia().getEntityManager()).listaFiltro(idProvincia);
			 Conexion.getInstancia().desconectarse();
			 return lista; 			 
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Path("/lista")
	@GET
	@Produces("application/json; charset=utf-8")
	public List<Distrito> listaDistritos(){	
		 try {
			 Conexion.getInstancia().conectarse();
			 List<Distrito> lista=new DistritoDAO(Conexion.getInstancia().getEntityManager()).lista();			 
			 Conexion.getInstancia().desconectarse();
			 return lista;
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@POST
	@Consumes("application/json; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public String agregarDistrito(Distrito distrito){
		try {
			Conexion.getInstancia().conectarse();
			new DistritoDAO(Conexion.getInstancia().getEntityManager()).registrar(distrito);
			Conexion.getInstancia().desconectarse();
			return "Distrito registrado satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en el registro";
		}
	}
	
	@Path("/carga")
	@POST
	@Consumes("text/plain; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public String cargarDistritos(String sql){
		try {
			Conexion.getInstancia().conectarse();
			new DistritoDAO(Conexion.getInstancia().getEntityManager()).cargaMasiva(sql);
			Conexion.getInstancia().desconectarse();
			return "Distritos cargados satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en la carga";
		}
	}
	@PUT
	@Consumes("application/json; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public String modificarDistrito(Distrito distrito){
		try {
			Conexion.getInstancia().conectarse();
			new DistritoDAO(Conexion.getInstancia().getEntityManager()).editar(distrito);
			Conexion.getInstancia().desconectarse();
			return "Distrito modificado satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en la modificación";
		}
	}
	@DELETE
	@Consumes("text/plain; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public String eliminarDistrito(
			@QueryParam("id") int id){
		try {
			Conexion.getInstancia().conectarse();
			new DistritoDAO(Conexion.getInstancia().getEntityManager()).eliminar(id);
			Conexion.getInstancia().desconectarse();
			return "Distrito eliminado satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en la eliminación";
		}
	}
}
