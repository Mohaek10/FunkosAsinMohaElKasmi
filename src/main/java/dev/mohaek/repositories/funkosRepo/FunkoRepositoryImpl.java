package dev.mohaek.repositories.funkosRepo;

import dev.mohaek.models.Funko;
import dev.mohaek.models.Modelo;
import dev.mohaek.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        return CompletableFuture.supplyAsync(()->{
            String query = "UPDATE FUNKOS SET cod = ?, MyID = ?, nombre = ?, modelo = ?, precio = ?, fecha = ? WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Actualizando el funko: " + funko);
                stmt.setObject(1, funko.getUuid());
                stmt.setLong(2, funko.getMyId());
                stmt.setString(3, funko.getName());
                stmt.setObject(4, funko.getModelo());
                stmt.setDouble(5, funko.getPrecio());
                stmt.setObject(6, funko.getFecha_lanzamiento());
                stmt.setLong(7, funko.getId());
                var res = stmt.executeUpdate();
                if (res > 0) {
                    logger.debug("Funko actualizado");
                } else {
                    logger.error("Error al actualizar el funko");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return funko;
        });
    }

    @Override
    public CompletableFuture<Optional<Funko>> findById(Long aLong) throws SQLException {
        return CompletableFuture.supplyAsync(()->{
           Optional<Funko> funko = Optional.empty();
              String query = "SELECT * FROM FUNKOS WHERE id = ?";
              try (var connection = db.getConnection();
                   var stmt = connection.prepareStatement(query)
              ) {
                  logger.debug("Buscando el funko con id: " + aLong);
                  stmt.setLong(1, aLong);
                  var res = stmt.executeQuery();
                  if (res.next()) {
                      funko = Optional.of(Funko.builder()
                              .id(res.getLong("id"))
                              .uuid(res.getObject("cod", UUID.class))
                              .myId(res.getLong("MyID"))
                              .name(res.getString("nombre"))
                              .modelo(Modelo.valueOf(res.getString("modelo")))
                              .precio(res.getDouble("precio"))
                              .fecha_lanzamiento(res.getDate("fecha").toLocalDate())
                              .build());
                  } else {
                      logger.error("Funko no encontrado con id: " + aLong);
                  }
              } catch (SQLException e) {
                  throw new RuntimeException(e);
              }
              return funko;
        });
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
