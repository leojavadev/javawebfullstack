package br.com.fullstack.cursofullstack.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullstack.cursofullstack.domain.Cliente;
import br.com.fullstack.cursofullstack.domain.ItemPedido;
import br.com.fullstack.cursofullstack.domain.PagamentoComBoleto;
import br.com.fullstack.cursofullstack.domain.Pedido;
import br.com.fullstack.cursofullstack.domain.enums.EstadoPagamento;
import br.com.fullstack.cursofullstack.repositories.ItemPedidoRepository;
import br.com.fullstack.cursofullstack.repositories.PagamentoRepository;
import br.com.fullstack.cursofullstack.repositories.PedidoRepository;
import br.com.fullstack.cursofullstack.resources.exceptions.AuthorizationException;
import br.com.fullstack.cursofullstack.security.UserSS;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Optional<Pedido> find(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		if(pedido == null) {
			throw new ObjectNotFoundException(
					"Pedido não encontrado. ID: " + id + " Tipo: " + Pedido.class.getName()
			);
		}
		return pedido;
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}		
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.);
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		obj = repo.save(obj);
		
		/** Imprime os dados do pedido no console */
		//System.out.print(obj);
		
		/** Mostra os dados do pedido no log do spring (no perfil Test) ou envia por e-mail (no perfil Dev)  */
		emailService.sendOrderConfirmationEmail(obj);
		
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}
