<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./common/file_url.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>善园</title>
 <script type="text/javascript" src="<%=resource_url%>res/js/visualization/echarts-all-3.js"></script>
 <script type="text/javascript" src="<%=resource_url%>res/js/visualization/dataTool.min.js"></script>
 <!--<script type="text/javascript" src="<%=resource_url%>res/js/visualization/world.js"></script>-->
 <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
 <script type="text/javascript" src="<%=resource_url%>res/js/visualization/bmap.min.js"></script>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>

<style>
.echart_li {
	width: 100%;
	position: absolute;
	z-index: 1000;
	margin-top: 74px;
}

.echart_li span {
	display: inline-block;
}

.echart_li li {
	list-style: none;
}

.echart_li .echart_ul {
	width: 68%;
	margin: auto;
	text-align: center;
}

.echart_sp {
	width: 25px;
	height: 35px;
	text-align: center;
	background: #878d97;
	line-height: 35px;
	font-weight: bold;
	font-size: 20px;
	color:#fff;
}
.textB{
	position: absolute;
	color: #fff;
	z-index: 1;
	width: 100%;
	text-align:center;
	top: 14px;
	font-size: 30px;
	text-shadow: 1px 2px #6eabe2;
	}
</style>
<body style="margin: 0; font-family:'微软雅黑">
	<b class="textB">善园捐款动态图</b>
   <div class="echart_li">
       <div class="echart_ul">
           <span style="color:#fff">累计捐款：</span>
           <c:forEach var="ite" items="${totalMoney }" varStatus="index">
           	 <span class="echart_sp">${ite }</span>
           </c:forEach>
          <span class="echart_sp">元</span>
           <!-- <span class="echart_sp">2</span>
           <span class="echart_sp">2</span>
           <span class="echart_sp">2</span>
           <span class="echart_sp">2</span>
            -->
       </div>
	</div>
    <%--柱状图--%>
    <div id="pillar2" style="width:460px;height:305px;position: absolute;z-index: 9;right: 0;bottom: 157px;"></div>
	<div class="btnBox" style="position: fixed;bottom: 0;right:0">
		<input type="button" id="open" value="启动更新数据" />
		<input type="button" id="stop" value="停止更新数据"/>
	</div>
   </div>
       <div id="container" style="height: 1000px;width: 100%;"></div>
    <%--柱状图--%>
    <script type="text/javascript" src="<%=resource_url%>res/js/zhuzhuangtu.js"></script>
</body>
<script>
$(function(){
var data = [];
var geoCoordMap = {};
var DataFromTo = [];
	$.ajax({
			url : '<%=resource_url%>index/visualization_list.do',
			data : {},
			async : false,
			success : function(result) {
				var data1 = result.obj, data2 = result.obj1;
				for (var i = 0; i < data1.length; i++) {
					data.push({
						name : data1[i].name,
						value : data1[i].value
					});
				}
				geoCoordMap = data2;
			}
		});

		$.ajax({
			url : '<%=resource_url%>index/visualization_feixianlist.do',
			data : {},
			async : false,
			success : function(result) {
				var data1 = result.obj;
				DataFromTo=eval(data1);
			} 
		});

		var dom = document.getElementById("container");
		var myChart = echarts.init(dom);
		var app = {};
		option = null;
		//var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';
		//var otherPath = 'path://M917.965523 917.331585c0 22.469758-17.891486 40.699957-39.913035 40.699957-22.058388 0-39.913035-18.2302-39.913035-40.699957l-0.075725-0.490164-1.087774 0c-18.945491-157.665903-148.177807-280.296871-306.821991-285.4748-3.412726 0.151449-6.751774 0.562818-10.240225 0.562818-3.450589 0-6.789637-0.410346-10.202363-0.524956-158.606321 5.139044-287.839661 127.806851-306.784128 285.436938l-1.014096 0 0.075725 0.490164c0 22.469758-17.854647 40.699957-39.913035 40.699957s-39.915082-18.2302-39.915082-40.699957l-0.373507-3.789303c0-6.751774 2.026146-12.903891 4.91494-18.531052 21.082154-140.712789 111.075795-258.241552 235.432057-312.784796C288.420387 530.831904 239.989351 444.515003 239.989351 346.604042c0-157.591201 125.33352-285.361213 279.924387-285.361213 154.62873 0 279.960203 127.770012 279.960203 285.361213 0 97.873098-48.391127 184.15316-122.103966 235.545644 124.843356 54.732555 215.099986 172.863023 235.808634 314.211285 2.437515 5.290493 4.01443 10.992355 4.01443 17.181311L917.965523 917.331585zM719.822744 346.679767c0-112.576985-89.544409-203.808826-199.983707-203.808826-110.402459 0-199.944821 91.232864-199.944821 203.808826s89.542362 203.808826 199.944821 203.808826C630.278335 550.488593 719.822744 459.256752 719.822744 346.679767z';
		var convertData = function(data) {
			var res = [];
			for (var i = 0; i < data.length; i++) {
				var dataItem = data[i];
				var fromCoord = geoCoordMap[dataItem[0].name];
				var toCoord = geoCoordMap[dataItem[1].name];
				if (fromCoord && toCoord) {
					res.push({
						fromName : dataItem[0].name,
						toName : dataItem[1].name,
						coords : [ fromCoord, toCoord ]
					});
				}
			}
			return res;
		};
		var convertData2 = function (data) {
		 var res = [];
		 for (var i = 0; i < data.length; i++) {
		 var geoCoord = geoCoordMap[data[i].name];
		 if (geoCoord) {
		 res.push({
		 name: data[i].name,
		 value: geoCoord.concat(data[i].value)
		 });
		 var res0=res[i].value[0],res1=res[i].value[1],res2=res[i].value[2],res3=res[i].value[3],res4=res[i].value[4],res5=res[i].value[5];
		 res[i].value[2]=res5;
		 res[i].value[3]=res2;
		 res[i].value[4]=res3;
		 res[i].value[5]=res4;
		 }
		 }
		 return res;
		 };
		var color = [ '#a6c84c', '#ffa022', '#46bee9' ];
		option = {
			backgroundColor : '#404a59',
			title : {
				text : '',
				//subtext : '数据归善园所有',
				left : 'center',
				textStyle : {
					color : '#fff',
					fontSize:20,
					shadowOffsetX:1,
					shadowOffsetY:2,
					shadowColor:'#6eabe2'

				}
			},
			/*  geo: {
			     map: 'world',
			     label: {
			         emphasis: {
			             show: false
			         }
			     },
			     roam: true,
				layoutCenter: ['-100%', '130%'],
				layoutSize: 10000,
			     itemStyle: {
			         normal: {
			             areaColor: '#323c48',
			             borderColor: '#404a59'
			         },
			         emphasis: {
			             areaColor: '#2a333d'
			         }
			     }
			}, */
			bmap : {
			center : [ 110.97, 35.71 ],
			zoom : 6,
			roam : true,
			mapStyle : {
			styleJson : [
			{
			'featureType': 'land',     //调整土地颜色
			'elementType': 'geometry',
			'stylers': {
			'color': '#081734'
			}
			},
			{
			'featureType': 'building',   //调整建筑物颜色
			'elementType': 'geometry',
			'stylers': {
			'color': '#04406F'
			}
			},
			{
			'featureType': 'building',   //调整建筑物标签是否可视
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'highway',     //调整高速道路颜色
			'elementType': 'geometry',
			'stylers': {
			'color': '#081633'
			}
			},
			{
			'featureType': 'highway',    //调整高速名字是否可视
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'arterial',   //调整一些干道颜色
			'elementType': 'geometry',
			'stylers': {
			'color':'#081633'
			}
			},
			{
			'featureType': 'arterial',
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'green',
			'elementType': 'geometry',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'water',
			'elementType': 'geometry',
			'stylers': {
			'color': '#044161'
			}
			},
			{
			'featureType': 'subway',    //调整地铁颜色
			'elementType': 'geometry.stroke',
			'stylers': {
			'color': '#081633'
			}
			},
			{
			'featureType': 'subway',
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'railway',
			'elementType': 'geometry',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'railway',
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'all',     //调整所有的标签的边缘颜色
			'elementType': 'labels.text.stroke',
			'stylers': {
			'color': 'rgba（0,0,0,0）'
			}
			},
			{
			'featureType': 'all',     //调整所有标签的填充颜色
			'elementType': 'labels.text.fill',
			'stylers': {
			'color': '#717f98'
			}
			},
			{
			'featureType': 'manmade',
			'elementType': 'geometry',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'manmade',
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'local',
			'elementType': 'geometry',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'local',
			'elementType': 'labels',
			'stylers': {
			'visibility': 'off'
			}
			},
			{
			'featureType': 'subway',
			'elementType': 'geometry',
			'stylers': {
			'lightness': -65
			}
			},
			{
			'featureType': 'railway',
			'elementType': 'all',
			'stylers': {
			'lightness': -40
			}
			},
			{
			'featureType': 'boundary',
			'elementType': 'geometry',
			'stylers': {
			'color': '#8b8787',
			'weight': '1',
			'lightness': -29
			}
			}/*{
			'featureType' : 'water',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#404a59'
			}
			}, {
			'featureType' : 'land',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#323c48'
			}
			}, {
			'featureType' : 'railway',
			'elementType' : 'all',
			'stylers' : {
			'visibility' : 'off'
			}
			}, {
			'featureType' : 'highway',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#323c48'
			}
			}, {
			'featureType' : 'highway',
			'elementType' : 'labels',
			'stylers' : {
			'visibility' : 'off'
			}
			}, {
			'featureType' : 'arterial',
			'elementType' : 'geometry',
			'stylers' : {
			'color' : '#1e2126'
			}
			}, {
			'featureType' : 'arterial',
			'elementType' : 'geometry.fill',
			'stylers' : {
			'color' : '#1e2126'
			}
			},{
			'featureType' : 'poi',
			'elementType' : 'all',
			'stylers' : {
			'visibility' : 'off'
			}
			}, {
			'featureType' : 'green',
			'elementType' : 'all',
			'stylers' : {
			'visibility' : 'off'
			}
			}, {
			'featureType' : 'subway',
			'elementType' : 'all',
			'stylers' : {
			'visibility' : 'off'
			}
			}, {
			'featureType' : 'manmade',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#404a59'
			}
			}, {
			'featureType' : 'local',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#404a59'
			}
			}, {
			'featureType' : 'arterial',
			'elementType' : 'labels',
			'stylers' : {
			'visibility' : 'off'
			}
			}, {
			'featureType' : 'boundary',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#1e2126'
			}
			}, {
			'featureType' : 'building',
			'elementType' : 'all',
			'stylers' : {
			'color' : '#404a59'
			}
			}, {
			'featureType' : 'label',
			'elementType' : 'labels.text.fill',
			'stylers' : {
			'color' : '#999'
			}
			}*/ ]
			}
			},
			series : [{
				name : '捐款详情',
				type : 'scatter',
				coordinateSystem : 'bmap',
				data : convertData2(data),
				symbolSize : function(val) {
					return val[2] / 10;
				},
				label : {
					normal : {
						formatter : '{b}',
						position : 'right',
						show : false
					},
					emphasis : {
						show : false
					}
				},
				itemStyle : {
					normal : {
						color : '#f4e925'
					}
				},
			},
			 {
				name : '捐款详情',
				type : 'effectScatter',
				coordinateSystem : 'bmap',
				data : convertData2(data.sort(function(a, b) {
					return b.value - a.value;
				}).slice(0, 6)),
				symbolSize : function(val) {
					return val[2] / 10;
				},
				showEffectOn : 'render',
				rippleEffect : {
					brushType : 'stroke'
				},
				hoverAnimation : true,
				label : {
					normal : {
						formatterL : '{b}',
						position : 'right',
						show : false
					}
				},
				itemStyle : {
					normal : {
						color : '#f4e925',
						shadowBlur : 10,
						shadowColor : '#f4e925'
					}
				},
				zlevel : 1
			}, {
				name : '捐款详情',
				type : 'lines',
				coordinateSystem : 'bmap',
				zlevel : 2,
				effect: {
					show: true,
					period: 6,
					trailLength: 0.7,
					color: '#fff',
					symbolSize: 4,
					loop:false
				},
				lineStyle: {
					normal: {
						width: '',
						color: '#a6c84c',
						curveness: 0.2
					}
				},
				data : convertData(DataFromTo)
			}],
			tooltip : {
				show : true,
				trigger : 'item',
				formatter: function(params) { 
                  if(typeof(params.data.coords)=='undefined'&&Number(params.data.value[4])<=0){
						return params.seriesName + '<br>总捐款金额：' + params.data.value[3] + '元';
					}
				  else if(typeof(params.data.coords)=='undefined'&&Number(params.data.value[4])>0){
						return '受助人：'+params.data.value[5] + '<br>获捐金额：' + params.data.value[3] + '元<br>获捐次数：' + params.data.value[4] + ' 人次';
					}
					else{
						return '受助人：'+params.data.toName +','+params.data.coords[1][4] + '<br>获捐金额：' + params.data.coords[1][2] + '元<br>获捐次数：' + params.data.coords[1][3] + ' 人次';
					}
            	} ,
            	triggerOn:'none'
			},
		};
		if (option && typeof option === "object") {
			var total = option.series[0].data.length;
			var count = Math.floor(Math.random()*(total+1));
			myChart.setOption(option, true);
			setInterval(function(){
				myChart.dispatchAction({
					type:'showTip',
					seriesIndex:0,
					dataIndex:count
				});	
				/*setTimeout(function(){
					myChart.dispatchAction({
						type:'hideTip',
					});		
				},2000);*/
				count++;
				if(count>=total){count = 0;}
			},2000);
			
			
		}
	})
	function show(){
	var data = [];
	var geoCoordMap = {};
	var DataFromTo = [];
	$.ajax({
			url : '<%=resource_url%>index/visualization_list.do',
			data : {},
			async : false,
			success : function(result) {
				var data1 = result.obj, data2 = result.obj1;
				for (var i = 0; i < data1.length; i++) {
					data.push({
						name : data1[i].name,
						value : data1[i].value
					});
				}
				geoCoordMap = data2;
			}
		});

		$.ajax({
			url : '<%=resource_url%>index/visualization_feixianlist.do',
			data : {},
			async : false,
			success : function(result) {
				var data1 = result.obj;
				DataFromTo=eval(data1);
			} 
		});

		var dom = document.getElementById("container");
		var myChart = echarts.init(dom);
		var app = {};
		option = null;
		//var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';
		var otherPath = 'path://M917.965523 917.331585c0 22.469758-17.891486 40.699957-39.913035 40.699957-22.058388 0-39.913035-18.2302-39.913035-40.699957l-0.075725-0.490164-1.087774 0c-18.945491-157.665903-148.177807-280.296871-306.821991-285.4748-3.412726 0.151449-6.751774 0.562818-10.240225 0.562818-3.450589 0-6.789637-0.410346-10.202363-0.524956-158.606321 5.139044-287.839661 127.806851-306.784128 285.436938l-1.014096 0 0.075725 0.490164c0 22.469758-17.854647 40.699957-39.913035 40.699957s-39.915082-18.2302-39.915082-40.699957l-0.373507-3.789303c0-6.751774 2.026146-12.903891 4.91494-18.531052 21.082154-140.712789 111.075795-258.241552 235.432057-312.784796C288.420387 530.831904 239.989351 444.515003 239.989351 346.604042c0-157.591201 125.33352-285.361213 279.924387-285.361213 154.62873 0 279.960203 127.770012 279.960203 285.361213 0 97.873098-48.391127 184.15316-122.103966 235.545644 124.843356 54.732555 215.099986 172.863023 235.808634 314.211285 2.437515 5.290493 4.01443 10.992355 4.01443 17.181311L917.965523 917.331585zM719.822744 346.679767c0-112.576985-89.544409-203.808826-199.983707-203.808826-110.402459 0-199.944821 91.232864-199.944821 203.808826s89.542362 203.808826 199.944821 203.808826C630.278335 550.488593 719.822744 459.256752 719.822744 346.679767z';
		var convertData = function(data) {
			var res = [];
			for (var i = 0; i < data.length; i++) {
				var dataItem = data[i];
				var fromCoord = geoCoordMap[dataItem[0].name];
				var toCoord = geoCoordMap[dataItem[1].name];
				if (fromCoord && toCoord) {
					res.push({
						fromName : dataItem[0].name,
						toName : dataItem[1].name,
						coords : [ fromCoord, toCoord ]
					});
				}
			}
			return res;
		};
		var convertData2 = function (data) {
		 var res = [];
		 for (var i = 0; i < data.length; i++) {
		 var geoCoord = geoCoordMap[data[i].name];
		 if (geoCoord) {
		 res.push({
		 name: data[i].name,
		 value: geoCoord.concat(data[i].value)
		 });
		 var res0=res[i].value[0],res1=res[i].value[1],res2=res[i].value[2],res3=res[i].value[3],res4=res[i].value[4],res5=res[i].value[5];
		 res[i].value[2]=res5;
		 res[i].value[3]=res2;
		 res[i].value[4]=res3;
		 res[i].value[5]=res4;
		 }
		 }
		 return res;
		 };
		var color = [ '#a6c84c', '#ffa022', '#46bee9' ];
		option = {
			backgroundColor : '#404a59',
			title : {
				text : '',
				//subtext : '数据归善园所有',
				left : 'center',
				textStyle : {
					color : '#fff',
					fontSize: 20,
				}
			},
			/*  geo: {
			     map: 'world',
			     label: {
			         emphasis: {
			             show: false
			         }
			     },
			     roam: true,
				layoutCenter: ['-100%', '130%'],
				layoutSize: 10000,
			     itemStyle: {
			         normal: {
			             areaColor: '#323c48',
			             borderColor: '#404a59'
			         },
			         emphasis: {
			             areaColor: '#2a333d'
			         }
			     }
			 }, */
			bmap : {
				center : [ 110.97, 35.71 ],
				zoom : 6,
				roam : true,
				mapStyle : {
					styleJson : [ {
	'featureType': 'land',     //调整土地颜色
	'elementType': 'geometry',
	'stylers': {
	'color': '#081734'
	}
	},
	{
	'featureType': 'building',   //调整建筑物颜色
	'elementType': 'geometry',
	'stylers': {
	'color': '#04406F'
	}
	},
	{
	'featureType': 'building',   //调整建筑物标签是否可视
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'highway',     //调整高速道路颜色
	'elementType': 'geometry',
	'stylers': {
	'color': '#081633'
	}
	},
	{
	'featureType': 'highway',    //调整高速名字是否可视
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'arterial',   //调整一些干道颜色
	'elementType': 'geometry',
	'stylers': {
	'color':'#081633'
	}
	},
	{
	'featureType': 'arterial',
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'green',
	'elementType': 'geometry',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'water',
	'elementType': 'geometry',
	'stylers': {
	'color': '#044161'
	}
	},
	{
	'featureType': 'subway',    //调整地铁颜色
	'elementType': 'geometry.stroke',
	'stylers': {
	'color': '#081633'
	}
	},
	{
	'featureType': 'subway',
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'railway',
	'elementType': 'geometry',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'railway',
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'all',     //调整所有的标签的边缘颜色
	'elementType': 'labels.text.stroke',
	'stylers': {
	'color': 'rgba（0,0,0,0）'
	}
	},
	{
	'featureType': 'all',     //调整所有标签的填充颜色
	'elementType': 'labels.text.fill',
	'stylers': {
	'color': '#717f98'
	}
	},
	{
	'featureType': 'manmade',
	'elementType': 'geometry',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'manmade',
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'local',
	'elementType': 'geometry',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'local',
	'elementType': 'labels',
	'stylers': {
	'visibility': 'off'
	}
	},
	{
	'featureType': 'subway',
	'elementType': 'geometry',
	'stylers': {
	'lightness': -65
	}
	},
	{
	'featureType': 'railway',
	'elementType': 'all',
	'stylers': {
	'lightness': -40
	}
	},
	{
	'featureType': 'boundary',
	'elementType': 'geometry',
	'stylers': {
	'color': '#8b8787',
	'weight': '1',
	'lightness': -29
	}
	}/*{
						'featureType' : 'water',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#404a59'
						}
					}, {
						'featureType' : 'land',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#323c48'
						}
					}, {
						'featureType' : 'railway',
						'elementType' : 'all',
						'stylers' : {
							'visibility' : 'off'
						}
					}, {
						'featureType' : 'highway',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#323c48'
						}
					}, {
						'featureType' : 'highway',
						'elementType' : 'labels',
						'stylers' : {
							'visibility' : 'off'
						}
					}, {
						'featureType' : 'arterial',
						'elementType' : 'geometry',
						'stylers' : {
							'color' : '#1e2126'
						}
					}, {
						'featureType' : 'arterial',
						'elementType' : 'geometry.fill',
						'stylers' : {
							'color' : '#1e2126'
						}
					}, {
						'featureType' : 'poi',
						'elementType' : 'all',
						'stylers' : {
							'visibility' : 'off'
						}
					}, {
						'featureType' : 'green',
						'elementType' : 'all',
						'stylers' : {
							'visibility' : 'off'
						}
					}, {
						'featureType' : 'subway',
						'elementType' : 'all',
						'stylers' : {
							'visibility' : 'off'
						}
					}, {
						'featureType' : 'manmade',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#404a59'
						}
					}, {
						'featureType' : 'local',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#404a59'
						}
					}, {
						'featureType' : 'arterial',
						'elementType' : 'labels',
						'stylers' : {
							'visibility' : 'off'
						}
					}, {
						'featureType' : 'boundary',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#1e2126'
						}
					}, {
						'featureType' : 'building',
						'elementType' : 'all',
						'stylers' : {
							'color' : '#404a59'
						}
					}, {
						'featureType' : 'label',
						'elementType' : 'labels.text.fill',
						'stylers' : {
							'color' : '#999'
						}
					}*/ ]
				}
			},
			series : [{
				name : '捐款详情',
				type : 'scatter',
				coordinateSystem : 'bmap',
				data : convertData2(data),
				symbolSize : function(val) {
					return val[2] / 10;
				},
				label : {
					normal : {
						formatter : '{b}',
						position : 'right',
						show : false
					},
					emphasis : {
						show : false
					}
				},
				itemStyle : {
					normal : {
						color : '#f4e925',
						shadowBlur : 10,
						shadowColor : '#f4e925'
					}
				},
			},
			{
				name : '捐款详情',
				type : 'effectScatter',
				coordinateSystem : 'bmap',
				data : convertData2(data.sort(function(a, b) {
					return b.value - a.value;
				}).slice(0, 6)),
				symbolSize : function(val) {
					return val[2] / 10;
				},
				showEffectOn : 'render',
				rippleEffect : {
					brushType : 'stroke'
				},
				hoverAnimation : true,
				label : {
					normal : {
						formatterL : '{b}',
						position : 'right',
						show : false
					}
				},
				itemStyle : {
					normal : {
						color : '#f4e925',
						shadowBlur : 10,
						shadowColor : '#333'
					}
				},
				zlevel : 1
			}, {
				name : '捐款详情',
				type : 'lines',
				coordinateSystem : 'bmap',
				zlevel : 2,
                effect: {
                    show: true,
                    period: 6,
                    trailLength: 0.7,
                    color: '#fff',
                    symbolSize: 4,
                    loop:false
                },
                lineStyle: {
                        normal: {
                        width: '',
                        color: '#a6c84c',
                        curveness: 0.2
                    }
                },
				data : convertData(DataFromTo)
			} 
			],
			tooltip : {
				show : true,
				trigger : 'item',
				formatter: function(params) { 
                  if(typeof(params.data.coords)=='undefined'&&Number(params.data.value[4])<=0){
						return params.seriesName + '<br>总捐款金额：' + params.data.value[3] + '元';
					}
				  else if(typeof(params.data.coords)=='undefined'&&Number(params.data.value[4])>0){
						return '受助人：'+params.data.value[5] + '<br>获捐金额：' + params.data.value[3] + '元<br>获捐次数：' + params.data.value[4] + ' 人次';
					}
					else{
						return params.data.fromName + '<br>总捐款金额：' + params.data.coords[0][2] + '元';
					}
            	} 
			},
		};
		if (option && typeof option === "object") {
			var total = option.series[0].data.length;
			var count = 0;
			myChart.setOption(option, true);
			setInterval(function(){
				myChart.dispatchAction({
					type:'showTip',
					seriesIndex:0,
					dataIndex:count
				});	
				count++;
				if(count>=total){count = 0;}
			},2000);
		}
	}
</script>
<script>
	$(function(){
		var int=self.setInterval("show()",15000);
		var int2=self.setInterval("show()",3600000);
		$('#open').click(function(){
			window.location.reload();
		});
		$('#stop').click(function(){
			clearInterval(int);
		});
		
	});
</script>
</html>

