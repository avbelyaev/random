<template>
  <div class="feed">
    <p>What's new</p>
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
        {message: 'Whatsapp will finally be deleted'}
      ]
    }
  }
}
</script>

<style lang="scss">
.feed {
  background-color: yellow;
  width: 250px;
  border: 2px solid black;
}
</style>
