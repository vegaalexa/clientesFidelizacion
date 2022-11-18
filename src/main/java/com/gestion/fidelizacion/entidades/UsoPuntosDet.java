package com.gestion.fidelizacion.entidades;

import java.util.Date;

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
@Table(name = "uso_puntos_det")
public class UsoPuntosDet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int puntaje_utilizado;

    @ManyToOne
    @JoinColumn(name = "uso_punto_cab_id")
    private UsoPuntosCab uso_punto_cab_id;
    
    @ManyToOne
    @JoinColumn(name = "bolsa_punto_id")
    private BolsaPuntos bolsapuntos;
    
    /*Relacion muchos a uno con la entidad Cliente*/
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    
    public UsoPuntosDet() {
            super();
    }

    public UsoPuntosDet(int puntaje_utilizado, UsoPuntosCab uso_punto_cab_id, BolsaPuntos bolsapuntos,  Cliente cliente) {
        this.puntaje_utilizado = puntaje_utilizado;
        this.uso_punto_cab_id = uso_punto_cab_id;
        this.bolsapuntos = bolsapuntos;
        this.cliente = cliente;
    }

    public UsoPuntosDet(Long id, int puntaje_utilizado, UsoPuntosCab uso_punto_cab_id, BolsaPuntos bolsapuntos,  Cliente cliente) {
        this.id = id;
        this.puntaje_utilizado = puntaje_utilizado;
        this.uso_punto_cab_id = uso_punto_cab_id;
        this.bolsapuntos = bolsapuntos;
        this.cliente = cliente;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntaje_utilizado() {
        return puntaje_utilizado;
    }

    public void setPuntaje_utilizado(int puntaje_utilizado) {
        this.puntaje_utilizado = puntaje_utilizado;
    }

    public UsoPuntosCab getUso_punto_cab_id() {
        return uso_punto_cab_id;
    }

    public void setUso_punto_cab_id(UsoPuntosCab uso_punto_cab_id) {
        this.uso_punto_cab_id = uso_punto_cab_id;
    }

    public BolsaPuntos getBolsapuntos() {
        return bolsapuntos;
    }

    public void setBolsapuntos(BolsaPuntos bolsapuntos) {
        this.bolsapuntos = bolsapuntos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
      
}
