package ru.morozov.test.Test_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.morozov.test.Test_app.service.RequestService;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/two-ip-ru")
@Slf4j
@RequiredArgsConstructor
public class TwoIpController {

    private final RequestService requestService;

    @GetMapping
    @ResponseBody
    public void getDataFromAgify(@RequestParam(defaultValue = "", required = false) String ip) throws FileNotFoundException {
        requestService.saveTwoIp(requestService.getTwoIpInfo(ip).join());
    }
}
