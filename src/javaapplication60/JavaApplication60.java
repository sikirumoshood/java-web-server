/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @Author Sikiru- Moshood
 */


package javaapplication60;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Sikiru Moshood
 */
public class JavaApplication60 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
          
          
           ServerSocket ssocket= new ServerSocket(9270);
           System.out.println("Server listening on port 9270...");
           while(true){
               
             try{  
                Socket connectionSock = ssocket.accept();
                System.out.println("Connection made" + connectionSock);
                BufferedReader br = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
                String requestLine ;
                boolean isRequestLine = true;
                String []requests = {""};
                while((requestLine = br.readLine()) != null)
                {
                    if(isRequestLine)
                    {
                        requests = requestLine.split(" ");
                        isRequestLine = false;
                    }
                    if( requestLine.length() <= 0 )
                        break;
                    
                    System.out.println(requestLine);
                }
                
                PrintWriter pr = new PrintWriter( connectionSock.getOutputStream());
                
                
                
                
                
                //writing response body
                //Check resquest file and return response.
                System.out.println("[ ");
                for(String req: requests)
                {
                    System.out.print(req + ", ");
                }
                System.out.println(" ]");
                 
                 
                
                
                 
                if(requests[1].equals("/"))
                {
                    pr.print("HTTP/1.1 200 OK \r\n");
                    pr.print("Content-Type: text/html \r\n");
                    pr.print("\r\n"); //End of writing headers
                    pr.print("<h1>YOU ARE IN THE ROOT DIRECTORY</h1>");
                }
                
                
                else{
                    
                        String [] memes = requests[1].split("\\.");
                        String meme  = memes[memes.length-1];
                        System.out.println("******PRINTING MIMES******");
                         System.out.println("[ ");
                        for(String mime: requests[1].split("\\."))
                        {
                            System.out.print(mime + ", ");
                        }
                        System.out.println(" ]");
                        //what type of file is requested if image, process else read as text and return
                        ArrayList<String> pageMemes = new ArrayList();
                        pageMemes.add("html");
                        pageMemes.add("css");
                        pageMemes.add("xml");
                        pageMemes.add("js");
                        pageMemes.add("txt");
                        pageMemes.add("php"); //you can add more later
                        
                        
                        ArrayList<String> imageMemes = new ArrayList();
                        imageMemes.add("jpeg");
                        imageMemes.add("jpg");
                        imageMemes.add("png");
                        
                        
                       
                        
                        if (pageMemes.contains(meme))
                        {
                            
                            System.out.println("\n\n[TEXT REQUEST]\n\n");
                            try{
                                    FileInputStream fir = new FileInputStream("src/www"+requests[1]);
                                    BufferedReader br1 = new BufferedReader(new InputStreamReader(fir));
                                    String line = "";

                                    //Only print headers when the file is found.

                                    pr.print("HTTP/1.1 200 OK \r\n");
                                    pr.print("Content-Type: text/"+meme+" \r\n");
                                    pr.print("\r\n"); //End of writing headers
                                    while((line = br1.readLine()) != null)
                                    {

                                        pr.print(line);
                                    }
                            }
                            catch(Exception e)
                            {
                                pr.print("HTTP/1.1 400 NOTFOUND \r\n");
                                pr.print("Content-Type: text/"+meme+" \r\n");
                                pr.print("\r\n"); //End of writing headers
                                pr.print("<p class='display-4'>File not found</p>");
                            }
                        }
                        
                        else if( imageMemes.contains(meme))
                        {
                            
                            System.out.println("\n\n[IMAGES REQUEST]\n\n");
                            try{
                                
                                File f = new File("src/www"+requests[1]);
                                FileInputStream fir = new FileInputStream(f);
                                DataOutputStream dos = new DataOutputStream(connectionSock.getOutputStream());
                                DataInputStream dis = new DataInputStream(fir);
                                String line = "";

                                //Only print headers when the file is found.

                                pr.print("HTTP/1.1 200 OK \r\n");
                               
                                pr.print("Content-Type: image/"+meme+" \r\n");
                                
                                pr.print("Content-Length: "+f.length() +" \r\n");
                                
                                pr.print("\r\n"); //End of writing headers
                                
                                byte [] buffer = new byte[1024];
                                int lengthRead = 0;
                                while((lengthRead = dis.read(buffer)) != -1)
                                {
                                    System.out.println("[read: "+lengthRead+"bytes");
                                    dos.write(buffer,0,lengthRead);
                                   
                                   
                                }
                                
                                dis.close();
                                dos.flush();
                                //dos.close();
                                
                            }
                            
                            catch(Exception e)
                            {
                                pr.print("HTTP/1.1 404 NOTFOUND \r\n");
                                pr.print("Content-Type: text/"+meme+" \r\n");
                                pr.print("\r\n"); //End of writing headers
                                pr.print("<p class='display-4'>File not found</p>");
                            }
                            
                        }
                        
                        else{
                            
                            pr.print("HTTP/1.1 400 BADREQUEST \r\n");
                            pr.print("Content-Type: text/html \r\n");
                            pr.print("\r\n"); //End of writing headers
                            pr.print("<h1>File type not supported</h1>");
                            
                            
                        }
                        
                        
                        

                    }
                
                
                pr.flush();
                pr.close();
                
             }
              catch(Exception e)
              {
                      
                      System.out.println(e.toString());
             }
             
             
           }
          
           
              
                    
       }       
       catch(IOException err)
       {
           System.out.print(err.toString());
           
       }
    }
    
}
