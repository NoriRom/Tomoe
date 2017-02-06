package com.tomoe;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

public class Tomoe extends Activity implements View.OnClickListener {

	private Button Launcher = null;
	private Button Stopper  = null; 
	private WebView mWebView = null;
	private Button SaveOption  = null;
	public TextView TextSave = null;
	private CheckBox checkvideo = null;
	private CheckBox checktv = null;
	private CheckBox checkmail = null;
	private CheckBox checkSMS = null;
	private CheckBox checklumiere = null;
	private CheckBox checkstopauto = null;
	public TextView Detecttatus = null;
	public ImageView StatusButton = null;

	
    @Override
    public void onClick(View v) {
      
    	String link=null;
    	String contain="";

    	TextSave= (TextView)findViewById(R.id.TextSave);
    	Detecttatus = (TextView)findViewById(R.id.DetectStatus); 
    	checkvideo= (CheckBox)findViewById(R.id.checkvideo);
    	checktv= (CheckBox)findViewById(R.id.checktv);
    	checkmail= (CheckBox)findViewById(R.id.checkmail);
    	checkSMS= (CheckBox)findViewById(R.id.checkSMS);
		checklumiere= (CheckBox)findViewById(R.id.checklumiere);
		checkstopauto= (CheckBox)findViewById(R.id.checkstopauto);
    	StatusButton = (ImageView)findViewById(R.id.imageStatus);
    	
    	
    	//event en fonction du bouton
    	switch(v.getId()) { 	
    	case R.id.imageStatus:
			mWebView = (WebView) findViewById(R.id.webview);
			mWebView.loadUrl("Test Button Image");
    		//if(StatusButton.getBackground().equals(R.drawable.allumer))
    		if(StatusButton.getDrawable().getConstantState().equals
            (getResources().getDrawable(R.drawable.allumer).getConstantState()))
    		{
    			link="http://82.240.19.22:8383/yana-server/plugins/detect_porte/stop_detect_porte.php";
    			  //on charge la page
    		    	mWebView = (WebView) findViewById(R.id.webview);
    		  	    mWebView.loadUrl(link);
    		}
    		//if(StatusButton.getBackground().equals(R.drawable.eteint))
    		if(StatusButton.getDrawable().getConstantState().equals
    	            (getResources().getDrawable(R.drawable.eteint).getConstantState()))
    		{
    			//link="http://81.56.16.35/yana-server/plugins/detect_porte/launch_detect_porte.php";
				link="http://82.240.19.22:8383/yana-server/plugins/detect_porte/launch_detect_porte.php";
    			  //on charge la page
    		    	mWebView = (WebView) findViewById(R.id.webview);
    		  	    mWebView.loadUrl(link); 
    		}
        break;
	    case R.id.Launcher:
	    link="http://82.240.19.22:8383/yana-server/plugins/detect_porte/launch_detect_porte.php";
	  //on charge la page
    	mWebView = (WebView) findViewById(R.id.webview);
  	    mWebView.loadUrl(link);  
	    break;
	    case R.id.Stopper:
	    link="http://82.240.19.22:8383/yana-server/plugins/detect_porte/stop_detect_porte.php";
	  //on charge la page
    	mWebView = (WebView) findViewById(R.id.webview);
  	    mWebView.loadUrl(link);  
  	    break;
	    case R.id.SaveOption:
	    	link="http://82.240.19.22:8383/yana-server/plugins/detect_porte/detectporte.API.php?action=option";
	    	if(checktv.isChecked())
	    		contain=contain+"&tv=1";
	    	if(checkvideo.isChecked())
	    		contain=contain+"&video=1";
	    	if(checkmail.isChecked())
	    		contain=contain+"&mail=1";
	    	if(checkSMS.isChecked())
	    		contain=contain+"&texto=1";
			if(checklumiere.isChecked())
				contain=contain+"&lumiere=1";
			if(checkstopauto.isChecked())
				contain=contain+"&stopauto=1";
	    	//TextSave.setText(contain);
	    	link=link+contain;
	    	//on lance le thread qui va se connecter et maj IHM
	    	ThreadConnection t = new ThreadConnection("option"+contain,"updtOptions");
	    	t.start();

  	    break;
    	}
    	//on charge la page
    	//mWebView = (WebView) findViewById(R.id.webview);
  	    //mWebView.loadUrl(link);   

    	// Execute some code after 2 seconds have passed
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() { 
             public void run() { 
            	//on met � jour le statut de la d�tection
             	ThreadConnection status = new ThreadConnection("status","getStatus");
             	status.start();
             } 
        }, 3000); 
       
  	
    	Log.d("MessageDebugTest", "test");//logs

    }
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tomoe);
        
        Launcher = (Button)findViewById(R.id.Launcher);
        Stopper = (Button)findViewById(R.id.Stopper);
        SaveOption = (Button)findViewById(R.id.SaveOption);
        StatusButton = (ImageView)findViewById(R.id.imageStatus);   
        
        Launcher.setOnClickListener(this);
        Stopper.setOnClickListener(this);
        SaveOption.setOnClickListener(this);
        StatusButton.setOnClickListener(this);
        
        
        ThreadConnection showvideo = new ThreadConnection("showvideo","getOptions");
        showvideo.start();
		ThreadConnection showtexto = new ThreadConnection("showsms","getOptions");
		showtexto.start();
		ThreadConnection showlumiere = new ThreadConnection("showlumiere","getOptions");
		showlumiere.start();
		ThreadConnection showstopauto = new ThreadConnection("showstopauto","getOptions");
		showstopauto.start();
		ThreadConnection showmail = new ThreadConnection("showmail","getOptions");
        showmail.start();
        ThreadConnection showtele = new ThreadConnection("showtele","getOptions");
        showtele.start();
        
        ThreadConnection status = new ThreadConnection("status","getStatus");
        status.start();
        
	    //test connection
/*	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

	    if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
	      boolean wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	      Log.d("NetworkState", "L'interface de connexion active est du Wifi : " + wifi);
	      
	    }
*/

	   
    }
    
    public class ThreadConnection extends Thread {
    	private String _link, _action;
    	
    	public ThreadConnection (String link, String action){
    		setLink(link);
    		_action=action;
    	}
    	
    	  public void run() {

    		  System.setProperty("http.keepAlive", "false");
    		  OutputStreamWriter writer = null;
    		  BufferedReader reader = null;
    		  URLConnection connexion = null;
    		  int connected=0;
    		  try {
    		    // Encodage des param�tres de la requ�te
    		    String donnees = URLEncoder.encode("action", "UTF-8")+ "="+URLEncoder.encode("option", "UTF-8");
    		   // donnees += "&"+URLEncoder.encode("video", "UTF-8")+ "=" + URLEncoder.encode("1", "UTF-8");
				  Log.d("MessageDebugTest", donnees);//logs

    		    // On a envoy� les donn�es � une adresse distante
    		    URL url = new URL ("http://82.240.19.22:8383/yana-server/plugins/detect_porte/detectporte.API.php?action="+_link);
    		    connexion = url.openConnection();
    		    connexion.setDoOutput(true);
    		    //connexion.setChunkedStreamingMode(0);

				  //test chargement !!!!!!!!!!!!!!!!!!!!!!!!!!! A ENLEVER
				  mWebView = (WebView) findViewById(R.id.webview);
				  mWebView.loadUrl("http://82.240.19.22:8383/yana-server/plugins/detect_porte/detectporte.API.php?action="+_link);
				  ////////////////////////////////////////////////////

				  // On envoie la requ�te ici
    		    writer = new OutputStreamWriter(connexion.getOutputStream());

    		    // On ins�re les donn�es dans notre flux
    		    writer.write(donnees);

    		    // Et on s'assure que le flux est vid�
    		    writer.flush();

    		    // On lit la r�ponse ici
    		    reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
    		    String ligne;
    		    

    		    // Tant que � ligne � n'est pas null, c'est que le flux n'a pas termin� d'envoyer des informations
    		    while ((ligne = reader.readLine()) != null) {
    		    	connected=1; //flag de connection

    		    	if(ligne.equalsIgnoreCase("1") && _action.equals("updtOptions") ){

    		     		  runOnUiThread(new Runnable() {    		    			  
    		                  @Override
    		                  public void run() {
    		                      TextView t = (TextView) findViewById(R.id.TextSave);
    		             t.setText("Saved");
    		                  }});
    		    	}else if (ligne.equalsIgnoreCase("0") && _action.equals("getOptions")){
    		    		//affichage des checkbox en fonction de l'info qu'on r�cup�re
    		    		if ( _link.equals("showvideo")){
    		    			runOnUiThread(new Runnable() {    		    			  
    	  		                  @Override
    	  		                  public void run() {
    	  		                	CheckBox checkvideo = (CheckBox)findViewById(R.id.checkvideo);
    	  		                	checkvideo.setChecked(false);
    	  		                  }});
    	    		    }else if ( _link.equals("showsms")){
    	    		    	runOnUiThread(new Runnable() {    		    			  
    	  		                  @Override
    	  		                  public void run() {
    	  		                	CheckBox checkSMS = (CheckBox)findViewById(R.id.checkSMS);
    	  		                	checkSMS.setChecked(false);
    	  		                  }});
    	    		    }else if ( _link.equals("showlumiere")){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								CheckBox checklumiere = (CheckBox)findViewById(R.id.checklumiere);
								checklumiere.setChecked(false);
							}});
						}else if ( _link.equals("showstopauto")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									CheckBox checkstopauto = (CheckBox)findViewById(R.id.checkstopauto);
									checkstopauto.setChecked(false);
								}});
						}else if ( _link.equals("showmail")){
    	    		    	runOnUiThread(new Runnable() {    		    			  
    	  		                  @Override
    	  		                  public void run() {
    	  		                	CheckBox checkmail = (CheckBox)findViewById(R.id.checkmail);
    	  		                	checkmail.setChecked(false);
    	  		                  }});
    	    		    }else if ( _link.equals("showtele")){
    	    		    	runOnUiThread(new Runnable() {    		    			  
    	  		                  @Override
    	  		                  public void run() {
    	  		                	CheckBox checktv = (CheckBox)findViewById(R.id.checktv);
    	  		                	checktv.setChecked(false);
    	  		                  }});
    	    		    }
    		    	}else if (ligne.equalsIgnoreCase("1") && _action.equals("getStatus")){// Si c'est en Running
    		    		runOnUiThread(new Runnable() {		    			  
  		                  @Override
  		                  public void run() {
  		                      TextView DetectStatus = (TextView) findViewById(R.id.DetectStatus);
  		                    DetectStatus.setText("Status : Running");//maj du statut
  		                  ImageView StatusButton = (ImageView)findViewById(R.id.imageStatus);       
  		            	StatusButton.setImageResource(R.drawable.allumer);//maj de l'image du statut
							  StatusButton.setImageDrawable(getResources().getDrawable(R.drawable.allumer));
							  Log.d("MessageDebugTest", "test1");//logs
  		                  }});
    		    	}else if (ligne.equalsIgnoreCase("0") && _action.equals("getStatus")){ //Si C'est Stopped
    		    		runOnUiThread(new Runnable() {		    			  
    		                  @Override
    		                  public void run() {
    		                      TextView DetectStatus = (TextView) findViewById(R.id.DetectStatus);
    		                    DetectStatus.setText("Status : Stopped");//maj du statut
    		                    ImageView StatusButton = (ImageView)findViewById(R.id.imageStatus);       
    	    		            	StatusButton.setImageResource(R.drawable.eteint);//maj de l'image du statut
								  StatusButton.setImageDrawable(getResources().getDrawable(R.drawable.eteint));
								  Log.d("MessageDebugTest", "test2");//logs
    		                  }});
      		    	}
    		    	else
  		     		  runOnUiThread(new Runnable() {		    			  
		                  @Override
		                  public void run() {
		                      TextView t = (TextView) findViewById(R.id.TextSave);
		             t.setText("Not Saved");
		                  }});
    		      Log.d("MessageDebugTest", "Done");
    		    }
    		  } catch (Exception e) {
    		    e.printStackTrace();
    		  } finally {
    		    try{writer.close();}catch(Exception e){}
    		    try{reader.close();}catch(Exception e){}
    		    try{/*connexion.disconect();*/ /* TextSave.setText("error");*/}catch(Exception e){}
    		  } 
    		  //TextSave.setText(status);
    		  if(connected==0){
		    		runOnUiThread(new Runnable() {  		    			  
	                  @Override
	                  public void run() {
	                      TextView t = (TextView) findViewById(R.id.TextSave);
	                      t.setText("Impossible to connect");
	                      TextSave.setText("error");
	                  }});
		    	}
 
    		  
    	  }

		public String getLink() {
			return _link;
		}

		public void setLink(String _link) {
			this._link = _link;
		}
    }

    public class ThreadWaitingStatus extends Thread {
    	    	
    }
    


    
    
}
