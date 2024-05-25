<script setup lang="ts">
import { ref } from 'vue'
import QuestionView from './QuestionView.vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  s: Object
})
const emit = defineEmits(['submitting', 'submitted', 'notSubmitted'])
const route = useRoute()

const survey = JSON.parse(
  '{"name":"test","content":[{"type":"Text","content":"Example"},{"type":"Question","key":"name","question":"What is your name","required":true,"input":{"type":"TextInput","prompt":""}},{"type":"Question","key":"age","question":"How old are you?","required":false,"input":{"type":"SingleChoice","choices":[{"encoding":"1","description":"< 18"},{"encoding":"2","description":">= 18"}]}},{"type":"Question","key":"fruit","question":"What fruit do you like?","required":false,"input":{"type":"MultiChoice","choices":[{"encoding":"apple","description":"Apple"},{"encoding":"banana","description":"Banana"}]}}],"id":1}'
)

const answers = ref(new Map<string, string | Array<string> | null>())

const required = survey.content
  .filter((x) => x.type === 'Question')
  .filter((x) => x.required)
  .map((x) => x.key)

function setValue(key: string, value: any) {
  answers.value.set(key, value)
}

function checkRequired() {
  let ok = true
  required.forEach((x) => {
    if (!answers.value.get(x)) {
      ok = false
    }
  })
  return ok
}

async function handleSubmit() {
  if (!checkRequired()) {
    // TODO: highlight required
    return
  }
  emit('submitting')
  const response = await fetch(route.path, {
    method: 'post',
    body: JSON.stringify(answers)
  })
  if (!response.ok) {
    emit('notSubmitted')
  } else {
    emit('submitted')
  }
}
</script>

<template>
  <h1>{{ survey.name }}</h1>
  <br />
  <form @submit.prevent="handleSubmit">
    <div v-for="[idx, content] in survey.content.entries()" v-bind:key="idx">
      <p v-if="content.type == 'Text'">{{ content.content }}</p>
      <QuestionView
        v-if="content.type == 'Question'"
        :q="content"
        @set-value="(val) => setValue(content.key, val)"
      />
    </div>
    <button type="submit">Submit</button>
  </form>
</template>
