package com.app.indokordsa.record.api;

//import java.util.List;
//import okhttp3.MultipartBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
//import retrofit2.http.Multipart;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
//import retrofit2.http.Part;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiRoute {
    @POST
    @FormUrlEncoded
    Call<String>login(
            @Url String url,
            @Field("username") String username,
            @Field("password") String password
    );
    @POST
    @FormUrlEncoded
    Call<String>loginV2(
            @Url String url,
            @Field("barcode") String barcode
    );

    @Multipart
    @POST
    Call<String> updateProfile(
            @Url String url,
            @Part("id") RequestBody id,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("nama") RequestBody nama,
            @Part MultipartBody.Part image);

    @Multipart
    @POST
    Call<String> updateProfilev2(
            @Url String url,
            @Part("id") RequestBody id,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("nama") RequestBody nama);

    @GET
    Call<String>getData(@Url String url);

    @POST
    @FormUrlEncoded
    Call<String>sync(
            @Url String url,
            @Field("data_local") String data_local
    );

    @POST
    @FormUrlEncoded
    Call<String>update_alasan_checklist(
            @Url String url,
            @Field("id_penugasan") String id_penugasan,
            @Field("alasan") String alasan
    );
    @POST
    @FormUrlEncoded
    Call<String>update_alasan_questionnaire(
            @Url String url,
            @Field("id_kuesioner_result") String id_kuesioner_result,
            @Field("alasan") String alasan
    );

    @POST
    @FormUrlEncoded
    Call<String>save_checklist(
            @Url String url,
            @Field("id_penugasan") String id_penugasan,
            @Field("id_operator") String id_operator,
            @Field("no") String no,
            @Field("val") String val,
            @Field("ket") String ket
    );

    @POST
    @FormUrlEncoded
    Call<String>save_todolist(
            @Url String url,
            @Field("id_todolist") String id_todolist,
            @Field("id_user") String id_user,
            @Field("tanggal") String tanggal,
            @Field("id_area") String id_area,
            @Field("id_group") String id_group,
            @Field("id_shift") String id_shift,
            @Field("time") String time,
            @Field("remarks") String remarks,
            @Field("action") String action,
            @Field("id_status") String id_status,
            @Field("id_pic") String id_pic
    );

    @POST
    @FormUrlEncoded
    Call<String>delete_todolist(
            @Url String url,
            @Field("id_todolist") String id_todolist,
            @Field("id_user") String id_user
    );

    @POST
    @FormUrlEncoded
    Call<String>save_questionneir(
            @Url String url,
            @Field("id_kuesioner_result_detail") String id_kuesioner_result_detail,
            @Field("id_kuesioner_result") String id_kuesioner_result,
            @Field("id_user") String id_user,
            @Field("jawaban") String jawaban,
            @Field("result") String result
    );

    @POST
    @FormUrlEncoded
    Call<String>cek_nfc(
            @Url String url,
            @Field("id_penugasan") String id_penugasan,
            @Field("nfc") String nfc
    );

//    @Multipart
//    @POST
//    Call<String> etc(
//            @Url String url,
//            @Part List<MultipartBody.Part> files
//    );
}
