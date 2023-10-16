package dev.mohaek.services;

import dev.mohaek.models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FuncoCacheImpl implements FunkoCache{

    private final Logger logger = LoggerFactory.getLogger(FuncoCacheImpl.class);

    private final int maxSize;

    private final Map<Long,Funko> cache;

    private final ScheduledExecutorService cleaner;

    public FuncoCacheImpl(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize,0.75f,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Funko> eldest) {
                return size() > maxSize;
            }
        };
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.cleaner.scheduleAtFixedRate(this::clear,1,1,java.util.concurrent.TimeUnit.MINUTES);
    }
    @Override
    public void put(Long key, Funko value) {
        logger.debug("Añadiendo funko a cache con id: " + key + " y valor: " + value);
        cache.put(key,value);
    }

    @Override
    public Funko get(Long key) {
        logger.debug("Obteniendo funko de cache con id: " + key);
        return cache.get(key);
    }

    @Override
    public void remove(Long key) {
        logger.debug("Eliminando funko de cache con id: " + key);
        cache.remove(key);
    }

    @Override
    public void clear() {

    }

    @Override
    public void shutdown() {
        cleaner.shutdown();
    }
}
