package com.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetLocalIPAddress {
    public static void main(String[] args) {
        try {
            // 創建 Bash 腳本
            String[] command = {"bash", "-c", "ifconfig | grep 'inet ' | grep -v '127.0.0.1' | cut -d: -f2 | awk '{print $1}'"};

            
            // 創建 ProcessBuilder 對象
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // 設置工作目錄
            processBuilder.directory(new File(System.getProperty("user.dir")));

            // 啟動進程
            Process process = processBuilder.start();

            // 獲取輸出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 讀取 Bash 輸出
            String line;
            StringBuilder ipAddress = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                ipAddress.append(line);
            }

            // 關閉輸出流
            reader.close();

            // 將 IPv4 地址寫入到 ip.txt 檔案中
            FileWriter writer = new FileWriter("ip.txt");
            writer.write(ipAddress.toString());
            writer.close();

            // 提示訊息
            System.out.println("IPv4 地址已經寫入到 ip.txt 檔案中。");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
