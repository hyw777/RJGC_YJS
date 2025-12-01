<template>
  <div class="search">
    <NewIndexView
        :show-background-description="false"
        style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"></NewIndexView>
    <div class="display">
      <div class="ai-recommend">
        <div class="ai-header">
          <div class="ai-left">
            <span class="ai-title">AI为您推荐</span>
            <div class="ai-sub">基于您的搜索为您智能推荐相似商家或体验</div>
          </div>
          <div class="ai-action">
            <el-button size="small" type="primary" @click="fetchAIRecommendationsWrapper" :loading="aiLoading">生成推荐</el-button>
          </div>
        </div>
        <div class="ai-body" v-if="aiRecommendations.length">
          <div class="ai-content" v-for="(item, idx) in aiRecommendations" :key="idx">
            <div v-html="renderMarkdown(item)"></div>
          </div>
        </div>
        <div class="ai-empty" v-else>点击“生成推荐”根据当前搜索内容获取智能推荐</div>
      </div>
      
      <div class="title">
        相关商家
      </div>
      <div class="mid">
        <div class="show-box" v-for="(record,index) in result.records" :key="index">
          <router-link class="img" :to="{path:'/merchantDetail',query:{id:record.businessId}}">
            <img :src="filePath(record.image)" >
          </router-link>
          <div class="info-box">
            <div class="info1">
              <span>{{record.name}}</span>
            </div>
            <div class="info2">
              <el-rate v-model="record.stars" size="large" disabled></el-rate>
              <div class="rate-box">
                <div style="margin-right: 1%">{{record.stars}}</div>
                <span>({{record.reviewCount}} reviews)</span>
              </div>
            </div>
            <div class="info3">
              <span>{{record.categories}}</span>
            </div>
          </div>
        </div>
      </div>
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="search.page"
          :page-size="search.pageSize"
          :hide-on-single-page="search.page"
          :total="result.total"
          class="page" background layout="prev,pager, next"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import {useSearch} from "@/hooks/UseSearch";
import { useAuthStore } from "@/stores/UseAuthStore";
import {useRoute} from "vue-router";
import {computed, onMounted, ref, toRefs, watch} from "vue";
import {UseSearchStore} from "@/stores/UseSearchStore";
import { marked } from 'marked';

const filePath  =(file) => {
  if(file == null){
    return ``
  }
  return  file.includes('http') ? file : `/api/images/${file}`;
}

let searchStore = UseSearchStore();
let route = useRoute()
// get fetchAIRecommendations and ai state from hook
let {search,getResult, fetchAIRecommendations, aiLoading, aiRecommendations} = useSearch()

function  handleCurrentChange(val){
  getResult.value(val,route.query.info)
}

let result = computed(()=>searchStore.result)

// expose a thin wrapper so the page triggers the hook function using current route.query.info
async function fetchAIRecommendationsWrapper() {
  const query = (route.query.info as string) || '';
  const APIkey = 'sk-jsppmnzualuadnsjwnneaqsupkcpjfoungipzaahqygoqhqw'
  try {
    // set loading
    if (aiLoading && typeof aiLoading === 'object') aiLoading.value = true;
    const url = 'https://api.siliconflow.cn/v1/chat/completions';
    const body = {
      model: 'deepseek-ai/DeepSeek-V2.5',
      messages: [
        { role: 'user', content: `基于我的搜索内容："${query}"，请推荐相似的商家或体验，并简要说明推荐理由。` }
      ],
      // request streaming if the API supports it (service-dependent)
      stream: true
    };

    const headers: Record<string, string> = { 'Content-Type': 'application/json' };
    headers.Authorization = `Bearer ${APIkey}`;

    const response = await fetch(url, {
      method: 'POST',
      headers,
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      // non-2xx — try to parse JSON error then throw
      const errText = await response.text();
      throw new Error(`AI API error: ${response.status} ${errText}`);
    }

    // If the server sends a streaming response, read it progressively
    const reader = response.body?.getReader();
    const decoder = new TextDecoder();

    if (reader) {
      // accumulate only the `content` fields (delta.content or final message.content)
      let accumulatedContent = '';

      while (true) {
        const { value, done } = await reader.read();
        if (done) break;
        const chunk = decoder.decode(value, { stream: true });

        // split into lines (SSE often sends lines like: data: {...})
        const lines = chunk.split(/\r?\n/);
        for (let rawLine of lines) {
          const line = rawLine.trim();
          if (!line) continue;
          // remove SSE prefix if present
          const payload = line.startsWith('data:') ? line.replace(/^data:\s*/i, '') : line;
          if (payload === '[DONE]') continue;

          try {
            const parsed = JSON.parse(payload);
            // prefer incremental delta content, fallback to message content or result
            const delta = parsed?.choices?.[0]?.delta;
            const contentPart = delta?.content ?? parsed?.choices?.[0]?.message?.content ?? parsed?.result;
            if (contentPart != null) {
              accumulatedContent += String(contentPart);
              if (aiRecommendations && typeof aiRecommendations === 'object') {
                aiRecommendations.value = [toMarkdown(accumulatedContent)];
              }
            }
          } catch (e) {
            // not a JSON payload; ignore non-JSON fragments
          }
        }
      }

      // final: if the stream itself produced a large JSON blob as text, try to extract content
      let finalContent = accumulatedContent;
      try {
        const maybe = JSON.parse(accumulatedContent);
        finalContent = maybe?.choices?.[0]?.message?.content ?? maybe?.result ?? accumulatedContent;
      } catch (e) {
        // keep accumulatedContent
      }

      if (aiRecommendations && typeof aiRecommendations === 'object') aiRecommendations.value = [toMarkdown(finalContent)];
    } else {
      // fallback: non-streaming response
      const data = await response.json();
      const content = data?.choices?.[0]?.message?.content ?? data?.result ?? JSON.stringify(data);
      const md = toMarkdown(Array.isArray(content) ? content.join('\n\n') : String(content));
      if (aiRecommendations && typeof aiRecommendations === 'object') aiRecommendations.value = [md];
    }
  } catch (err) {
    console.error('AI recommend error:', err);
    if (aiRecommendations && typeof aiRecommendations === 'object') aiRecommendations.value = [];
  } finally {
    if (aiLoading && typeof aiLoading === 'object') aiLoading.value = false;
  }
}

// simple helper to convert plain text into a Markdown block — adjust as needed
function toMarkdown(text: string) {
  if (!text) return '';
  const trimmed = text.trim();
  
  // Check if text already contains markdown-like formatting
  if (/^(#|\-|>|```|\d+\.)/m.test(trimmed)) {
    // If it's already markdown, ensure it has a title if missing
    if (/^### 推荐结果/.test(trimmed)) {
      return trimmed;
    } else {
      return `### 推荐结果\n\n${trimmed}`;
    }
  }

  // For plain text, convert to markdown
  const asParagraphs = trimmed.split(/\n{2,}/).map(p => p.trim()).filter(Boolean).join('\n\n');
  return `### 推荐结果\n\n${asParagraphs}`;
}

function renderMarkdown(markdownText: string) {
  if (!markdownText) return '';
  return marked(markdownText);
}

</script>

<style scoped>
.search {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
}

.display {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  margin: 1% 15% 0px;
}

.title {
  margin-bottom: 2%;
  align-self: start;
  font-size: 25px;
  font-weight: bold;
}

.mid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-content: start;
  height: 80%;
}

.show-box {
  display: flex;
  flex-direction: row;
  width: 45%;
  height: 20%;
  padding-bottom: 1.8%;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
  margin-bottom: 4%;
}

.img {
  width: 25%;
  height: 140px;
  cursor: pointer;
}

.img img {
  width: 100%;
  height: 100%;
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: start;
  width: 75%;
  height: 100%;
  margin-left: 4%;
}

.info1 {
  color: #2D2E2F;
  font-size: 30px;
  margin-bottom: 2.3%;
}

.info2 {
  display: flex;
  margin-bottom: 2%;
}

.info2 span {
  color: #6E7072;
}

.rate-box {
  display: flex;
  align-items: center;
  margin-left: 3%;
  width: 100%;
}

.info3 span {
  color: #6E7072;
  font-size: 16px;
  padding: 0.5%;
  background-color: #edeff1;
  margin-left: 1.1%;
  margin-right: 5%;
}

:deep(.info2 svg) { vertical-align: middle; }

/* AI recommend styles */
.ai-recommend {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 18px;
  padding: 14px 16px;
  background: #fbfbfd;
  border: 1px solid #eef2f6;
  border-radius: 8px;
}
.ai-header { display:flex; justify-content:space-between; align-items:center; }
.ai-left { display:flex; flex-direction:column; }
.ai-title { font-weight:700; font-size:18px; color:#0f1721; }
.ai-sub { color:#6b6f73; font-size:13px; margin-top:4px; }
.ai-body { display:flex; gap:10px; flex-wrap:wrap; margin-top:6px; }
.ai-item { background:#fff; padding:8px 12px; border-radius:12px; border:1px solid #e6eef6; color:#16202b; font-size:14px; }
.ai-empty { color:#6b6f73; font-size:13px; }

.page {
  margin-left: 40%;
}
</style>