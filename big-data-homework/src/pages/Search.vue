<template>
  <div class="search">
    <NewIndexView
      :show-background-description="false"
      style="background-image: none; height: 131px; min-height: auto"
    />
    <div class="display">
      <div class="ai-recommend">
        <div class="ai-header">
          <div class="ai-left">
            <span class="ai-title">AI为您推荐</span>
            <div class="ai-sub">基于您的搜索为您智能推荐相似商家或体验</div>
          </div>
          <div class="ai-action">
            <el-button
              size="small"
              type="primary"
              @click="fetchAIRecommendationsWrapper"
              :loading="aiLoading"
              >生成推荐</el-button
            >
          </div>
        </div>
        <div class="ai-body" v-if="aiRecommendations.length">
          <div
            class="ai-content"
            v-for="(item, idx) in aiRecommendations"
            :key="idx"
          >
            <div v-html="renderMarkdown(item)"></div>
          </div>
        </div>
        <div class="ai-top" v-if="aiTop.length">
          <div class="ai-top-title">优先推荐商家</div>
          <div class="ai-top-list">
            <div class="top-card" v-for="(t, i) in aiTop" :key="i">
              <div class="top-thumb">
                <img
                  v-if="t.record && t.record.image"
                  :src="filePath(t.record.image)"
                  alt="商家图"
                />
                <div v-else class="thumb-placeholder"></div>
              </div>
              <div class="top-body">
                <div class="top-name">
                  <span v-if="t.businessId">{{
                    (t.name || "").replace(/\*\*/g, "")
                  }}</span>
                  <span v-else>{{ (t.name || "").replace(/\*\*/g, "") }}</span>
                </div>
                <div class="top-reason">
                  {{ t.record ? t.record.categories || "" : "" }}
                </div>
                <div class="top-action">
                  <el-button
                    v-if="t.businessId"
                    type="primary"
                    @click="
                      () => {
                        router.push({
                          path: '/merchantDetail',
                          query: { id: t.businessId },
                        });
                      }
                    "
                    >查看详情</el-button
                  >
                  <el-button v-else type="text" disabled>查看详情</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="ai-empty" v-else>
          点击“生成推荐”根据当前搜索内容获取智能推荐
        </div>
      </div>

      <div class="title-section">
        <div class="title">相关商家</div>
        <div class="filters">
          <el-select
            v-model="selectedDistance"
            placeholder="选择距离"
            @change="handleDistanceChange"
            size="small"
            style="width: 200px; margin-left: 20px"
          >
            <el-option
              v-for="option in distanceOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </div>
      </div>
      <div class="mid">
        <div class="show-box" v-for="(record, index) in records" :key="index">
          <router-link
            class="img"
            :to="{ path: '/merchantDetail', query: { id: record.businessId } }"
          >
            <img v-if="record.image" :src="filePath(record.image)" />
            <div v-else class="avatar-placeholder">
              {{ getInitials(record.name) }}
            </div>
          </router-link>
          <div class="info-box">
            <div class="info1">
              <span>{{ record.name }}</span>
            </div>
            <div class="info2">
              <el-rate v-model="record.stars" size="large" disabled></el-rate>
              <div class="rate-box">
                <div style="margin-right: 1%">{{ record.stars }}</div>
                <span>({{ record.reviewCount }} reviews)</span>
              </div>
            </div>
            <div class="info3">
              <span>{{ record.categories }}</span>
            </div>
            <div class="info4" v-if="record.distance !== undefined">
              <span>距离: {{ formatDistance(record.distance) }}</span>
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
        class="page"
        background
        layout="prev,pager, next"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import { useSearch } from "@/hooks/UseSearch";
import { useAuthStore } from "@/stores/UseAuthStore";
import { useRoute, useRouter } from "vue-router";
import { computed, onMounted, ref, toRefs, watch } from "vue";
import { UseSearchStore } from "@/stores/UseSearchStore";
import { marked } from "marked";

const filePath = (file: any) => {
  if (file == null) {
    console.log("null");
    return "";
  }
  // 如果图片链接已经是完整的URL，直接使用；否则拼接静态服务器地址
  const fullPath = file.includes("http")
    ? file
    : `http://localhost:3000/images/${file}.jpg`;
  return fullPath;
};
let searchStore = UseSearchStore();
let route = useRoute();
const router = useRouter();
// get fetchAIRecommendations and ai state from hook
let { search, getResult, aiLoading, aiRecommendations } = useSearch();
// parsed top recommendations from AI (array of {name, reason, businessId?, record?})
import { ref as _ref } from "vue";
const aiTop = _ref([]);

// --- simple localStorage cache for AI replies (persist across refresh/navigation)
const CACHE_PREFIX = "ai_recommend_v1_";
function cacheKeyFor(q: string) {
  const key = String(q || "").trim();
  return CACHE_PREFIX + (key ? encodeURIComponent(key) : "__global");
}
function saveCacheFor(q: string) {
  try {
    const payload = {
      aiRecommendations: Array.isArray(aiRecommendations?.value)
        ? aiRecommendations.value
        : [],
      aiTop: Array.isArray(aiTop?.value) ? aiTop.value : [],
      ts: Date.now(),
    };
    localStorage.setItem(cacheKeyFor(q), JSON.stringify(payload));
  } catch (e) {
    console.warn("saveCacheFor failed", e);
  }
}
function loadCacheFor(q: string) {
  try {
    const raw = localStorage.getItem(cacheKeyFor(q));
    if (!raw) return false;
    const obj = JSON.parse(raw);
    if (obj?.aiRecommendations) {
      if (aiRecommendations && typeof aiRecommendations === "object")
        aiRecommendations.value = obj.aiRecommendations;
    }
    if (obj?.aiTop) aiTop.value = obj.aiTop;
    return true;
  } catch (e) {
    console.warn("loadCacheFor failed", e);
    return false;
  }
}
function clearCacheFor(q: string) {
  try {
    localStorage.removeItem(cacheKeyFor(q));
  } catch (e) {
    /* ignore */
  }
}

function handleCurrentChange(val) {
  // 从路由参数中获取经纬度信息
  const lat = route.query.lat ? Number(route.query.lat) : null;
  const lng = route.query.lng ? Number(route.query.lng) : null;
  const distance = selectedDistance.value;

  getResult(val, route.query.info, lat, lng, distance);
  console.log("当前页码：" + JSON.stringify(result.value));
}

// 添加距离变化处理方法
function handleDistanceChange(value) {
  // 重新搜索，使用新的距离筛选条件
  const lat = route.query.lat ? Number(route.query.lat) : null;
  const lng = route.query.lng ? Number(route.query.lng) : null;

  getResult(1, route.query.info, lat, lng, value);
}

// load cached AI reply for current query on mount; clear when query changes
onMounted(() => {
  const q = String(route.query.info || "");
  loadCacheFor(q);
});

watch(
  () => route.query.info,
  (nv, ov) => {
    const q = String(nv || "");
    // If there's cached data for the new query, load it; otherwise clear displayed AI reply
    if (!loadCacheFor(q)) {
      if (aiRecommendations && typeof aiRecommendations === "object")
        aiRecommendations.value = [];
      aiTop.value = [];
    }
  }
);

let result = computed(() => searchStore.result);
// safe derived values for template
import { computed as _computed } from "vue";
const records = _computed(() =>
  result.value && Array.isArray(result.value.records)
    ? result.value.records
    : []
);
const total = _computed(() =>
  result.value && typeof result.value.total === "number"
    ? result.value.total
    : 0
);

// helper: parse top recommendations from AI text
function parseTopRecommendations(text: string) {
  aiTop.value = [];
  if (!text) return;
  // normalize line endings
  const lines = String(text)
    .split(/\r?\n/)
    .map((l) => l.trim())
    .filter(Boolean);

  // collect lines that look like numbered recommendations (e.g. "1. 店名 — reason")
  const recLines: string[] = [];
  for (const line of lines) {
    const m = line.match(/^\s*(?:\d+)\.\s*(.+)$/);
    if (m) recLines.push(m[1]);
  }

  // fallback: try to find a block starting with '优先推荐' and grab next lines
  if (recLines.length === 0) {
    const idx = lines.findIndex((l) => /优先推荐|推荐列表|推荐结果/i.test(l));
    if (idx >= 0) {
      for (let i = idx + 1; i < Math.min(lines.length, idx + 8); i++) {
        const l = lines[i];
        if (/^\s*\d+\./.test(l)) recLines.push(l.replace(/^\s*\d+\.\s*/, ""));
      }
    }
  }

  // take up to 3 recommendations and split into name + reason heuristically
  for (let i = 0; i < Math.min(3, recLines.length); i++) {
    const raw = recLines[i];
    // try separators: em-dash, en-dash, hyphen, colon, Chinese colon
    const parts = raw.split(/\s*[—–\-:：]\s*/);
    const name = parts[0] ? parts[0].trim() : raw.trim();
    const reason = parts.slice(1).join(" ").trim() || "";

    // try to match the full record from records by name (best-effort)
    let matchedRecord: any = null;
    for (const r of records.value) {
      if (!r || !r.name) continue;
      const rn = String(r.name).trim();
      // case-insensitive comparison and inclusion checks
      if (
        rn === name ||
        rn.includes(name) ||
        name.includes(rn) ||
        rn.toLowerCase() === name.toLowerCase()
      ) {
        matchedRecord = r;
        break;
      }
    }

    if (matchedRecord) {
      aiTop.value.push({
        name,
        reason,
        businessId: matchedRecord.businessId ?? null,
        record: matchedRecord,
      });
    } else {
      aiTop.value.push({ name, reason, businessId: null, record: null });
    }
  }
}

// expose a thin wrapper so the page triggers the hook function using current route.query.info
async function fetchAIRecommendationsWrapper() {
  const query = (route.query.info as string) || "";
  // prepare first up to 10 records for AI context
  const recordsForAI = records.value.slice(0, 12).map((r: any, idx: number) => {
    const name = r?.name ?? '未知商家';
    const stars = (r?.stars !== undefined && r?.stars !== null) ? r.stars : '无评分';
    const categories = r?.categories ?? '未分类';
    return `${idx + 1}. ${name} — 评分: ${stars} — 分类: ${categories}`;
  }).join('\n');

  // 根据记录数量调整推荐策略
  if (records.value.length === 0) {
    // 当没有记录时，显示提示信息
    if (aiRecommendations && typeof aiRecommendations === 'object') {
      aiRecommendations.value = ['当前没有找到相关商家，请尝试更换搜索关键词。'];
    }
    aiTop.value = [];
    return;
  } else if (records.value.length <= 3) {
    // 当记录数小于等于3时，直接推荐当前所有商家
    if (aiRecommendations && typeof aiRecommendations === 'object') {
      aiRecommendations.value = ['根据您的搜索，我们找到了以下相关商家，为您直接推荐：'];
    }
    
    // 清空之前的推荐
    aiTop.value = [];
    
    // 将当前所有商家作为推荐项
    for (let i = 0; i < records.value.length; i++) {
      const record = records.value[i];
      aiTop.value.push({
        name: record.name,
        reason: `匹配您的搜索关键词"${query}"`,
        businessId: record.businessId ?? null,
        record: record
      });
    }
    return;
  }

  const APIkey = 'sk-jsppmnzualuadnsjwnneaqsupkcpjfoungipzaahqygoqhqw'
  try {
    // set loading
    if (aiLoading && typeof aiLoading === "object") aiLoading.value = true;
    const url = "https://api.siliconflow.cn/v1/chat/completions";
    const body = {
      model: "deepseek-ai/DeepSeek-V2.5",
      messages: [
        {
          role: "user",
          content: `我当前的搜索关键词："${query}"。下面是搜索结果中前 ${Math.min(
            5,
            records.length
          )} 条商家数据（格式：序号. 名称 — 评分 — 分类）：\n${recordsForAI}\n\n请基于上述商家列表，从“店名、评分、分类”三方面综合分析，给出对用户的智能推荐（推荐相似商家或体验），并对每条推荐写出简短的推荐理由。最后给出 3 条优先推荐（按重要性排序）。`,
        },
      ],
      // request streaming if the API supports it (service-dependent)
      stream: true,
    };

    const headers: Record<string, string> = {
      "Content-Type": "application/json",
    };
    headers.Authorization = `Bearer ${APIkey}`;

    const response = await fetch(url, {
      method: "POST",
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
      let accumulatedContent = "";

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
          const payload = line.startsWith("data:")
            ? line.replace(/^data:\s*/i, "")
            : line;
          if (payload === "[DONE]") continue;

          try {
            const parsed = JSON.parse(payload);
            // prefer incremental delta content, fallback to message content or result
            const delta = parsed?.choices?.[0]?.delta;
            const contentPart =
              delta?.content ??
              parsed?.choices?.[0]?.message?.content ??
              parsed?.result;
            if (contentPart != null) {
              accumulatedContent += String(contentPart);
              if (aiRecommendations && typeof aiRecommendations === "object") {
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
        finalContent =
          maybe?.choices?.[0]?.message?.content ??
          maybe?.result ??
          accumulatedContent;
      } catch (e) {
        // keep accumulatedContent
      }

      if (aiRecommendations && typeof aiRecommendations === "object")
        aiRecommendations.value = [toMarkdown(finalContent)];
      // parse top recommendations from the final AI text
      parseTopRecommendations(finalContent);
      try {
        saveCacheFor(query);
      } catch (e) {
        /* ignore */
      }
    } else {
      // fallback: non-streaming response
      const data = await response.json();
      const content =
        data?.choices?.[0]?.message?.content ??
        data?.result ??
        JSON.stringify(data);
      const md = toMarkdown(
        Array.isArray(content) ? content.join("\n\n") : String(content)
      );
      if (aiRecommendations && typeof aiRecommendations === "object")
        aiRecommendations.value = [md];
      parseTopRecommendations(md);
      try {
        saveCacheFor(query);
      } catch (e) {
        /* ignore */
      }
    }
  } catch (err) {
    console.error("AI recommend error:", err);
    if (aiRecommendations && typeof aiRecommendations === "object")
      aiRecommendations.value = [];
  } finally {
    if (aiLoading && typeof aiLoading === "object") aiLoading.value = false;
  }
}

// simple helper to convert plain text into a Markdown block — adjust as needed
function toMarkdown(text: string) {
  if (!text) return "";
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
  const asParagraphs = trimmed
    .split(/\n{2,}/)
    .map((p) => p.trim())
    .filter(Boolean)
    .join("\n\n");
  return `### 推荐结果\n\n${asParagraphs}`;
}

function renderMarkdown(markdownText: string) {
  if (!markdownText) return "";
  return marked(markdownText);
}

// 添加距离格式化函数
function formatDistance(distance: number): string {
  if (distance < 1) {
    // 小于1公里显示米
    return Math.round(distance * 1000) + "m";
  } else {
    // 大于等于1公里显示公里，保留一位小数
    return distance.toFixed(1) + "km";
  }
}

// 添加获取首字母的方法
function getInitials(name: string): string {
  if (!name) return "?";
  return name.charAt(0).toUpperCase();
}

// 添加距离筛选相关数据
const distanceOptions = [
  { label: "不限距离", value: 0 },
  { label: "1公里以内", value: 1 },
  { label: "3公里以内", value: 3 },
  { label: "5公里以内", value: 5 },
  { label: "10公里以内", value: 10 },
  { label: "20公里以内", value: 20 },
];

const selectedDistance = ref(10); // 默认10公里
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
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  position: relative;
  padding-bottom: 8px;
  margin-bottom: 0; /* 移除原有的 margin-bottom */
}

.title::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #3b82f6, #60a5fa);
  border-radius: 2px;
}

.mid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.show-box {
  display: flex;
  flex-direction: row;
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.show-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border-color: #cbd5e1;
}

.img {
  width: 160px;
  height: 160px;
  cursor: pointer;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}

.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

/* 添加头像占位符样式 */
.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 48px;
  font-weight: bold;
  border-radius: 10px;
}

.img:hover img {
  transform: scale(1.05);
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: calc(100% - 180px);
  height: 180px;
  margin-left: 20px;
}

.info1 {
  color: #1e293b;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info2 {
  display: flex;
  flex-direction: column;
  margin-bottom: 12px;
}

.info2 span {
  color: #6e7072;
}

.rate-box {
  display: flex;
  align-items: center;
  margin-top: 8px;
}

.rate-box div:first-child {
  color: #f59e0b;
  font-weight: 600;
  margin-right: 8px;
}

.rate-box span {
  color: #64748b;
  font-size: 14px;
}

.info3 {
  margin-top: auto;
}

.info3 span {
  color: #4b5563;
  font-size: 14px;
  padding: 4px 12px;
  background-color: #f1f5f9;
  border-radius: 20px;
  margin-right: 8px;
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 180px; /* 限制最大宽度 */
}
:deep(.info2 svg) {
  vertical-align: middle;
}

.info4 {
  margin-top: 8px;
}

.info4 span {
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
}

/* AI recommend styles */
.ai-recommend {
  margin-bottom: 24px;
  padding: 20px;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.ai-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #e2e8f0;
}

.ai-left {
  display: flex;
  flex-direction: column;
}

.ai-title {
  font-weight: 700;
  font-size: 20px;
  color: #1e293b;
  margin-bottom: 4px;
}

.ai-sub {
  color: #64748b;
  font-size: 14px;
}

.ai-action .el-button {
  background-color: #3b82f6;
  border-color: #3b82f6;
  transition: all 0.3s ease;
}

.ai-action .el-button:hover {
  background-color: #2563eb;
  border-color: #2563eb;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(59, 130, 246, 0.2);
}

.ai-body {
  margin-top: 16px;
  padding: 16px;
  background-color: #f1f5f9;
  border-radius: 8px;
  border-left: 4px solid #3b82f6;
}

.ai-content {
  line-height: 1.6;
  color: #334155;
}

.ai-empty {
  text-align: center;
  padding: 32px;
  color: #94a3b8;
  font-size: 14px;
  background-color: #f8fafc;
  border-radius: 8px;
  border: 1px dashed #cbd5e1;
}

.page {
  margin-left: 40%;
}

.ai-top {
  margin-top: 20px;
  padding: 20px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.ai-top-title {
  font-weight: 700;
  font-size: 18px;
  color: #1e293b;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e2e8f0;
}

.ai-top-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.top-card {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  background: #f8fafc;
  padding: 16px;
  border-radius: 10px;
  transition: all 0.3s ease;
  border: 1px solid #e2e8f0;
}

.top-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  border-color: #cbd5e1;
}

.top-thumb img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.thumb-placeholder {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.thumb-placeholder::after {
  content: "📷";
  font-size: 24px;
}

.top-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.top-name {
  font-weight: 700;
  font-size: 16px;
  color: #1e293b;
}

.top-reason {
  color: #64748b;
  font-size: 13px;
  line-height: 1.4;
  min-height: 36px;
}

.top-action .el-button {
  margin-top: 8px;
  padding: 6px 12px;
  font-size: 12px;
}

/* thumbnail for aiTop */
.top-card {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}
.top-thumb img {
  width: 72px;
  height: 72px;
  object-fit: cover;
  border-radius: 6px;
}
.thumb-placeholder {
  width: 72px;
  height: 72px;
  background: #eef2f6;
  border-radius: 6px;
}
.top-body {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.filters {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 添加选择框样式 */
.el-select {
  width: 200px !important;
  margin-left: 20px;
}

.el-select .el-input__inner {
  border-radius: 6px;
  border-color: #dcdfe6;
}

</style>
