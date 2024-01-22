package css.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: 自定义切面类，在方法执行前打印被调用的方法信息、执行后打印调用返回值
 * @Author: CSS
 * @Date: 2023/12/7 22:46
 */



@Aspect//切面
@Component
public class MyAspect {

    private static Logger log = LoggerFactory.getLogger(MyAspect.class);

    //切点
    @Pointcut("execution(public * css.module..*.*Controller.*(..))")
    public void myPointcut(){

    }

    //环绕通知
    @Around(value = "myPointcut()")
    public Object test(ProceedingJoinPoint pjp) throws Throwable {
        //类名
        Class<?> clazz = pjp.getTarget().getClass();
        String clazzName = clazz.getName();

        //方法名
        String methodName = pjp.getSignature().getName();

        ObjectMapper mapper = new ObjectMapper();

        //参数数组
        Object[] args = pjp.getArgs();

        log.info("类名：{}；方法名：{}；参数：",clazzName,methodName,mapper.writeValueAsString(args));

        //执行被调用方法
        Object obj = pjp.proceed();

        //log.info("执行结果为：{}",mapper.writeValueAsString(obj));

        return obj;
    }
}
