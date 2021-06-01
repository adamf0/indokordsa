package com.app.indokordsa.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.indokordsa.databinding.ItemRowSliderBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ViewPagerListener;
import com.app.indokordsa.record.db.DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<String> list_slide;
    ViewPagerListener listener;
    DB db;
    SessionManager session;
    HashMap<String, String> data;

    public ViewPagerAdapter(Context context, ViewPagerListener listener, ArrayList<String> list_slide) {
        this.context = context;
        this.list_slide = list_slide;
        this.listener = listener;

        db = new DB(context);
        session = new SessionManager(context);
        data = session.getSession();
    }

    @SuppressLint("SimpleDateFormat")
    void loadMonth(ItemRowSliderBinding binding){
        try {
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-01");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            String StartDate = sdf0.format(new Date());

            String dateAsString = sdf1.format(new Date());
            Date dateFromString = sdf1.parse(dateAsString);
            assert dateFromString != null;

            Calendar c = Calendar.getInstance();
            c.setTime(dateFromString);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date end = c.getTime();

            if(db.countListCheckList(data.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end))>0){
                binding.lbTitleNotificationMonthItemRowSlider.setVisibility(View.VISIBLE);
                binding.lbNotificationMonthItemRowSlider.setVisibility(View.VISIBLE);
            }
            else{
                binding.lbTitleNotificationMonthItemRowSlider.setVisibility(View.GONE);
                binding.lbNotificationMonthItemRowSlider.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    void loadDay(ItemRowSliderBinding binding){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        String StartDate = sdf0.format(cal.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date end = c.getTime();

        if(db.countKuesionerResultByIdUser(data.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end))>0){
            binding.lbTitleNotificationDayItemRowSlider.setVisibility(View.VISIBLE);
            binding.lbNotificationDayItemRowSlider.setVisibility(View.VISIBLE);
        }
        else{
            binding.lbTitleNotificationDayItemRowSlider.setVisibility(View.GONE);
            binding.lbNotificationDayItemRowSlider.setVisibility(View.GONE);
        }
    }

    public int getCount() {
        return list_slide.size();
    }
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemRowSliderBinding binding = ItemRowSliderBinding.inflate(layoutInflater, container, false);

        if(list_slide.get(position).equals("slide 1")){
            binding.layoutPage1ItemRowSlider.setVisibility(View.VISIBLE);
            binding.layoutPage2ItemRowSlider.setVisibility(View.GONE);
        }
        else if(list_slide.get(position).equals("slide 2")){
            binding.layoutPage1ItemRowSlider.setVisibility(View.GONE);
            binding.layoutPage2ItemRowSlider.setVisibility(View.VISIBLE);
        }
        else{
            binding.layoutPage1ItemRowSlider.setVisibility(View.GONE);
            binding.layoutPage2ItemRowSlider.setVisibility(View.GONE);
        }

        loadMonth(binding);
        loadDay(binding);
        binding.layoutChecklistItemRowSlider.setOnClickListener(v -> listener.toCheckList());
        binding.layoutQuesionerItemRowSlider.setOnClickListener(v -> listener.toQuesioner());
        binding.layoutTodolistItemRowSlider.setOnClickListener(v -> listener.toTodolist());

        ViewPager vp = (ViewPager) container;
        vp.addView(binding.getRoot(), 0);
        return binding.getRoot();
    }
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
