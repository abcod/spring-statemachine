package com.code.state.config;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.stereotype.Component;

/**
 * 动态创建状态机
 */
@Slf4j
@Component
public class StateMachineFactory {

    private final BeanFactory beanFactory;

    public StateMachineFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public StateMachine<String, String> build() {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        String id = "stateMachine-" + RandomUtil.randomString(4);
        log.info("创建状态机，machineId：{}", id);

        try {

            builder.configureConfiguration()
                    .withConfiguration()
                    .machineId(id)
                    .beanFactory(beanFactory);

            //配置状态
            StateConfigurer<String, String> stateConfigurer = builder.configureStates()
                    .withStates()
                    .initial(SimpleStateMachineConfig.STATE_LIST.get(0));
            for (String s : SimpleStateMachineConfig.STATE_LIST) {
                stateConfigurer.state(s);
            }

            //配置状态转移
            StateMachineTransitionConfigurer<String, String> transitionConfigurer = builder.configureTransitions();
            for (String fromState : SimpleStateMachineConfig.STATE_LIST) {
                for (String toState : SimpleStateMachineConfig.STATE_LIST) {
                    //外部事件
                    transitionConfigurer = transitionConfigurer.withExternal()
                            .source(fromState)
                            .target(toState)
                            .event(fromState + "-" + toState)
                            .and();
                }
            }
        } catch (Exception e) {
            log.error("状态机创建失败：" + e.getMessage(), e);
        }

        return builder.build();
    }
}
