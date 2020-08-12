package cc.mrbird.febs.auth.runner;

import cc.mrbird.febs.auth.service.impl.RedisClientDetailsService;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author MrBird
 */
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;
    private final RedisClientDetailsService redisClientDetailsService;


    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {

            // 将 oauth_client_details全表刷入 redis
            redisClientDetailsService.loadAllClientToCache();

            FebsUtil.printSystemUpBanner(environment);
        }
    }
}
