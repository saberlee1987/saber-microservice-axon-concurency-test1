package com.saber.rmicroservice.axon.concurency.test1.config;

import com.saber.rmicroservice.axon.concurency.test1.user.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.repository.GenericJpaRepository;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.axonframework.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

@Configuration
@Slf4j
public class AppConfig {

    @PersistenceContext
    private EntityManager entityManager;

    private final PlatformTransactionManager transactionManager;

    public AppConfig(@Qualifier(value = "transactionManager") PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Bean
    public XStreamSerializer xStreamSerializer(){
        return new XStreamSerializer();
    }

    @Bean
    public SimpleCommandBus commandBus(){
        SimpleCommandBus simpleCommandBus = new SimpleCommandBus();
        simpleCommandBus.setDispatchInterceptors(Collections.singletonList(new BeanValidationInterceptor()));
        SpringTransactionManager transactionManager =new SpringTransactionManager(this.transactionManager);
        simpleCommandBus.setTransactionManager(transactionManager);
        return simpleCommandBus;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor(){
        AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor =
                new AnnotationCommandHandlerBeanPostProcessor();
        annotationCommandHandlerBeanPostProcessor.setCommandBus(this.commandBus());
        return annotationCommandHandlerBeanPostProcessor;
    }

    @Bean(name = "commandGateway")
    public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean(){
        CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean = new CommandGatewayFactoryBean<>();
        commandGatewayFactoryBean.setCommandBus(commandBus());
        return commandGatewayFactoryBean;
    }

    @Bean
    public SimpleEntityManagerProvider simpleEntityManagerProvider(){
        return new SimpleEntityManagerProvider(entityManager);

    }

    @Bean(name = "userRepository")
    public GenericJpaRepository<UserEntity> userEntityGenericJpaRepository(){
        SimpleEntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
        return new GenericJpaRepository<>(entityManagerProvider,UserEntity.class);
    }
}
