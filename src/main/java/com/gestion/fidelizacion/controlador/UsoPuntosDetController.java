package com.gestion.fidelizacion.controlador;

import com.gestion.fidelizacion.entidades.BolsaPuntos;
import com.gestion.fidelizacion.entidades.Cliente;
import com.gestion.fidelizacion.entidades.Premio;
import com.gestion.fidelizacion.entidades.UsoPuntosCab;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.fidelizacion.entidades.UsoPuntosDet;
import com.gestion.fidelizacion.entidades.UsoPuntosCab;
import com.gestion.fidelizacion.servicio.BolsaPuntosService;
import com.gestion.fidelizacion.servicio.UsoPuntosCabService;
import com.gestion.fidelizacion.util.paginacion.PageRender;
import com.gestion.fidelizacion.servicio.UsoPuntosDetService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;

@Controller
public class UsoPuntosDetController {

	@Autowired
	private UsoPuntosDetService usopuntosdetService;
        
        @Autowired
	private UsoPuntosCabService usopuntoscabService;
        
        @Autowired
	private BolsaPuntosService bolsapuntosService;
	
	@GetMapping("/usopuntosdetVer/{id}")
	public String verDetallesDeUsopuntosDet(@PathVariable(value = "id") Long id,Map<String,Object> modelo,RedirectAttributes flash) {
		UsoPuntosDet usopuntosdet = usopuntosdetService.findOne(id);
		if(usopuntosdet == null) {
			flash.addFlashAttribute("error", "No existe en la base de datos");
			return "redirect:/usopuntosdetListar";
		}
		
		modelo.put("usopuntosdet",usopuntosdet);
		modelo.put("titulo", "Detalles de uso de puntos ");
		return "usopuntosdetVer";
	}
	
        @GetMapping({"/usopuntosdetListar/{id}"})
	public String listarUsopuntosDet(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo, @PathVariable(value = "id") Long id) {
		Pageable pageRequest = PageRequest.of(page, 4);
                UsoPuntosCab usopuntoscab = usopuntoscabService.findOne(id);
                usopuntoscab.setId(id);
                
                UsoPuntosDet usopuntosdet = new UsoPuntosDet();
                usopuntosdet.setUso_punto_cab_id(usopuntoscab);
                System.out.println("---------------->ID DE LA CABECERA:"+usopuntosdet.getUso_punto_cab_id().getId());
                /*Obs, trae bien el dato a filtrar pero el parametro por el que espera se filtre debe ser la columna uso_punto_cab_id, no id como 
                está actualmente*/
		//UsoPuntosDet usopuntosdetListar = usopuntosdetService.findOne(usopuntoscab);//findOne(usopuntosdet.getUso_punto_cab_id());
              //Page<UsoPuntosDet> usopuntosdet = usopuntosdetService.findAll(pageRequest);
		//PageRender<UsoPuntosDet> pageRender = new PageRender<>("/usopuntosdetListar", usopuntosdet);
		
		modelo.addAttribute("titulo","Listado de uso de puntos detalle");
		//modelo.addAttribute("usopuntosdet",usopuntosdetListar);
		//modelo.addAttribute("page", pageRender);
		
		return "usopuntosdetListar";
	}
        
        @GetMapping("/cargardetForm/{id}")
        public String cargarUsoPuntosDetalle(Model modelo, @PathVariable(value = "id") Long id, RedirectAttributes flash) {
            System.out.println("---->LLEGOOO AL FORMULARIO PARA CARGAR DETALLE!!!");
            UsoPuntosDet usopuntosdet = null;
		if(id > 0) {
                    System.out.println("---->ENTRO AL ERROR DE DETALLE 1!!!");
			usopuntosdet = usopuntosdetService.findOne(id);
                        //usopuntosdet.setUso_punto_cab_id(id);
                        System.out.println("---->ID:"+ id);
                        
			/*if(usopuntosdet == null) {
				flash.addFlashAttribute("error", "El ID del usopuntosdet no existe en la base de datos");
				return "redirect:/cargardetForm";
			}*/
		}
		else {
                        System.out.println("---->ENTRO AL ERROR DE DETALLE 2!!!");
			flash.addFlashAttribute("error", "El ID del usopuntosdet no puede ser cero");
			return "redirect:/cargardetForm";
		}
                
                List<BolsaPuntos> listBolsapuntos = bolsapuntosService.findAll(); 
                modelo.addAttribute("listBolsapuntos",listBolsapuntos);
            System.out.println("---->PASO 1!!!");
		//UsoPuntosDet usopuntosdet = new UsoPuntosDet();
		modelo.addAttribute("usopuntosdet", usopuntosdet);
		modelo.addAttribute("titulo", "Registro de detalles");
		return "cargardetForm";
	}
	
	@GetMapping("/usopuntosdetForm/{id}")
	public String editarUsopuntosdet(@PathVariable(value = "id") Long id,Map<String, Object> modelo,RedirectAttributes flash) {
		UsoPuntosDet usopuntosdet = null;
		if(id > 0) {
			usopuntosdet = usopuntosdetService.findOne(id);
			if(usopuntosdet == null) {
				flash.addFlashAttribute("error", "El ID del usopuntosdet no existe en la base de datos");
				return "redirect:/usopuntosdetListar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del usopuntosdet no puede ser cero");
			return "redirect:/usopuntosdetListar";
		}
		
		modelo.put("usopuntosdet",usopuntosdet);
		modelo.put("titulo", "Edición de usopuntosdet");
		return "usopuntosdetForm";
	}
	
	@DeleteMapping("/usopuntosdetEliminar/{id}")
	public String eliminarUsopuntosdet(@PathVariable(value = "id") Long id,RedirectAttributes flash) {
		if(id > 0) {
			usopuntosdetService.delete(id);
			flash.addFlashAttribute("success", "Usopuntosdet eliminado con exito");
		}
		return "redirect:/usopuntosdetListar";
	}
        
        
	
}
