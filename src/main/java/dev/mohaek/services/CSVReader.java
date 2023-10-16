package dev.mohaek.services;



import dev.mohaek.models.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class CSVReader {
    public ArrayList<Funko> readFileFunko() {
        IdGenerator myIdgenerator = new IdGenerator();
        String ruta = Paths.get("").toAbsolutePath()+ File.separator + "data" + File.separator + "funkos.csv";
        ArrayList<Funko> funks = new ArrayList<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try(com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))){
            String[] linea;
            reader.readNext();
            while ((linea = reader.readNext()) != null){
                Funko funk = new Funko();
                funk.setUuid(UUID.fromString(linea[0].length()>36?linea[0].substring(0,35):linea[0]));
                funk.setMyId(myIdgenerator.generateId());
                funk.setName(linea[1]);
                funk.setModelo(Modelo.valueOf(linea[2]));
                funk.setPrecio(Double.parseDouble(linea[3]));
                funk.setFecha_lanzamiento(LocalDate.parse(linea[4],formato));
                funks.add(funk);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return funks;
    }


    public static void main(String[] args) {
        CSVReader rs = new CSVReader();
        rs.readFileFunko();
        rs.readFileFunko().forEach(System.out::println);

    }
}