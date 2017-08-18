package com.tns.espapp.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.AttachmentData;
import com.tns.espapp.CaptureData;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.R;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.FeedbackRecordData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */

public class GetCheckListSavedFragment extends Fragment {

    private int setSize =0;

    private TableLayout tl;
    private TableRow tr;
    private CheckBox cb;
    private CheckBox chk_selectall;
    private Button sendButton;

    private ArrayList<String> checkedValueArrayList = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();


    String key;
    public static final String mypreference = "chk_test";
    List<String> valueFormlist = new ArrayList<>();


    String setFormname;
    DatabaseHandler db;
    List<String> keyFormlist = new ArrayList<>();

    List<String> keyFormlist_holder = new ArrayList<>();


    List<ChecklistData> getFormlist;

    SharedPreferences sharedpreferences;


    public GetCheckListSavedFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_check_list_saved, container, false);
        TextView textview = (TextView) v.findViewById(R.id.set_formane);
        db = new DatabaseHandler(getActivity());
        chk_selectall =(CheckBox)v.findViewById(R.id.chk_selectall);
        sendButton = (Button)v.findViewById(R.id.sendButton);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        setFormname = getArguments().getString("PARAM1");

        getFormlist  = db.getAllChecklistwithFormno(setFormname);
        for(ChecklistData chk : getFormlist){

            keyFormlist_holder.add(chk.getName_value());
            Log.v("keyHolder",chk.getName_value());
        }

        textview.setText(setFormname);
        String gettext = getForm_pre(setFormname);

        if (gettext.equals("1")) {
            ArrayList<HashMap<String, String>> mapget = db.getForm(setFormname);

            int size = mapget.size();

            if (size > 0) {
                for (HashMap<String, String> map : mapget) {

                    for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                        key = mapEntry.getKey();
                        String value = mapEntry.getValue();
                        valueFormlist.add(value);
                        keyFormlist.add(key);
                    }

                }

                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(keyFormlist); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

                keyFormlist.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
                keyFormlist.addAll(primesWithoutDuplicates);


                keyFormlist_holder.add("Status");

                setView(v, keyFormlist_holder, valueFormlist);



            } else {


            }

        }
        chk_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                selectAllCheckBox(isChecked);

            }
        });

        sendButtonClickListener();

        return v;
    }

    public void setView(View v, List<String> key, List<String> value) {

        tl = (TableLayout) v.findViewById(R.id.main_table);
        TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < key.size(); i++) {

            TextView label_date = new TextView(getActivity());
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            label_date.setLayoutParams(params);
            label_date.setGravity(Gravity.CENTER);
            label_date.setBackgroundResource(R.drawable.edit_text_design_table);
            label_date.setId(20 + i);
            label_date.setPadding(0, 5, 0, 5);
            label_date.setText(key.get(i));
            label_date.setTextColor(Color.WHITE);

            tr_head.addView(label_date);
            // add the column to the table row here

        }

        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        Integer count = 0;
        TextView labelDATE = null;
        int getcount = value.size();
        int setCounter = 0;
        while (getcount > 0) {
            tr = new TableRow(getActivity());
            tr.setId(100 + getcount);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            for (int k = 0; k < key.size(); k++) {

                labelDATE = new TextView(getActivity());

                LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                labelDATE.setLayoutParams(params);
                labelDATE.setGravity(Gravity.CENTER);
                labelDATE.setId(200 + setCounter);
                labelDATE.setBackgroundResource(R.drawable.edit_text_design);
                String getStr = value.get(setCounter);

                if (getStr.equalsIgnoreCase("No")||getStr.equalsIgnoreCase("0")) {
                    cb = new CheckBox(getActivity());
                    LinearLayout.LayoutParams cb_params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    cb.setLayoutParams(cb_params);
                    cb.setBackgroundResource(R.drawable.edit_text_design);
                    cb.setId(500 + getcount);
                    checkBoxArrayList.add(cb);
                    tr.addView(cb);

                } else {
                    labelDATE.setText(value.get(setCounter));
                    labelDATE.setPadding(0, 15, 0, 15);
                    labelDATE.setTextColor(Color.BLACK);
                    tr.addView(labelDATE);
                }


                setCounter++;
            }


            tl.addView(tr, new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            getcount = getcount - key.size();
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    for (int i = 0; i < tr.getChildCount(); i++) {
                        TextView companyTV, valueTV, yearTV;
                        String company = null, value, year;
                        final View view = tr.getChildAt(i);
                        if (view instanceof TextView) {
                            companyTV = (TextView) tr.getChildAt(i);


                        }
                        if (view instanceof CheckBox) {

                        }


                    }
                }
            });
            count++;

        }

        //   Utility.displayMessage(getContext(),""+checkBoxArrayList.size());

// finally add this to the table row

    }


    public String getForm_pre(String key) {

        String gets = sharedpreferences.getString(key, "");
        return gets;
    }

    private void selectAllCheckBox(boolean status)
    {
        for(int i=0;i<checkBoxArrayList.size();i++)
        {
            CheckBox checkBox =checkBoxArrayList.get(i);
            if(status){
                checkBox.setChecked(true);

            }
            else {
                checkBox.setChecked(false);

            }
        }

    }


    private void sendButtonClickListener()
    {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = null;

                checkedValueArrayList.clear();

                for (int i = 1; i < tl.getChildCount(); i++) {
                    View child = tl.getChildAt(i);

                    if (child instanceof TableRow) {

                        TableRow row = (TableRow) child;
                        for (int x = 0; x < row.getChildCount(); x++) {

                            View view = row.getChildAt(x);

                            if(view instanceof  CheckBox)
                            {
                                CheckBox checkBox= (CheckBox)row.getChildAt(x);
                                if(checkBox.isChecked())
                                {
                                    ViewGroup viewGroup=(ViewGroup)checkBox.getParent();
                                    TableRow tableRow =(TableRow) viewGroup;
                                    for (int j =0;j<tableRow.getChildCount();j++)
                                    {
                                        View view1 = tableRow.getChildAt(j);
                                        if(view1.getClass().getName().equalsIgnoreCase("android.widget.Textview"))
                                        {
                                            data = ((TextView)row.getChildAt(j)).getText().toString();
                                            checkedValueArrayList.add(data);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                printCheckedValue();
                createJSON(keyFormlist,checkedValueArrayList);

            }

        });

    }
    private void printCheckedValue()
    {


        for(int i =0;i<checkedValueArrayList.size();i++)
        {

          //  Utility.displayMessage(getContext(),checkedValueArrayList.get(i));
        }
    }


    private void createJSON(List<String> key, List<String> value) {
        int count =0;
        key.removeAll(Arrays.asList("Status"));
             setSize = value.size()/key.size();


        final JSONObject jsonObject = new JSONObject();

        try {

            JSONArray obj = new JSONArray();
            try {
                for(int i = 0; i <setSize ; i++) {

                    // 1st object
                    JSONObject list1 = new JSONObject();
                           int   j=count;

                        for( j = 0; j <key.size(); j++) {


                            list1.put(key.get(j), value.get(count));
                            count++;
                        }

                    list1.put("Formno",setFormname);
                    obj.put(list1);
                }
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }


            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", setFormname );

            jsonObject.put("parameterList", obj);
            //jsonObject.put("values", jsonArrayParametervalue);

            Log.v("getJSON",jsonObject.toString());
             Toast.makeText(getActivity(),jsonObject.toString().toString(),Toast.LENGTH_LONG ).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




}





/*
{
        "FormName": [
        {
        "formName": "Form7",
        "fieldName": "testName",
        "fieldValue": "UserName",
        "dataType": "String",
        "size": 20,
        "decimal": 0
        },
        {
        "formName": "Form7",
        "fieldName": "testdob",
        "fieldValue": "DOB",
        "dataType": "Date",
        "size": 10,
        "decimal": 0
        },
        {
        "formName": "Form7",
        "fieldName": "testsalary",
        "fieldValue": "Salary",
        "dataType": "Numeric",
        "size": 10,
        "decimal": 2
        }
        ]
        }
*/



















































/*
public class GetCheckListSavedFragment extends Fragment {


    private TableLayout tl;
    private TableRow tr;
    private CheckBox cb;
    private CheckBox chk_selectall;

    String key;
    public static final String mypreference = "chk_test";
    List<String> valueFormlist = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();

    String setFormname;
    DatabaseHandler db;
    List<String> keyFormlist = new ArrayList<>();


    SharedPreferences sharedpreferences;
    public GetCheckListSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_get_check_list_saved, container, false);
        TextView textview =(TextView)v.findViewById(R.id.set_formane);
        db = new DatabaseHandler(getActivity());
        chk_selectall =(CheckBox)v.findViewById(R.id.chk_selectall);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        setFormname = getArguments().getString("PARAM1");
       // getFormlist = db.getAllChecklistwithFormno(setFormname);
        textview.setText(setFormname);


        String gettext = getForm_pre(setFormname);

        if(gettext.equals("1")) {
            ArrayList<HashMap<String, String>> mapget = db.getForm(setFormname);

            int size = mapget.size();

            if (size > 0) {
                for (HashMap<String, String> map : mapget) {

                    for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                         key = mapEntry.getKey();
                        String value = mapEntry.getValue();
                        valueFormlist.add(value);
                        keyFormlist.add(key);



                        //  Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
                    }

                }



                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(keyFormlist); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

                keyFormlist.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
                keyFormlist.addAll(primesWithoutDuplicates);

                int keysize = keyFormlist.size();

             //  initLinearBindsave(v, keyFormlist, valueFormlist);

                setView(v,keyFormlist, valueFormlist);
               // initLineaHorizontal_Bindsave(v, valueFormlist,keysize);


            } else {


            }

        }
        chk_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                selectAllCheckBox(isChecked);

            }
        });




        return v;
    }

    public void setView(View v, List<String> key, List<String> value) {

        tl = (TableLayout) v.findViewById(R.id.main_table);

        TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < key.size(); i++) {

            TextView label_date = new TextView(getActivity());
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            label_date.setLayoutParams(params);
            label_date.setGravity(Gravity.CENTER);
            label_date.setBackgroundResource(R.drawable.edit_text_design_table);
            ;
            label_date.setId(20 + i);
            label_date.setPadding(0, 5, 0, 5);
            label_date.setText(key.get(i));
            label_date.setTextColor(Color.WHITE);

            tr_head.addView(label_date);
            // add the column to the table row here

        }

        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        Integer count = 0;
        TextView labelDATE = null;
        int getcount = value.size();
        int setCounter = 0;
        while (getcount > 0) {
            tr = new TableRow(getActivity());
            tr.setId(100 + getcount);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            for (int k = 0; k < key.size(); k++) {

                labelDATE = new TextView(getActivity());

                LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                labelDATE.setLayoutParams(params);
                labelDATE.setGravity(Gravity.CENTER);
                labelDATE.setId(200 + setCounter);
                labelDATE.setBackgroundResource(R.drawable.edit_text_design);
                ;
                String getStr = value.get(setCounter);
                if (getStr.equalsIgnoreCase("No")) {
                    labelDATE.setText("");
                } else {
                    labelDATE.setText(value.get(setCounter));
                }

                labelDATE.setPadding(0, 5, 0, 5);
                labelDATE.setTextColor(Color.BLACK);
                tr.addView(labelDATE);
                setCounter++;
            }
            cb = new CheckBox(getActivity());
            cb.setId(500 + getcount);
            checkBoxArrayList.add(cb);
            tr.addView(cb);

            tl.addView(tr, new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            getcount = getcount - key.size();
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    for (int i = 0; i < tr.getChildCount(); i++) {
                        TextView companyTV, valueTV, yearTV;
                        String company = null, value, year;
                        final View view = tr.getChildAt(i);
                        if (view instanceof TextView) {
                            companyTV = (TextView) tr.getChildAt(i);
                            company = companyTV.getText().toString();

                        }
                        if (view instanceof CheckBox) {

                        }

                    }
                }
            });
            count++;

        }

      //  Utility.displayMessage(getContext(),""+checkBoxArrayList.size());
// finally add this to the table row

    }

    private void selectAllCheckBox(boolean status)
    {
        for(int i=0;i<checkBoxArrayList.size();i++)
        {
            CheckBox checkBox =checkBoxArrayList.get(i);
            if(status){
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);
            }
        }
    }

    public String getForm_pre(String key ){

        String gets =  sharedpreferences.getString(key, "");
        return gets;
    }



    public void initLinearBindsave(final View view, List<String> checklistDataList,List<String> savelist) {

        int size = checklistDataList.size();
        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main_header);
        for (int i = 0; i < checklistDataList.size(); i++) {
            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);
            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(checklistDataList.get(i));
            tv3.setTextSize(16);
            tv3.setTextColor(Color.BLACK);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2f);

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

        */
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

            *//*


            LL3.addView(tv3);
            LL3.addView(etv3);
            li1.addView(LL3);


        }


    }


    public void initLineaHorizontal_header_Bindsave(final View view, List<String> checklistDataList, int size){

        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main_header);
        CustomAdapter_Header adapter = new CustomAdapter_Header(getActivity(), checklistDataList);


        // create a gridview
        GridView gridView= new GridView(getActivity());

        gridView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        gridView.setNumColumns(size);
        gridView.setAdapter(adapter);

        // Adding the gridview to the RelativeLayout as a child
        li1.addView(gridView);

    }

    public void initLineaHorizontal_Bindsave(final View view, List<String> checklistDataList, int size) {
     //   LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
        GridView gridView =(GridView)view.findViewById(R.id.gridView_value) ;
        CustomAdapter adapter = new CustomAdapter(getActivity(), checklistDataList);




        // create a RelativeLayou

        // create a gridview
      //  GridView gridView= new GridView(getActivity());


       // gridView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        gridView.setNumColumns(size);

        gridView.setAdapter(adapter);

        // Adding the gridview to the RelativeLayout as a child
      //  li1.addView(gridView);


    }



    public class CustomAdapter extends BaseAdapter {

       List<String> result;
        Context context;

        private  LayoutInflater inflater=null;
        public CustomAdapter(Context c, List osNameList) {
            // TODO Auto-generated constructor stub
            result=osNameList;
            context=c;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView os_text;

        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.gridview_text_getchecklist_adapter, null);
            holder.os_text =(TextView) rowView.findViewById(R.id.grid_text);


       String check = result.get(position);

            if(check.equals("0")){
                holder.os_text.setText("No");
            }else {

                holder.os_text.setText(result.get(position));
            }
        */
/*

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_SHORT).show();
                }
            });

            *//*


            return rowView;
        }

    }

    public class CustomAdapter_Header extends BaseAdapter {

        List<String> result;
        Context context;

        private  LayoutInflater inflater=null;
        public CustomAdapter_Header(Context c, List osNameList) {
            // TODO Auto-generated constructor stub
            result=osNameList;
            context=c;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView os_text;

        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.gridview_text_getchecklist_header_adapter, null);
            holder.os_text =(TextView) rowView.findViewById(R.id.grid_text);

            holder.os_text.setText(result.get(position));

        */
/*

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_SHORT).show();
                }
            });

            *//*


            return rowView;
        }

    }
}
*/
