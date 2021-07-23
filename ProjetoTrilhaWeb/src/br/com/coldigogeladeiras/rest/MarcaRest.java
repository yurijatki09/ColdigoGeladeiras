	package br.com.coldigogeladeiras.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.jdbc.JDBCProdutoDAO;
import br.com.coldigogeladeiras.modelo.Marca;
import br.com.coldigogeladeiras.modelo.Produto;

//Definindo a classe que irá ser chamada caso alguem acesse o endereço "/ProjetoTrilhaWeb/rest/marca" 
@Path("marca")
public class MarcaRest extends UtilRest {
	
	//Método que irá ser utilizado para a tranferência de informação
	@GET
	//Definindo a classe que irá ser chamada caso alguem acesse o endereço "/ProjetoTrilhaWeb/rest/marca/buscar"
	@Path("/buscar")
	//valor que retornar ao cliente deve ser em JSON
	@Produces(MediaType.APPLICATION_JSON)
	
	//Método para buscar a lista de marcas no banco
	public	Response buscar() {
		try {
			List<Marca> listaMarcas = new ArrayList<Marca>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscar();
			conec.fecharConexao();
			return this.buildResponse(listaMarcas);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir (String marcaParam) {
		
		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			String msg = "";
			
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			boolean verificaMarca = jdbcMarca.verificaMarcaCadastrada(marca.getNome());
			conec.fecharConexao();
			if(verificaMarca) {
				msg = "Marca ja existente!";
				return this.buildErrorResponse(msg);
			}else {
				boolean retorno = jdbcMarca.inserir(marca);
				
				if(retorno) {
					msg = "Marca cadastrada com sucesso!";
					return this.buildResponse(msg);
				}else {
					msg = "Erro ao cadastrar marca";
					return this.buildErrorResponse(msg);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/buscarNome")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {
		
		try {
			
			List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscarPorNome(nome);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaMarcas);
			return this.buildResponse(json);
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {
		
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			String msg = "";
			
			boolean verificacao = jdbcMarca.verificaProduto(id);
			if(verificacao) {
				boolean retorno = jdbcMarca.deletar(id);
				
				if(retorno) {
					msg = "Marca excluída com sucesso!";				
				}else {
					msg = "Erro ao excluir marca.";
					conec.fecharConexao();
					return this.buildErrorResponse(msg);
				}
			}else {
				msg = "A marca não pode ser excluída pois existe um produto registrado com essa marca.";
				conec.fecharConexao();
				return this.buildErrorResponse(msg);
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {
		
		try {
			Marca marca = new Marca();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			marca = jdbcMarca.buscarPorId(id);
			
			conec.fecharConexao();
			
			return this.buildResponse(marca);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String marcaParam) {
		
		try {
			Marca marca = new Gson().fromJson(marcaParam,Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			boolean retorno = jdbcMarca.alterar(marca);

			conec.fecharConexao();
			String msg;
			if(retorno) {
				msg = "Marca alterada com sucesso!";
			}else {
				msg = "Erro ao alterar marca.";
				return this.buildErrorResponse(msg);
			}
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@PUT
	@Path("/alterarStatus/{id}")
	@Consumes("application/*")
	public Response alterarStatus (@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			String msg = "";
			
			
			int retorno = jdbcMarca.alterarStatus(id);
			conec.fecharConexao();
			if(retorno==1) {
				msg = "A marca está ativa!";				
			}else if(retorno==0){
				msg = "A marca está inativa!";
			}else {
				msg = "Erro ao alterar status da marca!";
				return this.buildErrorResponse(msg);
			}
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Erro"+e.getMessage());
			return this.buildErrorResponse(e.getMessage());
		}
	}

}
