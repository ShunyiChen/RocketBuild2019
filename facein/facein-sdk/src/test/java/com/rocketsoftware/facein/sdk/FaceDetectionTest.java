package com.rocketsoftware.facein.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FaceDetectionTest 
    extends TestCase
{
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FaceDetectionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FaceDetectionTest.class );
    }
    
    public void testPutFile() throws ClientProtocolException, IOException {
    	// make sure cookies is turn on
    	CookieHandler.setDefault(new CookieManager());
		HttpClientBuilder builder = HttpClientBuilder.create();
		HttpClient httpclient = builder.build();
	    HttpPost httppost = new HttpPost("http://localhost:8080/api/v1/files/12345");
	    // add header
	    httppost.setHeader("User-Agent", USER_AGENT);
	    
//	    File file = new File("C:/Demo/aa.png");
	    
	    File file = new File("C:/Demo/temp/aa.png");
	    MultipartEntityBuilder meb = MultipartEntityBuilder.create();
	    meb.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName());
	    HttpEntity entity = meb.build();
	    httppost.setEntity(entity);
	    System.out.println("executing request " + httppost.getRequestLine());

	    HttpResponse response = httpclient.execute(httppost);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result.toString());
	}
}
