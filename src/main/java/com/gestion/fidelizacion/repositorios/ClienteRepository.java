package com.gestion.fidelizacion.repositorios;

//import org.springframework.data.repository.PagingAndSortingRepository;

import com.gestion.fidelizacion.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    
    @Query(value = "select * from clientes c where c.nombre like %?1% or c.apellido like %?1% or to_char(c.fecha_nac, 'yyyy-mm-dd') like %?1%", nativeQuery=true)
   
    public List<Cliente> findAll(String palabraClave);
    
    //List<Cliente> findByPalabra(@Param("palabraClave") String palabraClave);
    
}
