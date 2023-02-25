package ru.morozov.test.Test_app.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.morozov.test.Test_app.service.RequestService;

import java.io.FileNotFoundException;


@RestController
@RequestMapping("/agify-io")
@Slf4j
@RequiredArgsConstructor
public class AgifyController {
    private final RequestService requestService;

    @GetMapping
    @ResponseBody
    public void getDataFromAgify() throws FileNotFoundException {
        requestService.saveAgify(requestService.getAgifyInfo().join());
    }
}
