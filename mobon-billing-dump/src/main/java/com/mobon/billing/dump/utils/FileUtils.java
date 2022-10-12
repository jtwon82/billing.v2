package com.mobon.billing.dump.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @FileName : FileUtil.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 08. 19. 
 * @Author dkchoi
 * @Comment : 파일 처리.
 */
@Slf4j
public abstract class FileUtils {

    private FileUtils(){
        throw new AssertionError();
    }

    
	/**
	 * @Method Name : appendStringToFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 파일에 문자열을 추가하기 위한 매소드.
	 * @param destDir 처리할 파일.
     * @param writeFile 처리할 파일.
     * @param strData 처리할 파일.
	 * @return
	 */
    public static void appendStringToFile(String destDir, String writeFile, String strData) {

        File file = new File(destDir + writeFile);

        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {

            out.println(strData);

        } catch (IOException e){
            log.error("appendStringToFile Error!");
            log.error("FileName : " + writeFile );
            log.error("Message : " + strData );
        }

    }

    /**
	 * @Method Name : appendStringToFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 파일 삭제용 매소드.
     * @param destDir 처리할 파일 경로.
     * @param deleteFile 처리할 파일.
     * @return boolean 파일삭제 성공여부.
	 */
    public static boolean deleteFile(String destDir, String deleteFile){

        Path filePath = FileSystems.getDefault().getPath(destDir, deleteFile);

        try {

            Files.delete(filePath);

        } catch (IOException | SecurityException e) {

            log.error("# Delete File Error #" );
            log.error(e.getMessage());
            return false;
        }

        return true;
    }
    
    
    /**
	 * @Method Name : makeDir
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 폴더 생성용 매소드.
     * @param destDir 생성할 폴더 경로.
     * @return boolean 폴더생성결과.
	 */
    public static boolean makeDir(String destDir) {
    	boolean result = true;
    	
    	File file  =  new File (destDir);
    	
    	if(!file.exists()){
    		try {
    			log.info("Make Dir => " + destDir);
				Files.createDirectory(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}
    	}
    	
    	return result;
	}
}