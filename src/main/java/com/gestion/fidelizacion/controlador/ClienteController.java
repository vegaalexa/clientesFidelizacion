package com.gestion.fidelizacion.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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

import com.gestion.fidelizacion.entidades.Cliente;
import com.gestion.fidelizacion.util.paginacion.PageRender;
import com.lowagie.text.DocumentException;
import com.gestion.fidelizacion.servicio.ClienteService;

@Controller
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
        
        @GetMapping({"/","/home",""})
	public String listarHome(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 4);
		//Page<Cliente> empleados = clienteService.findAll(pageRequest);
		//PageRender<Cliente> pageRender = new PageRender<>("/listar", empleados);
		
		modelo.addAttribute("titulo","Listado de clientes");
		//modelo.addAttribute("empleados",empleados);
		//modelo.addAttribute("page", pageRender);
		
		return "home";
	}
	
	@GetMapping("/ver/{id}")
	public String verDetallesDelCliente(@PathVariable(value = "id") Long id,Map<String,Object> modelo,RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);
		if(cliente == null) {
			flash.addFlashAttribute("error", "El empleado no existe en la base de datos");
			return "redirect:/listar";
		}
		
		modelo.put("empleado",cliente);
		modelo.put("titulo", "Detalles del empleado " + cliente.getNombre());
		return "ver";
	}
	
	@GetMapping({"/listar",""})
        //@GetMapping({"/","/listar",""})
	public String listarEmpleados(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> cliente = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", cliente);
		
		modelo.addAttribute("titulo","Listado de clientes");
		modelo.addAttribute("empleados",cliente);
		modelo.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@GetMapping("/form")
	public String mostrarFormularioDeRegistrarCliente(Map<String,Object> modelo) {
		Cliente cliente = new Cliente();
		modelo.put("empleado", cliente);
		modelo.put("titulo", "Registro de clientes");
		return "form";
	}
	
	@PostMapping("/form")
	public String guardarCliente(@Valid Cliente cliente,BindingResult result,Model modelo,RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de cliente");
			return "form";
		}
		
		String mensaje = (cliente.getId() != null) ? "El cliente ha sido editato con exito" : "Cliente registrado con exito";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}
	
	@GetMapping("/form/{id}")
	public String editarCliente(@PathVariable(value = "id") Long id,Map<String, Object> modelo,RedirectAttributes flash) {
		Cliente cliente = null;
		if(id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
			return "redirect:/listar";
		}
		
		modelo.put("empleado",cliente);
		modelo.put("titulo", "Edición de cliente");
		return "form";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarCliente(@PathVariable(value = "id") Long id,RedirectAttributes flash) {
		if(id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito");
		}
		return "redirect:/listar";
	}
	
        /*
	@GetMapping("/exportarPDF")
	public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Empleados_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Empleado> empleados = clienteService.findAll();
		
		EmpleadoExporterPDF exporter = new EmpleadoExporterPDF(empleados);
		exporter.exportar(response);
	}
	
	@GetMapping("/exportarExcel")
	public void exportarListadoDeEmpleadosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Empleados_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Empleado> empleados = clienteService.findAll();
		
		EmpleadoExporterExcel exporter = new EmpleadoExporterExcel(empleados);
		exporter.exportar(response);
	}
        */
}
