package info.seltenheim.mate.controllers;

import controllers.Assets.Asset;
import play.api.mvc.Action;
import play.api.mvc.AnyContent;

public class Assets {
    public static Action<AnyContent> versioned(String path, String file) {
        return controllers.Assets.versioned(path, new Asset(file));
    }
}