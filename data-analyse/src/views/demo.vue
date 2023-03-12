<template>
  <div>
    <button @click="openWord()">打开 Word</button>
    <div id="word-container"></div>
  </div>
</template>

<script>
import * as Office from 'office-js';

export default {
  methods: {
    async openWord() {
      try {
        await Office.initialize();
        const doc = await Office.context.document.getSelectedDataAsync(Office.CoercionType.Text);
        doc.setSelectedDataAsync('Hello, Word!');
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>
