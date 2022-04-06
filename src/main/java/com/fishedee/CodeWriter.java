package com.fishedee;

import java.io.*;

public class CodeWriter {
    public void write(String outputFileName,String data){
        FileOutputStream outFile = null;
        OutputStreamWriter outputStreamWriter = null;
        StringReader stringReader = null;
        try{
            outFile = new FileOutputStream(outputFileName);
            outputStreamWriter = new OutputStreamWriter(outFile,"UTF-8");
            stringReader = new StringReader(data);
            char[] buffer = new char[1024];
            int length = 0;
            while( (length = stringReader.read(buffer)) != -1 ){
                outputStreamWriter.write(buffer,0,length);
            }
            outputStreamWriter.flush();
        }catch(IOException e){
            throw new CrashException("写入不了文件"+outputFileName,e);
        }finally {
            if( stringReader != null ){
                stringReader.close();
            }
            if( outFile != null ){
                try{
                    outFile.close();
                }catch(IOException e){
                    throw new CrashException("无法关闭文件",e);
                }
            }
        }
    }
}
