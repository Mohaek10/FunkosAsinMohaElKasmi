package dev.mohaek;

import dev.mohaek.models.Funko;
import dev.mohaek.services.CSVReader;
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
    public static void main(String[] args) {
        ArrayList<Funko> funkos= new CSVReader().readFileFunko();
        DatabaseManager db = DatabaseManager.getInstance();
    }
}