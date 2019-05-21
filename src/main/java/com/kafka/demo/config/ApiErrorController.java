/**
 * 
 */
package com.kafka.demo.config;

import com.gemantic.springcloud.model.ResponseMessage;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@ApiIgnore
@RestController
public class ApiErrorController implements ErrorController {
    
    private static final String ERROR_PATH = "/error";
    
    @RequestMapping(value = ERROR_PATH)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseMessage error() {
        return ResponseMessage.error(404, -4041, null);
    }
    
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    
}
