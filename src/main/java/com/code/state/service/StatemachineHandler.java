package com.code.state.service;

import cn.hutool.core.util.ReflectUtil;
import com.code.state.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.StateMachine;

@Slf4j
public class StatemachineHandler {

    public static Result sendEvent(StateMachine<String, String> stateMachine, String event) {
        log.info("-----------------------------------------------------------------");

        event = event.toUpperCase();

        ObjectStateMachine<?, ?> objStateMachine = (ObjectStateMachine<?, ?>) stateMachine;
        Object beanName = ReflectUtil.getFieldValue(objStateMachine, "beanName");
        log.info("状态机的beanName：{}，事件：{}", beanName, event);
        if (stateMachine.getState() == null) {
            log.error("状态机未知状态");
            return Result.from(false, "状态机未知状态");
        }

        //转换前的状态
        Object fromState = stateMachine.getState().getIds();

        //这里发送事件进行状态变换
        boolean success = stateMachine.sendEvent(event);

        //转换后的状态
        Object toState = stateMachine.getState().getIds();

        String machineResult = String.format("[%s]->[%s]", fromState, toState);
        log.info("状态机转换结果：{} {}", success, machineResult);
        return Result.from(success, machineResult);
    }
}
