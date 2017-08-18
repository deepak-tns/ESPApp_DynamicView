package com.tns.espapp.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TNS on 09-Aug-17.
 */

public class FinalCheckListData {

    private int sNo;
    private String desc;
    private String sts;
    private String remark;
    private String photos;

    private String path;


    private  int count;
    private List<String> multiphotos = new ArrayList<>();

    public FinalCheckListData(int sNo, String desc, String sts, String remark,String photos)
    {
        this.sNo = sNo;
        this.desc = desc;
        this.sts = sts;
        this.remark = remark;
        this.photos = photos;
    }



    public FinalCheckListData(ArrayList<String> multiphotos) {
        this.multiphotos = multiphotos;
    }


    public int getsNo() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public List<String> getMultiphotos() {
        return multiphotos;
    }

    public void setMultiphotos(List<String> multiphotos) {
        this.multiphotos = multiphotos;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
