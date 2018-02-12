define(['jquery'],function($){
  var o={
    throttle:function(fn, delay, mustRunDelay){
      var timer = null;
      var t_start;
      return function(){
        var context = this, args = arguments, t_curr = +new Date();
        clearTimeout(timer);
        if (!t_start) {
          t_start = t_curr;
        }
        if (t_curr - t_start >= mustRunDelay) {
          fn.apply(context, args);
          t_start = t_curr;
        } else {
          timer = setTimeout(function() {
            fn.apply(context, args);
          }, delay);
        }
      }
    },
    screen:function (){
      var s;
      if(/msie/.test(navigator.userAgent.toLowerCase())){
        s={w:document.documentElement.clientWidth,h:document.documentElement.clientHeight}
      }else if( /opera/.test(navigator.userAgent.toLowerCase())){
        s={w:Math.min(window.innerWidth, document.body.clientWidth),h:Math.min(window.innerHeight, document.body.clientHeight)};
      }else{
        s={w:Math.min(window.innerWidth, document.documentElement.clientWidth),h:Math.min(window.innerHeight, document.documentElement.clientHeight)}
      }
      s.left = document.documentElement.scrollLeft || document.body.scrollLeft;
      s.top = document.documentElement.scrollTop || document.body.scrollTop;
      s.sw = document.documentElement.scrollWidth || document.body.scrollWidth;
      s.sh = document.documentElement.scrollHeight || document.body.scrollHeight;
      return s;
    }
  };
  return o;
});