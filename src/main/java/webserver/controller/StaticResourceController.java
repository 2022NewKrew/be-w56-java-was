package webserver.controller;

import common.controller.AbstractController;
import common.dto.ControllerRequest;
import common.dto.ControllerResponse;
import common.controller.ControllerType;
import lombok.extern.slf4j.Slf4j;
import webserver.dto.response.HttpStatus;

@Slf4j
public class StaticResourceController extends AbstractController {

    @Override
    public ControllerResponse doService(ControllerRequest request) {
        log.debug("[" + StaticResourceController.log.getName() + "] doService()");
        return ControllerResponse.builder()
                .httpStatus(HttpStatus.OK)
                .header(request.getHeader())
                .redirectTo(ControllerType.STATIC.getUrl() + request.getUrl())
                .build();
    }
}
