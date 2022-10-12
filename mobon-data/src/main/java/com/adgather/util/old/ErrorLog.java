package com.adgather.util.old;

import java.util.ArrayList;


public class ErrorLog {
	
	/**
	 * 오류메세지
	 * @param e
	 * @return
	 */
	public static String getStack( Exception e){
		StringBuffer stack = new StringBuffer("\n###### ERROR LOG ######\n");
		StackTraceElement[] stacks = e.getStackTrace();
		int i = 0;
		boolean check = true;
		for(StackTraceElement element : stacks){
			if(element.getMethodName().equals("doGet")){
				stack.append("CLASS_NAME : ");
				stack.append(element.getFileName());
				stack.append(" / ");
				stack.append("LINE : ");
				stack.append(element.getLineNumber());
				stack.append(" / MESSAGE : ");
				stack.append(e);
				stack.append("\n");
				i++;
				break;
			}else{
				stack.append("CLASS_NAME : ");
				stack.append(element.getFileName());
				stack.append(" / ");
				stack.append("LINE : ");
				stack.append(element.getLineNumber());
				stack.append(" / MESSAGE : ");
				stack.append(e);
				stack.append("\n");
				i++;
			}
			if(i>1){
				break;
			}
			if(i > 0){
				check = false;
			}
		}
		if(check){
			stack.append(e);
			stack.append("\n");
		}
		stack.append("###### END ERROR LOG ######");
		return stack.toString();
	}
	
	public static String getStack(Exception e, String code, Object sessionInfo){
		return getStack(e, code, sessionInfo, false);
	}
	
	public static String getStack(Exception e, Object sessionInfo){
		String code = "";
		try {
			return e.getMessage(); 
		}catch(Exception e1) {
		}
		return getStack(e, code, sessionInfo, false);
	}
	
	public static String getStackIncreaseNum(Exception e, Object sessionInfo){
		String code = String.format("CLASS:%S:%S:%S"
		                            , e.getStackTrace()[0].getClassName()
		                            , e.getStackTrace()[0].getMethodName()
		                            , e.getMessage());
		//RedisUtil.incCount(DateUtils.getDate("yyyyMMdd")+":"+code);
		return code;
	}
	
	/**
	 * 에러 , 에러데이터 redis 수집
	 * @param e
	 * @param code
	 * @param sessionInfo
	 * @param className
	 * @return
	 */
	public static String getStack(Exception e, String code, Object sessionInfo, boolean fail){
		StringBuffer stack = new StringBuffer();
		StringBuffer retrurnStack = new StringBuffer();
		StackTraceElement[] stacks = e.getStackTrace();
		stack.append("\n###### ERROR LOG ######\n");
		int i = 0;
		boolean check = true;
		for(StackTraceElement element : stacks){
			if(element.getMethodName().equals("doGet")){
				stack.append("CLASS_NAME : ");
				stack.append(element.getFileName());
				stack.append(" / ");
				stack.append("LINE : ");
				stack.append(element.getLineNumber());
				stack.append(" / MESSAGE : ");
				stack.append(e);
				stack.append("\n");
				i++;
			}else{
				stack.append("CLASS_NAME : ");
				stack.append(element.getFileName());
				stack.append(" / ");
				stack.append("LINE : ");
				stack.append(element.getLineNumber());
				stack.append(" / MESSAGE : ");
				stack.append(e);
				stack.append("\n");
				i++;
			}
			if(i>1){
				if(check){
					retrurnStack.append(stack.toString());
					check = false;
				}
			}
		}
		if(check){
			retrurnStack.append("\n###### ERROR LOG ######\n");
			retrurnStack.append(e);
			retrurnStack.append("\n");
			stack.append(e);
			stack.append("\n");
		}
		stack.append("###### END ERROR LOG ######");
		retrurnStack.append("###### END ERROR LOG ######");
		
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append(stack.toString());
		errorMessage.append("\n\n");
		errorMessage.append("errorData:");
		
		if(sessionInfo instanceof String){
			errorMessage.append(sessionInfo);
		}else if(sessionInfo instanceof ArrayList){
			errorMessage.append(sessionInfo.toString());
		}else{
			//errorMessage.append(JsonMapper.fromObject(sessionInfo).toString());
		}

		//if (!ConfigServlet.shopDataFileSave && !ConfigServlet.dumpDataFileSave && !ConfigServlet.chargeDataFileSave) {
		//	String temp = fail?"":ConfigServlet.SERVER_HOST+":";
		//	RedisUtilSub.setErrorLog(temp+code, errorMessage.toString(),fail);
		//}
		return retrurnStack.toString();
	}
	
	public static String getDebugStack( Exception e ){
		StringBuffer stack = new StringBuffer("\n###### ERROR LOG ######\n");
		StackTraceElement[] stacks = e.getStackTrace();
		for(StackTraceElement element : stacks){
			stack.append("CLASS_NAME : ");
			stack.append(element.getFileName());
			stack.append(" / ");
			stack.append("LINE : ");
			stack.append(element.getLineNumber());
			stack.append(" / MESSAGE : ");
			stack.append(e);
			stack.append("\n");
		}
		stack.append("###### END ERROR LOG ######");

		//if (!ConfigServlet.shopDataFileSave && !ConfigServlet.dumpDataFileSave && !ConfigServlet.chargeDataFileSave) {
		//	RedisUtilSub.setErrorLog(e.getClass().toString(), stack.toString(),false);
		//}
		return stack.toString();
	}
	
}