package com.fishedee;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class CodeWriter {
    public void write(String outputFileName,String data){
        FileWriter fileWriter = null;
        StringReader stringReader = null;
        try{
            fileWriter = new FileWriter(outputFileName);
            stringReader = new StringReader(data);
            char[] buffer = new char[1024];
            int length = 0;
            while( (length = stringReader.read(buffer)) != -1 ){
                fileWriter.write(buffer,0,length);
            }
        }catch(IOException e){
            throw new CrashException("写入不了文件"+outputFileName,e);
        }
    }
}
