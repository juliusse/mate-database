package info.seltenheim.mate.controllers;

import info.seltenheim.mate.service.MateService;
import info.seltenheim.services.filesystem.FileSystemService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MateController extends Controller {
    // private static final Form<SettingsFormData> settingsForm =
    // Form.form(SettingsFormData.class);

    @Autowired
    MateService mateService;

    @Autowired
    FileSystemService fileSystemService;

    public Result index() throws IOException {
        return ok(info.seltenheim.mate.views.html.layout.render(true));
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

    public Result getMeta() throws IOException {
        return ok(mateService.getMetaInformationAsJson());
    }

    public Result addMate() throws IOException {
        final int mateCount = request().body().asJson().get("count").asInt();
        mateService.addMate(mateCount);
        return ok();
    }

    public Result getLogEntries() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return ok(objectMapper.writeValueAsBytes(mateService.getAllLogEntries()));
    }

    public Result getImage(String name) throws IOException {
        final File image = fileSystemService.getImageAsFile(name);
        String hash = null;
        InputStream in = null;

        try {
            in = new FileInputStream(image);
            hash = DigestUtils.sha256Hex(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
        final String oldEtag = request().getHeader("If-None-Match");
        if (oldEtag != null && oldEtag.equals(hash)) {
            return status(NOT_MODIFIED);
        }

        response().setContentType("image/png");
        response().setHeader("Etag", hash);
        return ok(image);
    }

    public Result setCurrentBottlePrice() throws IOException {
        final double newPricePerBottle;
        try {
            newPricePerBottle = request().body().asJson().findValue("newBottlePrice").asDouble();
        } catch (Exception e) {
            return status(BAD_REQUEST);
        }

        mateService.setCurrentBottlePrice(newPricePerBottle);
        return ok();
    }
}
