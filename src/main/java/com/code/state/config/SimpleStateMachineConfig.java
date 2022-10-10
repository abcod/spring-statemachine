package com.code.state.config;

import com.code.state.listener.SimpleListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * 简易状态机配置，默认状态机名称为stateMachine
 *
 * 如果使用枚举类作为状态和事件，继承EnumStateMachineConfigurerAdapter
 */
@Slf4j
@Configuration
@EnableStateMachine
public class SimpleStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    public static final List<String> STATE_LIST = Arrays.asList("A", "B", "C", "D", "E");

    public static final String STATE_INITIAL = "A";


    /**
     * 配置状态的状态，起始状态和最终状态，所有状态
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {

        StateConfigurer<String, String> configurer = states.withStates()
                //配置起始状态
                .initial(STATE_INITIAL)
                //终态可不配置，配置终态后无法再转为其他状态
                .end("E");

        //所有状态
        for (String s : STATE_LIST) {
            configurer.state(s);
        }
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        //系统启动自启
        config.withConfiguration()
                .autoStartup(true)
                .listener(new SimpleListenerImpl());
    }

    /**
     * 配置状态转换
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        for (String fromState : STATE_LIST) {
            for (String toState : STATE_LIST) {
                //外部事件
                transitions = transitions.withExternal()
                        .source(fromState)
                        .target(toState)
                        .event(fromState + "-" + toState)
                        .and();
            }
        }
    }

}
