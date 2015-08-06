package com.messi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // write your code here

        //getGpuInfo();

        getGpuMemoryUsage();

        getGpuUtil();

    }

    private static void getGpuProcessMemoryUsage(int pid) {
        String command = "nvidia-smi";
        Process process = null;
        String line = null;
        int gpuMemoryUsage = 0;

        Pattern gpuMemoryUsagePattern = Pattern.compile("(|)([\\x20\\t]+)([\\d]+)([\\x20\\t]+)([\\d]+)([\\x20\\t]+)((G|C\\+G))([\\x20\\t]+)([0-9a-zA-Z_\\-\\.\\/\\=(  )]+)([\\x20\\t]+)([\\d]+)(MiB)( |)");

        try {
            process = Runtime.getRuntime().exec(command);

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                Matcher memMatcher = gpuMemoryUsagePattern.matcher(line);

                //Get Memory Usage
                if (memMatcher.find()){
                    System.out.println(memMatcher.group(3) + ": " + memMatcher.group(5)+ ": " + memMatcher.group(7)+ ": " + memMatcher.group(10)+ ": " + memMatcher.group(12));
                    if(memMatcher.group(2).equals("pid")){
                        gpuMemoryUsage = Integer.parseInt(memMatcher.group(12));
                    }
                }
            }

            //System.out.println("Memory Usage: " + memUsage + "%");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getGpuMemoryUsage() {
        String command = "nvidia-smi";
        Process process = null;
        String line = null;

        int counter = 0;

        Pattern gpuGpuUtilPattern = Pattern.compile("(|)([\\x20\\t]+)([\\d]+)(MiB)([\\x20\\t]+)(/)([\\x20\\t]+)([\\d]+)(MiB)(|)");

        try {
            process = Runtime.getRuntime().exec(command);

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                Matcher memMatcher = gpuGpuUtilPattern.matcher(line);

                //Get Memory Usage
                if (memMatcher.find()){
                    System.out.println("GPU Memory Util(Device " + counter + "): " + memMatcher.group(3) + " / " + memMatcher.group(8));
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getGpuUtil() {
        String command = "nvidia-smi";
        Process process = null;
        String line = null;

        int counter = 0;

        Pattern gpuGpuUtilPattern = Pattern.compile("(|)([\\x20\\t]+)([\\d]+)(%)([\\x20\\t]+)(Default)([\\x20\\t]+)(|)");

        try {
            process = Runtime.getRuntime().exec(command);

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                Matcher memMatcher = gpuGpuUtilPattern.matcher(line);

                //Get Memory Usage
                if (memMatcher.find()){
                    System.out.println("GPU Util(Device " + counter + "): " + memMatcher.group(3) + "%");
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getGpuInfo() {
        String command = "nvidia-smi -q -d MEMORY,UTILIZATION";
        Process process = null;
        String line = null;
        int memTotal = 0, memUsed = 0;
        double memUsage = 0;

        Pattern memoryPattern = Pattern.compile("([\\x20\\t]+)((Total|Used|Free))([\\x20\\t]+)(:)(\\s)([\\d]+)(\\s)(MiB)");

        try {
            process = Runtime.getRuntime().exec(command);

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                Matcher memMatcher = memoryPattern.matcher(line);

                //Get Memory Usage
                if (memMatcher.find()){
                    System.out.println(memMatcher.group(2) + ": " + memMatcher.group(7));
                    if(memMatcher.group(2).equals("Total")){
                        memTotal = Integer.parseInt(memMatcher.group(7));
                    }
                    if(memMatcher.group(2).equals("Used")){
                        memUsed = Integer.parseInt(memMatcher.group(7));
                        memUsage = (double)memUsed/memTotal;
                    }
                }
            }

            System.out.println("Memory Usage: " + memUsage + "%");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
