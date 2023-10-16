package dev.mohaek.repositories.crud;

import dev.mohaek.models.Funko;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CrudRepository<T,ID> {
    CompletableFuture<Funko> save(T t) throws SQLException;

    CompletableFuture<Funko> update(T t) throws SQLException;

    CompletableFuture<Optional<Funko>> findById(ID id) throws SQLException;

    CompletableFuture<Void> deleteAll() throws SQLException;

    CompletableFuture<Boolean> deleteById(ID id) throws SQLException;

    CompletableFuture<List<Funko>> findAll() throws SQLException;
}
