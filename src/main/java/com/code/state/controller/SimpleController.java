package com.code.state.controller;

import com.code.state.config.GuardStateMachineConfig;
import com.code.state.config.HierarchicalStateMachineConfig;
import com.code.state.config.StateMachineFactory;
import com.code.state.dto.Result;
import com.code.state.service.StatemachineHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SimpleController {

    private final ApplicationContext applicationContext;

    public SimpleController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @GetMapping("/simple")
    public Result simple(@RequestParam String event) {
        StateMachine<String, String> stateMachine = applicationContext.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, StateMachine.class);
        return StatemachineHandler.sendEvent(stateMachine, event);
    }

    @GetMapping("/persist")
    public Result persist(@RequestParam String id, @RequestParam String event) throws Exception {
        StateMachine<String, String> stateMachine = applicationContext.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, StateMachine.class);
        StateMachinePersister<String, String, String> stateMachinePersister = applicationContext.getBean(StateMachinePersister.class);

        //恢复状态机的状态
        stateMachinePersister.restore(stateMachine, id);

        Result result = StatemachineHandler.sendEvent(stateMachine, event);

        //如果状态进行了变化，进行保存
        if (result.isSuccess()) {
            stateMachinePersister.persist(stateMachine, id);
        }

        return result;
    }

    @GetMapping("/guard")
    public Result guard(@RequestParam String event) {
        StateMachine<String, String> stateMachine = applicationContext.getBean(GuardStateMachineConfig.STATEMACHINE_NAME, StateMachine.class);
        return StatemachineHandler.sendEvent(stateMachine, event);
    }


    @GetMapping("/hierarchical")
    public Result hierarchical(@RequestParam String event) {
        StateMachine<String, String> stateMachine = applicationContext.getBean(HierarchicalStateMachineConfig.STATEMACHINE_NAME, StateMachine.class);
        return StatemachineHandler.sendEvent(stateMachine, event);
    }

    @GetMapping("/factory")
    public Result factory(@RequestParam String event) {
        StateMachineFactory factory = applicationContext.getBean(StateMachineFactory.class);

        //创建并启动状态机
        StateMachine<String, String> stateMachine = factory.build();
        stateMachine.start();

        Result result = StatemachineHandler.sendEvent(stateMachine, event);

        //关闭状态机
        stateMachine.stop();

        return result;
    }
}
