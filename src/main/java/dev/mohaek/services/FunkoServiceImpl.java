package dev.mohaek.services;

import dev.mohaek.models.Funko;
import dev.mohaek.repositories.funkosRepo.FuncoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class FunkoServiceImpl implements FunkoService {
    private static final int CACHE_TAMANO = 10;
    private static FunkoServiceImpl instance;
    private final FunkoCache cache;
    private final Logger logger= LoggerFactory.getLogger(FunkoServiceImpl.class);
    private final FuncoRepository repository;

    private FunkoServiceImpl(FuncoRepository repository) {
        this.repository = repository;
        this.cache = new FuncoCacheImpl(CACHE_TAMANO);
    }

    public static FunkoServiceImpl getInstance(FuncoRepository repository) {
        if(instance==null){
            instance = new FunkoServiceImpl(repository);
        }
        return instance;
    }
    @Override
    public List<Funko> findAll() throws Exception {
        return repository.findAll().get();
    }

    @Override
    public List<Funko> findByNombre(String nombre) throws Exception {
        return repository.findByNombre(nombre).get();
    }

    @Override
    public Optional<Funko> findById(long id) throws Exception {
        Funko funko = cache.get(id);
        if(funko!=null){
            logger.debug("Funko encontrado en cache");
            return Optional.of(funko);
        }else{
            logger.debug("Funko no encontrado en cache");
           return repository.findById(id).get();
        }
    }

    @Override
    public Funko save(Funko funko) throws Exception {
       funko = repository.save(funko).get();
         cache.put(funko.getId(),funko);
            return funko;
    }

    @Override
    public Funko update(Funko funko) throws Exception {
        funko = repository.update(funko).get();
        cache.put(funko.getId(),funko);
        return funko;
    }

    @Override
    public boolean deleteById(long id) throws Exception {
        var deleted =repository.deleteById(id).get();

        if(deleted){
            cache.remove(id);
        }
        return deleted;

    }

    @Override
    public void deleteAll() throws Exception {
        repository.deleteAll().get();
        cache.clear();
    }
}
