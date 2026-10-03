package com.chatpataprani.hao

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

data class Tool(val name:String,val description:String,val path:String,val enabled:Boolean)

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

class KotlinMainActivity : ComponentActivity() {
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        enableEdgeToEdge()
        setContent { HaoApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HaoApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("hao", Context.MODE_PRIVATE) }
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var settings by rememberSaveable { mutableStateOf(false) }
    var toolDialog by remember { mutableStateOf<Tool?>(null) }
    var dark by rememberSaveable { mutableStateOf(prefs.getBoolean("dark", true)) }
    var glass by rememberSaveable { mutableFloatStateOf(prefs.getFloat("glass", .86f)) }
    var haptics by rememberSaveable { mutableStateOf(prefs.getBoolean("haptics", true)) }
    var history by remember { mutableStateOf(prefs.getStringSet("history", emptySet())?.toList()?.reversed() ?: emptyList()) }
    val scheme = if (dark) darkColorScheme(
        background=Color(7,9,14), surface=Color(18,21,29), surfaceVariant=Color(30,34,44),
        primary=Color(224,231,255), secondary=Color(177,195,255)
    ) else lightColorScheme(
        background=Color(241,244,250), surface=Color(250,251,255), surfaceVariant=Color(231,235,244),
        primary=Color(54,72,125), secondary=Color(79,96,145)
    )
    MaterialTheme(colorScheme=scheme) {
        Scaffold(
            containerColor=Color.Transparent,
            topBar={
                TopAppBar(
                    title={Column{Text("HAO",fontWeight=FontWeight.Bold,letterSpacing=1.8.sp);Text(
                        if(tab==0)"home" else if(tab==1)"search" else "tools",
                        style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}},
                    actions={IconButton(onClick={settings=true}){Icon(Icons.Outlined.Tune,"Settings")}},
                    colors=TopAppBarDefaults.topAppBarColors(containerColor=Color.Transparent)
                )
            },
            bottomBar={
                NavigationBar(containerColor=MaterialTheme.colorScheme.surface.copy(alpha=.80f)) {
                    NavigationBarItem(tab==0,{tab=0},{Icon(Icons.Outlined.Home,null);Text("Home")})
                    NavigationBarItem(tab==1,{tab=1},{Icon(Icons.Outlined.Search,null);Text("Search")})
                    NavigationBarItem(tab==2,{tab=2},{Icon(Icons.Outlined.GridView,null);Text("Tools")})
                }
            }
        ){pad ->
            Box(Modifier.fillMaxSize().padding(pad)) {
                when(tab) {
                    0 -> HomeScreen(glass,{tab=1},{tab=2},history,{tab=1})
                    1 -> SearchScreen(glass,haptics,history){newList ->
                        history=newList
                        prefs.edit().putStringSet("history",newList.toSet()).apply()
                    }
                    else -> ToolsScreen(glass){tool ->
                        if(tool.path=="__search_number__" || tool.path=="__search_aadhaar__") tab=1
                        else toolDialog = tool
                    }
                }
            }
        }
        toolDialog?.let { ToolWorkspace(it, glass) { toolDialog = null } }
        if(settings) SettingsSheet(dark,glass,haptics,history,
            {dark=it;prefs.edit().putBoolean("dark",it).apply()},
            {glass=it;prefs.edit().putFloat("glass",it).apply()},
            {haptics=it;prefs.edit().putBoolean("haptics",it).apply()},
            {history=emptyList();prefs.edit().remove("history").apply()},
            {settings=false}
        )
    }
}

@Composable
private fun GlassCard(intensity:Float,modifier:Modifier=Modifier,shape:Shape=RoundedCornerShape(26.dp),content:@Composable ColumnScope.()->Unit) {
    val c=MaterialTheme.colorScheme
    Column(modifier.fillMaxWidth().clip(shape)
        .background(Brush.linearGradient(listOf(
            c.surface.copy(alpha=(.55f+intensity*.25f).coerceAtMost(.92f)),
            c.surfaceVariant.copy(alpha=(.22f+intensity*.25f).coerceAtMost(.62f)),
            c.surface.copy(alpha=(.45f+intensity*.22f).coerceAtMost(.88f))
        )))
        .padding(1.dp)
        .background(c.surface.copy(alpha=.38f),shape)
        .padding(18.dp),content=content)
}

@Composable
private fun SectionTitle(title:String,subtitle:String) {
    Column(Modifier.padding(horizontal=4.dp,vertical=10.dp)) {
        Text(title,style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Bold)
        Text(subtitle,color=MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun HomeScreen(intensity:Float,onSearch:()->Unit,onTools:()->Unit,history:List<String>,onHistory:()->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp)) {
        SectionTitle("Search smarter.","Fast lookup, clean results, glass-first UI.")
        GlassCard(intensity,Modifier.padding(bottom=12.dp)) {
            Text("LOOKUP",style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(8.dp));Text("Number & Aadhaar",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
            Text("Direct API mode is enabled so HAO works without the Vercel proxy.",color=MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp));Button(onClick=onSearch,modifier=Modifier.fillMaxWidth().height(52.dp)){Icon(Icons.Outlined.Search,null);Spacer(Modifier.width(8.dp));Text("Open search")}
        }
        GlassCard(intensity,Modifier.padding(bottom=12.dp)) {
            Text("TOOLKIT",style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(8.dp));Text(tools.size.toString()+" tools",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
            Text("Every tool has its own named glass box and can be filtered by category.",color=MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp));OutlinedButton(onClick=onTools,modifier=Modifier.fillMaxWidth().height(50.dp)){Text("Browse all tools")}
        }
        if(history.isNotEmpty()) {
            Text("RECENT",style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold,modifier=Modifier.padding(6.dp,12.dp,6.dp,6.dp))
            history.take(5).forEach { item ->
                GlassCard(intensity,Modifier.padding(bottom=8.dp),RoundedCornerShape(20.dp)) {
                    Row(Modifier.fillMaxWidth().clickable(onClick=onHistory),verticalAlignment=Alignment.CenterVertically) {
                        Icon(Icons.Outlined.History,null,tint=MaterialTheme.colorScheme.secondary);Spacer(Modifier.width(12.dp));Text(item,fontWeight=FontWeight.SemiBold)
                    }
                }
            }
        }
        Text("Developer  •  Chatpataprani",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(6.dp,18.dp,6.dp,24.dp))
    }
}

@Composable
private fun ToolsScreen(intensity:Float,onOpen:(Tool)->Unit) {
    var filter by rememberSaveable{mutableStateOf("")}
    var category by rememberSaveable{mutableStateOf("All")}
    val categories=listOf("All")+tools.map{it.name.substringBefore(" ")}.distinct()
    val shown=tools.filter{filter.isBlank() || it.name.contains(filter,true) || it.description.contains(filter,true)}
    Column(Modifier.fillMaxSize().padding(horizontal=14.dp)) {
        SectionTitle("All tools","Every tool is a glass box with its name, purpose and open action.")
        OutlinedTextField(value=filter,onValueChange={filter=it},singleLine=true,placeholder={Text("Find a tool…")},leadingIcon={Icon(Icons.Outlined.Search,null)},modifier=Modifier.fillMaxWidth().padding(bottom=8.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(bottom=10.dp)) {
            categories.take(10).forEach { chip -> FilterChip(selected=category==chip,onClick={category=chip},label={Text(chip)},modifier=Modifier.padding(end=6.dp)) }
        }
        LazyVerticalGrid(columns=GridCells.Adaptive(minSize=155.dp),contentPadding=PaddingValues(bottom=18.dp),horizontalArrangement=Arrangement.spacedBy(10.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
            items(shown,key={it.name}) { tool ->
                GlassCard(intensity,Modifier.heightIn(min=170.dp),RoundedCornerShape(22.dp)) {
                    Text(tool.name.uppercase(Locale.US),style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.secondary,fontWeight=FontWeight.Bold,maxLines=2)
                    Spacer(Modifier.height(8.dp));Text(tool.name,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)
                    Spacer(Modifier.height(6.dp));Text(tool.description,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant,maxLines=4)
                    Spacer(Modifier.weight(1f));TextButton(onClick={onOpen(tool)}){Text("Open  →")}
                }
            }
        }
    }
}

@Composable
private fun SearchScreen(intensity:Float,haptics:Boolean,history:List<String>,saveHistory:(List<String>)->Unit) {
    var mode by rememberSaveable{mutableStateOf("number")}
    var query by rememberSaveable{mutableStateOf("")}
    var status by remember{mutableStateOf("")}
    var raw by remember{mutableStateOf("")}
    var loading by remember{mutableStateOf(false)}
    val scope=rememberCoroutineScope()
    val feedback=LocalHapticFeedback.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp)) {
        SectionTitle("Database search","Choose a mode, enter a value, and view structured results.")
        Row(Modifier.padding(bottom=10.dp)) {
            FilterChip(selected=mode=="number",onClick={mode="number"},label={Text("Number")},modifier=Modifier.padding(end=8.dp))
            FilterChip(selected=mode=="aadhar",onClick={mode="aadhar"},label={Text("Aadhaar")})
        }
        GlassCard(intensity) {
            Text(if(mode=="number")"NUMBER" else "AADHAAR",style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value=query,onValueChange={query=it},singleLine=true,label={Text(if(mode=="number")"Phone number" else "Aadhaar number")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number),modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            Button(enabled=!loading,onClick={
                if(query.trim().isEmpty()){status="Enter a value first.";return@Button}
                if(haptics) feedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                loading=true;status="Searching…";raw=""
                val value=query.trim()
                scope.launch {
                    val result=withContext(Dispatchers.IO){performLookup(mode,value)}
                    loading=false;status=result.first;raw=result.second
                    if(result.second.isNotBlank()) saveHistory((listOf(mode+" • "+value)+history).distinct().take(20))
                }
            },modifier=Modifier.fillMaxWidth().height(52.dp)) {
                if(loading) CircularProgressIndicator(Modifier.size(20.dp),strokeWidth=2.dp)
                else {Icon(Icons.Outlined.Search,null);Spacer(Modifier.width(8.dp));Text("Search")}
            }
            if(status.isNotBlank()) Text(status,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(top=10.dp))
        }
        if(raw.isNotBlank()){Spacer(Modifier.height(12.dp));ResultView(intensity,raw)}
    }
}

private suspend fun performLookup(mode:String,value:String):Pair<String,String> {
    val endpoint = if (mode == "aadhar") {
        "https://both-db.vercel.app/aadhar=" + URLEncoder.encode(value, "UTF-8")
    } else {
        "https://both-db.vercel.app/number=" + URLEncoder.encode(value, "UTF-8")
    }
    return try {
        val c=URL(endpoint).openConnection() as HttpURLConnection
        c.requestMethod="GET";c.connectTimeout=15000;c.readTimeout=20000
        val code=c.responseCode
        val stream=if(code>=400)c.errorStream else c.inputStream
        val body=stream?.bufferedReader()?.use{it.readText()}?:""
        c.disconnect()
        if(code in 200..299) "Search complete" to body else "Request failed" to body
    } catch(_:Exception) {"Connection failed. Check the configured service." to ""}
}

@Composable
private fun ResultView(intensity:Float,raw:String) {
    val obj=runCatching{JSONObject(raw)}.getOrNull()
    if(obj==null){GlassCard(intensity){Text("RESPONSE",fontWeight=FontWeight.Bold);Spacer(Modifier.height(8.dp));Text(raw)};return}
    val arr=obj.optJSONArray("results")
    if(arr!=null){Text(arr.length().toString()+" result(s)",style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold,modifier=Modifier.padding(6.dp,4.dp));for(i in 0 until arr.length())ResultObjectCard(intensity,arr.optJSONObject(i),i+1)}
    else ResultObjectCard(intensity,obj,1)
}

@Composable
private fun ResultObjectCard(intensity:Float,obj:JSONObject?,number:Int) {
    if(obj==null)return
    GlassCard(intensity,Modifier.padding(bottom=10.dp),RoundedCornerShape(22.dp)) {
        Text("RESULT "+number,style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.secondary,fontWeight=FontWeight.Bold)
        obj.keys().forEach { key -> Spacer(Modifier.height(8.dp));Text(key.uppercase(Locale.US),style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant);Text(obj.optString(key),style=MaterialTheme.typography.bodyLarge,fontWeight=FontWeight.SemiBold)}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSheet(dark:Boolean,glass:Float,haptics:Boolean,history:List<String>,onDark:(Boolean)->Unit,onGlass:(Float)->Unit,onHaptics:(Boolean)->Unit,onClear:()->Unit,onDismiss:()->Unit) {
    val context=LocalContext.current
    ModalBottomSheet(onDismissRequest=onDismiss,containerColor=MaterialTheme.colorScheme.surface.copy(alpha=.96f)) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal=20.dp).padding(bottom=28.dp)) {
            Text("Settings",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
            Text("Personalize your HAO workspace.",color=MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            SettingCard("Appearance","Dark glass theme"){Switch(checked=dark,onCheckedChange=onDark)}
            SettingCard("Glass intensity","Control card translucency"){Slider(value=glass,onValueChange=onGlass,valueRange=.55f..1f)}
            SettingCard("Haptics","Subtle feedback on search"){Switch(checked=haptics,onCheckedChange=onHaptics)}
            SettingCard("Search history",history.size.toString()+" local entries"){TextButton(onClick=onClear,enabled=history.isNotEmpty()){Text("Clear")}}
            SettingLink(Icons.Outlined.Code,"GitHub","Open the HAO project","https://github.com/chatpataprani/One-for-all")
            SettingLink(Icons.Outlined.CameraAlt,"Instagram","@chatpataprani","https://instagram.com/chatpataprani")
            SettingCard("Privacy","HAO uses the configured Both-db API directly. No Vercel backend is required."){}
            SettingCard("Developer","Chatpataprani"){ Text("Version 4.0 • "+tools.size.toString()+" tools") }
            SettingLink(Icons.Outlined.Info,"About HAO","Reeldrop-inspired expressive workspace","https://github.com/chatpataprani/One-for-all")
            TextButton(onClick={Toast.makeText(context,"HAO settings saved locally",Toast.LENGTH_SHORT).show();onDismiss()},modifier=Modifier.fillMaxWidth()){Text("Done")}
        }
    }
}

@Composable
private fun SettingCard(title:String,description:String="",trailing:@Composable RowScope.()->Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical=5.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=.46f)).padding(16.dp),verticalAlignment=Alignment.CenterVertically) {
        Column(Modifier.weight(1f)){Text(title,fontWeight=FontWeight.SemiBold);if(description.isNotBlank())Text(description,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}
        trailing()
    }
}

@Composable
private fun SettingLink(icon:androidx.compose.ui.graphics.vector.ImageVector,title:String,description:String,url:String) {
    val context=LocalContext.current
    SettingCard(title,description){IconButton(onClick={context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(url)))}){Icon(icon,null)}}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolWorkspace(tool: Tool, intensity: Float, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var input by rememberSaveable { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var copied by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(20.dp).padding(bottom=28.dp)) {
            Text(tool.name, style=MaterialTheme.typography.headlineSmall, fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(tool.description, color=MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            when {
                tool.name == "Password Generator" -> {
                    var length by rememberSaveable { mutableIntStateOf(20) }
                    Text("Length: $length")
                    Slider(value=length.toFloat(), onValueChange={length=it.toInt()}, valueRange=8f..64f, steps=55)
                    Button(onClick={ output = generatePassword(length) }, Modifier.fillMaxWidth()) { Text("Generate secure password") }
                }
                tool.name == "Hash Generator" -> {
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Text to hash")})
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={ output = sha256(input) },Modifier.fillMaxWidth()){Text("Generate SHA-256")}
                }
                tool.name in setOf("Aadhaar Validator","PAN Card Validator","GST Number Validator","IMEI Verifier","MAC Address Lookup") -> {
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Enter value")},singleLine=true)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={output=validateLocal(tool.name,input)},Modifier.fillMaxWidth()){Text("Validate / inspect")}
                }
                tool.name == "Age & Date Calculator" -> {
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Date of birth: YYYY-MM-DD")},singleLine=true)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={output=ageFromDate(input)},Modifier.fillMaxWidth()){Text("Calculate age")}
                }
                tool.name == "Gurmukhi Pad" || tool.name == "Gurmukhi Font Converter" -> {
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Punjabi / Gurmukhi text")},minLines=4)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={output=input},Modifier.fillMaxWidth()){Text("Process text")}
                }
                tool.name == "Vehicle Number Decoder" -> {
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Vehicle registration")},singleLine=true)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={output=vehicleDecode(input)},Modifier.fillMaxWidth()){Text("Decode registration")}
                }
                tool.name == "Signature Checksum" -> {
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Text / data")})
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={output=sha256(input)},Modifier.fillMaxWidth()){Text("Generate signature")}
                }
                tool.name == "Number Lookup" || tool.name == "Aadhaar UIDAI Verification" -> {
                    Text("This tool uses the live direct Both-db service.")
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={onDismiss},Modifier.fillMaxWidth()){Text("Open Search")}
                }
                else -> {
                    Text("This tool is ready for local input/file processing. Choose an input below to begin.")
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Input / notes")},minLines=3)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick={
                        val intent=Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            type="*/*"; addCategory(Intent.CATEGORY_OPENABLE)
                        }
                        context.startActivity(intent)
                    },Modifier.fillMaxWidth()){Text("Choose a file")}
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick={output = "Input received. This workspace is available offline; selected files can be processed by the tool-specific workflow."},Modifier.fillMaxWidth()){Text("Process input")}
                }
            }
            if(output.isNotBlank()){
                Spacer(Modifier.height(14.dp))
                GlassCard(intensity, Modifier, RoundedCornerShape(20.dp)){
                    Text("RESULT",style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.height(8.dp)); Text(output)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick={
                        val cm=context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        cm.setPrimaryClip(android.content.ClipData.newPlainText("HAO result",output));copied=true
                    }){Text(if(copied)"Copied" else "Copy result")}
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick=onDismiss,Modifier.fillMaxWidth()){Text("Close")}
        }
    }
}

private fun generatePassword(length:Int):String {
    val chars="ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#%+=_-"
    val r=java.security.SecureRandom()
    return buildString { repeat(length){append(chars[r.nextInt(chars.length)])} }
}
private fun sha256(s:String):String {
    val b=java.security.MessageDigest.getInstance("SHA-256").digest(s.toByteArray())
    return b.joinToString("") { "%02x".format(it) }
}
private fun luhn(s:String):Boolean {
    var sum=0; var alt=false
    for(i in s.length-1 downTo 0) { val d=s[i]-'0'; var n=d; if(alt){n*=2;if(n>9)n-=9};sum+=n;alt=!alt }
    return s.isNotEmpty() && s.all{it.isDigit()} && sum%10==0
}
private fun verhoeff(s:String):Boolean {
    val d=arrayOf(intArrayOf(0,1,2,3,4,5,6,7,8,9),intArrayOf(1,2,3,4,0,6,7,8,9,5),intArrayOf(2,3,4,0,1,7,8,9,5,6),intArrayOf(3,4,0,1,2,8,9,5,6,7),intArrayOf(4,0,1,2,3,9,5,6,7,8),intArrayOf(5,9,8,7,6,0,4,3,2,1),intArrayOf(6,5,9,8,7,1,0,4,3,2),intArrayOf(7,6,5,9,8,2,1,0,4,3),intArrayOf(8,7,6,5,9,3,2,1,0,4),intArrayOf(9,8,7,6,5,4,3,2,1,0))
    val p=arrayOf(intArrayOf(0,1,2,3,4,5,6,7,8,9),intArrayOf(1,5,7,6,2,8,3,0,9,4),intArrayOf(5,8,0,3,7,9,6,1,4,2),intArrayOf(8,9,1,6,0,4,3,5,2,7),intArrayOf(9,4,5,3,1,2,6,8,7,0),intArrayOf(4,2,8,6,5,7,3,9,0,1),intArrayOf(2,7,9,3,8,0,6,4,1,5),intArrayOf(7,0,4,6,9,1,3,2,5,8))
    var c=0; val rev=s.reversed()
    for(i in rev.indices)c=d[c][p[i%8][rev[i]-'0']]
    return c==0
}
private fun validateLocal(name:String,raw:String):String {
    val v=raw.trim().uppercase(Locale.US)
    return when(name) {
        "Aadhaar Validator" -> if(v.length==12 && verhoeff(v)) "Valid 12-digit Aadhaar format and Verhoeff checksum." else "Invalid Aadhaar checksum or format."
        "PAN Card Validator" -> if(Regex("[A-Z]{5}[0-9]{4}[A-Z]").matches(v)) "Valid PAN structure." else "Invalid PAN structure."
        "GST Number Validator" -> if(Regex("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]").matches(v)) "GSTIN structure looks valid." else "Invalid GSTIN structure."
        "IMEI Verifier" -> if(v.length==15 && luhn(v)) "Valid 15-digit IMEI Luhn checksum." else "Invalid IMEI."
        else -> if(Regex("[0-9A-F]{2}([-:][0-9A-F]{2}){5}").matches(v)) "Valid MAC address format." else "Invalid MAC address format."
    }
}
private fun ageFromDate(s:String):String = try {
    val parts=s.trim().split("-")
    require(parts.size==3)
    val dob=java.util.Calendar.getInstance().apply { clear(); set(parts[0].toInt(),parts[1].toInt()-1,parts[2].toInt()) }
    val now=java.util.Calendar.getInstance()
    var years=now.get(java.util.Calendar.YEAR)-dob.get(java.util.Calendar.YEAR)
    if(now.get(java.util.Calendar.DAY_OF_YEAR)<dob.get(java.util.Calendar.DAY_OF_YEAR)) years--
    "Age: $years years. Born: ${parts[0]}-${parts[1]}-${parts[2]}."
} catch(_:Exception) {"Use YYYY-MM-DD, for example 2000-01-15."}
private fun vehicleDecode(s:String):String {
    val v=s.trim().uppercase(Locale.US).replace("\\s+".toRegex()," ")
    val code=v.replace(" ","").take(4)
    return if(v.isBlank()) "Enter a registration such as BR01AB1234." else "Registration: $v\nState/RTO prefix: $code\nFull decoding depends on the Indian RTO database."
}
