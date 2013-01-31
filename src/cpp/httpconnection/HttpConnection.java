package cpp.httpconnection;

import java.io.*;
import java.net.*;

import cpp.validatecode.ValidateCode;

public class HttpConnection 
{
    public String sendPost(java.net.URL url,String parameters,String session) throws IOException 
    {   
//		打开链接;
        URLConnection connection = url.openConnection();     
        
        connection.setDoInput(true);
        connection.setDoOutput(true);   
        connection.setRequestProperty("Cookie", session);
        /**   
         * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...   
         */    
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1"); 
     	//向页面传递数据。post的关键所在
        out.write(parameters); 
        out.flush();     
        out.close();     
        /**   
         * 这样就可以发送一个看起来象这样的POST：    
         * POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:   
         * text/plain Content-type: application/x-www-form-urlencoded   
         * Content-length: 99 username=bob password=someword   
         */    
        // 一旦发送成功，用以下方法就可以得到服务器的回应   
        String sCurrentLine ="", sTotalString="";     
        InputStream l_urlStream;     
        l_urlStream = connection.getInputStream();     
        // 传说中的三层包装?    
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));     
        while ((sCurrentLine = l_reader.readLine()) != null) 
        {     
            sTotalString += sCurrentLine;     
    
        }     
System.out.println("返回数据\n"+sTotalString);   
        return sTotalString;
    }    

	public static void main (String args[]) throws IOException, URISyntaxException
	{
		java.net.URL code = new java.net.URL("http://172.20.32.19:8080/rdms2/submit/validateImage.jsp");
		java.net.URL login = new java.net.URL("http://172.20.32.19:8080/rdms2/submit/index.do");
		
//		System.out.println(new ValidateCode().getValidateCode (new File ("c:\\untitled.png").toURL()));
		
		String validatecode[] = new ValidateCode().getValidateCode (code);
System.out.println("验证码\t"+validatecode[0]);
		
		String parameters="productCode=Portal&username=xuhui&password=notmypassword&validateImageCode="+validatecode[0];
		
		new HttpConnection().sendPost(login,parameters,validatecode[1]);
		
	}
}
