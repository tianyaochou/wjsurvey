<script setup lang="ts">
import SurveyComponent from '../components/SurveyComponent.vue'
import { ref } from 'vue'

const submited = ref(false)
const submitting = ref(false)

function handleSubmitting(answers) {
  submitting.value = true
}

function handleSubmitted() {
  submitting.value = false
  submited.value = true
}
</script>

<template>
  <SurveyComponent
    v-if="!(submitting || submited)"
    @submitting="handleSubmitting"
    @submitted="handleSubmitted"
  />
  <p v-if="submitting">Submitting survey...</p>
  <p v-if="submited">Thank you for your participation!</p>
</template>
