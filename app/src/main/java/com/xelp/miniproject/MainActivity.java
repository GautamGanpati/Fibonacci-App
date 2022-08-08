package com.xelp.miniproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText item1,item2;
    Button add;
    ListView listView;
    ArrayList<String>itemlist=new ArrayList<>();
    ArrayAdapter<String>arrayAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item1=findViewById(R.id.editTextName);
        item2=findViewById(R.id.editTextMobile);

        add=findViewById(R.id.button);
        listView=findViewById(R.id.list);

        mAuth = FirebaseAuth.getInstance();

        itemlist = com.xelp.miniproject.FileHelper.readData(this);

        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,itemlist);
        listView.setAdapter(arrayAdapter);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
              String itemName="";

                if(TextUtils.isEmpty(item1.getText().toString()) || TextUtils.isEmpty(item2.getText().toString()) ){
//                    itemName="Text Fields cannot be empty";
                    Toast. makeText(getApplicationContext(), "Text fields cannot be empty...", Toast.LENGTH_SHORT).show();
                    return;
                }

                int num1=Integer.parseInt(item1.getText().toString());
                int num2=Integer.parseInt(item2.getText().toString());



                itemName=fibonacci(num1,num2);

                itemlist.add(itemName);

                item1.setText("");
                item2.setText("");

                com.xelp.miniproject.FileHelper.writeData(itemlist,getApplicationContext());
                arrayAdapter.notifyDataSetChanged();


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete");
                alert.setCancelable(false);
                alert.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        itemlist.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        com.xelp.miniproject.FileHelper.writeData(itemlist,getApplicationContext());
                    }
                });
                AlertDialog alertDialog=alert.create();
                alertDialog.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.idLogOut:
                Toast.makeText(this,"User Logged Out..",Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private static String fibonacci(int n1, int n2){
        int sum, i,j=11;
        StringBuilder sb=new StringBuilder();
        if(n2==0 && n1==0){
            return "Both Number Can't be 0";
        }
        for(i = 2; i <=j; i++)
        {
            sum = n1 + n2;
            n1 = n2;
            n2 = sum;
            sb.append(sum+" ");

        }
        return sb.toString();
    }

}