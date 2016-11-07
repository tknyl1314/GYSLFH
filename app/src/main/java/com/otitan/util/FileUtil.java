package com.otitan.util;


import java.io.File;
import java.io.FileFilter;

/**
 * Created by Whs on 2016/10/10 0010.
 */

public class FileUtil  {

    //过滤文件指定文件
    public void fileFilter(String filepath){
        File  file =new File(filepath);
        if(file.isDirectory()){

        }
    }
    public  class  dbfilter implements  FileFilter{
        @Override
        public boolean accept(File file) {
            if(file.isDirectory())
                return true;
            else
            {
                String name = file.getName();
                if((name.endsWith(".sqlite") || name.endsWith(".db"))&& name.contains("GYSLFH")){

                    return  true;
                }
                else{
                    return false;
                }

            }
        }
    }

}
