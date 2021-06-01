package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;
import com.app.indokordsa.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.indokordsa.Util.passing_result_question;

public class KuesionerResult extends BaseObservable implements Parcelable {
    private String no;
    private String id;
    private Shift shift;
    private Area area;
    private Kuesioner kuesioner;
    private String jawaban;
    private String result;
    private String status;
    private ArrayList<KuesionerResultDetail1> list_pertanyaan = new ArrayList<>();
    private String tanggal;

    KuesionerResult(){

    }
    public KuesionerResult(String no, String id, Shift shift, Area area, Kuesioner kuesioner, String jawaban, ArrayList<KuesionerResultDetail1> list_pertanyaan, String result, String status, String tanggal){
        setNo(no);
        setId(id);
        setShift(shift);
        setArea(area);
        setKuesioner(kuesioner);
        setJawaban(jawaban);
        setResult(result);
        setStatus(status);
        setList_pertanyaan(list_pertanyaan);
        setTanggal(tanggal);
    }

    protected KuesionerResult(Parcel in) {
        no = in.readString();
        id = in.readString();
        shift = in.readParcelable(Shift.class.getClassLoader());
        area = in.readParcelable(Area.class.getClassLoader());
        kuesioner = in.readParcelable(Kuesioner.class.getClassLoader());
        jawaban = in.readString();
        result = in.readString();
        status = in.readString();
        list_pertanyaan = in.createTypedArrayList(KuesionerResultDetail1.CREATOR);
        tanggal = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(no);
        dest.writeString(id);
        dest.writeParcelable(shift, flags);
        dest.writeParcelable(area, flags);
        dest.writeParcelable(kuesioner, flags);
        dest.writeString(jawaban);
        dest.writeString(result);
        dest.writeString(status);
        dest.writeTypedList(list_pertanyaan);
        dest.writeString(tanggal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KuesionerResult> CREATOR = new Creator<KuesionerResult>() {
        @Override
        public KuesionerResult createFromParcel(Parcel in) {
            return new KuesionerResult(in);
        }

        @Override
        public KuesionerResult[] newArray(int size) {
            return new KuesionerResult[size];
        }
    };

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
        notifyPropertyChanged(BR.shift);
    }

    @Bindable
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
        notifyPropertyChanged(BR.area);
    }

    @Bindable
    public Kuesioner getKuesioner() {
        return kuesioner;
    }

    public void setKuesioner(Kuesioner kuesioner) {
        this.kuesioner = kuesioner;
        notifyPropertyChanged(BR.kuesioner);
    }

    @Bindable
    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
        notifyPropertyChanged(BR.jawaban);
    }

    @Bindable
    public String getResult() {
        return this.result.replace("\\","");
//        return this.result;
    }

    public void setResult(String result) {
        this.result = result.replace("\\","");
//        this.result = result;
        notifyPropertyChanged(BR.result);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public ArrayList<KuesionerResultDetail1> getList_pertanyaan() {
        return list_pertanyaan;
    }

    @Bindable
    public ArrayList<KuesionerResultDetail1> getListPertanyaanWithPassData() {
        return passing_result_question(this.list_pertanyaan, this.result);
    }

    public void setList_pertanyaan(ArrayList<KuesionerResultDetail1> list_pertanyaan) {
        this.list_pertanyaan = list_pertanyaan;
        notifyPropertyChanged(BR.list_pertanyaan);
    }

    @Bindable
    public int getTotalPertanyaan(){
        return list_pertanyaan.size();
    }

    @Bindable
    public int getTotalSelesaiPertanyaan(){
        int tmp = 0;
        if(getJawaban().equals("1")){
            tmp = list_pertanyaan.size();
        }
        else {
            for (KuesionerResultDetail1 item : list_pertanyaan) { //topik
                int _tmp1 = 0;
                int _tmp_end = item.getDetail().size();
                for (KuesionerResultDetail2 sub_item : item.getDetail()) { //pertanyaan
                    if (sub_item.isDone()) {
                        _tmp1++;
                    }
                }
                if (_tmp1 == _tmp_end) {
                    tmp++;
                }
            }
        }
        return tmp;
    }

    @Bindable
    public String getTanggal() {
        return tanggal;
    }

    @Bindable
    public String getTanggalFormat() {
        return Util.reFormatDatev1(tanggal);
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
        notifyPropertyChanged(BR.tanggal);
    }

    @Bindable
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
        notifyPropertyChanged(BR.no);
    }

    public JSONObject getResultDone(){
        JSONObject tmp = new JSONObject();
        try {
            tmp.put("id",getId());
            tmp.put("jawaban",getJawaban());

            JSONArray list_topic = new JSONArray();
            for (int i=0;i<getList_pertanyaan().size();i++){
                KuesionerResultDetail1 item = getList_pertanyaan().get(i);

                JSONObject topic = new JSONObject();
                topic.put("id",item.getId());

                JSONArray question_topic = new JSONArray();
                for(int j=0;j<item.getDetail().size();j++){
                    KuesionerResultDetail2 item_ = item.getDetail().get(j);

                    JSONObject question = new JSONObject();
                    question.put("id",item_.getId());
                    question.put("val",item_.getVal());
                    question.put("start",item_.getStart());
                    question.put("end",item_.getEnd());
                    question.put("duration",item_.getDuration());
                    question_topic.put(question);
                }
                topic.put("sub",question_topic);
                list_topic.put(topic);
            }
            tmp.put("detail",list_topic);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tmp;
    }
}
