const state={kind:"number"};
const form=document.getElementById("searchForm");
const input=document.getElementById("value");
const label=document.getElementById("inputLabel");
const status=document.getElementById("status");
const result=document.getElementById("result");
const button=document.getElementById("searchBtn");

document.querySelectorAll(".tab").forEach(tab=>{
  tab.addEventListener("click",()=>{
    state.kind=tab.dataset.kind;
    document.querySelectorAll(".tab").forEach(x=>x.classList.toggle("active",x===tab));
    const aadhaar=state.kind==="aadhar";
    label.textContent=aadhaar?"Aadhaar":"Number";
    input.placeholder=aadhaar?"Enter test Aadhaar":"Enter test number";
    input.inputMode=aadhaar?"numeric":"tel";
    input.value="";
    status.textContent="";
    result.classList.add("hidden");
  });
});

form.addEventListener("submit",async e=>{
  e.preventDefault();
  const value=input.value.trim();
  if(!value)return;
  button.disabled=true;
  status.className="status";
  status.textContent="Searching…";
  result.classList.add("hidden");
  try{
    const res=await fetch("/api/lookup",{
      method:"POST",
      headers:{"content-type":"application/json","accept":"application/json"},
      body:JSON.stringify({kind:state.kind,value})
    });
    const data=await res.json().catch(()=>({error:"Invalid JSON response"}));
    if(!res.ok)throw new Error(data.message||data.error||`HTTP ${res.status}`);
    render(data);
    status.textContent="Search complete";
  }catch(err){
    status.className="status error";
    status.textContent=err.message||"Search failed";
  }finally{button.disabled=false}
});

function esc(v){
  return String(v??"").replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
}
function render(data){
  const rows=Array.isArray(data.results)?data.results:[];
  const cards=rows.map((r,i)=>`<article class="card">
    <div class="meta"><span>Result ${i+1}</span><span>source: modihh ji</span></div>
    <div class="grid">${Object.entries(r).map(([k,v])=>`<div class="field"><b>${esc(k)}</b><span>${esc(v===null?"null":v)}</span></div>`).join("")}</div>
  </article>`).join("");
  result.innerHTML=`<div class="meta"><span>${esc(data.type||state.kind)} lookup</span><span>${esc(data.count??rows.length)} result(s)</span></div>
    ${cards||'<div class="card">No results returned.</div>'}
    <details class="raw"><summary>Raw API response</summary><pre>${esc(JSON.stringify(data,null,2))}</pre></details>`;
  result.classList.remove("hidden");
}