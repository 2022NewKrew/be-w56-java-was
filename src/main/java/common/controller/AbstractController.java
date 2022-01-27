package common.controller;

import common.dto.ControllerRequest;
import common.dto.ControllerResponse;

public abstract class AbstractController {
    public abstract ControllerResponse doService(ControllerRequest controllerRequest);
}
