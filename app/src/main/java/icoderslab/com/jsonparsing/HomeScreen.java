package icoderslab.com.jsonparsing;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity {
    private String classtag= HomeScreen.class.getSimpleName();  //return name of underlying class
    private ListView lv;
    private ProgressDialog progress;
    private String url="https://raw.githubusercontent.com/mobilesiri/JSON-Parsing-in-Android/master/index.html"; //passing url
    ArrayList<HashMap<String,String>> studentslist; //arraylist to save key value pair from json
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        studentslist=new ArrayList<>();
        lv= (ListView) findViewById(R.id.list); //from home screen list view
       new getStudents().execute(); // it will execute your AsyncTask
    }
//--------------------------//-------------------------------//---------------------//
    public class getStudents extends AsyncTask<Void,Void,Void> {
        protected void onPreExecute(){
            super.onPreExecute(); //it will use pre defined preExecute method in async task
            progress=new ProgressDialog(HomeScreen.this);
            progress.setMessage("Fetching JSON.,."); // show what you want in the progress dialog
            progress.setCancelable(false); //progress dialog is not cancellable here
            progress.show();
        }
        protected Void doInBackground(Void...arg0){
            HTTP_Handler hh = new HTTP_Handler(); // object of HTTP_Handler
            String jString = hh.makeHTTPCall(url); //calling makeHTTPCall method and string its response in a string
            Log.e(classtag, "Response from URL: " + jString);
            if (jString != null) {
                try {
                    JSONObject jObj = new JSONObject(jString); //our json data starts with the object
                    JSONArray students = jObj.getJSONArray("studentsinfo"); //fetch array from studentsinfo object
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i); //get object from i index
                        String id=student.getString("id");   //save string from variable 'id' to string
                        String name=student.getString("name");
                        String email=student.getString("email");
                        String address=student.getString("address");
                        String gender=student.getString("gender");

                        JSONObject phone=student.getJSONObject("phone"); //get object from phone
                        String mobile=phone.getString("mobile");  //save string from variable 'mobile' to string
                        String home=phone.getString("home");
                        String office=phone.getString("office");

                        HashMap<String, String> studentdata = new HashMap<>(); //create a hash map to set key and value pair

                        studentdata.put("id", id); //hash map will save key and its value
                        studentdata.put("name", name);
                        studentdata.put("email", email);
                        studentdata.put("address", address);
                        studentdata.put("gender",gender);
                        studentdata.put("mobile", mobile);
                        studentdata.put("home", home);
                        studentdata.put("office", office);
                        studentslist.add(studentdata); //now save all of the key value pairs to arraylist
                    }
                } catch (final JSONException e) {
                    Log.e(classtag, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show(); //show if you catch any exception with data
                        }
                    });
                }
            } else {
                Log.e(classtag, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check internet connection!",
                            Toast.LENGTH_LONG).show();//show if you are unable to connect with the internet or if jString is null
                    }
                });
            }
            return null;
        }
        protected void onPostExecute(Void Result){
            super.onPostExecute(Result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            ListAdapter adapter=new SimpleAdapter(
                    HomeScreen.this,
                    studentslist,
                    R.layout.bucket_list,
                    new String[]{"name","email","address","gender","mobile","home","office"},
                    new int[]{R.id.list_Name,R.id.list_Email,R.id.list_Address ,R.id.list_Gender,R.id.list_Mobile,R.id.list_Home,R.id.list_Office});
            lv.setAdapter(adapter);
//            SimpleAdapter (Context context,
//                    List<? extends Map<String, ?>> data,
//            int resource,
//            String[] from,
//            int[] to)
            //this will pass your json values to the bucket_list xml and whatever it is stored of key 'name' will be
            // displayed to text view list_Name
        }
    }
}

