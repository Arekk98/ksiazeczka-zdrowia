package com.project.ksiazeczkazdrowiadlazwierzat.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.AnimalData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.AnimalGeneral;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.CommentData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.VisitData;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.Animal;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.AvailableService;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.Comment;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.User;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.Visit;
import com.project.ksiazeczkazdrowiadlazwierzat.data.repository.AnimalRepository;
import com.project.ksiazeczkazdrowiadlazwierzat.data.repository.ServiceRepository;
import com.project.ksiazeczkazdrowiadlazwierzat.data.repository.UserRepository;

import static com.project.ksiazeczkazdrowiadlazwierzat.service.common.Commons.TIMEOUT;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AnimalService(AnimalRepository animalRepository, ServiceRepository serviceRepository, UserRepository userRepository,
                         UserService userService) {
        this.animalRepository = animalRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<AnimalGeneral> getAllAnimals() {
        return animalRepository.findAll()
                .map(this::toGeneral)
                .collectList()
                .block(TIMEOUT);
    }

    public List<AnimalGeneral> getAnimalsForUser(String username) {
        return getAllAnimals().stream()
                .filter(animal -> username.equals(animal.getOwner().getUsername()))
                .collect(Collectors.toList());
    }

    public AnimalData getAnimal(String id, String username, boolean isVet) {
        Optional<Animal> animal = animalRepository.findById(id).blockOptional(TIMEOUT);

        if (isVet || animal.map(Animal::getOwner).filter(owner -> owner.equals(username)).isPresent()) {
            return animal.map(this::convert).orElse(new AnimalData());
        } else {
            return new AnimalData();
        }
    }

    public String saveAnimal(AnimalData animalData, String username, boolean isVet) {
        Animal animal;
        if (StringUtils.isNotBlank(animalData.getId())) {
            animal = animalRepository.findById(animalData.getId()).blockOptional(TIMEOUT).orElse(new Animal());
        } else {
            animal = new Animal();
            animalData.setId(null);
        }

        if (!isVet && StringUtils.isNotBlank(animal.getOwner()) && !Objects.equals(username, animal.getOwner())) {
            return null;
        }

        copyToAnimal(animalData, animal);
        Animal saved = animalRepository.save(animal).blockOptional(TIMEOUT).orElse(new Animal());
        return saved.getId();
    }

    public void deleteAnimal(String id, String username, boolean isVet) {
        if (StringUtils.isBlank(id)) {
            return;
        }
        Optional<Animal> animal = animalRepository.findById(id).blockOptional(TIMEOUT);

        if (isVet || animal.map(Animal::getOwner).filter(owner -> owner.equals(username)).isPresent()) {
            animalRepository.deleteById(id).block(TIMEOUT);
        }
    }

    public List<VisitData> getVisitsForAnimal(String animalId) {
        if (StringUtils.isBlank(animalId)) {
            return Collections.emptyList();
        }

        return animalRepository.findById(animalId)
                .blockOptional(TIMEOUT)
                .map(Animal::getVisits)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public String newVisit(String animalId, VisitData visitData) {
        if (StringUtils.isBlank(animalId)) {
            return null;
        }

        return animalRepository.findById(animalId)
                .blockOptional(TIMEOUT)
                .flatMap(animal -> {
                    animal.getVisits().add(convert(visitData).setId(UUID.randomUUID().toString()));
                    return animalRepository.save(animal).blockOptional(TIMEOUT);
                })
                .map(Animal::getId)
                .orElse(null);
    }

    public void deleteVisit(String visitId, String animalId) {
        if (StringUtils.isNoneBlank(visitId, animalId)) {
            animalRepository.findById(animalId)
                    .blockOptional(TIMEOUT)
                    .ifPresent(animal -> {
                        animal.getVisits().stream()
                                .filter(visit -> visitId.equals(visit.getId()))
                                .findAny()
                                .ifPresent(visit -> {
                                    animal.getVisits().remove(visit);
                                    animalRepository.save(animal).block(TIMEOUT);
                                });
                    });
        }
    }

    public List<CommentData> getCommentsForAnimal(String animalId) {
        if (StringUtils.isBlank(animalId)) {
            return Collections.emptyList();
        }

        return animalRepository.findById(animalId)
                .blockOptional(TIMEOUT)
                .map(Animal::getComments)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public String newComment(String animalId, CommentData commentData) {
        if (StringUtils.isBlank(animalId)) {
            return null;
        }

        return animalRepository.findById(animalId)
                .blockOptional(TIMEOUT)
                .flatMap(animal -> {
                    animal.getComments().add(convert(commentData).setId(UUID.randomUUID().toString()));
                    return animalRepository.save(animal).blockOptional(TIMEOUT);
                })
                .map(Animal::getId)
                .orElse(null);
    }

    public void deleteComment(String commentId, String animalId) {
        if (StringUtils.isNoneBlank(commentId, animalId)) {
            animalRepository.findById(animalId)
                    .blockOptional(TIMEOUT)
                    .ifPresent(animal -> {
                        animal.getComments().stream()
                                .filter(comment -> commentId.equals(comment.getId()))
                                .findAny()
                                .ifPresent(comment -> {
                                    animal.getComments().remove(comment);
                                    animalRepository.save(animal).block(TIMEOUT);
                                });
                    });
        }
    }

    private AnimalData convert(Animal animal) {
        return new AnimalData()
                .setId(animal.getId())
                .setName(animal.getName())
                .setSpecies(animal.getSpecies())
                .setBreed(animal.getBreed())
                .setDateOfBirth(LocalDate.parse(animal.getDateOfBirth()))
                .setSex(animal.getSex())
                .setCoat(animal.getCoat())
                .setTypeOfHair(animal.getTypeOfHair())
                .setOwnerUsername(animal.getOwner())
                .setImageId(animal.getImageId());
    }

    private VisitData convert(Visit visit) {
        return new VisitData()
                .setDate(LocalDateTime.parse(visit.getDateOfVisit()))
                .setId(visit.getId())
                .setVet(userService.getShortData(visit.getVet()))
                .setServicesNames(getServicesNames(visit));
    }

    private Visit convert(VisitData visitData) {
        return new Visit()
                .setDateOfVisit(visitData.getDate().toString())
                .setVet(userRepository.findByUsername(visitData.getVet().getUsername()).blockOptional(TIMEOUT).map(User::getUsername).orElse(null))
                .setServices(serviceRepository.findAllById(visitData.getServicesIds()).collectList().block(TIMEOUT));
    }

    private CommentData convert(Comment comment) {
        return new CommentData()
                .setDate(LocalDate.parse(comment.getDate()))
                .setId(comment.getId())
                .setValue(comment.getValue());
    }

    private Comment convert(CommentData commentData) {
        return new Comment()
                .setDate(commentData.getDate().toString())
                .setValue(commentData.getValue());
    }

    private List<String> getServicesNames(Visit visit) {
        return visit.getServices()
                .stream()
                .map(AvailableService::getName)
                .collect(Collectors.toList());
    }

    private AnimalGeneral toGeneral(Animal animal) {
        return new AnimalGeneral()
                .setId(animal.getId())
                .setName(animal.getName())
                .setSpecies(animal.getSpecies())
                .setOwner(userService.getShortData(animal.getOwner()));
    }

    private void copyToAnimal(AnimalData animalData, Animal animal) {
        animal.setId(animalData.getId())
                .setName(animalData.getName())
                .setSex(animalData.getSex())
                .setDateOfBirth(animalData.getDateOfBirth().toString())
                .setSpecies(animalData.getSpecies())
                .setBreed(animalData.getBreed())
                .setCoat(animalData.getCoat())
                .setTypeOfHair(animalData.getTypeOfHair())
                .setImageId(animalData.getImageId())
                .setOwner(getOwner(animal, animalData.getOwnerUsername()));
    }

    private String getOwner(Animal animal, String username) {
        return userRepository.findByUsername(username)
                .blockOptional(TIMEOUT)
                .map(User::getUsername)
                .orElse(animal.getOwner());
    }
}
