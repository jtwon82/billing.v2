package com.mobon.billing.dump.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

public interface DumpFileService {

	/**
	 * @Method Name : FileReadProcess
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 파일을 읽고 Summary시키는 서비스.
	 * @param AnHourAgo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> FileReadProcess(String AnHourAgo) throws Exception;
	
	/**
	 * @Method Name : RetryFileReadProcess
	 * @Date : 2020. 08. 20.
	 * @Author : dkchoi
	 * @Comment : Summary된 데이터 파일을 읽는 서비스.
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> RetryFileReadProcess() throws Exception;
	

	/**
	 * @Method Name : makeRetryFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 데이터 등록 오류 시 해당 데이터를 파일로 남기는 서비스.
	 * @param Object
	 * @return
	 * @throws Exception
	 */
	public void makeRetryFile(Collection<Future<Object>> futures);
	
	/**
	 * @Method Name : moveRetryFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 재처리 데이터 처리 성공 시. 성공 폴더로 이동시키는 서비스.
	 * @param Object
	 * @return
	 * @throws Exception
	 */
	public void moveRetryFile(Collection<Future<Object>> futures);
	
	
	/**
	 * @Method Name : removeRetryFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 재처리 성공파일을 보관기간에 따라 제거.
	 * @param long removeDate 보관기간(일)
	 * @return
	 * @throws Exception
	 */
	public void removeRetryFile(long removeDate);
}
