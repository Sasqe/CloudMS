package com.gcu.util;

import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;


public class Tracer extends AbstractMonitoringInterceptor
{
 	private static final long serialVersionUID = -5378974652504403928L;

	public Tracer() 
    {
    }

	// If true will use the dynamic Spring logger
    public Tracer(boolean useDynamicLogger) 
    {
    	setUseDynamicLogger(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log log) throws Throwable 
    {
    	// Before Method Invocation display a start method trace log statement
        String name = createInvocationTraceName(invocation);
        long start = System.currentTimeMillis();
        log.trace("GCU Method " + name + " execution started at:" + new Date());
        try 
        {
        	// Invoke the method
            return invocation.proceed();
        }
        finally 
        {
        	// After Method Invocation display an end method trace log statement
            long end = System.currentTimeMillis();
            long time = end - start;
            log.trace("GCU Method " + name + " execution lasted:" + time + " ms");
            log.trace("GCU Method " + name + " execution ended at:" + new Date());            
            if (time > 10)
                log.warn("GCU Method execution longer than 10 ms!");
        }
    }
}

