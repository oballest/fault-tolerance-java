package com.redhat.gps.faulttolerance;

import java.time.temporal.ChronoUnit;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/faultTolerance")
public class FaultToleranceResource {
    
    private static Logger logger = LoggerFactory.getLogger(FaultToleranceResource.class);
    
    @POST
    @Path("/retry/{error}")
    @Produces(MediaType.TEXT_PLAIN)
    @Retry(maxRetries = 3, delay = 5, delayUnit = ChronoUnit.SECONDS)
    public String retry(@PathParam("error") String param){
        logger.info("Ejecutando metodo retry");
        if("err".equals(param)){
            throw new WebApplicationException();
        }

        return "Reintento";
    }

    @POST
    @Path("/fallback/{error}")
    @Produces(MediaType.TEXT_PLAIN)
    @Fallback(fallbackMethod = "metodoFallback")
    public String fallback(@PathParam("error") String param){
        logger.info("Ejecutando metodo principal");
        if("err".equals(param)){
            throw new WebApplicationException();
        }
        return "Principal";
    }

    public String metodoFallback(String param){
        logger.info("Ejecutando metodo Fallback");
        return "Fallback";
    }


    @POST
    @Path("/timeOut/{time}")
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(value = 10, unit = ChronoUnit.SECONDS)
    public String timeOut(@PathParam("time") int waitTime){
        logger.info("Ejecutando metodo TimeOut");

        long time = System.currentTimeMillis();
        long espera = waitTime * 1000;

        while(System.currentTimeMillis() < time + espera){
            
        }

        return "TimeOut";
    }

    @POST
    @Path("/circuitBreaker/{error}")
    @Produces(MediaType.TEXT_PLAIN)
    @CircuitBreaker(requestVolumeThreshold = 4, successThreshold = 1, failureRatio = 0.5, delayUnit = ChronoUnit.SECONDS, delay = 20)
    public String circuitBreaker(@PathParam("error") String param){
        logger.info("Ejecutando metodo circuit breaker");
        if("err".equals(param)){
            throw new WebApplicationException();
        }
        return "circuitBreaker";
    }

}