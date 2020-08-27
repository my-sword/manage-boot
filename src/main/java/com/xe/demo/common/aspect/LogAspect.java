package com.xe.demo.common.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xe.demo.common.annotation.ControllerLog;
import com.xe.demo.common.annotation.ServiceLog;
import com.xe.demo.common.pojo.ParamData;
import com.xe.demo.common.utils.DateUtil;
import com.xe.demo.common.utils.IPUtil;
import com.xe.demo.model.AuthUser;
import com.xe.demo.model.ILog;
import com.xe.demo.service.LogService;

/**
 * 操作日志记录切点类
 */
@Aspect
@Component//把普通pojo实例化到spring容器中，相当于配置文件中的bean  因为不会自动扫描需要声明Spring的管理类
public class LogAspect {
	@Autowired// 注入Service用于把日志保存数据库
	private LogService logService;
	// 本地异常日志记录对象
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	// Service层注解切点
	@Pointcut("@annotation(com.xe.demo.common.annotation.ServiceLog)")
	public void serviceAspect() {
	}

	// Controller层注解切点
	@Pointcut("@annotation(com.xe.demo.common.annotation.ControllerLog)")
	public void controllerAspect() {
	}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * @param joinPoint 切点
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {//IDEA左边有个"粉红m"表示切点的方法
		getLog(joinPoint, null);
	}

	/**
	 * 异常通知 用于拦截service层记录异常日志
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		getLog(joinPoint, e);
	}
	
	private ILog getLog(JoinPoint joinPoint, Throwable e){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 读取session中的用户
		AuthUser user = (AuthUser) request.getAttribute("loginUser");
		String username = "匿名操作";
		ParamData params = new ParamData();//封装HashMap的参数类型返回 用户名、密码、ip的集合
		if (null != user) {//判断是否登录，否则从 request的返回username值给username
			username = user.getUsername();
		}else{
			if(params.containsKey("username")){//Map是否存在username键
				username = params.getString("username");
			}
		}
		//页面菜单：用户日志
		ILog log = new ILog();
		log.setUsername(username);
		log.setType(1);//0操作日志;1异常日志
		log.setUrl(request.getRequestURI());
		//输出切点的方法 com.xe.demo.controller+  LoginController.logout   +()
		log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
		log.setParams(params.toString());
		log.setRequestip(IPUtil.getIpAdd(request));
		log.setOperDate(DateUtil.getCurDateTime());
		try {
			if(null == e){//ControllerLog是控制层注解  也就是操作日志
				ControllerLog logAnnotation = getControllerAnnotation(joinPoint);//自注解方法
				log.setDescription(logAnnotation.value());
				log.setType(logAnnotation.type());
			}else{//异常日志
				log.setDetail(e.getMessage());
				ServiceLog logAnnotation = getServiceAnnotation(joinPoint);
				log.setDescription(logAnnotation.value());
				log.setType(logAnnotation.type());
			}
			// 保存数据库
			logService.save(log);
		} catch (Exception ex) {
			// 记录本地异常日志
			logger.error("==异常通知异常==");
			logger.error("异常信息:{}", ex.getMessage());
		}
		return log;
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * @param joinPoint 切点
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static ControllerLog getControllerAnnotation(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		ControllerLog logAnnotation = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					logAnnotation = method.getAnnotation(ControllerLog.class);
					break;
				}
			}
		}
		return logAnnotation;
	}

	/**
	 * 获取注解中对方法的描述信息 用于service层注解
	 * @param joinPoint 切点
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static ServiceLog getServiceAnnotation(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		ServiceLog logAnnotation = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					logAnnotation = method.getAnnotation(ServiceLog.class);
					break;
				}
			}
		}
		return logAnnotation;
	}
}