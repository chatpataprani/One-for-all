import http from "node:http";
import {readFile} from "node:fs/promises";
import {extname,join,normalize} from "node:path";
import {fileURLToPath} from "node:url";
const root=fileURLToPath(new URL(".",import.meta.url)),port=process.env.PORT||3000;
const base=(process.env.BOTH_DB_API_URL||"https://both-db.vercel.app").replace(/\/$/,"");
const mime={".html":"text/html; charset=utf-8",".js":"text/javascript; charset=utf-8",".css":"text/css; charset=utf-8"};
function send(res,status,type,body){res.writeHead(status,{"content-type":type});res.end(body)}
const server=http.createServer(async(req,res)=>{
 try{
  if(req.method==="POST"&&req.url==="/api/lookup"){
   let raw="";for await(const chunk of req)raw+=chunk;
   const body=JSON.parse(raw||"{}"),kind=body.kind==="aadhar"?"aadhar":"number",value=String(body.value||"").trim();
   if(!value)return send(res,400,"application/json",JSON.stringify({error:"missing_value"}));
   const path=kind==="aadhar"?(process.env.BOTH_DB_AADHAAR_PATH||"/aadhar="):(process.env.BOTH_DB_NUMBER_PATH||"/number=");
   const target=base+(path.endsWith("=")?path+encodeURIComponent(value):path);
   const headers={accept:"application/json"};if(process.env.BOTH_DB_API_KEY)headers.authorization="Bearer "+process.env.BOTH_DB_API_KEY;
   const upstream=await fetch(target,{headers}),text=await upstream.text();
   res.writeHead(upstream.status,{"content-type":"application/json; charset=utf-8"});return res.end(text);
  }
  const requested=(req.url||"/").split("?")[0],rel=requested==="/"?"index.html":requested.replace(/^\/+/, ""),file=normalize(join(root,rel));
  if(!file.startsWith(root))return send(res,403,"text/plain","Forbidden");
  const data=await readFile(file);send(res,200,mime[extname(file)]||"application/octet-stream",data);
 }catch(e){send(res,404,"text/plain","Not found")}
});
server.listen(port,()=>console.log(`HAO running at http://localhost:${port}`));