const tools=[['🧮','Age Calculator','age-calculator'],['🪪','Aadhaar Validator','aadhaar'],['📞','Phone Lookup','phone'],['🔐','Hash Generator','hash'],['🖼️','Image Forensics','image-forensics'],['📄','Document Metadata','metadata'],['📡','IP Lookup','ip'],['📊','CDR Analysis','cdr'],['🌐','IPDR Analysis','ipdr'],['📱','IMEI Verifier','imei'],['🔗','MAC Lookup','mac'],['🧾','PAN Tools','pan'],['🏦','IFSC Lookup','ifsc'],['🚗','Vehicle Tools','vehicle'],['🧠','Fingerprint Matcher','fingerprint'],['🕵️','Steganography','steganography'],['🔎','OCR','ocr'],['🔳','QR Code Tools','qrcode'],['🔑','Password Generator','password'],['✍️','Signature Checksum','signature'],['🅰️','Punjabi Transliteration','transliterate'],['🔤','Font Converter','font-converter'],['⚖️','File Comparison','compare'],['📁','General PDF & Image Tools','general'],['💳','GST Tools','gst'],['📌','CDR/IPDR utilities','general']];
const esc=s=>String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const app=document.querySelector('#app');
function render(){app.innerHTML='<div class="shell"><header class="top"><div class="topbar"><div class="brand">one-for-all<span>.</span></div><div class="nav"><button id="homeBtn" class="active">Home</button><button id="lookupBtn">DB Lab</button></div></div></header><main><section class="hero"><div class="card"><div class="eyebrow">chatpataprani workflow</div><h1>paste. pick.<br>use. dip.</h1><p>A single learning-focused shell for the tools extracted from the supplied Punjab ZIP. Database access is built into the app through the supplied Both-db test API.</p><div class="inputrow"><input id="quick" placeholder="search a tool…"><button class="primary" id="search">find tool</button></div></div><div class="card"><div class="eyebrow">Project status</div><h2>One for All</h2><p class="muted">Tool shell + Both-db test lab.</p><div class="stats"><div class="stat"><b>26</b><span>tool entries</span></div><div class="stat"><b>57</b><span>source HTML screens</span></div><div class="stat"><b>1</b><span>server API adapter</span></div></div><div class="warning">API credentials stay server-side. Never put the Both-db secret in browser JavaScript.</div></div></section><section class="section"><h2>Toolbox</h2><div class="muted">Open a tool module directly inside One-for-all.</div><div id="tools" class="tools"></div></section><section id="viewer" class="panel"></section><section class="footer">Learning project. Uses Reeldrop as UI inspiration; it is a separate implementation.</section></main></div>';drawTools(tools);document.querySelector('#lookupBtn').onclick=showDb;document.querySelector('#search').onclick=()=>filterTools(document.querySelector('#quick').value);document.querySelector('#quick').oninput=e=>filterTools(e.target.value);}
function drawTools(list){document.querySelector('#tools').innerHTML=list.map(([i,n,p])=>'<button class="tool" data-path="'+p+'"><span class="icon">'+i+'</span><b>'+esc(n)+'</b><small>Open integrated tool</small></button>').join('');document.querySelectorAll('.tool').forEach(b=>b.onclick=()=>openTool(b.dataset.path));}
function filterTools(q){q=q.toLowerCase();drawTools(tools.filter(x=>x[1].toLowerCase().includes(q)||x[2].includes(q)));}
function openTool(path){const t=tools.find(x=>x[2]===path);const name=t?.[1]||path;document.querySelector('#viewer').innerHTML='<div class="card module-card"><div class="eyebrow">chatpataprani module</div><h2>'+esc(name)+'</h2><p class="muted">This tool is selected inside One-for-all. The workspace stays in-app; no external tool page is opened.</p><div class="module-status"><span class="success-dot"></span>Module ready</div><div class="module-info"><div><b>Module ID</b><span>'+esc(path)+'</span></div><div><b>Workspace</b><span>One-for-all</span></div><div><b>Brand</b><span>chatpataprani</span></div></div></div>';document.querySelector('#viewer').scrollIntoView({behavior:'smooth'});}
function showDb(){document.querySelector('#viewer').innerHTML='<div class="card"><div class="eyebrow">Both-db direct API</div><h2>Number & Aadhaar Lookup</h2><p class="muted">These buttons call the supplied Both-db endpoints directly and render the returned JSON as a readable card.</p><div class="lookup-grid"><div class="lookup-box"><span class="lookup-icon">📞</span><h3>Number Lookup</h3><p class="muted">Calls <code>https://both-db.vercel.app/number=</code></p><input id="numberValue" value="TEST-0001" placeholder="synthetic number"><button class="primary lookup-btn" id="numberRun">Number Lookup</button></div><div class="lookup-box"><span class="lookup-icon">🪪</span><h3>Aadhaar Lookup</h3><p class="muted">Calls <code>https://both-db.vercel.app/aadhar=</code></p><input id="aadharValue" value="TEST-0001" placeholder="synthetic Aadhaar"><button class="primary lookup-btn" id="aadharRun">Aadhaar Lookup</button></div></div><div id="dbResult" class="result-card"><div class="result-empty">Choose a lookup button to view the returned data.</div></div></div>';document.querySelector('#numberRun').onclick=()=>runDirectLookup('number');document.querySelector('#aadharRun').onclick=()=>runDirectLookup('aadhar');document.querySelector('#viewer').scrollIntoView({behavior:'smooth'});}
function displayValue(v){return v===null||v===undefined||v===''?'—':String(v)}
function resultField(label,value,wide=false){return '<div class="result-item'+(wide?' field-wide':'')+'"><span>'+esc(label)+'</span><strong>'+esc(displayValue(value))+'</strong></div>'}
function renderLookupResult(out,data,kind,value,status){
  const results=Array.isArray(data?.results)?data.results:[];
  const query=data?.number??data?.aadhar??value;
  const state=data?.status==='success'?'success':'response';
  const source='modihh ji';
  const cards=results.map((item,i)=>'<article class="match-card"><div class="match-header"><div><span class="match-number">MATCH '+String(i+1).padStart(2,'0')+'</span><h3>'+esc(displayValue(item.name))+'</h3></div><span class="source-pill">'+source+'</span></div><div class="field-grid">'+
    resultField('Father / Spouse',item.fathersName)+
    resultField('Phone',item.phoneNumber)+
    resultField('Aadhaar',item.aadharNumber)+
    resultField('Other Number',item.otherNumber)+
    resultField('District',item.district)+
    resultField('Pincode',item.pincode)+
    resultField('State',item.state)+
    resultField('Town',item.town)+
    resultField('Address',item.address,true)+
    resultField('Source',source)+
    '</div></article>').join('');
  out.innerHTML='<div class="lookup-summary"><div><span class="result-badge">'+esc(String(kind).toUpperCase())+'</span><h3>Lookup result</h3><p>Query: <code>'+esc(query)+'</code></p></div><div class="summary-stats"><div><b>'+esc(data?.count??results.length)+'</b><span>matches</span></div><div><b>'+esc(data?.lookup_ms??'—')+(data?.lookup_ms!=null?' ms':'')+'</b><span>lookup time</span></div><div><b>'+esc(data?.credits_used??'—')+'</b><span>credits</span></div></div></div>'+
    '<div class="result-meta"><span class="success-dot"></span>'+esc(state)+' · HTTP '+status+' · developer: '+esc(data?.developer??'—')+' · source: '+source+'</div>'+
    (cards||'<div class="result-empty">No matching records returned.</div>')+
    '<details class="raw-json"><summary>View raw JSON</summary><pre>'+esc(JSON.stringify(data,null,2))+'</pre></details>';
}
async function runDirectLookup(kind){
  const input=document.querySelector(kind==='number'?'#numberValue':'#aadharValue');
  const out=document.querySelector('#dbResult');
  const value=input.value.trim();
  if(!value){out.innerHTML='<div class="result-error">Enter a synthetic test value first.</div>';return}
  const endpoint=kind==='number'?'https://both-db.vercel.app/number=':'https://both-db.vercel.app/aadhar=';
  out.innerHTML='<div class="result-loading"><span class="spinner"></span> Querying Both-db…</div>';
  try{
    const r=await fetch(endpoint+encodeURIComponent(value),{headers:{accept:'application/json'}});
    const text=await r.text();
    let data; try{data=JSON.parse(text)}catch{data={raw:text}}
    if(!r.ok) throw new Error('HTTP '+r.status+' — '+(data?.message||data?.error||'request failed'));
    renderLookupResult(out,data,kind,value,r.status);
  }catch(e){
    out.innerHTML='<div class="result-error"><strong>Lookup failed</strong><span>'+esc(e.message)+'</span><small>If the browser blocks the direct request, enable CORS on Both-db for this site.</small></div>';
  }
}
render();