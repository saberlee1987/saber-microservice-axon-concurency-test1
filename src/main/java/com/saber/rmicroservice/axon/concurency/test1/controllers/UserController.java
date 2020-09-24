package com.saber.rmicroservice.axon.concurency.test1.controllers;

import com.saber.rmicroservice.axon.concurency.test1.dto.UserDto;
import com.saber.rmicroservice.axon.concurency.test1.user.command.UserCreationCommand;
import com.saber.rmicroservice.axon.concurency.test1.user.command.UserModificationCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final CommandGateway commandGateway;

    public UserController(@Qualifier(value = "commandGateway") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public void createNewUser(@RequestBody UserDto userDto) {
        log.info("Start createNewUser ............");
        UserCreationCommand userCreationCommand = new UserCreationCommand();
        userCreationCommand.setId(userDto.getId());
        userCreationCommand.setAge(userDto.getAge());
        userCreationCommand.setUsername(userDto.getUsername());
        try {
            commandGateway.sendAndWait(userCreationCommand);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        log.info("End createNewUser ............");

    }

    @PutMapping
    public void handleUpdateUser(@RequestBody UserDto userDto){
        log.info("Start handleUpdateUser ...........................");
        UserModificationCommand userModificationCommand = new UserModificationCommand();
        userModificationCommand.setId(userDto.getId());
        userModificationCommand.setAge(userDto.getAge());
        userModificationCommand.setUsername(userDto.getUsername());
        try {
            commandGateway.sendAndWait(userModificationCommand);
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        log.info("End handleUpdateUser ...........................");
    }
}
