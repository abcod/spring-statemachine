package com.code.state.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;

/**
 * 持久化状态机
 */
@Slf4j
@Configuration
@EnableStateMachine(name = PersistStateMachineConfig.STATEMACHINE_NAME)
public class PersistStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    public static final String STATEMACHINE_NAME = "persistStateMachine";

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        //系统启动自启
        config.withConfiguration()
                .autoStartup(true)
                .machineId(STATEMACHINE_NAME);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        for (String fromState : SimpleStateMachineConfig.STATE_LIST) {
            for (String toState : SimpleStateMachineConfig.STATE_LIST) {
                //外部事件
                transitions = transitions.withExternal()
                        .source(fromState)
                        .target(toState)
                        .event(fromState + "-" + toState)
                        .and();
            }
        }
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {

        StateConfigurer<String, String> configurer = states.withStates()
                //配置起始状态
                .initial(SimpleStateMachineConfig.STATE_INITIAL);

        //所有状态
        for (String s : SimpleStateMachineConfig.STATE_LIST) {
            configurer.state(s);
        }
    }
}
