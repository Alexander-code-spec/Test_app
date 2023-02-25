package ru.morozov.test.Test_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.morozov.test.Test_app.model.Agify;
import ru.morozov.test.Test_app.model.TwoIp;
import ru.morozov.test.Test_app.storage.AgifyResponseStorage;
import ru.morozov.test.Test_app.storage.TwoApiSotrage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestService {
    private  final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.)" +
            "{3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private final RestTemplate restTemplate = new RestTemplate();
    private final AgifyResponseStorage agifyResponseStorage;
    private final TwoApiSotrage twoApiSotrage;
    private final String fileName;

    @Autowired
    public RequestService(AgifyResponseStorage agifyResponseStorage, TwoApiSotrage twoApiSotrage, Environment env){
        this.agifyResponseStorage = agifyResponseStorage;
        this.twoApiSotrage = twoApiSotrage;
        this.fileName = env.getProperty("file");
    }

    public void saveAgify(List<Agify> agifyList){
        agifyResponseStorage.save(agifyList);
    }

    public void saveTwoIp(TwoIp ipInfo){
        twoApiSotrage.save(ipInfo);
    }

    @Async
    public CompletableFuture<List<Agify>> getAgifyInfo() throws FileNotFoundException {
        List<String> names = agifyResponseStorage.loadFromFile(fileName);
        List<String> webPageLinks = new ArrayList<>();

        for(String name: names){
            String url = "https://api.agify.io/?name=" + name;
            webPageLinks.add(url);
        }

        List<CompletableFuture<Agify>> pageContentFutures = webPageLinks.stream()
                .map(this::downloadWebPage)
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                pageContentFutures.toArray(new CompletableFuture[0])
        );

        return allFutures.thenApply(
                v -> pageContentFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<TwoIp> getTwoIpInfo(String ip){
        String url = "https://api.2ip.me/geo.json?ip=";
        if(!ip.isEmpty() && PATTERN.matcher(ip).matches()){
            url = url + ip;
        }
        String finalUrl = url;
        return CompletableFuture.supplyAsync(() -> restTemplate.getForObject(finalUrl, TwoIp.class));
    }

    CompletableFuture<Agify> downloadWebPage(String pageLink) {
        return CompletableFuture.supplyAsync(() -> restTemplate.getForObject(pageLink, Agify.class));
    }

}
