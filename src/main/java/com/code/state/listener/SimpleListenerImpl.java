package com.code.state.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

@Slf4j
public class SimpleListenerImpl implements StateMachineListener<String, String> {
    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        log.info("【接口实现类监听】stateChanged，from：{}，to：{}", from, to);
    }

    @Override
    public void stateEntered(State<String, String> state) {
        log.info("【接口实现类监听】stateEntered，state：{}", state);
    }

    @Override
    public void stateExited(State<String, String> state) {
        log.info("【接口实现类监听】stateExited，state：{}", state);
    }

    @Override
    public void eventNotAccepted(Message<String> event) {
        log.info("【接口实现类监听】eventNotAccepted，event：{}", event);
    }

    @Override
    public void transition(Transition<String, String> transition) {
        log.info("【接口实现类监听】transition，transition：{}", transition);
    }

    @Override
    public void transitionStarted(Transition<String, String> transition) {
        log.info("【接口实现类监听】transitionStarted，transition：{}", transition);
    }

    @Override
    public void transitionEnded(Transition<String, String> transition) {
        log.info("【接口实现类监听】transitionEnded，transition：{}", transition);
    }

    @Override
    public void stateMachineStarted(StateMachine<String, String> stateMachine) {
        log.info("【接口实现类监听】stateMachineStarted，stateMachine：{}", stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<String, String> stateMachine) {
        log.info("【接口实现类监听】stateMachineStopped，stateMachine：{}", stateMachine);
    }

    @Override
    public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
        log.error("【接口实现类监听】stateMachineError，Exception：" + exception.getMessage(), exception);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        log.info("【接口实现类监听】extendedStateChanged，key：{}，value：{}", key, value);
    }

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        log.info("【接口实现类监听】stateContext：{}", stateContext.getStage());
    }
}
