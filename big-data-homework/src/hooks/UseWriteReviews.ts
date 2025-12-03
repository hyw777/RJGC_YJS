import { ref } from "vue";
import axios from "axios";

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
      const res = await axios.post("/api/review/writeReview", request.value);

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
