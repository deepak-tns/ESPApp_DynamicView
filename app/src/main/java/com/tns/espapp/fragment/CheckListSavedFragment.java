package com.tns.espapp.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tns.espapp.R;
import com.tns.espapp.adapter.CheckListAdapterListview;
import com.tns.espapp.adapter.CheckListAdapterListview_Save;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_WORLD_WRITEABLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListSavedFragment extends Fragment implements View.OnClickListener {
    private static  String INSERT  ;
    public static final String mypreference = "chk_test";
    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private DatabaseHandler db;
    private CheckListAdapterListview_Save lstadapter;
    List<ChecklistData> getFormlist;
    List<String> saveFormlist = new ArrayList<>();
    ArrayList <String>  listkey;

    int b_id = 100;
   private final int ED_ID = 101;
    int rb_id = 102;
    int cb_id = 103;

    int t_id = 104;
    Button   btn_send;

    List<EditText> allEds = new ArrayList<EditText>();
    List<RadioGroup> allRGs = new ArrayList<RadioGroup>();

    HashMap<String,List<EditText>> hashMap_edt = new HashMap();
    HashMap<String,List<RadioGroup>> hashMap_rg = new HashMap();

    HashMap<Integer,String> hashMap = new HashMap<>();
    SQLiteDatabase  myDataBase;
    private SQLiteStatement insertStmt;

    SharedPreferences sharedpreferences;

    String setFormname;
    public CheckListSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_list_saved, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listview_chk);
        TextView textview =(TextView)v.findViewById(R.id.set_formane);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        btn_send =(Button)v.findViewById(R.id.btn_send) ;
        btn_send.setOnClickListener(this);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);


        db = new DatabaseHandler(getActivity());
        setFormname = getArguments().getString("PARAM1");

        getFormlist = db.getAllChecklistwithFormno(setFormname);
        textview.setText(setFormname);

     //   db. deleteFormcheckListData(setFormname);

       // clear_form_shared_pre();
      /*  String gettext = getForm_pre(setFormname);
        if(gettext.equals("1")) {
            ArrayList<HashMap<String, String>> mapget = db.getForm(setFormname);

            int size = mapget.size();

            if (size > 0) {
                for (HashMap<String, String> map : mapget)
                    for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                        String key = mapEntry.getKey();
                        String value = mapEntry.getValue();
                         saveFormlist.add(value);
                      //  Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
                    }

              //  initLinearBindsave(v, getFormlist, saveFormlist);
              //  btn_send.setVisibility(View.GONE);

            }
        }else
            {
          //  initLinearBind(v, getFormlist);

        }*/

        initLinearBind(v, getFormlist);


      /*  lstadapter = new CheckListAdapterListview_Save(getActivity(), R.layout.checklist_data_adapter, getFormlist);
        listView.setAdapter(lstadapter);
        lstadapter.notifyDataSetChanged();*/




        return v;
    }


    public void initLinearBind(final View view, List<ChecklistData> checklistDataList) {
           listkey = new ArrayList<>();
        int size = checklistDataList.size();
        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
        for (int i = 0; i < checklistDataList.size(); i++) {
            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(checklistDataList.get(i).getName_value());
            tv3.setTextSize(16);
            tv3.setTextColor(Color.BLACK);
            tv3.setId(t_id + 1);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            allEds.add(etv3);

            hashMap.put(i, checklistDataList.get(i).getName());
            listkey.add( checklistDataList.get(i).getName());


            hashMap_edt.put(checklistDataList.get(i).getName(),allEds);

            etv3.setLayoutParams(edtParams_4);
          //  etv3.setHint(checklistDataList.get(i).getName());
            etv3.setTextSize(16);
            etv3.setTextColor(Color.BLACK);
            etv3.setId( i);



            // etv3.setFocusable(false);


            RadioGroup rg = new RadioGroup(getActivity());
            LinearLayout.LayoutParams rg_group_param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            rg.setLayoutParams(rg_group_param);
            allRGs.add(rg);
            hashMap_rg.put(checklistDataList.get(i).getName(),allRGs);
            hashMap.put(i, checklistDataList.get(i).getName());
            rg.setOrientation(LinearLayout.HORIZONTAL);
            rg.setId(i );

            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setText("Yes " );
            rdbtn.setTextSize(16);
            rdbtn.setChecked(false);



            RadioButton rdbtn2 = new RadioButton(getActivity());
            rdbtn2.setText("No " );
            rdbtn2.setTextSize(16);

            rg.addView(rdbtn);
            rg.addView(rdbtn2);

        /*

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // int id=group.getCheckedRadioButtonId();
                    //  RadioButton rb=(RadioButton) findViewById(id);
                    RadioButton rb=(RadioButton) view. findViewById(checkedId);
                    String radioText=rb.getText().toString();


                }
            });


            */

            LL3.addView(tv3);

            if (checklistDataList.get(i).getDataType().equalsIgnoreCase("Numeric")) {

                etv3.setInputType(InputType.TYPE_CLASS_NUMBER);

            }

            if (checklistDataList.get(i).getDataType().equalsIgnoreCase("Date")) {

              //  etv3.setText(dateFormatter.format(cal.getTime()));
                etv3.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_my_calendar, 0);
                etv3.setFocusable(false);
                etv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setdate(etv3);
                    }
                });
            }

            if (checklistDataList.get(i).getDataType().equalsIgnoreCase("Boolean")) {

               /* if (rg.getParent() != null)
                    ((ViewGroup) rg.getParent()).removeView(rg);*/
                LL3.addView(rg);
            } else {
                LL3.addView(etv3);
            }


            li1.addView(LL3);


        }

        listkey.add("Status");
        createDynamicDatabase(getActivity(),setFormname,listkey);


    }
    public void initLinearBindsave(final View view, List<ChecklistData> checklistDataList,List<String> savelist) {
        int size = checklistDataList.size();
        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
        for (int i = 0; i < checklistDataList.size(); i++) {
            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(checklistDataList.get(i).getName_value());
            tv3.setTextSize(16);
            tv3.setTextColor(Color.BLACK);
            tv3.setId(t_id + 1);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            allEds.add(etv3);

            hashMap.put(i, checklistDataList.get(i).getName());
            hashMap_edt.put(checklistDataList.get(i).getName(),allEds);

            etv3.setLayoutParams(edtParams_4);
            //  etv3.setHint(checklistDataList.get(i).getName());
            etv3.setTextSize(16);
            etv3.setTextColor(Color.BLACK);
            etv3.setId( i);

            String txt = savelist.get(i).toString();
            etv3.setText(txt);
            etv3.setFocusable(false);



            // etv3.setFocusable(false);


            RadioGroup rg = new RadioGroup(getActivity());
            LinearLayout.LayoutParams rg_group_param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            rg.setLayoutParams(rg_group_param);
            allRGs.add(rg);
            hashMap_rg.put(checklistDataList.get(i).getName(),allRGs);
            hashMap.put(i, checklistDataList.get(i).getName());
            rg.setOrientation(LinearLayout.HORIZONTAL);
            rg.setId(i );

            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setText("Yes " );
            rdbtn.setTextSize(16);
            rdbtn.setChecked(false);



            RadioButton rdbtn2 = new RadioButton(getActivity());
            rdbtn2.setText("No " );
            rdbtn2.setTextSize(16);

            rg.addView(rdbtn);
            rg.addView(rdbtn2);

        /*
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // int id=group.getCheckedRadioButtonId();
                    //  RadioButton rb=(RadioButton) findViewById(id);
                    RadioButton rb=(RadioButton) view. findViewById(checkedId);
                    String radioText=rb.getText().toString();


                }
            });

            */

            LL3.addView(tv3);
            LL3.addView(etv3);
            li1.addView(LL3);


        }


    }

    private void setdate(final EditText edt) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                   /*   addmeeting_edt_selectdate.setText(dayOfMonth + "-"
                              + dateFormatter.format (monthOfYear + 1) + "-" + year);*/
                        cal.set(year, monthOfYear, dayOfMonth);

                        edt.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }


    @Override
    public void onClick(final View v) {

        boolean b = true;

        final ArrayList<String>  listvalue = new ArrayList<>();
        final String[] strings = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++){
            String s = hashMap.get(i);
            strings[i] = allEds.get(i).getText().toString();

            if(strings[i].equals("")){
                Toast.makeText(getActivity(),"Field can't be empty",Toast.LENGTH_LONG).show();
                b = false;
                break;
            }else {
                b = true;
            }
           Toast.makeText(getActivity(),strings[i]+","+ s,Toast.LENGTH_LONG).show();

           // listkey.add( s);
            listvalue.add(strings[i]);


        }

       /* for(int i=0 ; i<allRGs.size() ; i++)
        {
          final String finalS =hashMap.get(i);
            int radioBtnChecked = allRGs.get(i).getCheckedRadioButtonId();
            RadioButton rb=(RadioButton)getActivity(). findViewById(radioBtnChecked);
           allRGs.get(i).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    RadioButton rb=(RadioButton)getActivity(). findViewById(checkedId);
                    String radioText=rb.getText().toString();
                //   Toast.makeText(getActivity(),radioText +","+ finalS,Toast.LENGTH_LONG).show();

                }
            });



          //  Toast.makeText(getActivity(),rb.getText().toString() +","+ finalS,Toast.LENGTH_LONG).show();

        }
*/
       if(b) {
           listkey.add("0");
           insert(getActivity(), listvalue, listkey, setFormname);
           for(int i=0; i < allEds.size(); i++)
           {
               allEds.get(i).setText("");
           }

       }
     saveForm_pre(setFormname,"1");
      //  btn_send.setFocusable(false);


    }
    public void createDynamicDatabase(Context context, String tableName, ArrayList<String> title) {

        try {

            int i;
            String querryString = null;
            myDataBase = context.openOrCreateDatabase( "/mnt/sdcard/my.db", MODE_WORLD_WRITEABLE, null);
            if(title.size()==1) {//Opens database in writable mode.
                querryString = title.get(0) + " text";
            }
            else if(title.size() ==2) {
                querryString = title.get(0)+" text,";
                querryString+= title.get(1) +" text";
            }
            else{
                querryString = title.get(0)+" text,";
                for(i=1;i<title.size()-1;i++)
                {
                    querryString += title.get(i);
                    querryString +=" text";
                    querryString +=",";
                }
                querryString+= title.get(i) +" text";
            }


            querryString = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + querryString + ");";

          //  Toast.makeText(context, querryString, Toast.LENGTH_LONG).show();

            myDataBase.execSQL(querryString);
           // Toast.makeText(context, "Execute Query", Toast.LENGTH_LONG).show();

        } catch (SQLException ex) {
            Log.e(ex.getClass().getName(), ex.getMessage(), ex);

        }
    }

    public void insert(Context context, ArrayList<String> array_vals, ArrayList<String> title, String TABLE_NAME) {
        try {


            myDataBase = context.openOrCreateDatabase("/mnt/sdcard/my.db", MODE_WORLD_WRITEABLE, null);         //Opens database in writable mode.
            String titleString = null;
            String markString = null;
            int i;
            if(title.size() ==1)
            {
                titleString = title.get(0) ;
                markString = "?";
            }
            else if(title.size() ==2)
            {
                titleString = title.get(0) + ",";
                markString = "?,";
                titleString+= title.get(1) +" text";
                markString+="?";
            }
            else {

                titleString = title.get(0) + ",";
                markString = "?,";

                for (i = 1; i < title.size() - 1; i++) {
                    titleString += title.get(i);
                    titleString += ",";
                    markString += "?,";
                }
                titleString += title.get(i);
                markString += "?";
            }
            INSERT = "insert into " + TABLE_NAME + "(" + titleString + ")" + "values" + "(" + markString + ")";
            int s = 0;

            while (s < array_vals.size()) {

                System.out.println("Size of array1" + array_vals.size());
                int j = 1;
                this.insertStmt = this.myDataBase.compileStatement(INSERT);
                for (int k = 0; k < title.size(); k++) {


                    insertStmt.bindString(j, array_vals.get(k + s));


                    j++;
                }

                s += title.size();

            }
            insertStmt.executeInsert();
        } catch (SQLException ex) {
            Log.e(ex.getClass().getName(), ex.getMessage(), ex);
        }


    }

    public void saveForm_pre(String key,String value ){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public String getForm_pre(String key ){

         String gets =  sharedpreferences.getString(key, "");
       return gets;
    }

    public void clear_form_shared_pre(){
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.clear();
        editor.commit();
    }

}