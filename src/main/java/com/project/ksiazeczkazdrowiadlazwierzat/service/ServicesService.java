package com.project.ksiazeczkazdrowiadlazwierzat.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.ServiceData;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.AvailableService;
import com.project.ksiazeczkazdrowiadlazwierzat.data.repository.ServiceRepository;

import static com.project.ksiazeczkazdrowiadlazwierzat.service.common.Commons.TIMEOUT;

@Service
public class ServicesService {

    private final ServiceRepository serviceRepository;

    public ServicesService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceData> getAllServices() {
        return serviceRepository.findAll()
                .map(this::convert)
                .collectList()
                .block(TIMEOUT);
    }

    public String addService(ServiceData serviceData) {
        return serviceRepository.save(convert(serviceData))
                .blockOptional(TIMEOUT)
                .map(AvailableService::getId)
                .orElse(null);
    }

    public void deleteService(String id) {
        if (StringUtils.isNotBlank(id)) {
            serviceRepository.deleteById(id).block(TIMEOUT);
        }
    }

    private ServiceData convert(AvailableService service) {
        return new ServiceData()
                .setId(service.getId())
                .setName(service.getName())
                .setDescription(service.getDescription());
    }

    private AvailableService convert(ServiceData serviceData) {
        return new AvailableService()
                .setName(serviceData.getName())
                .setDescription(serviceData.getDescription());
    }
}
