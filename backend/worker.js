export default {
 async fetch(request, env) {
  const cors={ "Access-Control-Allow-Origin":"*", "Access-Control-Allow-Methods":"GET,POST,OPTIONS", "Access-Control-Allow-Headers":"Content-Type,Authorization" };
  if(request.method==="OPTIONS") return new Response(null,{headers:cors});
  const url=new URL(request.url);
  const json=(data,status=200)=>new Response(JSON.stringify(data),{status,headers:{...cors,"Content-Type":"application/json"}});
  if(url.pathname==="/health") return json({service:"GDSK API",status:"online",push:"FCM requires Firebase service account"});
  if(url.pathname==="/api/events"&&request.method==="GET"){
   const group=url.searchParams.get("group")||"";
   const rows=await env.DB.prepare("SELECT id,group_name,type,title,start_time,end_time,teacher,room,details,audience,event_date,created_at FROM events WHERE audience='all' OR group_name=? ORDER BY event_date,start_time").bind(group).all();
   return json({success:true,events:rows.results||[]});
  }
  if(url.pathname==="/api/events"&&request.method==="POST"){
   const auth=request.headers.get("Authorization")||"";
   if(!env.ADMIN_API_KEY||auth!=="Bearer "+env.ADMIN_API_KEY) return json({success:false,error:"unauthorized"},401);
   let e;try{e=await request.json()}catch{return json({success:false,error:"invalid_json"},400)}
   if(!e.title||!e.start_time||!e.event_date) return json({success:false,error:"title_start_date_required"},400);
   const id=crypto.randomUUID();
   await env.DB.prepare("INSERT INTO events(id,group_name,type,title,start_time,end_time,teacher,room,details,audience,event_date,created_at) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)").bind(id,e.audience==="all"?"*":(e.group_name||""),e.type||"lesson",e.title,e.start_time,e.end_time||"",e.teacher||"",e.room||"",e.details||"",e.audience==="all"?"all":"group",e.event_date,new Date().toISOString()).run();
   return json({success:true,id});
  }
  if(url.pathname==="/api/register"&&request.method==="POST"){
   let d;try{d=await request.json()}catch{return json({success:false},400)}
   if(!d.token||!d.group_name) return json({success:false,error:"token_and_group_required"},400);
   await env.DB.prepare("INSERT INTO devices(token,group_name,updated_at) VALUES(?,?,?) ON CONFLICT(token) DO UPDATE SET group_name=excluded.group_name,updated_at=excluded.updated_at").bind(d.token,d.group_name,new Date().toISOString()).run();
   return json({success:true});
  }
  return json({success:false,error:"not_found"},404);
 }
};