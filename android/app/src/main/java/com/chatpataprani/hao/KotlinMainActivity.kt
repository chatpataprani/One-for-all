package com.chatpataprani.hao

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

class KotlinMainActivity : Activity() {
    private val bg = Color.rgb(10,11,15)
    private val surface = Color.rgb(20,22,28)
    private val surface2 = Color.rgb(28,31,39)
    private val border = Color.rgb(52,56,68)
    private val text = Color.rgb(246,247,250)
    private val muted = Color.rgb(157,163,177)
    private val accent = Color.rgb(228,231,238)
    private var mode = "number"
    private var currentScreen = "home"
    private lateinit var query: EditText
    private lateinit var results: LinearLayout
    private lateinit var status: TextView
    private val backendUrl = BuildConfig.BACKEND_URL

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        home()
    }

    private fun tv(s: String, size: Float, color: Int = text) =
        TextView(this).apply { text=s; textSize=size; setTextColor(color) }

    private fun rounded(color: Int, radius: Float) =
        android.graphics.drawable.GradientDrawable().apply {
            setColor(color); cornerRadius=radius; setStroke(1,border)
        }

    private fun button(s: String, filled: Boolean=false) =
        Button(this).apply {
            text=s; textSize=13f; isAllCaps=false
            setTextColor(if (filled) Color.rgb(15,16,20) else text)
            background=rounded(if (filled) accent else surface2,18f)
            setPadding(16,0,16,0)
        }

    private fun page() = LinearLayout(this).apply {
        orientation=LinearLayout.VERTICAL; setPadding(18,10,18,14); setBackgroundColor(bg)
    }

    private fun card() = LinearLayout(this).apply {
        orientation=LinearLayout.VERTICAL; setPadding(17,16,17,17); background=rounded(surface,20f)
    }

    private fun lp(top:Int=0,bottom:Int=0)=LinearLayout.LayoutParams(-1,-2).apply {
        setMargins(0,top,0,bottom)
    }

    private fun header(p:LinearLayout, section:String, settings:Boolean=true) {
        val row=LinearLayout(this).apply { gravity=Gravity.CENTER_VERTICAL }
        row.addView(tv("HAO",14f).apply { setTypeface(null,Typeface.BOLD) },LinearLayout.LayoutParams(-2,48))
        row.addView(tv("  /  $section",13f,muted),LinearLayout.LayoutParams(0,48,1f))
        if(settings) row.addView(button("⋮").apply { setOnClickListener { settings() } },LinearLayout.LayoutParams(52,48))
        p.addView(row)
    }

    private fun title(p:LinearLayout,a:String,b:String) {
        p.addView(tv(a,30f).apply { setTypeface(null,Typeface.BOLD); setPadding(0,8,0,3) })
        p.addView(tv(b,14f,muted).apply { setPadding(0,0,0,8) })
    }

    private fun shell(content:LinearLayout,screen:String) {
        currentScreen=screen
        val root=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setBackgroundColor(bg) }
        root.addView(ScrollView(this).apply { addView(content) },LinearLayout.LayoutParams(-1,0,1f))
        val nav=LinearLayout(this).apply { setPadding(10,8,10,8); setBackgroundColor(bg) }
        listOf("Home","Search","Tools").forEachIndexed { i,label ->
            val active=(i==0&&screen=="home")||(i==1&&screen=="search")||(i==2&&screen=="tools")
            val b=button(label,active)
            nav.addView(b,LinearLayout.LayoutParams(0,54,1f).apply { if(i>0) leftMargin=7 })
            b.setOnClickListener { when(i){0->home();1->search();else->tools()} }
        }
        root.addView(nav,LinearLayout.LayoutParams(-1,70))
        setContentView(root)
    }

    private fun home() {
        val p=page(); header(p,"home"); title(p,"Search smarter.","A focused utility workspace with fast tools and clear descriptions.")
        val search=card()
        search.addView(tv("LOOKUP",11f,muted).apply { setTypeface(null,Typeface.BOLD) })
        search.addView(tv("Number & Aadhaar",22f).apply { setTypeface(null,Typeface.BOLD); setPadding(0,7,0,3) })
        search.addView(tv("Search the supplied test database. The upstream service is not embedded in the APK.",13f,muted))
        search.addView(button("Open search",true).apply { setOnClickListener { search() } },lp(15))
        p.addView(search,lp(8))
        val tools=card()
        tools.addView(tv("TOOLS",11f,muted).apply { setTypeface(null,Typeface.BOLD) })
        tools.addView(tv("Every tool has a name + what it does",21f).apply { setTypeface(null,Typeface.BOLD); setPadding(0,7,0,3) })
        tools.addView(tv("Browse utility cards for text, files, QR, images, PDFs and more.",13f,muted))
        tools.addView(button("Browse all tools").apply { setOnClickListener { tools() } },lp(15))
        p.addView(tools,lp(10))
        p.addView(tv("Developer  •  Chatpataprani",12f,muted).apply { setPadding(2,22,2,8) })
        shell(p,"home")
    }

    private fun search() {
        val p=page(); header(p,"search"); title(p,"Database search","Choose a mode, enter a value, then view structured results.")
        val tabs=LinearLayout(this)
        val n=button("Number",mode=="number"); val a=button("Aadhaar",mode=="aadhar")
        tabs.addView(n,LinearLayout.LayoutParams(0,50,1f))
        tabs.addView(a,LinearLayout.LayoutParams(0,50,1f).apply { leftMargin=8 })
        p.addView(tabs,lp(4,8))
        n.setOnClickListener { mode="number"; search() }; a.setOnClickListener { mode="aadhar"; search() }
        val box=card()
        box.addView(tv(if(mode=="number")"NUMBER" else "AADHAAR",11f,muted).apply { setTypeface(null,Typeface.BOLD) })
        query=EditText(this).apply {
            singleLine=true; textSize=16f; setTextColor(text); setHintTextColor(muted)
            hint=if(mode=="number")"Enter test number" else "Enter test Aadhaar"
            setPadding(15,0,15,0); background=rounded(Color.rgb(12,14,19),15f)
        }
        box.addView(query,lp(8,9))
        box.addView(button("Search",true).apply { setOnClickListener { lookup() } },LinearLayout.LayoutParams(-1,52))
        status=tv("",13f,muted).apply { setPadding(2,12,2,2) }
        box.addView(status); p.addView(box)
        results=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL }; p.addView(results)
        shell(p,"search")
    }

    private fun lookup() {
        val value=query.text.toString().trim()
        if(value.isEmpty()){status.text="Enter a value first.";return}
        if(backendUrl.isBlank()){status.text="Backend is not configured. No upstream API address is stored in this APK.";return}
        status.text="Searching…"; results.removeAllViews()
        Thread {
            var c:HttpURLConnection?=null
            try {
                val endpoint=backendUrl.trimEnd('/')+"/lookup?type=$mode&value="+URLEncoder.encode(value,"UTF-8")
                c=URL(endpoint).openConnection() as HttpURLConnection
                c.requestMethod="GET"; c.connectTimeout=15000; c.readTimeout=20000
                val code=c.responseCode
                val stream=if(code>=400)c.errorStream else c.inputStream
                val raw=stream?.bufferedReader()?.use{it.readText()}?:""
                runOnUiThread{render(code,raw)}
            } catch(_:Exception){runOnUiThread{status.text="Connection failed. Check the server."}}
            finally{c?.disconnect()}
        }.start()
    }

    private fun render(code:Int,raw:String) {
        status.text=if(code in 200..299)"Search complete" else "Request failed"
        if(raw.isBlank()){info("No response","The server returned an empty response.");return}
        try {
            val obj=JSONObject(raw); val arr=obj.optJSONArray("results")
            if(arr!=null){
                results.addView(tv(arr.length().toString()+" result(s)",13f,muted),lp(8,2))
                for(i in 0 until arr.length()) resultCard(arr.get(i),i+1)
            } else resultCard(obj,1)
        } catch(_ :Exception){info("Response",raw)}
    }

    private fun resultCard(item:Any,number:Int) {
        val c=card(); c.addView(tv("RESULT $number",11f,muted).apply{setTypeface(null,Typeface.BOLD)})
        if(item is JSONObject){
            val it=item.keys()
            while(it.hasNext()){
                val key=it.next()
                c.addView(tv(key.uppercase(Locale.US),10f,muted).apply{setPadding(0,10,0,1)})
                c.addView(tv(item.optString(key),14f))
            }
        } else c.addView(tv(item.toString(),14f))
        results.addView(c,lp(8))
    }

    private fun info(name:String,description:String) {
        val c=card()
        c.addView(tv(name,16f).apply{setTypeface(null,Typeface.BOLD)})
        c.addView(tv(description,13f,muted).apply{setPadding(0,5,0,0)})
        results.addView(c,lp(8))
    }

    private data class Tool(val name:String,val description:String,val action:String)

    private fun toolList()=listOf(
        Tool("Number Lookup","Search the supplied test database by phone number and show structured fields.","search"),
        Tool("Aadhaar Lookup","Search the supplied test database by Aadhaar value and show returned fields.","aadhar"),
        Tool("QR Generator","Create a QR code from text or a URL. Runs locally; no upload is required.","info"),
        Tool("QR Scanner","Read a QR code with the camera and copy the decoded text.","info"),
        Tool("Text Formatter","Clean, trim, uppercase, lowercase and normalize pasted text.","info"),
        Tool("JSON Viewer","Paste JSON and inspect it as readable key/value data.","info"),
        Tool("Base64","Encode text to Base64 or decode Base64 back to readable text.","info"),
        Tool("URL Encoder","Encode or decode URL text safely for query parameters.","info"),
        Tool("Hash Generator","Create common cryptographic hashes from text locally.","info"),
        Tool("Image Tools","Resize, convert and inspect image files without sending them to a server.","info"),
        Tool("PDF Tools","Open, inspect and organize PDF files available on the device.","info"),
        Tool("File Tools","Browse selected files and view basic filename, type and size information.","info"),
        Tool("Text Counter","Count characters, words and lines in pasted text.","info"),
        Tool("Color Picker","Inspect a color and copy its HEX/RGB representation.","info"),
        Tool("Settings","Manage app preferences, developer links, privacy information and reset options.","settings")
    )

    private fun tools() {
        val p=page(); header(p,"tools",false); title(p,"Tools","Each option tells you exactly what it is for before you open it.")
        toolList().forEach { tool ->
            val c=card(); val row=LinearLayout(this)
            row.addView(tv(tool.name,17f).apply{setTypeface(null,Typeface.BOLD)},LinearLayout.LayoutParams(0,-2,1f))
            row.addView(button("Open").apply{setOnClickListener{openTool(tool)}},LinearLayout.LayoutParams(78,46))
            c.addView(row); c.addView(tv(tool.description,13f,muted).apply{setPadding(0,6,0,0)})
            p.addView(c,lp(7))
        }
        shell(p,"tools")
    }

    private fun openTool(tool:Tool) {
        when(tool.action){
            "search"->{mode="number";search()}
            "aadhar"->{mode="aadhar";search()}
            "settings"->settings()
            else->AlertDialog.Builder(this).setTitle(tool.name).setMessage(tool.description+"\\n\\nThis native tool card is included in HAO's tool catalogue.").setPositiveButton("OK",null).show()
        }
    }

    private fun settings() {
        val box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,18,24,8)}
        box.addView(tv("HAO Settings",24f).apply{setTypeface(null,Typeface.BOLD)})
        box.addView(tv("Native Kotlin utility workspace",13f,muted))
        setting(box,"Developer","Chatpataprani",null)
        setting(box,"GitHub","Open the HAO project","https://github.com/chatpataprani/One-for-all")
        setting(box,"Instagram","Open @chatpataprani","https://instagram.com/chatpataprani")
        setting(box,"Privacy","The APK does not contain the upstream database address. A proxy is required for lookup requests.",null)
        setting(box,"About","HAO 3.0 • Kotlin • ReelDrop-inspired navigation",null)
        AlertDialog.Builder(this).setView(box).setNegativeButton("Close",null).show()
    }

    private fun setting(box:LinearLayout,name:String,description:String,url:String?) {
        val b=button("$name\\n$description"); b.gravity=Gravity.LEFT or Gravity.CENTER_VERTICAL
        b.setOnClickListener{if(url!=null)startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(url)))}
        box.addView(b,lp(7))
    }

    override fun onBackPressed(){if(currentScreen!="home")home() else super.onBackPressed()}
}
