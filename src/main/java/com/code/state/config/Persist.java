package com.code.state.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.util.StringUtils;

@Slf4j
@Configuration
public class Persist {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Bean
    public StateMachinePersister<String, String, String> stateMachinePersist() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<String, String, String>() {

            @Override
            public void write(StateMachineContext<String, String> context, String contextObj) {
                String state = context.getState();

                log.info("redis保存：状态机id={}，状态={}", contextObj, state);

                redisTemplate.opsForValue().set("statemachine:" + contextObj, state);
            }

            @Override
            public StateMachineContext<String, String> read(String contextObj) {
                String state = redisTemplate.opsForValue().get("statemachine:" + contextObj);
                log.info("redis查询：状态机id={}，状态={}", contextObj, state);

                //初始化
                if (!StringUtils.hasLength(state)) {
                    state = SimpleStateMachineConfig.STATE_INITIAL;
                }

                return new DefaultStateMachineContext<>(state, null, null,
                        null, null, contextObj);
            }
        });
    }


}
