import {ref} from "vue";
import axios from "axios";
import {useBaseInfo} from "@/hooks/UseBaseInfo";

export function useWorkbench() {
    let data =ref({
        "totalVisitCount": 10,
        "lastDayVisitCount": 1,
        "newVisits": -1,
        "yindex": "0,2,5,0,0,0,2,1",
        "xindex": "2024-06-05,2024-06-06,2024-06-07,2024-06-08,2024-06-09,2024-06-10,2024-06-11,2024-06-12"
    })

    let requestData = ref({
        "businessId": 1,
        "begin": "2024-06-05",
        "end": "2024-06-12"
    })

    let {baseInfo,getBaseInfo} = useBaseInfo()

    function getDate(d) {
        // 获取当前日期
        const currentDate = new Date();

        // 计算前七天的日期
        const previousDate = new Date(currentDate);
        previousDate.setDate(currentDate.getDate() - d);

        // 获取年、月、日
        const year = previousDate.getFullYear();
        const month = String(previousDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以需要加1
        const day = String(previousDate.getDate()).padStart(2, '0'); // 获取日期

        // 输出格式：年-月-日
        const formattedDate = `${year}-${month}-${day}`;
        return formattedDate;
    }

    async function getData(day) {
        // 获取当前一天日期
        requestData.value.end = getDate(1)
        // 获取前x天日期
        requestData.value.begin = getDate(day)
        await getBaseInfo()
        requestData.value.businessId = baseInfo.value.businessId
        await axios.post('/api/business/workBench',requestData.value).then(res => {
            data.value = res.data.data;
            data.value.xindex = res.data.data.xindex.split(',')
            data.value.yindex = res.data.data.yindex.split(',').map(Number)
            if(data.value.newVisits < 0){
                data.value.newVisits = `减少了${0-data.value.newVisits}次`
            }else if(data.value.newVisits == 0){
                data.value.newVisits = `保持一致`
            }else {
                data.value.newVisits = `增加了${data.value.newVisits}次`
            }
        }).catch(err => {
            console.error(err);
        })
    }

    return {getDate,data,getData}
}