<template>
  <div class="feed">
    <li v-for="post in posts">
      {{ post.message }}
    </li>
  </div>
</template>

<script>
export default {
  name: 'Feed',

  data() {
    return {
      posts: [
        {message: 'Instagram is coming soon'},
        {message: 'Whatsapp to be removed'},
      ]
    }
  }
}
</script>

<style scoped>
.feed {
  background-color: yellow;
  width: 250px;
  border: 2px solid black;
}
</style>
