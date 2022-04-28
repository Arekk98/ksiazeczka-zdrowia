package com.project.ksiazeczkazdrowiadlazwierzat.data.repository;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.User;

public interface UserRepository extends FirestoreReactiveRepository<User> {
    Mono<User> findByUsername(String username);
    Flux<User> findAllByRole(String role);
}
