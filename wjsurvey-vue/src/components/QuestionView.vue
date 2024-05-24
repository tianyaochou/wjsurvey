<script setup lang="ts">
import TextInput from './TextInput.vue'
import SingleChoice from './SingleChoice.vue'
import MultiChoice from './MultiChoice.vue'

const props = defineProps({
  q: Object
})

const emit = defineEmits(['setValue'])
const input = props.q.input

function setValue(val) {
  emit('setValue', val)
}
</script>

<template>
  <h3>{{ q.question }}<span v-if="q.required" style="color: red">*</span></h3>
  <TextInput
    v-if="input.type == 'TextInput' || input.type == 'LongTextInput'"
    :i="input"
    :qKey="q.key"
    @set-value="setValue"
  />
  <SingleChoice
    v-if="input.type == 'SingleChoice'"
    :i="input"
    :qKey="q.key"
    @set-value="setValue"
  />
  <MultiChoice v-if="input.type == 'MultiChoice'" :i="input" :qKey="q.key" @set-value="setValue" />
</template>
