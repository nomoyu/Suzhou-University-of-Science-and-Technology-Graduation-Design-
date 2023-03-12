<template>
  <div>
    <div v-if="loading">Loading...</div>
    <div v-else>
      <div v-if="type === 'pdf'">
        <div class="pdf-container">
          <embed :src="url" type="application/pdf" width="100%" height="100%">
        </div>
      </div>
      <div v-else-if="type === 'word'">
        <div class="word-container">
          <iframe :src="url" width="100%" height="100%"></iframe>
        </div>
      </div>
      <viewer :images="images" :visible.sync="viewerVisible" />
    </div>
  </div>
</template>

<script>
import Viewer from 'viewerjs';
import 'viewerjs/dist/viewer.css';
import VueViewer from 'vue-viewer';


export default {
  name: 'DocumentViewer',
  components: {
    Viewer
  },
  props: {
    url: {
      type: String,
      required: true
    },
    type: {
      type: String,
      required: true,
      validator: value => ['pdf', 'word'].includes(value)
    }
  },
  data() {
    return {
      loading: true,
      viewerVisible: false,
      images: []
    };
  },
  mounted() {
    if (this.type === 'pdf') {
      this.images.push({
        src: this.url,
        alt: ''
      });
    } else if (this.type === 'word') {
      this.images.push({
        src: this.url.replace(/\/view$/, '/preview'),
        alt: ''
      });
    }
    this.loading = false;
  },
  methods: {
    showViewer() {
      this.viewerVisible = true;
    }
  }
};
</script>

<style scoped>
.pdf-container,
.word-container {
  height: 500px;
}
</style>
