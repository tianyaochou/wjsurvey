<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps({
  i: Object
})

const val = ref('')
</script>

<template>
  <label>{{ i.prompt }}</label
  ><input type="text" v-model="val" @change="$emit('setValue', val)" />
</template>
