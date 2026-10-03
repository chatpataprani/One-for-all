export default async function handler(req,res){
  if(req.method!=="POST")return res.status(405).json({error:"method_not_allowed"});
  try{
    const body=typeof req.body==="string"?JSON.parse(req.body):req.body||{};
    const kind=body.kind==="aadhar"?"aadhar":"number";
    const value=String(body.value||"").trim();
    if(!value)return res.status(400).json({error:"missing_value"});

    // Keep the upstream service private: configure these only in Vercel/server environment variables.
    const base=String(process.env.BOTH_DB_API_URL||"").replace(/\/$/,"");
    if(!base)return res.status(500).json({error:"server_not_configured"});

    const path=kind==="aadhar"
      ? String(process.env.BOTH_DB_AADHAAR_PATH||"")
      : String(process.env.BOTH_DB_NUMBER_PATH||"");

    const target=path.endsWith("=")
      ? base+path+encodeURIComponent(value)
      : new URL(path,base).toString();

    const headers={accept:"application/json"};
    if(process.env.BOTH_DB_API_KEY)headers.authorization="Bearer "+process.env.BOTH_DB_API_KEY;

    const upstream=await fetch(target,{headers});
    const text=await upstream.text();
    let data; try{data=JSON.parse(text)}catch{data={raw:text}}
    return res.status(upstream.status).json(data);
  }catch(error){
    return res.status(502).json({error:"upstream_error",message:String(error?.message||error)});
  }
}
