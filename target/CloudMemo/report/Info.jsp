<%--
  Created by IntelliJ IDEA.
  User: tibashiki
  Date: 2023/05/25
  Time: 8:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9">

  <div class="data_list">
    <div class="data_list_title"><span class="glyphicon glyphicon-signal"></span>&nbsp;数据报表 </div>
    <div class="container-fluid">
      <div class="row" style="padding-top: 20px;">
        <div class="col-md-12">
          <%--柱状图容器--%>
          <div id="monthChart" style="height: 500px">

          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<%--
  echarts
      在需要使用的页面引入echarts的js文件
--%>
<script type="text/javascript" src="static/echarts/echarts.min.js"></script>
<script type="text/javascript">
  // prettier-ignore
  let dataAxis = ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够', '自', '动', '缩', '放'];
  // prettier-ignore
  let data = [220, 182, 191, 234, 290, 330, 310, 123, 442, 321, 90, 149, 210, 122, 133, 334, 198, 123, 125, 220];
  let yMax = 500;
  let dataShadow = [];
  for (let i = 0; i < data.length; i++) {
    dataShadow.push(yMax);
  }
  option = {
    title: {
      text: '特性示例：渐变色 阴影 点击缩放',
      subtext: 'Feature Sample: Gradient Color, Shadow, Click Zoom'
    },
    xAxis: {
      data: dataAxis,
      axisLabel: {
        inside: true,
        color: '#fff'
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
      z: 10
    },
    yAxis: {
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: '#999'
      }
    },
    dataZoom: [
      {
        type: 'inside'
      }
    ],
    series: [
      {
        type: 'bar',
        showBackground: true,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#2378f7' },
              { offset: 0.7, color: '#2378f7' },
              { offset: 1, color: '#83bff6' }
            ])
          }
        },
        data: data
      }
    ]
  };
  // Enable data zoom when user click bar.
  const zoomSize = 6;

</script>
