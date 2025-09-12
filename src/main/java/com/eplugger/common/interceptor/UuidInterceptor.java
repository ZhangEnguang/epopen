package com.eplugger.common.interceptor;

import com.eplugger.common.entity.EntityImpl;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class UuidInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        // 检查是否是插入操作
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            // 处理单个实体对象
            if (parameter instanceof EntityImpl) {
                EntityImpl entity = (EntityImpl) parameter;
                // 如果id为空，生成UUID
                if (entity.getId() == null) {
                    entity.setId(UUID.randomUUID().toString().replace("-", ""));
                }
            }
            // 处理集合情况（批量插入）
            else if (parameter instanceof Collection<?>) {
                Collection<?> collection = (Collection<?>) parameter;
                for (Object item : collection) {
                    if (item instanceof EntityImpl) {
                        EntityImpl entity = (EntityImpl) item;
                        if (entity.getId() == null) {
                            entity.setId(UUID.randomUUID().toString().replace("-", ""));
                        }
                    }
                }
            }
            // 处理Map参数中包含实体对象的集合的情况（mybatis批量插入常见方式）
            else if (parameter instanceof java.util.Map) {
                java.util.Map<?, ?> paramMap = (java.util.Map<?, ?>) parameter;
                for (Object value : paramMap.values()) {
                    if (value instanceof EntityImpl) {
                        EntityImpl entity = (EntityImpl) value;
                        if (entity.getId() == null) {
                            entity.setId(UUID.randomUUID().toString().replace("-", ""));
                        }
                    } else if (value instanceof Collection<?>) {
                        Collection<?> collection = (Collection<?>) value;
                        for (Object item : collection) {
                            if (item instanceof EntityImpl) {
                                EntityImpl entity = (EntityImpl) item;
                                if (entity.getId() == null) {
                                    entity.setId(UUID.randomUUID().toString().replace("-", ""));
                                }
                            }
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里配置一些属性
    }
} 