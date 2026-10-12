(function(){
  var L=["ru","kk","en","es"],saved=null;
  try{saved=localStorage.getItem("lang")}catch(e){}
  var nav=(navigator.language||"en").slice(0,2);
  var q=new URLSearchParams(location.search).get("lang");
  var lang=L.indexOf(q)>=0?q:L.indexOf(saved)>=0?saved:L.indexOf(nav)>=0?nav:"en";
  function set(l){
    document.documentElement.dataset.lang=l;document.documentElement.lang=l;
    try{localStorage.setItem("lang",l)}catch(e){}
    document.querySelectorAll(".langs button").forEach(function(b){b.classList.toggle("on",b.dataset.set===l)});
  }
  set(lang);
  document.addEventListener("DOMContentLoaded",function(){
    document.querySelectorAll(".langs button").forEach(function(b){b.onclick=function(){set(b.dataset.set)}});
    set(document.documentElement.dataset.lang);
  });
})();
