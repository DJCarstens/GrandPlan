package com.grandplan.server;

import com.grandplan.server.services.ApiLoginService;
import com.grandplan.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  //prevents conflict with client mapping (a.k.a. grandPlanController)
public class ServerController {

    @Autowired
    private ApiLoginService apiLoginService;

    public ServerController(ApiLoginService apiLoginService) {
        this.apiLoginService = apiLoginService;
    }

    @GetMapping("/getUser")
    public User getUser() {
        return new User().builder()
                .firstName("Homey")
                .email("Homey@home.ru")
                .lastName("McHome")
                .build();
    }

    @PostMapping("/validateLogin")
    public ResponseEntity<User> validate(@RequestBody User user){
        if(user!=null && apiLoginService.validateUserCredentials(user)!=null)
            return ResponseEntity.ok(user);
        else
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("x-error-code", "Username and password combination does not match");
            return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_FOUND);
        }
    }
}
