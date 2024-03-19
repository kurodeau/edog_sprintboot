package com.util;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HTMLSearch {
	
	 public static void main(String[] args) {
	        String packagePath = "src/main/resources/templates"; // 指定要搜索的包路径
	        String targetCode = "@{/fragment-footer}"; // 要搜索的目标代码

	        List<File> filesWithTargetCode = findFilesWithCode(new File(packagePath), targetCode);
	        if (filesWithTargetCode.isEmpty()) {
	            System.out.println("No files contain the target code.");
	        } else {
	            System.out.println("Files containing the target code:");
	            for (File file : filesWithTargetCode) {
	                System.out.println(file.getAbsolutePath());
	            }
	        }
	    }

	    public static List<File> findFilesWithCode(File directory, String targetCode) {
	        List<File> resultFiles = new ArrayList<>();
	        if (directory.isDirectory()) {
	            File[] files = directory.listFiles();
	            if (files != null) {
	                for (File file : files) {
	                    if (file.isDirectory()) {
	                        resultFiles.addAll(findFilesWithCode(file, targetCode)); // 递归搜索子目录
	                    } else if (file.getName().endsWith(".html")) { // 只处理HTML文件
	                        try {
	                            String content = new String(Files.readAllBytes(file.toPath()));
	                            if (content.contains(targetCode)) { // 检查文件内容是否包含目标代码
	                                resultFiles.add(file);
	                            }
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        }
	        return resultFiles;
	    }
	}


