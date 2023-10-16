package dev.mohaek.services;

import dev.mohaek.models.Funko;

import java.util.List;

public interface FunkoService {
    List<Funko> findAll() throws Exception;

    List<Funko> findByNombre(String nombre) throws Exception;

    Funko findById(long id) throws Exception;

    Funko save(Funko funko) throws Exception;

    Funko update(Funko funko) throws Exception;

    boolean deleteById(long id) throws Exception;

    void deleteAll() throws Exception;

}
