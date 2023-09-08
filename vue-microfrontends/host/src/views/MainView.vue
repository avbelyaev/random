<template>
  <div class="main">
    <p>Main page</p>
    <p>Hi, {{ this.$store.getters.fullName }}. Your score is {{ cnt }}</p>
    <button @click="addScore">
      Add +1 score
    </button>
    <p>Host store: {{ this.$store.state }} !</p>
    <h4>Your feed from a remote app</h4>
    <Feed />
  </div>
</template>

<script>
import Feed from 'remoteApp/Feed';

export default {
  name: 'MainView',
  components: {
    Feed
  },
  computed: {
    cnt() {
      return this.$store.state.count
    }
  },
  methods: {
    addScore() {
      this.$store.state.count += 1
    }
  }
}
</script>
