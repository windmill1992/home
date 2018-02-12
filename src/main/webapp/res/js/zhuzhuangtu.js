var pillar2 = echarts.init(document.getElementById('pillar2'));
pillar2.setOption({
    color:["#00afff"],
    tooltip : {
        trigger: 'axis'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        },
        formatter: "{a} <br/>{b} : {c}%"
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    calculable : true,
    xAxis : [
        {
            type: 'value',
            boundaryGap: [0, 0.01],
            "axisLabel": {
                "interval": 0,
                formatter: '{value}%',
            },
            splitLine:{
                show:false,
            },
            axisLabel: {
                interval:1,
                textStyle:{
                    color:'#fff'
                }
            },
        }
    ],
    yAxis : [
        {
            type: 'category',
            data: ['捐赠人',
                '捐赠人',
                '捐赠人',
                '捐赠人',
                '捐赠人',
                '捐赠人',
                '捐赠人',
                '受助人',
                '受助人',
                '受助人',
                '受助人',
            ],
            splitLine:{
                show:false,
            },
            axisLabel: {
                interval:1,
                textStyle:{
                    color:'#fff'
                }
            },
        }
    ],
    series : [
        {
            name: '2016年占比',
            type: 'bar',
            data: [2.00, 3.00, 5.00, 5.50, 6.50, 6.50, 7.50, 8.50, 8.50, 9.50, 10.0]
        }
    ]
});
