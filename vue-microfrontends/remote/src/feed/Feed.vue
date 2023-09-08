<template>
  <div class="feed-container">
    <p>News feed for: [ {{this.$store.getters.fullName}} ]</p>
    <p>Score: {{this.$store.state.score}}</p>
    <div class="feed">
      <li v-for="post in posts">
        {{ post.message }}
      </li>
    </div>
    <br/>
    <button @click="inc">
      Inc from remote ^
    </button>
  </div>
</template>

<script>
export default {
  name: 'Feed',

  computed: {
    posts() {
      return [
        {message: 'Telegram is coming soon'},
        {message: 'Instagram to be repaired'},
        {message: 'Whatsapp to be removed'},
      ]
    }
  },

  methods: {
    inc() {
      this.$store.state.score += 21;
    }
  }
}
</script>

<style scoped>
.feed-container {
  background-color: yellow;
  border: 2px solid black;
  width: 270px;
}
.feed {
  width: 250px;
  border: 1px solid black;
}
</style>
