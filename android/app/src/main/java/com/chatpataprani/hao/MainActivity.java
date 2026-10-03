package com.chatpataprani.hao;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class MainActivity extends Activity {
    LinearLayout root, results; EditText input; TextView status, label; Button numberTab, aadhaarTab, search; String kind="number";
    final int WHITE=Color.rgb(242,244,247), MUTED=Color.rgb(150,158,174), BG=Color.rgb(8,10,15), PANEL=Color.rgb(16,19,26);

    @Override public void onCreate(Bundle b){ super.onCreate(b); build(); }

    TextView tv(String s,int size,int color){ TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); t.setPadding(0,0,0,8); return t; }
    GradientDrawable bg(int color,int radius){ GradientDrawable g=new GradientDrawable(); g.setColor(color); g.setCornerRadius(radius); g.setStroke(1,Color.rgb(42,48,59)); return g; }

    void build(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,34,28,28); root.setBackgroundColor(BG);
        ScrollView scroll=new ScrollView(this); scroll.addView(root);
        TextView brand=tv("HAO",12,MUTED); brand.setTypeface(Typeface.DEFAULT,Typeface.BOLD); root.addView(brand);
        TextView title=tv("API Search",32,WHITE); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD); root.addView(title);
        root.addView(tv("Search the provided test Both-db API.",14,MUTED));
        LinearLayout tabs=new LinearLayout(this); tabs.setOrientation(LinearLayout.HORIZONTAL); tabs.setPadding(0,18,0,10);
        numberTab=tab("Number"); aadhaarTab=tab("Aadhaar"); tabs.addView(numberTab,new LinearLayout.LayoutParams(0,52,1)); tabs.addView(aadhaarTab,new LinearLayout.LayoutParams(0,52,1)); root.addView(tabs);
        label=tv("Number",13,MUTED); root.addView(label);
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        input=new EditText(this); input.setSingleLine(true); input.setTextColor(WHITE); input.setHintTextColor(MUTED); input.setHint("Enter test number"); input.setPadding(18,0,18,0); input.setBackground(bg(PANEL,12));
        row.addView(input,new LinearLayout.LayoutParams(0,56,1));
        search=new Button(this); search.setText("SEARCH"); search.setTextColor(Color.BLACK); search.setTextSize(12); search.setTypeface(Typeface.DEFAULT,Typeface.BOLD); search.setBackground(bg(Color.WHITE,12)); LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(120,56); sp.setMargins(10,0,0,0); row.addView(search,sp); root.addView(row);
        status=tv("",13,MUTED); status.setPadding(0,10,0,4); root.addView(status);
        results=new LinearLayout(this); results.setOrientation(LinearLayout.VERTICAL); results.setPadding(0,12,0,0); root.addView(results);
        numberTab.setOnClickListener(v->select("number")); aadhaarTab.setOnClickListener(v->select("aadhar")); search.setOnClickListener(v->lookup());
        select("number"); setContentView(scroll);
    }

    Button tab(String s){ Button b=new Button(this); b.setText(s); b.setTextSize(12); b.setAllCaps(false); b.setTextColor(MUTED); b.setBackground(bg(PANEL,12)); return b; }
    void select(String k){ kind=k; boolean a=k.equals("aadhar"); label.setText(a?"Aadhaar":"Number"); input.setHint(a?"Enter test Aadhaar":"Enter test number"); numberTab.setTextColor(a?MUTED:WHITE); aadhaarTab.setTextColor(a?WHITE:MUTED); input.setText(""); results.removeAllViews(); status.setText(""); }

    void lookup(){
        final String value=input.getText().toString().trim(); if(value.isEmpty()){status.setText("Enter a value first.");return;}
        search.setEnabled(false); status.setText("Searching…"); results.removeAllViews();
        new Thread(()->{
            try{
                String path=kind.equals("aadhar")?"/aadhar=":"/number=";
                URL u=new URL("https://both-db.vercel.app"+path+URLEncoder.encode(value,"UTF-8"));
                HttpURLConnection c=(HttpURLConnection)u.openConnection(); c.setRequestMethod("GET"); c.setRequestProperty("Accept","application/json"); c.setConnectTimeout(15000); c.setReadTimeout(20000);
                int code=c.getResponseCode(); InputStream is=code>=400?c.getErrorStream():c.getInputStream(); BufferedReader r=new BufferedReader(new InputStreamReader(is)); StringBuilder sb=new StringBuilder(); String line; while((line=r.readLine())!=null)sb.append(line);
                final String raw=sb.toString(); runOnUiThread(()->showResponse(code,raw));
            }catch(Exception e){runOnUiThread(()->{status.setText("Error: "+e.getMessage());search.setEnabled(true);});}
        }).start();
    }

    void showResponse(int code,String raw){
        search.setEnabled(true); status.setText(code>=200&&code<300?"Search complete":"API returned HTTP "+code);
        try{
            JSONObject obj=new JSONObject(raw); JSONArray arr=obj.optJSONArray("results");
            TextView meta=tv(obj.optString("type",kind)+" lookup  •  "+obj.optInt("count",arr==null?0:arr.length())+" result(s)",13,MUTED); results.addView(meta);
            if(arr!=null) for(int i=0;i<arr.length();i++) addCard(arr.getJSONObject(i),i+1);
            TextView rawTitle=tv("RAW API RESPONSE",11,MUTED); rawTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD); rawTitle.setPadding(0,18,0,6); results.addView(rawTitle);
            TextView rawView=tv(obj.toString(2),11,Color.rgb(190,198,210)); rawView.setTextIsSelectable(true); rawView.setPadding(14,14,14,14); rawView.setBackground(bg(Color.rgb(11,14,19),10)); results.addView(rawView);
        }catch(Exception e){ TextView t=tv(raw,12,WHITE); t.setTextIsSelectable(true); results.addView(t); }
    }

    void addCard(JSONObject o,int n){
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(14,14,14,8); card.setBackground(bg(Color.rgb(11,14,19),14));
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2); cp.setMargins(0,8,0,0); results.addView(card,cp);
        TextView h=tv("Result "+n+"   •   source: modihh ji",12,MUTED); card.addView(h);
        Iterator<String> it=o.keys(); while(it.hasNext()){String k=it.next(); Object v=o.opt(k); TextView f=tv(k+"\n"+(v==JSONObject.NULL?"null":String.valueOf(v)),13,WHITE); f.setPadding(0,7,0,7); card.addView(f);}
    }
}
