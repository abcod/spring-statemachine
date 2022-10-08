package com.code.state.controller;

import cn.hutool.core.util.ReflectUtil;
import com.code.state.config.PersistStateMachineConfig;
import com.code.state.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class PersistController {

    @Resource(name = PersistStateMachineConfig.STATEMACHINE_NAME)
    private StateMachine<String, String> stateMachine;

    @Resource(name = "stateMachinePersist")
    private StateMachinePersister<String, String, String> stateMachinePersist;

    @GetMapping("/persist")
    public Result sendEvent(@RequestParam String id, @RequestParam String event) throws Exception {
        log.info("-----------------------------------------------------------------");

        ObjectStateMachine<?, ?> objStateMachine = (ObjectStateMachine<?, ?>) stateMachine;
        Object beanName = ReflectUtil.getFieldValue(objStateMachine, "beanName");
        log.info("状态机的beanName：{}，事件：{}", beanName, event);

        //恢复状态机的状态
        stateMachinePersist.restore(stateMachine, id);

        //获取状态机当前的状态
        Object fromState = stateMachine.getState().getId();

        //这里发送事件进行状态转移
        boolean success = stateMachine.sendEvent(event);

        //状态机接收事件后的状态
        Object toState = stateMachine.getState().getId();
        String machineResult = String.format("[%s]->[%s]", fromState, toState);
        log.info("状态机转换结果：{} {}", success, machineResult);

        //如果状态进行了变化，进行保存
        if (success) {
            stateMachinePersist.persist(stateMachine, id);
        }

        return Result.from(success, machineResult);
    }
}
