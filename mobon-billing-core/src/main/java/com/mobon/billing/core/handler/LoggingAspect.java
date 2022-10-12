//package com.mobon.billing.core.handler;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StopWatch;
//
////@Aspect
////@Component
//public class LoggingAspect {
//	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
//
////	@Around("execution(* com.mobon..insertMany*(..)) ")
////	public Object aroundInsertManyMethod(ProceedingJoinPoint pjp) throws Throwable {
////		String sigName = pjp.getSignature().toShortString();
////		//StopWatch sw = new StopWatch(getClass().getSimpleName());
////		StopWatch sw = new StopWatch(getClass().getSimpleName() + Thread.currentThread().getName());
////		Object value = null;
////
////		try {
////			sw.start(pjp.getSignature().getName());
////
////			log.debug("== BEFORE {} ==", sigName);
////
////			value = pjp.proceed();
////			
////		} finally {
////			sw.stop();
////			if (sw.getTotalTimeMillis() > 1000) {
////				log.info("== AFTER {} [elapsed : {} ms, return : {}] ==", sigName, sw.getTotalTimeMillis(), value);
////			} else {
////				log.debug("== AFTER {} [elapsed : {} ms, return : {}] ==", sigName, sw.getTotalTimeMillis(), value);
////			}
////		}
////		return value;
////	}
//	
//	@Around("execution(* com.mobon.service..*(..))")
//	public Object aroundListenerMethod(ProceedingJoinPoint pjp) throws Throwable {
//		String sigName = pjp.getSignature().toShortString();
//		StopWatch sw = new StopWatch(getClass().getSimpleName() + Thread.currentThread().getName());
//		Object value = null;
//
//		try {
//			sw.start(pjp.getSignature().getName());
//
//			log.info("== BEFORE {} ==", sigName);
//			
//
//			value = pjp.proceed();
//			
//		} finally {
//			sw.stop();
//			log.info("== AFTER {} [elapsed : {} ms, return : {}] ==", sigName, sw.getTotalTimeMillis(), value);
//		}
//		return value;
//	}
////	
////	@Around("execution(* com.mobon..intoMaria*(..))")
////	public Object aroundIntoMariaMethod(ProceedingJoinPoint pjp) throws Throwable {
////		String sigName = pjp.getSignature().toShortString();
////		StopWatch sw = null;
////		Object value = null;
////
////		try {
////			sw = new StopWatch();
////			sw.start();
////
////			log.info("== BEFORE {} ==", sigName);
////
////			value = pjp.proceed();
////			
////		} finally {
////			sw.stop();
////			log.info("== AFTER {} [elapsed : {} ms, return : {}] ==", sigName, sw.getTotalTimeMillis(), value);
////		}
////		return value;
////	}
//	
////	@Around("execution(* com.mobon..WorkQueue.execute(..))")
////	public Object aroundWorkQueueExecuteMethod(ProceedingJoinPoint pjp) throws Throwable {
////		String sigName = pjp.getSignature().toShortString();
////		StopWatch sw = null;
////		Object value = null;
////
////		try {
////			sw = new StopWatch();
////			sw.start();
////
////			log.info("== BEFORE {} ==", sigName);
////
////			value = pjp.proceed();
////			
////		} finally {
////			sw.stop();
////			log.info("== AFTER {} [elapsed : {} ms, return : {}] ==", sigName, sw.getTotalTimeMillis(), value);
////		}
////		return value;
////	}
//
//	/*
//	 * @Around("execution(* com.example.demo..*(..))") public Object
//	 * loggingAdvice(ProceedingJoinPoint proceedingJoinPoint) { long start =
//	 * System.currentTimeMillis(); String sigName =
//	 * proceedingJoinPoint.getSignature().toShortString();
//	 * log.info("== BEFORE {} ==", sigName);
//	 * 
//	 * Object value = null; try { value = proceedingJoinPoint.proceed(); } catch
//	 * (Throwable e) { log.error(e.getMessage(), e); }
//	 * 
//	 * long duration = System.currentTimeMillis() - start;
//	 * log.info("== AFTER {} [{} ms, return : {}] ==", sigName, duration, value);
//	 * return value; }
//	 */
//}