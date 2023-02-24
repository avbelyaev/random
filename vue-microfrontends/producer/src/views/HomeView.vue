<template>
  <main>
    <div class="home">
      <p>Welcome Home</p>
      <Feed />
    </div>
  </main>
</template>

<script>
import Feed from "../components/Feed";
export default {
  name: 'HomeView',
  components: {Feed}
}
</script>

<style lang="scss">
.home {
  background-color: deepskyblue;
  height: 200px;
}
</style>