package system.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Client {
	private static ZooKeeper zoo;
	private static ZooMethods method;

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Welcome!");
		Scanner scanner = new Scanner(System.in);

        while (true) {

            String input = scanner.nextLine();
            String [] arr =input.split("\\s");
            
            if ("connect".equals(arr[0])) {
            	method= new ZooMethods();
            	zoo = method.connect(arr[1]);
                System.out.println("You are connected to " +arr[1] + " server.");
            }
            else if ("create".equals(arr[0])) {
            	try {
            		Stat stat = ZooMethods.exists(arr[1]);
                    if(stat == null){
                    ZooMethods.create(arr[1], "".getBytes()); 
                    System.out.println("File named \""+arr[1] + "\" is created.");
                    }
                    else{
                        System.out.println("File already exist.");
                        }
                 } catch (Exception e) {
                    System.out.println(e.getMessage());
                 }
            }
            else if ("delete".equals(arr[0])) {
            	try {
            		Stat stat = ZooMethods.exists(arr[1]);
                    if(stat != null){
                    	ZooMethods.delete(arr[1]); 
                    	System.out.println("File named \"" +arr[1] + "\" is deleted.");
                    }
                    else{
                        System.out.println("You cannot delete file. File named \""+arr[1]+"\" does not exist in server.");
                    }
                 } catch(Exception e) {
                    System.out.println(e.getMessage()); 
                 }
            }
            else if ("read".equals(arr[0])) {
            	try{
            		Stat stat = ZooMethods.exists(arr[1]);
                    if(stat != null){
                    	String data = ZooMethods.read(arr[1]);
                    	System.out.println(data);
                    }
                    else{
                    System.out.println("File does not exist.");
                    }
            	}
                 catch(Exception e) {
                        System.out.println(e.getMessage()); 
                     }
            }
            else if ("append".equals(arr[0])) {  	
            	try{
            		Stat stat = ZooMethods.exists(arr[1]);
                    if(stat != null){
                    	String str="";
                    	for(int i=2;i< arr.length;i++){
                    		str= str +arr[i]+ " ";
                    	}
                    	ZooMethods.append(arr[1],str.getBytes()); 
                    	System.out.println(str + "is appended to the file named \""+ arr[1] +"\".");
                    }
                    else{
                    System.out.println("File does not exist.");
                    }
            	}
                 catch(Exception e) {
                        System.out.println(e.getMessage()); 
                     }            
            	}
            else if ("update".equals(arr[0])) {  	//additional
            	try{
            		Stat stat = ZooMethods.exists(arr[1]);
                    if(stat != null){
                    	String str="";
                    	for(int i=2;i< arr.length;i++){
                    		str= str +arr[i]+ " ";
                    	}
                    	ZooMethods.update(arr[1],str.getBytes()); 
                    	System.out.println("File named \""+arr[1]+"\" is updated. New content : "+ str +".");
                    }
                    else{
                    System.out.println("File does not exist.");
                    }
            	}
                 catch(Exception e) {
                        System.out.println(e.getMessage()); 
                     }            
            	}
            else if ("list".equals(arr[0])) {  	    //additional
            	try{
            		
                    	String files =ZooMethods.getFiles(); 
                    	System.out.println(files);
            	}
                 catch(Exception e) {
                        System.out.println(e.getMessage()); 
                     }            
            	}
            else if ("exit".equals(arr[0])) {
            	ZooMethods.close();
                System.out.println("Session is terminated.");

            }
           
            else if("quit".equals(arr[0])){
            	break;
            }
            else {
                System.out.println("Enter valid command.");
            }
        }

        scanner.close();

    }

}


