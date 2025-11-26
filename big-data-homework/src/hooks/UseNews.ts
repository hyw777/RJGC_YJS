import { ref } from "vue";
import axios from "axios";

export function useNews() {
    const news = ref([]);

    const formatTime = (timeStr) => {
        const date = new Date(timeStr);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };

    const formatContentTime = (content) => {
        return content.replace(/(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d+)/g, match => formatTime(match.replace('.0', '')))
            .replace(/(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d+)/g, match => match.split('.')[0]);
    };

    news.value = news.value.map(item => ({
        ...item,
        time: formatTime(item.time),
        content: formatContentTime(item.content)
    }));

    async function getNews() {
        try {
            let res = await axios.get('/api/user/getNews');
            news.value = res.data.data.map(item => ({
                ...item,
                time: formatTime(item.time),
                content: formatContentTime(item.content)
            }));
            console.log(news.value);
        } catch (e) {
            console.log(e);
        }
    }

    return { news, getNews };
}
