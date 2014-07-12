package info.seltenheim.mate.controllers;

import info.seltenheim.mate.service.MateJunky;
import info.seltenheim.mate.service.MateService;
import info.seltenheim.services.filesystem.FileSystemService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

@Component
public class MateController extends Controller {
    // private static final Form<SettingsFormData> settingsForm =
    // Form.form(SettingsFormData.class);

    @Autowired
    MateService mateService;

    @Autowired
    FileSystemService fileSystemService;

    public Result index() throws IOException {
        final List<MateJunky> junkies = mateService.findAllJunkies();
        final int totalCount = mateService.getTotalBottleCount();
        return ok(info.seltenheim.mate.views.html.index.render(junkies, totalCount));
    }

    public Result showSettings() throws IOException {
        return ok(info.seltenheim.mate.views.html.settings.render());
    }

    public Result processSettings() throws IOException {
        final MultipartFormData formData = request().body().asMultipartFormData();
        final FilePart filePart1 = formData.getFile("1_image");
        final FilePart filePart2 = formData.getFile("2_image");
        final FilePart filePart3 = formData.getFile("3_image");

        if (filePart1 != null) {
            final File file1 = filePart1.getFile();
            final String extension = FilenameUtils.getExtension(file1.getName());
            fileSystemService.saveImage(FileUtils.readFileToByteArray(file1), "image1" + extension);
        }

        if (filePart2 != null) {
            final File file2 = filePart2.getFile();
            final String extension = FilenameUtils.getExtension(file2.getName());
            fileSystemService.saveImage(FileUtils.readFileToByteArray(file2), "image2" + extension);
        }

        if (filePart3 != null) {
            final File file3 = filePart3.getFile();
            final String extension = FilenameUtils.getExtension(file3.getName());
            fileSystemService.saveImage(FileUtils.readFileToByteArray(file3), "image3" + extension);
        }

        return redirect(routes.MateController.index());
    }

    public Result getImage(String name) throws FileNotFoundException {
        return ok(fileSystemService.getImageAsFile(name));
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
        final double amountMoney = Double.parseDouble(form.get("amount")[0]);
        final double pricePerBottle = Double.parseDouble(form.get("pricePerBottle")[0]);

        // validate fields
        if (username == null || username.isEmpty()) {
            flash("errorMessage", "No user selected");
            return redirect(routes.MateController.index());
        }

        // money is getting cut
        // there are no 'half' bottles or so
        final int bottles = (int) Math.floor(amountMoney / pricePerBottle);

        mateService.addRemainingBottles(username, bottles);

        return redirect(routes.MateController.index());
    }

    public Result countMate() throws IOException {
        // TODO more nice :)
        final Map<String, String[]> form = request().body().asFormUrlEncoded();
        final String username = form.get("username")[0];

        mateService.countMate(username);

        return ok(info.seltenheim.mate.views.html.userRow.render(mateService.findJunkyByName(username)));
    }
}
