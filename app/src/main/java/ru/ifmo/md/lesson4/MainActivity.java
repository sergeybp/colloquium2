package ru.ifmo.md.lesson4;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";

    Boolean inProgress = false;
    Button btnAdd, btnRead, btnClear;
    EditText etName;

    ListView list;
    DBHelper dbHelper;

    ArrayAdapter<String> condidates;
    ArrayList<String> cond = new ArrayList<String>();
    ArrayList<Integer> points = new ArrayList<Integer>();
    ArrayList<Double> percent = new ArrayList<Double>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ac);
        list = (ListView)findViewById(R.id.listView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);



        etName = (EditText) findViewById(R.id.etName);
        dbHelper = new DBHelper(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!inProgress){
                    Toast.makeText(getApplicationContext(),
                            "Press Start", Toast.LENGTH_SHORT).show();
                    return;
                }

                points.set(i,points.get(i)+1);
                double x = 0.0;
                for(int k = 0; k < percent.size();k++){
                    x+=points.get(k);
                }
                for(int  k = 0 ; k < percent.size();k++) {
                    percent.set(k, points.get(k) / x);
                }
                for(int k = 0; k < cond.size();k++) {
                    String a = cond.get(k);
                    String[] splits = a.split(" ");
                    a = "";
                    for (int j = 0; j < splits.length - 1; j++) {
                        a += splits[j];
                    }
                    a += " (" + points.get(k) + ")(" + percent.get(k) + ")";
                    cond.set(k, a);
                }
                condidates = new ArrayAdapter<String>(MainActivity.this,R.layout.item,cond);
                list.setAdapter(condidates);





            }

        });
    }





    @Override
    public void onClick(View v) {





        String name = etName.getText().toString();


        switch (v.getId()) {
            case R.id.btnAdd:
                if(inProgress){
                    Toast.makeText(getApplicationContext(),
                            "Not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }


                points.add(0);
                percent.add(0.0);
                cond.add(name+" (0)(0)");
                points.add(0);
                condidates = new ArrayAdapter<String>(this,R.layout.item,cond);
                list.setAdapter(condidates);
                break;
            case R.id.btnRead:
                if(inProgress){
                    Toast.makeText(getApplicationContext(),
                            "Not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                inProgress = true;
                break;
            case R.id.btnClear:
                if(!inProgress){
                    Toast.makeText(getApplicationContext(),
                            "Not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }


                cond.clear();
                points.clear();
                percent.clear();
                condidates = new ArrayAdapter<String>(this,R.layout.item,cond);
                list.setAdapter(condidates);
                inProgress = false;

                break;

        }

        dbHelper.close();
    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "score text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}