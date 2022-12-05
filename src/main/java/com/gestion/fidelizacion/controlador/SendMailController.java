package com.gestion.fidelizacion.controlador;

import com.gestion.fidelizacion.entidades.Cliente;
import com.gestion.fidelizacion.entidades.Sorteo;
import com.gestion.fidelizacion.servicio.ClienteService;
import com.gestion.fidelizacion.servicio.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SendMailController {

    @Autowired
    private MailService mailService;
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/sendMail/{id}")
    public String index(@PathVariable(value = "id") Long id, Model modelo){
        System.out.println(">>>>>>>LLEGA A EMAIL:"+id);
        Cliente cliente = clienteService.findOne(id);
        cliente.getEmail();
        System.out.println(">>>>>>>EMAIL:"+cliente.getEmail());
        
        modelo.addAttribute("cliente",cliente);
        return "send_mail_view";
    }

    @PostMapping("/sendMail")
    //public String sendMail(@RequestParam("name") String name, @RequestParam("mail") String mail, @RequestParam("subject") String subject, @RequestParam("body") String body){
    public String sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body, Cliente cliente, @RequestParam("name") String name, @RequestParam("mail") String mail){ 
       
         System.out.println(">>>>>>>LLEGA AL CONTROLADOR"+mail);
         
         
        String message = body +"\n\n Datos de contacto: " + "\nNombre: " + name + "\nE-mail: " + mail;
        //segundo parametro es del cliente
        mailService.sendMail("naty.dominguez28@fpuna.edu.py",mail,subject,message);

        return "send_mail_view";
    }
}
