package css.module.tryRedis;

import css.common.vo.ResultVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 通过整合redis练习场景配置
 * @Author: CSS
 * @Date: 2023/12/12 14:53
 */


@Api(tags = "测试redis")
@RestController("/tryRedis")
@Slf4j
public class TryRedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/incr")
    public ResultVo<String> incr(){
        Long num = stringRedisTemplate.opsForValue().increment("access-num");
        for (int i = 0; i < 1000; i++) {
            log.debug("debug日志执行...");
            log.info("info日志执行...");
            log.warn("warn日志执行...");
            log.error("error日志执行...");
        }
        return new ResultVo<>(true,"访问次数："+num);
    }

}
