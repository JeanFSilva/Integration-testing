package br.com.caelum.pm73.dao;


import static org.junit.Assert.assertEquals;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {

	@Test
	public void deveEncontrarPeloNomeEEmailMockado() {
		//NÃO ADIANTA
		// o Dao fala com o banco de dados
		//A única maneira de saber se funciona corretamente é batendo no banco de dados
//		Session session = Mockito.mock(Session.class);
//		Query query = Mockito.mock(Query.class);
//		UsuarioDao usuarioDao = new UsuarioDao(session);
//	
//		String sql = "from Usuario u where u.nome = :nome and u.email = :email";
//		
//		Usuario usuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
//		Mockito.when(session.createQuery(sql)).thenReturn(query);
//		Mockito.when(query.uniqueResult()).thenReturn(usuario);
//		Mockito.when(query.setParameter("nome", "Joao da Silva")).thenReturn(query);
//		Mockito.when(query.setParameter("email", "joao@dasilva.com.br")).thenReturn(query);
//	
//		Usuario usuarioDoBanco = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
//	
//		assertEquals(usuario.getNome(), usuarioDoBanco.getNome());
//		assertEquals(usuario.getEmail(), usuarioDoBanco.getEmail());
		
		
		Session session = new CriadorDeSessao().getSession();// Cria conexão com o banco de dados de verdade
		UsuarioDao usuarioDao = new UsuarioDao(session);
		
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
		session.close();
	}
	
}
