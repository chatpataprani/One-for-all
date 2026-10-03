package com.chatpataprani.hao

import android.app.Activity
import android.app.AlertDialog
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
    private val bg=Color.rgb(10,11,15); private val surface=Color.rgb(20,22,28); private val surface2=Color.rgb(28,31,39)
    private val border=Color.rgb(52,56,68); private val text=Color.rgb(246,247,250); private val muted=Color.rgb(157,163,177); private val accent=Color.rgb(228,231,238)
    private var mode="number"; private var currentScreen="home"; private lateinit var query:EditText; private lateinit var results:LinearLayout; private lateinit var status:TextView
    private val backendUrl=BuildConfig.BACKEND_URL
    data class Tool(val name:String,val description:String,val path:String,val implemented:Boolean)
    private val tools=listOf(
        Tool("Aadhaar Validator","Validate Aadhaar format and checksum using the Verhoeff algorithm; includes the official UIDAI link.","aadhaar/",true),
        Tool("Age & Date Calculator","Calculate exact age, date differences, weekdays and working-day timelines.","age-calculator/",true),
        Tool("CDR Analysis","Analyze call-data records with contact, duration, timeline and pattern views.","cdr/",true),
        Tool("File Comparison","Compare two files byte-by-byte and verify whether their contents match.","compare/",true),
        Tool("Fingerprint Matcher","Compare two fingerprint images and calculate similarity using the supplied analyzer.","fingerprint/",true),
        Tool("Gurmukhi Font Converter","Convert Punjabi text between Unicode and legacy Gurmukhi font formats.","font-converter/",true),
        Tool("Add PDF Page Numbers","Add page numbers to a PDF document.","general/add-page-numbers/",true),
        Tool("Add PDF Watermark","Add a watermark to PDF pages.","general/add-pdf-watermark/",true),
        Tool("Batch Rename","Rename multiple selected files using a consistent pattern.","general/batch-rename/",true),
        Tool("Compress Images","Reduce image file size while retaining useful quality.","general/compress-images/",true),
        Tool("Compress PDF","Reduce the size of PDF documents.","general/compress-pdf/",true),
        Tool("Convert Images","Convert images between supported image formats.","general/convert-images/",true),
        Tool("Crop Images","Crop selected images to a chosen area.","general/crop-images/",true),
        Tool("Decode QR","Read QR content from an image.","general/decode-qr/",true),
        Tool("Delete PDF Pages","Remove selected pages from a PDF.","general/delete-pdf-pages/",true),
        Tool("File Format Detector","Inspect a file and determine its detected format/type.","general/file-format-detector/",true),
        Tool("Generate QR","Generate a QR code from text or a URL.","general/generate-qr/",true),
        Tool("HTML to PDF","Convert HTML content into a PDF document.","general/html-to-pdf/",true),
        Tool("Image ↔ Base64","Convert images to Base64 and decode Base64 image data.","general/image-base64/",true),
        Tool("Image to QR","Turn image content into a QR payload where supported.","general/image-to-qr/",true),
        Tool("Images to PDF","Combine selected images into a PDF.","general/images-to-pdf/",true),
        Tool("Merge PDFs","Combine multiple PDF documents into one.","general/merge-pdfs/",true),
        Tool("PDF to Images","Render PDF pages as image files.","general/pdf-to-images/",true),
        Tool("Protect PDF","Protect a PDF with a password.","general/protect-pdf/",true),
        Tool("QR to Image","Convert QR content into a downloadable image representation.","general/qr-to-image/",true),
        Tool("Reorder PDF Pages","Rearrange pages in a PDF document.","general/reorder-pdf/",true),
        Tool("Resize Images","Resize selected images to chosen dimensions.","general/resize-images/",true),
        Tool("Rotate Images","Rotate selected images.","general/rotate-images/",true),
        Tool("Rotate PDF","Rotate PDF pages.","general/rotate-pdf/",true),
        Tool("Split PDF","Split a PDF into separate documents/pages.","general/split-pdf/",true),
        Tool("Text to PDF","Turn text into a PDF document.","general/text-to-pdf/",true),
        Tool("Unlock PDF","Remove a PDF password when the supplied password permits access.","general/unlock-pdf/",true),
        Tool("Watermark Images","Apply a watermark to image files.","general/watermark-images/",true),
        Tool("GST Number Validator","Validate GSTIN format/checksum and decode state, PAN and entity information.","gst/",true),
        Tool("Hash Generator","Generate common cryptographic hashes including SHA-256 and SHA-512.","hash/",true),
        Tool("IFSC Code Lookup","Look up Indian bank branch information from an IFSC code.","ifsc/",true),
        Tool("Image Forensics","Inspect images for metadata, ELA indicators and forensic markers.","image-forensics/",true),
        Tool("IMEI Verifier","Validate IMEI using Luhn and inspect TAC/device information available in the bundled database.","imei/",true),
        Tool("IP Address Lookup","Inspect public IP intelligence such as country, ISP, ASN and related risk signals.","ip/",true),
        Tool("IPDR Analysis","Analyze IP data records, sessions, usage and connection patterns.","ipdr/",true),
        Tool("MAC Address Lookup","Validate MAC addresses and identify vendors from the bundled OUI data.","mac/",true),
        Tool("Document Metadata","Extract available document/file metadata such as dates and author fields.","metadata/",true),
        Tool("OCR","Extract text from images with the supplied Tesseract-based interface.","ocr/",true),
        Tool("PAN Card Validator","Validate PAN structure and decode its embedded entity/type fields.","pan/",true),
        Tool("Password Generator","Generate cryptographically secure passwords and passphrases locally.","password/",true),
        Tool("Phone Number Lookup","Inspect phone-number country/operator information supported by the supplied database.","phone/",true),
        Tool("QR Code Tools","Generate and scan QR codes, including common text, URL, Wi-Fi and vCard formats.","qrcode/",true),
        Tool("Signature Checksum","Generate and verify the supplied daily SHA-256 signature codes.","signature/",true),
        Tool("Steganography","Encode or inspect hidden image data using the supplied LSB steganography interface.","steganography/",true),
        Tool("Gurmukhi Pad","Type Punjabi using English transliteration with live suggestions.","transliterate/",true),
        Tool("Vehicle Number Decoder","Decode Indian vehicle registration numbers, state/RTO information and BH-series formats.","vehicle/",true),
        Tool("Number Lookup","Search the configured lookup service by phone number and show returned structured fields.","__search_number__",true),
        Tool("Aadhaar UIDAI Verification","Use the supplied Aadhaar verification workflow when its required UIDAI verification flow is available.","__search_aadhaar__",true)
    )
    override fun onCreate(state:Bundle?){super.onCreate(state);window.statusBarColor=bg;window.navigationBarColor=bg;home()}
    private fun tv(s:String,size:Float,color:Int=text)=TextView(this).apply{text=s;textSize=size;setTextColor(color)}
    private fun rounded(color:Int,radius:Float)=android.graphics.drawable.GradientDrawable().apply{setColor(color);cornerRadius=radius;setStroke(1,border)}
    private fun button(s:String,filled:Boolean=false)=Button(this).apply{text=s;textSize=13f;isAllCaps=false;setTextColor(if(filled)Color.rgb(15,16,20) else text);background=rounded(if(filled)accent else surface2,18f);setPadding(16,0,16,0)}
    private fun page()=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,10,18,14);setBackgroundColor(bg)}
    private fun card()=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(17,16,17,17);background=rounded(surface,20f)}
    private fun lp(top:Int=0,bottom:Int=0)=LinearLayout.LayoutParams(-1,-2).apply{setMargins(0,top,0,bottom)}
    private fun header(p:LinearLayout,section:String){val row=LinearLayout(this).apply{gravity=Gravity.CENTER_VERTICAL};row.addView(tv("HAO",14f).apply{setTypeface(null,Typeface.BOLD)},LinearLayout.LayoutParams(-2,48));row.addView(tv("  /  $section",13f,muted),LinearLayout.LayoutParams(0,48,1f));row.addView(button("⋮").apply{setOnClickListener{settings()}},LinearLayout.LayoutParams(52,48));p.addView(row)}
    private fun title(p:LinearLayout,a:String,b:String){p.addView(tv(a,30f).apply{setTypeface(null,Typeface.BOLD);setPadding(0,8,0,3)});p.addView(tv(b,14f,muted).apply{setPadding(0,0,0,8)})}
    private fun shell(content:LinearLayout,screen:String){currentScreen=screen;val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(bg)};root.addView(ScrollView(this).apply{addView(content)},LinearLayout.LayoutParams(-1,0,1f));val nav=LinearLayout(this).apply{setPadding(10,8,10,8);setBackgroundColor(bg)};listOf("Home","Search","Tools").forEachIndexed{i,label->{val active=(i==0&&screen=="home")||(i==1&&screen=="search")||(i==2&&screen=="tools");val b=button(label,active);nav.addView(b,LinearLayout.LayoutParams(0,54,1f).apply{if(i>0)leftMargin=7});b.setOnClickListener{when(i){0->home();1->search();else->tools()}}}};root.addView(nav,LinearLayout.LayoutParams(-1,70));setContentView(root)}
    private fun home(){val p=page();header(p,"home");title(p,"Search smarter.","HAO now contains the complete tool catalogue from your supplied ZIP.");val search=card();search.addView(tv("LOOKUP",11f,muted).apply{setTypeface(null,Typeface.BOLD)});search.addView(tv("Number & Aadhaar",22f).apply{setTypeface(null,Typeface.BOLD);setPadding(0,7,0,3)});search.addView(tv("The upstream address is not shown in the interface.",13f,muted));search.addView(button("Open search",true).apply{setOnClickListener{search()}},lp(15));p.addView(search,lp(8));val all=card();all.addView(tv("TOOLS",11f,muted).apply{setTypeface(null,Typeface.BOLD)});all.addView(tv("${tools.size} tools",21f).apply{setTypeface(null,Typeface.BOLD);setPadding(0,7,0,3)});all.addView(tv("Every tool from the supplied ZIP is represented with its name, description and launch target.",13f,muted));all.addView(button("Browse all tools",true).apply{setOnClickListener{tools()}},lp(15));p.addView(all,lp(10));p.addView(tv("Developer  •  Chatpataprani",12f,muted).apply{setPadding(2,22,2,8)});shell(p,"home")}
    private fun search(){val p=page();header(p,"search");title(p,"Database search","Choose a mode, enter a value, then view structured results.");val tabs=LinearLayout(this);val n=button("Number",mode=="number");val a=button("Aadhaar",mode=="aadhar");tabs.addView(n,LinearLayout.LayoutParams(0,50,1f));tabs.addView(a,LinearLayout.LayoutParams(0,50,1f).apply{leftMargin=8});p.addView(tabs,lp(4,8));n.setOnClickListener{mode="number";search()};a.setOnClickListener{mode="aadhar";search()};val box=card();box.addView(tv(if(mode=="number")"NUMBER" else "AADHAAR",11f,muted).apply{setTypeface(null,Typeface.BOLD)});query=EditText(this).apply{singleLine=true;textSize=16f;setTextColor(text);setHintTextColor(muted);hint=if(mode=="number")"Enter number" else "Enter Aadhaar";setPadding(15,0,15,0);background=rounded(Color.rgb(12,14,19),15f)};box.addView(query,lp(8,9));box.addView(button("Search",true).apply{setOnClickListener{lookup()}},LinearLayout.LayoutParams(-1,52));status=tv("",13f,muted).apply{setPadding(2,12,2,2)};box.addView(status);p.addView(box);results=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL};p.addView(results);shell(p,"search")}
    private fun lookup(){val value=query.text.toString().trim();if(value.isEmpty()){status.text="Enter a value first.";return};if(backendUrl.isBlank()){status.text="Backend is not configured.";return};status.text="Searching…";results.removeAllViews();Thread{var c:HttpURLConnection?=null;try{val endpoint=backendUrl.trimEnd('/')+"/lookup?type=$mode&value="+URLEncoder.encode(value,"UTF-8");c=URL(endpoint).openConnection() as HttpURLConnection;c.requestMethod="GET";c.connectTimeout=15000;c.readTimeout=20000;val code=c.responseCode;val stream=if(code>=400)c.errorStream else c.inputStream;val raw=stream?.bufferedReader()?.use{it.readText()}?:"";runOnUiThread{render(code,raw)}}catch(_:Exception){runOnUiThread{status.text="Connection failed. Check the configured service."}}finally{c?.disconnect()}}.start()}
    private fun render(code:Int,raw:String){status.text=if(code in 200..299)"Search complete" else "Request failed";if(raw.isBlank()){info("No response","The service returned an empty response.");return};try{val obj=JSONObject(raw);val arr=obj.optJSONArray("results");if(arr!=null){results.addView(tv("${arr.length()} result(s)",13f,muted),lp(8,2));for(i in 0 until arr.length())resultCard(arr.get(i),i+1)}else resultCard(obj,1)}catch(_:Exception){info("Response",raw)}}
    private fun resultCard(item:Any,number:Int){val c=card();c.addView(tv("RESULT $number",11f,muted).apply{setTypeface(null,Typeface.BOLD)});if(item is JSONObject){val it=item.keys();while(it.hasNext()){val key=it.next();c.addView(tv(key.uppercase(Locale.US),10f,muted).apply{setPadding(0,10,0,1)});c.addView(tv(item.optString(key),14f))}}else c.addView(tv(item.toString(),14f));results.addView(c,lp(8))}
    private fun info(name:String,description:String){val c=card();c.addView(tv(name,16f).apply{setTypeface(null,Typeface.BOLD)});c.addView(tv(description,13f,muted).apply{setPadding(0,5,0,0)});results.addView(c,lp(8))}
    private fun tools(){val p=page();header(p,"tools");title(p,"All tools","Complete catalogue from the supplied ZIP. Each card has a name and a short description.");tools.forEach{tool->val c=card();val row=LinearLayout(this);row.addView(tv(tool.name,17f).apply{setTypeface(null,Typeface.BOLD)},LinearLayout.LayoutParams(0,-2,1f));row.addView(button("Open").apply{setOnClickListener{openTool(tool)}},LinearLayout.LayoutParams(78,46));c.addView(row);c.addView(tv(tool.description,13f,muted).apply{setPadding(0,6,0,0)});p.addView(c,lp(7))};shell(p,"tools")}
    private fun openTool(tool:Tool){when(tool.path){"__search_number__"->{mode="number";search()};"__search_aadhaar__"->{mode="aadhar";search()};else->AlertDialog.Builder(this).setTitle(tool.name).setMessage(tool.description+"\n\nThe tool is registered from the supplied ZIP. Its original page is not yet bundled into this native build.").setPositiveButton("OK",null).show()}}
    private fun settings(){val box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,18,24,8)};box.addView(tv("HAO Settings",24f).apply{setTypeface(null,Typeface.BOLD)});box.addView(tv("Kotlin utility workspace",13f,muted));setting(box,"Developer","Chatpataprani",null);setting(box,"GitHub","Open the HAO project","https://github.com/chatpataprani/One-for-all");setting(box,"Instagram","Open @chatpataprani","https://instagram.com/chatpataprani");setting(box,"Privacy","The upstream database address is not displayed in the UI.",null);setting(box,"Catalogue","${tools.size} tools imported from the supplied ZIP.",null);AlertDialog.Builder(this).setView(box).setNegativeButton("Close",null).show()}
    private fun setting(box:LinearLayout,name:String,description:String,url:String?){val b=button("$name\n$description");b.gravity=Gravity.LEFT or Gravity.CENTER_VERTICAL;b.setOnClickListener{if(url!=null)startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(url)))};box.addView(b,lp(7))}
    override fun onBackPressed(){if(currentScreen!="home")home()else super.onBackPressed()}
}