package com.app.indokordsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.indokordsa.view.model.Job;
import com.app.indokordsa.view.model.JawabanKuesioner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Util {
    public static String getPathFromUri(Activity activity, Uri selectedImage){
        if(selectedImage==null)
            return null;

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();

        return path;
    }
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public static Drawable buildCounterDrawable(Activity act, int layout, int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(act);
        View view = inflater.inflate(layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(act.getResources(), bitmap);
    }
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    public static String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }
    public static long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }
    public static long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }
    public static String reFormatDate(String date){
        try {
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat newformat = new SimpleDateFormat("MMM yyyy");
            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
            System.out.println(tz.getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH)); // WIB

            newformat.setTimeZone(tz);
            return newformat.format(oldformat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static Date string2Date(String date){
        Date tmp = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");

            format.setTimeZone(tz);
            tmp = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tmp;
    }
    public static String reFormatDatev1(String date){
        try {
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat newformat = new SimpleDateFormat("dd MMMM yyyy");
            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
            System.out.println(tz.getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH)); // WIB

            newformat.setTimeZone(tz);
            return newformat.format(Objects.requireNonNull(oldformat.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String reFormatDatev3(String date){
        try {
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");
            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
            System.out.println(tz.getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH)); // WIB

            newformat.setTimeZone(tz);
            return newformat.format(Objects.requireNonNull(oldformat.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append(toDec(id));
//        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
//        sb.append("ID (dec): ").append(toDec(id)).append('\n');
//        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');
//
//        String prefix = "android.nfc.tech.";
//        sb.append("Technologies: ");
//        for (String tech : tag.getTechList()) {
//            sb.append(tech.substring(prefix.length()));
//            sb.append(", ");
//        }
//
//        sb.delete(sb.length() - 2, sb.length());
//
//        for (String tech : tag.getTechList()) {
//            if (tech.equals(MifareClassic.class.getName())) {
//                sb.append('\n');
//                String type = "Unknown";
//
//                try {
//                    MifareClassic mifareTag = MifareClassic.get(tag);
//
//                    switch (mifareTag.getType()) {
//                        case MifareClassic.TYPE_CLASSIC:
//                            type = "Classic";
//                            break;
//                        case MifareClassic.TYPE_PLUS:
//                            type = "Plus";
//                            break;
//                        case MifareClassic.TYPE_PRO:
//                            type = "Pro";
//                            break;
//                    }
//                    sb.append("Mifare Classic type: ");
//                    sb.append(type);
//                    sb.append('\n');
//
//                    sb.append("Mifare size: ");
//                    sb.append(mifareTag.getSize()).append(" bytes");
//                    sb.append('\n');
//
//                    sb.append("Mifare sectors: ");
//                    sb.append(mifareTag.getSectorCount());
//                    sb.append('\n');
//
//                    sb.append("Mifare blocks: ");
//                    sb.append(mifareTag.getBlockCount());
//                } catch (Exception e) {
//                    sb.append("Mifare classic error: ").append(e.getMessage());
//                }
//            }
//
//            if (tech.equals(MifareUltralight.class.getName())) {
//                sb.append('\n');
//                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
//                String type = "Unknown";
//                switch (mifareUlTag.getType()) {
//                    case MifareUltralight.TYPE_ULTRALIGHT:
//                        type = "Ultralight";
//                        break;
//                    case MifareUltralight.TYPE_ULTRALIGHT_C:
//                        type = "Ultralight C";
//                        break;
//                }
//                sb.append("Mifare Ultralight type: ");
//                sb.append(type);
//            }
//        }

        return sb.toString();
    }
    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static ArrayList<Job> intersection(ArrayList<Job> server, ArrayList<Job> local) {
        ArrayList<Job> gamma = new ArrayList<>();
        for(int k=0;k<local.size();k++){
            Job source_local = local.get(k);
            for(int l=0;l<server.size();l++){
                Job source_server = server.get(l);
                if(source_server.getNo().equals(source_local.getNo())){
                    gamma.add(new Job(
                            source_local.getId_tugas(),
                            source_local.getId_penugasan(),
                            source_local.getNo(),
                            source_local.getCek(),
                            source_local.getKat(),
                            source_local.getVal(),
                            source_local.getKet()
                    ));
                }
            }
        }
        return gamma;
    }
    public static ArrayList<JawabanKuesioner> intersectionv2(ArrayList<JawabanKuesioner> server, ArrayList<JawabanKuesioner> local) {
        ArrayList<JawabanKuesioner> gamma = new ArrayList<>();
        for(int k=0;k<local.size();k++){
            JawabanKuesioner source_local = local.get(k);
            for(int l=0;l<server.size();l++){
                JawabanKuesioner source_server = server.get(l);
                if(
                        source_server.getTopik().getId().equals(source_local.getTopik().getId()) &&
                        source_server.getPertanyaan().getId().equals(source_local.getPertanyaan().getId())
                ){
                    gamma.add(new JawabanKuesioner(
                            source_local.getId(),
                            source_local.getTopik(),
                            source_local.getPertanyaan(),
                            source_local.getVal(),
                            source_local.getOther(),
                            source_local.getStart(),
                            source_local.getEnd(),
                            source_local.getRemarks()
                    ));
                }
            }
        }
        return gamma;
    }
    public static String SC(String table, String colum){
        return String.format("%s.%s",table,colum);
    }
}
