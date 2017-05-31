package com.example.joonas.soap_example;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;

import java.util.ArrayList;
import java.util.List;

import static com.example.joonas.soap_example.R.id.none;
import static com.example.joonas.soap_example.R.id.parent;
import static com.example.joonas.soap_example.R.id.textView4;


public class MainActivity extends AppCompatActivity {

    EditText textBox;
    Spinner spinner;
    Spinner spinner2;
    TextView returnVTextView;

    String URL = "https://apinf.io:3002/Temperature_Unit_Convertor/ConvertTemperature.asmx?WSDL";
    String NAMESPACE = "http://www.webservicex.net/";
    String SOAP_ACTION = "http://www.webserviceX.NET/ConvertTemp";
    String METHOD_NAME = "ConvertTemp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tempUnit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.tempUnit, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        textBox = (EditText)findViewById(R.id.textbox);
        returnVTextView = (TextView) findViewById(R.id.textView4);

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallWebService().execute(textBox.getText().toString(),
                        spinner.getSelectedItem().toString(),spinner2.getSelectedItem().toString());
            }
        });
    }
    class CallWebService extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            returnVTextView.setText(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            SoapObject soapObject = new SoapObject( "http://www.webserviceX.NET/",
                    "ConvertTemp");

            PropertyInfo propertyInfoTemp = new PropertyInfo();
            propertyInfoTemp.setName("Temperature");
            propertyInfoTemp.setValue(params[0]);
            propertyInfoTemp.setType(String.class);

            soapObject.addProperty(propertyInfoTemp);

            PropertyInfo propertyInfoFUnit = new PropertyInfo();
            propertyInfoFUnit.setName("FromUnit");
            propertyInfoFUnit.setValue(params[1]);
            propertyInfoFUnit.setType(String.class);

            soapObject.addProperty(propertyInfoFUnit);

            PropertyInfo propertyInfoTUnit = new PropertyInfo();
            propertyInfoTUnit.setName("ToUnit");
            propertyInfoTUnit.setValue(params[2]);
            propertyInfoTUnit.setType(String.class);

            soapObject.addProperty(propertyInfoTUnit);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);
            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                result = envelope.getResponse().toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = e.toString();
            }
            return result;
        }
    }
}