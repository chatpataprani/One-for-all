package com.chatpataprani.hao;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class MainActivity extends Activity {
    // Public app server only. The upstream Both-db endpoint is NEVER shipped in the Android UI/client.
    static final String BACKEND_URL = "https://one-for-all.vercel.app/api/lookup";
    static final String DEV_NAME = "Chatpataprani";
    static final String GITHUB_URL = "https://github.com/chatpataprani/One-for-all";
    static final String INSTAGRAM_URL = "https://instagram.com/chatpataprani";

    LinearLayout root, results;
    EditText input;
    TextView status, label;
    Button numberTab, aadhaarTab, search;
    String kind="number";
    final int WHITE=Color.rgb(242,244,247), MUTED=Color.rgb(150,158,174), BG=Color.rgb(8,10,15), PANEL=Color.rgb(16,19,26);

    @Override public void onCreate(Bundle b){ super.onCreate(b); build(); }

    TextView tv(String s,int size,int color){
        TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        t.setPadding(0,0,0,8); return t;
    }
    GradientDrawable bg(int color,int radius){
        GradientDrawable g=new GradientDrawable(); g.setColor(color); g.setCornerRadius(radius);
        g.setStroke(1,Color.rgb(42,48,59)); return g;
    }
    Button action(String text){
        Button b=new Button(this); b.setText(text); b.setTextSize(12); b.setAllCaps(false);
        b.setTextColor(WHITE); b.setBackground(bg(PANEL,12)); return b;
    }

    void build(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(22,28,22,30); root.setBackgroundColor(BG);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); scroll.addView(root);

        LinearLayout top=new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL);
        TextView brand=tv("HAO",15,WHITE); brand.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        top.addView(brand,new LinearLayout.LayoutParams(0,42,1));
        Button settings=action("⚙");
        top.addView(settings,new LinearLayout.LayoutParams(58,42));
        root.addView(top);

        TextView title=tv("Search smarter.",30,WHITE); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        root.addView(title);
        root.addView(tv("Fast lookup • clean results • private upstream routing",14,MUTED));

        LinearLayout tabs=new LinearLayout(this); tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setPadding(0,18,0,10);
        numberTab=tab("Number"); aadhaarTab=tab("Aadhaar");
        tabs.addView(numberTab,new LinearLayout.LayoutParams(0,52,1));
        LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(0,52,1); ap.setMargins(8,0,0,0);
        tabs.addView(aadhaarTab,ap); root.addView(tabs);

        label=tv("NUMBER LOOKUP",11,MUTED); label.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        root.addView(label);
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        input=new EditText(this); input.setSingleLine(true); input.setTextColor(WHITE);
        input.setHintTextColor(MUTED); input.setHint("Enter a test number"); input.setTextSize(15);
        input.setPadding(16,0,16,0); input.setBackground(bg(PANEL,14));
        row.addView(input,new LinearLayout.LayoutParams(0,58,1));
        search=new Button(this); search.setText("Search"); search.setTextColor(Color.BLACK);
        search.setTextSize(12); search.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        search.setBackground(bg(Color.WHITE,14));
        LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(112,58); sp.setMargins(9,0,0,0);
        row.addView(search,sp); root.addView(row);

        status=tv("",13,MUTED); status.setPadding(2,12,2,4); root.addView(status);
        results=new LinearLayout(this); results.setOrientation(LinearLayout.VERTICAL);
        results.setPadding(0,8,0,0); root.addView(results);

        TextView privacy=tv("Your query is sent to the HAO server. The upstream API address is not displayed in the app.",11,MUTED);
        privacy.setPadding(2,20,2,0); root.addView(privacy);

        numberTab.setOnClickListener(v->select("number"));
        aadhaarTab.setOnClickListener(v->select("aadhar"));
        search.setOnClickListener(v->lookup());
        settings.setOnClickListener(v->showSettings());
        input.setOnEditorActionListener((v,id,event)->{ lookup(); return true; });

        select("number"); setContentView(scroll);
    }

    Button tab(String s){
        Button b=new Button(this); b.setText(s); b.setTextSize(12); b.setAllCaps(false);
        b.setTextColor(MUTED); b.setBackground(bg(PANEL,12)); return b;
    }

    void select(String k){
        kind=k; boolean a=k.equals("aadhar");
        label.setText(a?"AADHAAR LOOKUP":"NUMBER LOOKUP");
        input.setHint(a?"Enter a test Aadhaar":"Enter a test number");
        numberTab.setTextColor(a?MUTED:WHITE); aadhaarTab.setTextColor(a?WHITE:MUTED);
        input.setText(""); results.removeAllViews(); status.setText("");
    }

    void lookup(){
        final String value=input.getText().toString().trim();
        if(value.isEmpty()){status.setText("Enter a value first.");return;}
        search.setEnabled(false); status.setText("Searching…"); results.removeAllViews();

        new Thread(()->{
            HttpURLConnection c=null;
            try{
                URL u=new URL(BACKEND_URL);
                c=(HttpURLConnection)u.openConnection();
                c.setRequestMethod("POST"); c.setDoOutput(true);
                c.setRequestProperty("Accept","application/json");
                c.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                c.setConnectTimeout(15000); c.setReadTimeout(20000);
                JSONObject body=new JSONObject(); body.put("kind",kind); body.put("value",value);
                OutputStream os=c.getOutputStream(); os.write(body.toString().getBytes("UTF-8")); os.close();

                int code=c.getResponseCode();
                InputStream is=code>=400?c.getErrorStream():c.getInputStream();
                BufferedReader r=new BufferedReader(new InputStreamReader(is==null?new ByteArrayInputStream(new byte[0]):is));
                StringBuilder sb=new StringBuilder(); String line;
                while((line=r.readLine())!=null) sb.append(line);
                final String raw=sb.toString();
                runOnUiThread(()->showResponse(code,raw));
            }catch(Exception e){
                runOnUiThread(()->{status.setText("Connection error. Check the server and try again.");search.setEnabled(true);});
            }finally{ if(c!=null)c.disconnect(); }
        }).start();
    }

    void showResponse(int code,String raw){
        search.setEnabled(true);
        status.setText(code>=200&&code<300?"Search complete":"Request failed • HTTP "+code);
        try{
            JSONObject obj=new JSONObject(raw); JSONArray arr=obj.optJSONArray("results");
            int count=arr==null?0:arr.length();
            TextView meta=tv((kind.equals("aadhar")?"Aadhaar":"Number")+" • "+count+" result(s)",13,MUTED);
            results.addView(meta);
            if(arr!=null) for(int i=0;i<arr.length();i++) addCard(arr.getJSONObject(i),i+1);
            TextView rawTitle=tv("RESPONSE DATA",11,MUTED); rawTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            rawTitle.setPadding(0,18,0,6); results.addView(rawTitle);
            TextView rawView=tv(obj.toString(2),11,Color.rgb(190,198,210)); rawView.setTextIsSelectable(true);
            rawView.setPadding(14,14,14,14); rawView.setBackground(bg(Color.rgb(11,14,19),10));
            results.addView(rawView);
        }catch(Exception e){
            TextView t=tv(raw,12,WHITE); t.setTextIsSelectable(true); t.setPadding(14,14,14,14);
            t.setBackground(bg(Color.rgb(11,14,19),10)); results.addView(t);
        }
    }

    void addCard(JSONObject o,int n){
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(16,14,16,10); card.setBackground(bg(Color.rgb(11,14,19),14));
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2); cp.setMargins(0,8,0,0);
        results.addView(card,cp);
        TextView h=tv("Result "+n,12,MUTED); h.setTypeface(Typeface.DEFAULT,Typeface.BOLD); card.addView(h);
        Iterator<String> it=o.keys();
        while(it.hasNext()){
            String k=it.next(); Object v=o.opt(k);
            TextView f=tv(k.toUpperCase()+"\n"+(v==JSONObject.NULL?"null":String.valueOf(v)),13,WHITE);
            f.setPadding(0,8,0,7); card.addView(f);
        }
    }

    void showSettings(){
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(26,20,26,8);
        TextView h=tv("HAO Settings",22,WHITE); h.setTypeface(Typeface.DEFAULT,Typeface.BOLD); box.addView(h);
        box.addView(tv("Simple controls and developer info.",13,MUTED));
        addSetting(box,"Clear search","Clear the current result screen",()->{results.removeAllViews();status.setText("");});
        addSetting(box,"GitHub","Open the HAO source repository",()->open(GITHUB_URL));
        addSetting(box,"Instagram","Open @chatpataprani",()->open(INSTAGRAM_URL));
        addSetting(box,"Developer","Chatpataprani",null);
        addSetting(box,"Privacy","Only the HAO server endpoint is used by this app. The upstream API address is kept server-side.",null);
        AlertDialog d=new AlertDialog.Builder(this).setView(box).setNegativeButton("Close",null).create();
        d.show();
    }

    void addSetting(LinearLayout box,String title,String desc,final Runnable action){
        Button b=action(title+"\n"+desc);
        b.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL); b.setPadding(16,8,16,8);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,64); p.setMargins(0,8,0,0);
        box.addView(b,p); if(action!=null)b.setOnClickListener(v->action.run());
    }

    Button action(String text){ Button b=new Button(this); b.setText(text); b.setTextSize(12); b.setAllCaps(false); b.setTextColor(WHITE); b.setBackground(bg(PANEL,12)); return b; }

    void open(String url){
        try{ startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }catch(Exception ignored){}
    }
}
