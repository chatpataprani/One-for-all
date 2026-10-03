export default async function handler(req,res){
  if(req.method!=='POST') return res.status(405).json({error:'method_not_allowed'});
  try{
    const body=typeof req.body==='string'?JSON.parse(req.body):req.body||{};
    const kind=body.kind==='aadhar'?'aadhar':'number';
    const value=String(body.value||'').trim();
    if(!value) return res.status(400).json({error:'missing_value'});

    const base=process.env.BOTH_DB_API_URL||'https://both-db.vercel.app';
    const key=process.env.BOTH_DB_API_KEY;
    const endpoint=kind==='aadhar'
      ? (process.env.BOTH_DB_AADHAAR_PATH||'/aadhar=')
      : (process.env.BOTH_DB_NUMBER_PATH||'/number=');

    const url=new URL(endpoint,base);
    url.searchParams.set(endpoint.endsWith('=')?'': 'value', value);
    const target=endpoint.endsWith('=') ? endpoint + encodeURIComponent(value) : url.toString();

    const headers={'accept':'application/json'};
    if(key) headers.authorization='Bearer '+key;

    const upstream=await fetch(target,{method:'GET',headers});
    const text=await upstream.text();
    let data;
    try{data=JSON.parse(text)}catch{data={raw:text}}
    return res.status(upstream.status).json(data);
  }catch(error){
    return res.status(502).json({error:'upstream_error',message:String(error?.message||error)});
  }
}