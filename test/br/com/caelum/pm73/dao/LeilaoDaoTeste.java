package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTeste {

	private UsuarioDao usuarioDao;
	private Session session;
	private LeilaoDao leilaoDao;

	@Before
	public void antes(){
		session = new CriadorDeSessao().getSession();// Cria conexão com o banco de dados de verdade
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);
		
		session.beginTransaction();
	}
	
	@After
	public void depois() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveContarLeiloesNaoEncerrados() {
		Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");
		
		Leilao ativo = new Leilao("Geladeira", 1500.0, mauricio, false);
		Leilao encerrado = new Leilao("Xbox", 1500.0, mauricio, false);
		encerrado.encerra();
		
		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		
		assertEquals(1L, total);
	
	}
	
	@Test
	public void deveRetornarZeroSeNaoHaLeiloesNovos() {
		Usuario jean = new Usuario("Jean Silva", "jean@silva.com.br");
	
			Leilao encerrado = new Leilao("Xbox", 700.0, jean,false);
			Leilao tambemEncerrado = new Leilao("Geladeira", 1500.0, jean,false);
			encerrado.encerra();
			tambemEncerrado.encerra();
			
			usuarioDao.salvar(jean);
			leilaoDao.salvar(encerrado);
			leilaoDao.salvar(tambemEncerrado);
			
			long total = leilaoDao.total();
			
			assertEquals(0, total);
	}
	
	@Test
	public void deveRetornarLeilaoDeProdutosNovos() {
		Usuario zean = new Usuario("Zean", "zean@zean.com.br");
	
		Leilao produtoNovo = new Leilao("Xbox", 700.0, zean, false);
		Leilao produtoUsuado = new Leilao("Geladeira", 1500.0, zean, true);
		
		usuarioDao.salvar(zean);
		leilaoDao.salvar(produtoNovo);
		leilaoDao.salvar(produtoUsuado);

		List<Leilao> novos = leilaoDao.novos();
		
		assertEquals(1, novos.size());
		assertEquals("Xbox", novos.get(0).getNome());
	}
	
	@Test 
	public void deveTrazerSomenteLeiloesAntigos() {
		Usuario tean = new Usuario("Tean", "tean@tean.com.br");
		Leilao recente = new Leilao("Xbox", 700.0, tean, false);
		Leilao antigo = new Leilao("Geladeira", 1500.0, tean, true);
	
		
		Calendar dataRecente = Calendar.getInstance();
		Calendar dataAntiga = Calendar.getInstance();
		dataAntiga.add(Calendar.DAY_OF_MONTH, -10);
		
		recente.setDataAbertura(dataRecente);
		antigo.setDataAbertura(dataAntiga);
		
		usuarioDao.salvar(tean);
		leilaoDao.salvar(recente);
		leilaoDao.salvar(antigo);
		
		List<Leilao> antigos = leilaoDao.antigos();
	
		assertEquals(1, antigos.size());
		assertEquals("Geladeira", antigos.get(0).getNome());
	}
	
	@Test
	public void deveTrazerSomenteLeiloesAntigosHaMaisDe7Dias() {
		
		Usuario bean = new Usuario("Bean", "bean@bean.com.br");
		
		Leilao noLimite = new Leilao("Xbox", 700.0, bean, false);
	
		Calendar dataAntiga = Calendar.getInstance();
		dataAntiga.add(Calendar.DAY_OF_MONTH, -7);
		
		noLimite.setDataAbertura(dataAntiga);
		
		usuarioDao.salvar(bean);
		leilaoDao.salvar(noLimite);
		
		List<Leilao> antigos = leilaoDao.antigos();
	
		assertEquals(1, antigos.size());
	}
	
}
