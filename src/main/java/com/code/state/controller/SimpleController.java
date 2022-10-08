package com.code.state.controller;

import cn.hutool.core.util.ReflectUtil;
import com.code.state.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class SimpleController {

    @Resource(name = StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE)
    private StateMachine<String, String> stateMachine;


    @GetMapping("/simple")
    public Result sendEvent(@RequestParam String event) {
        log.info("-----------------------------------------------------------------");

        ObjectStateMachine<?, ?> objStateMachine = (ObjectStateMachine<?, ?>) stateMachine;
        Object beanName = ReflectUtil.getFieldValue(objStateMachine, "beanName");
        log.info("状态机的beanName：{}，事件：{}", beanName, event);
        if (stateMachine.getState() == null) {
            return Result.from(false, "状态机未知状态");
        }
        Object fromState = stateMachine.getState().getId();

        //这里发送事件进行状态变换
        boolean success = stateMachine.sendEvent(event);

        Object toState = stateMachine.getState().getId();
        String machineResult = String.format("[%s]->[%s]", fromState, toState);
        log.info("状态机转换结果：{} {}", success, machineResult);

        return Result.from(success, machineResult);
    }
}
