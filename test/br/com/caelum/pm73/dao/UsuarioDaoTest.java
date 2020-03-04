package br.com.caelum.pm73.dao;


import static org.junit.Assert.assertEquals;


import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {

	private UsuarioDao usuarioDao;
	private Session session;

	@Before
	public void antes(){
		session = new CriadorDeSessao().getSession();// Cria conexão com o banco de dados de verdade
		usuarioDao = new UsuarioDao(session);
	
		session.beginTransaction();
	}
	
	@After
	public void depois() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveEncontrarPeloNomeEEmailMockado() {
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
	}
	
	@Test
	public void deveRetornarNuloSemNaoEncontarUsuario() {
		Usuario usuario = usuarioDao.porNomeEEmail("Joao Joaquim", "joao@joaquim.com.br");
		Assert.assertNull(usuario);
	}
}
