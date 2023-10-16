package dev.mohaek;

import dev.mohaek.models.Funko;
import dev.mohaek.repositories.funkosRepo.FunkoRepositoryImpl;
import dev.mohaek.services.CSVReader;
import dev.mohaek.services.FunkoService;
import dev.mohaek.services.FunkoServiceImpl;
import dev.mohaek.services.database.DatabaseManager;
import org.h2.engine.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Funko> funkos= new CSVReader().readFileFunko();
        FunkoService funkoService = FunkoServiceImpl.getInstance(FunkoRepositoryImpl.getInstance(DatabaseManager.getInstance()));

        funkoService.findAll().forEach(System.out::println);
    }
}