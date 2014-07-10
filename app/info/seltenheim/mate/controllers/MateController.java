package info.seltenheim.mate.controllers;

import info.seltenheim.mate.service.MateJunky;
import info.seltenheim.mate.service.MateService;
import info.seltenheim.mate.service.formdata.SettingsFormData;
import info.seltenheim.mate.views.html.userRow;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class MateController extends Controller {
    private static final Form<SettingsFormData> settingsForm = Form.form(SettingsFormData.class);

    @Autowired
    MateService mateService;

    public Result index() throws IOException {
        final List<MateJunky> junkies = mateService.findAllJunkies();
        final int totalCount = mateService.getTotalBottleCount();
        return ok(info.seltenheim.mate.views.html.index.render(junkies, totalCount));
    }
    
    public Result showSettings() throws IOException {
        return ok(info.seltenheim.mate.views.html.settings.render(settingsForm));
    }
    
    public Result processSettings() throws IOException {
        return TODO;
    }

    public Result addJunky() throws IOException {
        // TODO more nice :)
        final String username = request().body().asFormUrlEncoded().get("username")[0];

        // TODO error handling
        mateService.addJunky(username);

        return redirect(routes.MateController.index());
    }

    public Result addCredit() throws IOException {
        // TODO more nice :)
        final Map<String, String[]> form = request().body().asFormUrlEncoded();
        final String username = form.get("username")[0];
        final double credit = Double.parseDouble(form.get("amount")[0]);

        // validate fields
        if (username == null || username.isEmpty()) {
            flash("errorMessage", "No user selected");
            return redirect(routes.MateController.index());
        }

        mateService.addCredit(username, credit);

        return redirect(routes.MateController.index());
    }

    public Result countMate() throws IOException {
        // TODO more nice :)
        final Map<String, String[]> form = request().body().asFormUrlEncoded();
        final String username = form.get("username")[0];

        mateService.countMate(username);

        return ok(userRow.render(mateService.findJunkyByName(username)));
    }
}
