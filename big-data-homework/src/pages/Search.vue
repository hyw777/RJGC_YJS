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
        <div class="ai-top" v-if="aiTop.length">
          <div class="ai-top-title">优先推荐商家</div>
          <div class="ai-top-list">
            <div class="top-card" v-for="(t, i) in aiTop" :key="i">
              <div class="top-thumb">
                <img v-if="t.record && t.record.image" :src="filePath(t.record.image)" alt="商家图" />
                <div v-else class="thumb-placeholder"></div>
              </div>
              <div class="top-body">
                <div class="top-name">
                  <span v-if="t.businessId">{{ (t.name || '').replace(/\*\*/g, '') }}</span>
                  <span v-else>{{ (t.name || '').replace(/\*\*/g, '') }}</span>
                </div>
                <div class="top-reason">{{ t.record ? (t.record.categories || '') : '' }}</div>
                <div class="top-action">
                  <el-button v-if="t.businessId" type="primary" @click="() => { router.push({ path: '/merchantDetail', query: { id: t.businessId } }) }">查看详情</el-button>
                  <el-button v-else type="text" disabled>查看详情</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="ai-empty" v-else>点击“生成推荐”根据当前搜索内容获取智能推荐</div>
      </div>
      
      <div class="title">
        相关商家
      </div>
      <div class="mid">
        <div class="show-box" v-for="(record,index) in records" :key="index">
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
            :total="total"
            class="page" background layout="prev,pager, next"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import {useSearch} from "@/hooks/UseSearch";
import { useAuthStore } from "@/stores/UseAuthStore";
import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, ref, toRefs, watch} from "vue";
import {UseSearchStore} from "@/stores/UseSearchStore";
import { marked } from 'marked';

const filePath = (file: any) => {
  if (file == null) {
    console.log('null');
    return ''
  }
  // 如果图片链接已经是完整的URL，直接使用；否则拼接静态服务器地址
  const fullPath = file.includes('http') ? file : `http://localhost:3000/images/${file}.jpg`;
  return fullPath;
}
let searchStore = UseSearchStore();
let route = useRoute()
const router = useRouter();
// get fetchAIRecommendations and ai state from hook
let {search, getResult, aiLoading, aiRecommendations} = useSearch()
// parsed top recommendations from AI (array of {name, reason, businessId?, record?})
import { ref as _ref } from 'vue';
const aiTop = _ref([])

// --- simple localStorage cache for AI replies (persist across refresh/navigation)
const CACHE_PREFIX = 'ai_recommend_v1_';
function cacheKeyFor(q: string) {
  const key = String(q || '').trim();
  return CACHE_PREFIX + (key ? encodeURIComponent(key) : '__global');
}
function saveCacheFor(q: string) {
  try {
    const payload = { aiRecommendations: Array.isArray(aiRecommendations?.value) ? aiRecommendations.value : [], aiTop: Array.isArray(aiTop?.value) ? aiTop.value : [], ts: Date.now() };
    localStorage.setItem(cacheKeyFor(q), JSON.stringify(payload));
  } catch (e) {
    console.warn('saveCacheFor failed', e);
  }
}
function loadCacheFor(q: string) {
  try {
    const raw = localStorage.getItem(cacheKeyFor(q));
    if (!raw) return false;
    const obj = JSON.parse(raw);
    if (obj?.aiRecommendations) {
      if (aiRecommendations && typeof aiRecommendations === 'object') aiRecommendations.value = obj.aiRecommendations;
    }
    if (obj?.aiTop) aiTop.value = obj.aiTop;
    return true;
  } catch (e) {
    console.warn('loadCacheFor failed', e);
    return false;
  }
}
function clearCacheFor(q: string) {
  try { localStorage.removeItem(cacheKeyFor(q)); } catch (e) { /* ignore */ }
}

function  handleCurrentChange(val){
  // call the hook function directly
  getResult(val, route.query.info)
  console.log("当前页码："+JSON.stringify(result.value))
}

// load cached AI reply for current query on mount; clear when query changes
onMounted(() => {
  const q = String(route.query.info || '');
  loadCacheFor(q);
});

watch(() => route.query.info, (nv, ov) => {
  const q = String(nv || '');
  // If there's cached data for the new query, load it; otherwise clear displayed AI reply
  if (!loadCacheFor(q)) {
    if (aiRecommendations && typeof aiRecommendations === 'object') aiRecommendations.value = [];
    aiTop.value = [];
  }
});

let result = computed(()=>searchStore.result)
// safe derived values for template
import { computed as _computed } from 'vue';
const records = _computed(() => (result.value && Array.isArray(result.value.records)) ? result.value.records : [])
const total = _computed(() => (result.value && typeof result.value.total === 'number') ? result.value.total : 0)

// helper: parse top recommendations from AI text
function parseTopRecommendations(text: string) {
  aiTop.value = [];
  if (!text) return;
  // normalize line endings
  const lines = String(text).split(/\r?\n/).map(l => l.trim()).filter(Boolean);

  // collect lines that look like numbered recommendations (e.g. "1. 店名 — reason")
  const recLines: string[] = [];
  for (const line of lines) {
    const m = line.match(/^\s*(?:\d+)\.\s*(.+)$/);
    if (m) recLines.push(m[1]);
  }

  // fallback: try to find a block starting with '优先推荐' and grab next lines
  if (recLines.length === 0) {
    const idx = lines.findIndex(l => /优先推荐|推荐列表|推荐结果/i.test(l));
    if (idx >= 0) {
      for (let i = idx + 1; i < Math.min(lines.length, idx + 8); i++) {
        const l = lines[i];
        if (/^\s*\d+\./.test(l)) recLines.push(l.replace(/^\s*\d+\.\s*/, ''));
      }
    }
  }

  // take up to 3 recommendations and split into name + reason heuristically
  for (let i = 0; i < Math.min(3, recLines.length); i++) {
    const raw = recLines[i];
    // try separators: em-dash, en-dash, hyphen, colon, Chinese colon
    const parts = raw.split(/\s*[—–\-:：]\s*/);
    const name = parts[0] ? parts[0].trim() : raw.trim();
    const reason = parts.slice(1).join(' ').trim() || '';

    // try to match the full record from records by name (best-effort)
    let matchedRecord: any = null;
    for (const r of records.value) {
      if (!r || !r.name) continue;
      const rn = String(r.name).trim();
      // case-insensitive comparison and inclusion checks
      if (rn === name || rn.includes(name) || name.includes(rn) || rn.toLowerCase() === name.toLowerCase()) {
        matchedRecord = r;
        break;
      }
    }

    if (matchedRecord) {
      aiTop.value.push({ name, reason, businessId: matchedRecord.businessId ?? null, record: matchedRecord });
    } else {
      aiTop.value.push({ name, reason, businessId: null, record: null });
    }
  }
}

// expose a thin wrapper so the page triggers the hook function using current route.query.info
async function fetchAIRecommendationsWrapper() {
  const query = (route.query.info as string) || '';
  // prepare first up to 10 records for AI context
  const recordsForAI = records.value.slice(0, 10).map((r: any, idx: number) => {
    const name = r?.name ?? '未知商家';
    const stars = (r?.stars !== undefined && r?.stars !== null) ? r.stars : '无评分';
    const categories = r?.categories ?? '未分类';
    return `${idx + 1}. ${name} — 评分: ${stars} — 分类: ${categories}`;
  }).join('\n');

  const APIkey = 'sk-jsppmnzualuadnsjwnneaqsupkcpjfoungipzaahqygoqhqw'
  try {
    // set loading
    if (aiLoading && typeof aiLoading === 'object') aiLoading.value = true;
    const url = 'https://api.siliconflow.cn/v1/chat/completions';
    const body = {
      model: 'deepseek-ai/DeepSeek-V2.5',
      messages: [
        { role: 'user', content: `我当前的搜索关键词："${query}"。下面是搜索结果中前 ${Math.min(5, records.length)} 条商家数据（格式：序号. 名称 — 评分 — 分类）：\n${recordsForAI}\n\n请基于上述商家列表，从“店名、评分、分类”三方面综合分析，给出对用户的智能推荐（推荐相似商家或体验），并对每条推荐写出简短的推荐理由。最后给出 3 条优先推荐（按重要性排序）。` }
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
      // parse top recommendations from the final AI text
      parseTopRecommendations(finalContent);
      try { saveCacheFor(query); } catch (e) { /* ignore */ }
    } else {
      // fallback: non-streaming response
      const data = await response.json();
      const content = data?.choices?.[0]?.message?.content ?? data?.result ?? JSON.stringify(data);
      const md = toMarkdown(Array.isArray(content) ? content.join('\n\n') : String(content));
      if (aiRecommendations && typeof aiRecommendations === 'object') aiRecommendations.value = [md];
      parseTopRecommendations(md);
      try { saveCacheFor(query); } catch (e) { /* ignore */ }
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

.ai-top { margin-top: 12px; padding: 12px; background: #fff; border: 1px solid #eef2f6; border-radius: 8px }
.ai-top-title { font-weight: 700; margin-bottom: 8px }
.ai-top-list { display: flex; gap: 12px; flex-wrap: wrap }
.top-card { background: #f9fbfd; padding: 10px; border-radius: 8px; min-width: 220px; box-shadow: 0 6px 12px rgba(10,20,30,0.04) }
.top-name { font-weight: 700; margin-bottom: 6px }
.top-reason { color: #6b6f73; font-size: 13px; min-height: 36px }
.top-action { margin-top: 8px }

/* thumbnail for aiTop */
.top-card { display: flex; gap: 12px; align-items: flex-start; }
.top-thumb img { width: 72px; height: 72px; object-fit: cover; border-radius: 6px; }
.thumb-placeholder { width: 72px; height: 72px; background: #eef2f6; border-radius: 6px; }
.top-body { flex: 1; display: flex; flex-direction: column; }
</style>