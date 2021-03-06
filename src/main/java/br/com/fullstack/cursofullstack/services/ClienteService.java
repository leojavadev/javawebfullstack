package br.com.fullstack.cursofullstack.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.fullstack.cursofullstack.domain.Cidade;
import br.com.fullstack.cursofullstack.domain.Cliente;
import br.com.fullstack.cursofullstack.domain.Endereco;
import br.com.fullstack.cursofullstack.domain.enums.Perfil;
import br.com.fullstack.cursofullstack.domain.enums.TipoCliente;
import br.com.fullstack.cursofullstack.dto.ClienteDTO;
import br.com.fullstack.cursofullstack.dto.ClienteNewDTO;
import br.com.fullstack.cursofullstack.repositories.ClienteRepository;
import br.com.fullstack.cursofullstack.repositories.EnderecoRepository;
import br.com.fullstack.cursofullstack.resources.exceptions.AuthorizationException;
import br.com.fullstack.cursofullstack.security.UserSS;
import br.com.fullstack.cursofullstack.services.exceptions.DataIntegrityException;
import br.com.fullstack.cursofullstack.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	public List<Cliente> findAll(){
		List<Cliente> lista = repo.findAll();
		if(lista == null) {
			throw new ObjectNotFoundException(
					"Nenhum cliente cadastrado! Tipo: " + Cliente.class.getName()
			);
		}
		return lista;
	}
	
	public Cliente find(Integer id){
		//Verifica se o usuário está logado
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		Optional<Cliente> cliente = repo.findById(id);
		return cliente.orElseThrow(
				() -> new ObjectNotFoundException(
					"Cliente não encontrado com o ID informado: ID = " + id + ""
							+ ", Tipo: " + Cliente.class.getName())
		);
	}
	
	public Cliente findByEmail(String email) {
		//Verifica se o usuário está logado
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		Cliente obj = repo.findByEmail(email);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + user.getId()
					+ ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente fromDto(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDto(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = cidadeService.find(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(
				"Para apagar um cliente, é necessário excluir"
				+ " os seus pedidos!"
			);
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		PageRequest pageRequest = PageRequest.of(
			page, linesPerPage, Direction.valueOf(direction), orderBy
		);
		return repo.findAll(pageRequest);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		//Verifica se o usuário está logado
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		//Deixa a imagem quadrada
		jpgImage = imageService.cropSquare(jpgImage);
		
		//Define a largura e altura da imagem para 200px
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(fileName, imageService.getInputStream(jpgImage, "jpg"), "image");		
	}
}
