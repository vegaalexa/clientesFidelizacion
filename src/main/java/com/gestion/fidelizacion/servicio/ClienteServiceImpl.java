package com.gestion.fidelizacion.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.fidelizacion.entidades.Cliente;
import com.gestion.fidelizacion.repositorios.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
        
	public List<Cliente> listAll(String palabraClave){
            System.out.println(palabraClave + "ingreso en cliente service");
            
            if(palabraClave != null){
                System.out.println(palabraClave + "ingreso en cliente service dentro del if");
                return clienteRepository.findAll(palabraClave);  
            } 
            return clienteRepository.findAll();
	}
     
        
	@Transactional(readOnly = true)
        public List<Cliente> findAll(){
            return clienteRepository.findAll();
        }
        
        
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Cliente empleado) {
		clienteRepository.save(empleado);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}
        
        /*Prueba de busqueda filtro cliente 
        public List<Cliente> findByPalabra(String palabraClave){
            return clienteRepository.findByPalabra(palabraClave);
        }
        */
    
}
