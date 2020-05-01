package com.example.convert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Конвертер валюты
public class MainActivity extends AppCompatActivity {

    String[] currency = {"RUB", "EUR", "USD", "GBP", "ALL", "XCD"};
    TextView finalEdit;
    Button button;
    EditText editText;
    TextView textView;
    String itemVal = "RUB", itemVal1 = "RUB";
    String edText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //selection = (TextView) findViewById(R.id.selection);
        //selectionF = (TextView) findViewById(R.id.selection_final);
        finalEdit = (TextView) findViewById(R.id.final_edit);
        editText = findViewById(R.id.source_edit);
        textView = findViewById(R.id.final_edit);
        button = findViewById(R.id.btn_ok);

        Spinner spinner = (Spinner) findViewById(R.id.source_spinner);
        Spinner spinnerF = (Spinner) findViewById(R.id.final_spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currency);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        spinnerF.setAdapter(adapter);

        //Отображение выпадающего списка валют
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                itemVal = (String) parent.getItemAtPosition(pos);
                //selection.setText(itemVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerF.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                itemVal1 = (String) parent.getItemAtPosition(pos);
                //selectionF.setText(itemVal1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Обработка нажатия на кнопку
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("")) {
                    edText = editText.getText().toString();
                    String adr = sumString(itemVal,itemVal1);
                    new Model().execute(adr);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    //Чтобы считать с сайта информацию по API, нужно создать ссылку
    public String sumString (String itemV, String itemV1)
    {
        String adr1 = new String("https://free.currconv.com/api/v7/convert?q=");
        String adr2 = new String("&compact=ultra&apiKey=e4e5e3d4047fa6dff94d/");

        String adr = adr1 + itemV + "_" + itemV1 + adr2;
        System.out.println(adr);
        return adr;
    }

    //Нужно перевести данные в тип double, затем обработать, и вернуть обратно в String для вывода на экран
    public String parseVal (String edText, String val)
    {
        double dVal=1, dText=1;
        try {
            dVal = Double.parseDouble(val);
            dText = Double.parseDouble(edText.trim());

        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        }

        dVal = dVal*dText;       //Перемножаем курс валюты и количество, введенное пользователем
        val = Double.toString(dVal);

        // int index1 = valyta.indexOf('.');
        //valyta = valyta.substring(0, index1 + 3);
        return val;
    }

    //Вызываем параллельный поток, который считает данные с сайта по API
    public class Model extends AsyncTask<String, Void, String> {

        String server_response;
        String valyta;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                    valyta = server_response.substring(11, server_response.length() - 1);
                    Log.v("valyta", valyta);

                    //finalEdit.setText(valyta);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return valyta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Нужно обработать полученные данные от пользователя и по API
            valyta = parseVal(edText,valyta);
            finalEdit.setText(valyta);
            Log.e("Response", "" + server_response);

        }

        // Конвертируем InputStream в String
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }

    }
}

