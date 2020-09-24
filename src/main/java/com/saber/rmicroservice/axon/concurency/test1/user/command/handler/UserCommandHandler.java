package com.saber.rmicroservice.axon.concurency.test1.user.command.handler;

import com.saber.rmicroservice.axon.concurency.test1.user.command.UserCreationCommand;
import com.saber.rmicroservice.axon.concurency.test1.user.command.UserModificationCommand;
import com.saber.rmicroservice.axon.concurency.test1.user.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCommandHandler {

    private final Repository<UserEntity> userEntityRepository;

    public UserCommandHandler(@Qualifier(value = "userRepository") Repository<UserEntity> userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @CommandHandler
    public void handleUserCreationCommand(UserCreationCommand userCreationCommand) {
        log.info("Start handleUserCreationCommand ...............");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userCreationCommand.getId());
        userEntity.setUsername(userCreationCommand.getUsername());
        userEntity.setAge(userCreationCommand.getAge());

        userEntityRepository.add(userEntity);

        log.info("New User Created : {} ", userEntity);
        log.debug("New User Created : {} ", userEntity);
        log.info("End handleUserCreationCommand ...................");
    }

    @CommandHandler
    public void handleUserModifyCommand(UserModificationCommand userModificationCommand) {

        log.info("Start handleUserModifyCommand ...................");
        UserEntity userEntity = this.userEntityRepository.load(userModificationCommand.getId());
        long versionInitial = userEntity.getVersion();
        log.debug("User Found : {} ; versionInitial: {}", userEntity, versionInitial);

        userEntity.setAge(userModificationCommand.getAge());
        userEntity.setUsername(userModificationCommand.getUsername());

        log.debug("User Modified to : {} ", userEntity);
        UserEntity userQueried = this.userEntityRepository.load(userModificationCommand.getId());
        long versionQueried = userQueried.getVersion();
        log.debug("User Queried : {} ; versionQueried : {} ", userQueried, versionQueried);

        long seconds = 20L;

        log.debug("Thread sleeping for {} seconds ", seconds);

        try {
            Thread.sleep(1000 * seconds);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        UserEntity userQueriedAgain = this.userEntityRepository.load(userModificationCommand.getId());
        long versionQueriedAgain = userQueriedAgain.getVersion();

        log.debug("User Queried Again : {} ; versionQueried : {} ",userQueriedAgain,versionQueriedAgain);

        log.info("End handleUserModifyCommand ...................");
    }
}
