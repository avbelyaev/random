<template>
  <main>
    <div class="home">
      <p>News feed</p>
      <Feed />
    </div>
  </main>
</template>

<script>
import Feed from "./Feed";

export default {
  name: 'HomeView',
  components: {
    Feed,
  },

  data() {
    return {
      igScope: {},
    }
  },

}
</script>

<style scoped>
.home {
  background-color: deepskyblue;
  height: 200px;
}
</style>