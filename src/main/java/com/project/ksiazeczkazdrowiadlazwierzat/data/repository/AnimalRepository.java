package com.project.ksiazeczkazdrowiadlazwierzat.data.repository;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.Animal;

public interface AnimalRepository extends FirestoreReactiveRepository<Animal> {
}
