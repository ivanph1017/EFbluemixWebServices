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

import services.beans.Departamento;
import services.dao.Conexion;
import services.dao.DepartamentoDAO;

@Path("/departamentos")
public class DepartamentosService {
	@GET
	@Consumes("text/plain; charset=utf-8")
	@Produces("application/json; charset=utf-8")
	public Departamento verDepartamento(
			@QueryParam("id") int id){
			try {
				Conexion.getInstancia().conectarse();
				Departamento dpto=new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).obtenerDepartamento(id);
				Conexion.getInstancia().desconectarse();
				return dpto;
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}
	
	
	@Path("/filtro/lista")
	@GET
	@Produces("application/json; charset=utf-8")
	public List<Departamento> listaDepartamentosFiltro(
			@QueryParam("idPais") int idPais){	
		 try {
			 Conexion.getInstancia().conectarse();
			 List<Departamento> lista=new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).listaFiltro(idPais);
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
	public List<Departamento> listaDepartamentos(){	
		 try {
			 Conexion.getInstancia().conectarse();
			 List<Departamento> lista=new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).lista();
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
	public String agregarDepartamento(Departamento departamento){
		try {
			Conexion.getInstancia().conectarse();
			new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).registrar(departamento);
			Conexion.getInstancia().desconectarse();
			return "Departamento registrado satisfactoriamente";
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
	public String cargarDepartamentos(String sql){
		try {
			Conexion.getInstancia().conectarse();
			new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).cargaMasiva(sql);
			Conexion.getInstancia().desconectarse();
			return "Departamentos cargados satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en la carga";
		}
	}
	
	@PUT
	@Consumes("application/json; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public String modificarDepartamento(Departamento departamento){
		try {
			Conexion.getInstancia().conectarse();
			new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).editar(departamento);
			Conexion.getInstancia().desconectarse();
			return "Departamento modificado satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en la modificación";
		}
	}
	@DELETE
	@Consumes("text/plain; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public String eliminarDepartamento(
			@QueryParam("id") int id){
		try {
			Conexion.getInstancia().conectarse();
			new DepartamentoDAO(Conexion.getInstancia().getEntityManager()).eliminar(id);
			Conexion.getInstancia().desconectarse();
			return "Departamento eliminado satisfactoriamente";
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Hubo un error en la eliminación";
		}
	}
}
