package hust.iwsrc.hellospringboot;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** 
 * @author: Yiwen Liang[ywhuak@163.com]
 * @date: Created on 18:44 2018/5/4 
 * @Description:  
 */
@Aspect  
@Component  
public class MyAspectJ {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MyAspectJ.class); 
	
	@Pointcut("execution(* hust.iwsrc.hellospringboot.HelloRestController.*(..))")
	private void webLog(){}
	
    @Before("webLog()")  
    public void deBefore(JoinPoint joinPoint) throws Throwable {  
        // 接收到请求，记录请求内容  
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();  
        HttpServletRequest request = attributes.getRequest();  
        // 记录下请求内容  
        LOGGER.info("URL : " + request.getRequestURL().toString());  
        LOGGER.info("HTTP_METHOD : " + request.getMethod());  
        LOGGER.info("IP : " + request.getRemoteAddr());  
        LOGGER.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());  
        LOGGER.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));  
  
    }  
  
    @AfterReturning(returning = "ret", pointcut = "webLog()")  
    public void doAfterReturning(Object ret) throws Throwable {  
        // 处理完请求，返回内容  
    	LOGGER.info("方法的返回值 : " + ret);  
    }  
  
    //后置异常通知  
    @AfterThrowing("webLog()")  
    public void throwss(JoinPoint jp){  
    	LOGGER.info("方法异常时执行.....");  
    }  
  
    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行  
    @After("webLog()")  
    public void after(JoinPoint jp){  
    	LOGGER.info("方法最后执行.....");  
    }  
  
    //环绕通知,环绕增强，相当于MethodInterceptor  
    @Around("webLog()")  
    public Object arround(ProceedingJoinPoint pjp) {  
    	LOGGER.info("方法环绕start.....");  
        try {  
            Object o =  pjp.proceed();  
            LOGGER.info("方法环绕proceed，结果是 :" + o);  
            return o;  
        } catch (Throwable e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  

}
