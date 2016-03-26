package info.seltenheim.mate.controllers;

import info.seltenheim.mate.service.MateJunky;
import info.seltenheim.mate.service.MateService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class JunkyController extends Controller {
    @Autowired
    MateService mateService;

    public Result getAllJunkies() throws IOException {
        final List<MateJunky> junkies = mateService.findAllJunkies();

        return ok(Json.toJson(junkies));
    }

    public Result getJunky(int id) throws IOException {
        final MateJunky junky = mateService.findJunkyById(id);

        return ok(Json.toJson(junky));
    }
    
    public Result getJunkyDrinkMate(int id) throws IOException {
    	final MateJunky junky = mateService.findJunkyById(id);
    	
    	mateService.drinkMate(junky);
    	
        return ok(Json.toJson(junky));
    }

    public Result createJunky() throws IOException {
    	final MateJunky newJunky = Json.fromJson(request().body().asJson(), MateJunky.class);
        final int id = mateService.addJunky(newJunky.getName()).getId();
        newJunky.setId(id);
        return ok(Json.toJson(newJunky));
    }

    public Result updateJunky(int id) throws IOException {
        final MateJunky junky = Json.fromJson(request().body().asJson(), MateJunky.class);
        mateService.updateJunky(junky);
        
        return ok(Json.toJson(junky));
    }
}
