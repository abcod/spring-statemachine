package com.code.state.controller;

import cn.hutool.core.util.ReflectUtil;
import com.code.state.config.StateMachineFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.StateMachine;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FactoryController {

    private StateMachineFactory factory;

    public FactoryController(StateMachineFactory factory) {
        this.factory = factory;
    }

    @GetMapping("/factory")
    public String sendEvent(@RequestParam String event) {
        if (!StringUtils.hasLength(event)) {
            return "event为空";
        }

        log.info("-----------------------------------------------------------------");

        //创建并启动状态机
        StateMachine<String, String> stateMachine = factory.build();
        ObjectStateMachine<String, String> objectStateMachine = (ObjectStateMachine<String, String>) stateMachine;
        objectStateMachine.start();

        Object beanName = ReflectUtil.getFieldValue(objectStateMachine, "beanName");
        log.info("状态机的id：{}，事件：{}", objectStateMachine.getId(), event);
        if (objectStateMachine.getState() == null) {
            return "状态机未知状态";
        }
        Object fromState = objectStateMachine.getState().getId();

        //这里发送事件进行状态变换，sendEvent(String)方法过时
        boolean result = objectStateMachine.sendEvent(event);

        Object toState = objectStateMachine.getState().getId();
        String machineResult = String.format("%s: [%s]->[%s]", result, fromState, toState);
        log.info("状态机转换结果：{}", machineResult);

        //关闭状态机
        objectStateMachine.stop();

        return machineResult;
    }
}
