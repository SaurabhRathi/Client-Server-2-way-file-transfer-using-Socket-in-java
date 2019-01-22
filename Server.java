package hi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class Server{  
	public static void main(String args[])throws Exception{   
		String filename;
		//System.out.println("Entering File: ");
		Scanner sc=new Scanner(System.in);
		filename= "file.txt";//sc.nextLine();
		String result = null;
		sc.close();
		while(true)
		{
			//create server socket on port 5000
			ServerSocket ss=new ServerSocket(5000); 
			System.out.println ("Waiting for request");
			Socket s=ss.accept();  
			System.out.println ("Connected With "+s.getInetAddress().toString());
			
			
			DataInputStream din=new DataInputStream(s.getInputStream());  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			
			//server receiving 
			
			System.out.println("Starting to recieve...");
			
			String ip = "", receivedFile = "";
			
			
			try{
				//while(!ip.equals("start"))
					//ip=br.readLine(); 
				 
					//dout.writeUTF(ip); 
					//dout.flush();  
					
					receivedFile=din.readUTF(); 
					System.out.println("Receiving file: "+ receivedFile);
					receivedFile = "server" + receivedFile;
					System.out.println("Saving as file: "+ receivedFile);
				//
				long sz=Long.parseLong(din.readUTF());
				System.out.println ("File Size: "+(sz)+" B");
				
				byte b[]=new byte [1024];
				System.out.println("Receiving file..");
				FileOutputStream fos=new FileOutputStream(new File(receivedFile),true);
				long bytesRead;
				do
				{
					bytesRead = din.read(b, 0, b.length);
					fos.write(b,0,b.length);
				}while(!(bytesRead<1024));
				
				System.out.println("Completed");
				fos.close(); 
				//dout.close();  	
				//s.close();  
			}
			catch(EOFException e)
			{
				//do nothing
			}
			
			try{
				File f = new File(receivedFile);
				
				BufferedReader br = new BufferedReader(new FileReader(f)); 
				
				String ipFile = "";
				String st; 
				
				while ((st = br.readLine()) != null){ 
				    //System.out.println(st); 
					ipFile += st;
				}
				
//				System.out.println(ipFile);
				
				HashMap<String,Integer> hMap = new HashMap<>();
				 hMap.clear();
				String[] words = ipFile.split(" ");
				
				int cnt=0;
				for(String word : words)
				{cnt++;}
				
				
				//System.out.println(words.length);
				
				for (int i=0;i<cnt-2;i++) {
					//System.out.println(word);
					if(hMap.containsKey(words[i])) {
			            int count = hMap.get(words[i]);
			            hMap.put(words[i],count+1);
			         } else {
//			        	System.out.print(words[i]);
			            hMap.put(words[i],1);
			         }
				}
				
				result = "";
				
				for(HashMap.Entry<String,Integer> entry : hMap.entrySet()){
					result += entry.getKey() + " " + entry.getValue().toString() + " ";
				}
				
				System.out.println("Result : ");
				System.out.println(result);
				
				File fi = new File(filename);
				
				RandomAccessFile fr = new RandomAccessFile(fi, "rw");
				
		        try {
		            //fr = new FileWriter(fi);
		            fr.seek(0);
		            for(HashMap.Entry<String,Integer> entry : hMap.entrySet()){
						//result += entry.getKey() + " " + entry.getValue().toString() + "\n";
		            	fr.write((entry.getKey() + " " + entry.getValue().toString() + " ").getBytes());
		            	System.out.println((entry.getKey() + " " + entry.getValue().toString() + " "));
					}
		            fr.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			     
			}
			catch(Exception e){
				System.out.println("File error");
			}
			
			//server sending back a file
			try{
				String str="";  
				
				str=din.readUTF();
				System.out.println("SendGet....Ok");
				
				if(str.equals("start")){  
				
					System.out.println("Sending File: "+filename);
					dout.writeUTF(filename);  
					dout.flush();  
					
					File f=new File(filename);
					FileInputStream fin=new FileInputStream(f);
					
					long sz=(int) f.length();
					
					dout.writeUTF(Long.toString(sz)); 
					dout.flush(); 
					
					byte b[]=new byte [1024];
					
					int read;
					
//					dout.writeUTF(Long.toString(sz)); 
//					dout.flush(); 
//					
					System.out.println ("Size: "+sz);
					System.out.println ("Buf size: "+ss.getReceiveBufferSize());
					
					while((read = fin.read(b)) != -1){
					    dout.write(b, 0, read); 
					    dout.flush(); 
					}
					fin.close();
					
					System.out.println("..ok"); 
					dout.flush(); 
				}  
//				dout.writeUTF("stop");  
				System.out.println("Send Complete");
				dout.flush();  
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("An error occured");
			}
			din.close();  
			s.close();  
			ss.close();  
		}
	}
}  