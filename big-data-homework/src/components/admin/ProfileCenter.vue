<template>
  <div class="container">
    <div class="left-panel">
      <div class="category" v-show="!display">
        <table>
          <tbody>
            <tr>
              <td>CurrentProfile</td>
              <td>
                <div class="name">{{ cate?.userGroupName }}</div>
              </td>
            </tr>
            <tr>
              <td>ConditionComment</td>
              <td>
                <div class="comment">{{ cate?.conditionComment }}</div>
              </td>
            </tr>
            <tr>
              <td>GroupNum</td>
              <td>{{ cate?.userGroupNum }}</td>
            </tr>
            <tr>
              <td>CreateTime</td>
              <td>{{ cate?.createTime }}</td>
            </tr>
            <tr>
              <td>UpdateTime</td>
              <td>{{ cate?.updateTime }}</td>
            </tr>
          </tbody>
        </table>
        <el-button @click="display = !display">Select</el-button>
        <div
          ref="chartRef"
          style="margin-top: 33px; width: 600px; height: 400px"
        ></div>
      </div>
      <div v-if="display">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="userGroupName" label="GroupName" width="180" />
          <el-table-column
            prop="conditionComment"
            label="ConditionComment"
            width="280"
          />
          <el-table-column prop="userGroupNum" label="GroupNum" width="180" />
          <el-table-column prop="createTime" label="CreateTime" width="180" />
          <el-table-column prop="updateTime" label="UpdateTime" width="180" />
          <el-table-column width="100" />
          <!-- 自定义选择列 -->
          <el-table-column label="Action" width="180">
            <template #default="scope">
              <el-button @click="findProfile(scope.row)">Select</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!--数据展示在这里-->

    <div class="right-panel">
      <div class="info-area">
        
        <!--           如何查询的是商家，则展示商家信息-->
        <div
          class="business-info"
          v-if="cate?.userGroupName.includes('商户') && !display"
          v-for="(info, index) in data"
          :key="index"
        >
          <div class="each-business">
            <div class="info-left">
              <img :src="`/api/images/${info.image}`" style="width: 180px; height: 180px" />
            </div>
            <div class="info-right">
              <div class="text-line"></div>
              <div class="info-head">
                <p class="business-name">{{ info.name }}</p>
                <p class="business-reviews">({{ info.reviewCount }} reviews)</p>
                <el-rate
                  v-model="info.stars"
                  disabled
                  style="margin-left: 30px; margin-top: 18px"
                />
                <p class="business-stars">{{ info.stars }}</p>
              </div>
              <div class="info-body">
                <p class="business-categories">{{ info.categories }}</p>
              </div>
              <div class="info-foot">
                <el-icon
                  style="color: #0073bb; font-size: 30px; margin-top: 30px"
                  ><OfficeBuilding
                /></el-icon>
                <p class="business-address">
                  {{ info.address }}
                </p>
              </div>
              <div class="info-bottom"></div>
            </div>
          </div>
        </div>
        <!--           如何查询的是用户，则展示用户信息-->
        <div
          class="user-info"
          v-if="cate?.userGroupName.includes('用户') && !display"
          v-for="(info, index) in data2"
          :key="index"
        >
          <div class="each-user">
            <div class="info-left">
              <img
                src="https://img2.baidu.com/it/u=660058380,2227239641&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=716"
                style="width: 90px; height: 90px"
              />
            </div>
            <div class="info-right">
              <div class="text-line"></div>
              <div class="info-head">
                <p class="user-name">{{ info.name }}</p>
                <p class="elite">elite:{{ info.elite }}</p>
              </div>
              <div class="info-body">
                <p class="user-age">{{ info.age }}</p>
                <p class="user-address">{{ info.city }}</p>
                <p class="cool">cool:{{ info.cool }}</p>
              </div>
              <div class="info-foot">
                <p class="review-number">{{ info.reviewCount }} reviews</p>
                <p class="useful">useful:{{ info.useful }}</p>
              </div>
              <div class="info-bottom">
                <p class="yelp-since">
                  yelpingSince:{{ formatDate(info.yelpingSince) }}
                </p>
                <p class="funny">funny:{{ info.funny }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="pageBottom"  v-if="cate?.userGroupName.includes('商户')">
        <el-pagination  
          background
          v-model:current-page="profileDTO.pageNum"
          layout="prev, pager, next"
          :page-count="Math.ceil(cate?.userGroupNum/7)"
          @current-change="findProfile(cate)"
          style="margin-left: 73px; margin-top: 40px"
        />
      </div>

      <div class="pageBottom"  v-if="cate?.userGroupName.includes('用户')">
        <el-pagination  
          background
          v-model:current-page="profileDTO.pageNum"
          layout="prev, pager, next"
          :page-count="Math.ceil(cate?.userGroupNum/9)"
          @current-change="findProfile(cate)"
          style="margin-left: 73px; margin-top: 40px"
        />
      </div>
    </div>

    
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, watchEffect } from "vue";
import * as echarts from "echarts";

const profileDTO = ref({
  pageSize: 0,
  pageNum: 1,
  id: "",
  groupName: "",
});

const tableData = ref([]);
const display = ref(false);
import axios from "@/interceptor/Axios";
const cate = ref();

// 响应式数据
let num1 = ref(100);
let num2 = ref(200);
let num3 = ref(300); // 假设num3是总数

// 图表容器的ref
const chartRef = ref(null);

// ECharts实例
let myChart = null;

// 监听num1, num2, num3的变化，并更新图表
watchEffect(() => {
  console.log("数据变化了");
  if (myChart) {
    // 这里可以直接重新调用setChartOptions来更新整个图表选项
    // 但更高效的方法是只更新变化的部分
    myChart.setOption({
      series: [
        {
          data: [
            { value: num1.value, name: "符合画像总人数" },
            { value: num2.value, name: "其他人数" },
          ],
        },
      ],
    });
  }
});

// 设置图表的选项
function setChartOptions() {
  const option = {
    title: {
      text: cate.value.userGroupName,
      left: "center",
      top: "80%",
    },
    tooltip: {
      trigger: "item",
    },
    legend: {
      orient: "vertical",
      left: "left",
    },
    series: [
      {
        name: "访问来源",
        type: "pie",
        radius: "55%",
        data: [
          { value: num1.value, name: "符合画像总人数" },
          { value: num2.value, name: "其他人数" },
          // 这里不直接添加num3，因为它代表总数，不单独显示
        ],
        label: {
          show: true,
          position: "outside", // 或者 'inside' 根据你的需求
          formatter: "{b}: {d}%", // 自定义标签格式，显示数据名和百分比
          // 其他标签样式配置...
        },
        color: ["#ff9900", "#5470c6"], // 自定义颜色
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  };
  myChart.setOption(option);
}

/*这里是模拟数据*/
const data = ref([]);

const data2 = ref([]);

const findProfile = (category) => {
  profileDTO.value.id = category.id;
  profileDTO.value.groupName = category.userGroupName;

  console.log(profileDTO.value);

  /*发送请求给后端获取符合画像的商家/用户信息*/
  axios
    .post("/api/profileManage/getDetail", profileDTO.value)
    .then((res) => {
      if (category.userGroupName.includes("用户")) {
        data2.value = res.data.data;
      } else {
        data.value = res.data.data;
      }
    })
    .catch((error) => {
      console.log(error);
    });

  console.log("当前类别" + category);
  cate.value = category;
  display.value = !display;
  num1.value = category.userGroupNum;
  console.log("num1的值变化为：" + num1.value);
  if (category.userGroupName.includes("商户")) {
    num3.value = 150346;
    num2.value = num3.value - num1.value;
    console.log("总共有这么多商家：" + num3.value);
  } else {
    num3.value = 1987897;
    num2.value = num3.value - num1.value;
    console.log("总共有这么多用户：" + num3.value);
  }

  myChart = echarts.init(chartRef.value);
  setChartOptions();
console.log(cate.value+"hahha")


};

const getCategories = async () => {
  try {
    const res = await axios.get("/api/profileManage/getCategories");
    // 假设res.data.data是一个包含多个对象的数组
    const formattedData = res.data.data.map((item) => ({
      id: item.id,
      userGroupName: item.userGroupName,
      conditionComment: item.conditionComment,
      userGroupNum: item.userGroupNum,
      createTime: formatDate(item.createTime), // 格式化createTime
      updateTime: formatDate(item.updateTime), // 格式化updateTime
    }));
    tableData.value = formattedData; // 更新tableData以显示格式化后的数据
  } catch (err) {
    console.log(err);
  }
};

onMounted(async () => {
  // 组件挂载后执行
  await getCategories(); // 等待获取分类数据完成
  // 确保 tableData.value 至少有一个元素
  if (tableData.value.length > 0) {
    // 开始执行查询具体画像
    findProfile(tableData.value[4]); // 使用正确的索引访问方法
  } else {
    console.log("tableData 为空");
  }
});

// 格式化日期的函数
function formatDate(dateString) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`; // 格式化到秒
}
</script>

<style scoped>
.container {
  display: flex;
}


.category table td {
  height: 10px;
  width: 180px;
}

.category table {
  table-layout: fixed;
}
.name {
  height: 30px;
  width: 220px;
  overflow: auto;
}
.comment {
  height: 30px;
  width: 220px;
  overflow: auto;
}

/* 给表格单元格添加边框 */
.category table td {
  border: 1px solid #000; /* 黑色边框，宽度为1px */
  padding: 8px; /* 可选，根据需要设置内边距 */
  overflow-x: auto; /* 允许水平滚动 */
}

/*返回的数据展示区域*/
.info-area {
  width: 100%;
  height: 77vh;
  overflow: auto;
}

.each-user {
  display: flex;
  margin-bottom: 0px;
  border: rgba(198, 193, 193, 0.18) 2px solid;
}

.info-right {
  display: flex;
  flex-direction: column;
}

.info-head {
  display: flex;
  margin-top: -18px;
}

.info-body {
  font-size: 14px;
  margin-top: -4vh;
  display: flex;
}

.info-foot {
  display: flex;
  margin-top: -25px;
  font-size: 14px;
}
.info-bottom {
  font-size: 14px;
  display: flex;
  margin-top: -22px;
}

.user-name {
  font-size: 18px;
  color: #216d7d;
}

.elite {
  margin-top: 20px;
  font-size: 14px;
  margin-left: 200px;
}

.user-address {
  margin-left: 30px;
  width: 28vh;
}
.review-number {
  width: 33vh;
}
.yelp-since {
  width: 33vh;
}
.cool {
  margin-left: 37%;
  color: #efc015;
}
.useful,
.funny {
  margin-left: 36%;
  color: #efc015;
}

.text-line {
  height: 1px;
  width: 80vh;
}

.each-business {
  display: flex;
  margin-bottom: 0px;
  border: rgba(198, 193, 193, 0.18) 2px solid;
}

.business-name {
  font-size: 29px;
  margin-top: 13px;
}

.business-stars {
  margin-top: 22px;
}

.business-reviews {
  font-size: 18px;
  margin-top: 23px;
}

.business-categories {
  font-size: 16px;
  width: 90%;
}

.business-address {
  font-size: 19px;
  margin-top: 30px;
}
</style>
