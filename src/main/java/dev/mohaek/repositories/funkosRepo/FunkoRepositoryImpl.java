package dev.mohaek.repositories.funkosRepo;

import dev.mohaek.models.Funko;
import dev.mohaek.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FunkoRepositoryImpl implements FuncoRepository {
    private static FunkoRepositoryImpl instance;

    private final Logger logger = LoggerFactory.getLogger(FunkoRepositoryImpl.class);

    private final DatabaseManager db;

    public FunkoRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }

    public static FunkoRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new FunkoRepositoryImpl(db);
        }
        return instance;
    }


    @Override
    public CompletableFuture<Funko> save(Funko funko) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO FUNKOS (cod,MyID,nombre,modelo,precio, fecha) VALUES (?,?,?,?,?,?)";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ) {
                logger.debug("Insertando funko " + funko);
                stmt.setObject(1, funko.getUuid());
                stmt.setLong(2, funko.getMyId());
                stmt.setString(3, funko.getName());
                stmt.setObject(4, funko.getModelo());
                stmt.setDouble(5, funko.getPrecio());
                stmt.setObject(6, funko.getFecha_lanzamiento());
                var res = stmt.executeUpdate();
                if (res > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    while (rs.next()) {
                        funko.setId(rs.getLong(1));
                    }
                    rs.close();
                } else {
                    logger.error("Error al guardar el funko");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return funko;
        });
    }


    @Override
    public CompletableFuture<Funko> update(Funko funko) throws SQLException {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Funko>> findById(Long aLong) throws SQLException {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteAll() throws SQLException {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Long aLong) throws SQLException {
        return null;
    }

    @Override
    public CompletableFuture<List<Funko>> findAll() throws SQLException {
        return null;
    }

    @Override
    public CompletableFuture<List<Funko>> findByNombre(String nombre) {
        return null;
    }
}
