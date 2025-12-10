import { ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";
export function useMerchant() {
  let merchantVisible = ref(false);
  let merchantForm = ref({
    name: "",
    address: "",
    city: "",
    state: "",
    postalCode: "",
    latitude: null,
    longitude: null,
    categories: "",
    hours: "",
    attributes: "",
  });
  async function apply() {
    await axios
      .post("/api/business/register", merchantForm.value)
      .then((response) => {
        ElMessage({
          message: "申请发送成功,请等待管理员审核",
          type: "success",
        });
      })
      .catch((error) => {
        ElMessage({
          message: error,
          type: "error",
        });
      });
  }
  async function submitMerchan() {
    await apply();
    merchantVisible.value = false;
  }
  return { merchantVisible, merchantForm, submitMerchan };
}
