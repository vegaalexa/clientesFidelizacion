package com.gestion.fidelizacion.repositorios;

//import org.springframework.data.repository.PagingAndSortingRepository;

import com.gestion.fidelizacion.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
