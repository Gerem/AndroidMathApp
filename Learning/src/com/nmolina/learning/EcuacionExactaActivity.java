package com.nmolina.learning;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EcuacionExactaActivity extends Activity {
	private EditText firstEditView;
	private EditText secondEditView;	
	private ProgressDialog pb;
	private String queryFunction;
	private LinearLayout mainLayout;
	//Results views.
	private TextView mResult;
	private TextView nResult;
	private TextView isExact;
	private int id=1;
	String result = "";
	String secondView;
	//WAEngine engine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecuacion_exacta);
        mainLayout = (LinearLayout)findViewById(R.id.LinearLayout1);
        firstEditView = (EditText)findViewById(R.id.first_num_editText);        
        mResult = (TextView)findViewById(R.id.mResult);
        nResult = (TextView)findViewById(R.id.nResult);		
        isExact = (TextView)findViewById(R.id.isExact);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void calculate(View view){    	
    	pb = ProgressDialog.show(this, "Calculating", "Please wait..");
    	String mQuery = firstEditView.getText().toString().split("\\+")[0]; 
    	getXML("d/dy(" + mQuery.replace("dx", "")+")");
    	
    	
    	String nQuery = firstEditView.getText().toString().split("\\+")[1]; 
    	getXML("d/dx(" + nQuery.replace("dy", "") +")");
//    	nResult.setText(result);
    }
    public void getXML(String query){
    	this.queryFunction = query;    	
    	Thread thread = new Thread()
    	{
    	    @Override
    	    public void run() {    	    	
    	    	webServiceConnect(queryFunction);    
    	    	handler.sendEmptyMessage(0);
    	    }
    	};    	
    	thread.start();	    	
    }
    //Connecting Webservice.
    public void webServiceConnect(String query){
    	
    	try {
        	String function = query.replace(" ", "%20");
        	NodeList nodeList = null;	    		
	    	URL sourceUrl = new URL("http://api.wolframalpha.com/v2/query?appid=64K52U-3YXE57PLG2&input=" +
	    							function +"&format=image,plaintext");
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder db = dbf.newDocumentBuilder();
	    	Document doc = db.parse(new InputSource(sourceUrl.openStream()));
	    	doc.getDocumentElement().normalize();
	    	nodeList = doc.getElementsByTagName("subpod");
    		Node node = nodeList.item(0);
    		Element element = (Element)node;
    		result = element.getElementsByTagName("plaintext").item(0).getChildNodes().item(0).getNodeValue();    	
        }catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if(id==1){
        		mResult.setText(result);
        		++id;
        	}else{
        		nResult.setText(result);
        		pb.dismiss();
        		id = 1;
        		if(mResult.getText().toString().split("=")[1].equals(nResult.getText().toString().split("=")[1])){
        			isExact.setText("EQUALS!");
        		}else{
        			isExact.setText("NOT EQUALS!");
        		}
        	}        	
        	
        	
        }
    };
}
