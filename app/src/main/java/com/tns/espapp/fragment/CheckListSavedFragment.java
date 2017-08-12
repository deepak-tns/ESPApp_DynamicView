package com.tns.espapp.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.AttachmentData;
import com.tns.espapp.CaptureData;
import com.tns.espapp.DrawBitmapAll;
import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.activity.RealPathUtil;
import com.tns.espapp.database.FeedbackRecordData;
import com.tns.espapp.database.FinalCheckListData;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
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

    private TableLayout tl;
    private TableRow tr;


    List<ChecklistData> getFormlist;

    ArrayList <String>  listkey;

    private int PICK_ATTACHMENT_REQUEST = 1;
    private int ADD_CAPTURE_IMAGE = 2;

    int count_addAttachment = 0;
    int count_capture_Image = 0;
    int BTNCONSTANT = 2;

    private String realPath;
    private String stgetImage;

    private AttachmentAdapter adapter_attachment;
    private ArrayList<String> attachment_ImageList = new ArrayList();

    private ArrayList<String> capture_ImageList  = new ArrayList();
    private ArrayList<AttachmentData> attachmentDatas = new ArrayList<>();
     String current_date;
    ArrayList<String> arrayList;

    private ListView lst_attachment;

    int t_id = 104;
    Button   btn_send;
    ImageView iv_addAttachment;
    List<EditText> allEds = new ArrayList<EditText>();
    List<RadioGroup> allRGs = new ArrayList<RadioGroup>();

    ListView finallistview;
     LinearLayout linearLayoutphotos;

    HashMap<String,List<EditText>> hashMap_edt = new HashMap();
    HashMap<Integer,String> hashMap = new HashMap<>();
    SQLiteDatabase  myDataBase;
    private SQLiteStatement insertStmt;

    SharedPreferences sharedpreferences;

    TextView ivPhotos1,ivPhotos2,ivPhotos3,ivPhotos4,ivPhotos5;

    String setFormname;
    public CheckListSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_list_saved, container, false);

        TextView textview =(TextView)v.findViewById(R.id.set_formane);
        finallistview=(ListView)v.findViewById(R.id.final_listview);

        iv_addAttachment = (ImageView) v.findViewById(R.id.iv_add_attachment);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        current_date = dateFormat.format(cal.getTime());

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

      //  db. deleteFormcheckListData(setFormname);

        initLinearBind(v, getFormlist);


        List<FinalCheckListData> value = new ArrayList<>();
        value.add(new FinalCheckListData(1,"new Site","","","yes"));
        value.add(new FinalCheckListData(2,"everything in Site","","","yes"));

       setView_Multiphotos(v,value);


 /*       FinalCheckListAdapterListview_Save finalCheckListAdapterListview_save = new FinalCheckListAdapterListview_Save(getActivity(),R.layout.final_checklist_data_adapter,value);
         finallistview.setAdapter(finalCheckListAdapterListview_save);
           finalCheckListAdapterListview_save.notifyDataSetChanged();*/


        return v;
    }


    public void initLinearBind( View view, List<ChecklistData> checklistDataList) {
           listkey = new ArrayList<>();

        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
        for (int i = 0; i < checklistDataList.size(); i++) {
            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(checklistDataList.get(i).getName_value());
            tv3.setTextSize(16);
            tv3.setTextColor(Color.BLACK);
            tv3.setId(t_id + 1);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2f);
            allEds.add(etv3);
            hashMap.put(i, checklistDataList.get(i).getName());
            listkey.add( checklistDataList.get(i).getName());
            hashMap_edt.put(checklistDataList.get(i).getName(),allEds);

            edtParams_3.setMargins(5, 20, 5, 20);
            etv3.setLayoutParams(edtParams_3);
            etv3.setBackgroundResource(R.drawable.edt_border);
            etv3.setPadding(10,10,10,10);
          //  etv3.setHint(checklistDataList.get(i).getName());
            etv3.setTextSize(16);
            etv3.setTextColor(Color.BLACK);
            etv3.setId( i);

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


            LL3.addView(etv3);
            li1.addView(LL3);


        }

        listkey.add("Status");
        createDynamicDatabase(getActivity(),setFormname,listkey);


    }

    public void setView_Multiphotos(View v,List<FinalCheckListData> value) {

        tl = (TableLayout) v.findViewById(R.id.main_table_tb);
        TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));


            TextView label_sno = new TextView(getActivity());
            LinearLayout.LayoutParams param = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.4f);
        label_sno.setLayoutParams(param);
        label_sno.setGravity(Gravity.CENTER);
        label_sno.setBackgroundResource(R.drawable.edit_text_design_table);

        label_sno.setPadding(0, 5, 0, 5);
        label_sno.setText("SNo");
        label_sno.setTextColor(Color.WHITE);
        //description........................................................................................
        TextView label_desc = new TextView(getActivity());
        LinearLayout.LayoutParams paramdesc = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        label_desc.setLayoutParams(paramdesc);
        label_desc.setGravity(Gravity.CENTER);
        label_desc.setBackgroundResource(R.drawable.edit_text_design_table);

        label_desc.setPadding(0, 5, 0, 5);
        label_desc.setText("Description");
        label_desc.setTextColor(Color.WHITE);

        //satus........................................................................................
        TextView label_sts = new TextView(getActivity());
        LinearLayout.LayoutParams paramsts = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.5f);
        label_sts.setLayoutParams(paramsts);
        label_sts.setGravity(Gravity.CENTER);
        label_sts.setBackgroundResource(R.drawable.edit_text_design_table);
        label_sts.setPadding(0, 5, 0, 5);
        label_sts.setText("Status");
        label_sts.setTextColor(Color.WHITE);

        //status........................................................................................
        TextView label_remark = new TextView(getActivity());
        LinearLayout.LayoutParams paramremark = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.6f);

        label_remark.setLayoutParams(paramremark);
        label_remark.setGravity(Gravity.CENTER);
        label_remark.setBackgroundResource(R.drawable.edit_text_design_table);
        label_remark.setPadding(0, 5, 0, 5);
        label_remark.setText("Remark");
        label_remark.setTextColor(Color.WHITE);

        //status........................................................................................
        TextView label_photos = new TextView(getActivity());
        LinearLayout.LayoutParams param_photos = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.7f);

        label_photos.setLayoutParams(param_photos);
        label_photos.setGravity(Gravity.CENTER);
        label_photos.setBackgroundResource(R.drawable.edit_text_design_table);
        label_photos.setPadding(0, 5, 0, 5);
        label_photos.setText("Photos");
        label_photos.setTextColor(Color.WHITE);



            tr_head.addView(label_sno);
            tr_head.addView(label_desc);
            tr_head.addView(label_sts);
            tr_head.addView(label_remark);
            tr_head.addView(label_photos);

            // add the column to the table row here



        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        lst_attachment = new ListView(getActivity());

            for (int k = 0; k < value.size(); k++) {


                tr = new TableRow(getActivity());
                tr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView label_sno2 = new TextView(getActivity());
                LinearLayout.LayoutParams param2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.4f);
                label_sno2.setLayoutParams(param2);

                label_sno2.setGravity(Gravity.CENTER);
                label_sno2.setBackgroundResource(R.drawable.edit_text_design);

                label_sno2.setPadding(0, 5, 0, 5);
                label_sno2.setText(value.get(k).getsNo()+"");
                label_sno2.setTextColor(Color.BLACK);
                //description........................................................................................
                TextView label_desc2 = new TextView(getActivity());
                LinearLayout.LayoutParams paramdesc2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
                label_desc2.setLayoutParams(paramdesc2);
                label_desc2.setGravity(Gravity.CENTER);
                label_desc2.setBackgroundResource(R.drawable.edit_text_design);

                label_desc2.setPadding(0, 5, 0, 5);
                label_desc2.setText(value.get(k).getDesc());
                label_desc2.setTextColor(Color.BLACK);

                //status........................................................................................
                EditText label_sts2 = new EditText(getActivity());
                LinearLayout.LayoutParams paramsts2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.5f);
                label_sts2.setLayoutParams(paramsts2);
                label_sts2.setGravity(Gravity.CENTER);
                label_sts2.setBackgroundResource(R.drawable.edit_text_design);
                label_sts2.setPadding(0, 5, 0, 5);
               // label_sts2.setText(value.get(k).getSts());
                label_sts2.setTextColor(Color.BLACK);
                label_sts2.setTextSize(14);

                //remark........................................................................................
                EditText label_remark2 = new EditText(getActivity());
                LinearLayout.LayoutParams paramremark2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.6f);

                label_remark2.setLayoutParams(paramremark2);
                label_remark2.setGravity(Gravity.CENTER);
                label_remark2.setBackgroundResource(R.drawable.edit_text_design);
                label_remark2.setPadding(0, 5, 0, 5);
               // label_remark2.setText(value.get(k).getRemark());
                label_remark2.setTextColor(Color.BLACK);
                label_remark2.setTextSize(14);

                //photos........................................................................................
                linearLayoutphotos = new LinearLayout(getActivity());
                LinearLayout.LayoutParams paramlinearLayoutphotos = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.7f);
                linearLayoutphotos.setLayoutParams(paramlinearLayoutphotos);
                linearLayoutphotos.setOrientation(LinearLayout.VERTICAL);
                linearLayoutphotos.setBackgroundResource(R.drawable.edit_text_design);

                TextView label_photos2 = new TextView(getActivity());
                final LinearLayout.LayoutParams param_photos2 = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                label_photos2.setLayoutParams(param_photos2);
                label_photos2.setGravity(Gravity.CENTER);
                label_photos2.setBackgroundResource(R.drawable.edit_text_design);
                label_photos2.setPadding(0, 5, 0, 5);
               // label_photos2.setText(value.get(k).getPhotos());
                label_photos2.setBackgroundResource(android.R.drawable.ic_input_add);
                label_photos2.setTextColor(Color.BLACK);
                label_photos2.setId(BTNCONSTANT+k);

                label_photos2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int btnId = v.getId();
                        switch (btnId)
                        {
                            case  2:
                            {
                                 arrayList = new ArrayList<String>();
                              //  arrayList.add(String.valueOf(btnId));
                                if (count_capture_Image < 5) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                                    count_capture_Image++;

                                }
                            }
                            break;
                            case 3:
                            {
                                arrayList = new ArrayList<String>();
                                if (count_capture_Image < 5) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                                    count_capture_Image++;

                                }
                               // arrayList.add(String.valueOf(btnId));
                            }
                            case 4:
                            {
                                arrayList = new ArrayList<String>();
                                if (count_capture_Image < 5) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                                    count_capture_Image++;

                                }
                              //  arrayList.add(String.valueOf(btnId));
                            }
                            break;


                        }

                       // linearLayoutphotos.removeAllViews();

                      /*
                      else
                      {

                            Toast.makeText(getActivity(), "Maximum 5 Attachment at a time", Toast.LENGTH_LONG).show();
                        }*/

                    }
                });



                linearLayoutphotos.addView(label_photos2);

                tr.addView(label_sno2);
                tr.addView(label_desc2);
                tr.addView(label_sts2);
                tr.addView(label_remark2);
                tr.addView(linearLayoutphotos);

                tl.addView(tr, new TableLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
            }


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


        }

        //   Utility.displayMessage(getContext(),""+checkBoxArrayList.size());

// finally add this to the table row





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
          // Toast.makeText(getActivity(),strings[i]+","+ s,Toast.LENGTH_LONG).show();

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
           listvalue.add("0");
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
           /* else if(title.size() ==2) {
                querryString = title.get(0)+" text,";
                querryString+= title.get(1) +" text";
            }*/
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
        /*    else if(title.size() ==2)
            {
                titleString = title.get(0) + ",";
                markString = "?,";
                titleString+= title.get(1) +" text";
                markString+="?";
            }*/
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_ATTACHMENT_REQUEST) {

                Uri uri = data.getData();

                try
                {
                    realPath = RealPathUtil.getPath_File_Attacah(getActivity(), uri);
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_LONG).show();
                }


                if(realPath != null) {
                    File file = new File(realPath);

                    stgetImage = file.getName();
                    attachment_ImageList.add(stgetImage);

                    attachmentDatas.add(new AttachmentData(stgetImage, realPath));

                 /*
                 adapter_attachment = new AttachmentAdapter(getActivity(), R.layout.add_attachment_feedbackfrag_adapter, attachment_ImageList);
                    lst_attachment.setAdapter(adapter_attachment);
                    ListviewHelper.getListViewSize(lst_attachment);
                 */

                    if (stgetImage != null) {
                        //  linearLayout_add_attachment.addView(tableLayout(stgetImage), linearLayout_add_attachment.getChildCount());
                    }

          /*      try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    String endkmImageEncodeString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);

                    // Log.d(TAG, String.valueOf(bitmap));

                    // ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    // imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                }
            }
            if (requestCode == ADD_CAPTURE_IMAGE) {
                String capturepath = "";

                //   String[] all_path = data.getStringArrayExtra("all_path");
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

                SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
                String current_time_str = time_formatter.format(System.currentTimeMillis());

                String destinationpath = Environment.getExternalStorageDirectory().toString();
                File destination = new File(destinationpath + "/ESP/");
                if (!destination.exists()) {
                    destination.mkdirs();
                }

                File file = null;
                FileOutputStream fo;
                try {
                    // destination.createNewFile();

                    file = new File(destination,  current_date + "_" + current_time_str + ".jpg");
                    fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                    System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
                    String path = (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));

                    String startkmImageEncodeString = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
                    String totalcapturepath = destinationpath + "/ESP/FeedBack/" + capturepath;

                    capture_ImageList.add(path);
                    arrayList.add(path);
                String datastring =path;

             /*   adapter_attachment = new AttachmentAdapter(getActivity(), R.layout.add_attachment_feedbackfrag_adapter, capture_ImageList);
                lst_attachment.setAdapter(adapter_attachment);
                ListviewHelper.getListViewSize(lst_attachment);*/

              if(arrayList.size() > 0){
                   // for(int a= 0; a<arrayList.size();a++){
                        TextView label_photo2add = new TextView(getActivity());
                        LinearLayout.LayoutParams param_photos2_add = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        label_photo2add.setLayoutParams(param_photos2_add);
                        label_photo2add.setGravity(Gravity.CENTER);
                        label_photo2add.setBackgroundResource(R.drawable.edit_text_design);
                        label_photo2add.setPadding(0, 5, 0, 5);
                        label_photo2add.setText(datastring);
                        label_photo2add.setTextColor(Color.BLACK);

                        linearLayoutphotos.addView(label_photo2add);

                   // }
                }






             /*
                if(count_capture_Image == 0){
                    ivPhotos1.setVisibility(View.VISIBLE);
                    ivPhotos1.setText(path);
                }
                if(count_capture_Image == 1){
                    ivPhotos2.setVisibility(View.VISIBLE);
                    ivPhotos2.setText(path);
                }
                if(count_capture_Image == 2){
                    ivPhotos3.setVisibility(View.VISIBLE);
                    ivPhotos3.setText(path);
                }
                if(count_capture_Image == 3){
                    ivPhotos4.setVisibility(View.VISIBLE);
                    ivPhotos4.setText(path);
                }
                if(count_capture_Image == 4){
                    ivPhotos5.setVisibility(View.VISIBLE);
                    ivPhotos5.setText(path);
                }*/




                if(capture_ImageList.size()>0){

                    for(String datas : capture_ImageList){
                        Toast.makeText(getActivity(), datas, Toast.LENGTH_LONG).show();
                    }
                }

            }
        }

    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private class AttachmentAdapter extends ArrayAdapter {

        ArrayList<String> arrayList;
        public AttachmentAdapter(Context context, int resource, ArrayList<String> list) {
            super(context, resource, list);
            this.arrayList = list;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // super.getView(position, convertView, parent);
            AttachmentAdapter.ViewHolder viewHolder = new AttachmentAdapter.ViewHolder();

            String s = arrayList.get(position);
            if (convertView == null) {

                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_attachment_feedbackfrag_adapter, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_name_add_attachment_adapter);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_delete_add_attachment_adapter);
                convertView.setTag(viewHolder);


            } else {
                viewHolder = (AttachmentAdapter.ViewHolder) convertView.getTag();

            }

            viewHolder.textView.setText(s);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count_addAttachment = count_addAttachment - 1;

                    arrayList.remove(position);
                    adapter_attachment.notifyDataSetChanged();
                    ListviewHelper.getListViewSize(lst_attachment);
                    attachmentDatas.remove(position);

                    // attachment_ImageList.remove(position);


                }
            });


            return convertView;
        }

        private class ViewHolder {
            TextView textView;
            ImageView imageView;


        }
    }



    public class FinalCheckListAdapterListview_Save extends ArrayAdapter {
        Context context;
        List<FinalCheckListData> getListArray;
        public FinalCheckListAdapterListview_Save(Context context, int resource, List<FinalCheckListData> list) {
            super(context, resource, list);
            this.context = context;
            this.getListArray = list;

        }



        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.final_checklist_data_adapter, parent, false);

            lst_attachment =(ListView)convertView.findViewById(R.id.lstcapture_adapter);
            TextView tvSno=(TextView)convertView.findViewById(R.id.tv_sno);
            TextView tvDesc=(TextView)convertView.findViewById(R.id.tv_description);
            TextView tvSts=(TextView)convertView.findViewById(R.id.tv_status);
            TextView tvRemark=(TextView)convertView.findViewById(R.id.tv_remark);
            TextView ivPhotos=(TextView) convertView.findViewById(R.id.tv_photos);

             ivPhotos1=(TextView) convertView.findViewById(R.id.tv_photos_1);
             ivPhotos2=(TextView) convertView.findViewById(R.id.tv_photos_2);
             ivPhotos3=(TextView) convertView.findViewById(R.id.tv_photos_3);
             ivPhotos4=(TextView) convertView.findViewById(R.id.tv_photos_4);
             ivPhotos5=(TextView) convertView.findViewById(R.id.tv_photos_5);

          //  lst_attachment =(ListView)convertView.findViewById(R.id.lst_final_checklist_data_adapter);

            FinalCheckListData data =getListArray.get(position);
            tvSno.setText(data.getsNo()+"");
            tvDesc.setText(data.getDesc());
            tvSts.setText(data.getSts());
            tvRemark.setText(data.getRemark());
            //ivPhotos.setText(data.getPhotos());
            if(data.getPhotos().equals("yes"))
                ivPhotos.setBackgroundResource(android.R.drawable.ic_input_add);

          ivPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





                            if (count_capture_Image < 5) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                                count_capture_Image++;

                            }

                      /*      adapter_attachment = new AttachmentAdapter(getActivity(), R.layout.add_attachment_feedbackfrag_adapter, attachment_ImageList);
                            lst_attachment.setAdapter(adapter_attachment);
                            ListviewHelper.getListViewSize(lst_attachment);
                            adapter_attachment.notifyDataSetChanged();*/

                  else {

                        Toast.makeText(getActivity(), "Maximum 5 Attachment at a time", Toast.LENGTH_LONG).show();

                    }


                }
            });

            return convertView;

        }



    }



}