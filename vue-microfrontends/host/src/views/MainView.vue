<template>
  <div class="main">
    <p>Hi, {{ this.$store.getters.fullName }}. Your score is {{ score }}</p>
    <button @click="addScore">
      Inc from host v
    </button>
  </div>
  <Feed />
</template>

<script>
import Feed from 'remoteApp/Feed';

export default {
  name: 'MainView',
  components: {
    Feed
  },
  computed: {
    score() {
      return this.$store.state.score
    }
  },
  methods: {
    addScore() {
      this.$store.state.score += 13
    }
  }
}
</script>
