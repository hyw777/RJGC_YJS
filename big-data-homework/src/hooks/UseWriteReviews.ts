import { ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

export function useWriteReviews() {
  let isDisplayed = ref(false);
  let request = ref({
    businessId: "",
    bid: "",
    text: "",
    stars: 0,
  });
  let satisfactionData = ref({
    reviewId: 0,
    cool: 0,
    useful: 0,
    funny: 0,
  });

  function switchStatus() {
    isDisplayed.value = !isDisplayed.value;
  }

  // 添加情感分析函数
  async function analyzeSentiment(content: string) {
    try {
      const response = await axios.post("ppi/models/sentiment/", 
        `content=${encodeURIComponent(content)}`,
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }
      );
      return response.data.sentiment_result;
    } catch (error) {
      console.error("情感分析失败:", error);
      throw error;
    }
  }

  async function writeReview(businessId: string, bid: string) {
    console.log(`businessId = ${businessId}  bid = ${bid}`);

    // 输入验证
    if (request.value.stars === 0) {
      ElMessage({
        message: "请先选择评分",
        type: "warning",
      });
      return;
    }

    if (!request.value.text || request.value.text.trim() === "") {
      ElMessage({
        message: "请输入评论内容",
        type: "warning",
      });
      return;
    }

    request.value.businessId = businessId;
    request.value.bid = bid;

    try {
      // 在提交评论前进行情感分析
      const sentimentResult = await analyzeSentiment(request.value.text);
      
      // 将情感分析结果添加到请求数据中
      const reviewData = {
        ...request.value,
        aiStars: sentimentResult['score_5point']
      };

      const res = await axios.post("/api/review/writeReview", reviewData);

      if (res.data.code === 200) {
        ElMessage({
          message: "评论提交成功",
          type: "success",
        });

        // 重置表单
        request.value.text = "";
        request.value.stars = 0;

        // 关闭评论表单
        isDisplayed.value = false;

        // 触发自定义事件通知其他组件刷新数据
        window.dispatchEvent(new CustomEvent("reviewSubmitted"));
      } else {
        ElMessage({
          message: res.data.message || "提交失败",
          type: "error",
        });
      }
    } catch (error) {
      ElMessage({
        message: "网络错误，请稍后重试",
        type: "error",
      });
      console.error("提交评价失败:", error);
    }
  }

  async function satisfaction() {
    try {
      let res = await axios.put(
        "/api/review/satisfaction",
        satisfactionData.value
      );
      ElMessage({
        message: res.data.data,
        type: "success",
      });
    } catch (error) {
      console.log(error);
    }
  }

  return {
    isDisplayed,
    switchStatus,
    writeReview,
    request,
    satisfactionData,
    satisfaction,
  };
}