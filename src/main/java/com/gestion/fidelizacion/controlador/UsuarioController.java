package com.gestion.fidelizacion.controlador;

import com.gestion.fidelizacion.entidades.Usuario;
import com.gestion.fidelizacion.servicio.UsuarioService;
import com.gestion.fidelizacion.util.paginacion.PageRender;
import com.gestion.fidelizacion.util.reportes.UsuarioExporterPDF;
import com.lowagie.text.DocumentException;
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

@Controller
public class UsuarioController {
    
	@Autowired
	private UsuarioService usuarioService;
        
        
	@GetMapping("/usuarioVer/{id}")
	public String verDetallesDelUsuario(@PathVariable(value = "id") Long id,Map<String,Object> modelo,RedirectAttributes flash) {
		Usuario usuario = usuarioService.findOne(id);
		if(usuario == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/usuarioListar";
		}
		
		modelo.put("usuario",usuario);
		modelo.put("titulo", "Detalles del usuario " + usuario.getNombre());
		return "usuarioVer";
	}
	
	@GetMapping({"/usuarioListar",""})
        public String listarUsuarios(@RequestParam(name = "page",defaultValue = "0") int page, Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Usuario> usuario = usuarioService.findAll(pageRequest);
		PageRender<Usuario> pageRender = new PageRender<>("/usuarioListar", usuario);
                
		
                List<Usuario> listaUsuarios = usuarioService.findAll();
                
		modelo.addAttribute("titulo","Listado de usuarios");
		modelo.addAttribute("usuario",listaUsuarios);
                //modelo.addAttribute("palabraClave",palabraClave);
		modelo.addAttribute("page", pageRender);
		
		return "usuarioListar";
	}
        
        /*
        @GetMapping({"/usuarioListar/{palabraClave}",""})
        public String listarClientesFiltrados(@RequestParam(name = "page",defaultValue = "0") int page, Model modelo,@Param ("palabraClave") String palabraClave) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Usuario> usuario = usuarioService.findAll(pageRequest);
		PageRender<Usuario> pageRender = new PageRender<>("/usuarioListar", usuario);
                
		System.out.println(palabraClave + "Ingresa a controlador");
                List<Usuario> listaUsuarios = usuarioService.listAll(palabraClave);
                
		modelo.addAttribute("titulo","Listado de clientes");
		modelo.addAttribute("usuario",listaUsuarios);
                modelo.addAttribute("palabraClave",palabraClave);
		modelo.addAttribute("page", pageRender);
		
		return "usuarioListar";
	}
        */
        
	@GetMapping("/usuarioregistroForm")
	public String mostrarFormularioDeRegistrarCliente(Map<String,Object> modelo) {
		Usuario usuario = new Usuario();
		modelo.put("usuario", usuario);
		modelo.put("titulo", "Registro de usuarios");
		return "usuarioregistroForm";
	}
	
	@PostMapping("/usuarioregistroForm")
	public String guardarUsuario(@Valid Usuario usuario,BindingResult result,Model modelo,RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
                        System.out.println("error en registro");
			modelo.addAttribute("titulo", "Registro de usuario");
			return "usuarioregistroForm";
		}
		
		String mensaje = (usuario.getId() != null) ? "El Usuario ha sido editato con exito" : "Usuario registrado con exito";
		
		usuarioService.save(usuario);
                System.out.println(usuario + "usuario registrado");
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/login";
	}
	
	@GetMapping("/usuarioregistroForm/{id}")
	public String editarUsuario(@PathVariable(value = "id") Long id,Map<String, Object> modelo,RedirectAttributes flash) {
		Usuario usuario = null;
		if(id > 0) {
			usuario = usuarioService.findOne(id);
			if(usuario == null) {
				flash.addFlashAttribute("error", "El ID del usuario no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del usuario no puede ser cero");
			return "redirect:/listar";
		}
		
		modelo.put("usuario",usuario);
		modelo.put("titulo", "Edición de usuario");
		return "usuarioregistroForm";
	}
	
	@GetMapping("/usuarioEliminar/{id}")
	public String eliminarUsuario(@PathVariable(value = "id") Long id,RedirectAttributes flash) {
		if(id > 0) {
			usuarioService.delete(id);
			flash.addFlashAttribute("success", "Usuario eliminado con exito");
		}
		return "redirect:/usuarioListar";
	}
	
	@GetMapping("/usuarioExportarPDf")
	public void exportarListadoDeUsuariosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Usuarios_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Usuario> usuario = usuarioService.findAll();
		
		UsuarioExporterPDF exporter = new UsuarioExporterPDF(usuario);
		exporter.exportar(response);
	}
	
        /*
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