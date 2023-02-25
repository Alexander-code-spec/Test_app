package ru.morozov.test.Test_app.storage;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.morozov.test.Test_app.exception.FileLoadException;
import ru.morozov.test.Test_app.model.TwoIp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

@Component
@Slf4j
public class TwoApiSotrage{
    public void save(TwoIp ipInfo){
        Optional<TwoIp> inf = Optional.ofNullable(ipInfo);
        Gson gson = new Gson();

        if (inf.isPresent()){
            try {
                deleteDirectory(new File("TwoIp"));
            } catch (IOException e) {
                throw new RuntimeException("Нет такой дирректории");
            }
            boolean fdir = new File("TwoIp").mkdir();
            if(fdir) {
                try (FileWriter writer = new FileWriter("TwoIp/" + Paths.get(inf.get().getIp() + ".json").toFile())) {
                    log.info("Запись данных объекта " + inf.get().getIp());
                    gson.toJson(inf, writer);
                } catch (IOException e) {
                    throw new FileLoadException(e.getMessage());
                }
            } else {
                log.error("Дирректория не удалось создать");
            }
        } else {
            log.info("Список данных с ресурса twoIp пуст");
        }
    }
}
