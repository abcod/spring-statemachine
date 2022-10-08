package com.code.state.command;

import com.code.state.config.GuardStateMachineConfig;
import com.code.state.config.HierarchicalStateMachineConfig;
import com.code.state.config.PersistStateMachineConfig;
import com.code.state.config.StateMachineFactory;
import com.code.state.dto.Result;
import com.code.state.service.StatemachineHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.persist.StateMachinePersister;

@Slf4j
@ShellComponent
public class StatemachineCommand {

    @Autowired
    @Qualifier(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE)
    private StateMachine<String, String> simpleStateMachine;

    @Autowired
    @Qualifier(PersistStateMachineConfig.STATEMACHINE_NAME)
    private StateMachine<String, String> persistStateMachine;

    @Autowired
    @Qualifier(GuardStateMachineConfig.STATEMACHINE_NAME)
    private StateMachine<String, String> guardStateMachine;

    @Autowired
    @Qualifier(HierarchicalStateMachineConfig.STATEMACHINE_NAME)
    private StateMachine<String, String> hieStateMachine;

    @Autowired
    @Qualifier("stateMachinePersist")
    private StateMachinePersister<String, String, String> stateMachinePersist;

    @Autowired
    private StateMachineFactory factory;

    @ShellMethod(key = "simple", value = "简易状态机")
    public void simple(String event) {
        StatemachineHandler.sendEvent(simpleStateMachine, event);
    }

    @ShellMethod(key = "persist", value = "持久化状态机")
    public void persist(String id, String event) throws Exception {
        //恢复状态机的状态
        stateMachinePersist.restore(persistStateMachine, id);

        Result result = StatemachineHandler.sendEvent(persistStateMachine, event);

        //如果状态进行了变化，进行保存
        if (result.isSuccess()) {
            stateMachinePersist.persist(persistStateMachine, id);
        }
    }

    @ShellMethod(key = "guard", value = "状态机拦截")
    public void guard(String event) {
        StatemachineHandler.sendEvent(simpleStateMachine, event);
    }

    @ShellMethod(key = "factory", value = "工厂状态机")
    public void factory(String event) {
        //创建并启动状态机
        StateMachine<String, String> stateMachine = factory.build();
        stateMachine.start();

        StatemachineHandler.sendEvent(stateMachine, event);

        //关闭状态机
        stateMachine.stop();
    }
}
