var Yv=Object.defineProperty;var $v=(t,s,r)=>s in t?Yv(t,s,{enumerable:!0,configurable:!0,writable:!0,value:r}):t[s]=r;var tu=(t,s,r)=>($v(t,typeof s!="symbol"?s+"":s,r),r);import{c as Ei,u as ma}from"./_commonjsHelpers.f9c16f81.js";import{b as va}from"./paths.5c662560.js";function Ir(){return{convert:t=>t==null||t===""?null:String(t),asString:t=>String(t),type:t=>t.string().optional()}}function iu(t){return{convert:s=>{if(typeof s=="object")return s;if(typeof s=="string"){const r=JSON.parse(s);return typeof r!="object"?null:r}return null},asString:s=>JSON.stringify(s),type:s=>s.record(t(s).optional()).optional()}}function Er(t){return{convert:s=>{if(Array.isArray(s))return s;if(typeof s=="string"){const r=JSON.parse(s);return Array.isArray(r)?r:null}return null},asString:s=>JSON.stringify(s),type:s=>s.array(t(s)).optional()}}function ru(t){return{convert:s=>s==null||!t.find(r=>r===s)?null:s,asString:s=>String(s),type:s=>s.enum(t).optional()}}function an(t,s){const r=o=>s.convert(o[t]);return{name:jv(t),key:t,asEnv:o=>({[t]:s.asString(o)}),zod:{type:s.type},get:r,getOrDefault:(o,l)=>r(o)??l}}function jv(t){return t.toLowerCase().replace(/([-_][a-z])/gi,s=>s.toUpperCase().replace("-","").replace("_",""))}var Hv={APP_LOGO:an("APP_LOGO",Ir()),APP_TITLE:an("APP_TITLE",Ir()),APP_FAVICON:an("APP_FAVICON",Ir()),SITE_ROOT:an("SITE_ROOT",Ir()),SITE_META:an("SITE_META",iu(t=>t.string())),CUSTOM_STYLES:an("CUSTOM_STYLES",Er(t=>t.string())),FIELDS_SORTING:an("FIELDS_SORTING",ru(["default","alphabetical"])),ARGUMENTS_SORTING:an("ARGUMENTS_SORTING",ru(["default","alphabetical"])),QUERY_GENERATION_FACTORIES:an("QUERY_GENERATION_FACTORIES",iu(t=>t.union([t.string(),t.boolean(),t.number(),t.null(),t.record(t.unknown())]))),PAGES:an("PAGES",Er(t=>{const s=t.lazy(()=>t.object({title:t.string().min(1),content:t.union([t.array(s),t.string().min(1)])}));return s})),EXTERNAL_LINKS:an("EXTERNAL_LINKS",Er(t=>t.object({label:t.string().min(1),href:t.string().min(1),position:t.union([t.literal("header"),t.literal("navigation")]).optional(),kind:t.string().min(1).optional(),group:t.string().min(1).optional()}))),DIRECTIVES:an("DIRECTIVES",Er(t=>t.object({name:t.string().min(1),args:t.array(t.string())})))};function qv(){return{convert:t=>{if(t===null||t===void 0)return null;switch(typeof t){case"boolean":return t;case"string":const s=t.toLowerCase().trim();return s==="true"||s==="t"||s==="1";case"number":return t!==0;default:return null}},asString:t=>String(t),type:t=>t.boolean().optional()}}an("MAGIDOC_GENERATE",qv());var Tn={},Qv={get exports(){return Tn},set exports(t){Tn=t}};/**
 * @license
 * Lodash <https://lodash.com/>
 * Copyright OpenJS Foundation and other contributors <https://openjsf.org/>
 * Released under MIT license <https://lodash.com/license>
 * Based on Underscore.js 1.8.3 <http://underscorejs.org/LICENSE>
 * Copyright Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
 */(function(t,s){(function(){var r,o="4.17.21",l=200,h="Unsupported core-js use. Try https://npms.io/search?q=ponyfill.",g="Expected a function",T="Invalid `variable` option passed into `_.template`",I="__lodash_hash_undefined__",O=500,_="__lodash_placeholder__",x=1,B=2,K=4,V=1,he=2,se=1,ye=2,Yn=4,Te=8,Ne=16,De=32,Me=64,Fe=128,$n=256,R=512,q=30,Ge="...",rt=800,yt=16,Ri=1,Lt=2,Di=3,Pn=1/0,En=9007199254740991,kt=17976931348623157e292,Tt=0/0,on=4294967295,w=on-1,P=on>>>1,k=[["ary",Fe],["bind",se],["bindKey",ye],["curry",Te],["curryRight",Ne],["flip",R],["partial",De],["partialRight",Me],["rearg",$n]],C="[object Arguments]",le="[object Array]",be="[object AsyncFunction]",ue="[object Boolean]",ln="[object Date]",ei="[object DOMException]",st="[object Error]",Pe="[object Function]",_a="[object GeneratorFunction]",bn="[object Map]",ni="[object Number]",sc="[object Null]",jn="[object Object]",Sa="[object Promise]",ac="[object Proxy]",ti="[object RegExp]",wn="[object Set]",ii="[object String]",xi="[object Symbol]",oc="[object Undefined]",ri="[object WeakMap]",lc="[object WeakSet]",si="[object ArrayBuffer]",Ct="[object DataView]",Br="[object Float32Array]",Mr="[object Float64Array]",Gr="[object Int8Array]",Vr="[object Int16Array]",Wr="[object Int32Array]",Yr="[object Uint8Array]",$r="[object Uint8ClampedArray]",jr="[object Uint16Array]",Hr="[object Uint32Array]",uc=/\b__p \+= '';/g,cc=/\b(__p \+=) '' \+/g,fc=/(__e\(.*?\)|\b__t\)) \+\n'';/g,Na=/&(?:amp|lt|gt|quot|#39);/g,Aa=/[&<>"']/g,hc=RegExp(Na.source),dc=RegExp(Aa.source),pc=/<%-([\s\S]+?)%>/g,gc=/<%([\s\S]+?)%>/g,Oa=/<%=([\s\S]+?)%>/g,mc=/\.|\[(?:[^[\]]*|(["'])(?:(?!\1)[^\\]|\\.)*?\1)\]/,vc=/^\w*$/,yc=/[^.[\]]+|\[(?:(-?\d+(?:\.\d+)?)|(["'])((?:(?!\2)[^\\]|\\.)*?)\2)\]|(?=(?:\.|\[\])(?:\.|\[\]|$))/g,qr=/[\\^$.*+?()[\]{}|]/g,Tc=RegExp(qr.source),Qr=/^\s+/,Ic=/\s/,Ec=/\{(?:\n\/\* \[wrapped with .+\] \*\/)?\n?/,bc=/\{\n\/\* \[wrapped with (.+)\] \*/,wc=/,? & /,_c=/[^\x00-\x2f\x3a-\x40\x5b-\x60\x7b-\x7f]+/g,Sc=/[()=,{}\[\]\/\s]/,Nc=/\\(\\)?/g,Ac=/\$\{([^\\}]*(?:\\.[^\\}]*)*)\}/g,Ra=/\w*$/,Oc=/^[-+]0x[0-9a-f]+$/i,Rc=/^0b[01]+$/i,Dc=/^\[object .+?Constructor\]$/,xc=/^0o[0-7]+$/i,Lc=/^(?:0|[1-9]\d*)$/,kc=/[\xc0-\xd6\xd8-\xf6\xf8-\xff\u0100-\u017f]/g,Li=/($^)/,Cc=/['\n\r\u2028\u2029\\]/g,ki="\\ud800-\\udfff",Fc="\\u0300-\\u036f",Pc="\\ufe20-\\ufe2f",Uc="\\u20d0-\\u20ff",Da=Fc+Pc+Uc,xa="\\u2700-\\u27bf",La="a-z\\xdf-\\xf6\\xf8-\\xff",Bc="\\xac\\xb1\\xd7\\xf7",Mc="\\x00-\\x2f\\x3a-\\x40\\x5b-\\x60\\x7b-\\xbf",Gc="\\u2000-\\u206f",Vc=" \\t\\x0b\\f\\xa0\\ufeff\\n\\r\\u2028\\u2029\\u1680\\u180e\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200a\\u202f\\u205f\\u3000",ka="A-Z\\xc0-\\xd6\\xd8-\\xde",Ca="\\ufe0e\\ufe0f",Fa=Bc+Mc+Gc+Vc,Jr="['’]",Wc="["+ki+"]",Pa="["+Fa+"]",Ci="["+Da+"]",Ua="\\d+",Yc="["+xa+"]",Ba="["+La+"]",Ma="[^"+ki+Fa+Ua+xa+La+ka+"]",zr="\\ud83c[\\udffb-\\udfff]",$c="(?:"+Ci+"|"+zr+")",Ga="[^"+ki+"]",Xr="(?:\\ud83c[\\udde6-\\uddff]){2}",Kr="[\\ud800-\\udbff][\\udc00-\\udfff]",Ft="["+ka+"]",Va="\\u200d",Wa="(?:"+Ba+"|"+Ma+")",jc="(?:"+Ft+"|"+Ma+")",Ya="(?:"+Jr+"(?:d|ll|m|re|s|t|ve))?",$a="(?:"+Jr+"(?:D|LL|M|RE|S|T|VE))?",ja=$c+"?",Ha="["+Ca+"]?",Hc="(?:"+Va+"(?:"+[Ga,Xr,Kr].join("|")+")"+Ha+ja+")*",qc="\\d*(?:1st|2nd|3rd|(?![123])\\dth)(?=\\b|[A-Z_])",Qc="\\d*(?:1ST|2ND|3RD|(?![123])\\dTH)(?=\\b|[a-z_])",qa=Ha+ja+Hc,Jc="(?:"+[Yc,Xr,Kr].join("|")+")"+qa,zc="(?:"+[Ga+Ci+"?",Ci,Xr,Kr,Wc].join("|")+")",Xc=RegExp(Jr,"g"),Kc=RegExp(Ci,"g"),Zr=RegExp(zr+"(?="+zr+")|"+zc+qa,"g"),Zc=RegExp([Ft+"?"+Ba+"+"+Ya+"(?="+[Pa,Ft,"$"].join("|")+")",jc+"+"+$a+"(?="+[Pa,Ft+Wa,"$"].join("|")+")",Ft+"?"+Wa+"+"+Ya,Ft+"+"+$a,Qc,qc,Ua,Jc].join("|"),"g"),ef=RegExp("["+Va+ki+Da+Ca+"]"),nf=/[a-z][A-Z]|[A-Z]{2}[a-z]|[0-9][a-zA-Z]|[a-zA-Z][0-9]|[^a-zA-Z0-9 ]/,tf=["Array","Buffer","DataView","Date","Error","Float32Array","Float64Array","Function","Int8Array","Int16Array","Int32Array","Map","Math","Object","Promise","RegExp","Set","String","Symbol","TypeError","Uint8Array","Uint8ClampedArray","Uint16Array","Uint32Array","WeakMap","_","clearTimeout","isFinite","parseInt","setTimeout"],rf=-1,me={};me[Br]=me[Mr]=me[Gr]=me[Vr]=me[Wr]=me[Yr]=me[$r]=me[jr]=me[Hr]=!0,me[C]=me[le]=me[si]=me[ue]=me[Ct]=me[ln]=me[st]=me[Pe]=me[bn]=me[ni]=me[jn]=me[ti]=me[wn]=me[ii]=me[ri]=!1;var ge={};ge[C]=ge[le]=ge[si]=ge[Ct]=ge[ue]=ge[ln]=ge[Br]=ge[Mr]=ge[Gr]=ge[Vr]=ge[Wr]=ge[bn]=ge[ni]=ge[jn]=ge[ti]=ge[wn]=ge[ii]=ge[xi]=ge[Yr]=ge[$r]=ge[jr]=ge[Hr]=!0,ge[st]=ge[Pe]=ge[ri]=!1;var sf={À:"A",Á:"A",Â:"A",Ã:"A",Ä:"A",Å:"A",à:"a",á:"a",â:"a",ã:"a",ä:"a",å:"a",Ç:"C",ç:"c",Ð:"D",ð:"d",È:"E",É:"E",Ê:"E",Ë:"E",è:"e",é:"e",ê:"e",ë:"e",Ì:"I",Í:"I",Î:"I",Ï:"I",ì:"i",í:"i",î:"i",ï:"i",Ñ:"N",ñ:"n",Ò:"O",Ó:"O",Ô:"O",Õ:"O",Ö:"O",Ø:"O",ò:"o",ó:"o",ô:"o",õ:"o",ö:"o",ø:"o",Ù:"U",Ú:"U",Û:"U",Ü:"U",ù:"u",ú:"u",û:"u",ü:"u",Ý:"Y",ý:"y",ÿ:"y",Æ:"Ae",æ:"ae",Þ:"Th",þ:"th",ß:"ss",Ā:"A",Ă:"A",Ą:"A",ā:"a",ă:"a",ą:"a",Ć:"C",Ĉ:"C",Ċ:"C",Č:"C",ć:"c",ĉ:"c",ċ:"c",č:"c",Ď:"D",Đ:"D",ď:"d",đ:"d",Ē:"E",Ĕ:"E",Ė:"E",Ę:"E",Ě:"E",ē:"e",ĕ:"e",ė:"e",ę:"e",ě:"e",Ĝ:"G",Ğ:"G",Ġ:"G",Ģ:"G",ĝ:"g",ğ:"g",ġ:"g",ģ:"g",Ĥ:"H",Ħ:"H",ĥ:"h",ħ:"h",Ĩ:"I",Ī:"I",Ĭ:"I",Į:"I",İ:"I",ĩ:"i",ī:"i",ĭ:"i",į:"i",ı:"i",Ĵ:"J",ĵ:"j",Ķ:"K",ķ:"k",ĸ:"k",Ĺ:"L",Ļ:"L",Ľ:"L",Ŀ:"L",Ł:"L",ĺ:"l",ļ:"l",ľ:"l",ŀ:"l",ł:"l",Ń:"N",Ņ:"N",Ň:"N",Ŋ:"N",ń:"n",ņ:"n",ň:"n",ŋ:"n",Ō:"O",Ŏ:"O",Ő:"O",ō:"o",ŏ:"o",ő:"o",Ŕ:"R",Ŗ:"R",Ř:"R",ŕ:"r",ŗ:"r",ř:"r",Ś:"S",Ŝ:"S",Ş:"S",Š:"S",ś:"s",ŝ:"s",ş:"s",š:"s",Ţ:"T",Ť:"T",Ŧ:"T",ţ:"t",ť:"t",ŧ:"t",Ũ:"U",Ū:"U",Ŭ:"U",Ů:"U",Ű:"U",Ų:"U",ũ:"u",ū:"u",ŭ:"u",ů:"u",ű:"u",ų:"u",Ŵ:"W",ŵ:"w",Ŷ:"Y",ŷ:"y",Ÿ:"Y",Ź:"Z",Ż:"Z",Ž:"Z",ź:"z",ż:"z",ž:"z",Ĳ:"IJ",ĳ:"ij",Œ:"Oe",œ:"oe",ŉ:"'n",ſ:"s"},af={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"},of={"&amp;":"&","&lt;":"<","&gt;":">","&quot;":'"',"&#39;":"'"},lf={"\\":"\\","'":"'","\n":"n","\r":"r","\u2028":"u2028","\u2029":"u2029"},uf=parseFloat,cf=parseInt,Qa=typeof Ei=="object"&&Ei&&Ei.Object===Object&&Ei,ff=typeof self=="object"&&self&&self.Object===Object&&self,ke=Qa||ff||Function("return this")(),es=s&&!s.nodeType&&s,It=es&&!0&&t&&!t.nodeType&&t,Ja=It&&It.exports===es,ns=Ja&&Qa.process,un=function(){try{var m=It&&It.require&&It.require("util").types;return m||ns&&ns.binding&&ns.binding("util")}catch{}}(),za=un&&un.isArrayBuffer,Xa=un&&un.isDate,Ka=un&&un.isMap,Za=un&&un.isRegExp,eo=un&&un.isSet,no=un&&un.isTypedArray;function Ze(m,b,E){switch(E.length){case 0:return m.call(b);case 1:return m.call(b,E[0]);case 2:return m.call(b,E[0],E[1]);case 3:return m.call(b,E[0],E[1],E[2])}return m.apply(b,E)}function hf(m,b,E,F){for(var Q=-1,oe=m==null?0:m.length;++Q<oe;){var Oe=m[Q];b(F,Oe,E(Oe),m)}return F}function cn(m,b){for(var E=-1,F=m==null?0:m.length;++E<F&&b(m[E],E,m)!==!1;);return m}function df(m,b){for(var E=m==null?0:m.length;E--&&b(m[E],E,m)!==!1;);return m}function to(m,b){for(var E=-1,F=m==null?0:m.length;++E<F;)if(!b(m[E],E,m))return!1;return!0}function at(m,b){for(var E=-1,F=m==null?0:m.length,Q=0,oe=[];++E<F;){var Oe=m[E];b(Oe,E,m)&&(oe[Q++]=Oe)}return oe}function Fi(m,b){var E=m==null?0:m.length;return!!E&&Pt(m,b,0)>-1}function ts(m,b,E){for(var F=-1,Q=m==null?0:m.length;++F<Q;)if(E(b,m[F]))return!0;return!1}function ve(m,b){for(var E=-1,F=m==null?0:m.length,Q=Array(F);++E<F;)Q[E]=b(m[E],E,m);return Q}function ot(m,b){for(var E=-1,F=b.length,Q=m.length;++E<F;)m[Q+E]=b[E];return m}function is(m,b,E,F){var Q=-1,oe=m==null?0:m.length;for(F&&oe&&(E=m[++Q]);++Q<oe;)E=b(E,m[Q],Q,m);return E}function pf(m,b,E,F){var Q=m==null?0:m.length;for(F&&Q&&(E=m[--Q]);Q--;)E=b(E,m[Q],Q,m);return E}function rs(m,b){for(var E=-1,F=m==null?0:m.length;++E<F;)if(b(m[E],E,m))return!0;return!1}var gf=ss("length");function mf(m){return m.split("")}function vf(m){return m.match(_c)||[]}function io(m,b,E){var F;return E(m,function(Q,oe,Oe){if(b(Q,oe,Oe))return F=oe,!1}),F}function Pi(m,b,E,F){for(var Q=m.length,oe=E+(F?1:-1);F?oe--:++oe<Q;)if(b(m[oe],oe,m))return oe;return-1}function Pt(m,b,E){return b===b?Rf(m,b,E):Pi(m,ro,E)}function yf(m,b,E,F){for(var Q=E-1,oe=m.length;++Q<oe;)if(F(m[Q],b))return Q;return-1}function ro(m){return m!==m}function so(m,b){var E=m==null?0:m.length;return E?os(m,b)/E:Tt}function ss(m){return function(b){return b==null?r:b[m]}}function as(m){return function(b){return m==null?r:m[b]}}function ao(m,b,E,F,Q){return Q(m,function(oe,Oe,de){E=F?(F=!1,oe):b(E,oe,Oe,de)}),E}function Tf(m,b){var E=m.length;for(m.sort(b);E--;)m[E]=m[E].value;return m}function os(m,b){for(var E,F=-1,Q=m.length;++F<Q;){var oe=b(m[F]);oe!==r&&(E=E===r?oe:E+oe)}return E}function ls(m,b){for(var E=-1,F=Array(m);++E<m;)F[E]=b(E);return F}function If(m,b){return ve(b,function(E){return[E,m[E]]})}function oo(m){return m&&m.slice(0,fo(m)+1).replace(Qr,"")}function en(m){return function(b){return m(b)}}function us(m,b){return ve(b,function(E){return m[E]})}function ai(m,b){return m.has(b)}function lo(m,b){for(var E=-1,F=m.length;++E<F&&Pt(b,m[E],0)>-1;);return E}function uo(m,b){for(var E=m.length;E--&&Pt(b,m[E],0)>-1;);return E}function Ef(m,b){for(var E=m.length,F=0;E--;)m[E]===b&&++F;return F}var bf=as(sf),wf=as(af);function _f(m){return"\\"+lf[m]}function Sf(m,b){return m==null?r:m[b]}function Ut(m){return ef.test(m)}function Nf(m){return nf.test(m)}function Af(m){for(var b,E=[];!(b=m.next()).done;)E.push(b.value);return E}function cs(m){var b=-1,E=Array(m.size);return m.forEach(function(F,Q){E[++b]=[Q,F]}),E}function co(m,b){return function(E){return m(b(E))}}function lt(m,b){for(var E=-1,F=m.length,Q=0,oe=[];++E<F;){var Oe=m[E];(Oe===b||Oe===_)&&(m[E]=_,oe[Q++]=E)}return oe}function Ui(m){var b=-1,E=Array(m.size);return m.forEach(function(F){E[++b]=F}),E}function Of(m){var b=-1,E=Array(m.size);return m.forEach(function(F){E[++b]=[F,F]}),E}function Rf(m,b,E){for(var F=E-1,Q=m.length;++F<Q;)if(m[F]===b)return F;return-1}function Df(m,b,E){for(var F=E+1;F--;)if(m[F]===b)return F;return F}function Bt(m){return Ut(m)?Lf(m):gf(m)}function _n(m){return Ut(m)?kf(m):mf(m)}function fo(m){for(var b=m.length;b--&&Ic.test(m.charAt(b)););return b}var xf=as(of);function Lf(m){for(var b=Zr.lastIndex=0;Zr.test(m);)++b;return b}function kf(m){return m.match(Zr)||[]}function Cf(m){return m.match(Zc)||[]}var Ff=function m(b){b=b==null?ke:Mt.defaults(ke.Object(),b,Mt.pick(ke,tf));var E=b.Array,F=b.Date,Q=b.Error,oe=b.Function,Oe=b.Math,de=b.Object,fs=b.RegExp,Pf=b.String,fn=b.TypeError,Bi=E.prototype,Uf=oe.prototype,Gt=de.prototype,Mi=b["__core-js_shared__"],Gi=Uf.toString,fe=Gt.hasOwnProperty,Bf=0,ho=function(){var e=/[^.]+$/.exec(Mi&&Mi.keys&&Mi.keys.IE_PROTO||"");return e?"Symbol(src)_1."+e:""}(),Vi=Gt.toString,Mf=Gi.call(de),Gf=ke._,Vf=fs("^"+Gi.call(fe).replace(qr,"\\$&").replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g,"$1.*?")+"$"),Wi=Ja?b.Buffer:r,ut=b.Symbol,Yi=b.Uint8Array,po=Wi?Wi.allocUnsafe:r,$i=co(de.getPrototypeOf,de),go=de.create,mo=Gt.propertyIsEnumerable,ji=Bi.splice,vo=ut?ut.isConcatSpreadable:r,oi=ut?ut.iterator:r,Et=ut?ut.toStringTag:r,Hi=function(){try{var e=Nt(de,"defineProperty");return e({},"",{}),e}catch{}}(),Wf=b.clearTimeout!==ke.clearTimeout&&b.clearTimeout,Yf=F&&F.now!==ke.Date.now&&F.now,$f=b.setTimeout!==ke.setTimeout&&b.setTimeout,qi=Oe.ceil,Qi=Oe.floor,hs=de.getOwnPropertySymbols,jf=Wi?Wi.isBuffer:r,yo=b.isFinite,Hf=Bi.join,qf=co(de.keys,de),Re=Oe.max,Ue=Oe.min,Qf=F.now,Jf=b.parseInt,To=Oe.random,zf=Bi.reverse,ds=Nt(b,"DataView"),li=Nt(b,"Map"),ps=Nt(b,"Promise"),Vt=Nt(b,"Set"),ui=Nt(b,"WeakMap"),ci=Nt(de,"create"),Ji=ui&&new ui,Wt={},Xf=At(ds),Kf=At(li),Zf=At(ps),eh=At(Vt),nh=At(ui),zi=ut?ut.prototype:r,fi=zi?zi.valueOf:r,Io=zi?zi.toString:r;function c(e){if(Ee(e)&&!J(e)&&!(e instanceof te)){if(e instanceof hn)return e;if(fe.call(e,"__wrapped__"))return El(e)}return new hn(e)}var Yt=function(){function e(){}return function(n){if(!Ie(n))return{};if(go)return go(n);e.prototype=n;var i=new e;return e.prototype=r,i}}();function Xi(){}function hn(e,n){this.__wrapped__=e,this.__actions__=[],this.__chain__=!!n,this.__index__=0,this.__values__=r}c.templateSettings={escape:pc,evaluate:gc,interpolate:Oa,variable:"",imports:{_:c}},c.prototype=Xi.prototype,c.prototype.constructor=c,hn.prototype=Yt(Xi.prototype),hn.prototype.constructor=hn;function te(e){this.__wrapped__=e,this.__actions__=[],this.__dir__=1,this.__filtered__=!1,this.__iteratees__=[],this.__takeCount__=on,this.__views__=[]}function th(){var e=new te(this.__wrapped__);return e.__actions__=qe(this.__actions__),e.__dir__=this.__dir__,e.__filtered__=this.__filtered__,e.__iteratees__=qe(this.__iteratees__),e.__takeCount__=this.__takeCount__,e.__views__=qe(this.__views__),e}function ih(){if(this.__filtered__){var e=new te(this);e.__dir__=-1,e.__filtered__=!0}else e=this.clone(),e.__dir__*=-1;return e}function rh(){var e=this.__wrapped__.value(),n=this.__dir__,i=J(e),a=n<0,u=i?e.length:0,f=md(0,u,this.__views__),d=f.start,p=f.end,v=p-d,S=a?p:d-1,N=this.__iteratees__,D=N.length,L=0,M=Ue(v,this.__takeCount__);if(!i||!a&&u==v&&M==v)return $o(e,this.__actions__);var j=[];e:for(;v--&&L<M;){S+=n;for(var X=-1,H=e[S];++X<D;){var ee=N[X],re=ee.iteratee,rn=ee.type,Ye=re(H);if(rn==Lt)H=Ye;else if(!Ye){if(rn==Ri)continue e;break e}}j[L++]=H}return j}te.prototype=Yt(Xi.prototype),te.prototype.constructor=te;function bt(e){var n=-1,i=e==null?0:e.length;for(this.clear();++n<i;){var a=e[n];this.set(a[0],a[1])}}function sh(){this.__data__=ci?ci(null):{},this.size=0}function ah(e){var n=this.has(e)&&delete this.__data__[e];return this.size-=n?1:0,n}function oh(e){var n=this.__data__;if(ci){var i=n[e];return i===I?r:i}return fe.call(n,e)?n[e]:r}function lh(e){var n=this.__data__;return ci?n[e]!==r:fe.call(n,e)}function uh(e,n){var i=this.__data__;return this.size+=this.has(e)?0:1,i[e]=ci&&n===r?I:n,this}bt.prototype.clear=sh,bt.prototype.delete=ah,bt.prototype.get=oh,bt.prototype.has=lh,bt.prototype.set=uh;function Hn(e){var n=-1,i=e==null?0:e.length;for(this.clear();++n<i;){var a=e[n];this.set(a[0],a[1])}}function ch(){this.__data__=[],this.size=0}function fh(e){var n=this.__data__,i=Ki(n,e);if(i<0)return!1;var a=n.length-1;return i==a?n.pop():ji.call(n,i,1),--this.size,!0}function hh(e){var n=this.__data__,i=Ki(n,e);return i<0?r:n[i][1]}function dh(e){return Ki(this.__data__,e)>-1}function ph(e,n){var i=this.__data__,a=Ki(i,e);return a<0?(++this.size,i.push([e,n])):i[a][1]=n,this}Hn.prototype.clear=ch,Hn.prototype.delete=fh,Hn.prototype.get=hh,Hn.prototype.has=dh,Hn.prototype.set=ph;function qn(e){var n=-1,i=e==null?0:e.length;for(this.clear();++n<i;){var a=e[n];this.set(a[0],a[1])}}function gh(){this.size=0,this.__data__={hash:new bt,map:new(li||Hn),string:new bt}}function mh(e){var n=cr(this,e).delete(e);return this.size-=n?1:0,n}function vh(e){return cr(this,e).get(e)}function yh(e){return cr(this,e).has(e)}function Th(e,n){var i=cr(this,e),a=i.size;return i.set(e,n),this.size+=i.size==a?0:1,this}qn.prototype.clear=gh,qn.prototype.delete=mh,qn.prototype.get=vh,qn.prototype.has=yh,qn.prototype.set=Th;function wt(e){var n=-1,i=e==null?0:e.length;for(this.__data__=new qn;++n<i;)this.add(e[n])}function Ih(e){return this.__data__.set(e,I),this}function Eh(e){return this.__data__.has(e)}wt.prototype.add=wt.prototype.push=Ih,wt.prototype.has=Eh;function Sn(e){var n=this.__data__=new Hn(e);this.size=n.size}function bh(){this.__data__=new Hn,this.size=0}function wh(e){var n=this.__data__,i=n.delete(e);return this.size=n.size,i}function _h(e){return this.__data__.get(e)}function Sh(e){return this.__data__.has(e)}function Nh(e,n){var i=this.__data__;if(i instanceof Hn){var a=i.__data__;if(!li||a.length<l-1)return a.push([e,n]),this.size=++i.size,this;i=this.__data__=new qn(a)}return i.set(e,n),this.size=i.size,this}Sn.prototype.clear=bh,Sn.prototype.delete=wh,Sn.prototype.get=_h,Sn.prototype.has=Sh,Sn.prototype.set=Nh;function Eo(e,n){var i=J(e),a=!i&&Ot(e),u=!i&&!a&&pt(e),f=!i&&!a&&!u&&qt(e),d=i||a||u||f,p=d?ls(e.length,Pf):[],v=p.length;for(var S in e)(n||fe.call(e,S))&&!(d&&(S=="length"||u&&(S=="offset"||S=="parent")||f&&(S=="buffer"||S=="byteLength"||S=="byteOffset")||Xn(S,v)))&&p.push(S);return p}function bo(e){var n=e.length;return n?e[Ss(0,n-1)]:r}function Ah(e,n){return fr(qe(e),_t(n,0,e.length))}function Oh(e){return fr(qe(e))}function gs(e,n,i){(i!==r&&!Nn(e[n],i)||i===r&&!(n in e))&&Qn(e,n,i)}function hi(e,n,i){var a=e[n];(!(fe.call(e,n)&&Nn(a,i))||i===r&&!(n in e))&&Qn(e,n,i)}function Ki(e,n){for(var i=e.length;i--;)if(Nn(e[i][0],n))return i;return-1}function Rh(e,n,i,a){return ct(e,function(u,f,d){n(a,u,i(u),d)}),a}function wo(e,n){return e&&Bn(n,xe(n),e)}function Dh(e,n){return e&&Bn(n,Je(n),e)}function Qn(e,n,i){n=="__proto__"&&Hi?Hi(e,n,{configurable:!0,enumerable:!0,value:i,writable:!0}):e[n]=i}function ms(e,n){for(var i=-1,a=n.length,u=E(a),f=e==null;++i<a;)u[i]=f?r:zs(e,n[i]);return u}function _t(e,n,i){return e===e&&(i!==r&&(e=e<=i?e:i),n!==r&&(e=e>=n?e:n)),e}function dn(e,n,i,a,u,f){var d,p=n&x,v=n&B,S=n&K;if(i&&(d=u?i(e,a,u,f):i(e)),d!==r)return d;if(!Ie(e))return e;var N=J(e);if(N){if(d=yd(e),!p)return qe(e,d)}else{var D=Be(e),L=D==Pe||D==_a;if(pt(e))return qo(e,p);if(D==jn||D==C||L&&!u){if(d=v||L?{}:hl(e),!p)return v?od(e,Dh(d,e)):ad(e,wo(d,e))}else{if(!ge[D])return u?e:{};d=Td(e,D,p)}}f||(f=new Sn);var M=f.get(e);if(M)return M;f.set(e,d),Vl(e)?e.forEach(function(H){d.add(dn(H,n,i,H,e,f))}):Ml(e)&&e.forEach(function(H,ee){d.set(ee,dn(H,n,i,ee,e,f))});var j=S?v?Ps:Fs:v?Je:xe,X=N?r:j(e);return cn(X||e,function(H,ee){X&&(ee=H,H=e[ee]),hi(d,ee,dn(H,n,i,ee,e,f))}),d}function xh(e){var n=xe(e);return function(i){return _o(i,e,n)}}function _o(e,n,i){var a=i.length;if(e==null)return!a;for(e=de(e);a--;){var u=i[a],f=n[u],d=e[u];if(d===r&&!(u in e)||!f(d))return!1}return!0}function So(e,n,i){if(typeof e!="function")throw new fn(g);return Ti(function(){e.apply(r,i)},n)}function di(e,n,i,a){var u=-1,f=Fi,d=!0,p=e.length,v=[],S=n.length;if(!p)return v;i&&(n=ve(n,en(i))),a?(f=ts,d=!1):n.length>=l&&(f=ai,d=!1,n=new wt(n));e:for(;++u<p;){var N=e[u],D=i==null?N:i(N);if(N=a||N!==0?N:0,d&&D===D){for(var L=S;L--;)if(n[L]===D)continue e;v.push(N)}else f(n,D,a)||v.push(N)}return v}var ct=Ko(Un),No=Ko(ys,!0);function Lh(e,n){var i=!0;return ct(e,function(a,u,f){return i=!!n(a,u,f),i}),i}function Zi(e,n,i){for(var a=-1,u=e.length;++a<u;){var f=e[a],d=n(f);if(d!=null&&(p===r?d===d&&!tn(d):i(d,p)))var p=d,v=f}return v}function kh(e,n,i,a){var u=e.length;for(i=z(i),i<0&&(i=-i>u?0:u+i),a=a===r||a>u?u:z(a),a<0&&(a+=u),a=i>a?0:Yl(a);i<a;)e[i++]=n;return e}function Ao(e,n){var i=[];return ct(e,function(a,u,f){n(a,u,f)&&i.push(a)}),i}function Ce(e,n,i,a,u){var f=-1,d=e.length;for(i||(i=Ed),u||(u=[]);++f<d;){var p=e[f];n>0&&i(p)?n>1?Ce(p,n-1,i,a,u):ot(u,p):a||(u[u.length]=p)}return u}var vs=Zo(),Oo=Zo(!0);function Un(e,n){return e&&vs(e,n,xe)}function ys(e,n){return e&&Oo(e,n,xe)}function er(e,n){return at(n,function(i){return Kn(e[i])})}function St(e,n){n=ht(n,e);for(var i=0,a=n.length;e!=null&&i<a;)e=e[Mn(n[i++])];return i&&i==a?e:r}function Ro(e,n,i){var a=n(e);return J(e)?a:ot(a,i(e))}function Ve(e){return e==null?e===r?oc:sc:Et&&Et in de(e)?gd(e):Od(e)}function Ts(e,n){return e>n}function Ch(e,n){return e!=null&&fe.call(e,n)}function Fh(e,n){return e!=null&&n in de(e)}function Ph(e,n,i){return e>=Ue(n,i)&&e<Re(n,i)}function Is(e,n,i){for(var a=i?ts:Fi,u=e[0].length,f=e.length,d=f,p=E(f),v=1/0,S=[];d--;){var N=e[d];d&&n&&(N=ve(N,en(n))),v=Ue(N.length,v),p[d]=!i&&(n||u>=120&&N.length>=120)?new wt(d&&N):r}N=e[0];var D=-1,L=p[0];e:for(;++D<u&&S.length<v;){var M=N[D],j=n?n(M):M;if(M=i||M!==0?M:0,!(L?ai(L,j):a(S,j,i))){for(d=f;--d;){var X=p[d];if(!(X?ai(X,j):a(e[d],j,i)))continue e}L&&L.push(j),S.push(M)}}return S}function Uh(e,n,i,a){return Un(e,function(u,f,d){n(a,i(u),f,d)}),a}function pi(e,n,i){n=ht(n,e),e=ml(e,n);var a=e==null?e:e[Mn(gn(n))];return a==null?r:Ze(a,e,i)}function Do(e){return Ee(e)&&Ve(e)==C}function Bh(e){return Ee(e)&&Ve(e)==si}function Mh(e){return Ee(e)&&Ve(e)==ln}function gi(e,n,i,a,u){return e===n?!0:e==null||n==null||!Ee(e)&&!Ee(n)?e!==e&&n!==n:Gh(e,n,i,a,gi,u)}function Gh(e,n,i,a,u,f){var d=J(e),p=J(n),v=d?le:Be(e),S=p?le:Be(n);v=v==C?jn:v,S=S==C?jn:S;var N=v==jn,D=S==jn,L=v==S;if(L&&pt(e)){if(!pt(n))return!1;d=!0,N=!1}if(L&&!N)return f||(f=new Sn),d||qt(e)?ul(e,n,i,a,u,f):dd(e,n,v,i,a,u,f);if(!(i&V)){var M=N&&fe.call(e,"__wrapped__"),j=D&&fe.call(n,"__wrapped__");if(M||j){var X=M?e.value():e,H=j?n.value():n;return f||(f=new Sn),u(X,H,i,a,f)}}return L?(f||(f=new Sn),pd(e,n,i,a,u,f)):!1}function Vh(e){return Ee(e)&&Be(e)==bn}function Es(e,n,i,a){var u=i.length,f=u,d=!a;if(e==null)return!f;for(e=de(e);u--;){var p=i[u];if(d&&p[2]?p[1]!==e[p[0]]:!(p[0]in e))return!1}for(;++u<f;){p=i[u];var v=p[0],S=e[v],N=p[1];if(d&&p[2]){if(S===r&&!(v in e))return!1}else{var D=new Sn;if(a)var L=a(S,N,v,e,n,D);if(!(L===r?gi(N,S,V|he,a,D):L))return!1}}return!0}function xo(e){if(!Ie(e)||wd(e))return!1;var n=Kn(e)?Vf:Dc;return n.test(At(e))}function Wh(e){return Ee(e)&&Ve(e)==ti}function Yh(e){return Ee(e)&&Be(e)==wn}function $h(e){return Ee(e)&&vr(e.length)&&!!me[Ve(e)]}function Lo(e){return typeof e=="function"?e:e==null?ze:typeof e=="object"?J(e)?Fo(e[0],e[1]):Co(e):eu(e)}function bs(e){if(!yi(e))return qf(e);var n=[];for(var i in de(e))fe.call(e,i)&&i!="constructor"&&n.push(i);return n}function jh(e){if(!Ie(e))return Ad(e);var n=yi(e),i=[];for(var a in e)a=="constructor"&&(n||!fe.call(e,a))||i.push(a);return i}function ws(e,n){return e<n}function ko(e,n){var i=-1,a=Qe(e)?E(e.length):[];return ct(e,function(u,f,d){a[++i]=n(u,f,d)}),a}function Co(e){var n=Bs(e);return n.length==1&&n[0][2]?pl(n[0][0],n[0][1]):function(i){return i===e||Es(i,e,n)}}function Fo(e,n){return Gs(e)&&dl(n)?pl(Mn(e),n):function(i){var a=zs(i,e);return a===r&&a===n?Xs(i,e):gi(n,a,V|he)}}function nr(e,n,i,a,u){e!==n&&vs(n,function(f,d){if(u||(u=new Sn),Ie(f))Hh(e,n,d,i,nr,a,u);else{var p=a?a(Ws(e,d),f,d+"",e,n,u):r;p===r&&(p=f),gs(e,d,p)}},Je)}function Hh(e,n,i,a,u,f,d){var p=Ws(e,i),v=Ws(n,i),S=d.get(v);if(S){gs(e,i,S);return}var N=f?f(p,v,i+"",e,n,d):r,D=N===r;if(D){var L=J(v),M=!L&&pt(v),j=!L&&!M&&qt(v);N=v,L||M||j?J(p)?N=p:we(p)?N=qe(p):M?(D=!1,N=qo(v,!0)):j?(D=!1,N=Qo(v,!0)):N=[]:Ii(v)||Ot(v)?(N=p,Ot(p)?N=$l(p):(!Ie(p)||Kn(p))&&(N=hl(v))):D=!1}D&&(d.set(v,N),u(N,v,a,f,d),d.delete(v)),gs(e,i,N)}function Po(e,n){var i=e.length;if(i)return n+=n<0?i:0,Xn(n,i)?e[n]:r}function Uo(e,n,i){n.length?n=ve(n,function(f){return J(f)?function(d){return St(d,f.length===1?f[0]:f)}:f}):n=[ze];var a=-1;n=ve(n,en($()));var u=ko(e,function(f,d,p){var v=ve(n,function(S){return S(f)});return{criteria:v,index:++a,value:f}});return Tf(u,function(f,d){return sd(f,d,i)})}function qh(e,n){return Bo(e,n,function(i,a){return Xs(e,a)})}function Bo(e,n,i){for(var a=-1,u=n.length,f={};++a<u;){var d=n[a],p=St(e,d);i(p,d)&&mi(f,ht(d,e),p)}return f}function Qh(e){return function(n){return St(n,e)}}function _s(e,n,i,a){var u=a?yf:Pt,f=-1,d=n.length,p=e;for(e===n&&(n=qe(n)),i&&(p=ve(e,en(i)));++f<d;)for(var v=0,S=n[f],N=i?i(S):S;(v=u(p,N,v,a))>-1;)p!==e&&ji.call(p,v,1),ji.call(e,v,1);return e}function Mo(e,n){for(var i=e?n.length:0,a=i-1;i--;){var u=n[i];if(i==a||u!==f){var f=u;Xn(u)?ji.call(e,u,1):Os(e,u)}}return e}function Ss(e,n){return e+Qi(To()*(n-e+1))}function Jh(e,n,i,a){for(var u=-1,f=Re(qi((n-e)/(i||1)),0),d=E(f);f--;)d[a?f:++u]=e,e+=i;return d}function Ns(e,n){var i="";if(!e||n<1||n>En)return i;do n%2&&(i+=e),n=Qi(n/2),n&&(e+=e);while(n);return i}function Z(e,n){return Ys(gl(e,n,ze),e+"")}function zh(e){return bo(Qt(e))}function Xh(e,n){var i=Qt(e);return fr(i,_t(n,0,i.length))}function mi(e,n,i,a){if(!Ie(e))return e;n=ht(n,e);for(var u=-1,f=n.length,d=f-1,p=e;p!=null&&++u<f;){var v=Mn(n[u]),S=i;if(v==="__proto__"||v==="constructor"||v==="prototype")return e;if(u!=d){var N=p[v];S=a?a(N,v,p):r,S===r&&(S=Ie(N)?N:Xn(n[u+1])?[]:{})}hi(p,v,S),p=p[v]}return e}var Go=Ji?function(e,n){return Ji.set(e,n),e}:ze,Kh=Hi?function(e,n){return Hi(e,"toString",{configurable:!0,enumerable:!1,value:Zs(n),writable:!0})}:ze;function Zh(e){return fr(Qt(e))}function pn(e,n,i){var a=-1,u=e.length;n<0&&(n=-n>u?0:u+n),i=i>u?u:i,i<0&&(i+=u),u=n>i?0:i-n>>>0,n>>>=0;for(var f=E(u);++a<u;)f[a]=e[a+n];return f}function ed(e,n){var i;return ct(e,function(a,u,f){return i=n(a,u,f),!i}),!!i}function tr(e,n,i){var a=0,u=e==null?a:e.length;if(typeof n=="number"&&n===n&&u<=P){for(;a<u;){var f=a+u>>>1,d=e[f];d!==null&&!tn(d)&&(i?d<=n:d<n)?a=f+1:u=f}return u}return As(e,n,ze,i)}function As(e,n,i,a){var u=0,f=e==null?0:e.length;if(f===0)return 0;n=i(n);for(var d=n!==n,p=n===null,v=tn(n),S=n===r;u<f;){var N=Qi((u+f)/2),D=i(e[N]),L=D!==r,M=D===null,j=D===D,X=tn(D);if(d)var H=a||j;else S?H=j&&(a||L):p?H=j&&L&&(a||!M):v?H=j&&L&&!M&&(a||!X):M||X?H=!1:H=a?D<=n:D<n;H?u=N+1:f=N}return Ue(f,w)}function Vo(e,n){for(var i=-1,a=e.length,u=0,f=[];++i<a;){var d=e[i],p=n?n(d):d;if(!i||!Nn(p,v)){var v=p;f[u++]=d===0?0:d}}return f}function Wo(e){return typeof e=="number"?e:tn(e)?Tt:+e}function nn(e){if(typeof e=="string")return e;if(J(e))return ve(e,nn)+"";if(tn(e))return Io?Io.call(e):"";var n=e+"";return n=="0"&&1/e==-Pn?"-0":n}function ft(e,n,i){var a=-1,u=Fi,f=e.length,d=!0,p=[],v=p;if(i)d=!1,u=ts;else if(f>=l){var S=n?null:fd(e);if(S)return Ui(S);d=!1,u=ai,v=new wt}else v=n?[]:p;e:for(;++a<f;){var N=e[a],D=n?n(N):N;if(N=i||N!==0?N:0,d&&D===D){for(var L=v.length;L--;)if(v[L]===D)continue e;n&&v.push(D),p.push(N)}else u(v,D,i)||(v!==p&&v.push(D),p.push(N))}return p}function Os(e,n){return n=ht(n,e),e=ml(e,n),e==null||delete e[Mn(gn(n))]}function Yo(e,n,i,a){return mi(e,n,i(St(e,n)),a)}function ir(e,n,i,a){for(var u=e.length,f=a?u:-1;(a?f--:++f<u)&&n(e[f],f,e););return i?pn(e,a?0:f,a?f+1:u):pn(e,a?f+1:0,a?u:f)}function $o(e,n){var i=e;return i instanceof te&&(i=i.value()),is(n,function(a,u){return u.func.apply(u.thisArg,ot([a],u.args))},i)}function Rs(e,n,i){var a=e.length;if(a<2)return a?ft(e[0]):[];for(var u=-1,f=E(a);++u<a;)for(var d=e[u],p=-1;++p<a;)p!=u&&(f[u]=di(f[u]||d,e[p],n,i));return ft(Ce(f,1),n,i)}function jo(e,n,i){for(var a=-1,u=e.length,f=n.length,d={};++a<u;){var p=a<f?n[a]:r;i(d,e[a],p)}return d}function Ds(e){return we(e)?e:[]}function xs(e){return typeof e=="function"?e:ze}function ht(e,n){return J(e)?e:Gs(e,n)?[e]:Il(ce(e))}var nd=Z;function dt(e,n,i){var a=e.length;return i=i===r?a:i,!n&&i>=a?e:pn(e,n,i)}var Ho=Wf||function(e){return ke.clearTimeout(e)};function qo(e,n){if(n)return e.slice();var i=e.length,a=po?po(i):new e.constructor(i);return e.copy(a),a}function Ls(e){var n=new e.constructor(e.byteLength);return new Yi(n).set(new Yi(e)),n}function td(e,n){var i=n?Ls(e.buffer):e.buffer;return new e.constructor(i,e.byteOffset,e.byteLength)}function id(e){var n=new e.constructor(e.source,Ra.exec(e));return n.lastIndex=e.lastIndex,n}function rd(e){return fi?de(fi.call(e)):{}}function Qo(e,n){var i=n?Ls(e.buffer):e.buffer;return new e.constructor(i,e.byteOffset,e.length)}function Jo(e,n){if(e!==n){var i=e!==r,a=e===null,u=e===e,f=tn(e),d=n!==r,p=n===null,v=n===n,S=tn(n);if(!p&&!S&&!f&&e>n||f&&d&&v&&!p&&!S||a&&d&&v||!i&&v||!u)return 1;if(!a&&!f&&!S&&e<n||S&&i&&u&&!a&&!f||p&&i&&u||!d&&u||!v)return-1}return 0}function sd(e,n,i){for(var a=-1,u=e.criteria,f=n.criteria,d=u.length,p=i.length;++a<d;){var v=Jo(u[a],f[a]);if(v){if(a>=p)return v;var S=i[a];return v*(S=="desc"?-1:1)}}return e.index-n.index}function zo(e,n,i,a){for(var u=-1,f=e.length,d=i.length,p=-1,v=n.length,S=Re(f-d,0),N=E(v+S),D=!a;++p<v;)N[p]=n[p];for(;++u<d;)(D||u<f)&&(N[i[u]]=e[u]);for(;S--;)N[p++]=e[u++];return N}function Xo(e,n,i,a){for(var u=-1,f=e.length,d=-1,p=i.length,v=-1,S=n.length,N=Re(f-p,0),D=E(N+S),L=!a;++u<N;)D[u]=e[u];for(var M=u;++v<S;)D[M+v]=n[v];for(;++d<p;)(L||u<f)&&(D[M+i[d]]=e[u++]);return D}function qe(e,n){var i=-1,a=e.length;for(n||(n=E(a));++i<a;)n[i]=e[i];return n}function Bn(e,n,i,a){var u=!i;i||(i={});for(var f=-1,d=n.length;++f<d;){var p=n[f],v=a?a(i[p],e[p],p,i,e):r;v===r&&(v=e[p]),u?Qn(i,p,v):hi(i,p,v)}return i}function ad(e,n){return Bn(e,Ms(e),n)}function od(e,n){return Bn(e,cl(e),n)}function rr(e,n){return function(i,a){var u=J(i)?hf:Rh,f=n?n():{};return u(i,e,$(a,2),f)}}function $t(e){return Z(function(n,i){var a=-1,u=i.length,f=u>1?i[u-1]:r,d=u>2?i[2]:r;for(f=e.length>3&&typeof f=="function"?(u--,f):r,d&&We(i[0],i[1],d)&&(f=u<3?r:f,u=1),n=de(n);++a<u;){var p=i[a];p&&e(n,p,a,f)}return n})}function Ko(e,n){return function(i,a){if(i==null)return i;if(!Qe(i))return e(i,a);for(var u=i.length,f=n?u:-1,d=de(i);(n?f--:++f<u)&&a(d[f],f,d)!==!1;);return i}}function Zo(e){return function(n,i,a){for(var u=-1,f=de(n),d=a(n),p=d.length;p--;){var v=d[e?p:++u];if(i(f[v],v,f)===!1)break}return n}}function ld(e,n,i){var a=n&se,u=vi(e);function f(){var d=this&&this!==ke&&this instanceof f?u:e;return d.apply(a?i:this,arguments)}return f}function el(e){return function(n){n=ce(n);var i=Ut(n)?_n(n):r,a=i?i[0]:n.charAt(0),u=i?dt(i,1).join(""):n.slice(1);return a[e]()+u}}function jt(e){return function(n){return is(Kl(Xl(n).replace(Xc,"")),e,"")}}function vi(e){return function(){var n=arguments;switch(n.length){case 0:return new e;case 1:return new e(n[0]);case 2:return new e(n[0],n[1]);case 3:return new e(n[0],n[1],n[2]);case 4:return new e(n[0],n[1],n[2],n[3]);case 5:return new e(n[0],n[1],n[2],n[3],n[4]);case 6:return new e(n[0],n[1],n[2],n[3],n[4],n[5]);case 7:return new e(n[0],n[1],n[2],n[3],n[4],n[5],n[6])}var i=Yt(e.prototype),a=e.apply(i,n);return Ie(a)?a:i}}function ud(e,n,i){var a=vi(e);function u(){for(var f=arguments.length,d=E(f),p=f,v=Ht(u);p--;)d[p]=arguments[p];var S=f<3&&d[0]!==v&&d[f-1]!==v?[]:lt(d,v);if(f-=S.length,f<i)return sl(e,n,sr,u.placeholder,r,d,S,r,r,i-f);var N=this&&this!==ke&&this instanceof u?a:e;return Ze(N,this,d)}return u}function nl(e){return function(n,i,a){var u=de(n);if(!Qe(n)){var f=$(i,3);n=xe(n),i=function(p){return f(u[p],p,u)}}var d=e(n,i,a);return d>-1?u[f?n[d]:d]:r}}function tl(e){return zn(function(n){var i=n.length,a=i,u=hn.prototype.thru;for(e&&n.reverse();a--;){var f=n[a];if(typeof f!="function")throw new fn(g);if(u&&!d&&ur(f)=="wrapper")var d=new hn([],!0)}for(a=d?a:i;++a<i;){f=n[a];var p=ur(f),v=p=="wrapper"?Us(f):r;v&&Vs(v[0])&&v[1]==(Fe|Te|De|$n)&&!v[4].length&&v[9]==1?d=d[ur(v[0])].apply(d,v[3]):d=f.length==1&&Vs(f)?d[p]():d.thru(f)}return function(){var S=arguments,N=S[0];if(d&&S.length==1&&J(N))return d.plant(N).value();for(var D=0,L=i?n[D].apply(this,S):N;++D<i;)L=n[D].call(this,L);return L}})}function sr(e,n,i,a,u,f,d,p,v,S){var N=n&Fe,D=n&se,L=n&ye,M=n&(Te|Ne),j=n&R,X=L?r:vi(e);function H(){for(var ee=arguments.length,re=E(ee),rn=ee;rn--;)re[rn]=arguments[rn];if(M)var Ye=Ht(H),sn=Ef(re,Ye);if(a&&(re=zo(re,a,u,M)),f&&(re=Xo(re,f,d,M)),ee-=sn,M&&ee<S){var _e=lt(re,Ye);return sl(e,n,sr,H.placeholder,i,re,_e,p,v,S-ee)}var An=D?i:this,et=L?An[e]:e;return ee=re.length,p?re=Rd(re,p):j&&ee>1&&re.reverse(),N&&v<ee&&(re.length=v),this&&this!==ke&&this instanceof H&&(et=X||vi(et)),et.apply(An,re)}return H}function il(e,n){return function(i,a){return Uh(i,e,n(a),{})}}function ar(e,n){return function(i,a){var u;if(i===r&&a===r)return n;if(i!==r&&(u=i),a!==r){if(u===r)return a;typeof i=="string"||typeof a=="string"?(i=nn(i),a=nn(a)):(i=Wo(i),a=Wo(a)),u=e(i,a)}return u}}function ks(e){return zn(function(n){return n=ve(n,en($())),Z(function(i){var a=this;return e(n,function(u){return Ze(u,a,i)})})})}function or(e,n){n=n===r?" ":nn(n);var i=n.length;if(i<2)return i?Ns(n,e):n;var a=Ns(n,qi(e/Bt(n)));return Ut(n)?dt(_n(a),0,e).join(""):a.slice(0,e)}function cd(e,n,i,a){var u=n&se,f=vi(e);function d(){for(var p=-1,v=arguments.length,S=-1,N=a.length,D=E(N+v),L=this&&this!==ke&&this instanceof d?f:e;++S<N;)D[S]=a[S];for(;v--;)D[S++]=arguments[++p];return Ze(L,u?i:this,D)}return d}function rl(e){return function(n,i,a){return a&&typeof a!="number"&&We(n,i,a)&&(i=a=r),n=Zn(n),i===r?(i=n,n=0):i=Zn(i),a=a===r?n<i?1:-1:Zn(a),Jh(n,i,a,e)}}function lr(e){return function(n,i){return typeof n=="string"&&typeof i=="string"||(n=mn(n),i=mn(i)),e(n,i)}}function sl(e,n,i,a,u,f,d,p,v,S){var N=n&Te,D=N?d:r,L=N?r:d,M=N?f:r,j=N?r:f;n|=N?De:Me,n&=~(N?Me:De),n&Yn||(n&=~(se|ye));var X=[e,n,u,M,D,j,L,p,v,S],H=i.apply(r,X);return Vs(e)&&vl(H,X),H.placeholder=a,yl(H,e,n)}function Cs(e){var n=Oe[e];return function(i,a){if(i=mn(i),a=a==null?0:Ue(z(a),292),a&&yo(i)){var u=(ce(i)+"e").split("e"),f=n(u[0]+"e"+(+u[1]+a));return u=(ce(f)+"e").split("e"),+(u[0]+"e"+(+u[1]-a))}return n(i)}}var fd=Vt&&1/Ui(new Vt([,-0]))[1]==Pn?function(e){return new Vt(e)}:ta;function al(e){return function(n){var i=Be(n);return i==bn?cs(n):i==wn?Of(n):If(n,e(n))}}function Jn(e,n,i,a,u,f,d,p){var v=n&ye;if(!v&&typeof e!="function")throw new fn(g);var S=a?a.length:0;if(S||(n&=~(De|Me),a=u=r),d=d===r?d:Re(z(d),0),p=p===r?p:z(p),S-=u?u.length:0,n&Me){var N=a,D=u;a=u=r}var L=v?r:Us(e),M=[e,n,i,a,u,N,D,f,d,p];if(L&&Nd(M,L),e=M[0],n=M[1],i=M[2],a=M[3],u=M[4],p=M[9]=M[9]===r?v?0:e.length:Re(M[9]-S,0),!p&&n&(Te|Ne)&&(n&=~(Te|Ne)),!n||n==se)var j=ld(e,n,i);else n==Te||n==Ne?j=ud(e,n,p):(n==De||n==(se|De))&&!u.length?j=cd(e,n,i,a):j=sr.apply(r,M);var X=L?Go:vl;return yl(X(j,M),e,n)}function ol(e,n,i,a){return e===r||Nn(e,Gt[i])&&!fe.call(a,i)?n:e}function ll(e,n,i,a,u,f){return Ie(e)&&Ie(n)&&(f.set(n,e),nr(e,n,r,ll,f),f.delete(n)),e}function hd(e){return Ii(e)?r:e}function ul(e,n,i,a,u,f){var d=i&V,p=e.length,v=n.length;if(p!=v&&!(d&&v>p))return!1;var S=f.get(e),N=f.get(n);if(S&&N)return S==n&&N==e;var D=-1,L=!0,M=i&he?new wt:r;for(f.set(e,n),f.set(n,e);++D<p;){var j=e[D],X=n[D];if(a)var H=d?a(X,j,D,n,e,f):a(j,X,D,e,n,f);if(H!==r){if(H)continue;L=!1;break}if(M){if(!rs(n,function(ee,re){if(!ai(M,re)&&(j===ee||u(j,ee,i,a,f)))return M.push(re)})){L=!1;break}}else if(!(j===X||u(j,X,i,a,f))){L=!1;break}}return f.delete(e),f.delete(n),L}function dd(e,n,i,a,u,f,d){switch(i){case Ct:if(e.byteLength!=n.byteLength||e.byteOffset!=n.byteOffset)return!1;e=e.buffer,n=n.buffer;case si:return!(e.byteLength!=n.byteLength||!f(new Yi(e),new Yi(n)));case ue:case ln:case ni:return Nn(+e,+n);case st:return e.name==n.name&&e.message==n.message;case ti:case ii:return e==n+"";case bn:var p=cs;case wn:var v=a&V;if(p||(p=Ui),e.size!=n.size&&!v)return!1;var S=d.get(e);if(S)return S==n;a|=he,d.set(e,n);var N=ul(p(e),p(n),a,u,f,d);return d.delete(e),N;case xi:if(fi)return fi.call(e)==fi.call(n)}return!1}function pd(e,n,i,a,u,f){var d=i&V,p=Fs(e),v=p.length,S=Fs(n),N=S.length;if(v!=N&&!d)return!1;for(var D=v;D--;){var L=p[D];if(!(d?L in n:fe.call(n,L)))return!1}var M=f.get(e),j=f.get(n);if(M&&j)return M==n&&j==e;var X=!0;f.set(e,n),f.set(n,e);for(var H=d;++D<v;){L=p[D];var ee=e[L],re=n[L];if(a)var rn=d?a(re,ee,L,n,e,f):a(ee,re,L,e,n,f);if(!(rn===r?ee===re||u(ee,re,i,a,f):rn)){X=!1;break}H||(H=L=="constructor")}if(X&&!H){var Ye=e.constructor,sn=n.constructor;Ye!=sn&&"constructor"in e&&"constructor"in n&&!(typeof Ye=="function"&&Ye instanceof Ye&&typeof sn=="function"&&sn instanceof sn)&&(X=!1)}return f.delete(e),f.delete(n),X}function zn(e){return Ys(gl(e,r,_l),e+"")}function Fs(e){return Ro(e,xe,Ms)}function Ps(e){return Ro(e,Je,cl)}var Us=Ji?function(e){return Ji.get(e)}:ta;function ur(e){for(var n=e.name+"",i=Wt[n],a=fe.call(Wt,n)?i.length:0;a--;){var u=i[a],f=u.func;if(f==null||f==e)return u.name}return n}function Ht(e){var n=fe.call(c,"placeholder")?c:e;return n.placeholder}function $(){var e=c.iteratee||ea;return e=e===ea?Lo:e,arguments.length?e(arguments[0],arguments[1]):e}function cr(e,n){var i=e.__data__;return bd(n)?i[typeof n=="string"?"string":"hash"]:i.map}function Bs(e){for(var n=xe(e),i=n.length;i--;){var a=n[i],u=e[a];n[i]=[a,u,dl(u)]}return n}function Nt(e,n){var i=Sf(e,n);return xo(i)?i:r}function gd(e){var n=fe.call(e,Et),i=e[Et];try{e[Et]=r;var a=!0}catch{}var u=Vi.call(e);return a&&(n?e[Et]=i:delete e[Et]),u}var Ms=hs?function(e){return e==null?[]:(e=de(e),at(hs(e),function(n){return mo.call(e,n)}))}:ia,cl=hs?function(e){for(var n=[];e;)ot(n,Ms(e)),e=$i(e);return n}:ia,Be=Ve;(ds&&Be(new ds(new ArrayBuffer(1)))!=Ct||li&&Be(new li)!=bn||ps&&Be(ps.resolve())!=Sa||Vt&&Be(new Vt)!=wn||ui&&Be(new ui)!=ri)&&(Be=function(e){var n=Ve(e),i=n==jn?e.constructor:r,a=i?At(i):"";if(a)switch(a){case Xf:return Ct;case Kf:return bn;case Zf:return Sa;case eh:return wn;case nh:return ri}return n});function md(e,n,i){for(var a=-1,u=i.length;++a<u;){var f=i[a],d=f.size;switch(f.type){case"drop":e+=d;break;case"dropRight":n-=d;break;case"take":n=Ue(n,e+d);break;case"takeRight":e=Re(e,n-d);break}}return{start:e,end:n}}function vd(e){var n=e.match(bc);return n?n[1].split(wc):[]}function fl(e,n,i){n=ht(n,e);for(var a=-1,u=n.length,f=!1;++a<u;){var d=Mn(n[a]);if(!(f=e!=null&&i(e,d)))break;e=e[d]}return f||++a!=u?f:(u=e==null?0:e.length,!!u&&vr(u)&&Xn(d,u)&&(J(e)||Ot(e)))}function yd(e){var n=e.length,i=new e.constructor(n);return n&&typeof e[0]=="string"&&fe.call(e,"index")&&(i.index=e.index,i.input=e.input),i}function hl(e){return typeof e.constructor=="function"&&!yi(e)?Yt($i(e)):{}}function Td(e,n,i){var a=e.constructor;switch(n){case si:return Ls(e);case ue:case ln:return new a(+e);case Ct:return td(e,i);case Br:case Mr:case Gr:case Vr:case Wr:case Yr:case $r:case jr:case Hr:return Qo(e,i);case bn:return new a;case ni:case ii:return new a(e);case ti:return id(e);case wn:return new a;case xi:return rd(e)}}function Id(e,n){var i=n.length;if(!i)return e;var a=i-1;return n[a]=(i>1?"& ":"")+n[a],n=n.join(i>2?", ":" "),e.replace(Ec,`{
/* [wrapped with `+n+`] */
`)}function Ed(e){return J(e)||Ot(e)||!!(vo&&e&&e[vo])}function Xn(e,n){var i=typeof e;return n=n??En,!!n&&(i=="number"||i!="symbol"&&Lc.test(e))&&e>-1&&e%1==0&&e<n}function We(e,n,i){if(!Ie(i))return!1;var a=typeof n;return(a=="number"?Qe(i)&&Xn(n,i.length):a=="string"&&n in i)?Nn(i[n],e):!1}function Gs(e,n){if(J(e))return!1;var i=typeof e;return i=="number"||i=="symbol"||i=="boolean"||e==null||tn(e)?!0:vc.test(e)||!mc.test(e)||n!=null&&e in de(n)}function bd(e){var n=typeof e;return n=="string"||n=="number"||n=="symbol"||n=="boolean"?e!=="__proto__":e===null}function Vs(e){var n=ur(e),i=c[n];if(typeof i!="function"||!(n in te.prototype))return!1;if(e===i)return!0;var a=Us(i);return!!a&&e===a[0]}function wd(e){return!!ho&&ho in e}var _d=Mi?Kn:ra;function yi(e){var n=e&&e.constructor,i=typeof n=="function"&&n.prototype||Gt;return e===i}function dl(e){return e===e&&!Ie(e)}function pl(e,n){return function(i){return i==null?!1:i[e]===n&&(n!==r||e in de(i))}}function Sd(e){var n=gr(e,function(a){return i.size===O&&i.clear(),a}),i=n.cache;return n}function Nd(e,n){var i=e[1],a=n[1],u=i|a,f=u<(se|ye|Fe),d=a==Fe&&i==Te||a==Fe&&i==$n&&e[7].length<=n[8]||a==(Fe|$n)&&n[7].length<=n[8]&&i==Te;if(!(f||d))return e;a&se&&(e[2]=n[2],u|=i&se?0:Yn);var p=n[3];if(p){var v=e[3];e[3]=v?zo(v,p,n[4]):p,e[4]=v?lt(e[3],_):n[4]}return p=n[5],p&&(v=e[5],e[5]=v?Xo(v,p,n[6]):p,e[6]=v?lt(e[5],_):n[6]),p=n[7],p&&(e[7]=p),a&Fe&&(e[8]=e[8]==null?n[8]:Ue(e[8],n[8])),e[9]==null&&(e[9]=n[9]),e[0]=n[0],e[1]=u,e}function Ad(e){var n=[];if(e!=null)for(var i in de(e))n.push(i);return n}function Od(e){return Vi.call(e)}function gl(e,n,i){return n=Re(n===r?e.length-1:n,0),function(){for(var a=arguments,u=-1,f=Re(a.length-n,0),d=E(f);++u<f;)d[u]=a[n+u];u=-1;for(var p=E(n+1);++u<n;)p[u]=a[u];return p[n]=i(d),Ze(e,this,p)}}function ml(e,n){return n.length<2?e:St(e,pn(n,0,-1))}function Rd(e,n){for(var i=e.length,a=Ue(n.length,i),u=qe(e);a--;){var f=n[a];e[a]=Xn(f,i)?u[f]:r}return e}function Ws(e,n){if(!(n==="constructor"&&typeof e[n]=="function")&&n!="__proto__")return e[n]}var vl=Tl(Go),Ti=$f||function(e,n){return ke.setTimeout(e,n)},Ys=Tl(Kh);function yl(e,n,i){var a=n+"";return Ys(e,Id(a,Dd(vd(a),i)))}function Tl(e){var n=0,i=0;return function(){var a=Qf(),u=yt-(a-i);if(i=a,u>0){if(++n>=rt)return arguments[0]}else n=0;return e.apply(r,arguments)}}function fr(e,n){var i=-1,a=e.length,u=a-1;for(n=n===r?a:n;++i<n;){var f=Ss(i,u),d=e[f];e[f]=e[i],e[i]=d}return e.length=n,e}var Il=Sd(function(e){var n=[];return e.charCodeAt(0)===46&&n.push(""),e.replace(yc,function(i,a,u,f){n.push(u?f.replace(Nc,"$1"):a||i)}),n});function Mn(e){if(typeof e=="string"||tn(e))return e;var n=e+"";return n=="0"&&1/e==-Pn?"-0":n}function At(e){if(e!=null){try{return Gi.call(e)}catch{}try{return e+""}catch{}}return""}function Dd(e,n){return cn(k,function(i){var a="_."+i[0];n&i[1]&&!Fi(e,a)&&e.push(a)}),e.sort()}function El(e){if(e instanceof te)return e.clone();var n=new hn(e.__wrapped__,e.__chain__);return n.__actions__=qe(e.__actions__),n.__index__=e.__index__,n.__values__=e.__values__,n}function xd(e,n,i){(i?We(e,n,i):n===r)?n=1:n=Re(z(n),0);var a=e==null?0:e.length;if(!a||n<1)return[];for(var u=0,f=0,d=E(qi(a/n));u<a;)d[f++]=pn(e,u,u+=n);return d}function Ld(e){for(var n=-1,i=e==null?0:e.length,a=0,u=[];++n<i;){var f=e[n];f&&(u[a++]=f)}return u}function kd(){var e=arguments.length;if(!e)return[];for(var n=E(e-1),i=arguments[0],a=e;a--;)n[a-1]=arguments[a];return ot(J(i)?qe(i):[i],Ce(n,1))}var Cd=Z(function(e,n){return we(e)?di(e,Ce(n,1,we,!0)):[]}),Fd=Z(function(e,n){var i=gn(n);return we(i)&&(i=r),we(e)?di(e,Ce(n,1,we,!0),$(i,2)):[]}),Pd=Z(function(e,n){var i=gn(n);return we(i)&&(i=r),we(e)?di(e,Ce(n,1,we,!0),r,i):[]});function Ud(e,n,i){var a=e==null?0:e.length;return a?(n=i||n===r?1:z(n),pn(e,n<0?0:n,a)):[]}function Bd(e,n,i){var a=e==null?0:e.length;return a?(n=i||n===r?1:z(n),n=a-n,pn(e,0,n<0?0:n)):[]}function Md(e,n){return e&&e.length?ir(e,$(n,3),!0,!0):[]}function Gd(e,n){return e&&e.length?ir(e,$(n,3),!0):[]}function Vd(e,n,i,a){var u=e==null?0:e.length;return u?(i&&typeof i!="number"&&We(e,n,i)&&(i=0,a=u),kh(e,n,i,a)):[]}function bl(e,n,i){var a=e==null?0:e.length;if(!a)return-1;var u=i==null?0:z(i);return u<0&&(u=Re(a+u,0)),Pi(e,$(n,3),u)}function wl(e,n,i){var a=e==null?0:e.length;if(!a)return-1;var u=a-1;return i!==r&&(u=z(i),u=i<0?Re(a+u,0):Ue(u,a-1)),Pi(e,$(n,3),u,!0)}function _l(e){var n=e==null?0:e.length;return n?Ce(e,1):[]}function Wd(e){var n=e==null?0:e.length;return n?Ce(e,Pn):[]}function Yd(e,n){var i=e==null?0:e.length;return i?(n=n===r?1:z(n),Ce(e,n)):[]}function $d(e){for(var n=-1,i=e==null?0:e.length,a={};++n<i;){var u=e[n];a[u[0]]=u[1]}return a}function Sl(e){return e&&e.length?e[0]:r}function jd(e,n,i){var a=e==null?0:e.length;if(!a)return-1;var u=i==null?0:z(i);return u<0&&(u=Re(a+u,0)),Pt(e,n,u)}function Hd(e){var n=e==null?0:e.length;return n?pn(e,0,-1):[]}var qd=Z(function(e){var n=ve(e,Ds);return n.length&&n[0]===e[0]?Is(n):[]}),Qd=Z(function(e){var n=gn(e),i=ve(e,Ds);return n===gn(i)?n=r:i.pop(),i.length&&i[0]===e[0]?Is(i,$(n,2)):[]}),Jd=Z(function(e){var n=gn(e),i=ve(e,Ds);return n=typeof n=="function"?n:r,n&&i.pop(),i.length&&i[0]===e[0]?Is(i,r,n):[]});function zd(e,n){return e==null?"":Hf.call(e,n)}function gn(e){var n=e==null?0:e.length;return n?e[n-1]:r}function Xd(e,n,i){var a=e==null?0:e.length;if(!a)return-1;var u=a;return i!==r&&(u=z(i),u=u<0?Re(a+u,0):Ue(u,a-1)),n===n?Df(e,n,u):Pi(e,ro,u,!0)}function Kd(e,n){return e&&e.length?Po(e,z(n)):r}var Zd=Z(Nl);function Nl(e,n){return e&&e.length&&n&&n.length?_s(e,n):e}function ep(e,n,i){return e&&e.length&&n&&n.length?_s(e,n,$(i,2)):e}function np(e,n,i){return e&&e.length&&n&&n.length?_s(e,n,r,i):e}var tp=zn(function(e,n){var i=e==null?0:e.length,a=ms(e,n);return Mo(e,ve(n,function(u){return Xn(u,i)?+u:u}).sort(Jo)),a});function ip(e,n){var i=[];if(!(e&&e.length))return i;var a=-1,u=[],f=e.length;for(n=$(n,3);++a<f;){var d=e[a];n(d,a,e)&&(i.push(d),u.push(a))}return Mo(e,u),i}function $s(e){return e==null?e:zf.call(e)}function rp(e,n,i){var a=e==null?0:e.length;return a?(i&&typeof i!="number"&&We(e,n,i)?(n=0,i=a):(n=n==null?0:z(n),i=i===r?a:z(i)),pn(e,n,i)):[]}function sp(e,n){return tr(e,n)}function ap(e,n,i){return As(e,n,$(i,2))}function op(e,n){var i=e==null?0:e.length;if(i){var a=tr(e,n);if(a<i&&Nn(e[a],n))return a}return-1}function lp(e,n){return tr(e,n,!0)}function up(e,n,i){return As(e,n,$(i,2),!0)}function cp(e,n){var i=e==null?0:e.length;if(i){var a=tr(e,n,!0)-1;if(Nn(e[a],n))return a}return-1}function fp(e){return e&&e.length?Vo(e):[]}function hp(e,n){return e&&e.length?Vo(e,$(n,2)):[]}function dp(e){var n=e==null?0:e.length;return n?pn(e,1,n):[]}function pp(e,n,i){return e&&e.length?(n=i||n===r?1:z(n),pn(e,0,n<0?0:n)):[]}function gp(e,n,i){var a=e==null?0:e.length;return a?(n=i||n===r?1:z(n),n=a-n,pn(e,n<0?0:n,a)):[]}function mp(e,n){return e&&e.length?ir(e,$(n,3),!1,!0):[]}function vp(e,n){return e&&e.length?ir(e,$(n,3)):[]}var yp=Z(function(e){return ft(Ce(e,1,we,!0))}),Tp=Z(function(e){var n=gn(e);return we(n)&&(n=r),ft(Ce(e,1,we,!0),$(n,2))}),Ip=Z(function(e){var n=gn(e);return n=typeof n=="function"?n:r,ft(Ce(e,1,we,!0),r,n)});function Ep(e){return e&&e.length?ft(e):[]}function bp(e,n){return e&&e.length?ft(e,$(n,2)):[]}function wp(e,n){return n=typeof n=="function"?n:r,e&&e.length?ft(e,r,n):[]}function js(e){if(!(e&&e.length))return[];var n=0;return e=at(e,function(i){if(we(i))return n=Re(i.length,n),!0}),ls(n,function(i){return ve(e,ss(i))})}function Al(e,n){if(!(e&&e.length))return[];var i=js(e);return n==null?i:ve(i,function(a){return Ze(n,r,a)})}var _p=Z(function(e,n){return we(e)?di(e,n):[]}),Sp=Z(function(e){return Rs(at(e,we))}),Np=Z(function(e){var n=gn(e);return we(n)&&(n=r),Rs(at(e,we),$(n,2))}),Ap=Z(function(e){var n=gn(e);return n=typeof n=="function"?n:r,Rs(at(e,we),r,n)}),Op=Z(js);function Rp(e,n){return jo(e||[],n||[],hi)}function Dp(e,n){return jo(e||[],n||[],mi)}var xp=Z(function(e){var n=e.length,i=n>1?e[n-1]:r;return i=typeof i=="function"?(e.pop(),i):r,Al(e,i)});function Ol(e){var n=c(e);return n.__chain__=!0,n}function Lp(e,n){return n(e),e}function hr(e,n){return n(e)}var kp=zn(function(e){var n=e.length,i=n?e[0]:0,a=this.__wrapped__,u=function(f){return ms(f,e)};return n>1||this.__actions__.length||!(a instanceof te)||!Xn(i)?this.thru(u):(a=a.slice(i,+i+(n?1:0)),a.__actions__.push({func:hr,args:[u],thisArg:r}),new hn(a,this.__chain__).thru(function(f){return n&&!f.length&&f.push(r),f}))});function Cp(){return Ol(this)}function Fp(){return new hn(this.value(),this.__chain__)}function Pp(){this.__values__===r&&(this.__values__=Wl(this.value()));var e=this.__index__>=this.__values__.length,n=e?r:this.__values__[this.__index__++];return{done:e,value:n}}function Up(){return this}function Bp(e){for(var n,i=this;i instanceof Xi;){var a=El(i);a.__index__=0,a.__values__=r,n?u.__wrapped__=a:n=a;var u=a;i=i.__wrapped__}return u.__wrapped__=e,n}function Mp(){var e=this.__wrapped__;if(e instanceof te){var n=e;return this.__actions__.length&&(n=new te(this)),n=n.reverse(),n.__actions__.push({func:hr,args:[$s],thisArg:r}),new hn(n,this.__chain__)}return this.thru($s)}function Gp(){return $o(this.__wrapped__,this.__actions__)}var Vp=rr(function(e,n,i){fe.call(e,i)?++e[i]:Qn(e,i,1)});function Wp(e,n,i){var a=J(e)?to:Lh;return i&&We(e,n,i)&&(n=r),a(e,$(n,3))}function Yp(e,n){var i=J(e)?at:Ao;return i(e,$(n,3))}var $p=nl(bl),jp=nl(wl);function Hp(e,n){return Ce(dr(e,n),1)}function qp(e,n){return Ce(dr(e,n),Pn)}function Qp(e,n,i){return i=i===r?1:z(i),Ce(dr(e,n),i)}function Rl(e,n){var i=J(e)?cn:ct;return i(e,$(n,3))}function Dl(e,n){var i=J(e)?df:No;return i(e,$(n,3))}var Jp=rr(function(e,n,i){fe.call(e,i)?e[i].push(n):Qn(e,i,[n])});function zp(e,n,i,a){e=Qe(e)?e:Qt(e),i=i&&!a?z(i):0;var u=e.length;return i<0&&(i=Re(u+i,0)),yr(e)?i<=u&&e.indexOf(n,i)>-1:!!u&&Pt(e,n,i)>-1}var Xp=Z(function(e,n,i){var a=-1,u=typeof n=="function",f=Qe(e)?E(e.length):[];return ct(e,function(d){f[++a]=u?Ze(n,d,i):pi(d,n,i)}),f}),Kp=rr(function(e,n,i){Qn(e,i,n)});function dr(e,n){var i=J(e)?ve:ko;return i(e,$(n,3))}function Zp(e,n,i,a){return e==null?[]:(J(n)||(n=n==null?[]:[n]),i=a?r:i,J(i)||(i=i==null?[]:[i]),Uo(e,n,i))}var eg=rr(function(e,n,i){e[i?0:1].push(n)},function(){return[[],[]]});function ng(e,n,i){var a=J(e)?is:ao,u=arguments.length<3;return a(e,$(n,4),i,u,ct)}function tg(e,n,i){var a=J(e)?pf:ao,u=arguments.length<3;return a(e,$(n,4),i,u,No)}function ig(e,n){var i=J(e)?at:Ao;return i(e,mr($(n,3)))}function rg(e){var n=J(e)?bo:zh;return n(e)}function sg(e,n,i){(i?We(e,n,i):n===r)?n=1:n=z(n);var a=J(e)?Ah:Xh;return a(e,n)}function ag(e){var n=J(e)?Oh:Zh;return n(e)}function og(e){if(e==null)return 0;if(Qe(e))return yr(e)?Bt(e):e.length;var n=Be(e);return n==bn||n==wn?e.size:bs(e).length}function lg(e,n,i){var a=J(e)?rs:ed;return i&&We(e,n,i)&&(n=r),a(e,$(n,3))}var ug=Z(function(e,n){if(e==null)return[];var i=n.length;return i>1&&We(e,n[0],n[1])?n=[]:i>2&&We(n[0],n[1],n[2])&&(n=[n[0]]),Uo(e,Ce(n,1),[])}),pr=Yf||function(){return ke.Date.now()};function cg(e,n){if(typeof n!="function")throw new fn(g);return e=z(e),function(){if(--e<1)return n.apply(this,arguments)}}function xl(e,n,i){return n=i?r:n,n=e&&n==null?e.length:n,Jn(e,Fe,r,r,r,r,n)}function Ll(e,n){var i;if(typeof n!="function")throw new fn(g);return e=z(e),function(){return--e>0&&(i=n.apply(this,arguments)),e<=1&&(n=r),i}}var Hs=Z(function(e,n,i){var a=se;if(i.length){var u=lt(i,Ht(Hs));a|=De}return Jn(e,a,n,i,u)}),kl=Z(function(e,n,i){var a=se|ye;if(i.length){var u=lt(i,Ht(kl));a|=De}return Jn(n,a,e,i,u)});function Cl(e,n,i){n=i?r:n;var a=Jn(e,Te,r,r,r,r,r,n);return a.placeholder=Cl.placeholder,a}function Fl(e,n,i){n=i?r:n;var a=Jn(e,Ne,r,r,r,r,r,n);return a.placeholder=Fl.placeholder,a}function Pl(e,n,i){var a,u,f,d,p,v,S=0,N=!1,D=!1,L=!0;if(typeof e!="function")throw new fn(g);n=mn(n)||0,Ie(i)&&(N=!!i.leading,D="maxWait"in i,f=D?Re(mn(i.maxWait)||0,n):f,L="trailing"in i?!!i.trailing:L);function M(_e){var An=a,et=u;return a=u=r,S=_e,d=e.apply(et,An),d}function j(_e){return S=_e,p=Ti(ee,n),N?M(_e):d}function X(_e){var An=_e-v,et=_e-S,nu=n-An;return D?Ue(nu,f-et):nu}function H(_e){var An=_e-v,et=_e-S;return v===r||An>=n||An<0||D&&et>=f}function ee(){var _e=pr();if(H(_e))return re(_e);p=Ti(ee,X(_e))}function re(_e){return p=r,L&&a?M(_e):(a=u=r,d)}function rn(){p!==r&&Ho(p),S=0,a=v=u=p=r}function Ye(){return p===r?d:re(pr())}function sn(){var _e=pr(),An=H(_e);if(a=arguments,u=this,v=_e,An){if(p===r)return j(v);if(D)return Ho(p),p=Ti(ee,n),M(v)}return p===r&&(p=Ti(ee,n)),d}return sn.cancel=rn,sn.flush=Ye,sn}var fg=Z(function(e,n){return So(e,1,n)}),hg=Z(function(e,n,i){return So(e,mn(n)||0,i)});function dg(e){return Jn(e,R)}function gr(e,n){if(typeof e!="function"||n!=null&&typeof n!="function")throw new fn(g);var i=function(){var a=arguments,u=n?n.apply(this,a):a[0],f=i.cache;if(f.has(u))return f.get(u);var d=e.apply(this,a);return i.cache=f.set(u,d)||f,d};return i.cache=new(gr.Cache||qn),i}gr.Cache=qn;function mr(e){if(typeof e!="function")throw new fn(g);return function(){var n=arguments;switch(n.length){case 0:return!e.call(this);case 1:return!e.call(this,n[0]);case 2:return!e.call(this,n[0],n[1]);case 3:return!e.call(this,n[0],n[1],n[2])}return!e.apply(this,n)}}function pg(e){return Ll(2,e)}var gg=nd(function(e,n){n=n.length==1&&J(n[0])?ve(n[0],en($())):ve(Ce(n,1),en($()));var i=n.length;return Z(function(a){for(var u=-1,f=Ue(a.length,i);++u<f;)a[u]=n[u].call(this,a[u]);return Ze(e,this,a)})}),qs=Z(function(e,n){var i=lt(n,Ht(qs));return Jn(e,De,r,n,i)}),Ul=Z(function(e,n){var i=lt(n,Ht(Ul));return Jn(e,Me,r,n,i)}),mg=zn(function(e,n){return Jn(e,$n,r,r,r,n)});function vg(e,n){if(typeof e!="function")throw new fn(g);return n=n===r?n:z(n),Z(e,n)}function yg(e,n){if(typeof e!="function")throw new fn(g);return n=n==null?0:Re(z(n),0),Z(function(i){var a=i[n],u=dt(i,0,n);return a&&ot(u,a),Ze(e,this,u)})}function Tg(e,n,i){var a=!0,u=!0;if(typeof e!="function")throw new fn(g);return Ie(i)&&(a="leading"in i?!!i.leading:a,u="trailing"in i?!!i.trailing:u),Pl(e,n,{leading:a,maxWait:n,trailing:u})}function Ig(e){return xl(e,1)}function Eg(e,n){return qs(xs(n),e)}function bg(){if(!arguments.length)return[];var e=arguments[0];return J(e)?e:[e]}function wg(e){return dn(e,K)}function _g(e,n){return n=typeof n=="function"?n:r,dn(e,K,n)}function Sg(e){return dn(e,x|K)}function Ng(e,n){return n=typeof n=="function"?n:r,dn(e,x|K,n)}function Ag(e,n){return n==null||_o(e,n,xe(n))}function Nn(e,n){return e===n||e!==e&&n!==n}var Og=lr(Ts),Rg=lr(function(e,n){return e>=n}),Ot=Do(function(){return arguments}())?Do:function(e){return Ee(e)&&fe.call(e,"callee")&&!mo.call(e,"callee")},J=E.isArray,Dg=za?en(za):Bh;function Qe(e){return e!=null&&vr(e.length)&&!Kn(e)}function we(e){return Ee(e)&&Qe(e)}function xg(e){return e===!0||e===!1||Ee(e)&&Ve(e)==ue}var pt=jf||ra,Lg=Xa?en(Xa):Mh;function kg(e){return Ee(e)&&e.nodeType===1&&!Ii(e)}function Cg(e){if(e==null)return!0;if(Qe(e)&&(J(e)||typeof e=="string"||typeof e.splice=="function"||pt(e)||qt(e)||Ot(e)))return!e.length;var n=Be(e);if(n==bn||n==wn)return!e.size;if(yi(e))return!bs(e).length;for(var i in e)if(fe.call(e,i))return!1;return!0}function Fg(e,n){return gi(e,n)}function Pg(e,n,i){i=typeof i=="function"?i:r;var a=i?i(e,n):r;return a===r?gi(e,n,r,i):!!a}function Qs(e){if(!Ee(e))return!1;var n=Ve(e);return n==st||n==ei||typeof e.message=="string"&&typeof e.name=="string"&&!Ii(e)}function Ug(e){return typeof e=="number"&&yo(e)}function Kn(e){if(!Ie(e))return!1;var n=Ve(e);return n==Pe||n==_a||n==be||n==ac}function Bl(e){return typeof e=="number"&&e==z(e)}function vr(e){return typeof e=="number"&&e>-1&&e%1==0&&e<=En}function Ie(e){var n=typeof e;return e!=null&&(n=="object"||n=="function")}function Ee(e){return e!=null&&typeof e=="object"}var Ml=Ka?en(Ka):Vh;function Bg(e,n){return e===n||Es(e,n,Bs(n))}function Mg(e,n,i){return i=typeof i=="function"?i:r,Es(e,n,Bs(n),i)}function Gg(e){return Gl(e)&&e!=+e}function Vg(e){if(_d(e))throw new Q(h);return xo(e)}function Wg(e){return e===null}function Yg(e){return e==null}function Gl(e){return typeof e=="number"||Ee(e)&&Ve(e)==ni}function Ii(e){if(!Ee(e)||Ve(e)!=jn)return!1;var n=$i(e);if(n===null)return!0;var i=fe.call(n,"constructor")&&n.constructor;return typeof i=="function"&&i instanceof i&&Gi.call(i)==Mf}var Js=Za?en(Za):Wh;function $g(e){return Bl(e)&&e>=-En&&e<=En}var Vl=eo?en(eo):Yh;function yr(e){return typeof e=="string"||!J(e)&&Ee(e)&&Ve(e)==ii}function tn(e){return typeof e=="symbol"||Ee(e)&&Ve(e)==xi}var qt=no?en(no):$h;function jg(e){return e===r}function Hg(e){return Ee(e)&&Be(e)==ri}function qg(e){return Ee(e)&&Ve(e)==lc}var Qg=lr(ws),Jg=lr(function(e,n){return e<=n});function Wl(e){if(!e)return[];if(Qe(e))return yr(e)?_n(e):qe(e);if(oi&&e[oi])return Af(e[oi]());var n=Be(e),i=n==bn?cs:n==wn?Ui:Qt;return i(e)}function Zn(e){if(!e)return e===0?e:0;if(e=mn(e),e===Pn||e===-Pn){var n=e<0?-1:1;return n*kt}return e===e?e:0}function z(e){var n=Zn(e),i=n%1;return n===n?i?n-i:n:0}function Yl(e){return e?_t(z(e),0,on):0}function mn(e){if(typeof e=="number")return e;if(tn(e))return Tt;if(Ie(e)){var n=typeof e.valueOf=="function"?e.valueOf():e;e=Ie(n)?n+"":n}if(typeof e!="string")return e===0?e:+e;e=oo(e);var i=Rc.test(e);return i||xc.test(e)?cf(e.slice(2),i?2:8):Oc.test(e)?Tt:+e}function $l(e){return Bn(e,Je(e))}function zg(e){return e?_t(z(e),-En,En):e===0?e:0}function ce(e){return e==null?"":nn(e)}var Xg=$t(function(e,n){if(yi(n)||Qe(n)){Bn(n,xe(n),e);return}for(var i in n)fe.call(n,i)&&hi(e,i,n[i])}),jl=$t(function(e,n){Bn(n,Je(n),e)}),Tr=$t(function(e,n,i,a){Bn(n,Je(n),e,a)}),Kg=$t(function(e,n,i,a){Bn(n,xe(n),e,a)}),Zg=zn(ms);function em(e,n){var i=Yt(e);return n==null?i:wo(i,n)}var nm=Z(function(e,n){e=de(e);var i=-1,a=n.length,u=a>2?n[2]:r;for(u&&We(n[0],n[1],u)&&(a=1);++i<a;)for(var f=n[i],d=Je(f),p=-1,v=d.length;++p<v;){var S=d[p],N=e[S];(N===r||Nn(N,Gt[S])&&!fe.call(e,S))&&(e[S]=f[S])}return e}),tm=Z(function(e){return e.push(r,ll),Ze(Hl,r,e)});function im(e,n){return io(e,$(n,3),Un)}function rm(e,n){return io(e,$(n,3),ys)}function sm(e,n){return e==null?e:vs(e,$(n,3),Je)}function am(e,n){return e==null?e:Oo(e,$(n,3),Je)}function om(e,n){return e&&Un(e,$(n,3))}function lm(e,n){return e&&ys(e,$(n,3))}function um(e){return e==null?[]:er(e,xe(e))}function cm(e){return e==null?[]:er(e,Je(e))}function zs(e,n,i){var a=e==null?r:St(e,n);return a===r?i:a}function fm(e,n){return e!=null&&fl(e,n,Ch)}function Xs(e,n){return e!=null&&fl(e,n,Fh)}var hm=il(function(e,n,i){n!=null&&typeof n.toString!="function"&&(n=Vi.call(n)),e[n]=i},Zs(ze)),dm=il(function(e,n,i){n!=null&&typeof n.toString!="function"&&(n=Vi.call(n)),fe.call(e,n)?e[n].push(i):e[n]=[i]},$),pm=Z(pi);function xe(e){return Qe(e)?Eo(e):bs(e)}function Je(e){return Qe(e)?Eo(e,!0):jh(e)}function gm(e,n){var i={};return n=$(n,3),Un(e,function(a,u,f){Qn(i,n(a,u,f),a)}),i}function mm(e,n){var i={};return n=$(n,3),Un(e,function(a,u,f){Qn(i,u,n(a,u,f))}),i}var vm=$t(function(e,n,i){nr(e,n,i)}),Hl=$t(function(e,n,i,a){nr(e,n,i,a)}),ym=zn(function(e,n){var i={};if(e==null)return i;var a=!1;n=ve(n,function(f){return f=ht(f,e),a||(a=f.length>1),f}),Bn(e,Ps(e),i),a&&(i=dn(i,x|B|K,hd));for(var u=n.length;u--;)Os(i,n[u]);return i});function Tm(e,n){return ql(e,mr($(n)))}var Im=zn(function(e,n){return e==null?{}:qh(e,n)});function ql(e,n){if(e==null)return{};var i=ve(Ps(e),function(a){return[a]});return n=$(n),Bo(e,i,function(a,u){return n(a,u[0])})}function Em(e,n,i){n=ht(n,e);var a=-1,u=n.length;for(u||(u=1,e=r);++a<u;){var f=e==null?r:e[Mn(n[a])];f===r&&(a=u,f=i),e=Kn(f)?f.call(e):f}return e}function bm(e,n,i){return e==null?e:mi(e,n,i)}function wm(e,n,i,a){return a=typeof a=="function"?a:r,e==null?e:mi(e,n,i,a)}var Ql=al(xe),Jl=al(Je);function _m(e,n,i){var a=J(e),u=a||pt(e)||qt(e);if(n=$(n,4),i==null){var f=e&&e.constructor;u?i=a?new f:[]:Ie(e)?i=Kn(f)?Yt($i(e)):{}:i={}}return(u?cn:Un)(e,function(d,p,v){return n(i,d,p,v)}),i}function Sm(e,n){return e==null?!0:Os(e,n)}function Nm(e,n,i){return e==null?e:Yo(e,n,xs(i))}function Am(e,n,i,a){return a=typeof a=="function"?a:r,e==null?e:Yo(e,n,xs(i),a)}function Qt(e){return e==null?[]:us(e,xe(e))}function Om(e){return e==null?[]:us(e,Je(e))}function Rm(e,n,i){return i===r&&(i=n,n=r),i!==r&&(i=mn(i),i=i===i?i:0),n!==r&&(n=mn(n),n=n===n?n:0),_t(mn(e),n,i)}function Dm(e,n,i){return n=Zn(n),i===r?(i=n,n=0):i=Zn(i),e=mn(e),Ph(e,n,i)}function xm(e,n,i){if(i&&typeof i!="boolean"&&We(e,n,i)&&(n=i=r),i===r&&(typeof n=="boolean"?(i=n,n=r):typeof e=="boolean"&&(i=e,e=r)),e===r&&n===r?(e=0,n=1):(e=Zn(e),n===r?(n=e,e=0):n=Zn(n)),e>n){var a=e;e=n,n=a}if(i||e%1||n%1){var u=To();return Ue(e+u*(n-e+uf("1e-"+((u+"").length-1))),n)}return Ss(e,n)}var Lm=jt(function(e,n,i){return n=n.toLowerCase(),e+(i?zl(n):n)});function zl(e){return Ks(ce(e).toLowerCase())}function Xl(e){return e=ce(e),e&&e.replace(kc,bf).replace(Kc,"")}function km(e,n,i){e=ce(e),n=nn(n);var a=e.length;i=i===r?a:_t(z(i),0,a);var u=i;return i-=n.length,i>=0&&e.slice(i,u)==n}function Cm(e){return e=ce(e),e&&dc.test(e)?e.replace(Aa,wf):e}function Fm(e){return e=ce(e),e&&Tc.test(e)?e.replace(qr,"\\$&"):e}var Pm=jt(function(e,n,i){return e+(i?"-":"")+n.toLowerCase()}),Um=jt(function(e,n,i){return e+(i?" ":"")+n.toLowerCase()}),Bm=el("toLowerCase");function Mm(e,n,i){e=ce(e),n=z(n);var a=n?Bt(e):0;if(!n||a>=n)return e;var u=(n-a)/2;return or(Qi(u),i)+e+or(qi(u),i)}function Gm(e,n,i){e=ce(e),n=z(n);var a=n?Bt(e):0;return n&&a<n?e+or(n-a,i):e}function Vm(e,n,i){e=ce(e),n=z(n);var a=n?Bt(e):0;return n&&a<n?or(n-a,i)+e:e}function Wm(e,n,i){return i||n==null?n=0:n&&(n=+n),Jf(ce(e).replace(Qr,""),n||0)}function Ym(e,n,i){return(i?We(e,n,i):n===r)?n=1:n=z(n),Ns(ce(e),n)}function $m(){var e=arguments,n=ce(e[0]);return e.length<3?n:n.replace(e[1],e[2])}var jm=jt(function(e,n,i){return e+(i?"_":"")+n.toLowerCase()});function Hm(e,n,i){return i&&typeof i!="number"&&We(e,n,i)&&(n=i=r),i=i===r?on:i>>>0,i?(e=ce(e),e&&(typeof n=="string"||n!=null&&!Js(n))&&(n=nn(n),!n&&Ut(e))?dt(_n(e),0,i):e.split(n,i)):[]}var qm=jt(function(e,n,i){return e+(i?" ":"")+Ks(n)});function Qm(e,n,i){return e=ce(e),i=i==null?0:_t(z(i),0,e.length),n=nn(n),e.slice(i,i+n.length)==n}function Jm(e,n,i){var a=c.templateSettings;i&&We(e,n,i)&&(n=r),e=ce(e),n=Tr({},n,a,ol);var u=Tr({},n.imports,a.imports,ol),f=xe(u),d=us(u,f),p,v,S=0,N=n.interpolate||Li,D="__p += '",L=fs((n.escape||Li).source+"|"+N.source+"|"+(N===Oa?Ac:Li).source+"|"+(n.evaluate||Li).source+"|$","g"),M="//# sourceURL="+(fe.call(n,"sourceURL")?(n.sourceURL+"").replace(/\s/g," "):"lodash.templateSources["+ ++rf+"]")+`
`;e.replace(L,function(H,ee,re,rn,Ye,sn){return re||(re=rn),D+=e.slice(S,sn).replace(Cc,_f),ee&&(p=!0,D+=`' +
__e(`+ee+`) +
'`),Ye&&(v=!0,D+=`';
`+Ye+`;
__p += '`),re&&(D+=`' +
((__t = (`+re+`)) == null ? '' : __t) +
'`),S=sn+H.length,H}),D+=`';
`;var j=fe.call(n,"variable")&&n.variable;if(!j)D=`with (obj) {
`+D+`
}
`;else if(Sc.test(j))throw new Q(T);D=(v?D.replace(uc,""):D).replace(cc,"$1").replace(fc,"$1;"),D="function("+(j||"obj")+`) {
`+(j?"":`obj || (obj = {});
`)+"var __t, __p = ''"+(p?", __e = _.escape":"")+(v?`, __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
`:`;
`)+D+`return __p
}`;var X=Zl(function(){return oe(f,M+"return "+D).apply(r,d)});if(X.source=D,Qs(X))throw X;return X}function zm(e){return ce(e).toLowerCase()}function Xm(e){return ce(e).toUpperCase()}function Km(e,n,i){if(e=ce(e),e&&(i||n===r))return oo(e);if(!e||!(n=nn(n)))return e;var a=_n(e),u=_n(n),f=lo(a,u),d=uo(a,u)+1;return dt(a,f,d).join("")}function Zm(e,n,i){if(e=ce(e),e&&(i||n===r))return e.slice(0,fo(e)+1);if(!e||!(n=nn(n)))return e;var a=_n(e),u=uo(a,_n(n))+1;return dt(a,0,u).join("")}function ev(e,n,i){if(e=ce(e),e&&(i||n===r))return e.replace(Qr,"");if(!e||!(n=nn(n)))return e;var a=_n(e),u=lo(a,_n(n));return dt(a,u).join("")}function nv(e,n){var i=q,a=Ge;if(Ie(n)){var u="separator"in n?n.separator:u;i="length"in n?z(n.length):i,a="omission"in n?nn(n.omission):a}e=ce(e);var f=e.length;if(Ut(e)){var d=_n(e);f=d.length}if(i>=f)return e;var p=i-Bt(a);if(p<1)return a;var v=d?dt(d,0,p).join(""):e.slice(0,p);if(u===r)return v+a;if(d&&(p+=v.length-p),Js(u)){if(e.slice(p).search(u)){var S,N=v;for(u.global||(u=fs(u.source,ce(Ra.exec(u))+"g")),u.lastIndex=0;S=u.exec(N);)var D=S.index;v=v.slice(0,D===r?p:D)}}else if(e.indexOf(nn(u),p)!=p){var L=v.lastIndexOf(u);L>-1&&(v=v.slice(0,L))}return v+a}function tv(e){return e=ce(e),e&&hc.test(e)?e.replace(Na,xf):e}var iv=jt(function(e,n,i){return e+(i?" ":"")+n.toUpperCase()}),Ks=el("toUpperCase");function Kl(e,n,i){return e=ce(e),n=i?r:n,n===r?Nf(e)?Cf(e):vf(e):e.match(n)||[]}var Zl=Z(function(e,n){try{return Ze(e,r,n)}catch(i){return Qs(i)?i:new Q(i)}}),rv=zn(function(e,n){return cn(n,function(i){i=Mn(i),Qn(e,i,Hs(e[i],e))}),e});function sv(e){var n=e==null?0:e.length,i=$();return e=n?ve(e,function(a){if(typeof a[1]!="function")throw new fn(g);return[i(a[0]),a[1]]}):[],Z(function(a){for(var u=-1;++u<n;){var f=e[u];if(Ze(f[0],this,a))return Ze(f[1],this,a)}})}function av(e){return xh(dn(e,x))}function Zs(e){return function(){return e}}function ov(e,n){return e==null||e!==e?n:e}var lv=tl(),uv=tl(!0);function ze(e){return e}function ea(e){return Lo(typeof e=="function"?e:dn(e,x))}function cv(e){return Co(dn(e,x))}function fv(e,n){return Fo(e,dn(n,x))}var hv=Z(function(e,n){return function(i){return pi(i,e,n)}}),dv=Z(function(e,n){return function(i){return pi(e,i,n)}});function na(e,n,i){var a=xe(n),u=er(n,a);i==null&&!(Ie(n)&&(u.length||!a.length))&&(i=n,n=e,e=this,u=er(n,xe(n)));var f=!(Ie(i)&&"chain"in i)||!!i.chain,d=Kn(e);return cn(u,function(p){var v=n[p];e[p]=v,d&&(e.prototype[p]=function(){var S=this.__chain__;if(f||S){var N=e(this.__wrapped__),D=N.__actions__=qe(this.__actions__);return D.push({func:v,args:arguments,thisArg:e}),N.__chain__=S,N}return v.apply(e,ot([this.value()],arguments))})}),e}function pv(){return ke._===this&&(ke._=Gf),this}function ta(){}function gv(e){return e=z(e),Z(function(n){return Po(n,e)})}var mv=ks(ve),vv=ks(to),yv=ks(rs);function eu(e){return Gs(e)?ss(Mn(e)):Qh(e)}function Tv(e){return function(n){return e==null?r:St(e,n)}}var Iv=rl(),Ev=rl(!0);function ia(){return[]}function ra(){return!1}function bv(){return{}}function wv(){return""}function _v(){return!0}function Sv(e,n){if(e=z(e),e<1||e>En)return[];var i=on,a=Ue(e,on);n=$(n),e-=on;for(var u=ls(a,n);++i<e;)n(i);return u}function Nv(e){return J(e)?ve(e,Mn):tn(e)?[e]:qe(Il(ce(e)))}function Av(e){var n=++Bf;return ce(e)+n}var Ov=ar(function(e,n){return e+n},0),Rv=Cs("ceil"),Dv=ar(function(e,n){return e/n},1),xv=Cs("floor");function Lv(e){return e&&e.length?Zi(e,ze,Ts):r}function kv(e,n){return e&&e.length?Zi(e,$(n,2),Ts):r}function Cv(e){return so(e,ze)}function Fv(e,n){return so(e,$(n,2))}function Pv(e){return e&&e.length?Zi(e,ze,ws):r}function Uv(e,n){return e&&e.length?Zi(e,$(n,2),ws):r}var Bv=ar(function(e,n){return e*n},1),Mv=Cs("round"),Gv=ar(function(e,n){return e-n},0);function Vv(e){return e&&e.length?os(e,ze):0}function Wv(e,n){return e&&e.length?os(e,$(n,2)):0}return c.after=cg,c.ary=xl,c.assign=Xg,c.assignIn=jl,c.assignInWith=Tr,c.assignWith=Kg,c.at=Zg,c.before=Ll,c.bind=Hs,c.bindAll=rv,c.bindKey=kl,c.castArray=bg,c.chain=Ol,c.chunk=xd,c.compact=Ld,c.concat=kd,c.cond=sv,c.conforms=av,c.constant=Zs,c.countBy=Vp,c.create=em,c.curry=Cl,c.curryRight=Fl,c.debounce=Pl,c.defaults=nm,c.defaultsDeep=tm,c.defer=fg,c.delay=hg,c.difference=Cd,c.differenceBy=Fd,c.differenceWith=Pd,c.drop=Ud,c.dropRight=Bd,c.dropRightWhile=Md,c.dropWhile=Gd,c.fill=Vd,c.filter=Yp,c.flatMap=Hp,c.flatMapDeep=qp,c.flatMapDepth=Qp,c.flatten=_l,c.flattenDeep=Wd,c.flattenDepth=Yd,c.flip=dg,c.flow=lv,c.flowRight=uv,c.fromPairs=$d,c.functions=um,c.functionsIn=cm,c.groupBy=Jp,c.initial=Hd,c.intersection=qd,c.intersectionBy=Qd,c.intersectionWith=Jd,c.invert=hm,c.invertBy=dm,c.invokeMap=Xp,c.iteratee=ea,c.keyBy=Kp,c.keys=xe,c.keysIn=Je,c.map=dr,c.mapKeys=gm,c.mapValues=mm,c.matches=cv,c.matchesProperty=fv,c.memoize=gr,c.merge=vm,c.mergeWith=Hl,c.method=hv,c.methodOf=dv,c.mixin=na,c.negate=mr,c.nthArg=gv,c.omit=ym,c.omitBy=Tm,c.once=pg,c.orderBy=Zp,c.over=mv,c.overArgs=gg,c.overEvery=vv,c.overSome=yv,c.partial=qs,c.partialRight=Ul,c.partition=eg,c.pick=Im,c.pickBy=ql,c.property=eu,c.propertyOf=Tv,c.pull=Zd,c.pullAll=Nl,c.pullAllBy=ep,c.pullAllWith=np,c.pullAt=tp,c.range=Iv,c.rangeRight=Ev,c.rearg=mg,c.reject=ig,c.remove=ip,c.rest=vg,c.reverse=$s,c.sampleSize=sg,c.set=bm,c.setWith=wm,c.shuffle=ag,c.slice=rp,c.sortBy=ug,c.sortedUniq=fp,c.sortedUniqBy=hp,c.split=Hm,c.spread=yg,c.tail=dp,c.take=pp,c.takeRight=gp,c.takeRightWhile=mp,c.takeWhile=vp,c.tap=Lp,c.throttle=Tg,c.thru=hr,c.toArray=Wl,c.toPairs=Ql,c.toPairsIn=Jl,c.toPath=Nv,c.toPlainObject=$l,c.transform=_m,c.unary=Ig,c.union=yp,c.unionBy=Tp,c.unionWith=Ip,c.uniq=Ep,c.uniqBy=bp,c.uniqWith=wp,c.unset=Sm,c.unzip=js,c.unzipWith=Al,c.update=Nm,c.updateWith=Am,c.values=Qt,c.valuesIn=Om,c.without=_p,c.words=Kl,c.wrap=Eg,c.xor=Sp,c.xorBy=Np,c.xorWith=Ap,c.zip=Op,c.zipObject=Rp,c.zipObjectDeep=Dp,c.zipWith=xp,c.entries=Ql,c.entriesIn=Jl,c.extend=jl,c.extendWith=Tr,na(c,c),c.add=Ov,c.attempt=Zl,c.camelCase=Lm,c.capitalize=zl,c.ceil=Rv,c.clamp=Rm,c.clone=wg,c.cloneDeep=Sg,c.cloneDeepWith=Ng,c.cloneWith=_g,c.conformsTo=Ag,c.deburr=Xl,c.defaultTo=ov,c.divide=Dv,c.endsWith=km,c.eq=Nn,c.escape=Cm,c.escapeRegExp=Fm,c.every=Wp,c.find=$p,c.findIndex=bl,c.findKey=im,c.findLast=jp,c.findLastIndex=wl,c.findLastKey=rm,c.floor=xv,c.forEach=Rl,c.forEachRight=Dl,c.forIn=sm,c.forInRight=am,c.forOwn=om,c.forOwnRight=lm,c.get=zs,c.gt=Og,c.gte=Rg,c.has=fm,c.hasIn=Xs,c.head=Sl,c.identity=ze,c.includes=zp,c.indexOf=jd,c.inRange=Dm,c.invoke=pm,c.isArguments=Ot,c.isArray=J,c.isArrayBuffer=Dg,c.isArrayLike=Qe,c.isArrayLikeObject=we,c.isBoolean=xg,c.isBuffer=pt,c.isDate=Lg,c.isElement=kg,c.isEmpty=Cg,c.isEqual=Fg,c.isEqualWith=Pg,c.isError=Qs,c.isFinite=Ug,c.isFunction=Kn,c.isInteger=Bl,c.isLength=vr,c.isMap=Ml,c.isMatch=Bg,c.isMatchWith=Mg,c.isNaN=Gg,c.isNative=Vg,c.isNil=Yg,c.isNull=Wg,c.isNumber=Gl,c.isObject=Ie,c.isObjectLike=Ee,c.isPlainObject=Ii,c.isRegExp=Js,c.isSafeInteger=$g,c.isSet=Vl,c.isString=yr,c.isSymbol=tn,c.isTypedArray=qt,c.isUndefined=jg,c.isWeakMap=Hg,c.isWeakSet=qg,c.join=zd,c.kebabCase=Pm,c.last=gn,c.lastIndexOf=Xd,c.lowerCase=Um,c.lowerFirst=Bm,c.lt=Qg,c.lte=Jg,c.max=Lv,c.maxBy=kv,c.mean=Cv,c.meanBy=Fv,c.min=Pv,c.minBy=Uv,c.stubArray=ia,c.stubFalse=ra,c.stubObject=bv,c.stubString=wv,c.stubTrue=_v,c.multiply=Bv,c.nth=Kd,c.noConflict=pv,c.noop=ta,c.now=pr,c.pad=Mm,c.padEnd=Gm,c.padStart=Vm,c.parseInt=Wm,c.random=xm,c.reduce=ng,c.reduceRight=tg,c.repeat=Ym,c.replace=$m,c.result=Em,c.round=Mv,c.runInContext=m,c.sample=rg,c.size=og,c.snakeCase=jm,c.some=lg,c.sortedIndex=sp,c.sortedIndexBy=ap,c.sortedIndexOf=op,c.sortedLastIndex=lp,c.sortedLastIndexBy=up,c.sortedLastIndexOf=cp,c.startCase=qm,c.startsWith=Qm,c.subtract=Gv,c.sum=Vv,c.sumBy=Wv,c.template=Jm,c.times=Sv,c.toFinite=Zn,c.toInteger=z,c.toLength=Yl,c.toLower=zm,c.toNumber=mn,c.toSafeInteger=zg,c.toString=ce,c.toUpper=Xm,c.trim=Km,c.trimEnd=Zm,c.trimStart=ev,c.truncate=nv,c.unescape=tv,c.uniqueId=Av,c.upperCase=iv,c.upperFirst=Ks,c.each=Rl,c.eachRight=Dl,c.first=Sl,na(c,function(){var e={};return Un(c,function(n,i){fe.call(c.prototype,i)||(e[i]=n)}),e}(),{chain:!1}),c.VERSION=o,cn(["bind","bindKey","curry","curryRight","partial","partialRight"],function(e){c[e].placeholder=c}),cn(["drop","take"],function(e,n){te.prototype[e]=function(i){i=i===r?1:Re(z(i),0);var a=this.__filtered__&&!n?new te(this):this.clone();return a.__filtered__?a.__takeCount__=Ue(i,a.__takeCount__):a.__views__.push({size:Ue(i,on),type:e+(a.__dir__<0?"Right":"")}),a},te.prototype[e+"Right"]=function(i){return this.reverse()[e](i).reverse()}}),cn(["filter","map","takeWhile"],function(e,n){var i=n+1,a=i==Ri||i==Di;te.prototype[e]=function(u){var f=this.clone();return f.__iteratees__.push({iteratee:$(u,3),type:i}),f.__filtered__=f.__filtered__||a,f}}),cn(["head","last"],function(e,n){var i="take"+(n?"Right":"");te.prototype[e]=function(){return this[i](1).value()[0]}}),cn(["initial","tail"],function(e,n){var i="drop"+(n?"":"Right");te.prototype[e]=function(){return this.__filtered__?new te(this):this[i](1)}}),te.prototype.compact=function(){return this.filter(ze)},te.prototype.find=function(e){return this.filter(e).head()},te.prototype.findLast=function(e){return this.reverse().find(e)},te.prototype.invokeMap=Z(function(e,n){return typeof e=="function"?new te(this):this.map(function(i){return pi(i,e,n)})}),te.prototype.reject=function(e){return this.filter(mr($(e)))},te.prototype.slice=function(e,n){e=z(e);var i=this;return i.__filtered__&&(e>0||n<0)?new te(i):(e<0?i=i.takeRight(-e):e&&(i=i.drop(e)),n!==r&&(n=z(n),i=n<0?i.dropRight(-n):i.take(n-e)),i)},te.prototype.takeRightWhile=function(e){return this.reverse().takeWhile(e).reverse()},te.prototype.toArray=function(){return this.take(on)},Un(te.prototype,function(e,n){var i=/^(?:filter|find|map|reject)|While$/.test(n),a=/^(?:head|last)$/.test(n),u=c[a?"take"+(n=="last"?"Right":""):n],f=a||/^find/.test(n);u&&(c.prototype[n]=function(){var d=this.__wrapped__,p=a?[1]:arguments,v=d instanceof te,S=p[0],N=v||J(d),D=function(ee){var re=u.apply(c,ot([ee],p));return a&&L?re[0]:re};N&&i&&typeof S=="function"&&S.length!=1&&(v=N=!1);var L=this.__chain__,M=!!this.__actions__.length,j=f&&!L,X=v&&!M;if(!f&&N){d=X?d:new te(this);var H=e.apply(d,p);return H.__actions__.push({func:hr,args:[D],thisArg:r}),new hn(H,L)}return j&&X?e.apply(this,p):(H=this.thru(D),j?a?H.value()[0]:H.value():H)})}),cn(["pop","push","shift","sort","splice","unshift"],function(e){var n=Bi[e],i=/^(?:push|sort|unshift)$/.test(e)?"tap":"thru",a=/^(?:pop|shift)$/.test(e);c.prototype[e]=function(){var u=arguments;if(a&&!this.__chain__){var f=this.value();return n.apply(J(f)?f:[],u)}return this[i](function(d){return n.apply(J(d)?d:[],u)})}}),Un(te.prototype,function(e,n){var i=c[n];if(i){var a=i.name+"";fe.call(Wt,a)||(Wt[a]=[]),Wt[a].push({name:n,func:i})}}),Wt[sr(r,ye).name]=[{name:"wrapper",func:r}],te.prototype.clone=th,te.prototype.reverse=ih,te.prototype.value=rh,c.prototype.at=kp,c.prototype.chain=Cp,c.prototype.commit=Fp,c.prototype.next=Pp,c.prototype.plant=Bp,c.prototype.reverse=Mp,c.prototype.toJSON=c.prototype.valueOf=c.prototype.value=Gp,c.prototype.first=c.prototype.head,oi&&(c.prototype[oi]=Up),c},Mt=Ff();It?((It.exports=Mt)._=Mt,es._=Mt):ke._=Mt}).call(Ei)})(Qv,Tn);const Jv="/api/dev-2.x/graphql-gtfs/v1",zv='[{"title":"Introduction","content":"\\n# Static documentation\\n\\nThis is the static documentation of the OTP GraphQL GTFS API v1.\\n\\nPlease also check out the interactive API explorer built into every instance and available\\nat http://localhost:8080/graphiql \\n"}]',Xv="OTP GTFS GraphQL API v1",Kv='{"Polyline":"<>","GeoJson":"<>"}',Zv="true",Eu={SITE_ROOT:Jv,PAGES:zv,APP_TITLE:Xv,QUERY_GENERATION_FACTORIES:Kv,MAGIDOC_GENERATE:Zv};function lI(t){return t.get(Eu)}function ey(t,s){return t.getOrDefault(Eu,s)}function ae(t,s){if(!!!t)throw new Error(s)}function tt(t){return typeof t=="object"&&t!==null}function xn(t,s){if(!!!t)throw new Error(s??"Unexpected invariant triggered.")}const ny=/\r\n|[\n\r]/g;function ca(t,s){let r=0,o=1;for(const l of t.body.matchAll(ny)){if(typeof l.index=="number"||xn(!1),l.index>=s)break;r=l.index+l[0].length,o+=1}return{line:o,column:s+1-r}}function ty(t){return bu(t.source,ca(t.source,t.start))}function bu(t,s){const r=t.locationOffset.column-1,o="".padStart(r)+t.body,l=s.line-1,h=t.locationOffset.line-1,g=s.line+h,T=s.line===1?r:0,I=s.column+T,O=`${t.name}:${g}:${I}
`,_=o.split(/\r\n|[\n\r]/g),x=_[l];if(x.length>120){const B=Math.floor(I/80),K=I%80,V=[];for(let he=0;he<x.length;he+=80)V.push(x.slice(he,he+80));return O+su([[`${g} |`,V[0]],...V.slice(1,B+1).map(he=>["|",he]),["|","^".padStart(K)],["|",V[B+1]]])}return O+su([[`${g-1} |`,_[l-1]],[`${g} |`,x],["|","^".padStart(I)],[`${g+1} |`,_[l+1]]])}function su(t){const s=t.filter(([o,l])=>l!==void 0),r=Math.max(...s.map(([o])=>o.length));return s.map(([o,l])=>o.padStart(r)+(l?" "+l:"")).join(`
`)}function iy(t){const s=t[0];return s==null||"kind"in s||"length"in s?{nodes:s,source:t[1],positions:t[2],path:t[3],originalError:t[4],extensions:t[5]}:s}class G extends Error{constructor(s,...r){var o,l,h;const{nodes:g,source:T,positions:I,path:O,originalError:_,extensions:x}=iy(r);super(s),this.name="GraphQLError",this.path=O??void 0,this.originalError=_??void 0,this.nodes=au(Array.isArray(g)?g:g?[g]:void 0);const B=au((o=this.nodes)===null||o===void 0?void 0:o.map(V=>V.loc).filter(V=>V!=null));this.source=T??(B==null||(l=B[0])===null||l===void 0?void 0:l.source),this.positions=I??(B==null?void 0:B.map(V=>V.start)),this.locations=I&&T?I.map(V=>ca(T,V)):B==null?void 0:B.map(V=>ca(V.source,V.start));const K=tt(_==null?void 0:_.extensions)?_==null?void 0:_.extensions:void 0;this.extensions=(h=x??K)!==null&&h!==void 0?h:Object.create(null),Object.defineProperties(this,{message:{writable:!0,enumerable:!0},name:{enumerable:!1},nodes:{enumerable:!1},source:{enumerable:!1},positions:{enumerable:!1},originalError:{enumerable:!1}}),_!=null&&_.stack?Object.defineProperty(this,"stack",{value:_.stack,writable:!0,configurable:!0}):Error.captureStackTrace?Error.captureStackTrace(this,G):Object.defineProperty(this,"stack",{value:Error().stack,writable:!0,configurable:!0})}get[Symbol.toStringTag](){return"GraphQLError"}toString(){let s=this.message;if(this.nodes)for(const r of this.nodes)r.loc&&(s+=`

`+ty(r.loc));else if(this.source&&this.locations)for(const r of this.locations)s+=`

`+bu(this.source,r);return s}toJSON(){const s={message:this.message};return this.locations!=null&&(s.locations=this.locations),this.path!=null&&(s.path=this.path),this.extensions!=null&&Object.keys(this.extensions).length>0&&(s.extensions=this.extensions),s}}function au(t){return t===void 0||t.length===0?void 0:t}function Le(t,s,r){return new G(`Syntax Error: ${r}`,{source:t,positions:[s]})}class ry{constructor(s,r,o){this.start=s.start,this.end=r.end,this.startToken=s,this.endToken=r,this.source=o}get[Symbol.toStringTag](){return"Location"}toJSON(){return{start:this.start,end:this.end}}}class wu{constructor(s,r,o,l,h,g){this.kind=s,this.start=r,this.end=o,this.line=l,this.column=h,this.value=g,this.prev=null,this.next=null}get[Symbol.toStringTag](){return"Token"}toJSON(){return{kind:this.kind,value:this.value,line:this.line,column:this.column}}}const _u={Name:[],Document:["definitions"],OperationDefinition:["name","variableDefinitions","directives","selectionSet"],VariableDefinition:["variable","type","defaultValue","directives"],Variable:["name"],SelectionSet:["selections"],Field:["alias","name","arguments","directives","selectionSet"],Argument:["name","value"],FragmentSpread:["name","directives"],InlineFragment:["typeCondition","directives","selectionSet"],FragmentDefinition:["name","variableDefinitions","typeCondition","directives","selectionSet"],IntValue:[],FloatValue:[],StringValue:[],BooleanValue:[],NullValue:[],EnumValue:[],ListValue:["values"],ObjectValue:["fields"],ObjectField:["name","value"],Directive:["name","arguments"],NamedType:["name"],ListType:["type"],NonNullType:["type"],SchemaDefinition:["description","directives","operationTypes"],OperationTypeDefinition:["type"],ScalarTypeDefinition:["description","name","directives"],ObjectTypeDefinition:["description","name","interfaces","directives","fields"],FieldDefinition:["description","name","arguments","type","directives"],InputValueDefinition:["description","name","type","defaultValue","directives"],InterfaceTypeDefinition:["description","name","interfaces","directives","fields"],UnionTypeDefinition:["description","name","directives","types"],EnumTypeDefinition:["description","name","directives","values"],EnumValueDefinition:["description","name","directives"],InputObjectTypeDefinition:["description","name","directives","fields"],DirectiveDefinition:["description","name","arguments","locations"],SchemaExtension:["directives","operationTypes"],ScalarTypeExtension:["name","directives"],ObjectTypeExtension:["name","interfaces","directives","fields"],InterfaceTypeExtension:["name","interfaces","directives","fields"],UnionTypeExtension:["name","directives","types"],EnumTypeExtension:["name","directives","values"],InputObjectTypeExtension:["name","directives","fields"]},sy=new Set(Object.keys(_u));function ou(t){const s=t==null?void 0:t.kind;return typeof s=="string"&&sy.has(s)}var vn;(function(t){t.QUERY="query",t.MUTATION="mutation",t.SUBSCRIPTION="subscription"})(vn||(vn={}));var Y;(function(t){t.QUERY="QUERY",t.MUTATION="MUTATION",t.SUBSCRIPTION="SUBSCRIPTION",t.FIELD="FIELD",t.FRAGMENT_DEFINITION="FRAGMENT_DEFINITION",t.FRAGMENT_SPREAD="FRAGMENT_SPREAD",t.INLINE_FRAGMENT="INLINE_FRAGMENT",t.VARIABLE_DEFINITION="VARIABLE_DEFINITION",t.SCHEMA="SCHEMA",t.SCALAR="SCALAR",t.OBJECT="OBJECT",t.FIELD_DEFINITION="FIELD_DEFINITION",t.ARGUMENT_DEFINITION="ARGUMENT_DEFINITION",t.INTERFACE="INTERFACE",t.UNION="UNION",t.ENUM="ENUM",t.ENUM_VALUE="ENUM_VALUE",t.INPUT_OBJECT="INPUT_OBJECT",t.INPUT_FIELD_DEFINITION="INPUT_FIELD_DEFINITION"})(Y||(Y={}));var y;(function(t){t.NAME="Name",t.DOCUMENT="Document",t.OPERATION_DEFINITION="OperationDefinition",t.VARIABLE_DEFINITION="VariableDefinition",t.SELECTION_SET="SelectionSet",t.FIELD="Field",t.ARGUMENT="Argument",t.FRAGMENT_SPREAD="FragmentSpread",t.INLINE_FRAGMENT="InlineFragment",t.FRAGMENT_DEFINITION="FragmentDefinition",t.VARIABLE="Variable",t.INT="IntValue",t.FLOAT="FloatValue",t.STRING="StringValue",t.BOOLEAN="BooleanValue",t.NULL="NullValue",t.ENUM="EnumValue",t.LIST="ListValue",t.OBJECT="ObjectValue",t.OBJECT_FIELD="ObjectField",t.DIRECTIVE="Directive",t.NAMED_TYPE="NamedType",t.LIST_TYPE="ListType",t.NON_NULL_TYPE="NonNullType",t.SCHEMA_DEFINITION="SchemaDefinition",t.OPERATION_TYPE_DEFINITION="OperationTypeDefinition",t.SCALAR_TYPE_DEFINITION="ScalarTypeDefinition",t.OBJECT_TYPE_DEFINITION="ObjectTypeDefinition",t.FIELD_DEFINITION="FieldDefinition",t.INPUT_VALUE_DEFINITION="InputValueDefinition",t.INTERFACE_TYPE_DEFINITION="InterfaceTypeDefinition",t.UNION_TYPE_DEFINITION="UnionTypeDefinition",t.ENUM_TYPE_DEFINITION="EnumTypeDefinition",t.ENUM_VALUE_DEFINITION="EnumValueDefinition",t.INPUT_OBJECT_TYPE_DEFINITION="InputObjectTypeDefinition",t.DIRECTIVE_DEFINITION="DirectiveDefinition",t.SCHEMA_EXTENSION="SchemaExtension",t.SCALAR_TYPE_EXTENSION="ScalarTypeExtension",t.OBJECT_TYPE_EXTENSION="ObjectTypeExtension",t.INTERFACE_TYPE_EXTENSION="InterfaceTypeExtension",t.UNION_TYPE_EXTENSION="UnionTypeExtension",t.ENUM_TYPE_EXTENSION="EnumTypeExtension",t.INPUT_OBJECT_TYPE_EXTENSION="InputObjectTypeExtension"})(y||(y={}));function fa(t){return t===9||t===32}function Ni(t){return t>=48&&t<=57}function Su(t){return t>=97&&t<=122||t>=65&&t<=90}function ya(t){return Su(t)||t===95}function Nu(t){return Su(t)||Ni(t)||t===95}function ay(t){var s;let r=Number.MAX_SAFE_INTEGER,o=null,l=-1;for(let g=0;g<t.length;++g){var h;const T=t[g],I=oy(T);I!==T.length&&(o=(h=o)!==null&&h!==void 0?h:g,l=g,g!==0&&I<r&&(r=I))}return t.map((g,T)=>T===0?g:g.slice(r)).slice((s=o)!==null&&s!==void 0?s:0,l+1)}function oy(t){let s=0;for(;s<t.length&&fa(t.charCodeAt(s));)++s;return s}function ly(t,s){const r=t.replace(/"""/g,'\\"""'),o=r.split(/\r\n|[\n\r]/g),l=o.length===1,h=o.length>1&&o.slice(1).every(K=>K.length===0||fa(K.charCodeAt(0))),g=r.endsWith('\\"""'),T=t.endsWith('"')&&!g,I=t.endsWith("\\"),O=T||I,_=!(s!=null&&s.minimize)&&(!l||t.length>70||O||h||g);let x="";const B=l&&fa(t.charCodeAt(0));return(_&&!B||h)&&(x+=`
`),x+=r,(_||O)&&(x+=`
`),'"""'+x+'"""'}var A;(function(t){t.SOF="<SOF>",t.EOF="<EOF>",t.BANG="!",t.DOLLAR="$",t.AMP="&",t.PAREN_L="(",t.PAREN_R=")",t.SPREAD="...",t.COLON=":",t.EQUALS="=",t.AT="@",t.BRACKET_L="[",t.BRACKET_R="]",t.BRACE_L="{",t.PIPE="|",t.BRACE_R="}",t.NAME="Name",t.INT="Int",t.FLOAT="Float",t.STRING="String",t.BLOCK_STRING="BlockString",t.COMMENT="Comment"})(A||(A={}));class uy{constructor(s){const r=new wu(A.SOF,0,0,0,0);this.source=s,this.lastToken=r,this.token=r,this.line=1,this.lineStart=0}get[Symbol.toStringTag](){return"Lexer"}advance(){return this.lastToken=this.token,this.token=this.lookahead()}lookahead(){let s=this.token;if(s.kind!==A.EOF)do if(s.next)s=s.next;else{const r=fy(this,s.end);s.next=r,r.prev=s,s=r}while(s.kind===A.COMMENT);return s}}function cy(t){return t===A.BANG||t===A.DOLLAR||t===A.AMP||t===A.PAREN_L||t===A.PAREN_R||t===A.SPREAD||t===A.COLON||t===A.EQUALS||t===A.AT||t===A.BRACKET_L||t===A.BRACKET_R||t===A.BRACE_L||t===A.PIPE||t===A.BRACE_R}function Kt(t){return t>=0&&t<=55295||t>=57344&&t<=1114111}function Dr(t,s){return Au(t.charCodeAt(s))&&Ou(t.charCodeAt(s+1))}function Au(t){return t>=55296&&t<=56319}function Ou(t){return t>=56320&&t<=57343}function Dt(t,s){const r=t.source.body.codePointAt(s);if(r===void 0)return A.EOF;if(r>=32&&r<=126){const o=String.fromCodePoint(r);return o==='"'?`'"'`:`"${o}"`}return"U+"+r.toString(16).toUpperCase().padStart(4,"0")}function Ae(t,s,r,o,l){const h=t.line,g=1+r-t.lineStart;return new wu(s,r,o,h,g,l)}function fy(t,s){const r=t.source.body,o=r.length;let l=s;for(;l<o;){const h=r.charCodeAt(l);switch(h){case 65279:case 9:case 32:case 44:++l;continue;case 10:++l,++t.line,t.lineStart=l;continue;case 13:r.charCodeAt(l+1)===10?l+=2:++l,++t.line,t.lineStart=l;continue;case 35:return hy(t,l);case 33:return Ae(t,A.BANG,l,l+1);case 36:return Ae(t,A.DOLLAR,l,l+1);case 38:return Ae(t,A.AMP,l,l+1);case 40:return Ae(t,A.PAREN_L,l,l+1);case 41:return Ae(t,A.PAREN_R,l,l+1);case 46:if(r.charCodeAt(l+1)===46&&r.charCodeAt(l+2)===46)return Ae(t,A.SPREAD,l,l+3);break;case 58:return Ae(t,A.COLON,l,l+1);case 61:return Ae(t,A.EQUALS,l,l+1);case 64:return Ae(t,A.AT,l,l+1);case 91:return Ae(t,A.BRACKET_L,l,l+1);case 93:return Ae(t,A.BRACKET_R,l,l+1);case 123:return Ae(t,A.BRACE_L,l,l+1);case 124:return Ae(t,A.PIPE,l,l+1);case 125:return Ae(t,A.BRACE_R,l,l+1);case 34:return r.charCodeAt(l+1)===34&&r.charCodeAt(l+2)===34?yy(t,l):py(t,l)}if(Ni(h)||h===45)return dy(t,l,h);if(ya(h))return Ty(t,l);throw Le(t.source,l,h===39?`Unexpected single quote character ('), did you mean to use a double quote (")?`:Kt(h)||Dr(r,l)?`Unexpected character: ${Dt(t,l)}.`:`Invalid character: ${Dt(t,l)}.`)}return Ae(t,A.EOF,o,o)}function hy(t,s){const r=t.source.body,o=r.length;let l=s+1;for(;l<o;){const h=r.charCodeAt(l);if(h===10||h===13)break;if(Kt(h))++l;else if(Dr(r,l))l+=2;else break}return Ae(t,A.COMMENT,s,l,r.slice(s+1,l))}function dy(t,s,r){const o=t.source.body;let l=s,h=r,g=!1;if(h===45&&(h=o.charCodeAt(++l)),h===48){if(h=o.charCodeAt(++l),Ni(h))throw Le(t.source,l,`Invalid number, unexpected digit after 0: ${Dt(t,l)}.`)}else l=sa(t,l,h),h=o.charCodeAt(l);if(h===46&&(g=!0,h=o.charCodeAt(++l),l=sa(t,l,h),h=o.charCodeAt(l)),(h===69||h===101)&&(g=!0,h=o.charCodeAt(++l),(h===43||h===45)&&(h=o.charCodeAt(++l)),l=sa(t,l,h),h=o.charCodeAt(l)),h===46||ya(h))throw Le(t.source,l,`Invalid number, expected digit but got: ${Dt(t,l)}.`);return Ae(t,g?A.FLOAT:A.INT,s,l,o.slice(s,l))}function sa(t,s,r){if(!Ni(r))throw Le(t.source,s,`Invalid number, expected digit but got: ${Dt(t,s)}.`);const o=t.source.body;let l=s+1;for(;Ni(o.charCodeAt(l));)++l;return l}function py(t,s){const r=t.source.body,o=r.length;let l=s+1,h=l,g="";for(;l<o;){const T=r.charCodeAt(l);if(T===34)return g+=r.slice(h,l),Ae(t,A.STRING,s,l+1,g);if(T===92){g+=r.slice(h,l);const I=r.charCodeAt(l+1)===117?r.charCodeAt(l+2)===123?gy(t,l):my(t,l):vy(t,l);g+=I.value,l+=I.size,h=l;continue}if(T===10||T===13)break;if(Kt(T))++l;else if(Dr(r,l))l+=2;else throw Le(t.source,l,`Invalid character within String: ${Dt(t,l)}.`)}throw Le(t.source,l,"Unterminated string.")}function gy(t,s){const r=t.source.body;let o=0,l=3;for(;l<12;){const h=r.charCodeAt(s+l++);if(h===125){if(l<5||!Kt(o))break;return{value:String.fromCodePoint(o),size:l}}if(o=o<<4|bi(h),o<0)break}throw Le(t.source,s,`Invalid Unicode escape sequence: "${r.slice(s,s+l)}".`)}function my(t,s){const r=t.source.body,o=lu(r,s+2);if(Kt(o))return{value:String.fromCodePoint(o),size:6};if(Au(o)&&r.charCodeAt(s+6)===92&&r.charCodeAt(s+7)===117){const l=lu(r,s+8);if(Ou(l))return{value:String.fromCodePoint(o,l),size:12}}throw Le(t.source,s,`Invalid Unicode escape sequence: "${r.slice(s,s+6)}".`)}function lu(t,s){return bi(t.charCodeAt(s))<<12|bi(t.charCodeAt(s+1))<<8|bi(t.charCodeAt(s+2))<<4|bi(t.charCodeAt(s+3))}function bi(t){return t>=48&&t<=57?t-48:t>=65&&t<=70?t-55:t>=97&&t<=102?t-87:-1}function vy(t,s){const r=t.source.body;switch(r.charCodeAt(s+1)){case 34:return{value:'"',size:2};case 92:return{value:"\\",size:2};case 47:return{value:"/",size:2};case 98:return{value:"\b",size:2};case 102:return{value:"\f",size:2};case 110:return{value:`
`,size:2};case 114:return{value:"\r",size:2};case 116:return{value:"	",size:2}}throw Le(t.source,s,`Invalid character escape sequence: "${r.slice(s,s+2)}".`)}function yy(t,s){const r=t.source.body,o=r.length;let l=t.lineStart,h=s+3,g=h,T="";const I=[];for(;h<o;){const O=r.charCodeAt(h);if(O===34&&r.charCodeAt(h+1)===34&&r.charCodeAt(h+2)===34){T+=r.slice(g,h),I.push(T);const _=Ae(t,A.BLOCK_STRING,s,h+3,ay(I).join(`
`));return t.line+=I.length-1,t.lineStart=l,_}if(O===92&&r.charCodeAt(h+1)===34&&r.charCodeAt(h+2)===34&&r.charCodeAt(h+3)===34){T+=r.slice(g,h),g=h+1,h+=4;continue}if(O===10||O===13){T+=r.slice(g,h),I.push(T),O===13&&r.charCodeAt(h+1)===10?h+=2:++h,T="",g=h,l=h;continue}if(Kt(O))++h;else if(Dr(r,h))h+=2;else throw Le(t.source,h,`Invalid character within String: ${Dt(t,h)}.`)}throw Le(t.source,h,"Unterminated string.")}function Ty(t,s){const r=t.source.body,o=r.length;let l=s+1;for(;l<o;){const h=r.charCodeAt(l);if(Nu(h))++l;else break}return Ae(t,A.NAME,s,l,r.slice(s,l))}const Iy=10,Ru=2;function W(t){return xr(t,[])}function xr(t,s){switch(typeof t){case"string":return JSON.stringify(t);case"function":return t.name?`[function ${t.name}]`:"[function]";case"object":return Ey(t,s);default:return String(t)}}function Ey(t,s){if(t===null)return"null";if(s.includes(t))return"[Circular]";const r=[...s,t];if(by(t)){const o=t.toJSON();if(o!==t)return typeof o=="string"?o:xr(o,r)}else if(Array.isArray(t))return _y(t,r);return wy(t,r)}function by(t){return typeof t.toJSON=="function"}function wy(t,s){const r=Object.entries(t);return r.length===0?"{}":s.length>Ru?"["+Sy(t)+"]":"{ "+r.map(([l,h])=>l+": "+xr(h,s)).join(", ")+" }"}function _y(t,s){if(t.length===0)return"[]";if(s.length>Ru)return"[Array]";const r=Math.min(Iy,t.length),o=t.length-r,l=[];for(let h=0;h<r;++h)l.push(xr(t[h],s));return o===1?l.push("... 1 more item"):o>1&&l.push(`... ${o} more items`),"["+l.join(", ")+"]"}function Sy(t){const s=Object.prototype.toString.call(t).replace(/^\[object /,"").replace(/]$/,"");if(s==="Object"&&typeof t.constructor=="function"){const r=t.constructor.name;if(typeof r=="string"&&r!=="")return r}return s}const Wn=function(s,r){return s instanceof r};class Du{constructor(s,r="GraphQL request",o={line:1,column:1}){typeof s=="string"||ae(!1,`Body must be a string. Received: ${W(s)}.`),this.body=s,this.name=r,this.locationOffset=o,this.locationOffset.line>0||ae(!1,"line in locationOffset is 1-indexed and must be positive."),this.locationOffset.column>0||ae(!1,"column in locationOffset is 1-indexed and must be positive.")}get[Symbol.toStringTag](){return"Source"}}function Ny(t){return Wn(t,Du)}function Ay(t,s){return new xu(t,s).parseDocument()}function Oy(t,s){const r=new xu(t,s);r.expectToken(A.SOF);const o=r.parseValueLiteral(!1);return r.expectToken(A.EOF),o}class xu{constructor(s,r={}){const o=Ny(s)?s:new Du(s);this._lexer=new uy(o),this._options=r,this._tokenCounter=0}parseName(){const s=this.expectToken(A.NAME);return this.node(s,{kind:y.NAME,value:s.value})}parseDocument(){return this.node(this._lexer.token,{kind:y.DOCUMENT,definitions:this.many(A.SOF,this.parseDefinition,A.EOF)})}parseDefinition(){if(this.peek(A.BRACE_L))return this.parseOperationDefinition();const s=this.peekDescription(),r=s?this._lexer.lookahead():this._lexer.token;if(r.kind===A.NAME){switch(r.value){case"schema":return this.parseSchemaDefinition();case"scalar":return this.parseScalarTypeDefinition();case"type":return this.parseObjectTypeDefinition();case"interface":return this.parseInterfaceTypeDefinition();case"union":return this.parseUnionTypeDefinition();case"enum":return this.parseEnumTypeDefinition();case"input":return this.parseInputObjectTypeDefinition();case"directive":return this.parseDirectiveDefinition()}if(s)throw Le(this._lexer.source,this._lexer.token.start,"Unexpected description, descriptions are supported only on type definitions.");switch(r.value){case"query":case"mutation":case"subscription":return this.parseOperationDefinition();case"fragment":return this.parseFragmentDefinition();case"extend":return this.parseTypeSystemExtension()}}throw this.unexpected(r)}parseOperationDefinition(){const s=this._lexer.token;if(this.peek(A.BRACE_L))return this.node(s,{kind:y.OPERATION_DEFINITION,operation:vn.QUERY,name:void 0,variableDefinitions:[],directives:[],selectionSet:this.parseSelectionSet()});const r=this.parseOperationType();let o;return this.peek(A.NAME)&&(o=this.parseName()),this.node(s,{kind:y.OPERATION_DEFINITION,operation:r,name:o,variableDefinitions:this.parseVariableDefinitions(),directives:this.parseDirectives(!1),selectionSet:this.parseSelectionSet()})}parseOperationType(){const s=this.expectToken(A.NAME);switch(s.value){case"query":return vn.QUERY;case"mutation":return vn.MUTATION;case"subscription":return vn.SUBSCRIPTION}throw this.unexpected(s)}parseVariableDefinitions(){return this.optionalMany(A.PAREN_L,this.parseVariableDefinition,A.PAREN_R)}parseVariableDefinition(){return this.node(this._lexer.token,{kind:y.VARIABLE_DEFINITION,variable:this.parseVariable(),type:(this.expectToken(A.COLON),this.parseTypeReference()),defaultValue:this.expectOptionalToken(A.EQUALS)?this.parseConstValueLiteral():void 0,directives:this.parseConstDirectives()})}parseVariable(){const s=this._lexer.token;return this.expectToken(A.DOLLAR),this.node(s,{kind:y.VARIABLE,name:this.parseName()})}parseSelectionSet(){return this.node(this._lexer.token,{kind:y.SELECTION_SET,selections:this.many(A.BRACE_L,this.parseSelection,A.BRACE_R)})}parseSelection(){return this.peek(A.SPREAD)?this.parseFragment():this.parseField()}parseField(){const s=this._lexer.token,r=this.parseName();let o,l;return this.expectOptionalToken(A.COLON)?(o=r,l=this.parseName()):l=r,this.node(s,{kind:y.FIELD,alias:o,name:l,arguments:this.parseArguments(!1),directives:this.parseDirectives(!1),selectionSet:this.peek(A.BRACE_L)?this.parseSelectionSet():void 0})}parseArguments(s){const r=s?this.parseConstArgument:this.parseArgument;return this.optionalMany(A.PAREN_L,r,A.PAREN_R)}parseArgument(s=!1){const r=this._lexer.token,o=this.parseName();return this.expectToken(A.COLON),this.node(r,{kind:y.ARGUMENT,name:o,value:this.parseValueLiteral(s)})}parseConstArgument(){return this.parseArgument(!0)}parseFragment(){const s=this._lexer.token;this.expectToken(A.SPREAD);const r=this.expectOptionalKeyword("on");return!r&&this.peek(A.NAME)?this.node(s,{kind:y.FRAGMENT_SPREAD,name:this.parseFragmentName(),directives:this.parseDirectives(!1)}):this.node(s,{kind:y.INLINE_FRAGMENT,typeCondition:r?this.parseNamedType():void 0,directives:this.parseDirectives(!1),selectionSet:this.parseSelectionSet()})}parseFragmentDefinition(){const s=this._lexer.token;return this.expectKeyword("fragment"),this._options.allowLegacyFragmentVariables===!0?this.node(s,{kind:y.FRAGMENT_DEFINITION,name:this.parseFragmentName(),variableDefinitions:this.parseVariableDefinitions(),typeCondition:(this.expectKeyword("on"),this.parseNamedType()),directives:this.parseDirectives(!1),selectionSet:this.parseSelectionSet()}):this.node(s,{kind:y.FRAGMENT_DEFINITION,name:this.parseFragmentName(),typeCondition:(this.expectKeyword("on"),this.parseNamedType()),directives:this.parseDirectives(!1),selectionSet:this.parseSelectionSet()})}parseFragmentName(){if(this._lexer.token.value==="on")throw this.unexpected();return this.parseName()}parseValueLiteral(s){const r=this._lexer.token;switch(r.kind){case A.BRACKET_L:return this.parseList(s);case A.BRACE_L:return this.parseObject(s);case A.INT:return this.advanceLexer(),this.node(r,{kind:y.INT,value:r.value});case A.FLOAT:return this.advanceLexer(),this.node(r,{kind:y.FLOAT,value:r.value});case A.STRING:case A.BLOCK_STRING:return this.parseStringLiteral();case A.NAME:switch(this.advanceLexer(),r.value){case"true":return this.node(r,{kind:y.BOOLEAN,value:!0});case"false":return this.node(r,{kind:y.BOOLEAN,value:!1});case"null":return this.node(r,{kind:y.NULL});default:return this.node(r,{kind:y.ENUM,value:r.value})}case A.DOLLAR:if(s)if(this.expectToken(A.DOLLAR),this._lexer.token.kind===A.NAME){const o=this._lexer.token.value;throw Le(this._lexer.source,r.start,`Unexpected variable "$${o}" in constant value.`)}else throw this.unexpected(r);return this.parseVariable();default:throw this.unexpected()}}parseConstValueLiteral(){return this.parseValueLiteral(!0)}parseStringLiteral(){const s=this._lexer.token;return this.advanceLexer(),this.node(s,{kind:y.STRING,value:s.value,block:s.kind===A.BLOCK_STRING})}parseList(s){const r=()=>this.parseValueLiteral(s);return this.node(this._lexer.token,{kind:y.LIST,values:this.any(A.BRACKET_L,r,A.BRACKET_R)})}parseObject(s){const r=()=>this.parseObjectField(s);return this.node(this._lexer.token,{kind:y.OBJECT,fields:this.any(A.BRACE_L,r,A.BRACE_R)})}parseObjectField(s){const r=this._lexer.token,o=this.parseName();return this.expectToken(A.COLON),this.node(r,{kind:y.OBJECT_FIELD,name:o,value:this.parseValueLiteral(s)})}parseDirectives(s){const r=[];for(;this.peek(A.AT);)r.push(this.parseDirective(s));return r}parseConstDirectives(){return this.parseDirectives(!0)}parseDirective(s){const r=this._lexer.token;return this.expectToken(A.AT),this.node(r,{kind:y.DIRECTIVE,name:this.parseName(),arguments:this.parseArguments(s)})}parseTypeReference(){const s=this._lexer.token;let r;if(this.expectOptionalToken(A.BRACKET_L)){const o=this.parseTypeReference();this.expectToken(A.BRACKET_R),r=this.node(s,{kind:y.LIST_TYPE,type:o})}else r=this.parseNamedType();return this.expectOptionalToken(A.BANG)?this.node(s,{kind:y.NON_NULL_TYPE,type:r}):r}parseNamedType(){return this.node(this._lexer.token,{kind:y.NAMED_TYPE,name:this.parseName()})}peekDescription(){return this.peek(A.STRING)||this.peek(A.BLOCK_STRING)}parseDescription(){if(this.peekDescription())return this.parseStringLiteral()}parseSchemaDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("schema");const o=this.parseConstDirectives(),l=this.many(A.BRACE_L,this.parseOperationTypeDefinition,A.BRACE_R);return this.node(s,{kind:y.SCHEMA_DEFINITION,description:r,directives:o,operationTypes:l})}parseOperationTypeDefinition(){const s=this._lexer.token,r=this.parseOperationType();this.expectToken(A.COLON);const o=this.parseNamedType();return this.node(s,{kind:y.OPERATION_TYPE_DEFINITION,operation:r,type:o})}parseScalarTypeDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("scalar");const o=this.parseName(),l=this.parseConstDirectives();return this.node(s,{kind:y.SCALAR_TYPE_DEFINITION,description:r,name:o,directives:l})}parseObjectTypeDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("type");const o=this.parseName(),l=this.parseImplementsInterfaces(),h=this.parseConstDirectives(),g=this.parseFieldsDefinition();return this.node(s,{kind:y.OBJECT_TYPE_DEFINITION,description:r,name:o,interfaces:l,directives:h,fields:g})}parseImplementsInterfaces(){return this.expectOptionalKeyword("implements")?this.delimitedMany(A.AMP,this.parseNamedType):[]}parseFieldsDefinition(){return this.optionalMany(A.BRACE_L,this.parseFieldDefinition,A.BRACE_R)}parseFieldDefinition(){const s=this._lexer.token,r=this.parseDescription(),o=this.parseName(),l=this.parseArgumentDefs();this.expectToken(A.COLON);const h=this.parseTypeReference(),g=this.parseConstDirectives();return this.node(s,{kind:y.FIELD_DEFINITION,description:r,name:o,arguments:l,type:h,directives:g})}parseArgumentDefs(){return this.optionalMany(A.PAREN_L,this.parseInputValueDef,A.PAREN_R)}parseInputValueDef(){const s=this._lexer.token,r=this.parseDescription(),o=this.parseName();this.expectToken(A.COLON);const l=this.parseTypeReference();let h;this.expectOptionalToken(A.EQUALS)&&(h=this.parseConstValueLiteral());const g=this.parseConstDirectives();return this.node(s,{kind:y.INPUT_VALUE_DEFINITION,description:r,name:o,type:l,defaultValue:h,directives:g})}parseInterfaceTypeDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("interface");const o=this.parseName(),l=this.parseImplementsInterfaces(),h=this.parseConstDirectives(),g=this.parseFieldsDefinition();return this.node(s,{kind:y.INTERFACE_TYPE_DEFINITION,description:r,name:o,interfaces:l,directives:h,fields:g})}parseUnionTypeDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("union");const o=this.parseName(),l=this.parseConstDirectives(),h=this.parseUnionMemberTypes();return this.node(s,{kind:y.UNION_TYPE_DEFINITION,description:r,name:o,directives:l,types:h})}parseUnionMemberTypes(){return this.expectOptionalToken(A.EQUALS)?this.delimitedMany(A.PIPE,this.parseNamedType):[]}parseEnumTypeDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("enum");const o=this.parseName(),l=this.parseConstDirectives(),h=this.parseEnumValuesDefinition();return this.node(s,{kind:y.ENUM_TYPE_DEFINITION,description:r,name:o,directives:l,values:h})}parseEnumValuesDefinition(){return this.optionalMany(A.BRACE_L,this.parseEnumValueDefinition,A.BRACE_R)}parseEnumValueDefinition(){const s=this._lexer.token,r=this.parseDescription(),o=this.parseEnumValueName(),l=this.parseConstDirectives();return this.node(s,{kind:y.ENUM_VALUE_DEFINITION,description:r,name:o,directives:l})}parseEnumValueName(){if(this._lexer.token.value==="true"||this._lexer.token.value==="false"||this._lexer.token.value==="null")throw Le(this._lexer.source,this._lexer.token.start,`${br(this._lexer.token)} is reserved and cannot be used for an enum value.`);return this.parseName()}parseInputObjectTypeDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("input");const o=this.parseName(),l=this.parseConstDirectives(),h=this.parseInputFieldsDefinition();return this.node(s,{kind:y.INPUT_OBJECT_TYPE_DEFINITION,description:r,name:o,directives:l,fields:h})}parseInputFieldsDefinition(){return this.optionalMany(A.BRACE_L,this.parseInputValueDef,A.BRACE_R)}parseTypeSystemExtension(){const s=this._lexer.lookahead();if(s.kind===A.NAME)switch(s.value){case"schema":return this.parseSchemaExtension();case"scalar":return this.parseScalarTypeExtension();case"type":return this.parseObjectTypeExtension();case"interface":return this.parseInterfaceTypeExtension();case"union":return this.parseUnionTypeExtension();case"enum":return this.parseEnumTypeExtension();case"input":return this.parseInputObjectTypeExtension()}throw this.unexpected(s)}parseSchemaExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("schema");const r=this.parseConstDirectives(),o=this.optionalMany(A.BRACE_L,this.parseOperationTypeDefinition,A.BRACE_R);if(r.length===0&&o.length===0)throw this.unexpected();return this.node(s,{kind:y.SCHEMA_EXTENSION,directives:r,operationTypes:o})}parseScalarTypeExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("scalar");const r=this.parseName(),o=this.parseConstDirectives();if(o.length===0)throw this.unexpected();return this.node(s,{kind:y.SCALAR_TYPE_EXTENSION,name:r,directives:o})}parseObjectTypeExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("type");const r=this.parseName(),o=this.parseImplementsInterfaces(),l=this.parseConstDirectives(),h=this.parseFieldsDefinition();if(o.length===0&&l.length===0&&h.length===0)throw this.unexpected();return this.node(s,{kind:y.OBJECT_TYPE_EXTENSION,name:r,interfaces:o,directives:l,fields:h})}parseInterfaceTypeExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("interface");const r=this.parseName(),o=this.parseImplementsInterfaces(),l=this.parseConstDirectives(),h=this.parseFieldsDefinition();if(o.length===0&&l.length===0&&h.length===0)throw this.unexpected();return this.node(s,{kind:y.INTERFACE_TYPE_EXTENSION,name:r,interfaces:o,directives:l,fields:h})}parseUnionTypeExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("union");const r=this.parseName(),o=this.parseConstDirectives(),l=this.parseUnionMemberTypes();if(o.length===0&&l.length===0)throw this.unexpected();return this.node(s,{kind:y.UNION_TYPE_EXTENSION,name:r,directives:o,types:l})}parseEnumTypeExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("enum");const r=this.parseName(),o=this.parseConstDirectives(),l=this.parseEnumValuesDefinition();if(o.length===0&&l.length===0)throw this.unexpected();return this.node(s,{kind:y.ENUM_TYPE_EXTENSION,name:r,directives:o,values:l})}parseInputObjectTypeExtension(){const s=this._lexer.token;this.expectKeyword("extend"),this.expectKeyword("input");const r=this.parseName(),o=this.parseConstDirectives(),l=this.parseInputFieldsDefinition();if(o.length===0&&l.length===0)throw this.unexpected();return this.node(s,{kind:y.INPUT_OBJECT_TYPE_EXTENSION,name:r,directives:o,fields:l})}parseDirectiveDefinition(){const s=this._lexer.token,r=this.parseDescription();this.expectKeyword("directive"),this.expectToken(A.AT);const o=this.parseName(),l=this.parseArgumentDefs(),h=this.expectOptionalKeyword("repeatable");this.expectKeyword("on");const g=this.parseDirectiveLocations();return this.node(s,{kind:y.DIRECTIVE_DEFINITION,description:r,name:o,arguments:l,repeatable:h,locations:g})}parseDirectiveLocations(){return this.delimitedMany(A.PIPE,this.parseDirectiveLocation)}parseDirectiveLocation(){const s=this._lexer.token,r=this.parseName();if(Object.prototype.hasOwnProperty.call(Y,r.value))return r;throw this.unexpected(s)}node(s,r){return this._options.noLocation!==!0&&(r.loc=new ry(s,this._lexer.lastToken,this._lexer.source)),r}peek(s){return this._lexer.token.kind===s}expectToken(s){const r=this._lexer.token;if(r.kind===s)return this.advanceLexer(),r;throw Le(this._lexer.source,r.start,`Expected ${Lu(s)}, found ${br(r)}.`)}expectOptionalToken(s){return this._lexer.token.kind===s?(this.advanceLexer(),!0):!1}expectKeyword(s){const r=this._lexer.token;if(r.kind===A.NAME&&r.value===s)this.advanceLexer();else throw Le(this._lexer.source,r.start,`Expected "${s}", found ${br(r)}.`)}expectOptionalKeyword(s){const r=this._lexer.token;return r.kind===A.NAME&&r.value===s?(this.advanceLexer(),!0):!1}unexpected(s){const r=s??this._lexer.token;return Le(this._lexer.source,r.start,`Unexpected ${br(r)}.`)}any(s,r,o){this.expectToken(s);const l=[];for(;!this.expectOptionalToken(o);)l.push(r.call(this));return l}optionalMany(s,r,o){if(this.expectOptionalToken(s)){const l=[];do l.push(r.call(this));while(!this.expectOptionalToken(o));return l}return[]}many(s,r,o){this.expectToken(s);const l=[];do l.push(r.call(this));while(!this.expectOptionalToken(o));return l}delimitedMany(s,r){this.expectOptionalToken(s);const o=[];do o.push(r.call(this));while(this.expectOptionalToken(s));return o}advanceLexer(){const{maxTokens:s}=this._options,r=this._lexer.advance();if(s!==void 0&&r.kind!==A.EOF&&(++this._tokenCounter,this._tokenCounter>s))throw Le(this._lexer.source,r.start,`Document contains more that ${s} tokens. Parsing aborted.`)}}function br(t){const s=t.value;return Lu(t.kind)+(s!=null?` "${s}"`:"")}function Lu(t){return cy(t)?`"${t}"`:t}const Ry=5;function Lr(t,s){const[r,o]=s?[t,s]:[void 0,t];let l=" Did you mean ";r&&(l+=r+" ");const h=o.map(I=>`"${I}"`);switch(h.length){case 0:return"";case 1:return l+h[0]+"?";case 2:return l+h[0]+" or "+h[1]+"?"}const g=h.slice(0,Ry),T=g.pop();return l+g.join(", ")+", or "+T+"?"}function uu(t){return t}function zt(t,s){const r=Object.create(null);for(const o of t)r[s(o)]=o;return r}function Rt(t,s,r){const o=Object.create(null);for(const l of t)o[s(l)]=r(l);return o}function nt(t,s){const r=Object.create(null);for(const o of Object.keys(t))r[o]=s(t[o],o);return r}function Dy(t,s){let r=0,o=0;for(;r<t.length&&o<s.length;){let l=t.charCodeAt(r),h=s.charCodeAt(o);if(wr(l)&&wr(h)){let g=0;do++r,g=g*10+l-ha,l=t.charCodeAt(r);while(wr(l)&&g>0);let T=0;do++o,T=T*10+h-ha,h=s.charCodeAt(o);while(wr(h)&&T>0);if(g<T)return-1;if(g>T)return 1}else{if(l<h)return-1;if(l>h)return 1;++r,++o}}return t.length-s.length}const ha=48,xy=57;function wr(t){return!isNaN(t)&&ha<=t&&t<=xy}function kr(t,s){const r=Object.create(null),o=new Ly(t),l=Math.floor(t.length*.4)+1;for(const h of s){const g=o.measure(h,l);g!==void 0&&(r[h]=g)}return Object.keys(r).sort((h,g)=>{const T=r[h]-r[g];return T!==0?T:Dy(h,g)})}class Ly{constructor(s){this._input=s,this._inputLowerCase=s.toLowerCase(),this._inputArray=cu(this._inputLowerCase),this._rows=[new Array(s.length+1).fill(0),new Array(s.length+1).fill(0),new Array(s.length+1).fill(0)]}measure(s,r){if(this._input===s)return 0;const o=s.toLowerCase();if(this._inputLowerCase===o)return 1;let l=cu(o),h=this._inputArray;if(l.length<h.length){const _=l;l=h,h=_}const g=l.length,T=h.length;if(g-T>r)return;const I=this._rows;for(let _=0;_<=T;_++)I[0][_]=_;for(let _=1;_<=g;_++){const x=I[(_-1)%3],B=I[_%3];let K=B[0]=_;for(let V=1;V<=T;V++){const he=l[_-1]===h[V-1]?0:1;let se=Math.min(x[V]+1,B[V-1]+1,x[V-1]+he);if(_>1&&V>1&&l[_-1]===h[V-2]&&l[_-2]===h[V-1]){const ye=I[(_-2)%3][V-2];se=Math.min(se,ye+1)}se<K&&(K=se),B[V]=se}if(K>r)return}const O=I[g%3][T];return O<=r?O:void 0}}function cu(t){const s=t.length,r=new Array(s);for(let o=0;o<s;++o)r[o]=t.charCodeAt(o);return r}function In(t){if(t==null)return Object.create(null);if(Object.getPrototypeOf(t)===null)return t;const s=Object.create(null);for(const[r,o]of Object.entries(t))s[r]=o;return s}function ky(t){return`"${t.replace(Cy,Fy)}"`}const Cy=/[\x00-\x1f\x22\x5c\x7f-\x9f]/g;function Fy(t){return Py[t.charCodeAt(0)]}const Py=["\\u0000","\\u0001","\\u0002","\\u0003","\\u0004","\\u0005","\\u0006","\\u0007","\\b","\\t","\\n","\\u000B","\\f","\\r","\\u000E","\\u000F","\\u0010","\\u0011","\\u0012","\\u0013","\\u0014","\\u0015","\\u0016","\\u0017","\\u0018","\\u0019","\\u001A","\\u001B","\\u001C","\\u001D","\\u001E","\\u001F","","",'\\"',"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","\\\\","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","\\u007F","\\u0080","\\u0081","\\u0082","\\u0083","\\u0084","\\u0085","\\u0086","\\u0087","\\u0088","\\u0089","\\u008A","\\u008B","\\u008C","\\u008D","\\u008E","\\u008F","\\u0090","\\u0091","\\u0092","\\u0093","\\u0094","\\u0095","\\u0096","\\u0097","\\u0098","\\u0099","\\u009A","\\u009B","\\u009C","\\u009D","\\u009E","\\u009F"],wi=Object.freeze({});function ku(t,s,r=_u){const o=new Map;for(const ye of Object.values(y))o.set(ye,Cu(s,ye));let l,h=Array.isArray(t),g=[t],T=-1,I=[],O=t,_,x;const B=[],K=[];do{T++;const ye=T===g.length,Yn=ye&&I.length!==0;if(ye){if(_=K.length===0?void 0:B[B.length-1],O=x,x=K.pop(),Yn)if(h){O=O.slice();let Ne=0;for(const[De,Me]of I){const Fe=De-Ne;Me===null?(O.splice(Fe,1),Ne++):O[Fe]=Me}}else{O=Object.defineProperties({},Object.getOwnPropertyDescriptors(O));for(const[Ne,De]of I)O[Ne]=De}T=l.index,g=l.keys,I=l.edits,h=l.inArray,l=l.prev}else if(x){if(_=h?T:g[T],O=x[_],O==null)continue;B.push(_)}let Te;if(!Array.isArray(O)){var V,he;ou(O)||ae(!1,`Invalid AST Node: ${W(O)}.`);const Ne=ye?(V=o.get(O.kind))===null||V===void 0?void 0:V.leave:(he=o.get(O.kind))===null||he===void 0?void 0:he.enter;if(Te=Ne==null?void 0:Ne.call(s,O,_,x,B,K),Te===wi)break;if(Te===!1){if(!ye){B.pop();continue}}else if(Te!==void 0&&(I.push([_,Te]),!ye))if(ou(Te))O=Te;else{B.pop();continue}}if(Te===void 0&&Yn&&I.push([_,O]),ye)B.pop();else{var se;l={inArray:h,index:T,keys:g,edits:I,prev:l},h=Array.isArray(O),g=h?O:(se=r[O.kind])!==null&&se!==void 0?se:[],T=-1,I=[],x&&K.push(x),x=O}}while(l!==void 0);return I.length!==0?I[I.length-1][1]:t}function Uy(t){const s=new Array(t.length).fill(null),r=Object.create(null);for(const o of Object.values(y)){let l=!1;const h=new Array(t.length).fill(void 0),g=new Array(t.length).fill(void 0);for(let I=0;I<t.length;++I){const{enter:O,leave:_}=Cu(t[I],o);l||(l=O!=null||_!=null),h[I]=O,g[I]=_}if(!l)continue;const T={enter(...I){const O=I[0];for(let x=0;x<t.length;x++)if(s[x]===null){var _;const B=(_=h[x])===null||_===void 0?void 0:_.apply(t[x],I);if(B===!1)s[x]=O;else if(B===wi)s[x]=wi;else if(B!==void 0)return B}},leave(...I){const O=I[0];for(let x=0;x<t.length;x++)if(s[x]===null){var _;const B=(_=g[x])===null||_===void 0?void 0:_.apply(t[x],I);if(B===wi)s[x]=wi;else if(B!==void 0&&B!==!1)return B}else s[x]===O&&(s[x]=null)}};r[o]=T}return r}function Cu(t,s){const r=t[s];return typeof r=="object"?r:typeof r=="function"?{enter:r,leave:void 0}:{enter:t.enter,leave:t.leave}}function Gn(t){return ku(t,My)}const By=80,My={Name:{leave:t=>t.value},Variable:{leave:t=>"$"+t.name},Document:{leave:t=>U(t.definitions,`

`)},OperationDefinition:{leave(t){const s=ie("(",U(t.variableDefinitions,", "),")"),r=U([t.operation,U([t.name,s]),U(t.directives," ")]," ");return(r==="query"?"":r+" ")+t.selectionSet}},VariableDefinition:{leave:({variable:t,type:s,defaultValue:r,directives:o})=>t+": "+s+ie(" = ",r)+ie(" ",U(o," "))},SelectionSet:{leave:({selections:t})=>On(t)},Field:{leave({alias:t,name:s,arguments:r,directives:o,selectionSet:l}){const h=ie("",t,": ")+s;let g=h+ie("(",U(r,", "),")");return g.length>By&&(g=h+ie(`(
`,Nr(U(r,`
`)),`
)`)),U([g,U(o," "),l]," ")}},Argument:{leave:({name:t,value:s})=>t+": "+s},FragmentSpread:{leave:({name:t,directives:s})=>"..."+t+ie(" ",U(s," "))},InlineFragment:{leave:({typeCondition:t,directives:s,selectionSet:r})=>U(["...",ie("on ",t),U(s," "),r]," ")},FragmentDefinition:{leave:({name:t,typeCondition:s,variableDefinitions:r,directives:o,selectionSet:l})=>`fragment ${t}${ie("(",U(r,", "),")")} on ${s} ${ie("",U(o," ")," ")}`+l},IntValue:{leave:({value:t})=>t},FloatValue:{leave:({value:t})=>t},StringValue:{leave:({value:t,block:s})=>s?ly(t):ky(t)},BooleanValue:{leave:({value:t})=>t?"true":"false"},NullValue:{leave:()=>"null"},EnumValue:{leave:({value:t})=>t},ListValue:{leave:({values:t})=>"["+U(t,", ")+"]"},ObjectValue:{leave:({fields:t})=>"{"+U(t,", ")+"}"},ObjectField:{leave:({name:t,value:s})=>t+": "+s},Directive:{leave:({name:t,arguments:s})=>"@"+t+ie("(",U(s,", "),")")},NamedType:{leave:({name:t})=>t},ListType:{leave:({type:t})=>"["+t+"]"},NonNullType:{leave:({type:t})=>t+"!"},SchemaDefinition:{leave:({description:t,directives:s,operationTypes:r})=>ie("",t,`
`)+U(["schema",U(s," "),On(r)]," ")},OperationTypeDefinition:{leave:({operation:t,type:s})=>t+": "+s},ScalarTypeDefinition:{leave:({description:t,name:s,directives:r})=>ie("",t,`
`)+U(["scalar",s,U(r," ")]," ")},ObjectTypeDefinition:{leave:({description:t,name:s,interfaces:r,directives:o,fields:l})=>ie("",t,`
`)+U(["type",s,ie("implements ",U(r," & ")),U(o," "),On(l)]," ")},FieldDefinition:{leave:({description:t,name:s,arguments:r,type:o,directives:l})=>ie("",t,`
`)+s+(fu(r)?ie(`(
`,Nr(U(r,`
`)),`
)`):ie("(",U(r,", "),")"))+": "+o+ie(" ",U(l," "))},InputValueDefinition:{leave:({description:t,name:s,type:r,defaultValue:o,directives:l})=>ie("",t,`
`)+U([s+": "+r,ie("= ",o),U(l," ")]," ")},InterfaceTypeDefinition:{leave:({description:t,name:s,interfaces:r,directives:o,fields:l})=>ie("",t,`
`)+U(["interface",s,ie("implements ",U(r," & ")),U(o," "),On(l)]," ")},UnionTypeDefinition:{leave:({description:t,name:s,directives:r,types:o})=>ie("",t,`
`)+U(["union",s,U(r," "),ie("= ",U(o," | "))]," ")},EnumTypeDefinition:{leave:({description:t,name:s,directives:r,values:o})=>ie("",t,`
`)+U(["enum",s,U(r," "),On(o)]," ")},EnumValueDefinition:{leave:({description:t,name:s,directives:r})=>ie("",t,`
`)+U([s,U(r," ")]," ")},InputObjectTypeDefinition:{leave:({description:t,name:s,directives:r,fields:o})=>ie("",t,`
`)+U(["input",s,U(r," "),On(o)]," ")},DirectiveDefinition:{leave:({description:t,name:s,arguments:r,repeatable:o,locations:l})=>ie("",t,`
`)+"directive @"+s+(fu(r)?ie(`(
`,Nr(U(r,`
`)),`
)`):ie("(",U(r,", "),")"))+(o?" repeatable":"")+" on "+U(l," | ")},SchemaExtension:{leave:({directives:t,operationTypes:s})=>U(["extend schema",U(t," "),On(s)]," ")},ScalarTypeExtension:{leave:({name:t,directives:s})=>U(["extend scalar",t,U(s," ")]," ")},ObjectTypeExtension:{leave:({name:t,interfaces:s,directives:r,fields:o})=>U(["extend type",t,ie("implements ",U(s," & ")),U(r," "),On(o)]," ")},InterfaceTypeExtension:{leave:({name:t,interfaces:s,directives:r,fields:o})=>U(["extend interface",t,ie("implements ",U(s," & ")),U(r," "),On(o)]," ")},UnionTypeExtension:{leave:({name:t,directives:s,types:r})=>U(["extend union",t,U(s," "),ie("= ",U(r," | "))]," ")},EnumTypeExtension:{leave:({name:t,directives:s,values:r})=>U(["extend enum",t,U(s," "),On(r)]," ")},InputObjectTypeExtension:{leave:({name:t,directives:s,fields:r})=>U(["extend input",t,U(s," "),On(r)]," ")}};function U(t,s=""){var r;return(r=t==null?void 0:t.filter(o=>o).join(s))!==null&&r!==void 0?r:""}function On(t){return ie(`{
`,Nr(U(t,`
`)),`
}`)}function ie(t,s,r=""){return s!=null&&s!==""?t+s+r:""}function Nr(t){return ie("  ",t.replace(/\n/g,`
  `))}function fu(t){var s;return(s=t==null?void 0:t.some(r=>r.includes(`
`)))!==null&&s!==void 0?s:!1}function da(t,s){switch(t.kind){case y.NULL:return null;case y.INT:return parseInt(t.value,10);case y.FLOAT:return parseFloat(t.value);case y.STRING:case y.ENUM:case y.BOOLEAN:return t.value;case y.LIST:return t.values.map(r=>da(r,s));case y.OBJECT:return Rt(t.fields,r=>r.name.value,r=>da(r.value,s));case y.VARIABLE:return s==null?void 0:s[t.name.value]}}function Fn(t){if(t!=null||ae(!1,"Must provide name."),typeof t=="string"||ae(!1,"Expected name to be a string."),t.length===0)throw new G("Expected name to be a non-empty string.");for(let s=1;s<t.length;++s)if(!Nu(t.charCodeAt(s)))throw new G(`Names must only contain [_a-zA-Z0-9] but "${t}" does not.`);if(!ya(t.charCodeAt(0)))throw new G(`Names must start with [_a-zA-Z] but "${t}" does not.`);return t}function Gy(t){if(t==="true"||t==="false"||t==="null")throw new G(`Enum values cannot be named: ${t}`);return Fn(t)}function Ta(t){return vt(t)||Ke(t)||je(t)||Ln(t)||kn(t)||Cn(t)||xt(t)||$e(t)}function vt(t){return Wn(t,it)}function Ke(t){return Wn(t,Vn)}function Vy(t){if(!Ke(t))throw new Error(`Expected ${W(t)} to be a GraphQL Object type.`);return t}function je(t){return Wn(t,Ar)}function Wy(t){if(!je(t))throw new Error(`Expected ${W(t)} to be a GraphQL Interface type.`);return t}function Ln(t){return Wn(t,Or)}function kn(t){return Wn(t,Xt)}function Cn(t){return Wn(t,Rr)}function xt(t){return Wn(t,Xe)}function $e(t){return Wn(t,ne)}function Fu(t){return vt(t)||kn(t)||Cn(t)||Ia(t)&&Fu(t.ofType)}function Pu(t){return vt(t)||Ke(t)||je(t)||Ln(t)||kn(t)||Ia(t)&&Pu(t.ofType)}function Uu(t){return vt(t)||kn(t)}function Yy(t){return je(t)||Ln(t)}class Xe{constructor(s){Ta(s)||ae(!1,`Expected ${W(s)} to be a GraphQL type.`),this.ofType=s}get[Symbol.toStringTag](){return"GraphQLList"}toString(){return"["+String(this.ofType)+"]"}toJSON(){return this.toString()}}class ne{constructor(s){Bu(s)||ae(!1,`Expected ${W(s)} to be a GraphQL nullable type.`),this.ofType=s}get[Symbol.toStringTag](){return"GraphQLNonNull"}toString(){return String(this.ofType)+"!"}toJSON(){return this.toString()}}function Ia(t){return xt(t)||$e(t)}function Bu(t){return Ta(t)&&!$e(t)}function $y(t){if(!Bu(t))throw new Error(`Expected ${W(t)} to be a GraphQL nullable type.`);return t}function uI(t){return vt(t)||Ke(t)||je(t)||Ln(t)||kn(t)||Cn(t)}function jy(t){if(t){let s=t;for(;Ia(s);)s=s.ofType;return s}}function Mu(t){return typeof t=="function"?t():t}function Gu(t){return typeof t=="function"?t():t}class it{constructor(s){var r,o,l,h;const g=(r=s.parseValue)!==null&&r!==void 0?r:uu;this.name=Fn(s.name),this.description=s.description,this.specifiedByURL=s.specifiedByURL,this.serialize=(o=s.serialize)!==null&&o!==void 0?o:uu,this.parseValue=g,this.parseLiteral=(l=s.parseLiteral)!==null&&l!==void 0?l:(T,I)=>g(da(T,I)),this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(h=s.extensionASTNodes)!==null&&h!==void 0?h:[],s.specifiedByURL==null||typeof s.specifiedByURL=="string"||ae(!1,`${this.name} must provide "specifiedByURL" as a string, but got: ${W(s.specifiedByURL)}.`),s.serialize==null||typeof s.serialize=="function"||ae(!1,`${this.name} must provide "serialize" function. If this custom Scalar is also used as an input type, ensure "parseValue" and "parseLiteral" functions are also provided.`),s.parseLiteral&&(typeof s.parseValue=="function"&&typeof s.parseLiteral=="function"||ae(!1,`${this.name} must provide both "parseValue" and "parseLiteral" functions.`))}get[Symbol.toStringTag](){return"GraphQLScalarType"}toConfig(){return{name:this.name,description:this.description,specifiedByURL:this.specifiedByURL,serialize:this.serialize,parseValue:this.parseValue,parseLiteral:this.parseLiteral,extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes}}toString(){return this.name}toJSON(){return this.toString()}}class Vn{constructor(s){var r;this.name=Fn(s.name),this.description=s.description,this.isTypeOf=s.isTypeOf,this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(r=s.extensionASTNodes)!==null&&r!==void 0?r:[],this._fields=()=>Wu(s),this._interfaces=()=>Vu(s),s.isTypeOf==null||typeof s.isTypeOf=="function"||ae(!1,`${this.name} must provide "isTypeOf" as a function, but got: ${W(s.isTypeOf)}.`)}get[Symbol.toStringTag](){return"GraphQLObjectType"}getFields(){return typeof this._fields=="function"&&(this._fields=this._fields()),this._fields}getInterfaces(){return typeof this._interfaces=="function"&&(this._interfaces=this._interfaces()),this._interfaces}toConfig(){return{name:this.name,description:this.description,interfaces:this.getInterfaces(),fields:$u(this.getFields()),isTypeOf:this.isTypeOf,extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes}}toString(){return this.name}toJSON(){return this.toString()}}function Vu(t){var s;const r=Mu((s=t.interfaces)!==null&&s!==void 0?s:[]);return Array.isArray(r)||ae(!1,`${t.name} interfaces must be an Array or a function which returns an Array.`),r}function Wu(t){const s=Gu(t.fields);return Jt(s)||ae(!1,`${t.name} fields must be an object with field names as keys or a function which returns such an object.`),nt(s,(r,o)=>{var l;Jt(r)||ae(!1,`${t.name}.${o} field config must be an object.`),r.resolve==null||typeof r.resolve=="function"||ae(!1,`${t.name}.${o} field resolver must be a function if provided, but got: ${W(r.resolve)}.`);const h=(l=r.args)!==null&&l!==void 0?l:{};return Jt(h)||ae(!1,`${t.name}.${o} args must be an object with argument names as keys.`),{name:Fn(o),description:r.description,type:r.type,args:Yu(h),resolve:r.resolve,subscribe:r.subscribe,deprecationReason:r.deprecationReason,extensions:In(r.extensions),astNode:r.astNode}})}function Yu(t){return Object.entries(t).map(([s,r])=>({name:Fn(s),description:r.description,type:r.type,defaultValue:r.defaultValue,deprecationReason:r.deprecationReason,extensions:In(r.extensions),astNode:r.astNode}))}function Jt(t){return tt(t)&&!Array.isArray(t)}function $u(t){return nt(t,s=>({description:s.description,type:s.type,args:ju(s.args),resolve:s.resolve,subscribe:s.subscribe,deprecationReason:s.deprecationReason,extensions:s.extensions,astNode:s.astNode}))}function ju(t){return Rt(t,s=>s.name,s=>({description:s.description,type:s.type,defaultValue:s.defaultValue,deprecationReason:s.deprecationReason,extensions:s.extensions,astNode:s.astNode}))}function Hy(t){return $e(t.type)&&t.defaultValue===void 0}class Ar{constructor(s){var r;this.name=Fn(s.name),this.description=s.description,this.resolveType=s.resolveType,this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(r=s.extensionASTNodes)!==null&&r!==void 0?r:[],this._fields=Wu.bind(void 0,s),this._interfaces=Vu.bind(void 0,s),s.resolveType==null||typeof s.resolveType=="function"||ae(!1,`${this.name} must provide "resolveType" as a function, but got: ${W(s.resolveType)}.`)}get[Symbol.toStringTag](){return"GraphQLInterfaceType"}getFields(){return typeof this._fields=="function"&&(this._fields=this._fields()),this._fields}getInterfaces(){return typeof this._interfaces=="function"&&(this._interfaces=this._interfaces()),this._interfaces}toConfig(){return{name:this.name,description:this.description,interfaces:this.getInterfaces(),fields:$u(this.getFields()),resolveType:this.resolveType,extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes}}toString(){return this.name}toJSON(){return this.toString()}}class Or{constructor(s){var r;this.name=Fn(s.name),this.description=s.description,this.resolveType=s.resolveType,this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(r=s.extensionASTNodes)!==null&&r!==void 0?r:[],this._types=qy.bind(void 0,s),s.resolveType==null||typeof s.resolveType=="function"||ae(!1,`${this.name} must provide "resolveType" as a function, but got: ${W(s.resolveType)}.`)}get[Symbol.toStringTag](){return"GraphQLUnionType"}getTypes(){return typeof this._types=="function"&&(this._types=this._types()),this._types}toConfig(){return{name:this.name,description:this.description,types:this.getTypes(),resolveType:this.resolveType,extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes}}toString(){return this.name}toJSON(){return this.toString()}}function qy(t){const s=Mu(t.types);return Array.isArray(s)||ae(!1,`Must provide Array of types or a function which returns such an array for Union ${t.name}.`),s}class Xt{constructor(s){var r;this.name=Fn(s.name),this.description=s.description,this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(r=s.extensionASTNodes)!==null&&r!==void 0?r:[],this._values=Qy(this.name,s.values),this._valueLookup=new Map(this._values.map(o=>[o.value,o])),this._nameLookup=zt(this._values,o=>o.name)}get[Symbol.toStringTag](){return"GraphQLEnumType"}getValues(){return this._values}getValue(s){return this._nameLookup[s]}serialize(s){const r=this._valueLookup.get(s);if(r===void 0)throw new G(`Enum "${this.name}" cannot represent value: ${W(s)}`);return r.name}parseValue(s){if(typeof s!="string"){const o=W(s);throw new G(`Enum "${this.name}" cannot represent non-string value: ${o}.`+_r(this,o))}const r=this.getValue(s);if(r==null)throw new G(`Value "${s}" does not exist in "${this.name}" enum.`+_r(this,s));return r.value}parseLiteral(s,r){if(s.kind!==y.ENUM){const l=Gn(s);throw new G(`Enum "${this.name}" cannot represent non-enum value: ${l}.`+_r(this,l),{nodes:s})}const o=this.getValue(s.value);if(o==null){const l=Gn(s);throw new G(`Value "${l}" does not exist in "${this.name}" enum.`+_r(this,l),{nodes:s})}return o.value}toConfig(){const s=Rt(this.getValues(),r=>r.name,r=>({description:r.description,value:r.value,deprecationReason:r.deprecationReason,extensions:r.extensions,astNode:r.astNode}));return{name:this.name,description:this.description,values:s,extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes}}toString(){return this.name}toJSON(){return this.toString()}}function _r(t,s){const r=t.getValues().map(l=>l.name),o=kr(s,r);return Lr("the enum value",o)}function Qy(t,s){return Jt(s)||ae(!1,`${t} values must be an object with value names as keys.`),Object.entries(s).map(([r,o])=>(Jt(o)||ae(!1,`${t}.${r} must refer to an object with a "value" key representing an internal value but got: ${W(o)}.`),{name:Gy(r),description:o.description,value:o.value!==void 0?o.value:r,deprecationReason:o.deprecationReason,extensions:In(o.extensions),astNode:o.astNode}))}class Rr{constructor(s){var r;this.name=Fn(s.name),this.description=s.description,this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(r=s.extensionASTNodes)!==null&&r!==void 0?r:[],this._fields=Jy.bind(void 0,s)}get[Symbol.toStringTag](){return"GraphQLInputObjectType"}getFields(){return typeof this._fields=="function"&&(this._fields=this._fields()),this._fields}toConfig(){const s=nt(this.getFields(),r=>({description:r.description,type:r.type,defaultValue:r.defaultValue,deprecationReason:r.deprecationReason,extensions:r.extensions,astNode:r.astNode}));return{name:this.name,description:this.description,fields:s,extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes}}toString(){return this.name}toJSON(){return this.toString()}}function Jy(t){const s=Gu(t.fields);return Jt(s)||ae(!1,`${t.name} fields must be an object with field names as keys or a function which returns such an object.`),nt(s,(r,o)=>(!("resolve"in r)||ae(!1,`${t.name}.${o} field has a resolve property, but Input Types cannot define resolvers.`),{name:Fn(o),description:r.description,type:r.type,defaultValue:r.defaultValue,deprecationReason:r.deprecationReason,extensions:In(r.extensions),astNode:r.astNode}))}const aa=2147483647,oa=-2147483648,zy=new it({name:"Int",description:"The `Int` scalar type represents non-fractional signed whole numeric values. Int can represent values between -(2^31) and 2^31 - 1.",serialize(t){const s=Ai(t);if(typeof s=="boolean")return s?1:0;let r=s;if(typeof s=="string"&&s!==""&&(r=Number(s)),typeof r!="number"||!Number.isInteger(r))throw new G(`Int cannot represent non-integer value: ${W(s)}`);if(r>aa||r<oa)throw new G("Int cannot represent non 32-bit signed integer value: "+W(s));return r},parseValue(t){if(typeof t!="number"||!Number.isInteger(t))throw new G(`Int cannot represent non-integer value: ${W(t)}`);if(t>aa||t<oa)throw new G(`Int cannot represent non 32-bit signed integer value: ${t}`);return t},parseLiteral(t){if(t.kind!==y.INT)throw new G(`Int cannot represent non-integer value: ${Gn(t)}`,{nodes:t});const s=parseInt(t.value,10);if(s>aa||s<oa)throw new G(`Int cannot represent non 32-bit signed integer value: ${t.value}`,{nodes:t});return s}}),Xy=new it({name:"Float",description:"The `Float` scalar type represents signed double-precision fractional values as specified by [IEEE 754](https://en.wikipedia.org/wiki/IEEE_floating_point).",serialize(t){const s=Ai(t);if(typeof s=="boolean")return s?1:0;let r=s;if(typeof s=="string"&&s!==""&&(r=Number(s)),typeof r!="number"||!Number.isFinite(r))throw new G(`Float cannot represent non numeric value: ${W(s)}`);return r},parseValue(t){if(typeof t!="number"||!Number.isFinite(t))throw new G(`Float cannot represent non numeric value: ${W(t)}`);return t},parseLiteral(t){if(t.kind!==y.FLOAT&&t.kind!==y.INT)throw new G(`Float cannot represent non numeric value: ${Gn(t)}`,t);return parseFloat(t.value)}}),Se=new it({name:"String",description:"The `String` scalar type represents textual data, represented as UTF-8 character sequences. The String type is most often used by GraphQL to represent free-form human-readable text.",serialize(t){const s=Ai(t);if(typeof s=="string")return s;if(typeof s=="boolean")return s?"true":"false";if(typeof s=="number"&&Number.isFinite(s))return s.toString();throw new G(`String cannot represent value: ${W(t)}`)},parseValue(t){if(typeof t!="string")throw new G(`String cannot represent a non string value: ${W(t)}`);return t},parseLiteral(t){if(t.kind!==y.STRING)throw new G(`String cannot represent a non string value: ${Gn(t)}`,{nodes:t});return t.value}}),yn=new it({name:"Boolean",description:"The `Boolean` scalar type represents `true` or `false`.",serialize(t){const s=Ai(t);if(typeof s=="boolean")return s;if(Number.isFinite(s))return s!==0;throw new G(`Boolean cannot represent a non boolean value: ${W(s)}`)},parseValue(t){if(typeof t!="boolean")throw new G(`Boolean cannot represent a non boolean value: ${W(t)}`);return t},parseLiteral(t){if(t.kind!==y.BOOLEAN)throw new G(`Boolean cannot represent a non boolean value: ${Gn(t)}`,{nodes:t});return t.value}}),Hu=new it({name:"ID",description:'The `ID` scalar type represents a unique identifier, often used to refetch an object or as key for a cache. The ID type appears in a JSON response as a String; however, it is not intended to be human-readable. When expected as an input type, any string (such as `"4"`) or integer (such as `4`) input value will be accepted as an ID.',serialize(t){const s=Ai(t);if(typeof s=="string")return s;if(Number.isInteger(s))return String(s);throw new G(`ID cannot represent value: ${W(t)}`)},parseValue(t){if(typeof t=="string")return t;if(typeof t=="number"&&Number.isInteger(t))return t.toString();throw new G(`ID cannot represent value: ${W(t)}`)},parseLiteral(t){if(t.kind!==y.STRING&&t.kind!==y.INT)throw new G("ID cannot represent a non-string and non-integer value: "+Gn(t),{nodes:t});return t.value}}),Cr=Object.freeze([Se,zy,Xy,yn,Hu]);function Ky(t){return Cr.some(({name:s})=>t.name===s)}function Ai(t){if(tt(t)){if(typeof t.valueOf=="function"){const s=t.valueOf();if(!tt(s))return s}if(typeof t.toJSON=="function")return t.toJSON()}return t}function Zy(t){return Wn(t,mt)}class mt{constructor(s){var r,o;this.name=Fn(s.name),this.description=s.description,this.locations=s.locations,this.isRepeatable=(r=s.isRepeatable)!==null&&r!==void 0?r:!1,this.extensions=In(s.extensions),this.astNode=s.astNode,Array.isArray(s.locations)||ae(!1,`@${s.name} locations must be an Array.`);const l=(o=s.args)!==null&&o!==void 0?o:{};tt(l)&&!Array.isArray(l)||ae(!1,`@${s.name} args must be an object with argument names as keys.`),this.args=Yu(l)}get[Symbol.toStringTag](){return"GraphQLDirective"}toConfig(){return{name:this.name,description:this.description,locations:this.locations,args:ju(this.args),isRepeatable:this.isRepeatable,extensions:this.extensions,astNode:this.astNode}}toString(){return"@"+this.name}toJSON(){return this.toString()}}const eT=new mt({name:"include",description:"Directs the executor to include this field or fragment only when the `if` argument is true.",locations:[Y.FIELD,Y.FRAGMENT_SPREAD,Y.INLINE_FRAGMENT],args:{if:{type:new ne(yn),description:"Included when true."}}}),nT=new mt({name:"skip",description:"Directs the executor to skip this field or fragment when the `if` argument is true.",locations:[Y.FIELD,Y.FRAGMENT_SPREAD,Y.INLINE_FRAGMENT],args:{if:{type:new ne(yn),description:"Skipped when true."}}}),tT="No longer supported",qu=new mt({name:"deprecated",description:"Marks an element of a GraphQL schema as no longer supported.",locations:[Y.FIELD_DEFINITION,Y.ARGUMENT_DEFINITION,Y.INPUT_FIELD_DEFINITION,Y.ENUM_VALUE],args:{reason:{type:Se,description:"Explains why this element was deprecated, usually also including a suggestion for how to access supported similar data. Formatted using the Markdown syntax, as specified by [CommonMark](https://commonmark.org/).",defaultValue:tT}}}),Qu=new mt({name:"specifiedBy",description:"Exposes a URL that specifies the behavior of this scalar.",locations:[Y.SCALAR],args:{url:{type:new ne(Se),description:"The URL that specifies the behavior of this scalar."}}}),Zt=Object.freeze([eT,nT,qu,Qu]);function iT(t){return typeof t=="object"&&typeof(t==null?void 0:t[Symbol.iterator])=="function"}function _i(t,s){if($e(s)){const r=_i(t,s.ofType);return(r==null?void 0:r.kind)===y.NULL?null:r}if(t===null)return{kind:y.NULL};if(t===void 0)return null;if(xt(s)){const r=s.ofType;if(iT(t)){const o=[];for(const l of t){const h=_i(l,r);h!=null&&o.push(h)}return{kind:y.LIST,values:o}}return _i(t,r)}if(Cn(s)){if(!tt(t))return null;const r=[];for(const o of Object.values(s.getFields())){const l=_i(t[o.name],o.type);l&&r.push({kind:y.OBJECT_FIELD,name:{kind:y.NAME,value:o.name},value:l})}return{kind:y.OBJECT,fields:r}}if(Uu(s)){const r=s.serialize(t);if(r==null)return null;if(typeof r=="boolean")return{kind:y.BOOLEAN,value:r};if(typeof r=="number"&&Number.isFinite(r)){const o=String(r);return hu.test(o)?{kind:y.INT,value:o}:{kind:y.FLOAT,value:o}}if(typeof r=="string")return kn(s)?{kind:y.ENUM,value:r}:s===Hu&&hu.test(r)?{kind:y.INT,value:r}:{kind:y.STRING,value:r};throw new TypeError(`Cannot convert value to AST: ${W(r)}.`)}xn(!1,"Unexpected input type: "+W(s))}const hu=/^-?(?:0|[1-9][0-9]*)$/,Ea=new Vn({name:"__Schema",description:"A GraphQL Schema defines the capabilities of a GraphQL server. It exposes all available types and directives on the server, as well as the entry points for query, mutation, and subscription operations.",fields:()=>({description:{type:Se,resolve:t=>t.description},types:{description:"A list of all types supported by this server.",type:new ne(new Xe(new ne(Dn))),resolve(t){return Object.values(t.getTypeMap())}},queryType:{description:"The type that query operations will be rooted at.",type:new ne(Dn),resolve:t=>t.getQueryType()},mutationType:{description:"If this server supports mutation, the type that mutation operations will be rooted at.",type:Dn,resolve:t=>t.getMutationType()},subscriptionType:{description:"If this server support subscription, the type that subscription operations will be rooted at.",type:Dn,resolve:t=>t.getSubscriptionType()},directives:{description:"A list of all directives supported by this server.",type:new ne(new Xe(new ne(Ju))),resolve:t=>t.getDirectives()}})}),Ju=new Vn({name:"__Directive",description:`A Directive provides a way to describe alternate runtime execution and type validation behavior in a GraphQL document.

In some cases, you need to provide options to alter GraphQL's execution behavior in ways field arguments will not suffice, such as conditionally including or skipping a field. Directives provide this by describing additional information to the executor.`,fields:()=>({name:{type:new ne(Se),resolve:t=>t.name},description:{type:Se,resolve:t=>t.description},isRepeatable:{type:new ne(yn),resolve:t=>t.isRepeatable},locations:{type:new ne(new Xe(new ne(zu))),resolve:t=>t.locations},args:{type:new ne(new Xe(new ne(Fr))),args:{includeDeprecated:{type:yn,defaultValue:!1}},resolve(t,{includeDeprecated:s}){return s?t.args:t.args.filter(r=>r.deprecationReason==null)}}})}),zu=new Xt({name:"__DirectiveLocation",description:"A Directive can be adjacent to many parts of the GraphQL language, a __DirectiveLocation describes one such possible adjacencies.",values:{QUERY:{value:Y.QUERY,description:"Location adjacent to a query operation."},MUTATION:{value:Y.MUTATION,description:"Location adjacent to a mutation operation."},SUBSCRIPTION:{value:Y.SUBSCRIPTION,description:"Location adjacent to a subscription operation."},FIELD:{value:Y.FIELD,description:"Location adjacent to a field."},FRAGMENT_DEFINITION:{value:Y.FRAGMENT_DEFINITION,description:"Location adjacent to a fragment definition."},FRAGMENT_SPREAD:{value:Y.FRAGMENT_SPREAD,description:"Location adjacent to a fragment spread."},INLINE_FRAGMENT:{value:Y.INLINE_FRAGMENT,description:"Location adjacent to an inline fragment."},VARIABLE_DEFINITION:{value:Y.VARIABLE_DEFINITION,description:"Location adjacent to a variable definition."},SCHEMA:{value:Y.SCHEMA,description:"Location adjacent to a schema definition."},SCALAR:{value:Y.SCALAR,description:"Location adjacent to a scalar definition."},OBJECT:{value:Y.OBJECT,description:"Location adjacent to an object type definition."},FIELD_DEFINITION:{value:Y.FIELD_DEFINITION,description:"Location adjacent to a field definition."},ARGUMENT_DEFINITION:{value:Y.ARGUMENT_DEFINITION,description:"Location adjacent to an argument definition."},INTERFACE:{value:Y.INTERFACE,description:"Location adjacent to an interface definition."},UNION:{value:Y.UNION,description:"Location adjacent to a union definition."},ENUM:{value:Y.ENUM,description:"Location adjacent to an enum definition."},ENUM_VALUE:{value:Y.ENUM_VALUE,description:"Location adjacent to an enum value definition."},INPUT_OBJECT:{value:Y.INPUT_OBJECT,description:"Location adjacent to an input object type definition."},INPUT_FIELD_DEFINITION:{value:Y.INPUT_FIELD_DEFINITION,description:"Location adjacent to an input object field definition."}}}),Dn=new Vn({name:"__Type",description:"The fundamental unit of any GraphQL Schema is the type. There are many kinds of types in GraphQL as represented by the `__TypeKind` enum.\n\nDepending on the kind of a type, certain fields describe information about that type. Scalar types provide no information beyond a name, description and optional `specifiedByURL`, while Enum types provide their values. Object and Interface types provide the fields they describe. Abstract types, Union and Interface, provide the Object types possible at runtime. List and NonNull types compose other types.",fields:()=>({kind:{type:new ne(Zu),resolve(t){if(vt(t))return pe.SCALAR;if(Ke(t))return pe.OBJECT;if(je(t))return pe.INTERFACE;if(Ln(t))return pe.UNION;if(kn(t))return pe.ENUM;if(Cn(t))return pe.INPUT_OBJECT;if(xt(t))return pe.LIST;if($e(t))return pe.NON_NULL;xn(!1,`Unexpected type: "${W(t)}".`)}},name:{type:Se,resolve:t=>"name"in t?t.name:void 0},description:{type:Se,resolve:t=>"description"in t?t.description:void 0},specifiedByURL:{type:Se,resolve:t=>"specifiedByURL"in t?t.specifiedByURL:void 0},fields:{type:new Xe(new ne(Xu)),args:{includeDeprecated:{type:yn,defaultValue:!1}},resolve(t,{includeDeprecated:s}){if(Ke(t)||je(t)){const r=Object.values(t.getFields());return s?r:r.filter(o=>o.deprecationReason==null)}}},interfaces:{type:new Xe(new ne(Dn)),resolve(t){if(Ke(t)||je(t))return t.getInterfaces()}},possibleTypes:{type:new Xe(new ne(Dn)),resolve(t,s,r,{schema:o}){if(Yy(t))return o.getPossibleTypes(t)}},enumValues:{type:new Xe(new ne(Ku)),args:{includeDeprecated:{type:yn,defaultValue:!1}},resolve(t,{includeDeprecated:s}){if(kn(t)){const r=t.getValues();return s?r:r.filter(o=>o.deprecationReason==null)}}},inputFields:{type:new Xe(new ne(Fr)),args:{includeDeprecated:{type:yn,defaultValue:!1}},resolve(t,{includeDeprecated:s}){if(Cn(t)){const r=Object.values(t.getFields());return s?r:r.filter(o=>o.deprecationReason==null)}}},ofType:{type:Dn,resolve:t=>"ofType"in t?t.ofType:void 0}})}),Xu=new Vn({name:"__Field",description:"Object and Interface types are described by a list of Fields, each of which has a name, potentially a list of arguments, and a return type.",fields:()=>({name:{type:new ne(Se),resolve:t=>t.name},description:{type:Se,resolve:t=>t.description},args:{type:new ne(new Xe(new ne(Fr))),args:{includeDeprecated:{type:yn,defaultValue:!1}},resolve(t,{includeDeprecated:s}){return s?t.args:t.args.filter(r=>r.deprecationReason==null)}},type:{type:new ne(Dn),resolve:t=>t.type},isDeprecated:{type:new ne(yn),resolve:t=>t.deprecationReason!=null},deprecationReason:{type:Se,resolve:t=>t.deprecationReason}})}),Fr=new Vn({name:"__InputValue",description:"Arguments provided to Fields or Directives and the input fields of an InputObject are represented as Input Values which describe their type and optionally a default value.",fields:()=>({name:{type:new ne(Se),resolve:t=>t.name},description:{type:Se,resolve:t=>t.description},type:{type:new ne(Dn),resolve:t=>t.type},defaultValue:{type:Se,description:"A GraphQL-formatted string representing the default value for this input value.",resolve(t){const{type:s,defaultValue:r}=t,o=_i(r,s);return o?Gn(o):null}},isDeprecated:{type:new ne(yn),resolve:t=>t.deprecationReason!=null},deprecationReason:{type:Se,resolve:t=>t.deprecationReason}})}),Ku=new Vn({name:"__EnumValue",description:"One possible value for a given Enum. Enum values are unique values, not a placeholder for a string or numeric value. However an Enum value is returned in a JSON response as a string.",fields:()=>({name:{type:new ne(Se),resolve:t=>t.name},description:{type:Se,resolve:t=>t.description},isDeprecated:{type:new ne(yn),resolve:t=>t.deprecationReason!=null},deprecationReason:{type:Se,resolve:t=>t.deprecationReason}})});var pe;(function(t){t.SCALAR="SCALAR",t.OBJECT="OBJECT",t.INTERFACE="INTERFACE",t.UNION="UNION",t.ENUM="ENUM",t.INPUT_OBJECT="INPUT_OBJECT",t.LIST="LIST",t.NON_NULL="NON_NULL"})(pe||(pe={}));const Zu=new Xt({name:"__TypeKind",description:"An enum describing what kind of type a given `__Type` is.",values:{SCALAR:{value:pe.SCALAR,description:"Indicates this type is a scalar."},OBJECT:{value:pe.OBJECT,description:"Indicates this type is an object. `fields` and `interfaces` are valid fields."},INTERFACE:{value:pe.INTERFACE,description:"Indicates this type is an interface. `fields`, `interfaces`, and `possibleTypes` are valid fields."},UNION:{value:pe.UNION,description:"Indicates this type is a union. `possibleTypes` is a valid field."},ENUM:{value:pe.ENUM,description:"Indicates this type is an enum. `enumValues` is a valid field."},INPUT_OBJECT:{value:pe.INPUT_OBJECT,description:"Indicates this type is an input object. `inputFields` is a valid field."},LIST:{value:pe.LIST,description:"Indicates this type is a list. `ofType` is a valid field."},NON_NULL:{value:pe.NON_NULL,description:"Indicates this type is a non-null. `ofType` is a valid field."}}});new ne(Ea);new ne(Se);new ne(Se);const Pr=Object.freeze([Ea,Ju,zu,Dn,Xu,Fr,Ku,Zu]);function rT(t){return Pr.some(({name:s})=>t.name===s)}class ec{constructor(s){var r,o;this.__validationErrors=s.assumeValid===!0?[]:void 0,tt(s)||ae(!1,"Must provide configuration object."),!s.types||Array.isArray(s.types)||ae(!1,`"types" must be Array if provided but got: ${W(s.types)}.`),!s.directives||Array.isArray(s.directives)||ae(!1,`"directives" must be Array if provided but got: ${W(s.directives)}.`),this.description=s.description,this.extensions=In(s.extensions),this.astNode=s.astNode,this.extensionASTNodes=(r=s.extensionASTNodes)!==null&&r!==void 0?r:[],this._queryType=s.query,this._mutationType=s.mutation,this._subscriptionType=s.subscription,this._directives=(o=s.directives)!==null&&o!==void 0?o:Zt;const l=new Set(s.types);if(s.types!=null)for(const h of s.types)l.delete(h),Rn(h,l);this._queryType!=null&&Rn(this._queryType,l),this._mutationType!=null&&Rn(this._mutationType,l),this._subscriptionType!=null&&Rn(this._subscriptionType,l);for(const h of this._directives)if(Zy(h))for(const g of h.args)Rn(g.type,l);Rn(Ea,l),this._typeMap=Object.create(null),this._subTypeMap=Object.create(null),this._implementationsMap=Object.create(null);for(const h of l){if(h==null)continue;const g=h.name;if(g||ae(!1,"One of the provided types for building the Schema is missing a name."),this._typeMap[g]!==void 0)throw new Error(`Schema must contain uniquely named types but contains multiple types named "${g}".`);if(this._typeMap[g]=h,je(h)){for(const T of h.getInterfaces())if(je(T)){let I=this._implementationsMap[T.name];I===void 0&&(I=this._implementationsMap[T.name]={objects:[],interfaces:[]}),I.interfaces.push(h)}}else if(Ke(h)){for(const T of h.getInterfaces())if(je(T)){let I=this._implementationsMap[T.name];I===void 0&&(I=this._implementationsMap[T.name]={objects:[],interfaces:[]}),I.objects.push(h)}}}}get[Symbol.toStringTag](){return"GraphQLSchema"}getQueryType(){return this._queryType}getMutationType(){return this._mutationType}getSubscriptionType(){return this._subscriptionType}getRootType(s){switch(s){case vn.QUERY:return this.getQueryType();case vn.MUTATION:return this.getMutationType();case vn.SUBSCRIPTION:return this.getSubscriptionType()}}getTypeMap(){return this._typeMap}getType(s){return this.getTypeMap()[s]}getPossibleTypes(s){return Ln(s)?s.getTypes():this.getImplementations(s).objects}getImplementations(s){const r=this._implementationsMap[s.name];return r??{objects:[],interfaces:[]}}isSubType(s,r){let o=this._subTypeMap[s.name];if(o===void 0){if(o=Object.create(null),Ln(s))for(const l of s.getTypes())o[l.name]=!0;else{const l=this.getImplementations(s);for(const h of l.objects)o[h.name]=!0;for(const h of l.interfaces)o[h.name]=!0}this._subTypeMap[s.name]=o}return o[r.name]!==void 0}getDirectives(){return this._directives}getDirective(s){return this.getDirectives().find(r=>r.name===s)}toConfig(){return{description:this.description,query:this.getQueryType(),mutation:this.getMutationType(),subscription:this.getSubscriptionType(),types:Object.values(this.getTypeMap()),directives:this.getDirectives(),extensions:this.extensions,astNode:this.astNode,extensionASTNodes:this.extensionASTNodes,assumeValid:this.__validationErrors!==void 0}}}function Rn(t,s){const r=jy(t);if(!s.has(r)){if(s.add(r),Ln(r))for(const o of r.getTypes())Rn(o,s);else if(Ke(r)||je(r)){for(const o of r.getInterfaces())Rn(o,s);for(const o of Object.values(r.getFields())){Rn(o.type,s);for(const l of o.args)Rn(l.type,s)}}else if(Cn(r))for(const o of Object.values(r.getFields()))Rn(o.type,s)}return s}function sT(t){return t.kind===y.SCHEMA_DEFINITION||Oi(t)||t.kind===y.DIRECTIVE_DEFINITION}function Oi(t){return t.kind===y.SCALAR_TYPE_DEFINITION||t.kind===y.OBJECT_TYPE_DEFINITION||t.kind===y.INTERFACE_TYPE_DEFINITION||t.kind===y.UNION_TYPE_DEFINITION||t.kind===y.ENUM_TYPE_DEFINITION||t.kind===y.INPUT_OBJECT_TYPE_DEFINITION}function aT(t){return t.kind===y.SCHEMA_EXTENSION||ba(t)}function ba(t){return t.kind===y.SCALAR_TYPE_EXTENSION||t.kind===y.OBJECT_TYPE_EXTENSION||t.kind===y.INTERFACE_TYPE_EXTENSION||t.kind===y.UNION_TYPE_EXTENSION||t.kind===y.ENUM_TYPE_EXTENSION||t.kind===y.INPUT_OBJECT_TYPE_EXTENSION}function oT(t){const s=Object.create(null),r=t.getSchema(),o=r?r.getDirectives():Zt;for(const g of o)s[g.name]=g.args.map(T=>T.name);const l=t.getDocument().definitions;for(const g of l)if(g.kind===y.DIRECTIVE_DEFINITION){var h;const T=(h=g.arguments)!==null&&h!==void 0?h:[];s[g.name.value]=T.map(I=>I.name.value)}return{Directive(g){const T=g.name.value,I=s[T];if(g.arguments&&I)for(const O of g.arguments){const _=O.name.value;if(!I.includes(_)){const x=kr(_,I);t.reportError(new G(`Unknown argument "${_}" on directive "@${T}".`+Lr(x),{nodes:O}))}}return!1}}}function lT(t){const s=Object.create(null),r=t.getSchema(),o=r?r.getDirectives():Zt;for(const h of o)s[h.name]=h.locations;const l=t.getDocument().definitions;for(const h of l)h.kind===y.DIRECTIVE_DEFINITION&&(s[h.name.value]=h.locations.map(g=>g.value));return{Directive(h,g,T,I,O){const _=h.name.value,x=s[_];if(!x){t.reportError(new G(`Unknown directive "@${_}".`,{nodes:h}));return}const B=uT(O);B&&!x.includes(B)&&t.reportError(new G(`Directive "@${_}" may not be used on ${B}.`,{nodes:h}))}}}function uT(t){const s=t[t.length-1];switch("kind"in s||xn(!1),s.kind){case y.OPERATION_DEFINITION:return cT(s.operation);case y.FIELD:return Y.FIELD;case y.FRAGMENT_SPREAD:return Y.FRAGMENT_SPREAD;case y.INLINE_FRAGMENT:return Y.INLINE_FRAGMENT;case y.FRAGMENT_DEFINITION:return Y.FRAGMENT_DEFINITION;case y.VARIABLE_DEFINITION:return Y.VARIABLE_DEFINITION;case y.SCHEMA_DEFINITION:case y.SCHEMA_EXTENSION:return Y.SCHEMA;case y.SCALAR_TYPE_DEFINITION:case y.SCALAR_TYPE_EXTENSION:return Y.SCALAR;case y.OBJECT_TYPE_DEFINITION:case y.OBJECT_TYPE_EXTENSION:return Y.OBJECT;case y.FIELD_DEFINITION:return Y.FIELD_DEFINITION;case y.INTERFACE_TYPE_DEFINITION:case y.INTERFACE_TYPE_EXTENSION:return Y.INTERFACE;case y.UNION_TYPE_DEFINITION:case y.UNION_TYPE_EXTENSION:return Y.UNION;case y.ENUM_TYPE_DEFINITION:case y.ENUM_TYPE_EXTENSION:return Y.ENUM;case y.ENUM_VALUE_DEFINITION:return Y.ENUM_VALUE;case y.INPUT_OBJECT_TYPE_DEFINITION:case y.INPUT_OBJECT_TYPE_EXTENSION:return Y.INPUT_OBJECT;case y.INPUT_VALUE_DEFINITION:{const r=t[t.length-3];return"kind"in r||xn(!1),r.kind===y.INPUT_OBJECT_TYPE_DEFINITION?Y.INPUT_FIELD_DEFINITION:Y.ARGUMENT_DEFINITION}default:xn(!1,"Unexpected kind: "+W(s.kind))}}function cT(t){switch(t){case vn.QUERY:return Y.QUERY;case vn.MUTATION:return Y.MUTATION;case vn.SUBSCRIPTION:return Y.SUBSCRIPTION}}function fT(t){const s=t.getSchema(),r=s?s.getTypeMap():Object.create(null),o=Object.create(null);for(const h of t.getDocument().definitions)Oi(h)&&(o[h.name.value]=!0);const l=[...Object.keys(r),...Object.keys(o)];return{NamedType(h,g,T,I,O){const _=h.name.value;if(!r[_]&&!o[_]){var x;const B=(x=O[2])!==null&&x!==void 0?x:T,K=B!=null&&hT(B);if(K&&du.includes(_))return;const V=kr(_,K?du.concat(l):l);t.reportError(new G(`Unknown type "${_}".`+Lr(V),{nodes:h}))}}}}const du=[...Cr,...Pr].map(t=>t.name);function hT(t){return"kind"in t&&(sT(t)||aT(t))}function dT(t){var s,r,o;const l=t.getSchema(),h=(s=(r=(o=l==null?void 0:l.astNode)!==null&&o!==void 0?o:l==null?void 0:l.getQueryType())!==null&&r!==void 0?r:l==null?void 0:l.getMutationType())!==null&&s!==void 0?s:l==null?void 0:l.getSubscriptionType();let g=0;return{SchemaDefinition(T){if(h){t.reportError(new G("Cannot define a new schema within a schema extension.",{nodes:T}));return}g>0&&t.reportError(new G("Must provide only one schema definition.",{nodes:T})),++g}}}function pT(t){const s=t.getSchema(),r=Object.create(null);for(const l of t.getDocument().definitions)Oi(l)&&(r[l.name.value]=l);return{ScalarTypeExtension:o,ObjectTypeExtension:o,InterfaceTypeExtension:o,UnionTypeExtension:o,EnumTypeExtension:o,InputObjectTypeExtension:o};function o(l){const h=l.name.value,g=r[h],T=s==null?void 0:s.getType(h);let I;if(g?I=gT[g.kind]:T&&(I=mT(T)),I){if(I!==l.kind){const O=vT(l.kind);t.reportError(new G(`Cannot extend non-${O} type "${h}".`,{nodes:g?[g,l]:l}))}}else{const O=Object.keys({...r,...s==null?void 0:s.getTypeMap()}),_=kr(h,O);t.reportError(new G(`Cannot extend type "${h}" because it is not defined.`+Lr(_),{nodes:l.name}))}}}const gT={[y.SCALAR_TYPE_DEFINITION]:y.SCALAR_TYPE_EXTENSION,[y.OBJECT_TYPE_DEFINITION]:y.OBJECT_TYPE_EXTENSION,[y.INTERFACE_TYPE_DEFINITION]:y.INTERFACE_TYPE_EXTENSION,[y.UNION_TYPE_DEFINITION]:y.UNION_TYPE_EXTENSION,[y.ENUM_TYPE_DEFINITION]:y.ENUM_TYPE_EXTENSION,[y.INPUT_OBJECT_TYPE_DEFINITION]:y.INPUT_OBJECT_TYPE_EXTENSION};function mT(t){if(vt(t))return y.SCALAR_TYPE_EXTENSION;if(Ke(t))return y.OBJECT_TYPE_EXTENSION;if(je(t))return y.INTERFACE_TYPE_EXTENSION;if(Ln(t))return y.UNION_TYPE_EXTENSION;if(kn(t))return y.ENUM_TYPE_EXTENSION;if(Cn(t))return y.INPUT_OBJECT_TYPE_EXTENSION;xn(!1,"Unexpected type: "+W(t))}function vT(t){switch(t){case y.SCALAR_TYPE_EXTENSION:return"scalar";case y.OBJECT_TYPE_EXTENSION:return"object";case y.INTERFACE_TYPE_EXTENSION:return"interface";case y.UNION_TYPE_EXTENSION:return"union";case y.ENUM_TYPE_EXTENSION:return"enum";case y.INPUT_OBJECT_TYPE_EXTENSION:return"input object";default:xn(!1,"Unexpected kind: "+W(t))}}function yT(t){var s;const r=Object.create(null),o=t.getSchema(),l=(s=o==null?void 0:o.getDirectives())!==null&&s!==void 0?s:Zt;for(const T of l)r[T.name]=zt(T.args.filter(Hy),I=>I.name);const h=t.getDocument().definitions;for(const T of h)if(T.kind===y.DIRECTIVE_DEFINITION){var g;const I=(g=T.arguments)!==null&&g!==void 0?g:[];r[T.name.value]=zt(I.filter(TT),O=>O.name.value)}return{Directive:{leave(T){const I=T.name.value,O=r[I];if(O){var _;const x=(_=T.arguments)!==null&&_!==void 0?_:[],B=new Set(x.map(K=>K.name.value));for(const[K,V]of Object.entries(O))if(!B.has(K)){const he=Ta(V.type)?W(V.type):Gn(V.type);t.reportError(new G(`Directive "@${I}" argument "${K}" of type "${he}" is required, but it was not provided.`,{nodes:T}))}}}}}}function TT(t){return t.type.kind===y.NON_NULL_TYPE&&t.defaultValue==null}function gt(t,s,r){if(t){if(t.kind===y.VARIABLE){const o=t.name.value;if(r==null||r[o]===void 0)return;const l=r[o];return l===null&&$e(s)?void 0:l}if($e(s))return t.kind===y.NULL?void 0:gt(t,s.ofType,r);if(t.kind===y.NULL)return null;if(xt(s)){const o=s.ofType;if(t.kind===y.LIST){const h=[];for(const g of t.values)if(pu(g,r)){if($e(o))return;h.push(null)}else{const T=gt(g,o,r);if(T===void 0)return;h.push(T)}return h}const l=gt(t,o,r);return l===void 0?void 0:[l]}if(Cn(s)){if(t.kind!==y.OBJECT)return;const o=Object.create(null),l=zt(t.fields,h=>h.name.value);for(const h of Object.values(s.getFields())){const g=l[h.name];if(!g||pu(g.value,r)){if(h.defaultValue!==void 0)o[h.name]=h.defaultValue;else if($e(h.type))return;continue}const T=gt(g.value,h.type,r);if(T===void 0)return;o[h.name]=T}return o}if(Uu(s)){let o;try{o=s.parseLiteral(t,r)}catch{return}return o===void 0?void 0:o}xn(!1,"Unexpected input type: "+W(s))}}function pu(t,s){return t.kind===y.VARIABLE&&(s==null||s[t.name.value]===void 0)}function IT(t,s,r){var o;const l={},h=(o=s.arguments)!==null&&o!==void 0?o:[],g=zt(h,T=>T.name.value);for(const T of t.args){const I=T.name,O=T.type,_=g[I];if(!_){if(T.defaultValue!==void 0)l[I]=T.defaultValue;else if($e(O))throw new G(`Argument "${I}" of required type "${W(O)}" was not provided.`,{nodes:s});continue}const x=_.value;let B=x.kind===y.NULL;if(x.kind===y.VARIABLE){const V=x.name.value;if(r==null||!ET(r,V)){if(T.defaultValue!==void 0)l[I]=T.defaultValue;else if($e(O))throw new G(`Argument "${I}" of required type "${W(O)}" was provided the variable "$${V}" which was not provided a runtime value.`,{nodes:x});continue}B=r[V]==null}if(B&&$e(O))throw new G(`Argument "${I}" of non-null type "${W(O)}" must not be null.`,{nodes:x});const K=gt(x,O,r);if(K===void 0)throw new G(`Argument "${I}" has invalid value ${Gn(x)}.`,{nodes:x});l[I]=K}return l}function nc(t,s,r){var o;const l=(o=s.directives)===null||o===void 0?void 0:o.find(h=>h.name.value===t.name);if(l)return IT(t,l,r)}function ET(t,s){return Object.prototype.hasOwnProperty.call(t,s)}function tc(t,s){const r=new Map;for(const o of t){const l=s(o),h=r.get(l);h===void 0?r.set(l,[o]):h.push(o)}return r}function bT(t){return{DirectiveDefinition(o){var l;const h=(l=o.arguments)!==null&&l!==void 0?l:[];return r(`@${o.name.value}`,h)},InterfaceTypeDefinition:s,InterfaceTypeExtension:s,ObjectTypeDefinition:s,ObjectTypeExtension:s};function s(o){var l;const h=o.name.value,g=(l=o.fields)!==null&&l!==void 0?l:[];for(const I of g){var T;const O=I.name.value,_=(T=I.arguments)!==null&&T!==void 0?T:[];r(`${h}.${O}`,_)}return!1}function r(o,l){const h=tc(l,g=>g.name.value);for(const[g,T]of h)T.length>1&&t.reportError(new G(`Argument "${o}(${g}:)" can only be defined once.`,{nodes:T.map(I=>I.name)}));return!1}}function wT(t){return{Field:s,Directive:s};function s(r){var o;const l=(o=r.arguments)!==null&&o!==void 0?o:[],h=tc(l,g=>g.name.value);for(const[g,T]of h)T.length>1&&t.reportError(new G(`There can be only one argument named "${g}".`,{nodes:T.map(I=>I.name)}))}}function _T(t){const s=Object.create(null),r=t.getSchema();return{DirectiveDefinition(o){const l=o.name.value;if(r!=null&&r.getDirective(l)){t.reportError(new G(`Directive "@${l}" already exists in the schema. It cannot be redefined.`,{nodes:o.name}));return}return s[l]?t.reportError(new G(`There can be only one directive named "@${l}".`,{nodes:[s[l],o.name]})):s[l]=o.name,!1}}}function ST(t){const s=Object.create(null),r=t.getSchema(),o=r?r.getDirectives():Zt;for(const T of o)s[T.name]=!T.isRepeatable;const l=t.getDocument().definitions;for(const T of l)T.kind===y.DIRECTIVE_DEFINITION&&(s[T.name.value]=!T.repeatable);const h=Object.create(null),g=Object.create(null);return{enter(T){if(!("directives"in T)||!T.directives)return;let I;if(T.kind===y.SCHEMA_DEFINITION||T.kind===y.SCHEMA_EXTENSION)I=h;else if(Oi(T)||ba(T)){const O=T.name.value;I=g[O],I===void 0&&(g[O]=I=Object.create(null))}else I=Object.create(null);for(const O of T.directives){const _=O.name.value;s[_]&&(I[_]?t.reportError(new G(`The directive "@${_}" can only be used once at this location.`,{nodes:[I[_],O]})):I[_]=O)}}}}function NT(t){const s=t.getSchema(),r=s?s.getTypeMap():Object.create(null),o=Object.create(null);return{EnumTypeDefinition:l,EnumTypeExtension:l};function l(h){var g;const T=h.name.value;o[T]||(o[T]=Object.create(null));const I=(g=h.values)!==null&&g!==void 0?g:[],O=o[T];for(const _ of I){const x=_.name.value,B=r[T];kn(B)&&B.getValue(x)?t.reportError(new G(`Enum value "${T}.${x}" already exists in the schema. It cannot also be defined in this type extension.`,{nodes:_.name})):O[x]?t.reportError(new G(`Enum value "${T}.${x}" can only be defined once.`,{nodes:[O[x],_.name]})):O[x]=_.name}return!1}}function AT(t){const s=t.getSchema(),r=s?s.getTypeMap():Object.create(null),o=Object.create(null);return{InputObjectTypeDefinition:l,InputObjectTypeExtension:l,InterfaceTypeDefinition:l,InterfaceTypeExtension:l,ObjectTypeDefinition:l,ObjectTypeExtension:l};function l(h){var g;const T=h.name.value;o[T]||(o[T]=Object.create(null));const I=(g=h.fields)!==null&&g!==void 0?g:[],O=o[T];for(const _ of I){const x=_.name.value;OT(r[T],x)?t.reportError(new G(`Field "${T}.${x}" already exists in the schema. It cannot also be defined in this type extension.`,{nodes:_.name})):O[x]?t.reportError(new G(`Field "${T}.${x}" can only be defined once.`,{nodes:[O[x],_.name]})):O[x]=_.name}return!1}}function OT(t,s){return Ke(t)||je(t)||Cn(t)?t.getFields()[s]!=null:!1}function RT(t){const s=[];let r=Object.create(null);return{ObjectValue:{enter(){s.push(r),r=Object.create(null)},leave(){const o=s.pop();o||xn(!1),r=o}},ObjectField(o){const l=o.name.value;r[l]?t.reportError(new G(`There can be only one input field named "${l}".`,{nodes:[r[l],o.name]})):r[l]=o.name}}}function DT(t){const s=t.getSchema(),r=Object.create(null),o=s?{query:s.getQueryType(),mutation:s.getMutationType(),subscription:s.getSubscriptionType()}:{};return{SchemaDefinition:l,SchemaExtension:l};function l(h){var g;const T=(g=h.operationTypes)!==null&&g!==void 0?g:[];for(const I of T){const O=I.operation,_=r[O];o[O]?t.reportError(new G(`Type for ${O} already defined in the schema. It cannot be redefined.`,{nodes:I})):_?t.reportError(new G(`There can be only one ${O} type in schema.`,{nodes:[_,I]})):r[O]=I}return!1}}function xT(t){const s=Object.create(null),r=t.getSchema();return{ScalarTypeDefinition:o,ObjectTypeDefinition:o,InterfaceTypeDefinition:o,UnionTypeDefinition:o,EnumTypeDefinition:o,InputObjectTypeDefinition:o};function o(l){const h=l.name.value;if(r!=null&&r.getType(h)){t.reportError(new G(`Type "${h}" already exists in the schema. It cannot also be defined in this type definition.`,{nodes:l.name}));return}return s[h]?t.reportError(new G(`There can be only one type named "${h}".`,{nodes:[s[h],l.name]})):s[h]=l.name,!1}}const LT=Object.freeze([dT,DT,xT,NT,AT,bT,_T,fT,lT,ST,pT,oT,wT,RT,yT]);class kT{constructor(s,r){this._ast=s,this._fragments=void 0,this._fragmentSpreads=new Map,this._recursivelyReferencedFragments=new Map,this._onError=r}get[Symbol.toStringTag](){return"ASTValidationContext"}reportError(s){this._onError(s)}getDocument(){return this._ast}getFragment(s){let r;if(this._fragments)r=this._fragments;else{r=Object.create(null);for(const o of this.getDocument().definitions)o.kind===y.FRAGMENT_DEFINITION&&(r[o.name.value]=o);this._fragments=r}return r[s]}getFragmentSpreads(s){let r=this._fragmentSpreads.get(s);if(!r){r=[];const o=[s];let l;for(;l=o.pop();)for(const h of l.selections)h.kind===y.FRAGMENT_SPREAD?r.push(h):h.selectionSet&&o.push(h.selectionSet);this._fragmentSpreads.set(s,r)}return r}getRecursivelyReferencedFragments(s){let r=this._recursivelyReferencedFragments.get(s);if(!r){r=[];const o=Object.create(null),l=[s.selectionSet];let h;for(;h=l.pop();)for(const g of this.getFragmentSpreads(h)){const T=g.name.value;if(o[T]!==!0){o[T]=!0;const I=this.getFragment(T);I&&(r.push(I),l.push(I.selectionSet))}}this._recursivelyReferencedFragments.set(s,r)}return r}}class CT extends kT{constructor(s,r,o){super(s,o),this._schema=r}get[Symbol.toStringTag](){return"SDLValidationContext"}getSchema(){return this._schema}}function FT(t,s,r=LT){const o=[],l=new CT(t,s,g=>{o.push(g)}),h=r.map(g=>g(l));return ku(t,Uy(h)),o}function PT(t){const s=FT(t);if(s.length!==0)throw new Error(s.map(r=>r.message).join(`

`))}function UT(t,s){tt(t)&&tt(t.__schema)||ae(!1,`Invalid or incomplete introspection result. Ensure that you are passing "data" property of introspection response and no "errors" was returned alongside: ${W(t)}.`);const r=t.__schema,o=Rt(r.types,R=>R.name,R=>B(R));for(const R of[...Cr,...Pr])o[R.name]&&(o[R.name]=R);const l=r.queryType?_(r.queryType):null,h=r.mutationType?_(r.mutationType):null,g=r.subscriptionType?_(r.subscriptionType):null,T=r.directives?r.directives.map($n):[];return new ec({description:r.description,query:l,mutation:h,subscription:g,types:Object.values(o),directives:T,assumeValid:s==null?void 0:s.assumeValid});function I(R){if(R.kind===pe.LIST){const q=R.ofType;if(!q)throw new Error("Decorated type deeper than introspection query.");return new Xe(I(q))}if(R.kind===pe.NON_NULL){const q=R.ofType;if(!q)throw new Error("Decorated type deeper than introspection query.");const Ge=I(q);return new ne($y(Ge))}return O(R)}function O(R){const q=R.name;if(!q)throw new Error(`Unknown type reference: ${W(R)}.`);const Ge=o[q];if(!Ge)throw new Error(`Invalid or incomplete schema, unknown type: ${q}. Ensure that a full introspection query is used in order to build a client schema.`);return Ge}function _(R){return Vy(O(R))}function x(R){return Wy(O(R))}function B(R){if(R!=null&&R.name!=null&&R.kind!=null)switch(R.kind){case pe.SCALAR:return K(R);case pe.OBJECT:return he(R);case pe.INTERFACE:return se(R);case pe.UNION:return ye(R);case pe.ENUM:return Yn(R);case pe.INPUT_OBJECT:return Te(R)}const q=W(R);throw new Error(`Invalid or incomplete introspection result. Ensure that a full introspection query is used in order to build a client schema: ${q}.`)}function K(R){return new it({name:R.name,description:R.description,specifiedByURL:R.specifiedByURL})}function V(R){if(R.interfaces===null&&R.kind===pe.INTERFACE)return[];if(!R.interfaces){const q=W(R);throw new Error(`Introspection result missing interfaces: ${q}.`)}return R.interfaces.map(x)}function he(R){return new Vn({name:R.name,description:R.description,interfaces:()=>V(R),fields:()=>Ne(R)})}function se(R){return new Ar({name:R.name,description:R.description,interfaces:()=>V(R),fields:()=>Ne(R)})}function ye(R){if(!R.possibleTypes){const q=W(R);throw new Error(`Introspection result missing possibleTypes: ${q}.`)}return new Or({name:R.name,description:R.description,types:()=>R.possibleTypes.map(_)})}function Yn(R){if(!R.enumValues){const q=W(R);throw new Error(`Introspection result missing enumValues: ${q}.`)}return new Xt({name:R.name,description:R.description,values:Rt(R.enumValues,q=>q.name,q=>({description:q.description,deprecationReason:q.deprecationReason}))})}function Te(R){if(!R.inputFields){const q=W(R);throw new Error(`Introspection result missing inputFields: ${q}.`)}return new Rr({name:R.name,description:R.description,fields:()=>Me(R.inputFields)})}function Ne(R){if(!R.fields)throw new Error(`Introspection result missing fields: ${W(R)}.`);return Rt(R.fields,q=>q.name,De)}function De(R){const q=I(R.type);if(!Pu(q)){const Ge=W(q);throw new Error(`Introspection must provide output type for fields, but received: ${Ge}.`)}if(!R.args){const Ge=W(R);throw new Error(`Introspection result missing field args: ${Ge}.`)}return{description:R.description,deprecationReason:R.deprecationReason,type:q,args:Me(R.args)}}function Me(R){return Rt(R,q=>q.name,Fe)}function Fe(R){const q=I(R.type);if(!Fu(q)){const rt=W(q);throw new Error(`Introspection must provide input type for arguments, but received: ${rt}.`)}const Ge=R.defaultValue!=null?gt(Oy(R.defaultValue),q):void 0;return{description:R.description,type:q,defaultValue:Ge,deprecationReason:R.deprecationReason}}function $n(R){if(!R.args){const q=W(R);throw new Error(`Introspection result missing directive args: ${q}.`)}if(!R.locations){const q=W(R);throw new Error(`Introspection result missing directive locations: ${q}.`)}return new mt({name:R.name,description:R.description,isRepeatable:R.isRepeatable,locations:R.locations.slice(),args:Me(R.args)})}}function BT(t,s,r){var o,l,h,g;const T=[],I=Object.create(null),O=[];let _;const x=[];for(const w of s.definitions)if(w.kind===y.SCHEMA_DEFINITION)_=w;else if(w.kind===y.SCHEMA_EXTENSION)x.push(w);else if(Oi(w))T.push(w);else if(ba(w)){const P=w.name.value,k=I[P];I[P]=k?k.concat([w]):[w]}else w.kind===y.DIRECTIVE_DEFINITION&&O.push(w);if(Object.keys(I).length===0&&T.length===0&&O.length===0&&x.length===0&&_==null)return t;const B=Object.create(null);for(const w of t.types)B[w.name]=Yn(w);for(const w of T){var K;const P=w.name.value;B[P]=(K=gu[P])!==null&&K!==void 0?K:on(w)}const V={query:t.query&&se(t.query),mutation:t.mutation&&se(t.mutation),subscription:t.subscription&&se(t.subscription),..._&&Ge([_]),...Ge(x)};return{description:(o=_)===null||o===void 0||(l=o.description)===null||l===void 0?void 0:l.value,...V,types:Object.values(B),directives:[...t.directives.map(ye),...O.map(Ri)],extensions:Object.create(null),astNode:(h=_)!==null&&h!==void 0?h:t.astNode,extensionASTNodes:t.extensionASTNodes.concat(x),assumeValid:(g=r==null?void 0:r.assumeValid)!==null&&g!==void 0?g:!1};function he(w){return xt(w)?new Xe(he(w.ofType)):$e(w)?new ne(he(w.ofType)):se(w)}function se(w){return B[w.name]}function ye(w){const P=w.toConfig();return new mt({...P,args:nt(P.args,q)})}function Yn(w){if(rT(w)||Ky(w))return w;if(vt(w))return De(w);if(Ke(w))return Me(w);if(je(w))return Fe(w);if(Ln(w))return $n(w);if(kn(w))return Ne(w);if(Cn(w))return Te(w);xn(!1,"Unexpected type: "+W(w))}function Te(w){var P;const k=w.toConfig(),C=(P=I[k.name])!==null&&P!==void 0?P:[];return new Rr({...k,fields:()=>({...nt(k.fields,le=>({...le,type:he(le.type)})),...Pn(C)}),extensionASTNodes:k.extensionASTNodes.concat(C)})}function Ne(w){var P;const k=w.toConfig(),C=(P=I[w.name])!==null&&P!==void 0?P:[];return new Xt({...k,values:{...k.values,...En(C)},extensionASTNodes:k.extensionASTNodes.concat(C)})}function De(w){var P;const k=w.toConfig(),C=(P=I[k.name])!==null&&P!==void 0?P:[];let le=k.specifiedByURL;for(const ue of C){var be;le=(be=mu(ue))!==null&&be!==void 0?be:le}return new it({...k,specifiedByURL:le,extensionASTNodes:k.extensionASTNodes.concat(C)})}function Me(w){var P;const k=w.toConfig(),C=(P=I[k.name])!==null&&P!==void 0?P:[];return new Vn({...k,interfaces:()=>[...w.getInterfaces().map(se),...kt(C)],fields:()=>({...nt(k.fields,R),...Lt(C)}),extensionASTNodes:k.extensionASTNodes.concat(C)})}function Fe(w){var P;const k=w.toConfig(),C=(P=I[k.name])!==null&&P!==void 0?P:[];return new Ar({...k,interfaces:()=>[...w.getInterfaces().map(se),...kt(C)],fields:()=>({...nt(k.fields,R),...Lt(C)}),extensionASTNodes:k.extensionASTNodes.concat(C)})}function $n(w){var P;const k=w.toConfig(),C=(P=I[k.name])!==null&&P!==void 0?P:[];return new Or({...k,types:()=>[...w.getTypes().map(se),...Tt(C)],extensionASTNodes:k.extensionASTNodes.concat(C)})}function R(w){return{...w,type:he(w.type),args:w.args&&nt(w.args,q)}}function q(w){return{...w,type:he(w.type)}}function Ge(w){const P={};for(const C of w){var k;const le=(k=C.operationTypes)!==null&&k!==void 0?k:[];for(const be of le)P[be.operation]=rt(be.type)}return P}function rt(w){var P;const k=w.name.value,C=(P=gu[k])!==null&&P!==void 0?P:B[k];if(C===void 0)throw new Error(`Unknown type: "${k}".`);return C}function yt(w){return w.kind===y.LIST_TYPE?new Xe(yt(w.type)):w.kind===y.NON_NULL_TYPE?new ne(yt(w.type)):rt(w)}function Ri(w){var P;return new mt({name:w.name.value,description:(P=w.description)===null||P===void 0?void 0:P.value,locations:w.locations.map(({value:k})=>k),isRepeatable:w.repeatable,args:Di(w.arguments),astNode:w})}function Lt(w){const P=Object.create(null);for(const le of w){var k;const be=(k=le.fields)!==null&&k!==void 0?k:[];for(const ue of be){var C;P[ue.name.value]={type:yt(ue.type),description:(C=ue.description)===null||C===void 0?void 0:C.value,args:Di(ue.arguments),deprecationReason:Sr(ue),astNode:ue}}}return P}function Di(w){const P=w??[],k=Object.create(null);for(const le of P){var C;const be=yt(le.type);k[le.name.value]={type:be,description:(C=le.description)===null||C===void 0?void 0:C.value,defaultValue:gt(le.defaultValue,be),deprecationReason:Sr(le),astNode:le}}return k}function Pn(w){const P=Object.create(null);for(const le of w){var k;const be=(k=le.fields)!==null&&k!==void 0?k:[];for(const ue of be){var C;const ln=yt(ue.type);P[ue.name.value]={type:ln,description:(C=ue.description)===null||C===void 0?void 0:C.value,defaultValue:gt(ue.defaultValue,ln),deprecationReason:Sr(ue),astNode:ue}}}return P}function En(w){const P=Object.create(null);for(const le of w){var k;const be=(k=le.values)!==null&&k!==void 0?k:[];for(const ue of be){var C;P[ue.name.value]={description:(C=ue.description)===null||C===void 0?void 0:C.value,deprecationReason:Sr(ue),astNode:ue}}}return P}function kt(w){return w.flatMap(P=>{var k,C;return(k=(C=P.interfaces)===null||C===void 0?void 0:C.map(rt))!==null&&k!==void 0?k:[]})}function Tt(w){return w.flatMap(P=>{var k,C;return(k=(C=P.types)===null||C===void 0?void 0:C.map(rt))!==null&&k!==void 0?k:[]})}function on(w){var P;const k=w.name.value,C=(P=I[k])!==null&&P!==void 0?P:[];switch(w.kind){case y.OBJECT_TYPE_DEFINITION:{var le;const Pe=[w,...C];return new Vn({name:k,description:(le=w.description)===null||le===void 0?void 0:le.value,interfaces:()=>kt(Pe),fields:()=>Lt(Pe),astNode:w,extensionASTNodes:C})}case y.INTERFACE_TYPE_DEFINITION:{var be;const Pe=[w,...C];return new Ar({name:k,description:(be=w.description)===null||be===void 0?void 0:be.value,interfaces:()=>kt(Pe),fields:()=>Lt(Pe),astNode:w,extensionASTNodes:C})}case y.ENUM_TYPE_DEFINITION:{var ue;const Pe=[w,...C];return new Xt({name:k,description:(ue=w.description)===null||ue===void 0?void 0:ue.value,values:En(Pe),astNode:w,extensionASTNodes:C})}case y.UNION_TYPE_DEFINITION:{var ln;const Pe=[w,...C];return new Or({name:k,description:(ln=w.description)===null||ln===void 0?void 0:ln.value,types:()=>Tt(Pe),astNode:w,extensionASTNodes:C})}case y.SCALAR_TYPE_DEFINITION:{var ei;return new it({name:k,description:(ei=w.description)===null||ei===void 0?void 0:ei.value,specifiedByURL:mu(w),astNode:w,extensionASTNodes:C})}case y.INPUT_OBJECT_TYPE_DEFINITION:{var st;const Pe=[w,...C];return new Rr({name:k,description:(st=w.description)===null||st===void 0?void 0:st.value,fields:()=>Pn(Pe),astNode:w,extensionASTNodes:C})}}}}const gu=zt([...Cr,...Pr],t=>t.name);function Sr(t){const s=nc(qu,t);return s==null?void 0:s.reason}function mu(t){const s=nc(Qu,t);return s==null?void 0:s.url}function MT(t,s){t!=null&&t.kind===y.DOCUMENT||ae(!1,"Must provide valid Document AST."),(s==null?void 0:s.assumeValid)!==!0&&(s==null?void 0:s.assumeValidSDL)!==!0&&PT(t);const o=BT({description:void 0,types:[],directives:[],extensions:Object.create(null),extensionASTNodes:[],assumeValid:!1},t,s);if(o.astNode==null)for(const h of o.types)switch(h.name){case"Query":o.query=h;break;case"Mutation":o.mutation=h;break;case"Subscription":o.subscription=h;break}const l=[...o.directives,...Zt.filter(h=>o.directives.every(g=>g.name!==h.name))];return new ec({...o,directives:l})}function GT(t,s){const r=Ay(t,{noLocation:s==null?void 0:s.noLocation,allowLegacyFragmentVariables:s==null?void 0:s.allowLegacyFragmentVariables});return MT(r,{assumeValidSDL:s==null?void 0:s.assumeValidSDL,assumeValid:s==null?void 0:s.assumeValid})}const vu=`
"""A public transport agency"""
type Agency implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """Agency feed and id"""
  gtfsId: String!
  """Name of the agency"""
  name: String!
  """URL to the home page of the agency"""
  url: String!
  """ID of the time zone which this agency operates on"""
  timezone: String!
  lang: String
  """Phone number which customers can use to contact this agency"""
  phone: String
  """URL to a web page which has information of fares used by this agency"""
  fareUrl: String
  """List of routes operated by this agency"""
  routes: [Route]
  """
  By default, list of alerts which have an effect on all operations of the agency (e.g. a strike).
  It's also possible to return other relevant alerts through defining types.
  """
  alerts(
    """
    Returns alerts for these types that are relevant for the agency.
    By default only returns alerts that have an effect on all operations of the agency (e.g. a strike).
    """
    types: [AgencyAlertType]
  ): [Alert]
}


"""Entities, which are relevant for an agency and can contain alerts"""
enum AgencyAlertType {
  """Alerts affecting the agency."""
  AGENCY
  """Alerts affecting agency's routes"""
  ROUTES
  """
  Alerts affecting the different route types of the agency.
  Alerts that affect route types on all agencies can be fetched through Feed.
  """
  ROUTE_TYPES
}


"""Alert of a current or upcoming disruption in public transportation"""
type Alert implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """hashcode from the original GTFS-RT alert"""
  alertHash: Int
  """The feed in which this alert was published"""
  feed: String
  """
  Agency affected by the disruption. Note that this value is present only if the
  disruption has an effect on all operations of the agency (e.g. in case of a strike).
  """
  agency: Agency @deprecated(reason: """
  Alert can have multiple affected entities now instead of there being duplicate alerts
  for different entities. This will return only one of the affected agencies.
  Use entities instead.
  """)
  """Route affected by the disruption"""
  route: Route @deprecated(reason: """
  Alert can have multiple affected entities now instead of there being duplicate alerts
  for different entities. This will return only one of the affected routes.
  Use entities instead.
  """)
  """Trip affected by the disruption"""
  trip: Trip @deprecated(reason: """
  Alert can have multiple affected entities now instead of there being duplicate alerts
  for different entities. This will return only one of the affected trips.
  Use entities instead.
  """)
  """Stop affected by the disruption"""
  stop: Stop @deprecated(reason: """
  Alert can have multiple affected entities now instead of there being duplicate alerts
  for different entities. This will return only one of the affected stops.
  Use entities instead.
  """)
  """Patterns affected by the disruption"""
  patterns: [Pattern] @deprecated(reason: "This will always return an empty list. Use entities instead.")
  """Header of the alert, if available"""
  alertHeaderText: String
  """Header of the alert in all different available languages"""
  alertHeaderTextTranslations: [TranslatedString!]!
  """Long description of the alert"""
  alertDescriptionText: String!
  """Long descriptions of the alert in all different available languages"""
  alertDescriptionTextTranslations: [TranslatedString!]!
  """Url with more information"""
  alertUrl: String
  """Url with more information in all different available languages"""
  alertUrlTranslations: [TranslatedString!]!
  """Alert effect"""
  alertEffect: AlertEffectType
  """Alert cause"""
  alertCause: AlertCauseType
  """Alert severity level"""
  alertSeverityLevel: AlertSeverityLevelType
  """
  Time when this alert comes into effect. Format: Unix timestamp in seconds
  """
  effectiveStartDate: Long
  """
  Time when this alert is not in effect anymore. Format: Unix timestamp in seconds
  """
  effectiveEndDate: Long
  """Entities affected by the disruption."""
  entities: [AlertEntity]
}


"""Cause of a alert"""
enum AlertCauseType {
  """UNKNOWN_CAUSE"""
  UNKNOWN_CAUSE
  """OTHER_CAUSE"""
  OTHER_CAUSE
  """TECHNICAL_PROBLEM"""
  TECHNICAL_PROBLEM
  """STRIKE"""
  STRIKE
  """DEMONSTRATION"""
  DEMONSTRATION
  """ACCIDENT"""
  ACCIDENT
  """HOLIDAY"""
  HOLIDAY
  """WEATHER"""
  WEATHER
  """MAINTENANCE"""
  MAINTENANCE
  """CONSTRUCTION"""
  CONSTRUCTION
  """POLICE_ACTIVITY"""
  POLICE_ACTIVITY
  """MEDICAL_EMERGENCY"""
  MEDICAL_EMERGENCY
}


"""Effect of a alert"""
enum AlertEffectType {
  """NO_SERVICE"""
  NO_SERVICE
  """REDUCED_SERVICE"""
  REDUCED_SERVICE
  """SIGNIFICANT_DELAYS"""
  SIGNIFICANT_DELAYS
  """DETOUR"""
  DETOUR
  """ADDITIONAL_SERVICE"""
  ADDITIONAL_SERVICE
  """MODIFIED_SERVICE"""
  MODIFIED_SERVICE
  """OTHER_EFFECT"""
  OTHER_EFFECT
  """UNKNOWN_EFFECT"""
  UNKNOWN_EFFECT
  """STOP_MOVED"""
  STOP_MOVED
  """NO_EFFECT"""
  NO_EFFECT
  """ACCESSIBILITY_ISSUE"""
  ACCESSIBILITY_ISSUE
}


"""Entity related to an alert"""
union AlertEntity = Agency | Route | Pattern | Stop | Trip | StopOnRoute | StopOnTrip | RouteType | Unknown


"""Severity level of a alert"""
enum AlertSeverityLevelType {
  """Severity of alert is unknown"""
  UNKNOWN_SEVERITY
  """
  Info alerts are used for informational messages that should not have a
  significant effect on user's journey, for example: A single entrance to a
  metro station is temporarily closed.
  """
  INFO
  """
  Warning alerts are used when a single stop or route has a disruption that can
  affect user's journey, for example: All trams on a specific route are running
  with irregular schedules.
  """
  WARNING
  """
  Severe alerts are used when a significant part of public transport services is
  affected, for example: All train services are cancelled due to technical problems.
  """
  SEVERE
}


type OpeningHours {
  """
  OSM-formatted string of the opening hours.
  
  The spec is available at: https://wiki.openstreetmap.org/wiki/Key:opening_hours
  """
  osm: String
  """
  Opening hours for the selected dates using the local time of the parking lot.
  Each date can have multiple time spans.
  
  Note: This is not implemented yet and always returns null.
  """
  dates(
    """
    Opening hours will be returned for these dates. Dates should use YYYYMMDD format.
    """
    dates: [String!]!
  ): [LocalTimeSpanDate]
}


"""Bike park represents a location where bicycles can be parked."""
type BikePark implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the bike park"""
  bikeParkId: String
  """Name of the bike park"""
  name(
    """
    Returns name with the specified language, if found, otherwise returns with some default language.
    """
    language: String
  ): String!
  """Number of spaces available for bikes"""
  spacesAvailable: Int
  """
  If true, value of \`spacesAvailable\` is updated from a real-time source.
  """
  realtime: Boolean
  """Longitude of the bike park (WGS 84)"""
  lon: Float
  """Latitude of the bike park (WGS 84)"""
  lat: Float
  """
  Source specific tags of the bike park, which describe the available features.
  """
  tags: [String]
  """Opening hours of the parking facility"""
  openingHours: OpeningHours
}


"""
Vehicle parking represents a location where bicycles or cars can be parked.
"""
type VehicleParking implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the park"""
  vehicleParkingId: String
  """Name of the park"""
  name(
    """
    Returns name with the specified language, if found, otherwise returns with some default language.
    """
    language: String
  ): String!
  """
  If true, value of \`spacesAvailable\` is updated from a real-time source.
  """
  realtime: Boolean
  """Longitude of the bike park (WGS 84)"""
  lon: Float
  """Latitude of the bike park (WGS 84)"""
  lat: Float
  """URL which contains details of this vehicle parking."""
  detailsUrl: String
  """
  URL of an image which may be displayed to the user showing the vehicle parking.
  """
  imageUrl: String
  """
  Source specific tags of the vehicle parking, which describe the available features. For example
  park_and_ride, bike_lockers, or static_osm_data.
  """
  tags: [String]
  """A short translatable note containing details of this vehicle parking."""
  note(
    """
    Returns note with the specified language, if found, otherwise returns with some default language.
    """
    language: String
  ): String
  """
  The state of this vehicle parking.
  Only ones in an OPERATIONAL state may be used for Park and Ride.
  """
  state: VehicleParkingState
  """Does this vehicle parking have spaces (capacity) for bicycles."""
  bicyclePlaces: Boolean
  """
  Does this vehicle parking have spaces (capacity) for either wheelchair accessible (disabled)
  or normal cars.
  """
  anyCarPlaces: Boolean
  """
  Does this vehicle parking have spaces (capacity) for cars excluding wheelchair accessible spaces.
  Use anyCarPlaces to check if any type of car may use this vehicle parking.
  """
  carPlaces: Boolean
  """
  Does this vehicle parking have wheelchair accessible (disabled) car spaces (capacity).
  """
  wheelchairAccessibleCarPlaces: Boolean
  """The capacity (maximum available spaces) of this vehicle parking."""
  capacity: VehicleParkingSpaces
  """The currently available spaces at this vehicle parking."""
  availability: VehicleParkingSpaces
  """Opening hours of the parking facility"""
  openingHours: OpeningHours
}


"""
The state of the vehicle parking. TEMPORARILY_CLOSED and CLOSED are distinct states so that they
may be represented differently to the user.
"""
enum VehicleParkingState {
  """May be used for park and ride."""
  OPERATIONAL
  """Can't be used for park and ride."""
  TEMPORARILY_CLOSED
  """Can't be used for park and ride."""
  CLOSED
}


"""The number of spaces by type. null if unknown."""
type VehicleParkingSpaces {
  """The number of bicycle spaces."""
  bicycleSpaces: Int
  """The number of car spaces."""
  carSpaces: Int
  """The number of wheelchair accessible (disabled) car spaces."""
  wheelchairAccessibleCarSpaces: Int
}


"""
Bike rental station represents a location where users can rent bicycles for a fee.
"""
type BikeRentalStation implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the bike rental station"""
  stationId: String
  """Name of the bike rental station"""
  name: String!
  """
  Number of bikes currently available on the rental station.
  See field \`allowPickupNow\` to know if is currently possible to pick up a bike.
  """
  bikesAvailable: Int
  """
  Number of free spaces currently available on the rental station.
  Note that this value being 0 does not necessarily indicate that bikes cannot be returned
  to this station, as for example it might be possible to leave the bike in the vicinity of
  the rental station, even if the bike racks don't have any spaces available.
  See field \`allowDropoffNow\` to know if is currently possible to return a bike.
  """
  spacesAvailable: Int
  """
  A description of the current state of this bike rental station, e.g. "Station on"
  """
  state: String @deprecated(reason: "Use operative instead")
  """
  If true, values of \`bikesAvailable\` and \`spacesAvailable\` are updated from a
  real-time source. If false, values of \`bikesAvailable\` and \`spacesAvailable\`
  are always the total capacity divided by two.
  """
  realtime: Boolean
  """
  If true, bikes can be returned to this station if the station has spaces available
  or allows overloading.
  """
  allowDropoff: Boolean
  """
  If true, bikes can be picked up from this station if the station has bikes available.
  """
  allowPickup: Boolean
  """If true, bikes can be currently returned to this station."""
  allowDropoffNow: Boolean
  """If true, bikes can be currently picked up from this station."""
  allowPickupNow: Boolean
  networks: [String]
  """Longitude of the bike rental station (WGS 84)"""
  lon: Float
  """Latitude of the bike rental station (WGS 84)"""
  lat: Float
  """Nominal capacity (number of racks) of the rental station."""
  capacity: Int
  """
  If true, bikes can be returned even if spacesAvailable is zero or bikes > capacity.
  """
  allowOverloading: Boolean
  """Platform-specific URLs to begin renting a bike from this station."""
  rentalUris: BikeRentalStationUris
  """If true, station is on and in service."""
  operative: Boolean
}


"""
Vehicle rental station represents a location where users can rent bicycles etc. for a fee.
"""
type VehicleRentalStation implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the vehicle in the format of network:id"""
  stationId: String
  """Name of the vehicle rental station"""
  name: String!
  """
  Number of vehicles currently available on the rental station.
  See field \`allowPickupNow\` to know if is currently possible to pick up a vehicle.
  """
  vehiclesAvailable: Int
  """
  Number of free spaces currently available on the rental station.
  Note that this value being 0 does not necessarily indicate that vehicles cannot be returned
  to this station, as for example it might be possible to leave the vehicle in the vicinity of
  the rental station, even if the vehicle racks don't have any spaces available.
  See field \`allowDropoffNow\` to know if is currently possible to return a vehicle.
  """
  spacesAvailable: Int
  """
  If true, values of \`vehiclesAvailable\` and \`spacesAvailable\` are updated from a
  real-time source. If false, values of \`vehiclesAvailable\` and \`spacesAvailable\`
  are always the total capacity divided by two.
  """
  realtime: Boolean
  """
  If true, vehicles can be returned to this station if the station has spaces available
  or allows overloading.
  """
  allowDropoff: Boolean
  """
  If true, vehicles can be picked up from this station if the station has vehicles available.
  """
  allowPickup: Boolean
  """If true, vehicles can be currently returned to this station."""
  allowDropoffNow: Boolean
  """If true, vehicles can be currently picked up from this station."""
  allowPickupNow: Boolean
  """ID of the rental network."""
  network: String
  """Longitude of the vehicle rental station (WGS 84)"""
  lon: Float
  """Latitude of the vehicle rental station (WGS 84)"""
  lat: Float
  """Nominal capacity (number of racks) of the rental station."""
  capacity: Int
  """
  If true, vehicles can be returned even if spacesAvailable is zero or vehicles > capacity.
  """
  allowOverloading: Boolean
  """Platform-specific URLs to begin renting a vehicle from this station."""
  rentalUris: VehicleRentalUris
  """If true, station is on and in service."""
  operative: Boolean
}


"""Rental vehicle represents a vehicle that belongs to a rental network."""
type RentalVehicle implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the vehicle in the format of network:id"""
  vehicleId: String
  """Name of the vehicle"""
  name: String!
  """If true, vehicle is currently available for renting."""
  allowPickupNow: Boolean
  """ID of the rental network."""
  network: String
  """Longitude of the vehicle (WGS 84)"""
  lon: Float
  """Latitude of the vehicle (WGS 84)"""
  lat: Float
  """Platform-specific URLs to begin the vehicle."""
  rentalUris: VehicleRentalUris
  """If true, vehicle is not disabled."""
  operative: Boolean
  """The type of the rental vehicle (scooter, bicycle, car...)"""
  vehicleType: RentalVehicleType
}


type BikeRentalStationUris {
  """
  A URI that can be passed to an Android app with an {@code android.intent.action.VIEW} Android
  intent to support Android Deep Links.
  May be null if a rental URI does not exist.
  """
  android: String
  """
  A URI that can be used on iOS to launch the rental app for this station.
  May be {@code null} if a rental URI does not exist.
  """
  ios: String
  """
  A URL that can be used by a web browser to show more information about renting a vehicle at
  this station.
  May be {@code null} if a rental URL does not exist.
  """
  web: String
}


type VehicleRentalUris {
  """
  A URI that can be passed to an Android app with an {@code android.intent.action.VIEW} Android
  intent to support Android Deep Links.
  May be null if a rental URI does not exist.
  """
  android: String
  """
  A URI that can be used on iOS to launch the rental app for this rental network.
  May be {@code null} if a rental URI does not exist.
  """
  ios: String
  """
  A URL that can be used by a web browser to show more information about renting a vehicle.
  May be {@code null} if a rental URL does not exist.
  """
  web: String
}


type RentalVehicleType {
  """The vehicle's general form factor"""
  formFactor: FormFactor
  """The primary propulsion type of the vehicle"""
  propulsionType: PropulsionType
}


enum FormFactor {
  """A bicycle"""
  BICYCLE
  """A bicycle with additional space for cargo"""
  CARGO_BICYCLE
  """An automobile"""
  CAR
  """
  A moped that the rider sits on. For a disambiguation see https://github.com/NABSA/gbfs/pull/370#issuecomment-982631989
  """
  MOPED
  """
  A kick scooter that the rider either sits or stands on. Will be deprecated in GBFS v3.0.
  """
  SCOOTER
  """A kick scooter that the rider stands on"""
  SCOOTER_STANDING
  """A kick scooter with a seat"""
  SCOOTER_SEATED
  """A vehicle that doesn't fit into any other category"""
  OTHER
}


enum PropulsionType {
  """Pedal or foot propulsion"""
  HUMAN
  """
  Provides electric motor assist only in combination with human propulsion - no throttle mode
  """
  ELECTRIC_ASSIST
  """Powered by battery-powered electric motor with throttle mode"""
  ELECTRIC
  """Powered by gasoline combustion engine"""
  COMBUSTION
  """Powered by diesel combustion engine"""
  COMBUSTION_DIESEL
  """Powered by combined combustion engine and battery-powered motor"""
  HYBRID
  """
  Powered by combined combustion engine and battery-powered motor with plug-in charging
  """
  PLUG_IN_HYBRID
  """Powered by hydrogen fuel cell powered electric motor"""
  HYDROGEN_FUEL_CELL
}


enum BikesAllowed {
  """There is no bike information for the trip."""
  NO_INFORMATION
  """
  The vehicle being used on this particular trip can accommodate at least one bicycle.
  """
  ALLOWED
  """No bicycles are allowed on this trip."""
  NOT_ALLOWED
}


"""Car park represents a location where cars can be parked."""
type CarPark implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the car park"""
  carParkId: String
  """Name of the car park"""
  name(
    """
    Returns name with the specified language, if found, otherwise returns with some default language.
    """
    language: String
  ): String!
  """Number of parking spaces at the car park"""
  maxCapacity: Int
  """Number of currently available parking spaces at the car park"""
  spacesAvailable: Int
  """
  If true, value of \`spacesAvailable\` is updated from a real-time source.
  """
  realtime: Boolean
  """Longitude of the car park (WGS 84)"""
  lon: Float
  """Latitude of the car park (WGS 84)"""
  lat: Float
  """
  Source specific tags of the car park, which describe the available features.
  """
  tags: [String]
  """
  Opening hours for the selected dates using the local time of the park.
  Each date can have multiple time spans.
  """
  openingHours: OpeningHours
}


"""Cluster is a list of stops grouped by name and proximity"""
type Cluster implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the cluster"""
  gtfsId: String!
  """Name of the cluster"""
  name: String!
  """
  Latitude of the center of this cluster (i.e. average latitude of stops in this cluster)
  """
  lat: Float!
  """
  Longitude of the center of this cluster (i.e. average longitude of stops in this cluster)
  """
  lon: Float!
  """List of stops in the cluster"""
  stops: [Stop!]
}


type Coordinates {
  """Latitude (WGS 84)"""
  lat: Float
  """Longitude (WGS 84)"""
  lon: Float
}


type debugOutput {
  totalTime: Long
  pathCalculationTime: Long
  precalculationTime: Long
  renderingTime: Long
  timedOut: Boolean
}


"""
Departure row is a location, which lists departures of a certain pattern from a
stop. Departure rows are identified with the pattern, so querying departure rows
will return only departures from one stop per pattern
"""
type DepartureRow implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """Stop from which the departures leave"""
  stop: Stop
  """Latitude of the stop (WGS 84)"""
  lat: Float
  """Longitude of the stop (WGS 84)"""
  lon: Float
  """Pattern of the departure row"""
  pattern: Pattern
  """Departures of the pattern from the stop"""
  stoptimes(
    """
    Return rows departing after this time. Time format: Unix timestamp in seconds. Default: current time.
    """
    startTime: Long = 0
    """How many seconds ahead to search for departures. Default is one day."""
    timeRange: Int = 86400
    """Maximum number of departures to return."""
    numberOfDepartures: Int = 1
    """If true, only those departures which allow boarding are returned"""
    omitNonPickups: Boolean = false
    """If false, returns also canceled trips"""
    omitCanceled: Boolean = true
  ): [Stoptime]
}


type elevationProfileComponent {
  """The distance from the start of the step, in meters."""
  distance: Float
  """The elevation at this distance, in meters."""
  elevation: Float
}


type fare {
  type: String
  """ISO 4217 currency code"""
  currency: String
  """
  Fare price in cents. **Note:** this value is dependent on the currency used,
  as one cent is not necessarily ¹/₁₀₀ of the basic monerary unit.
  """
  cents: Int
  """Components which this fare is composed of"""
  components: [fareComponent]
}


"""Component of the fare (i.e. ticket) for a part of the itinerary"""
type fareComponent {
  """ID of the ticket type. Corresponds to \`fareId\` in **TicketType**."""
  fareId: String
  """ISO 4217 currency code"""
  currency: String
  """
  Fare price in cents. **Note:** this value is dependent on the currency used,
  as one cent is not necessarily ¹/₁₀₀ of the basic monerary unit.
  """
  cents: Int
  """List of routes which use this fare component"""
  routes: [Route]
}


"""
A feed provides routing data (stops, routes, timetables, etc.) from one or more public transport agencies.
"""
type Feed {
  """ID of the feed"""
  feedId: String!
  """List of agencies which provide data to this feed"""
  agencies: [Agency]
  """Alerts relevant for the feed."""
  alerts(
    """Returns alerts for these types that are relevant for the feed."""
    types: [FeedAlertType!]
  ): [Alert]
}


"""Entities, which are relevant for a feed and can contain alerts"""
enum FeedAlertType {
  """Alerts affecting the feed's agencies"""
  AGENCIES
  """
  Alerts affecting the route types across the feed.
  There might be alerts that only affect route types within an agency of the feed,
  and those can be fetched through the Agency.
  """
  ROUTE_TYPES
}


enum FilterPlaceType {
  """Stops"""
  STOP
  """Departure rows"""
  DEPARTURE_ROW
  """Old value for VEHICLE_RENT"""
  BICYCLE_RENT @deprecated(reason: "Use VEHICLE_RENT instead as it's clearer that it also returns rental scooters, cars...")
  """Vehicle (bicycles, scooters, cars ...) rental stations and vehicles"""
  VEHICLE_RENT
  """Parking lots (not rental stations) that contain spaces for bicycles"""
  BIKE_PARK
  """Parking lots that contain spaces for cars"""
  CAR_PARK
}


type Geometry {
  """The number of points in the string"""
  length: Int
  """
  List of coordinates of in a Google encoded polyline format (see
  https://developers.google.com/maps/documentation/utilities/polylinealgorithm)
  """
  points: Polyline
}


scalar GeoJson


type StopGeometries {
  """
  Representation of the stop geometries as GeoJSON (https://geojson.org/)
  """
  geoJson: GeoJson
  """
  Representation of a stop as a series of polylines.
  
  Polygons of flex stops are represented as linear rings (lines where the first and last point are the same).
  
  Proper stops are represented as single point "lines".
  """
  googleEncoded: [Geometry]
}


input InputBanned {
  """A comma-separated list of banned route ids"""
  routes: String
  """A comma-separated list of banned agency ids"""
  agencies: String
  """A comma-separated list of banned trip ids"""
  trips: String
  """
  A comma-separated list of banned stop ids. Note that these stops are only
  banned for boarding and disembarking vehicles — it is possible to get an
  itinerary where a vehicle stops at one of these stops
  """
  stops: String
  """
  A comma-separated list of banned stop ids. Only itineraries where these stops
  are not travelled through are returned, e.g. if a bus route stops at one of
  these stops, that route will not be used in the itinerary, even if the stop is
  not used for boarding or disembarking the vehicle.
  """
  stopsHard: String
}


input InputCoordinates {
  """Latitude of the place (WGS 84)"""
  lat: Float!
  """Longitude of the place (WGS 84)"""
  lon: Float!
  """
  The name of the place. If specified, the place name in results uses this value instead of \`"Origin"\` or \`"Destination"\`
  """
  address: String
  """
  The amount of time, in seconds, to spend at this location before venturing forth.
  """
  locationSlack: Int
}


input InputFilters {
  """Stops to include by GTFS id."""
  stops: [String]
  """Routes to include by GTFS id."""
  routes: [String]
  """Bike rentals to include by id (without network identifier)."""
  bikeRentalStations: [String]
  """Bike parks to include by id."""
  bikeParks: [String]
  """Car parks to include by id."""
  carParks: [String]
}


input InputModeWeight {
  """
  The weight of TRAM traverse mode. Values over 1 add cost to tram travel and values under 1 decrease cost
  """
  TRAM: Float
  """
  The weight of SUBWAY traverse mode. Values over 1 add cost to subway travel and values under 1 decrease cost
  """
  SUBWAY: Float
  """
  The weight of RAIL traverse mode. Values over 1 add cost to rail travel and values under 1 decrease cost
  """
  RAIL: Float
  """
  The weight of BUS traverse mode. Values over 1 add cost to bus travel and values under 1 decrease cost
  """
  BUS: Float
  """
  The weight of FERRY traverse mode. Values over 1 add cost to ferry travel and values under 1 decrease cost
  """
  FERRY: Float
  """
  The weight of CABLE_CAR traverse mode. Values over 1 add cost to cable car travel and values under 1 decrease cost
  """
  CABLE_CAR: Float
  """
  The weight of GONDOLA traverse mode. Values over 1 add cost to gondola travel and values under 1 decrease cost
  """
  GONDOLA: Float
  """
  The weight of FUNICULAR traverse mode. Values over 1 add cost to funicular travel and values under 1 decrease cost
  """
  FUNICULAR: Float
  """
  The weight of AIRPLANE traverse mode. Values over 1 add cost to airplane travel and values under 1 decrease cost
  """
  AIRPLANE: Float
}


input InputPreferred {
  """A comma-separated list of ids of the routes preferred by the user."""
  routes: String
  """A comma-separated list of ids of the agencies preferred by the user."""
  agencies: String
  """
  Penalty added for using every route that is not preferred if user set any
  route as preferred. We return number of seconds that we are willing to wait
  for preferred route.
  """
  otherThanPreferredRoutesPenalty: Int
}


"""Preferences for parking facilities used during the routing."""
input VehicleParkingInput {
  """
  Selection filters to include or exclude parking facilities.
  An empty list will include all facilities in the routing search.
  """
  filters: [ParkingFilter]
  """
  If \`preferred\` is non-empty, using a parking facility that doesn't contain
  at least one of the preferred conditions, will receive this extra cost and therefore avoided if
  preferred options are available.
  """
  unpreferredCost: Int
  """
  If non-empty every parking facility that doesn't match this set of conditions will
  receive an extra cost (defined by \`unpreferredCost\`) and therefore avoided.
  """
  preferred: [ParkingFilter]
}


input ParkingFilterOperation {
  """Filter parking facilities based on their tag"""
  tags: [String]
}


"""
The filter definition to include or exclude parking facilities used during routing.

Logically, the filter algorithm work as follows:

- The starting point is the set of all facilities, lets call it \`A\`.
- Then all \`select\` filters are applied to \`A\`, potentially reducing the number of facilities used.
  Let's call the result of this \`B\`.
  An empty \`select\` will lead to \`A\` being equal to \`B\`.
- Lastly, the \`not\` filters are applied to \`B\`, reducing the set further.
  Lets call this final set \`C\`.
  An empty \`not\` will lead to \`B\` being equal to \`C\`.
- The remaining parking facilities in \`C\` are used for routing.
"""
input ParkingFilter {
  """
  Exclude parking facilities based on their properties.
  
  If empty nothing is excluded from the initial set of facilities but may be filtered down
  further by the \`select\` filter.
  """
  not: [ParkingFilterOperation!]
  """
  Include parking facilities based on their properties.
  
  If empty everything is included from the initial set of facilities but may be filtered down
  further by the \`not\` filter.
  """
  select: [ParkingFilterOperation!]
}


"""
Relative importances of optimization factors. Only effective for bicycling legs.
Invariant: \`timeFactor + slopeFactor + safetyFactor == 1\`
"""
input InputTriangle {
  """Relative importance of safety"""
  safetyFactor: Float
  """Relative importance of flat terrain"""
  slopeFactor: Float
  """Relative importance of duration"""
  timeFactor: Float
}


input InputUnpreferred {
  """A comma-separated list of ids of the routes unpreferred by the user."""
  routes: String
  """A comma-separated list of ids of the agencies unpreferred by the user."""
  agencies: String
  """
  An cost function used to calculate penalty for an unpreferred route/agency. Function should return
  number of seconds that we are willing to wait for unpreferred route/agency.
  String must be of the format:
  \`A + B x\`, where A is fixed penalty and B is a multiplier of transit leg travel time x.
  For example: \`600 + 2.0 x\`
  """
  unpreferredCost: String
  """
  Penalty added for using route that is unpreferred, i.e. number of seconds that we are willing to
  wait for route that is unpreferred.
  
  Deprecated: Use \`unpreferredCost\` instead.
  """
  useUnpreferredRoutesPenalty: Int @deprecated(reason: "Use unpreferredCost instead")
}


enum RoutingErrorCode {
  """
  No transit connection was found between the origin and destination withing the operating day or the next day
  """
  NO_TRANSIT_CONNECTION
  """
  Transit connection was found, but it was outside the search window, see metadata for the next search window
  """
  NO_TRANSIT_CONNECTION_IN_SEARCH_WINDOW
  """
  The date specified is outside the range of data currently loaded into the system
  """
  OUTSIDE_SERVICE_PERIOD
  """
  The coordinates are outside the bounds of the data currently loaded into the system
  """
  OUTSIDE_BOUNDS
  """The specified location is not close to any streets or transit stops"""
  LOCATION_NOT_FOUND
  """
  No stops are reachable from the location specified. You can try searching using a different access or egress mode
  """
  NO_STOPS_IN_RANGE
  """
  The origin and destination are so close to each other, that walking is always better, but no direct mode was specified for the search
  """
  WALKING_BETTER_THAN_TRANSIT
  """
  An unknown error happened during the search. The details have been logged to the server logs
  """
  SYSTEM_ERROR
}


enum InputField {
  DATE_TIME
  FROM
  TO
}


"""Description of the reason, why the planner did not return any results"""
type RoutingError {
  """An enum describing the reason"""
  code: RoutingErrorCode!
  """
  An enum describing the field which should be changed, in order for the search to succeed
  """
  inputField: InputField
  """
  A textual description of why the search failed. The clients are expected to have their own translations based on the code, for user visible error messages.
  """
  description: String!
}


type Itinerary {
  """
  Time when the user leaves from the origin. Format: Unix timestamp in milliseconds.
  """
  startTime: Long
  """
  Time when the user arrives to the destination.. Format: Unix timestamp in milliseconds.
  """
  endTime: Long
  """Duration of the trip on this itinerary, in seconds."""
  duration: Long
  """Generalized cost of the itinerary. Used for debugging search results."""
  generalizedCost: Int
  """How much time is spent waiting for transit to arrive, in seconds."""
  waitingTime: Long
  """How much time is spent walking, in seconds."""
  walkTime: Long
  """How far the user has to walk, in meters."""
  walkDistance: Float
  """
  A list of Legs. Each Leg is either a walking (cycling, car) portion of the
  itinerary, or a transit leg on a particular vehicle. So a itinerary where the
  user walks to the Q train, transfers to the 6, then walks to their
  destination, has four legs.
  """
  legs: [Leg]!
  """Information about the fares for this itinerary"""
  fares: [fare]
  """
  How much elevation is gained, in total, over the course of the itinerary, in meters.
  """
  elevationGained: Float
  """
  How much elevation is lost, in total, over the course of the itinerary, in meters.
  """
  elevationLost: Float
  """Does the itinerary end without dropping off the rented bicycle:"""
  arrivedAtDestinationWithRentedBicycle: Boolean
  """
  A list of system notices. Contains debug information for itineraries.
  One use-case is to run a routing search with 'debugItineraryFilter: true'.
  This will then tag itineraries instead of removing them from the result.
  This make it possible to inspect the itinerary-filter-chain.
  """
  systemNotices: [SystemNotice]!
}


"A currency"
type Currency {
  "ISO-4217 currency code, for example \`USD\` or \`EUR\`."
  code: String!
  """
  Fractional digits of this currency. A value of 2 would express that in this currency
  100 minor units make up one major unit.
  
  Expressed more concretely: 100 Euro-cents make up one Euro.
  
  Note: Some currencies don't even have any fractional digits, for example the Japanese Yen.
  
  See also https://en.wikipedia.org/wiki/ISO_4217#Minor_unit_fractions
  """
  digits: Int!
}


"An amount of money."
type Money {
  "The currency of this money amount."
  currency: Currency!
  """
  Money amount in cents. **Note:** this value is dependent on the currency used,
  as one cent is not necessarily ¹/₁₀₀ of the basic monetary unit.
  """
  amount: Int!
}


"""
"
An ISO-8601-formatted duration, i.e. \`PT2H30M\` for 2 hours and 30 minutes.
"""
scalar Duration


type RideHailingProvider {
  "The ID of the ride hailing provider."
  id: String!
}


"An estimate for a ride on a hailed vehicle, like an Uber car."
type RideHailingEstimate {
  "The provider of the ride hailing service."
  provider: RideHailingProvider!
  "The lower bound of the price estimate of this ride."
  minPrice: Money!
  "The upper bound of the price estimate of this ride."
  maxPrice: Money!
  "The estimated time it takes for the vehicle to arrive."
  arrival: Duration!
  "The name of the ride, ie. UberX"
  productName: String
}


type Leg {
  """
  The date and time when this leg begins. Format: Unix timestamp in milliseconds.
  """
  startTime: Long
  """
  The date and time when this leg ends. Format: Unix timestamp in milliseconds.
  """
  endTime: Long
  """
  For transit leg, the offset from the scheduled departure time of the boarding
  stop in this leg, i.e. scheduled time of departure at boarding stop =
  \`startTime - departureDelay\`
  """
  departureDelay: Int
  """
  For transit leg, the offset from the scheduled arrival time of the alighting
  stop in this leg, i.e. scheduled time of arrival at alighting stop = \`endTime
  - arrivalDelay\`
  """
  arrivalDelay: Int
  """The mode (e.g. \`WALK\`) used when traversing this leg."""
  mode: Mode
  """The leg's duration in seconds"""
  duration: Float
  """Generalized cost of the leg. Used for debugging search results."""
  generalizedCost: Int
  """The leg's geometry."""
  legGeometry: Geometry
  """
  For transit legs, the transit agency that operates the service used for this leg. For non-transit legs, \`null\`.
  """
  agency: Agency
  """Whether there is real-time data about this Leg"""
  realTime: Boolean
  """State of real-time data"""
  realtimeState: RealtimeState
  """The distance traveled while traversing the leg in meters."""
  distance: Float
  """Whether this leg is a transit leg or not."""
  transitLeg: Boolean
  "Whether this leg is walking with a bike."
  walkingBike: Boolean
  """Whether this leg is traversed with a rented bike."""
  rentedBike: Boolean
  """The Place where the leg originates."""
  from: Place!
  """The Place where the leg ends."""
  to: Place!
  """
  For transit legs, the route that is used for traversing the leg. For non-transit legs, \`null\`.
  """
  route: Route
  """
  For transit legs, the trip that is used for traversing the leg. For non-transit legs, \`null\`.
  """
  trip: Trip
  """
  For transit legs, the service date of the trip. Format: YYYYMMDD. For non-transit legs, null.
  """
  serviceDate: String
  """
  For transit legs, intermediate stops between the Place where the leg
  originates and the Place where the leg ends. For non-transit legs, null.
  """
  intermediateStops: [Stop]
  """
  For transit legs, intermediate stops between the Place where the leg
  originates and the Place where the leg ends. For non-transit legs, null.
  Returns Place type, which has fields for e.g. departure and arrival times
  """
  intermediatePlaces: [Place]
  """
  Whether the destination of this leg (field \`to\`) is one of the intermediate places specified in the query.
  """
  intermediatePlace: Boolean
  steps: [step]
  """
  This is used to indicate if boarding this leg is possible only with special arrangements.
  """
  pickupType: PickupDropoffType
  """
  This is used to indicate if alighting from this leg is possible only with special arrangements.
  """
  dropoffType: PickupDropoffType
  """
  Interlines with previous leg.
  This is true when the same vehicle is used for the previous leg as for this leg
  and passenger can stay inside the vehicle.
  """
  interlineWithPreviousLeg: Boolean
  dropOffBookingInfo: BookingInfo
  pickupBookingInfo: BookingInfo
  """Applicable alerts for this leg."""
  alerts: [Alert]
  """Future legs with same origin and destination stops or stations"""
  nextLegs(
    """
    The number of alternative legs searched. If fewer than the requested number are found,
    then only the found legs are returned.
    """
    numberOfLegs: Int!
    """
    Transportation modes for which all stops in the parent station are used as possible origin stops
    for the next legs. For modes not listed, only the exact origin stop of the leg is considered.
    """
    originModesWithParentStation: [TransitMode!]
    """
    Transportation modes for which all stops in the parent station are used as possible destination stops
    for the next legs. For modes not listed, only the exact destination stop of the leg is considered.
    """
    destinationModesWithParentStation: [TransitMode!]
  ): [Leg!]
  "Estimate of a hailed ride like Uber."
  rideHailingEstimate: RideHailingEstimate
}


"""A span of time."""
type LocalTimeSpan {
  """The start of the time timespan as seconds from midnight."""
  from: Int!
  """The end of the timespan as seconds from midnight."""
  to: Int!
}


"""
A date using the local timezone of the object that can contain timespans.
"""
type LocalTimeSpanDate {
  """The time spans for this date."""
  timeSpans: [LocalTimeSpan]
  """The date of this time span. Format: YYYYMMDD."""
  date: String!
}


"""Identifies whether this stop represents a stop or station."""
enum LocationType {
  """A location where passengers board or disembark from a transit vehicle."""
  STOP
  """A physical structure or area that contains one or more stop."""
  STATION
  ENTRANCE
}


"""Long type"""
scalar Long


enum Mode {
  """AIRPLANE"""
  AIRPLANE
  """BICYCLE"""
  BICYCLE
  """BUS"""
  BUS
  """CABLE_CAR"""
  CABLE_CAR
  """CAR"""
  CAR
  """COACH"""
  COACH
  """FERRY"""
  FERRY
  """Enables flexible transit for access and egress legs"""
  FLEX
  """Enables flexible transit for access and egress legs"""
  FLEXIBLE @deprecated(reason: "Use FLEX instead")
  """FUNICULAR"""
  FUNICULAR
  """GONDOLA"""
  GONDOLA
  """Only used internally. No use for API users."""
  LEG_SWITCH
  """RAIL"""
  RAIL
  """SCOOTER"""
  SCOOTER
  """SUBWAY"""
  SUBWAY
  """TRAM"""
  TRAM
  """"Private car trips shared with others."""
  CARPOOL
  """A taxi, possibly operated by a public transport agency."""
  TAXI
  """A special transport mode, which includes all public transport."""
  TRANSIT
  """WALK"""
  WALK
  "Electric buses that draw power from overhead wires using poles."
  TROLLEYBUS
  "Railway in which the track consists of a single rail or a beam."
  MONORAIL
}


enum TransitMode {
  """AIRPLANE"""
  AIRPLANE
  """BUS"""
  BUS
  """CABLE_CAR"""
  CABLE_CAR
  """COACH"""
  COACH
  """FERRY"""
  FERRY
  """FUNICULAR"""
  FUNICULAR
  """GONDOLA"""
  GONDOLA
  """RAIL"""
  RAIL
  """SUBWAY"""
  SUBWAY
  """TRAM"""
  TRAM
  """"Private car trips shared with others."""
  CARPOOL
  """A taxi, possibly operated by a public transport agency."""
  TAXI
  "Electric buses that draw power from overhead wires using poles."
  TROLLEYBUS
  "Railway in which the track consists of a single rail or a beam."
  MONORAIL
}


"""An object with an ID"""
interface Node {
  """The ID of an object"""
  id: ID!
}


"""Optimization type for bicycling legs"""
enum OptimizeType {
  """Prefer faster routes"""
  QUICK
  """
  Prefer safer routes, i.e. avoid crossing streets and use bike paths when possible
  """
  SAFE
  """Prefer flat terrain"""
  FLAT
  """GREENWAYS"""
  GREENWAYS
  """
  **TRIANGLE** optimization type can be used to set relative preferences of optimization factors. See argument \`triangle\`.
  """
  TRIANGLE
}


"""Information about pagination in a connection."""
type PageInfo {
  """When paginating forwards, are there more items?"""
  hasNextPage: Boolean!
  """When paginating backwards, are there more items?"""
  hasPreviousPage: Boolean!
  """When paginating backwards, the cursor to continue."""
  startCursor: String
  """When paginating forwards, the cursor to continue."""
  endCursor: String
}


"""Realtime vehicle position"""
type VehiclePosition {
  """
  Feed-scoped ID that uniquely identifies the vehicle in the format FeedId:VehicleId
  """
  vehicleId: String
  """
  Human-readable label of the vehicle, eg. a publicly visible number or a license plate
  """
  label: String
  """Latitude of the vehicle"""
  lat: Float
  """Longitude of the vehicle"""
  lon: Float
  """The current stop where the vehicle will be or is currently arriving."""
  stopRelationship: StopRelationship
  """Speed of the vehicle in meters/second"""
  speed: Float
  """
  Bearing, in degrees, clockwise from North, i.e., 0 is North and 90 is East. This can be the
  compass bearing, or the direction towards the next stop or intermediate location.
  """
  heading: Float
  """
  When the position of the vehicle was recorded in seconds since the UNIX epoch.
  """
  lastUpdated: Long
  """Which trip this vehicles runs on."""
  trip: Trip!
}


"""Upcoming or current stop and how close the vehicle is to it."""
type StopRelationship {
  """How close the vehicle is to \`stop\`"""
  status: VehicleStopStatus!
  stop: Stop!
}


"""How close the vehicle is to the stop."""
enum VehicleStopStatus {
  """The vehicle is standing at the stop."""
  STOPPED_AT
  """The vehicle has departed the previous stop and is in transit."""
  IN_TRANSIT_TO
  """
  The vehicle is just about to arrive at the stop (on a stop display, the vehicle symbol typically flashes).
  """
  INCOMING_AT
}


"""
Pattern is sequence of stops used by trips on a specific direction and variant
of a route. Most routes have only two patterns: one for outbound trips and one
for inbound trips
"""
type Pattern implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """The route this pattern runs on"""
  route: Route!
  """
  Direction of the pattern. Possible values: 0, 1 or -1.
  -1 indicates that the direction is irrelevant, i.e. the route has patterns only in one direction.
  """
  directionId: Int
  """
  Name of the pattern. Pattern name can be just the name of the route or it can
  include details of destination and origin stops.
  """
  name: String
  """ID of the pattern"""
  code: String!
  """Vehicle headsign used by trips of this pattern"""
  headsign: String
  """Trips which run on this pattern"""
  trips: [Trip!]
  """Trips which run on this pattern on the specified date"""
  tripsForDate(
    """Return trips of the pattern active on this date. Format: YYYYMMDD"""
    serviceDate: String
  ): [Trip!]
  """List of stops served by this pattern"""
  stops: [Stop!]
  geometry: [Coordinates]
  """
  Coordinates of the route of this pattern in Google polyline encoded format
  """
  patternGeometry: Geometry
  """
  Hash code of the pattern. This value is stable and not dependent on the
  pattern id, i.e. this value can be used to check whether two patterns are the
  same, even if their ids have changed.
  """
  semanticHash: String
  """
  By default, list of alerts which have directly an effect on just the pattern.
  It's also possible to return other relevant alerts through defining types.
  """
  alerts(
    """
    Returns alerts for these types that are relevant for the pattern.
    By default, list of alerts which have directly an effect on just the pattern.
    """
    types: [PatternAlertType]
  ): [Alert]
  """Realtime-updated position of vehicles that are serving this pattern."""
  vehiclePositions: [VehiclePosition!]
  """Original Trip pattern for changed patterns"""
  originalTripPattern: Pattern
}


"""Entities, which are relevant for a pattern and can contain alerts"""
enum PatternAlertType {
  """Alerts affecting the pattern"""
  PATTERN
  """Alerts affecting the pattern's route's agency"""
  AGENCY
  """Alerts affecting the route that the pattern runs on"""
  ROUTE
  """Alerts affecting the route type of the route that the pattern runs on"""
  ROUTE_TYPE
  """Alerts affecting the trips which run on this pattern"""
  TRIPS
  """Alerts affecting the stops which are on this pattern"""
  STOPS_ON_PATTERN
  """Alerts affecting the stops of the trips which run on this pattern"""
  STOPS_ON_TRIPS
}


enum PickupDropoffType {
  """Regularly scheduled pickup / drop off."""
  SCHEDULED
  """No pickup / drop off available."""
  NONE
  """Must phone agency to arrange pickup / drop off."""
  CALL_AGENCY
  """Must coordinate with driver to arrange pickup / drop off."""
  COORDINATE_WITH_DRIVER
}


type ContactInfo {
  contactPerson: String
  phoneNumber: String
  eMail: String
  faxNumber: String
  infoUrl: String
  bookingUrl: String
  additionalDetails: String
}


type BookingTime {
  time: String
  daysPrior: Int
}


type BookingInfo {
  contactInfo: ContactInfo
  earliestBookingTime: BookingTime
  latestBookingTime: BookingTime
  minimumBookingNoticeSeconds: Long
  maximumBookingNoticeSeconds: Long
  message: String
  pickupMessage: String
  dropOffMessage: String
}


type Place {
  """
  For transit stops, the name of the stop. For points of interest, the name of the POI.
  """
  name: String
  """
  Type of vertex. (Normal, Bike sharing station, Bike P+R, Transit stop) Mostly
  used for better localization of bike sharing and P+R station names
  """
  vertexType: VertexType
  """Latitude of the place (WGS 84)"""
  lat: Float!
  """Longitude of the place (WGS 84)"""
  lon: Float!
  """
  The time the rider will arrive at the place. Format: Unix timestamp in milliseconds.
  """
  arrivalTime: Long!
  """
  The time the rider will depart the place. Format: Unix timestamp in milliseconds.
  """
  departureTime: Long!
  """The stop related to the place."""
  stop: Stop
  """The bike rental station related to the place"""
  bikeRentalStation: BikeRentalStation @deprecated(reason: "Use vehicleRentalStation and rentalVehicle instead")
  """The vehicle rental station related to the place"""
  vehicleRentalStation: VehicleRentalStation
  """The rental vehicle related to the place"""
  rentalVehicle: RentalVehicle
  """The bike parking related to the place"""
  bikePark: BikePark @deprecated(reason: "bikePark is deprecated. Use vehicleParking instead.")
  """The car parking related to the place"""
  carPark: CarPark @deprecated(reason: "carPark is deprecated. Use vehicleParking instead.")
  """The vehicle parking related to the place"""
  vehicleParking: VehicleParking
}


type placeAtDistance implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  place: PlaceInterface
  """Walking distance to the place along streets and paths"""
  distance: Int
}


"""A connection to a list of items."""
type placeAtDistanceConnection {
  edges: [placeAtDistanceEdge]
  pageInfo: PageInfo!
}


"""An edge in a connection."""
type placeAtDistanceEdge {
  """The item at the end of the edge"""
  node: placeAtDistance
  cursor: String!
}


"""Interface for places, e.g. stops, stations, parking areas.."""
interface PlaceInterface {
  id: ID!
  """Latitude of the place (WGS 84)"""
  lat: Float
  """Longitude of the place (WGS 84)"""
  lon: Float
}


type Plan {
  """The time and date of travel. Format: Unix timestamp in milliseconds."""
  date: Long
  """The origin"""
  from: Place!
  """The destination"""
  to: Place!
  """A list of possible itineraries"""
  itineraries: [Itinerary]!
  """A list of possible error messages as enum"""
  messageEnums: [String]!
  """A list of possible error messages in cleartext"""
  messageStrings: [String]!
  """A list of routing errors, and fields which caused them"""
  routingErrors: [RoutingError!]!
  """
  Use the cursor to go to the next "page" of itineraries. Copy the cursor from the last response
  to the pageCursor query parameter and keep the original request as is. This will enable you to
  search for itineraries in the next search-window.
  The cursor based paging only support stepping to the next page, as it does not support jumping.
  This is only usable when public transportation mode(s) are included in the query.
  """
  nextPageCursor: String
  """
  Use the cursor to go to the previous "page" of itineraries. Copy the cursor from the last
  response to the pageCursor query parameter and keep the original request otherwise as is.
  This will enable you to search for itineraries in the previous search-window.
  The cursor based paging only support stepping to the previous page, as it does not support
  jumping.
  This is only usable when public transportation mode(s) are included in the query.
  """
  previousPageCursor: String
  """
  This is the suggested search time for the "previous page" or time window. Insert it together
  with the searchWindowUsed in the request to get a new set of trips preceding in the
  search-window BEFORE the current search. No duplicate trips should be returned, unless a trip
  is delayed and new realtime-data is available.
  """
  prevDateTime: Long @deprecated(reason: "Use previousPageCursor instead")
  """
  This is the suggested search time for the "next page" or time window. Insert it together
  with the searchWindowUsed in the request to get a new set of trips following in the
  search-window AFTER the current search. No duplicate trips should be returned, unless a trip
  is delayed and new realtime-data is available.
  """
  nextDateTime: Long @deprecated(reason: "Use nextPageCursor instead")
  """
  This is the \`searchWindow\` used by the raptor search. It is provided here for debugging
  purpousess.
  
  The unit is seconds.
  """
  searchWindowUsed: Long
  """Information about the timings for the plan generation"""
  debugOutput: debugOutput!
}


"""
List of coordinates in an encoded polyline format (see
https://developers.google.com/maps/documentation/utilities/polylinealgorithm).
The value appears in JSON as a string.
"""
scalar Polyline


"""
Additional qualifier for a transport mode.
Note that qualifiers can only be used with certain transport modes.
"""
enum Qualifier {
  """The vehicle used for transport can be rented"""
  RENT
  """
  ~~HAVE~~
  **Currently not used**
  """
  HAVE @deprecated(reason: "Currently not used")
  """
  The vehicle used must be left to a parking area before continuing the journey.
  This qualifier is usable with transport modes \`CAR\` and \`BICYCLE\`.
  Note that the vehicle is only parked if the journey is continued with public
  transportation (e.g. if only \`CAR\` and \`WALK\` transport modes are allowed to
  be used, the car will not be parked as it is used for the whole journey).
  """
  PARK
  """
  ~~KEEP~~
  **Currently not used**
  """
  KEEP @deprecated(reason: "Currently not used")
  """The user can be picked up by someone else riding a vehicle"""
  PICKUP
  """The user can be dropped off by someone else riding a vehicle"""
  DROPOFF
  """The mode is used for the access part of the search."""
  ACCESS
  """The mode is used for the egress part of the search."""
  EGRESS
  """The mode is used for the direct street search."""
  DIRECT
  """Hailing a ride, for example via an app like Uber."""
  HAIL
}


type QueryType {
  """Fetches an object given its ID"""
  node(
    """The ID of an object"""
    id: ID!
  ): Node
  """Get all available feeds"""
  feeds: [Feed]
  """Get all agencies"""
  agencies: [Agency]
  """Return list of available ticket types"""
  ticketTypes: [TicketType]
  """
  Get a single agency based on agency ID, i.e. value of field \`gtfsId\` (ID format is \`FeedId:StopId\`)
  """
  agency(id: String!): Agency
  """Get all stops"""
  stops(
    """Return stops with these ids"""
    ids: [String]
    """Query stops by this name"""
    name: String
  ): [Stop]
  """Get all stops within the specified bounding box"""
  stopsByBbox(
    """Southern bound of the bounding box"""
    minLat: Float!
    """Western bound of the bounding box"""
    minLon: Float!
    """Northern bound of the bounding box"""
    maxLat: Float!
    """Eastern bound of the bounding box"""
    maxLon: Float!
    """List of feed ids from which stops are returned"""
    feeds: [String!]
  ): [Stop]
  """
  Get all stops within the specified radius from a location. The returned type
  is a Relay connection (see
  https://facebook.github.io/relay/graphql/connections.htm). The stopAtDistance
  type has two values: stop and distance.
  """
  stopsByRadius(
    """Latitude of the location (WGS 84)"""
    lat: Float!
    """Longitude of the location (WGS 84)"""
    lon: Float!
    """
    Radius (in meters) to search for from the specified location. Note that this
    is walking distance along streets and paths rather than a geographic distance.
    """
    radius: Int!
    """List of feed ids from which stops are returned"""
    feeds: [String!]
    before: String
    after: String
    first: Int
    last: Int
  ): stopAtDistanceConnection
  """
  Get all places (stops, stations, etc. with coordinates) within the specified
  radius from a location. The returned type is a Relay connection (see
  https://facebook.github.io/relay/graphql/connections.htm). The placeAtDistance
  type has two fields: place and distance. The search is done by walking so the
  distance is according to the network of walkable streets and paths.
  """
  nearest(
    """Latitude of the location (WGS 84)"""
    lat: Float!
    """Longitude of the location (WGS 84)"""
    lon: Float!
    """
    Maximum distance (in meters) to search for from the specified location. Note
    that this is walking distance along streets and paths rather than a
    geographic distance. Default is 2000m
    """
    maxDistance: Int = 2000
    """
    Maximum number of results. Search is stopped when this limit is reached. Default is 20.
    """
    maxResults: Int = 20
    """
    Only return places that are one of these types, e.g. \`STOP\` or \`BICYCLE_RENT\`
    """
    filterByPlaceTypes: [FilterPlaceType]
    """
    Only return places that are related to one of these transport modes. This
    argument can be used to return e.g. only nearest railway stations or only
    nearest places related to bicycling.
    """
    filterByModes: [Mode]
    """Only include places that match one of the given GTFS ids."""
    filterByIds: InputFilters
    before: String
    after: String
    first: Int
    last: Int
  ): placeAtDistanceConnection @async
  """
  Get a single departure row based on its ID (ID format is \`FeedId:StopId:PatternId\`)
  """
  departureRow(id: String!): DepartureRow
  """
  Get a single stop based on its ID, i.e. value of field \`gtfsId\` (ID format is \`FeedId:StopId\`)
  """
  stop(id: String!): Stop
  """
  Get a single station based on its ID, i.e. value of field \`gtfsId\` (format is \`FeedId:StopId\`)
  """
  station(id: String!): Stop
  """Get all stations"""
  stations(
    """Only return stations that match one of the ids in this list"""
    ids: [String]
    """Query stations by name"""
    name: String
  ): [Stop]
  """Get all routes"""
  routes(
    """Only return routes with these ids"""
    ids: [String]
    """Only return routes with these feedIds"""
    feeds: [String]
    """Query routes by this name"""
    name: String
    """Only include routes, which use one of these modes"""
    transportModes: [Mode]
  ): [Route]
  """
  Get a single route based on its ID, i.e. value of field \`gtfsId\` (format is \`FeedId:RouteId\`)
  """
  route(id: String!): Route
  """Get all trips"""
  trips(
    """Only return trips with these feedIds"""
    feeds: [String]
  ): [Trip]
  """
  Get a single trip based on its ID, i.e. value of field \`gtfsId\` (format is \`FeedId:TripId\`)
  """
  trip(id: String!): Trip
  """
  Finds a trip matching the given parameters. This query type is useful if the
  id of a trip is not known, but other details uniquely identifying the trip are
  available from some source (e.g. MQTT vehicle positions).
  """
  fuzzyTrip(
    """id of the route"""
    route: String!
    """
    Direction of the trip, possible values: 0, 1 or -1.
    -1 indicates that the direction is irrelevant, i.e. in case the route has
    trips only in one direction. See field \`directionId\` of Pattern.
    """
    direction: Int = -1
    """Departure date of the trip, format: YYYY-MM-DD"""
    date: String!
    """
    Departure time of the trip, format: seconds since midnight of the departure date
    """
    time: Int!
  ): Trip
  """Get cancelled TripTimes."""
  cancelledTripTimes(
    """Feed feedIds (e.g. ["HSL"])."""
    feeds: [String]
    """Route gtfsIds (e.g. ["HSL:1098"])."""
    routes: [String]
    """TripPattern codes (e.g. ["HSL:1098:1:01"])."""
    patterns: [String]
    """Trip gtfsIds (e.g. ["HSL:1098_20190405_Ma_2_1455"])."""
    trips: [String]
    """
    Only cancelled trip times scheduled to run on minDate or after are returned. Format: "2019-12-23" or "20191223".
    """
    minDate: String
    """
    Only cancelled trip times scheduled to run on maxDate or before are returned. Format: "2019-12-23" or "20191223".
    """
    maxDate: String
    """
    Only cancelled trip times that have first stop departure time at
    minDepartureTime or after are returned. Format: seconds since midnight of minDate.
    """
    minDepartureTime: Int
    """
    Only cancelled trip times that have first stop departure time at
    maxDepartureTime or before are returned. Format: seconds since midnight of maxDate.
    """
    maxDepartureTime: Int
    """
    Only cancelled trip times that have last stop arrival time at minArrivalTime
    or after are returned. Format: seconds since midnight of minDate.
    """
    minArrivalTime: Int
    """
    Only cancelled trip times that have last stop arrival time at maxArrivalTime
    or before are returned. Format: seconds since midnight of maxDate.
    """
    maxArrivalTime: Int
  ): [Stoptime]
  """Get all patterns"""
  patterns: [Pattern]
  """
  Get a single pattern based on its ID, i.e. value of field \`code\` (format is
  \`FeedId:RouteId:DirectionId:PatternVariantNumber\`)
  """
  pattern(id: String!): Pattern
  """Get all clusters"""
  clusters: [Cluster]
  """Get a single cluster based on its ID, i.e. value of field \`gtfsId\`"""
  cluster(id: String!): Cluster
  """Get all active alerts"""
  alerts(
    """Only return alerts in these feeds"""
    feeds: [String!]
    """Only return alerts with these severity levels"""
    severityLevel: [AlertSeverityLevelType!]
    """Only return alerts with these effects"""
    effect: [AlertEffectType!]
    """Only return alerts with these causes"""
    cause: [AlertCauseType!]
    """Only return alerts affecting these routes"""
    route: [String!]
    """Only return alerts affecting these stops"""
    stop: [String!]
  ): [Alert]
  """Get the time range for which the API has data available"""
  serviceTimeRange: serviceTimeRange
  """Get all bike rental stations"""
  bikeRentalStations(
    """
    Return bike rental stations with these ids.
    **Note:** if an id is invalid (or the bike rental station service is unavailable)
    the returned list will contain \`null\` values.
    """
    ids: [String]
  ): [BikeRentalStation] @deprecated(reason: "Use rentalVehicles or vehicleRentalStations instead")
  """
  Get a single bike rental station based on its ID, i.e. value of field \`stationId\`
  """
  bikeRentalStation(id: String!): BikeRentalStation @deprecated(reason: "Use rentalVehicle or vehicleRentalStation instead")
  """Get all rental vehicles"""
  rentalVehicles(
    """
    Return rental vehicles with these ids, i.e. value of field \`vehicleId\`.
    **Note:** if an id is invalid (or the rental service is unavailable)
    the returned list will contain \`null\` values.
    
    If this is provided all other filters are ignored.
    """
    ids: [String]
    """Return only rental vehicles that have this form factor."""
    formFactors: [FormFactor]
  ): [RentalVehicle]
  """
  Get a single rental vehicle based on its ID, i.e. value of field \`vehicleId\`
  """
  rentalVehicle(id: String!): RentalVehicle
  """Get all vehicle rental stations"""
  vehicleRentalStations(
    """
    Return vehicle rental stations with these ids, i.e. value of field \`stationId\`.
    **Note:** if an id is invalid (or the rental service is unavailable)
    the returned list will contain \`null\` values.
    """
    ids: [String]
  ): [VehicleRentalStation]
  """
  Get a single vehicle rental station based on its ID, i.e. value of field \`stationId\`
  """
  vehicleRentalStation(id: String!): VehicleRentalStation
  """Get all bike parks"""
  bikeParks: [BikePark] @deprecated(reason: "bikeParks is deprecated. Use vehicleParkings instead.")
  """
  Get a single bike park based on its ID, i.e. value of field \`bikeParkId\`
  """
  bikePark(id: String!): BikePark @deprecated(reason: "bikePark is deprecated. Use vehicleParking instead.")
  """Get all car parks"""
  carParks(
    """
    Return car parks with these ids.
    **Note:** if an id is invalid (or the car park service is unavailable) the returned list will contain \`null\` values.
    """
    ids: [String]
  ): [CarPark] @deprecated(reason: "carParks is deprecated. Use vehicleParkings instead.")
  """Get a single car park based on its ID, i.e. value of field \`carParkId\`"""
  carPark(id: String!): CarPark @deprecated(reason: "carPark is deprecated. Use vehicleParking instead.")
  """Get all vehicle parkings"""
  vehicleParkings(
    """
    Return vehicle parkings with these ids.
    **Note:** if an id is invalid (or the vehicle parking service is unavailable)
    the returned list will contain \`null\` values.
    """
    ids: [String]
  ): [VehicleParking]
  """Get a single vehicle parking based on its ID"""
  vehicleParking(id: String!): VehicleParking
  """Needed until https://github.com/facebook/relay/issues/112 is resolved"""
  viewer: QueryType
  """
  Plans an itinerary from point A to point B based on the given arguments
  """
  plan(
    """
    Date of departure or arrival in format YYYY-MM-DD. Default value: current date
    """
    date: String
    """
    Time of departure or arrival in format hh:mm:ss. Default value: current time
    """
    time: String
    """
    The geographical location where the itinerary begins.
    Use either this argument or \`fromPlace\`, but not both.
    """
    from: InputCoordinates
    """
    The geographical location where the itinerary ends.
    Use either this argument or \`toPlace\`, but not both.
    """
    to: InputCoordinates
    """
    The place where the itinerary begins in format \`name::place\`, where \`place\`
    is either a lat,lng pair (e.g. \`Pasila::60.199041,24.932928\`) or a stop id
    (e.g. \`Pasila::HSL:1000202\`).
    Use either this argument or \`from\`, but not both.
    """
    fromPlace: String
    """
    The place where the itinerary ends in format \`name::place\`, where \`place\` is
    either a lat,lng pair (e.g. \`Pasila::60.199041,24.932928\`) or a stop id
    (e.g. \`Pasila::HSL:1000202\`).
    Use either this argument or \`to\`, but not both.
    """
    toPlace: String
    """
    Whether the itinerary must be wheelchair accessible. Default value: false
    """
    wheelchair: Boolean
    """The maximum number of itineraries to return. Default value: 3."""
    numItineraries: Int = 3
    """
    The length of the search-window in seconds. This parameter is optional.
    
    The search-window is defined as the duration between the earliest-departure-time(EDT) and
    the latest-departure-time(LDT). OTP will search for all itineraries in this departure
    window. If \`arriveBy=true\` the \`dateTime\` parameter is the latest-arrival-time, so OTP
    will dynamically calculate the EDT. Using a short search-window is faster than using a
    longer one, but the search duration is not linear. Using a \\"too\\" short search-window will
    waste resources server side, while using a search-window that is too long will be slow.
    
    OTP will dynamically calculate a reasonable value for the search-window, if not provided.
    The calculation comes with a significant overhead (10-20% extra). Whether you should use the
    dynamic calculated value or pass in a value depends on your use-case. For a travel planner
    in a small geographical area, with a dense network of public transportation, a fixed value
    between 40 minutes and 2 hours makes sense. To find the appropriate search-window, adjust
    it so that the number of itineraries on average is around the wanted \`numItineraries\`. Make
    sure you set the \`numItineraries\` to a high number while testing. For a country wide area
    like Norway, using the dynamic search-window is the best.
    
    When paginating, the search-window is calculated using the \`numItineraries\` in the original
    search together with statistics from the search for the last page. This behaviour is
    configured server side, and can not be overridden from the client.
    
    The search-window used is returned to the response metadata as \`searchWindowUsed\` for
    debugging purposes.
    """
    searchWindow: Long
    """
    Use the cursor to get the next or previous page of results.
    The next page is a set of itineraries departing after the last itinerary in this result and
    the previous page is a set of itineraries departing before the first itinerary.
    This is only usable when public transportation mode(s) are included in the query.
    """
    pageCursor: String
    """
    The maximum distance (in meters) the user is willing to walk per walking
    section. If the only transport mode allowed is \`WALK\`, then the value of
    this argument is ignored.
    Default: 2000m
    Maximum value: 15000m
    **Note:** If this argument has a relatively small value and only some
    transport modes are allowed (e.g. \`WALK\` and \`RAIL\`), it is possible to get
    an itinerary which has (useless) back and forth public transport legs to
    avoid walking too long distances.
    
    DEPRECATED - Use walkReluctance instead
    """
    maxWalkDistance: Float @deprecated(reason: "Does nothing. Use walkReluctance instead.")
    """
    The maximum time (in seconds) of pre-transit travel when using
    drive-to-transit (park and ride or kiss and ride). Default value: 1800.
    
    DEPRECATED - Use walkReluctance or future reluctance parameters for other modes
    """
    maxPreTransitTime: Int
    """
    How expensive it is to drive a car when car&parking, increase this value to
    make car driving legs shorter. Default value: 1.
    """
    carParkCarLegWeight: Float
    """
    How easily bad itineraries are filtered from results. Value 0 (default)
    disables filtering. Itineraries are filtered if they are worse than another
    one in some respect (e.g. more walking) by more than the percentage of
    filtering level, which is calculated by dividing 100% by the value of this
    argument (e.g. \`itineraryFiltering = 0.5\` → 200% worse itineraries are filtered).
    """
    itineraryFiltering: Float
    """
    A multiplier for how bad biking is, compared to being in transit for equal
    lengths of time. Default value: 2.0
    """
    bikeReluctance: Float
    """
    A multiplier for how bad walking with a bike is, compared to being in transit for equal
    lengths of time. Default value: 5.0
    """
    bikeWalkingReluctance: Float
    """
    A multiplier for how bad driving is, compared to being in transit for equal
    lengths of time. Default value: 3.0
    """
    carReluctance: Float
    """
    A multiplier for how bad walking is, compared to being in transit for equal
    lengths of time. Empirically, values between 2 and 4 seem to correspond
    well to the concept of not wanting to walk too much without asking for
    totally ridiculous itineraries, but this observation should in no way be
    taken as scientific or definitive. Your mileage may vary. See
    https://github.com/opentripplanner/OpenTripPlanner/issues/4090 for impact on
    performance with high values. Default value: 2.0
    """
    walkReluctance: Float
    """
    How much more reluctant is the user to walk on streets with car traffic allowed. Default value: 1.0
    
    Deprecated, use walkSafetyFactor instead.
    """
    walkOnStreetReluctance: Float @deprecated(reason: "Use walkSafetyFactor instead")
    """
    How much worse is waiting for a transit vehicle than being on a transit
    vehicle, as a multiplier. The default value treats wait and on-vehicle time
    as the same. It may be tempting to set this higher than walkReluctance (as
    studies often find this kind of preferences among riders) but the planner
    will take this literally and walk down a transit line to avoid waiting at a
    stop. This used to be set less than 1 (0.95) which would make waiting
    offboard preferable to waiting onboard in an interlined trip. That is also
    undesirable. If we only tried the shortest possible transfer at each stop to
    neighboring stop patterns, this problem could disappear. Default value: 1.0.
    """
    waitReluctance: Float
    """
    How much less bad is waiting at the beginning of the trip (replaces
    \`waitReluctance\` on the first boarding). Default value: 0.4
    
    Deprecated, not used, the timetable-view replaces this functionality.
    """
    waitAtBeginningFactor: Float @deprecated(reason: "Removed in OTP 2")
    """
    Max walk speed along streets, in meters per second. Default value: 1.33
    """
    walkSpeed: Float
    """Max bike speed along streets, in meters per second. Default value: 5.0"""
    bikeSpeed: Float
    """Time to get on and off your own bike, in seconds. Default value: 0"""
    bikeSwitchTime: Int
    """
    Cost of getting on and off your own bike. Unit: seconds. Default value: 0
    """
    bikeSwitchCost: Int
    """
    Optimization type for bicycling legs, e.g. prefer flat terrain. Default value: \`QUICK\`
    """
    optimize: OptimizeType
    """
    Triangle optimization parameters for bicycling legs. Only effective when \`optimize\` is set to **TRIANGLE**.
    """
    triangle: InputTriangle
    """
    Whether the itinerary should depart at the specified time (false), or arrive
    to the destination at the specified time (true). Default value: false.
    """
    arriveBy: Boolean
    """An ordered list of intermediate locations to be visited."""
    intermediatePlaces: [InputCoordinates]
    """
    List of routes and agencies which are given higher preference when planning the itinerary
    """
    preferred: InputPreferred
    """
    List of routes and agencies which are given lower preference when planning the itinerary
    """
    unpreferred: InputUnpreferred
    """
    This prevents unnecessary transfers by adding a cost for boarding a vehicle. Unit: seconds. Default value: 600
    """
    walkBoardCost: Int
    """
    Separate cost for boarding a vehicle with a bicycle, which is more difficult
    than on foot. Unit: seconds. Default value: 600
    """
    bikeBoardCost: Int
    """
    List of routes, trips, agencies and stops which are not used in the itinerary
    """
    banned: InputBanned
    """
    An extra penalty added on transfers (i.e. all boardings except the first
    one). Not to be confused with bikeBoardCost and walkBoardCost, which are the
    cost of boarding a vehicle with and without a bicycle. The boardCosts are
    used to model the 'usual' perceived cost of using a transit vehicle, and the
    transferPenalty is used when a user requests even less transfers. In the
    latter case, we don't actually optimize for fewest transfers, as this can
    lead to absurd results. Consider a trip in New York from Grand Army Plaza
    (the one in Brooklyn) to Kalustyan's at noon. The true lowest transfers
    route is to wait until midnight, when the 4 train runs local the whole way.
    The actual fastest route is the 2/3 to the 4/5 at Nevins to the 6 at Union
    Square, which takes half an hour. Even someone optimizing for fewest
    transfers doesn't want to wait until midnight. Maybe they would be willing
    to walk to 7th Ave and take the Q to Union Square, then transfer to the 6.
    If this takes less than optimize_transfer_penalty seconds, then that's what
    we'll return. Default value: 0.
    """
    transferPenalty: Int
    """
    This argument has no use for itinerary planning and will be removed later.
    ~~When true, do not use goal direction or stop at the target, build a full SPT. Default value: false.~~
    """
    batch: Boolean @deprecated(reason: "Removed in OTP 2")
    """
    List of transportation modes that the user is willing to use. Default: \`["WALK","TRANSIT"]\`
    """
    transportModes: [TransportMode]
    """
    The weight multipliers for transit modes. WALK, BICYCLE, CAR, TRANSIT and LEG_SWITCH are not included.
    """
    modeWeight: InputModeWeight
    """
    Debug the itinerary-filter-chain. The filters will mark itineraries as deleted, but does NOT delete them when this is enabled.
    """
    debugItineraryFilter: Boolean
    """Is bike rental allowed? Default value: false"""
    allowBikeRental: Boolean @deprecated(reason: "Rental is specified by modes")
    """
    Whether arriving at the destination with a rented (station) bicycle is allowed without
    dropping it off. Default: false.
    """
    allowKeepingRentedBicycleAtDestination: Boolean
    """
    The cost of arriving at the destination with the rented bicycle, to discourage doing so.
    Default value: 0.
    """
    keepingRentedBicycleAtDestinationCost: Int
    """
    Invariant: \`boardSlack + alightSlack <= transferSlack\`. Default value: 0
    """
    boardSlack: Int
    """
    Invariant: \`boardSlack + alightSlack <= transferSlack\`. Default value: 0
    """
    alightSlack: Int
    """
    A global minimum transfer time (in seconds) that specifies the minimum
    amount of time that must pass between exiting one transit vehicle and
    boarding another. This time is in addition to time it might take to walk
    between transit stops. Default value: 0
    """
    minTransferTime: Int
    """
    Penalty (in seconds) for using a non-preferred transfer. Default value: 180
    """
    nonpreferredTransferPenalty: Int
    """Maximum number of transfers. Default value: 2"""
    maxTransfers: Int
    """
    This argument has currently no effect on which itineraries are returned. Use
    argument \`fromPlace\` to start the itinerary from a specific stop.
    ~~A transit stop that this trip must start from~~
    """
    startTransitStopId: String
    """
    ID of the trip on which the itinerary starts. This argument can be used to
    plan itineraries when the user is already onboard a vehicle. When using this
    argument, arguments \`time\` and \`from\` should be set based on a vehicle
    position message received from the vehicle running the specified trip.
    **Note:** this argument only takes into account the route and estimated
    travel time of the trip (and therefore arguments \`time\` and \`from\` must be
    used correctly to get meaningful itineraries).
    """
    startTransitTripId: String
    """
    No effect on itinerary planning, adjust argument \`time\` instead to get later departures.
    ~~The maximum wait time in seconds the user is willing to delay trip start. Only effective in Analyst.~~
    """
    claimInitialWait: Long @deprecated(reason: "Removed in OTP 2")
    """
    **Consider this argument experimental** – setting this argument to true
    causes timeouts and unoptimal routes in many cases.
    When true, reverse optimize (find alternative transportation mode, which
    still arrives to the destination in time) this search on the fly after
    processing each transit leg, rather than reverse-optimizing the entire path
    when it's done. Default value: false.
    """
    reverseOptimizeOnTheFly: Boolean @deprecated(reason: "Removed in OTP 2")
    """
    When false, return itineraries using canceled trips. Default value: true.
    """
    omitCanceled: Boolean = true
    """
    When true, realtime updates are ignored during this search. Default value: false
    """
    ignoreRealtimeUpdates: Boolean
    """
    Only useful for testing and troubleshooting.
    ~~If true, the remaining weight heuristic is disabled. Currently only
    implemented for the long distance path service. Default value: false.~~
    """
    disableRemainingWeightHeuristic: Boolean @deprecated(reason: "Removed in OTP 2.2")
    """
    Two-letter language code (ISO 639-1) used for returned text.
    **Note:** only part of the data has translations available and names of
    stops and POIs are returned in their default language. Due to missing
    translations, it is sometimes possible that returned text uses a mixture of two languages.
    """
    locale: String
    """
    List of ticket types that are allowed to be used in itineraries.
    See \`ticketTypes\` query for list of possible ticket types.
    """
    allowedTicketTypes: [String]
    """Tuning parameter for the search algorithm, mainly useful for testing."""
    heuristicStepsPerMainStep: Int
    """
    Whether legs should be compacted by performing a reversed search.
    **Experimental argument, will be removed!**
    """
    compactLegsByReversedSearch: Boolean @deprecated(reason: "Removed in OTP 2")
    """
    Which vehicle rental networks can be used. By default, all networks are allowed.
    
    Deprecated: Use \`allowedVehicleRentalNetworks\` instead.
    """
    allowedBikeRentalNetworks: [String] @deprecated(reason: "Use allowedVehicleRentalNetworks instead")
    """
    Which vehicle rental networks can be used. By default, all networks are allowed.
    """
    allowedVehicleRentalNetworks: [String]
    """
    Which vehicle rental networks cannot be used. By default, all networks are allowed.
    """
    bannedVehicleRentalNetworks: [String]
    """
    Factor for how much the walk safety is considered in routing. Value should be between 0 and 1.
    If the value is set to be 0, safety is ignored. Default is 1.0.
    """
    walkSafetyFactor: Float
    "Preferences for vehicle parking"
    parking: VehicleParkingInput
  ): Plan @async
}


enum RealtimeState {
  """
  The trip information comes from the GTFS feed, i.e. no real-time update has been applied.
  """
  SCHEDULED
  """
  The trip information has been updated, but the trip pattern stayed the same as the trip pattern of the scheduled trip.
  """
  UPDATED
  """The trip has been canceled by a real-time update."""
  CANCELED
  """
  The trip has been added using a real-time update, i.e. the trip was not present in the GTFS feed.
  """
  ADDED
  """
  The trip information has been updated and resulted in a different trip pattern
  compared to the trip pattern of the scheduled trip.
  """
  MODIFIED
}


"""
Route represents a public transportation service, usually from point A to point
B and *back*, shown to customers under a single name, e.g. bus 550. Routes
contain patterns (see field \`patterns\`), which describe different variants of
the route, e.g. outbound pattern from point A to point B and inbound pattern
from point B to point A.
"""
type Route implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the route in format \`FeedId:RouteId\`"""
  gtfsId: String!
  """Agency operating the route"""
  agency: Agency
  """Short name of the route, usually a line number, e.g. 550"""
  shortName: String
  """Long name of the route, e.g. Helsinki-Leppävaara"""
  longName(
    """
    If translated longName is found from gtfs translation.txt and wanted language is not same as
    feed's language then returns wanted translation. Otherwise uses name from routes.txt.
    """
    language: String
  ): String
  """Transport mode of this route, e.g. \`BUS\`"""
  mode: TransitMode
  """
  The raw GTFS route type as a integer. For the list of possible values, see:
  https://developers.google.com/transit/gtfs/reference/#routestxt and
  https://developers.google.com/transit/gtfs/reference/extended-route-types
  """
  type: Int
  desc: String
  url: String
  """
  The color (in hexadecimal format) the agency operating this route would prefer
  to use on UI elements (e.g. polylines on a map) related to this route. This
  value is not available for most routes.
  """
  color: String
  """
  The color (in hexadecimal format) the agency operating this route would prefer
  to use when displaying text related to this route. This value is not available
  for most routes.
  """
  textColor: String
  bikesAllowed: BikesAllowed
  """List of patterns which operate on this route"""
  patterns: [Pattern]
  """List of stops on this route"""
  stops: [Stop]
  """List of trips which operate on this route"""
  trips: [Trip]
  """
  List of alerts which have an effect on the route directly or indirectly.
  By default only alerts directly affecting this route are returned. It's also possible
  to return other relevant alerts through defining types.
  """
  alerts(
    """
    Returns alerts for these types that are relevant for the route.
    By default only returns alerts that directly affect this route.
    """
    types: [RouteAlertType]
  ): [Alert]
}


"""Entities that are relevant for routes that can contain alerts"""
enum RouteAlertType {
  """Alerts affecting the route's agency."""
  AGENCY
  """Alerts directly affecting the route."""
  ROUTE
  """Alerts affecting the route type of the route."""
  ROUTE_TYPE
  """Alerts affecting the stops that are on the route."""
  STOPS_ON_ROUTE
  """Alerts affecting the route's trips."""
  TRIPS
  """Alerts affecting the stops on some trips of the route."""
  STOPS_ON_TRIPS
  """Alerts affecting route's patterns."""
  PATTERNS
}


"""
Route type entity which covers all agencies if agency is null,
otherwise only relevant for one agency.
"""
type RouteType {
  """
  GTFS Route type.
  For the list of possible values, see:
      https://developers.google.com/transit/gtfs/reference/#routestxt and
      https://developers.google.com/transit/gtfs/reference/extended-route-types
  """
  routeType: Int!
  """A public transport agency"""
  agency: Agency
  """
  The routes which have the defined routeType and belong to the agency, if defined.
  Otherwise all routes of the feed that have the defined routeType.
  """
  routes: [Route]
}


"""Time range for which the API has data available"""
type serviceTimeRange {
  """
  Time from which the API has data available. Format: Unix timestamp in seconds
  """
  start: Long
  """
  Time until which the API has data available. Format: Unix timestamp in seconds
  """
  end: Long
}


"""
Actions to take relative to the current position when engaging a walking/driving step.
"""
enum RelativeDirection {
  DEPART
  HARD_LEFT
  LEFT
  SLIGHTLY_LEFT
  CONTINUE
  SLIGHTLY_RIGHT
  RIGHT
  HARD_RIGHT
  CIRCLE_CLOCKWISE
  CIRCLE_COUNTERCLOCKWISE
  ELEVATOR
  UTURN_LEFT
  UTURN_RIGHT
}


"""
The cardinal (compass) direction taken when engaging a walking/driving step.
"""
enum AbsoluteDirection {
  NORTH
  NORTHEAST
  EAST
  SOUTHEAST
  SOUTH
  SOUTHWEST
  WEST
  NORTHWEST
}


type step {
  """The distance in meters that this step takes."""
  distance: Float
  """The longitude of the start of the step."""
  lon: Float
  """The latitude of the start of the step."""
  lat: Float
  """The elevation profile as a list of { distance, elevation } values."""
  elevationProfile: [elevationProfileComponent]
  """
  The relative direction (e.g. left or right turn) to take when engaging this step.
  """
  relativeDirection: RelativeDirection
  """
  The cardinal (compass) direction (e.g. north, northeast) taken when engaging this step.
  """
  absoluteDirection: AbsoluteDirection
  """The name of the street, road, or path taken for this step."""
  streetName: String
  """When exiting a highway or traffic circle, the exit name/number."""
  exit: String
  """
  Indicates whether or not a street changes direction at an intersection.
  """
  stayOn: Boolean
  """
  This step is on an open area, such as a plaza or train platform,
  and thus the directions should say something like "cross".
  """
  area: Boolean
  """
  The name of this street was generated by the system, so we should only display it once, and
  generally just display right/left directions
  """
  bogusName: Boolean
  """Is this step walking with a bike?"""
  walkingBike: Boolean
  """A list of alerts (e.g. construction, detours) applicable to the step."""
  alerts: [Alert]
}


"""
Stop can represent either a single public transport stop, where passengers can
board and/or disembark vehicles, or a station, which contains multiple stops.
See field \`locationType\`.
"""
type Stop implements Node & PlaceInterface {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """Returns timetable of the specified pattern at this stop"""
  stopTimesForPattern(
    """Id of the pattern"""
    id: String!
    """
    Return  departures after this time. Format: Unix timestamp in seconds. Default value: current time
    """
    startTime: Long = 0
    """
    Return stoptimes within this time range, starting from \`startTime\`. Unit: Seconds
    """
    timeRange: Int = 86400
    numberOfDepartures: Int = 2
    """If true, only those departures which allow boarding are returned"""
    omitNonPickups: Boolean = false
    """If false, returns also canceled trips"""
    omitCanceled: Boolean = true
  ): [Stoptime]
  """ÌD of the stop in format \`FeedId:StopId\`"""
  gtfsId: String!
  """Name of the stop, e.g. Pasilan asema"""
  name(
    """
    If translated name is found from gtfs translation.txt and wanted language is not same as
    feed's language then returns wanted translation. Otherwise uses name from stops.txt.
    E.g. Swedish name for Pasilan asema is Böle station.
    """
    language: String
  ): String!
  """Latitude of the stop (WGS 84)"""
  lat: Float
  """Longitude of the stop (WGS 84)"""
  lon: Float
  """
  Representations of this stop's geometry. This is mainly interesting for flex stops which can be
  a polygon or a group of stops either consisting of either points or polygons.
  
  Regular fixed-schedule stops return a single point.
  
  Stations (parent stations with child stops) contain a geometry collection with a point for the
  central coordinate plus a convex hull polygon (https://en.wikipedia.org/wiki/Convex_hull) of all
  coordinates of the child stops.
  
  If there are only two child stops then the convex hull is a straight line between the them. If
  there is a single child stop then it's a single point.
  """
  geometries: StopGeometries
  """Stop code which is visible at the stop"""
  code: String
  """Description of the stop, usually a street name"""
  desc(
    """
    If translated description is found from gtfs translation.txt and wanted language is not same as
    feed's language then returns wanted translation. Otherwise uses descriptions from stops.txt.
    """
    language: String
  ): String
  """ID of the zone where this stop is located"""
  zoneId: String
  url(
    """
    If translated url is found from gtfs translation.txt and wanted language is not same as
    feed's language then returns wanted translation. Otherwise uses url from stops.txt.
    """
    language: String
  ): String
  """Identifies whether this stop represents a stop or station."""
  locationType: LocationType
  """
  The station which this stop is part of (or null if this stop is not part of a station)
  """
  parentStation: Stop
  """
  Whether wheelchair boarding is possible for at least some of vehicles on this stop
  """
  wheelchairBoarding: WheelchairBoarding
  direction: String
  timezone: String
  """
  The raw GTFS route type used by routes which pass through this stop. For the
  list of possible values, see:
  https://developers.google.com/transit/gtfs/reference/#routestxt and
  https://developers.google.com/transit/gtfs/reference/extended-route-types
  """
  vehicleType: Int
  """
  Transport mode (e.g. \`BUS\`) used by routes which pass through this stop or
  \`null\` if mode cannot be determined, e.g. in case no routes pass through the stop.
  Note that also other types of vehicles may use the stop, e.g. tram replacement
  buses might use stops which have \`TRAM\` as their mode.
  """
  vehicleMode: Mode
  """
  Identifier of the platform, usually a number. This value is only present for stops that are part of a station
  """
  platformCode: String
  """The cluster which this stop is part of"""
  cluster: Cluster
  """
  Returns all stops that are children of this station (Only applicable for stations)
  """
  stops: [Stop]
  """Routes which pass through this stop"""
  routes: [Route!]
  """Patterns which pass through this stop"""
  patterns: [Pattern]
  """List of nearby stops which can be used for transfers"""
  transfers(
    """
    Maximum distance to the transfer stop. Defaults to unlimited.
    **Note:** only stops that are linked as a transfer stops to this stop are
    returned, i.e. this does not do a query to search for *all* stops within
    radius of \`maxDistance\`.
    """
    maxDistance: Int
  ): [stopAtDistance]
  """Returns list of stoptimes for the specified date"""
  stoptimesForServiceDate(
    """Date in format YYYYMMDD"""
    date: String
    """If true, only those departures which allow boarding are returned"""
    omitNonPickups: Boolean = false
    """If false, returns also canceled trips"""
    omitCanceled: Boolean = false
  ): [StoptimesInPattern]
  """
  Returns list of stoptimes (arrivals and departures) at this stop, grouped by patterns
  """
  stoptimesForPatterns(
    """
    Return departures after this time. Format: Unix timestamp in seconds. Default value: current time
    """
    startTime: Long = 0
    """
    Return stoptimes within this time range, starting from \`startTime\`. Unit: Seconds
    """
    timeRange: Int = 86400
    numberOfDepartures: Int = 5
    """If true, only those departures which allow boarding are returned"""
    omitNonPickups: Boolean = false
    """If false, returns also canceled trips"""
    omitCanceled: Boolean = true
  ): [StoptimesInPattern]
  """Returns list of stoptimes (arrivals and departures) at this stop"""
  stoptimesWithoutPatterns(
    """
    Return departures after this time. Format: Unix timestamp in seconds. Default value: current time
    """
    startTime: Long = 0
    """
    Return stoptimes within this time range, starting from \`startTime\`. Unit: Seconds
    """
    timeRange: Int = 86400
    numberOfDepartures: Int = 5
    """If true, only those departures which allow boarding are returned"""
    omitNonPickups: Boolean = false
    """If false, returns also canceled trips"""
    omitCanceled: Boolean = true
  ): [Stoptime]
  """
  By default, list of alerts which have directly an effect on just the stop.
  It's also possible to return other relevant alerts through defining types.
  """
  alerts(
    """
    Returns alerts for these types that are relevant for the stop.
    By default, list of alerts which have directly an effect on just the stop.
    """
    types: [StopAlertType]
  ): [Alert]
}


"""Entities, which are relevant for a stop and can contain alerts"""
enum StopAlertType {
  """Alerts affecting the stop"""
  STOP
  """Alerts affecting the stop's patterns"""
  PATTERNS
  """Alerts affecting the agencies of the routes going through the stop"""
  AGENCIES_OF_ROUTES
  """Alerts affecting the routes that go through the stop"""
  ROUTES
  """Alerts affecting the trips that go through this stop"""
  TRIPS
  """Alerts affecting the stop on specific routes"""
  STOP_ON_ROUTES
  """Alerts affecting the stop on specific trips"""
  STOP_ON_TRIPS
}


type stopAtDistance implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  stop: Stop
  """Walking distance to the stop along streets and paths"""
  distance: Int
}


"""A connection to a list of items."""
type stopAtDistanceConnection {
  edges: [stopAtDistanceEdge]
  pageInfo: PageInfo!
}


"""An edge in a connection."""
type stopAtDistanceEdge {
  """The item at the end of the edge"""
  node: stopAtDistance
  cursor: String!
}


"""Stop that should (but not guaranteed) to exist on a route."""
type StopOnRoute {
  """
  Stop at the route. It's also possible that the stop is no longer on the route.
  """
  stop: Stop!
  """Route which contains the stop."""
  route: Route!
}


"""Stop that should (but not guaranteed) to exist on a trip."""
type StopOnTrip {
  """
  Stop at the trip. It's also possible that the stop is no longer on the trip.
  """
  stop: Stop!
  """Trip which contains the stop."""
  trip: Trip!
}


"""
Stoptime represents the time when a specific trip arrives to or departs from a specific stop.
"""
type Stoptime {
  """The stop where this arrival/departure happens"""
  stop: Stop
  """
  Scheduled arrival time. Format: seconds since midnight of the departure date
  """
  scheduledArrival: Int
  """
  Realtime prediction of arrival time. Format: seconds since midnight of the departure date
  """
  realtimeArrival: Int
  """
  The offset from the scheduled arrival time in seconds. Negative values
  indicate that the trip is running ahead of schedule.
  """
  arrivalDelay: Int
  """
  Scheduled departure time. Format: seconds since midnight of the departure date
  """
  scheduledDeparture: Int
  """
  Realtime prediction of departure time. Format: seconds since midnight of the departure date
  """
  realtimeDeparture: Int
  """
  The offset from the scheduled departure time in seconds. Negative values
  indicate that the trip is running ahead of schedule
  """
  departureDelay: Int
  """
  true, if this stop is used as a time equalization stop. false otherwise.
  """
  timepoint: Boolean
  """true, if this stoptime has real-time data available"""
  realtime: Boolean
  """State of real-time data"""
  realtimeState: RealtimeState
  """
  Whether the vehicle can be boarded at this stop. This field can also be used
  to indicate if boarding is possible only with special arrangements.
  """
  pickupType: PickupDropoffType
  """
  Whether the vehicle can be disembarked at this stop. This field can also be
  used to indicate if disembarkation is possible only with special arrangements.
  """
  dropoffType: PickupDropoffType
  """
  Departure date of the trip. Format: Unix timestamp (local time) in seconds.
  """
  serviceDay: Long
  """Trip which this stoptime is for"""
  trip: Trip
  """
  Vehicle headsign of the trip on this stop. Trip headsigns can change during
  the trip (e.g. on routes which run on loops), so this value should be used
  instead of \`tripHeadsign\` to display the headsign relevant to the user.
  """
  headsign(
    """
    If translated headsign is found from gtfs translation.txt and wanted language is not same as
    feed's language then returns wanted translation. Otherwise uses name from trip_headsign.txt.
    """
    language: String
  ): String
}


"""Stoptimes grouped by pattern"""
type StoptimesInPattern {
  pattern: Pattern
  stoptimes: [Stoptime]
}


"""Describes ticket type"""
type TicketType implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """
  Ticket type ID in format \`FeedId:TicketTypeId\`. Ticket type IDs are usually
  combination of ticket zones where the ticket is valid.
  """
  fareId: String!
  """Price of the ticket in currency that is specified in \`currency\` field"""
  price: Float
  """ISO 4217 currency code"""
  currency: String
  """
  List of zones where this ticket is valid.
  Corresponds to field \`zoneId\` in **Stop** type.
  """
  zones: [String!]
}


"""Text with language"""
type TranslatedString {
  text: String
  """Two-letter language code (ISO 639-1)"""
  language: String
}


"""Transportation mode which can be used in the itinerary"""
input TransportMode {
  mode: Mode!
  """Optional additional qualifier for transport mode, e.g. \`RENT\`"""
  qualifier: Qualifier
}


"""
Trip is a specific occurance of a pattern, usually identified by route, direction on the route and exact departure time.
"""
type Trip implements Node {
  """
  Global object ID provided by Relay. This value can be used to refetch this object using **node** query.
  """
  id: ID!
  """ID of the trip in format \`FeedId:TripId\`"""
  gtfsId: String!
  """The route the trip is running on"""
  route: Route!
  serviceId: String
  """List of dates when this trip is in service. Format: YYYYMMDD"""
  activeDates: [String]
  tripShortName: String
  """Headsign of the vehicle when running on this trip"""
  tripHeadsign(
    """
    If translated headsign is found from gtfs translation.txt and wanted language is not same as
    feed's language then returns wanted translation. Otherwise uses name from trip_headsign.txt.
    """
    language: String
  ): String
  """
  Short name of the route this trip is running. See field \`shortName\` of Route.
  """
  routeShortName: String
  """
  Direction code of the trip, i.e. is this the outbound or inbound trip of a
  pattern. Possible values: 0, 1 or \`null\` if the direction is irrelevant, i.e.
  the pattern has trips only in one direction.
  """
  directionId: String
  blockId: String
  shapeId: String
  """Whether the vehicle running this trip can be boarded by a wheelchair"""
  wheelchairAccessible: WheelchairBoarding
  """Whether bikes are allowed on board the vehicle running this trip"""
  bikesAllowed: BikesAllowed
  """The pattern the trip is running on"""
  pattern: Pattern
  """List of stops this trip passes through"""
  stops: [Stop!]!
  """
  Hash code of the trip. This value is stable and not dependent on the trip id.
  """
  semanticHash: String!
  """List of times when this trip arrives to or departs from a stop"""
  stoptimes: [Stoptime]
  """Departure time from the first stop"""
  departureStoptime(
    """
    Date for which the departure time is returned. Format: YYYYMMDD. If this
    argument is not used, field \`serviceDay\` in the stoptime will have a value of 0.
    """
    serviceDate: String
  ): Stoptime
  """Arrival time to the final stop"""
  arrivalStoptime(
    """
    Date for which the arrival time is returned. Format: YYYYMMDD. If this
    argument is not used, field \`serviceDay\` in the stoptime will have a value of 0.
    """
    serviceDate: String
  ): Stoptime
  stoptimesForDate(
    """Date for which stoptimes are returned. Format: YYYYMMDD"""
    serviceDate: String
  ): [Stoptime]
  """List of coordinates of this trip's route"""
  geometry: [[Float]]
  """
  Coordinates of the route of this trip in Google polyline encoded format
  """
  tripGeometry: Geometry
  """
  By default, list of alerts which have directly an effect on just the trip.
  It's also possible to return other relevant alerts through defining types.
  """
  alerts(
    """
    Returns alerts for these types that are relevant for the trip.
    By default, list of alerts which have directly an effect on just the trip.
    """
    types: [TripAlertType]
  ): [Alert]
}


"""Entities, which are relevant for a trip and can contain alerts"""
enum TripAlertType {
  """Alerts affecting the trip"""
  TRIP
  """Alerts affecting the trip's pattern"""
  PATTERN
  """Alerts affecting the trip's agency"""
  AGENCY
  """Alerts affecting the trip's route"""
  ROUTE
  """Alerts affecting the route type of the trip's route"""
  ROUTE_TYPE
  """
  Alerts affecting the stops visited on the trip.
  Some of the alerts can only affect the trip or its route on the stop.
  """
  STOPS_ON_TRIP
}


"""
A system notice is used to tag elements with system information for debugging
or other system related purpose. One use-case is to run a routing search with
'debugItineraryFilter: true'. This will then tag itineraries instead of removing
them from the result. This make it possible to inspect the itinerary-filter-chain.
A SystemNotice only has english text,
because the primary user are technical staff, like testers and developers.
"""
type SystemNotice {
  """Notice's tag"""
  tag: String
  """Notice's description"""
  text: String
}


"""
This is used for alert entities that we don't explicitly handle or they are missing.
"""
type Unknown {
  """Entity's description"""
  description: String
}


enum VertexType {
  """NORMAL"""
  NORMAL
  """TRANSIT"""
  TRANSIT
  """BIKEPARK"""
  BIKEPARK
  """BIKESHARE"""
  BIKESHARE
  """PARKANDRIDE"""
  PARKANDRIDE
}


enum WheelchairBoarding {
  """There is no accessibility information for the stop."""
  NO_INFORMATION
  """
  At least some vehicles at this stop can be boarded by a rider in a wheelchair.
  """
  POSSIBLE
  """Wheelchair boarding is not possible at this stop."""
  NOT_POSSIBLE
}

schema {
  query: QueryType
}
"""
Use an asynchronous data fetcher on a separate thread for this field.

This is useful when adding several queries in the same HTTP request, for example by using a batch: Those
fields annotated with this directive run in parallel.

This is only worth it when the execution is long running, i.e. more than ~50 milliseconds, so this doesn't happen by default.
"""
directive @async on FIELD_DEFINITION
`;class VT{constructor(s){tu(this,"mapping");this.mapping=s}getFor(s){return this.mapping.get(s.name)}}var Si;(function(t){t.UNION="union",t.ARGUMENT="argument",t.FIELD="field"})(Si||(Si={}));function WT(t){const s=new Map;return YT(t,r=>{Ke(r)&&$T(r,o=>{const l=pa(o.type);la(s,l).references.push({kind:Si.FIELD,parent:r,by:o}),jT(o,g=>{const T=pa(g.type);la(s,T).references.push({kind:Si.ARGUMENT,field:o,type:r,by:g})})}),Ln(r)&&r.getTypes().forEach(o=>{la(s,o).references.push({kind:Si.UNION,by:r})})}),new VT(s)}function YT(t,s){Object.entries(t.getTypeMap()).forEach(([,r])=>{r.name.startsWith("__")||s(r)})}function $T(t,s){Object.entries(t.getFields()).forEach(([,r])=>{s(r)})}function jT(t,s){Object.entries(t.args).forEach(([,r])=>{s(r)})}function la(t,s){let r=t.get(s.name);return r||(r={references:[]},t.set(s.name,r)),r}function pa(t){return $e(t)||xt(t)?pa(t.ofType):t}const He=iI(),ga=ey(Hv.DIRECTIVES,[]);var yu;const HT=Ur((yu=He.getQueryType())==null?void 0:yu.getFields());var Tu;const qT=Ur((Tu=He.getMutationType())==null?void 0:Tu.getFields());var Iu;const QT=Ur((Iu=He.getSubscriptionType())==null?void 0:Iu.getFields()),wa=Tn.keyBy(rc(),t=>t.name.toLocaleLowerCase()),JT=Tn.mapValues(wa,t=>{const s=ga.find(r=>(r==null?void 0:r.name)===t.name||(r==null?void 0:r.name)==="*");return s?s.args.some(r=>r==="*")?t.args:s.args.map(r=>t.args.find(o=>o.name===r)).filter(r=>!!r):[]}),zT=Ur(He.getTypeMap()),XT=WT(He);function Ur(t){return Tn.mapKeys(t||{},(s,r)=>r.toLocaleLowerCase())}function KT(){return Tn.size(He.getTypeMap())<=10}function cI(){return[ua("Queries",He.getQueryType()),ua("Mutations",He.getMutationType()),ua("Subscriptions",He.getSubscriptionType()),rI(),nI()].filter(t=>!!t)}function ua(t,s){return eI(t,ZT(s))}function ZT(t){return Tn.sortBy((t==null?void 0:t.getFields())||{},s=>s.name)}function eI(t,s){return s.length===0?null:{type:"menu",title:t,children:s.map(r=>({type:"page",title:r.name,section:t,deprecated:!!r.deprecationReason,href:ma.joinUrlPaths(va,t.toLocaleLowerCase(),r.name)}))}}function nI(){return KT()?null:{type:"menu",title:"Types",children:Tn.sortBy(Tn.map(He.getTypeMap()),s=>s.name).filter(s=>!s.name.startsWith("__")).map(s=>({type:"page",title:s.name,section:"Types",href:ma.joinUrlPaths(va,"types",s.name)}))}}function fI(){return!!He.getQueryType()}function hI(t){return HT[t.toLocaleLowerCase()]}function dI(){return!!He.getMutationType()}function pI(t){return qT[t.toLocaleLowerCase()]}function gI(){return!!He.getSubscriptionType()}function mI(t){return QT[t.toLocaleLowerCase()]}function vI(t){return zT[t.toLocaleLowerCase()]}function tI(t){return wa[t.toLocaleLowerCase()]}function yI(t){return tI(t.name)!==void 0}function TI(){return Tn.size(wa)>0}function II(t){return JT[t.name.toLocaleLowerCase()]||[]}function EI(t){if(t)return XT.getFor(t)}function bI(t){return Tn.flatMap(t.getFields(),s=>({field:s,possibleDescriptions:ic(s,t)}))}function ic(t,s){return t?t.description?[{description:t.description,from:s}]:Ke(s)?s.getInterfaces().flatMap(r=>ic(r.getFields()[t.name],r)):[]:[]}function rc(){return ga.some(t=>(t==null?void 0:t.name)==="*")?He.getDirectives().filter(t=>!["include","skip","deprecated","specifiedBy"].includes(t.name)):ga.filter(t=>!!(t!=null&&t.name)).map(({name:t})=>t?He.getDirective(t):void 0).filter(t=>!!t)}function iI(){return vu.trim().length===0?UT(JSON.parse(JSON.stringify({__schema:{types:[]}}))):GT(vu)}function rI(){const t=rc();return t.length===0?null:{type:"menu",title:"Directives",children:t.map(s=>({type:"page",title:s.name,href:ma.joinUrlPaths(va,"directives",s.name),section:"Directives"}))}}export{bI as A,vI as B,EI as C,yI as D,uI as E,cI as F,y as K,Si as R,Ke as a,je as b,vt as c,Ln as d,Cn as e,KT as f,ey as g,lI as h,kn as i,II as j,TI as k,Tn as l,tI as m,dI as n,pI as o,fI as p,hI as q,gI as r,He as s,Hv as t,mI as u,jy as v,$e as w,xt as x,Bu as y,Uu as z};
