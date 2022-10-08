package com.code.state.config;

import com.code.state.listener.SimpleListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.Map;
import java.util.Objects;

/**
 * 分层状态机
 */
@Slf4j
@Configuration
@EnableStateMachine(name = HierarchicalStateMachineConfig.STATEMACHINE_NAME)
public class HierarchicalStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    public static final String STATEMACHINE_NAME = "hierarchicalStateMachine";

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        //系统启动自启
        config.withConfiguration()
                .autoStartup(true)
                .machineId(STATEMACHINE_NAME)
                .listener(new SimpleListenerImpl());
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {

        states.withStates()
                .initial("S0", fooAction())
                .state("S0")
                .and()
                .withStates()
                    .parent("S0")
                    .initial("S1")
                    .state("S1")
                    .and()
                    .withStates()
                        .parent("S1")
                        .initial("S11")
                        .state("S11")
                        .state("S12")
                        .and()
                .withStates()
                    .parent("S0")
                    .state("S2")
                    .and()
                        .withStates()
                        .parent("S2")
                        .initial("S21")
                        .state("S21")
                        .and()
                        .withStates()
                            .parent("S21")
                            .initial("S211")
                            .state("S211")
                            .state("S212");
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                    .source("S1").target("S1").event("A")
                    .guard(fooGuard(1))
                    .and()
                .withExternal()
                    .source("S1").target("S11").event("B")
                    .and()
                .withExternal()
                    .source("S21").target("S211").event("B")
                    .and()
                .withExternal()
                    .source("S1").target("S2").event("C")
                    .and()
                .withExternal()
                    .source("S2").target("S1").event("C")
                    .and()
                .withExternal()
                    .source("S1").target("S0").event("D")
                    .and()
                .withExternal()
                    .source("S211").target("S21").event("D")
                    .and()
                .withExternal()
                    .source("S0").target("S211").event("E")
                    .and()
                .withExternal()
                    .source("S1").target("S211").event("F")
                    .and()
                .withExternal()
                    .source("S2").target("S11").event("F")
                    .and()
                .withExternal()
                    .source("S11").target("S211").event("G")
                    .and()
                .withExternal()
                    .source("S211").target("S0").event("G")
                    .and()
                .withInternal()
                    .source("S0").event("H")
                    .guard(fooGuard(0))
                    .action(fooAction())
                    .and()
                .withInternal()
                    .source("S2").event("H")
                    .guard(fooGuard(1))
                    .action(fooAction())
                    .and()
                .withInternal()
                    .source("S1").event("H")
                    .and()
                .withExternal()
                    .source("S11").target("S12").event("I")
                    .and()
                .withExternal()
                    .source("S211").target("S212").event("I")
                    .and()
                .withExternal()
                    .source("S12").target("S212").event("I");
    }

    public Action<String, String> fooAction() {
        return new Action<String, String>() {
            @Override
            public void execute(StateContext<String, String> context) {
                Map<Object, Object> variables = context.getExtendedState().getVariables();
                Integer foo = context.getExtendedState().get("foo", Integer.class);
                if (foo == null) {
                    log.info("Init foo to 0");
                    variables.put("foo", 0);
                } else if (foo == 0) {
                    log.info("Switch foo to 1");
                    variables.put("foo", 1);
                } else if (foo == 1) {
                    log.info("Switch foo to 0");
                    variables.put("foo", 0);
                }
            }
        };
    }


    public Guard<String, String> fooGuard(int match) {
        return new Guard<String, String>() {

            @Override
            public boolean evaluate(StateContext<String, String> context) {
                Object foo = context.getExtendedState().getVariables().get("foo");
                log.info("guard判断：{} == {}", foo, match);
                return Objects.equals(foo, match);
            }
        };
    }


}
