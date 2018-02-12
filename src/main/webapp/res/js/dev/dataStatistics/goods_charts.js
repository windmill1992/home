$(function(){
var xs=$('#time').val(),ys=$('#money').val(),num=$('#num').val();
//折线图
var line = echarts.init(document.getElementById('line'));

var xss = xs.split(",");
var yss = ys.split(",");
var nums = num.split(",");
line.setOption({
    color:["#fc9d32"],
    tooltip: {
        trigger: 'axis',
        axisPointer:{
            type: 'line',
            lineStyle: {
                color: '#ff9002',
                width: 1,
                type: 'solid'
            }
        }
    },
    toolbox: {
        show: true,
        feature: {
            dataZoom: {
                yAxisIndex: 'none'
            },
            dataView: {readOnly: false},
            magicType: {type: ['line', 'bar']}
        }
    },
    xAxis:  {
        type: 'category',
        boundaryGap: [0,200],
        //data: ['11/01','11/07','11/14','11/21','11/28'],
        data: xss,
        axisLabel: {
            interval:1,
            textStyle:{
                color:'#000'
            }
        },
        axisTick:{
            show:false,

        },
        axisLine:{
            lineStyle:{
                color:'#ccc',
                type:'solid'
            },
        },
    },
    yAxis: {
        type: 'value',
        data: ['0.1','0.5','1','1.5','2','2.5'],
        axisTick:{
            show:false,

        },
        axisLine:{
            show:false,
        },
    },
    series: [
        {
            name:'捐款金额',
            type:'line',
            //data:[200, 100, 600, 500, 1000, 800],
            data:yss,
        },
        {
            name:'捐赠人数',
            type:'line',
            symbol:'none',
            //data:[200, 100, 600, 500, 1000, 800],
            data:nums,
            itemStyle : {
                normal : {
                    lineStyle:{
                        color:'rgba(0,0,0,0)'
                    }
                }
            }
        }
    ],
}) ;
})