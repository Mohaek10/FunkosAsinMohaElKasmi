package dev.mohaek.repositories.funkosRepo;

import dev.mohaek.models.Funko;
import dev.mohaek.repositories.crud.CrudRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FuncoRepository extends CrudRepository<Funko, Long> {
CompletableFuture<List<Funko>> findByNombre(String nombre);
}
