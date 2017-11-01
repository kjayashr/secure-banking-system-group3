package com.ss.daoImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ss.dao.AdminDao;

public class AdminDaoImpl implements AdminDao {

	@Override
	public List<String> getExistingLogFilesPaths() {
		
		List<String> listOfFiles = new ArrayList<String>();

		String path= "C:\\logs";

		File dir = new File(path);
        
		File[] files = dir.listFiles();
		
        for (File file : files) {
            System.out.println("path" + file.getAbsolutePath());
            Pattern p = Pattern.compile("(my-application\\.log\\.)([0-9]*)"); 
            System.out.print("name" + file.getName());  

            Matcher m = p.matcher(file.getName()); 
            if (m.matches()) {   
              listOfFiles.add(m.group(2));
            }
         }
        return listOfFiles;
	}
	
}
