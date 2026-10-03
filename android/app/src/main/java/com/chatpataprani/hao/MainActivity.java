package com.chatpataprani.hao;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

public class MainActivity extends Activity {
    private static final String NUMBER_API = "https://both-db.vercel.app/number=";
    private static final String AADHAAR_API = "https://both-db.vercel.app/aadhar=";
    private static final String GITHUB_URL = "https://github.com/chatpataprani/One-for-all";
    private static final String INSTAGRAM_URL = "https://instagram.com/chatpataprani";

    private final int BG=Color.rgb(10,11,15), SURFACE=Color.rgb(19,21,27), SURFACE_2=Color.rgb(25,28,36);
    private final int BORDER=Color.rgb(48,52,63), TEXT=Color.rgb(246,247,250), MUTED=Color.rgb(157,163,177), ACCENT=Color.rgb(225,228,235);
    int screen=0;
    String kind="number";
    EditText query;
    TextView status;
    LinearLayout results;
    Button numberTab,aadhaarTab;
    WebView toolsWeb;

    @Override public void onCreate(Bundle state){super.onCreate(state);getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(BG);showHome();}

    TextView text(String value,float size,int color){TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);return t;}
    GradientDrawable rounded(int color,float radius){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(radius);g.setStroke(1,BORDER);return g;}
    Button button(String value,boolean filled){Button b=new Button(this);b.setText(value);b.setTextSize(13);b.setAllCaps(false);b.setTextColor(filled?Color.rgb(15,16,20):TEXT);b.setBackground(rounded(filled?ACCENT:SURFACE_2,16));b.setPadding(16,0,16,0);return b;}
    LinearLayout page(){LinearLayout p=new LinearLayout(this);p.setOrientation(LinearLayout.VERTICAL);p.setBackgroundColor(BG);p.setPadding(18,12,18,12);return p;}
    TextView title(String value){TextView t=text(value,30,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(0,8,0,4);return t;}
    TextView subtitle(String value){TextView t=text(value,14,MUTED);t.setPadding(0,0,0,8);return t;}
    int WRAP(){return LinearLayout.LayoutParams.WRAP_CONTENT;}
    LinearLayout.LayoutParams margin(int l,int t,int r,int b){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(l,t,r,b);return p;}
    LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(17,16,17,17);c.setBackground(rounded(SURFACE,20));return c;}

    void baseTop(LinearLayout p,String section,boolean settings){
        LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);
        TextView brand=text("HAO",14,TEXT);brand.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        TextView sec=text("  /  "+section,13,MUTED);
        bar.addView(brand,new LinearLayout.LayoutParams(WRAP(),44));bar.addView(sec,new LinearLayout.LayoutParams(0,44,1));
        if(settings){Button s=button("⋮",false);bar.addView(s,new LinearLayout.LayoutParams(52,44));s.setOnClickListener(v->showSettings());}
        p.addView(bar);
    }

    void setScreen(LinearLayout content,int which){
        screen=which;ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.addView(content);
        LinearLayout shell=new LinearLayout(this);shell.setOrientation(LinearLayout.VERTICAL);shell.setBackgroundColor(BG);
        shell.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));shell.addView(bottomNav(),new LinearLayout.LayoutParams(-1,70));setContentView(shell);
    }

    LinearLayout bottomNav(){
        LinearLayout nav=new LinearLayout(this);nav.setGravity(Gravity.CENTER);nav.setPadding(10,8,10,8);nav.setBackgroundColor(BG);
        String[] labels={"Home","Search","Tools"};
        for(int i=0;i<labels.length;i++){final int idx=i;Button b=button(labels[i],screen==i);LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(0,54,1);if(i>0)bp.setMargins(7,0,0,0);nav.addView(b,bp);b.setOnClickListener(v->{if(idx==0)showHome();else if(idx==1)showSearch();else showTools();});}
        return nav;
    }

    void showHome(){
        LinearLayout p=page();baseTop(p,"home",true);p.addView(title("Search smarter."));p.addView(subtitle("A clean utility workspace inspired by ReelDrop-style navigation."));
        LinearLayout hero=card();TextView h=text("FAST LOOKUP",11,MUTED);h.setTypeface(Typeface.DEFAULT,Typeface.BOLD);hero.addView(h);
        TextView ht=text("Number & Aadhaar",22,TEXT);ht.setTypeface(Typeface.DEFAULT,Typeface.BOLD);ht.setPadding(0,7,0,3);hero.addView(ht);
        hero.addView(text("Use the supplied test database directly from the app. No HAO server is required.",13,MUTED));
        Button open=button("Open search",true);hero.addView(open,margin(0,16,0,0));open.setOnClickListener(v->showSearch());p.addView(hero,margin(0,14,0,0));
        LinearLayout tools=card();tools.addView(text("TOOLS",11,MUTED));TextView tt=text("50+ local utilities",21,TEXT);tt.setTypeface(Typeface.DEFAULT,Typeface.BOLD);tt.setPadding(0,7,0,3);tools.addView(tt);
        tools.addView(text("PDF, image, QR, text and file tools bundled from your supplied ZIP.",13,MUTED));
        Button tb=button("Browse tools",false);tools.addView(tb,margin(0,16,0,0));tb.setOnClickListener(v->showTools());p.addView(tools,margin(0,12,0,0));
        TextView dev=text("Developer  •  Chatpataprani",12,MUTED);dev.setPadding(2,22,2,8);p.addView(dev);setScreen(p,0);
    }

    void showSearch(){
        LinearLayout p=page();baseTop(p,"search",true);p.addView(title("Database search"));
        p.addView(subtitle("Choose a mode, enter a synthetic/test value, and view structured results."));
        LinearLayout tabs=new LinearLayout(this);tabs.setPadding(0,8,0,12);
        numberTab=button("Number",kind.equals("number"));aadhaarTab=button("Aadhaar",kind.equals("aadhar"));
        tabs.addView(numberTab,new LinearLayout.LayoutParams(0,50,1));LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(0,50,1);ap.setMargins(8,0,0,0);tabs.addView(aadhaarTab,ap);p.addView(tabs);
        numberTab.setOnClickListener(v->{kind="number";updateMode();});aadhaarTab.setOnClickListener(v->{kind="aadhar";updateMode();});
        LinearLayout box=card();TextView l=text(kind.equals("aadhar")?"AADHAAR":"NUMBER",11,MUTED);l.setTypeface(Typeface.DEFAULT,Typeface.BOLD);box.addView(l);
        query=new EditText(this);query.setSingleLine(true);query.setTextColor(TEXT);query.setHintTextColor(MUTED);query.setTextSize(16);query.setHint(kind.equals("aadhar")?"Enter test Aadhaar":"Enter test number");query.setPadding(15,0,15,0);query.setBackground(rounded(Color.rgb(12,14,19),15));
        box.addView(query,margin(0,8,0,9));Button go=button("Search",true);box.addView(go,new LinearLayout.LayoutParams(-1,52));go.setOnClickListener(v->lookup());
        query.setOnEditorActionListener((v,id,e)->{lookup();return true;});status=text("",13,MUTED);status.setPadding(2,12,2,2);box.addView(status);p.addView(box);
        results=new LinearLayout(this);results.setOrientation(LinearLayout.VERTICAL);results.setPadding(0,8,0,0);p.addView(results);setScreen(p,1);
    }

    void updateMode(){
        boolean n=kind.equals("number");numberTab.setTextColor(n?Color.rgb(15,16,20):TEXT);numberTab.setBackground(rounded(n?ACCENT:SURFACE_2,16));
        aadhaarTab.setTextColor(!n?Color.rgb(15,16,20):TEXT);aadhaarTab.setBackground(rounded(!n?ACCENT:SURFACE_2,16));
        query.setHint(n?"Enter test number":"Enter test Aadhaar");query.setText("");results.removeAllViews();status.setText("");
    }

    void lookup(){
        final String value=query.getText().toString().trim();if(value.isEmpty()){status.setText("Enter a value first.");return;}
        final String target=(kind.equals("aadhar")?AADHAAR_API:NUMBER_API)+URLEncoder.encode(value,StandardCharsets.UTF_8);
        status.setText("Searching…");results.removeAllViews();
        new Thread(()->{HttpURLConnection c=null;try{
            URL u=new URL(target);c=(HttpURLConnection)u.openConnection();c.setRequestMethod("GET");c.setRequestProperty("Accept","application/json");c.setConnectTimeout(15000);c.setReadTimeout(20000);
            int code=c.getResponseCode();InputStream is=code>=400?c.getErrorStream():c.getInputStream();BufferedReader r=new BufferedReader(new InputStreamReader(is==null?new ByteArrayInputStream(new byte[0]):is));StringBuilder sb=new StringBuilder();String line;while((line=r.readLine())!=null)sb.append(line);
            final String raw=sb.toString();final int responseCode=code;runOnUiThread(()->renderResponse(responseCode,raw));
        }catch(Exception e){runOnUiThread(()->status.setText("Connection failed. Check your internet connection or the test API."));}finally{if(c!=null)c.disconnect();}}).start();
    }

    void renderResponse(int code,String raw){
        status.setText(code>=200&&code<300?"Search complete":"Request failed • HTTP "+code);
        if(raw==null||raw.isEmpty()){addInfoCard("Empty response","The test service returned no body.");return;}
        try{JSONObject obj=new JSONObject(raw);JSONArray arr=obj.optJSONArray("results");
            if(arr!=null){TextView meta=text((kind.equals("aadhar")?"Aadhaar":"Number")+"  •  "+arr.length()+" result(s)",13,MUTED);meta.setPadding(2,8,2,4);results.addView(meta);for(int i=0;i<arr.length();i++)addResultCard(arr.get(i),i+1);}
            else addResultCard(obj,1);
            TextView rawTitle=text("RAW RESPONSE",11,MUTED);rawTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);rawTitle.setPadding(2,18,2,6);results.addView(rawTitle);
            TextView rawView=text(obj.toString(2),11,Color.rgb(193,199,210));rawView.setTextIsSelectable(true);rawView.setPadding(14,14,14,14);rawView.setBackground(rounded(Color.rgb(12,14,19),14));results.addView(rawView,margin(0,0,0,10));
        }catch(Exception e){TextView rawView=text(raw,12,TEXT);rawView.setTextIsSelectable(true);rawView.setPadding(14,14,14,14);rawView.setBackground(rounded(Color.rgb(12,14,19),14));results.addView(rawView);}
    }

    void addResultCard(Object item,int n){
        LinearLayout c=card();TextView h=text("RESULT "+n,11,MUTED);h.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(h);
        if(item instanceof JSONObject){JSONObject o=(JSONObject)item;Iterator<String> it=o.keys();while(it.hasNext()){String k=it.next();Object v=o.opt(k);LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.VERTICAL);row.setPadding(0,10,0,0);row.addView(text(k.toUpperCase(Locale.US),10,MUTED));row.addView(text(v==JSONObject.NULL?"null":String.valueOf(v),14,TEXT));c.addView(row);}}else c.addView(text(String.valueOf(item),14,TEXT));
        results.addView(c,margin(0,8,0,0));
    }

    void addInfoCard(String h,String body){LinearLayout c=card();TextView t=text(h,16,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(t);c.addView(text(body,13,MUTED));results.addView(c,margin(0,8,0,0));}

    void showTools(){
        LinearLayout p=page();baseTop(p,"tools",false);p.addView(title("Tools"));p.addView(subtitle("The supplied ZIP is bundled locally. General utilities run inside the app."));
        toolsWeb=new WebView(this);configureWebView(toolsWeb);String toolsRoot=prepareToolsBundle();
        if(toolsRoot!=null)toolsWeb.loadUrl("file://"+toolsRoot+"/index.html");else p.addView(text("Tools bundle unavailable.",14,TEXT));
        p.addView(toolsWeb,new LinearLayout.LayoutParams(-1,0,1));setContentView(p);screen=2;
    }

    String prepareToolsBundle(){
        try{File root=new File(getFilesDir(),"tools/punjab");File marker=new File(root,"index.html");if(marker.exists())return root.getAbsolutePath();root.mkdirs();
            InputStream in=getAssets().open("tools-bundle.zip");java.util.zip.ZipInputStream zis=new java.util.zip.ZipInputStream(new BufferedInputStream(in));java.util.zip.ZipEntry entry;byte[] buffer=new byte[8192];
            while((entry=zis.getNextEntry())!=null){String name=entry.getName();if(name.startsWith("punjab.pages.dev-main/"))name=name.substring("punjab.pages.dev-main/".length());if(name.isEmpty())continue;
                File out=new File(root,name);String rootPath=root.getCanonicalPath();String outPath=out.getCanonicalPath();if(!outPath.equals(rootPath)&&!outPath.startsWith(rootPath+File.separator))continue;
                if(entry.isDirectory()){out.mkdirs();continue;}File parent=out.getParentFile();if(parent!=null)parent.mkdirs();FileOutputStream fos=new FileOutputStream(out);int n;while((n=zis.read(buffer))>0)fos.write(buffer,0,n);fos.close();}
            zis.close();return root.getAbsolutePath();
        }catch(Exception e){return null;}
    }

    void configureWebView(WebView w){WebSettings s=w.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(true);s.setAllowContentAccess(true);s.setBuiltInZoomControls(false);s.setDisplayZoomControls(false);w.setWebViewClient(new WebViewClient());w.setWebChromeClient(new WebChromeClient());}

    @Override public void onBackPressed(){if(screen==2&&toolsWeb!=null&&toolsWeb.canGoBack()){toolsWeb.goBack();return;}if(screen!=0){showHome();return;}super.onBackPressed();}

    void showSettings(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(24,18,24,8);TextView h=text("HAO Settings",24,TEXT);h.setTypeface(Typeface.DEFAULT,Typeface.BOLD);box.addView(h);box.addView(text("ReelDrop-inspired utility workspace",13,MUTED));
        setting(box,"Developer","Chatpataprani",null);setting(box,"GitHub","Open project repository",()->open(GITHUB_URL));setting(box,"Instagram","Open @chatpataprani",()->open(INSTAGRAM_URL));setting(box,"About","HAO • Number / Aadhaar test lookup • local tools bundle",null);setting(box,"Privacy","The lookup request is sent directly from the app to the supplied test service.",null);
        new AlertDialog.Builder(this).setView(box).setNegativeButton("Close",null).show();
    }
    void setting(LinearLayout box,String a,String b,final Runnable action){Button x=button(a+"\n"+b,false);x.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);x.setPadding(15,5,15,5);box.addView(x,margin(0,7,0,0));if(action!=null)x.setOnClickListener(v->action.run());}
    void open(String url){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception ignored){}}
}