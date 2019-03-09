/**
 * 
 */
package br.leo.lojaSeuManuel.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.leo.lojaSeuManuel.modelo.dao.conexao.ConexaoSql;
import br.leo.lojaSeuManuel.modelo.vo.AtributoCustomizavel;
import br.leo.lojaSeuManuel.modelo.vo.Produto;

/**
 * @author leonardo
 *
 */
public class ProdutoDaoSql implements ProdutoDao {

	
	
	
	/* (non-Javadoc)
	 * @see br.leo.lojaSeuManuel.modelo.dao.ProdutoDao#listar()
	 */
	@Override
	public List<Produto> listar() throws ClassNotFoundException, SQLException {

		PreparedStatement preparedStatement = null;
		
		Connection connection = null;
		
		ResultSet resultSet = null;
		
		String stringSQL="SELECT * FROM produto";
		
		List<Produto> listaDeProdutos = null;
		
		try {
			connection = ConexaoSql.getConnection();
			
			preparedStatement = connection.prepareStatement(stringSQL);
			
			resultSet = preparedStatement.executeQuery();
			
			AtributoCustomizavelDao atributosDao = null;

			while (resultSet.next()) {
				
				if (listaDeProdutos == null) {
					
					listaDeProdutos = new ArrayList<Produto>();
					
				}
				
				if (atributosDao == null) {
					
					atributosDao = new AtributoCustomizavelDaoSql();
					
				}
				
				int idProduto = resultSet.getInt("id_produto");
				
				List<AtributoCustomizavel> listaAtributosTemp = atributosDao.buscaPorChaveEstrangeiraProduto(idProduto);
				
				if (listaAtributosTemp == null) {
					
					listaAtributosTemp = new ArrayList<AtributoCustomizavel>();
					
					listaAtributosTemp.add(new AtributoCustomizavel("sem", "atributo customizável"));
					
				}
				
				listaDeProdutos.add(
						new Produto (
								idProduto,
								resultSet.getString("codigo"),
								resultSet.getString("nome"), 
								resultSet.getString("descricao"),
								resultSet.getInt("estoque"),
								resultSet.getDouble("preco"),
								listaAtributosTemp
						)
				);
				
			}
				
		} finally {
			
			ConexaoSql.closeConnection(connection, preparedStatement, resultSet);
			
		}
		
		return listaDeProdutos;
		
		
	}

	
	
	
	/* (non-Javadoc)
	 * @see br.leo.lojaSeuManuel.modelo.dao.ProdutoDao#buscaPorId(int)
	 */
	@Override
	public Produto buscaPorId(int id) throws ClassNotFoundException, SQLException {
		
		PreparedStatement preparedStatement = null;
		
		Connection connection = null;
		
		ResultSet resultSet = null;
		
		String stringSQL="SELECT * FROM produto WHERE id_produto = ?";
		
		Produto produtoRetorno = null;
		
		try {
			connection = ConexaoSql.getConnection();
			
			preparedStatement = connection.prepareStatement(stringSQL);
			
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				
				AtributoCustomizavelDao atributosDao = new AtributoCustomizavelDaoSql();
				
				int idProduto = resultSet.getInt("id_produto");
				
				List<AtributoCustomizavel> listaAtributos = atributosDao.buscaPorChaveEstrangeiraProduto(idProduto);
				
				if (listaAtributos == null) {
					
					listaAtributos = new ArrayList<AtributoCustomizavel>();
					
					listaAtributos.add(new AtributoCustomizavel("sem", "atributo customizável"));
					
				}
				
				produtoRetorno = new Produto (
								idProduto,
								resultSet.getString("codigo"),
								resultSet.getString("nome"), 
								resultSet.getString("descricao"),
								resultSet.getInt("estoque"),
								resultSet.getDouble("preco"),
								listaAtributos
				);
				
			}
				
		} finally {
			
			ConexaoSql.closeConnection(connection, preparedStatement, resultSet);
			
		}
		
		return produtoRetorno;
		
	}

	
	
	
	
	/* (non-Javadoc)
	 * @see br.leo.lojaSeuManuel.modelo.dao.ProdutoDao#adicionar(br.leo.lojaSeuManuel.modelo.vo.Produto)
	 */
	@Override
	public int adicionar(Produto produto) throws ClassNotFoundException, SQLException {

		PreparedStatement preparedStatement = null;
		
		Connection connection = null;
		
		ResultSet resultSet = null;
		
		int idGerado = 0;
		
		String stringSQL="INSERT INTO produto (codigo, nome, descricao, estoque, preco) VALUES (?,?,?,?,?)";
		
		try {
			
			connection = ConexaoSql.getConnection();
			preparedStatement = connection.prepareStatement(stringSQL, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, produto.getCodigo());
			preparedStatement.setString(2, produto.getNome());
			preparedStatement.setString(3, produto.getDescricao());
			preparedStatement.setInt(4, produto.getEstoque());
			preparedStatement.setDouble(5, produto.getPreco());
			
			preparedStatement.execute();
			
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (resultSet.next()) {
				
				idGerado = resultSet.getInt(1);
				
				AtributoCustomizavelDao atributoCustomizavelDao = new AtributoCustomizavelDaoSql();
				
				for (AtributoCustomizavel atributo : produto.getAtributosCustomizaveis()) {
					
					atributoCustomizavelDao.adicionar(atributo, idGerado);
					
				}
				
			}
			
		} finally {
			
			ConexaoSql.closeConnection(connection, preparedStatement, null);
			
		}
		
		return idGerado;
		
	}

	
	
	
	
	/* (non-Javadoc)
	 * @see br.leo.lojaSeuManuel.modelo.dao.ProdutoDao#editar(br.leo.lojaSeuManuel.modelo.vo.Produto)
	 */
	@Override
	public void editar(Produto produto) throws ClassNotFoundException, SQLException {
		
		PreparedStatement preparedStatement = null;
		
		Connection connection = null;
		
		String stringSQL="UPDATE produto SET codigo = ?, nome = ?, descricao = ?, estoque = ?, preco = ? WHERE id_produto = ?";
		
		try {
			
			connection = ConexaoSql.getConnection();
			preparedStatement = connection.prepareStatement(stringSQL);
			
			preparedStatement.setString(1, produto.getCodigo());
			preparedStatement.setString(2, produto.getNome());
			preparedStatement.setString(3, produto.getDescricao());
			preparedStatement.setInt(4, produto.getEstoque());
			preparedStatement.setDouble(5, produto.getPreco());
			preparedStatement.setInt(6, produto.getId());
			
			preparedStatement.execute();
			
			if (produto.getAtributosCustomizaveis() != null) {
				
				AtributoCustomizavelDao atributoCustomizavelDao = new AtributoCustomizavelDaoSql();
				
				List<AtributoCustomizavel> listDeAtributos = produto.getAtributosCustomizaveis();
				
				for (AtributoCustomizavel atributoCustomizavel : listDeAtributos) {
					
					atributoCustomizavelDao.editar(atributoCustomizavel);
					
				}
				
			}
			
		} finally {
			
			ConexaoSql.closeConnection(connection, preparedStatement, null);
			
		}
		

	}

	
	
	
	/* (non-Javadoc)
	 * @see br.leo.lojaSeuManuel.modelo.dao.ProdutoDao#excluir(int)
	 */
	@Override
	public void excluir(int id) throws ClassNotFoundException, SQLException {
		
		PreparedStatement preparedStatement = null;
		
		Connection connection = null;
		
		String stringSQL="DELETE FROM produto WHERE id_produto = ?";
		
		try {
			
			AtributoCustomizavelDao atributoCustomizavelDao = new AtributoCustomizavelDaoSql();
			
			List<AtributoCustomizavel> listaAtributos = atributoCustomizavelDao.buscaPorChaveEstrangeiraProduto(id);
			
			if (listaAtributos != null) {
				
				for (AtributoCustomizavel atributoCustomizavel : listaAtributos) {
					
					atributoCustomizavelDao.excluir(atributoCustomizavel.getId());
					
				}
				
			}
			
			connection = ConexaoSql.getConnection();
			preparedStatement = connection.prepareStatement(stringSQL);
			
			preparedStatement.setInt(1, id);
			preparedStatement.execute();
			
		} finally {
			
			ConexaoSql.closeConnection(connection, preparedStatement, null);
			
		}
		
	}

}
