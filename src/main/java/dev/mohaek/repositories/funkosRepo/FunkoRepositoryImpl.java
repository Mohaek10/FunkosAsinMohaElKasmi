package dev.mohaek.repositories.funkosRepo;

import dev.mohaek.models.Funko;
import dev.mohaek.models.Modelo;
import dev.mohaek.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        return CompletableFuture.runAsync(()->{
            String query = "DELETE FROM FUNKOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Borrando todos los funkos");
                var res = stmt.executeUpdate();
                if (res > 0) {
                    logger.debug("Funkos borrados");
                } else {
                    logger.error("Error al borrar los funkos");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public CompletableFuture<Boolean> deleteById(Long aLong) throws SQLException {
        return CompletableFuture.supplyAsync(()->{
            boolean deleted = false;
            String query = "DELETE FROM FUNKOS WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Borrando el funko con id: " + aLong);
                stmt.setLong(1, aLong);
                var res = stmt.executeUpdate();
                if (res > 0) {
                    logger.debug("Funko borrado");
                    deleted = true;
                } else {
                    logger.error("Error al borrar el funko");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return deleted;

        });
    }

    @Override
    public CompletableFuture<List<Funko>> findAll() throws SQLException {
        return CompletableFuture.supplyAsync(()->{
            List<Funko> lista=new ArrayList<>();
            String query = "SELECT * FROM FUNKOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Buscando todos los funkos");
                var res = stmt.executeQuery();
                while (res.next()) {
                    lista.add(Funko.builder()
                            .id(res.getLong("id"))
                            .uuid(res.getObject("cod", UUID.class))
                            .myId(res.getLong("MyID"))
                            .name(res.getString("nombre"))
                            .modelo(Modelo.valueOf(res.getString("modelo")))
                            .precio(res.getDouble("precio"))
                            .fecha_lanzamiento(res.getDate("fecha").toLocalDate())
                            .build());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return lista;
        });
    }

    @Override
    public CompletableFuture<List<Funko>> findByNombre(String nombre) {
        return CompletableFuture.supplyAsync(()->{
            var lista = new ArrayList<Funko>();
            String query = "SELECT * FROM FUNKOS WHERE nombre LIKE ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Buscando todos los funkos por nombre que contenga: " + nombre);
                // Vamos a usar Like para buscar por nombre
                stmt.setString(1, "%" + nombre + "%");
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    // Creamos un alumno
                    Funko funko = Funko.builder()
                            .id(rs.getLong("id"))
                            .uuid(rs.getObject("cod", UUID.class))
                            .myId(rs.getLong("MyID"))
                            .name(rs.getString("nombre"))
                            .modelo(Modelo.valueOf(rs.getString("modelo")))
                            .precio(rs.getDouble("precio"))
                            .fecha_lanzamiento(rs.getDate("fecha").toLocalDate())
                            .build();
                    // Lo añadimos a la lista
                    lista.add(funko);
                }
            } catch (SQLException e) {
                logger.error("Error al buscar funkos por nombre ", e);
                throw new RuntimeException(e);

            }
            return lista;
        });
    }
}
