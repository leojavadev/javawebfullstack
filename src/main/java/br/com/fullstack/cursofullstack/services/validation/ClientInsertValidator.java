package br.com.fullstack.cursofullstack.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullstack.cursofullstack.domain.Cliente;
import br.com.fullstack.cursofullstack.domain.enums.TipoCliente;
import br.com.fullstack.cursofullstack.dto.ClienteNewDTO;
import br.com.fullstack.cursofullstack.repositories.ClienteRepository;
import br.com.fullstack.cursofullstack.resources.exceptions.FieldMessage;
import br.com.fullstack.cursofullstack.services.validation.utils.BR;

public class ClientInsertValidator implements ConstraintValidator<ClientInsert, ClienteNewDTO>{
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClientInsert ann){}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido!"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido!"));
		}
		
		Cliente clienteEmail = clienteRepository.findByEmail(objDto.getEmail());
		
		if(clienteEmail != null){
			list.add(new FieldMessage("email", "E-mail já cadastrado!"));
		}
		
		for(FieldMessage e : list){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
}