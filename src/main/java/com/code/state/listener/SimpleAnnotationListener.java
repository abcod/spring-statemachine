package com.code.state.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@WithStateMachine
public class SimpleAnnotationListener {

    @OnTransitionStart
    public void transitionStart(Message<?> msg) {
        log.info("【注解监听】【transitionStart】：{}，没有source，没有target", msg.getPayload());
    }

    @OnTransitionStart(source = "A")
    public void transitionStartA(Message<?> msg) {
        log.info("【注解监听】【transitionStart】：{}，有source，没有target", msg.getPayload());
    }

    @OnTransitionStart(source = "A", target = "B")
    public void transitionStartAB(Message<?> msg) {
        log.info("【注解监听】【transitionStart】：{}，有source，有target", msg.getPayload());
    }

    @OnTransitionStart(target = "B")
    public void transitionStartB(Message<?> msg) {
        log.info("【注解监听】【transitionStart】：{}，没有source，有target", msg.getPayload());
    }

    @OnTransition
    public void onTransition(Message<?> msg) {
        log.info("【注解监听】【onTransition】：{}", msg.getPayload());
    }

    @OnStateExit
    public void onStateExit(Message<?> msg) {
        log.info("【注解监听】【onStateExit】：{}", msg.getPayload());
    }

    @OnStateEntry
    public void onStateEntry(Message<?> msg) {
        log.info("【注解监听】【onStateEntry】：{}", msg.getPayload());
    }

    @OnStateChanged
    public void onStateChanged(Message<?> msg) {
        log.info("【注解监听】【onStateChanged】：{}", msg.getPayload());
    }

    @OnTransitionEnd
    public void onTransitionEnd(Message<?> msg) {
        log.info("【注解监听】【onTransitionEnd】：{}", msg.getPayload());
    }

    @OnStateMachineStart
    public void onStart(Message<?> msg) {
        log.info("【注解监听】状态机启动：{}", msg);
    }

    @OnStateMachineStop
    public void onStop(Message<?> msg) {
        log.info("【注解监听】状态机停止：{}", msg);
    }

    @OnStateMachineError
    public void onError(Message<?> msg) {
        log.info("【注解监听】状态机错误：{}", msg);
    }

    @OnEventNotAccepted
    public void onEventNotAccepted(Message<?> msg) {
        log.info("【注解监听】事件错误：{}", msg);
    }

}
