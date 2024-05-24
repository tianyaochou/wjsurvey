<script setup lang="ts">
import { ref } from 'vue'
const props = defineProps({
  qKey: String,
  i: Object
})
const emit = defineEmits(['setValue'])

const val = ref(new Set())

function updateValue(c, e) {
  if (!e.target.checked) {
    val.value = val.value.delete(c.encoding)
  } else {
    val.value = val.value.add(c.encoding)
  }
  emit('setValue', val)
}
</script>

<template>
  <div v-for="[idx, c] in i.choices.entries()" :key="idx">
    <input type="checkbox" :name="qKey + c.encoding" @change="(e) => updateValue(c, e)" /><label
      :for="qKey + c.encoding"
      >{{ c.description }}</label
    >
  </div>
</template>
