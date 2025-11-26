<template>
  <div class="workbench">
    <div class="box">
      <div class="left">
        <div class="hh">
          <span class="hh1">总浏览量&nbsp;:&nbsp;&nbsp;</span>
          <span class="hh2">{{ data.totalVisitCount }}&nbsp;次</span>
        </div>
        <div class="hh">
          <span class="hh1">昨日浏览量&nbsp;:&nbsp;&nbsp;</span>
          <span class="hh2">{{ data.lastDayVisitCount }}&nbsp;次</span>
        </div>
        <div class="hh">
          <span class="hh1">昨日浏览量对比前日浏览量&nbsp;:&nbsp;&nbsp;</span>
          <span class="hh2">{{ data.newVisits }}&nbsp;</span>
        </div>
      </div>
      <div class="right">
        <div class="buttons">
          <el-button @click="handleClick(7)" type="primary" color="#E00707">近7日</el-button>
          <el-button @click="handleClick(30)" type="primary" color="#E00707">近30日</el-button>
        </div>
        <div id="main" style="width: 90%; height: 60%;"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, toRefs} from 'vue';
import { UseButtonStore } from '@/stores/UseButtonStore';
import { useWorkbench } from '@/hooks/UseWorkbench';
import * as echarts from 'echarts';

const buttonStore = UseButtonStore();
const { data, getData } = toRefs(useWorkbench())

let myChart

const handleClick = async (days: number) => {
  await getData.value(days);
  updateChart();
};

const updateChart = () => {
  myChart.setOption({
    title: {
      text: '浏览量统计'
    },
    tooltip: {},
    xAxis: {
      name: '日期',
      data: data.value.xindex,
      axisLabel: {
        rotate: 45, // 旋转角度
        interval: 0, // 标签间隔，0 表示所有标签都显示
        hideOverlap: true // 自动隐藏重叠标签
      }
    },
    yAxis: {
      name: '浏览量'
    },
    series: [
      {
        name: '浏览量',
        type: 'line',
        data: data.value.yindex
      }
    ]
  });
};

onMounted(async () => {
  buttonStore.setBossButton(2);
  myChart = echarts.init(document.getElementById('main'));
  handleClick(7)
});
</script>


<style scoped>
.workbench {
  display: flex;
  justify-content: center;
}

.box {
  display: flex;
  align-items: center;
  width: 80%;
  height: 90%;
}

.left {
  width: 45%;
  display: flex;
  flex-direction: column;
}

.hh {
  display: flex;
  width: 100%;
  margin-bottom:20px;
  color: #2D2E2F;
  flex-direction: column;
}

.hh1 {
  font-size: 30px;
  font-weight: 800;
}

.hh2 {
  margin-top: 10px;
  font-size: 23px;
}

.right {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  width: 55%;
}

.buttons {
  margin-top: 100px;
}
</style>
