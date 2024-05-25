<script setup lang="ts">
import SurveyComponent from '../components/SurveyComponent.vue'
import { ref } from 'vue'

const submitted = ref(false)
const submitting = ref(false)

function handleSubmitting(answers) {
  submitting.value = true
}

function handleSubmitted() {
  submitting.value = false
  submitted.value = true
}

function handleFailure() {
  console.log("handle failure")
  submitting.value = false
  submitted.value = false
}
</script>

<template>
  <SurveyComponent
    v-if="!(submitting || submitted)"
    @notSubmitted="handleFailure"
    @submitting="handleSubmitting"
    @submitted="handleSubmitted"
  />
  <p v-if="submitting">Submitting survey...</p>
  <p v-if="submitted">Thank you for your participation!</p>
</template>
