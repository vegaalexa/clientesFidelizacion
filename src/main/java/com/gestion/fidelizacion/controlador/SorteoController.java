package com.gestion.fidelizacion.controlador;

import com.gestion.fidelizacion.entidades.BolsaPuntos;
import com.gestion.fidelizacion.entidades.Cliente;
import com.gestion.fidelizacion.entidades.Premio;
import com.gestion.fidelizacion.entidades.Regla;
import com.gestion.fidelizacion.entidades.Sorteo;
import com.gestion.fidelizacion.servicio.PremioService;
import com.gestion.fidelizacion.servicio.BolsaPuntosService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gestion.fidelizacion.servicio.ReglaService;
import com.gestion.fidelizacion.servicio.SorteoService;
import com.gestion.fidelizacion.util.paginacion.PageRender;
import com.gestion.fidelizacion.util.reportes.GanadoresExporterPDF;
import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import es.secaro.thymeleafdemo.dto.Sorteo;
@Controller
public class SorteoController {

    @Autowired
    private ReglaService reglaService;
    
    @Autowired
    private PremioService premioService;
    
    @Autowired
    private BolsaPuntosService bolsapuntosService;
    
    @Autowired
    private SorteoService sorteoService;

    @GetMapping("/sorteoForm")
    public String sorteoForm(Model model) {
        //List<Regla> listadoReglas = reglaService.findAll();
        List<Regla> listadoReglas = reglaService.buscarPuntoshastaMayorA(0);
        model.addAttribute("listadoReglas", listadoReglas);
        List<Premio> listadoPremios = premioService.findAll();
        model.addAttribute("listadoPremios",listadoPremios);
        List<BolsaPuntos> listaBolsapuntos = bolsapuntosService.ListarClientesPorSaldopuntosEntre(1, 50);
        model.addAttribute("listaBolsapuntos",listaBolsapuntos);

        Sorteo sorteo = new Sorteo();
        model.addAttribute("sorteo", sorteo);
        model.addAttribute("titulo", "Sorteo..");
        return "sorteoForm";
    }

    @PostMapping("/sorteoForm")
    public String sorteoSubmit(@Valid Sorteo sorteo, BindingResult result,Model modelo,RedirectAttributes flash,SessionStatus status) {
        sorteo.getRegla().getId();
        System.out.println("ID DE LA REGLA SELECCIONADA:"+ sorteo.getRegla().getId());
        System.out.println("PUNTOS DESDE:"+ sorteo.getRegla().getPuntos_desde());
        System.out.println("PUNTOS HASTA:"+ sorteo.getRegla().getPuntos_hasta());
        Integer puntosDesde = sorteo.getRegla().getPuntos_desde();
        Integer puntosHasta = sorteo.getRegla().getPuntos_hasta();
        List<BolsaPuntos> listaBolsapuntos = bolsapuntosService.ListarClientesPorSaldopuntosEntre(puntosDesde, puntosHasta);
        //el rango del sorteo debe ser entre los clientes que cumplan la condicion de arriba

        Random aleatorio = new Random();
        BolsaPuntos r = listaBolsapuntos.get(aleatorio.nextInt(listaBolsapuntos.size()));
        System.out.println("EL GANADOR EEEESSSSS:"+ r.getId() +"-" +r.cliente.getNombre());
        Cliente clienteGanador = new Cliente(r.cliente.getId());
        sorteo.setCliente(clienteGanador);
        //Seteamos el valor de bolsa_puntos_id en sorteo
        BolsaPuntos bolsapuntos = new BolsaPuntos();
        bolsapuntos.setId(r.getId());
        sorteo.setBolsapuntos(bolsapuntos);

        modelo.addAttribute("listaBolsapuntos", listaBolsapuntos);
       
        sorteoService.save(sorteo);
        status.setComplete();
        flash.addFlashAttribute("success", "registrado con exito");
        return "redirect:/sorteoListar";
    }
    
    
    @GetMapping("/sorteoListar")
    public String listarSorteoGanadores(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Sorteo> sorteo = sorteoService.findAll(pageRequest);
        PageRender<Sorteo> pageRender = new PageRender<>("/sorteoListar", sorteo);

        modelo.addAttribute("titulo","Detalle de los clientes ganadores");
        modelo.addAttribute("sorteo",sorteo);
        modelo.addAttribute("page", pageRender);
	
            return "sorteoListar";
}
        //Metodo para listar el filtrado
        @GetMapping({"/sorteoListar/{palabraClave}"})
	public String listarFiltroSorteo(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo,@Param ("palabraClave") String palabraClave) {
            Pageable pageRequest = PageRequest.of(page, 4);
            Page<Sorteo> sorteo = sorteoService.findAll(pageRequest); 
            PageRender<Sorteo> pageRender = new PageRender<>("/sorteoListar", sorteo);

            System.out.println(palabraClave + "ingreso al controlador");

            List<Sorteo> listadosorteo = sorteoService.listAll(palabraClave);
            //modelo.addAttribute("listadocabecera",listadocabecera);

            modelo.addAttribute("titulo","Filtro de clientes ganadores");
            modelo.addAttribute("listadosorteo",listadosorteo);
            modelo.addAttribute("palabraClave",palabraClave);
            modelo.addAttribute("page", pageRender);

            return "sorteoListar";
	}

    @GetMapping("/sorteoexportarPDF")
    public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
            response.setContentType("application/pdf");

            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String fechaActual = dateFormatter.format(new Date());

            String cabecera = "Content-Disposition";
            String valor = "attachment; filename=Ganadores_" + fechaActual + ".pdf";

            response.setHeader(cabecera, valor);

            List<Sorteo> sorteo = sorteoService.findAll();

            GanadoresExporterPDF exporter = new GanadoresExporterPDF(sorteo);
            exporter.exportar(response);
    }

}
