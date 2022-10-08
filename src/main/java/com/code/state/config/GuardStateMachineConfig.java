package com.code.state.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;

/**
 * 状态机
 */
@Slf4j
@Configuration
@EnableStateMachine(name = GuardStateMachineConfig.STATEMACHINE_NAME)
public class GuardStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    public static final String STATEMACHINE_NAME = "guardStateMachine";
    private int times = 0;

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
                        .guard(guard())
                        .action(action(), errorAction())
                        .and();
            }
        }
    }

    @Bean
    public Guard<String, String> guard() {
        return new Guard<String, String>() {

            @Override
            public boolean evaluate(StateContext<String, String> context) {
                return ++times % 2 == 0;
            }
        };
    }

    @Bean
    public Action<String, String> action() {
        return new Action<String, String>() {

            @Override
            public void execute(StateContext<String, String> context) {
                String event = context.getEvent();
                log.info("触发action方法，event: " + event);
            }
        };
    }

    @Bean
    public Action<String, String> errorAction() {
        return new Action<String, String>() {

            @Override
            public void execute(StateContext<String, String> context) {
                String event = context.getEvent();
                log.error("触发action错误方法，event: " + event);
            }
        };
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
