package com.gestion.fidelizacion.repositorios;

//import org.springframework.data.repository.PagingAndSortingRepository;

import com.gestion.fidelizacion.entidades.Premio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremioRepository extends JpaRepository<Premio, Long>{

}
