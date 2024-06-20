package cn.com.goldwind.md4x.config.aop;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.com.goldwind.md4x.business.dao.SysOperationLogsDao;
import cn.com.goldwind.md4x.business.entity.datamart.SysOperationLogs;
import cn.com.goldwind.md4x.shiro.common.utils.TokenUtil;
import cn.com.goldwind.md4x.shiro.domain.entity.SysToken;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.shiro.service.IUserService;

/**
 * 
 * @author wangguiyu
 *
 */
@Aspect
@Component
public class LogAspect {
	private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* cn.com.goldwind.md4x.config.aop.*.*(..))")'
     */
    @Pointcut("@annotation(cn.com.goldwind.md4x.config.aop.OperationLogDetail)")
    public void operationLog(){}

    @Autowired
	private ITokenService tokenService;
    
    @Autowired
	private IUserService userService;
    
    @Autowired SysOperationLogsDao sysOperationLogsDao;
    
    
    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        //System.out.println("进入方法前执行.....");
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        SysOperationLogs logs = new SysOperationLogs();
        
        //1.获取切入点所在的方法
        //Method method = signature.getMethod();
        String apiMethod = signature.getDeclaringTypeName() + "." + signature.getName();
        logs.setApiMethod(apiMethod);
        
        //2.日志信息
        OperationLogDetail annotation = signature.getMethod().getAnnotation(OperationLogDetail.class);
        String operationType = null;
        String detail = null;
        String operationUnit = null;
        int level = 0;
        if(annotation != null) {
        	operationType = annotation.operationType().getValue();
        	logs.setOperationType(operationType);
        	
        	detail = annotation.detail();
        	logs.setApiMethodDesc(detail);
        	
        	operationUnit = annotation.operationUnit().getValue();
        	logs.setOperationUnit(operationUnit);
        	
        	level = annotation.level(); 
        	logs.setLevel(level);
        }
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //3.请求方式：POST/GET/other
        String httpMethod = request.getMethod();
        logs.setHttpMethod(httpMethod);
        
        //4.操作的url
        String requestURI = request.getRequestURI();
        //String requestURL = request.getRequestURL().toString();
        logs.setUrl(requestURI);
        
        //5.客户端ip
        String ip = request.getRemoteAddr();
        logs.setClientIp(ip);
        
        //6.请求人信息
        //User user = (User) request.getSession().getAttribute(SysUser.SESSION_KEY);
        // 1.从request里获取token
        String token = TokenUtil.getRequestToken(request);
     	// 2.根据request里携带的token去数据库查询
     	SysToken sysToken = tokenService.findByToken(token);
     	User user = userService.getById(sysToken.getUserId());
     	logs.setUserName(user.getUserName());
     	logs.setUserNickName(user.getNickName());

      //7.请求参数
      //get的参数
      //请求参数信息
        String paramter = "";
        if(httpMethod.equalsIgnoreCase("GET")){
        	Object[] args = joinPoint.getArgs();
            Object[] arguments  = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = args[i];
            }
            if (arguments != null) {
                try {
                    paramter = JSONObject.toJSONString(arguments);
                } catch (Exception e) {
                    paramter = arguments.toString();
                }
            }
            
        }else {//POST请求*
        	 //request.setCharacterEncoding("UTF-8");
        }
        Date createTime = new Date();
        logs.setCreateTime(createTime);
        //重要,logstash收集log
        //MDC.put("SeqId", UUID.randomUUID().toString());
        log.info(String.format("apiMethod=%s,operationType=%s,detail=%s,operationUnit=%s,level=%d,httpMethod=%s,requestURI=%s,ip=%s,userName=%s,nickName=%s,departmentName=%s,centerName=%s,paramter=%s,createTime=%s",apiMethod,operationType,detail,operationUnit,level,httpMethod,requestURI,ip,user.getUserName(),user.getNickName(),user.getDepartmentName(),user.getCenterName(),paramter,createTime));
        //MDC.clear();
        logs.setRequestParameters(paramter);
        sysOperationLogsDao.insertSelective(logs);
        
    }

    /**
     * 处理完请求，返回内容
     * @param ret
     */
    //@AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret) {
        //System.out.println("方法的返回值 : " + JSON.toJSONString(ret));
    }

    /**
     * 后置异常通知
     */
    //@AfterThrowing("operationLog()")
    public void throwss(JoinPoint jp){
        //System.out.println("方法异常时执行.....");
    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    //@After("operationLog()")
    public void after(JoinPoint jp){
        //System.out.println("方法最后执行.....");
    }

}