package com.wly.config.server.token;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
public class AccessTokenAspect {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(accessTokenCheck)")
    public Object checkAccessToken(ProceedingJoinPoint joinPoint, AccessTokenCheck accessTokenCheck) throws Throwable {
        StandardEvaluationContext context = buildContext(joinPoint);
        String token = extractToken(context, accessTokenCheck);
        String app = extractApp(context, accessTokenCheck);
        String env = extractEnv(context, accessTokenCheck);
        // 验证token
        if (!ConfAccessTokenHelper.checkAccessToken(token, app, env)) {
            throw new RuntimeException("Access token is invalid or expired");
        }

        // 执行原方法
        return joinPoint.proceed();
    }

    /**
     * 提取token，优先级：固定值 > SPEL表达式 > 请求头
     */
    private String extractToken(StandardEvaluationContext context, AccessTokenCheck accessTokenCheck) {
        // 1. 优先使用固定值
        if (StringUtils.hasText(accessTokenCheck.token())) {
            return accessTokenCheck.token();
        }

        // 2. 使用SPEL表达式提取
        if (StringUtils.hasText(accessTokenCheck.tokenExpr())) {
            return extractValueBySpel(context, accessTokenCheck.tokenExpr());
        }
        throw new IllegalArgumentException("No token provided");
    }

    private String extractApp(StandardEvaluationContext context, AccessTokenCheck accessTokenCheck) {
        // 1. 优先使用固定值
        if (StringUtils.hasText(accessTokenCheck.app())) {
            return accessTokenCheck.app();
        }

        // 2. 使用SPEL表达式提取
        if (StringUtils.hasText(accessTokenCheck.appExpr())) {
            return extractValueBySpel(context, accessTokenCheck.appExpr());
        }
        throw new IllegalArgumentException("No app provided");
    }

    private String extractEnv(StandardEvaluationContext context, AccessTokenCheck accessTokenCheck) {
        // 1. 优先使用固定值
        if (StringUtils.hasText(accessTokenCheck.env())) {
            return accessTokenCheck.env();
        }

        // 2. 使用SPEL表达式提取
        if (StringUtils.hasText(accessTokenCheck.envExpr())) {
            return extractValueBySpel(context, accessTokenCheck.envExpr());
        }
        throw new IllegalArgumentException("No env provided");
    }

    /**
     * 使用SPEL表达式从方法参数中提取token/appname
     */
    private String extractValueBySpel(StandardEvaluationContext context, String expression) {
        try {
            Expression expr = parser.parseExpression(expression);
            Object value = expr.getValue(context);

            if (value == null) {
                throw new IllegalArgumentException("Token/apname/env cannot be null from expression: " + expression);
            }

            return value.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract token/appname/env using SPEL: " + expression, e);
        }
    }

    private static StandardEvaluationContext buildContext(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        StandardEvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return context;
    }
}