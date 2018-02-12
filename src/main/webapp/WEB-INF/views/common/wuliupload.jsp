<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<head>
<title>File Upload Progress Demo #1</title>
<style>
body { padding: 30px }
form { display: block; margin: 20px auto; background: #eee; border-radius: 10px; padding: 15px }

.progress { position:relative; width:400px; border: 1px solid #ddd; padding: 1px; border-radius: 3px; }
.bar { background-color: #B4F5B4; width:0%; height:20px; border-radius: 3px; }
.percent { position:absolute; display:inline-block; top:3px; left:48%; }
</style>
</head>
<body>
    <h1>File Upload Progress Demo #1</h1>
	<code>&lt;input type="file" name="myfile"></code>
	    <form  id="form" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
        <input type="file" id="file" name="file"><br>
		 <input type="hidden" name="type" id="type" value="3"/>
        <input type="submit" value="Upload File to Server">
    </form>
        <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
        <input type="file" id="file1" name="file"><br>
        <input type="submit" value="Upload File to Server">
    </form>
    <div class="progress">
        <div class="bar"></div >
        <div class="percent">0%</div >
    </div>
    <div id="status"></div>   
<script src="http://www.17xs.org/res/js/jquery-1.8.2.min.js?v=20150511"></script>
<script src="http://www.17xs.org/res/js/util/jquery.form.js"></script>
<script>
$(function(){
		var bar = $('.bar');
var percent = $('.percent');
var status = $('#status');
 $('#file').change(function(){ 
	 $('#form').submit();
	 return false;
});

$('#file1').change(function(){
	 $('#form1').submit()
})

$('#form').submit(function(){
     window.ajaxForm(this);
});
window.ajaxForm=function(obj){
   
   $(obj).ajaxForm({
	  beforeSend: function() {},
	  fkdfj:function(){}
	  
   });
}
$('#form').ajaxForm({
    beforeSend: function() {
        status.empty();
        var percentVal = '0%';
        bar.width(percentVal)
        percent.html(percentVal);
    },
    uploadProgress: function(event, position, total, percentComplete) {
        var percentVal = percentComplete + '%';
        bar.width(percentVal)
        percent.html(percentVal);
    },
    success: function(data) {
// 		salert(data)
		data = data.replace("<PRE>", "").replace("</PRE>", "");
		var json = eval('(' + data + ')'); 
        alert(json.imageUrl);
        var percentVal = '100%';
        bar.width(percentVal)
        percent.html(percentVal+"aa");
    },
	complete: function(xhr) {
		var data=xhr.responseText;
	    data = data.replace("<PRE>", "").replace("</PRE>", "");
		var json = eval('(' + data + ')'); 
			//console.log(json);
		status.html(json.imageUrl);
		//console.log(data);
		//status.html(data);
	}
});    
$('#form1').ajaxForm({
    beforeSend: function() {
        status.empty();
        var percentVal = '0%';
        bar.width(percentVal)
        percent.html(percentVal);
    },
    uploadProgress: function(event, position, total, percentComplete) {
        var percentVal = percentComplete + '%';
        bar.width(percentVal)
        percent.html(percentVal);
    },
    success: function(data) {
// 		alert(data)
        var percentVal = '100%';
        bar.width(percentVal)
        percent.html(percentVal);
    },
	complete: function(xhr) {
		status.html(xhr.responseText);
	}
}); 
})
      
</script>
