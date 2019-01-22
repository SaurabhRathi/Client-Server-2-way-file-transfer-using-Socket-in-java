package hi;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

class Client{  
	public static void main(String args[])throws Exception{  
		//String address = "";
		String sendFile = "send.txt";
		Scanner sc=new Scanner(System.in);
		
		//create the socket on port 5000
		Socket s=new Socket("127.0.0.1",5000);
		
		DataInputStream din = new DataInputStream(s.getInputStream());  
		DataOutputStream dout = new DataOutputStream(s.getOutputStream()); 
		
		//uploading a file from client to server
		try{
			String ip="";  
			
			//ip = din.readUTF();
			System.out.println("Sending the first file from client to server.");
			
			if(!ip.equals("stop")){  
				
				System.out.println("Sending File: "+ sendFile);
				dout.writeUTF(sendFile);  
				dout.flush();  
				
				File f=new File(sendFile);
				FileInputStream fin=new FileInputStream(f);
				long sz=(int) f.length();
				
				byte b[]=new byte [1024];
				
				int read;
				
				dout.writeUTF(Long.toString(sz)); 
				dout.flush(); 
				
				//System.out.println ("Size: "+ sz);
				//System.out.println ("Buf size: "+ss.getReceiveBufferSize());
				
				while((read = fin.read(b)) != -1){
				    dout.write(b, 0, read); 
				    dout.flush(); 
				}
				fin.close();
				
				System.out.println("Transfer done..."); 
				dout.flush(); 
			}  
			dout.writeUTF(" ");  
			System.out.println("Send Complete from client to server");
			dout.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("An error occured");
		}
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		
		System.out.println("Type 'start' to start receiving from server : ");
		String str="",filename="";  
		try{
			while(!str.equals("start"))
				str=br.readLine(); 
			 
				dout.writeUTF(str); 
				dout.flush();  
				
				filename=din.readUTF(); 
				System.out.println("Receving file: "+filename);
				filename="client"+filename;
				System.out.println("Saving as file: "+filename);
			//
			long sz=Long.parseLong(din.readUTF());
			System.out.println ("File Size: "+(sz/(1024*1024))+" MB");
			
			byte b[]=new byte [2048];
			System.out.println("Receving file..");
			FileOutputStream fos=new FileOutputStream(new File(filename),true);
			long bytesRead;
			do
			{
				bytesRead = din.read(b, 0, b.length);
				fos.write(b,0,b.length);
			}while(!(bytesRead<2048));
			
			System.out.println("Completed");
			fos.close(); 
			dout.close();  	
			s.close();  
		}
		catch(EOFException e)
		{
			//do nothing
		}
	}
}  
//this is a typical client program 
