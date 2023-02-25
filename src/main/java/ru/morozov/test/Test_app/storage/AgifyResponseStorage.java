package ru.morozov.test.Test_app.storage;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.morozov.test.Test_app.exception.FileLoadException;
import ru.morozov.test.Test_app.model.Agify;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

@Component
@Slf4j
public class AgifyResponseStorage{

    public List<String> loadFromFile(String name) throws FileLoadException {
        File file = new File(Paths.get(name).toUri());

        List<String> names = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getName()))) {
            while (fileReader.ready()){
                String line = fileReader.readLine();
                if(!line.isEmpty()){
                    log.info("Загружено имя " + line);
                    names.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new FileLoadException("Ошибка при чтении из файла");
        }
        return names;
    }

    public void save(List<Agify> agifyList){
        Optional<List<Agify>> list = Optional.ofNullable(agifyList);
        Gson gson = new Gson();

        if (list.isPresent()){
            try {
                deleteDirectory(new File("Agify"));
            } catch (IOException e) {
                throw new RuntimeException("Нет такой дирректории");
            }
            boolean fdir = new File("Agify").mkdir();
            if(fdir){
                log.info("Дирректория создана, начинаю запись");
                for(Agify agify: list.get()){
                    try(FileWriter writer = new FileWriter("Agify/" + Paths.get(agify.getName() + ".json").toFile())) {
                        log.info("Запись данных объекта " + agify.getName());
                        gson.toJson(agify, writer);
                    } catch (IOException e) {
                        throw new FileLoadException(e.getMessage());
                    }
                }
            } else {
                log.error("Дирректория не удалось создать");
            }

        } else {
            log.info("Список данных с ресурса agify.io пуст");
        }
    }
}
