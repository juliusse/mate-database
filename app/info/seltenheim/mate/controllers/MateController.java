package info.seltenheim.mate.controllers;

import info.seltenheim.mate.service.MateJunky;
import info.seltenheim.mate.service.MateService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.mvc.Controller;
import play.mvc.Result;

@Component
public class MateController extends Controller {

    @Autowired
    MateService mateService;

    public Result index() throws IOException {
        final List<MateJunky> junkies = mateService.findAllJunkies();
        return ok(info.seltenheim.mate.views.html.index.render(junkies));
    }

    public Result addJunky() throws IOException {
        // TODO more nice :)
        final String username = request().body().asFormUrlEncoded().get("username")[0];

        // TODO error handling
        mateService.addJunky(username);

        return redirect(routes.MateController.index());
    }

    public Result addRemaining() throws IOException {
        // TODO more nice :)
        final Map<String, String[]> form = request().body().asFormUrlEncoded();
        final String username = form.get("username")[0];
        final int amountMoney = Integer.parseInt(form.get("amount")[0]);
        final int pricePerBottle = Integer.parseInt(form.get("pricePerBottle")[0]);

        // money is getting cut
        // there are no 'half' bottles or so
        final int bottles = amountMoney / pricePerBottle;

        mateService.addRemainingBottles(username, bottles);

        return redirect(routes.MateController.index());
    }
}
