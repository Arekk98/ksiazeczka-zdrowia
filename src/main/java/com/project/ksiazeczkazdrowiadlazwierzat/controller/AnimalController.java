package com.project.ksiazeczkazdrowiadlazwierzat.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.AnimalData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.HealthBookData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.AnimalGeneral;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.CommentData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.VisitData;
import com.project.ksiazeczkazdrowiadlazwierzat.service.AnimalService;
import com.project.ksiazeczkazdrowiadlazwierzat.service.UserService;

import static com.project.ksiazeczkazdrowiadlazwierzat.controller.util.UserUtils.getUsername;
import static com.project.ksiazeczkazdrowiadlazwierzat.controller.util.UserUtils.isLoggedAsVet;

@RestController
public class AnimalController {

    private final AnimalService animalService;
    private final UserService userService;

    public AnimalController(AnimalService animalService, UserService userService) {
        this.animalService = animalService;
        this.userService = userService;
    }

    @GetMapping("/animals")
    public List<AnimalGeneral> animals() {
        if (isLoggedAsVet()) {
            return animalService.getAllAnimals();
        } else {
            return animalService.getAnimalsForUser(getUsername());
        }
    }

    @GetMapping("/animal/{id}")
    public HealthBookData getAnimal(@PathVariable String id) {
        AnimalData animalData = animalService.getAnimal(id, getUsername(), isLoggedAsVet());
        HealthBookData healthBookData = new HealthBookData()
                .setAnimal(animalData)
                .setVisits(animalService.getVisitsForAnimal(animalData.getId()))
                .setComments(animalService.getCommentsForAnimal(animalData.getId()));

        if ((isLoggedAsVet())) {
            healthBookData.setOwner(userService.getUserData(animalData.getOwnerUsername()));
        }

        return healthBookData;
    }

    @PutMapping("/animal")
    public String saveAnimal(@RequestBody AnimalData animalData) {
        if (!isLoggedAsVet()) {
            animalData.setOwnerUsername(getUsername());
        }
        return animalService.saveAnimal(animalData, getUsername(), isLoggedAsVet());
    }

    @DeleteMapping("/animal/{id}")
    public void deleteAnimal(@PathVariable String id) {
        animalService.deleteAnimal(id, getUsername(), isLoggedAsVet());
    }

    @PostMapping("/visit")
    public String newVisit(@RequestParam String animalId, @RequestBody VisitData newVisit) {
        return animalService.newVisit(animalId, newVisit);
    }

    @DeleteMapping("/visit/{id}")
    public void deleteVisit(@PathVariable String id, @RequestParam String animalId) {
        animalService.deleteVisit(id, animalId);
    }

    @PostMapping("/comment")
    public String newComment(@RequestParam String animalId, @RequestBody CommentData newComment) {
        newComment.setDate(LocalDate.now());
        return animalService.newComment(animalId, newComment);
    }

    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable String id, @RequestParam String animalId) {
        animalService.deleteComment(id, animalId);
    }
}
