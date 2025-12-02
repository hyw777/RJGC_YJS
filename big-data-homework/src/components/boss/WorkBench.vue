<template>
  <div class="workbench">
    <div class="container">
      <div class="stats-card">
        <h2 class="card-title">数据概览</h2>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-label">总浏览量</div>
            <div class="stat-value">{{ data.totalVisitCount }}</div>
            <div class="stat-unit">次</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">昨日浏览量</div>
            <div class="stat-value">{{ data.lastDayVisitCount }}</div>
            <div class="stat-unit">次</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">昨日对比前日</div>
            <div class="stat-value" :class="{ 'positive': data.newVisits > 0, 'negative': data.newVisits < 0 }">
              {{ data.newVisits > 0 ? '+' : '' }}{{ data.newVisits }}
            </div>
          </div>
        </div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h2 class="card-title">浏览量统计</h2>
          <div class="chart-controls">
            <el-button 
              @click="handleClick(7)" 
              :type="activePeriod === 7 ? 'primary' : 'default'" 
              class="period-button"
            >
              近7日
            </el-button>
            <el-button 
              @click="handleClick(30)" 
              :type="activePeriod === 30 ? 'primary' : 'default'"
              class="period-button"
            >
              近30日
            </el-button>
          </div>
        </div>
        <div id="main" class="chart-container"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, toRefs, nextTick} from 'vue';
import { UseButtonStore } from '@/stores/UseButtonStore';
import { useWorkbench } from '@/hooks/UseWorkbench';
import * as echarts from 'echarts';

const buttonStore = UseButtonStore();
const { data, getData } = toRefs(useWorkbench())

let myChart
const activePeriod = ref(7)

const handleClick = async (days: number) => {
  activePeriod.value = days
  await getData.value(days);
  updateChart();
};

const updateChart = () => {
  if (!myChart) return;
  
  myChart.setOption({
    title: {
      text: '浏览量统计',
      textStyle: {
        color: '#343a40',
        fontSize: 18,
        fontWeight: 'normal'
      }
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#dee2e6',
      borderWidth: 1,
      textStyle: {
        color: '#495057'
      }
    },
    xAxis: {
      name: '日期',
      type: 'category',
      data: data.value.xindex,
      axisLine: {
        lineStyle: {
          color: '#dee2e6'
        }
      },
      axisLabel: {
        color: '#6c757d',
        rotate: 45,
        interval: 0,
        hideOverlap: true
      },
      axisTick: {
        show: false
      }
    },
    yAxis: {
      name: '浏览量',
      type: 'value',
      axisLine: {
        lineStyle: {
          color: '#dee2e6'
        }
      },
      axisLabel: {
        color: '#6c757d'
      },
      splitLine: {
        lineStyle: {
          color: '#f1f3f5'
        }
      }
    },
    series: [
      {
        name: '浏览量',
        type: 'line',
        data: data.value.yindex,
        smooth: true,
        symbolSize: 8,
        itemStyle: {
          color: '#6c757d'
        },
        lineStyle: {
          color: '#6c757d',
          width: 2
        },
        areaStyle: {
          color: 'rgba(108, 117, 125, 0.1)'
        }
      }
    ],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    }
  });
};

onMounted(async () => {
  buttonStore.setBossButton(2);
  // 确保DOM已渲染后再初始化图表
  await nextTick();
  const chartElement = document.getElementById('main');
  if (chartElement) {
    myChart = echarts.init(chartElement);
    handleClick(7);
  }
});
</script>


<style scoped>
.workbench {
  display: flex;
  justify-content: center;
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100%;
}

.container {
  display: flex;
  flex-direction: column;
  gap: 24px;
  width: 100%;
  max-width: 1200px;
}

.stats-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 24px;
  transition: box-shadow 0.3s ease;
}

.stats-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 24px 0;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-4px);
}

.stat-label {
  font-size: 16px;
  color: #6c757d;
  margin-bottom: 12px;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #343a40;
}

.stat-unit {
  font-size: 16px;
  color: #6c757d;
  margin-left: 8px;
}

.positive {
  color: #28a745;
}

.negative {
  color: #dc3545;
}

.chart-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 24px;
  transition: box-shadow 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.chart-controls {
  display: flex;
  gap: 12px;
}

.period-button {
  border-radius: 6px;
}

.chart-container {
  width: 100% !important;
  height: 500px !important;
}
</style>
