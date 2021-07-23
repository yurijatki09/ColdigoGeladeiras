package br.com.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import br.com.coldigogeladeiras.jdbcinterface.MarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;
import br.com.coldigogeladeiras.modelo.Produto;

public class JDBCMarcaDAO implements MarcaDAO {
	
	private Connection conexao;
	
	public JDBCMarcaDAO(Connection conexao) {
		this.conexao = conexao;
	}
	public List<Marca> buscar(){
		String comando = "SELECT * FROM marcas WHERE status = 1";
		
		List<Marca> listMarcas = new ArrayList<Marca>();
		
		Marca marca = null;
		
		try {
			//Conecta com o banco e prepara para receber um script SQL
			Statement stmt = conexao.createStatement();
			
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while(rs.next()) {
				marca = new Marca();
				
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				int status = rs.getInt("status");
				
				marca.setId(id);
				marca.setNome(nome);
				marca.setStatus(status);
				
				listMarcas.add(marca);
			}
		}catch (Exception ex){
			ex.printStackTrace();			
		}
		
		return listMarcas;
	}
	
	public boolean inserir(Marca marca) {
		
		String comando = "INSERT INTO marcas (id, nome) VALUE (?,?)";
		PreparedStatement p;
		
		try {
			
			//Prepara o comando para a execução no BD em que nos conectamos
			p = this.conexao.prepareStatement(comando);
			
			//Substitui no comando os "?" pelos valores da marca
			p.setInt(1, marca.getId());
			p.setString(2, marca.getNome());
			
			//Executa o comando no BD
			p.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public List<JsonObject> buscarPorNome(String nome){
		
		//Inicia a criação do comando SQL de busca
		String comando = "SELECT * FROM marcas ";
		//Se o item não estiver vazio...
		if(!nome.equals("")) {
			//concatena no comando WHERE buscando no nome da marca o texto da variavel nome
			comando += "WHERE nome LIKE '%" + nome + "%' ";
		}
		//Finaliza o comando ordenando alfabeticamente por nome
		
		comando += "ORDER BY nome ASC";
		
		List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
		JsonObject marca = null;
		
		try {
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			
			while(rs.next()) {
				
				int id = rs.getInt("id");
				String nomeMarca = rs.getString("nome");
				int status = rs.getInt("status");
				
				marca = new JsonObject();
				marca.addProperty("id", id);
				marca.addProperty("nome", nomeMarca);
				marca.addProperty("status", status);
				
				listaMarcas.add(marca);
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listaMarcas;
	}
	
	public boolean deletar (int id) {
		
		String comando = "DELETE FROM marcas WHERE id = ?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Marca buscarPorId(int id) {
		String comando = "SELECT * FROM marcas WHERE marcas.id = ?";
		Marca marca = new Marca();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				String nome = rs.getString("nome");
				
				marca.setId(id);
				marca.setNome(nome);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return marca;
		
	}
	
	public boolean alterar(Marca marca) {
		// TODO Auto-generated method stub
		String comando = "UPDATE marcas SET nome=? WHERE id=?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, marca.getNome());
			p.setInt(2, marca.getId());
			p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean verificaProduto (int id) {
		String comando = "SELECT p.*, m.nome as marca FROM produtos p INNER JOIN marcas m ON p.marcas_id = m.id WHERE marcas_id=?";
		boolean retorno = false;
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			
			if(!rs.next()) {
				retorno = true;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return retorno;
		
	}
	
	public int alterarStatus(int id) {
		
		String comandoConsulta = "SELECT status FROM marcas WHERE id = ?";
		int status = 0;
		try {
			PreparedStatement p = this.conexao.prepareStatement(comandoConsulta);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				status = rs.getInt("status");
			}
			if(status == 1) {
				status = 0;
			}else {
				status = 1;
			}
			String comandoEdicao = "UPDATE marcas SET status=? WHERE id=?";
			
			try {
				PreparedStatement ps = this.conexao.prepareStatement(comandoEdicao);
				ps.setInt(1, status);
				ps.setInt(2, id);
				ps.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
				return 2;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return 2;
		}
		return status;
	}
	
	public boolean verificaMarcaCadastrada(String nome) {
		
		String comando = "SELECT nome FROM marcas WHERE nome = ?";
		boolean retorno = false;
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setString(1, nome);
			ResultSet rs = p.executeQuery();
			if(rs.next()) {
				retorno = true;
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
}
