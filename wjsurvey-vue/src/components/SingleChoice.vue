<script setup lang="ts">
const props = defineProps({
  qKey: String,
  i: Object
})

const emit = defineEmits(['setValue'])
</script>

<template>
  <div v-for="[idx, c] in i.choices.entries()" :key="idx">
    <input type="radio" :name="qKey" @change="$emit('setValue', c.encoding)" /><label :for="qKey">{{
      c.description
    }}</label>
  </div>
</template>
