import {mkdir,cp,writeFile} from "node:fs/promises";
import {join} from "node:path";
const root=process.cwd(), dist=join(root,"dist");
await mkdir(dist,{recursive:true});
for(const f of ["index.html","app.js","styles.css","manifest.webmanifest","sw.js","vercel.json"]) await cp(join(root,f),join(dist,f));
await writeFile(join(dist,"build-info.json"),JSON.stringify({name:"One-for-all",brand:"chatpataprani",builtAt:new Date().toISOString(),mode:"static-pwa",externalNavigation:false},null,2));
console.log("Built One-for-all -> dist/");
