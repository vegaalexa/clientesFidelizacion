package com.gestion.fidelizacion.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "sorteo")
public class Sorteo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha_sorteo;
    
    @ManyToOne
    @JoinColumn(name = "regla_id")
    private Regla regla;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "premio_id")
    private Premio premio;
    
    @ManyToOne
    @JoinColumn(name = "bolsa_puntos_id")
    public BolsaPuntos bolsapuntos;


    public Date getFecha_sorteo() {
        return fecha_sorteo;
    }

    public void setFecha_sorteo(Date fecha_sorteo) {
        this.fecha_sorteo = fecha_sorteo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Regla getRegla() {
        return regla;
    }

    public void setRegla(Regla regla) {
        this.regla = regla;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Premio getPremio() {
        return premio;
    }

    public void setPremio(Premio premio) {
        this.premio = premio;
    }

    public BolsaPuntos getBolsapuntos() {
        return bolsapuntos;
    }

    public void setBolsapuntos(BolsaPuntos bolsapuntos) {
        this.bolsapuntos = bolsapuntos;
    }
    

}
