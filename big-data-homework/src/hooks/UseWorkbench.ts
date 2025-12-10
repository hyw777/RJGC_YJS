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

    // AI分析结果
    let aiAnalysis = ref({
        loading: false,
        recommendations: [] as string[],
        tips: [] as string[]
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

    // 获取AI分析建议
    async function getAIAnalysis() {
        aiAnalysis.value.loading = true;
        try {
            await getBaseInfo(); // 确保获取最新的商户信息
            
            // 准备分析数据
            const analysisData = {
                businessInfo: {
                    name: baseInfo.value.name,
                    categories: baseInfo.value.categories,
                    stars: baseInfo.value.stars,
                    reviewCount: baseInfo.value.reviewCount,
                    hours: baseInfo.value.hours,
                    goodForKids: baseInfo.value.goodForKids,
                    businessAcceptsCreditcards: baseInfo.value.businessAcceptsCreditcards
                },
                visitData: {
                    totalVisitCount: data.value.totalVisitCount,
                    lastDayVisitCount: data.value.lastDayVisitCount,
                    trend: data.value.newVisits
                },
                reviews: baseInfo.value.reviewVOList.slice(0, 5), // 取前5条评论用于分析
                period: activePeriod.value // 当前选择的时间段
            };

            // 调用AI API获取分析结果
            const aiResponse = await fetchAIAnalysis(analysisData);
            
            // 解析AI响应
            aiAnalysis.value.recommendations = parseRecommendations(aiResponse);
            aiAnalysis.value.tips = parseTips(aiResponse);
        } catch (error) {
            console.error("AI分析出错:", error);
            aiAnalysis.value.recommendations = ["分析服务暂时不可用，请稍后再试"];
        } finally {
            aiAnalysis.value.loading = false;
        }
    }

    // 调用AI API
    async function fetchAIAnalysis(data: any) {
        const API_KEY = 'sk-jsppmnzualuadnsjwnneaqsupkcpjfoungipzaahqygoqhqw'; // 实际项目中应从环境变量获取
        
        // 构造提示词
        let prompt = `你是一个专业的商业顾问，需要根据以下商户数据分析并提供优化建议和运营技巧：
        
商户信息：
- 名称：${data.businessInfo.name || '未知'}
- 分类：${data.businessInfo.categories || '未分类'}
- 评分：${data.businessInfo.stars || '暂无'}星
- 评论数：${data.businessInfo.reviewCount || 0}条
- 营业时间：${data.businessInfo.hours || '未提供'}
- 是否适合儿童：${data.businessInfo.goodForKids === "true" ? "是" : data.businessInfo.goodForKids === "false" ? "否" : "未知"}
- 是否接受信用卡：${data.businessInfo.businessAcceptsCreditcards === "true" ? "是" : data.businessInfo.businessAcceptsCreditcards === "false" ? "否" : "未知"}

访问数据：
- 总浏览量：${data.visitData.totalVisitCount || 0}次
- 昨日浏览量：${data.visitData.lastDayVisitCount || 0}次
- 浏览量趋势：${data.visitData.trend || '未知'}

用户评论（最多5条）：
`;
        
        // 限制评论长度以避免超出token限制
        const maxReviewLength = 200; // 进一步减少每条评论长度
        data.reviews.forEach((review: any, index: number) => {
            let reviewText = review.text || '无内容';
            // 如果评论太长则截断
            if (reviewText.length > maxReviewLength) {
                reviewText = reviewText.substring(0, maxReviewLength) + '...';
            }
            prompt += `${index + 1}. 评分：${review.stars || '无'}星，评论：${reviewText}\n`;
        });

        // 限制总prompt长度
        const maxPromptLength = 8000;
        if (prompt.length > maxPromptLength) {
            prompt = prompt.substring(0, maxPromptLength);
        }

        prompt += `
请基于以上信息，以专业的角度提供：
1. 3-5条针对性的优化建议，每条建议应具体可行
2. 3-5条实用的运营技巧，帮助商户提升业绩

请严格按照以下JSON格式输出结果，不要包含其他内容：
{
  "recommendations": ["建议1", "建议2", "..."],
  "tips": ["技巧1", "技巧2", "..."]
}`;

        // 配置请求参数，参照MerchantDetailView.vue中的工作配置
        const requestOptions = {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${API_KEY}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                model: 'deepseek-ai/DeepSeek-V3',
                messages: [
                    {
                        role: 'user',
                        content: prompt
                    }
                ],
                max_tokens: 2048,
                temperature: 0.1,  // 降低随机性，保证结果稳定
                top_p: 0.7,
                frequency_penalty: 0.5,
                n: 1,
                response_format: {type: 'json_object'}
            })
        };

        try {
            const response = await fetch('https://api.siliconflow.cn/v1/chat/completions', requestOptions);
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`API请求失败: ${response.status} ${response.statusText} - ${errorText}`);
            }
            const result = await response.json();
            return result.choices[0].message.content;
        } catch (error) {
            console.error("AI API调用失败:", error);
            throw error;
        }
    }

    // 解析推荐建议
    function parseRecommendations(aiResponse: string): string[] {
        try {
            const parsed = JSON.parse(aiResponse);
            return Array.isArray(parsed.recommendations) ? parsed.recommendations : [];
        } catch (error) {
            console.error("解析推荐建议失败:", error);
            return [];
        }
    }

    // 解析运营技巧
    function parseTips(aiResponse: string): string[] {
        try {
            const parsed = JSON.parse(aiResponse);
            return Array.isArray(parsed.tips) ? parsed.tips : [];
        } catch (error) {
            console.error("解析运营技巧失败:", error);
            return [];
        }
    }

    // 当前活跃时间段（从组件传入）
    const activePeriod = ref(7);

    return {getDate, data, getData, aiAnalysis, getAIAnalysis, activePeriod}
}