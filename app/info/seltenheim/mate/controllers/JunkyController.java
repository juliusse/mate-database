package info.seltenheim.mate.controllers;

import info.seltenheim.mate.service.MateJunky;
import info.seltenheim.mate.service.MateService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JunkyController extends Controller {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MateService mateService;

    public Result getAllJunkies() throws IOException {
        final List<MateJunky> junkies = mateService.findAllJunkies();

        return ok(objectMapper.writeValueAsBytes(junkies));
    }

    public Result getJunky(int id) throws IOException {
        final MateJunky junky = mateService.findJunkyById(id);

        return ok(objectMapper.writeValueAsBytes(junky));
    }

    public Result createJunky() throws IOException {
        final MateJunky newJunky = objectMapper.readValue(request().body().asText(), MateJunky.class);
        final int id = mateService.addJunky(newJunky.getName()).getId();

        return ok(id + "");
    }

    public Result updateJunky(int id) throws IOException {
        final MateJunky junky = objectMapper.readValue(request().body().asJson().toString(), MateJunky.class);

        mateService.updateJunky(junky);
        return ok(objectMapper.writeValueAsBytes(junky));
    }
}
