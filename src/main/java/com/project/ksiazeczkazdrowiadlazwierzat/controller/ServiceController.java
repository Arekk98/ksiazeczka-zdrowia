package com.project.ksiazeczkazdrowiadlazwierzat.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.ServiceData;
import com.project.ksiazeczkazdrowiadlazwierzat.service.ServicesService;

@RestController
public class ServiceController {

    private final ServicesService servicesService;

    public ServiceController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping("/services")
    public List<ServiceData> services() {
        return servicesService.getAllServices();
    }

    @PostMapping("/service")
    private String newService(@RequestBody ServiceData serviceData) {
        return servicesService.addService(serviceData);
    }

    @DeleteMapping("/service/{id}")
    private void removeService(@PathVariable String id) {
        servicesService.deleteService(id);
    }
}
