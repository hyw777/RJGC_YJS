<template>
  <div class="workbench">
    <!-- 加载指示器 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <el-icon class="is-loading" size="48"><Loading /></el-icon>
        <p>数据加载中...</p>
      </div>
    </div>

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

      <!-- AI策略分析模块 -->
      <div class="ai-analysis-card">
        <div class="ai-analysis-header">
          <h2 class="card-title">AI策略分析</h2>
          <el-button 
            @click="handleAIAnalysis" 
            :loading="aiAnalysis.loading"
            type="primary"
            class="analyze-button"
          >
            AI分析
          </el-button>
        </div>
        
        <div class="ai-analysis-content">
          <div v-if="aiAnalysis.loading" class="analysis-loading">
            <el-icon class="is-loading" size="24"><Loading /></el-icon>
            <span>AI正在分析中，请稍候...</span>
          </div>
          
          <div v-else-if="aiAnalysis.recommendations.length > 0 || aiAnalysis.tips.length > 0">
            <!-- 推荐建议 -->
            <div class="analysis-section" v-if="aiAnalysis.recommendations.length > 0">
              <h3 class="section-title">
                <el-icon color="#409eff"><Promotion /></el-icon>
                优化建议
              </h3>
              <ul class="recommendations-list">
                <li 
                  v-for="(recommendation, index) in aiAnalysis.recommendations" 
                  :key="index"
                  class="recommendation-item"
                >
                  <el-icon color="#409eff"><Lightning /></el-icon>
                  <span>{{ recommendation }}</span>
                </li>
              </ul>
            </div>
            
            <!-- 运营技巧 -->
            <div class="analysis-section" v-if="aiAnalysis.tips.length > 0">
              <h3 class="section-title">
                <el-icon color="#67c23a"><MagicStick /></el-icon>
                运营技巧
              </h3>
              <ul class="tips-list">
                <li 
                  v-for="(tip, index) in aiAnalysis.tips" 
                  :key="index"
                  class="tip-item"
                >
                  <el-icon color="#67c23a"><Star /></el-icon>
                  <span>{{ tip }}</span>
                </li>
              </ul>
            </div>
          </div>
          
          <div v-else class="analysis-empty">
            <el-icon size="48" color="#c0c4cc"><Help /></el-icon>
            <p>点击"AI分析"按钮获取AI优化建议</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, toRefs, nextTick} from 'vue';
import { UseButtonStore } from '@/stores/UseButtonStore';
import { useWorkbench } from '@/hooks/UseWorkbench';
import * as echarts from 'echarts';
import { 
  Loading, 
  Promotion, 
  Lightning, 
  MagicStick, 
  Star, 
  Help 
} from '@element-plus/icons-vue';

const buttonStore = UseButtonStore();
const workbench = useWorkbench();
const { data, getData, aiAnalysis, getAIAnalysis, activePeriod } = toRefs(workbench);

let myChart: echarts.ECharts | null = null // 添加类型声明
const loading = ref(true) // 添加加载状态

const handleClick = async (days: number) => {
  loading.value = true // 开始加载
  activePeriod.value = days
  await getData.value(days);
  updateChart();
  loading.value = false // 加载完成
};

// 处理AI分析
const handleAIAnalysis = async () => {
  workbench.activePeriod = activePeriod.value; // 传递当前时间段给hook
  await getAIAnalysis.value();
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
    await handleClick(7); // 等待初始数据加载完成
  }
});
</script>


<style scoped>
/* 添加加载样式 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-spinner {
  text-align: center;
}

.loading-spinner p {
  margin-top: 16px;
  color: #606266;
  font-size: 16px;
}

/* 添加旋转动画样式 - 仅应用于指定的加载图标 */
.loading-overlay .is-loading {
  animation: rotating 2s linear infinite;
}

.analysis-loading .is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

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

/* AI策略分析模块样式 */
.ai-analysis-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 24px;
  transition: box-shadow 0.3s ease;
}

.ai-analysis-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.ai-analysis-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.analyze-button {
  border-radius: 6px;
}

.analysis-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 32px;
  color: #6c757d;
}

.analysis-empty {
  text-align: center;
  padding: 48px 24px;
  color: #c0c4cc;
}

.analysis-empty p {
  margin-top: 16px;
  font-size: 16px;
}

.analysis-section {
  margin-bottom: 32px;
}

.analysis-section:last-child {
  margin-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 16px 0;
}

.recommendations-list,
.tips-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recommendation-item,
.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  margin-bottom: 12px;
  border-radius: 8px;
  background-color: #f8f9fa;
  transition: all 0.3s ease;
}

.recommendation-item:hover,
.tip-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.recommendation-item:last-child,
.tip-item:last-child {
  margin-bottom: 0;
}

.recommendation-item span,
.tip-item span {
  flex: 1;
  color: #495057;
  line-height: 1.6;
}
</style>
